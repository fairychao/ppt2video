package com.video.ppt.service.impl;

import com.video.ppt.common.exception.RRException;
import com.video.ppt.form.SpeechForm;
import com.video.ppt.service.FileDealService;
import com.video.ppt.service.TtsService;
import com.spire.presentation.*;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("fileDealService")
public class FileDealServiceImpl implements FileDealService {

    @Value("${file.filePath}")
    public String filePath;

    @Value("${file.separate}")
    public String separate;

    @Autowired
    private TtsService ttsService;

    /**
     * 合成视频
     */
    @Override
    public String getVideo(SpeechForm speechForm) {
        String inFileName = speechForm.getFileName();

        String saveFileName = inFileName.substring(inFileName.lastIndexOf(separate)+1);
        saveFileName= saveFileName.substring(0, saveFileName.indexOf("."));
        String mp4Path = filePath + saveFileName + ".mp4";
        try {
            //加载PowerPoint文档
            Presentation ppt = new Presentation();
            ppt.loadFromFile(speechForm.getFileName(), FileFormat.PPTX_2013);

            //获取幻灯片中的备注内容
            Map<Integer, String> audioMap = new HashMap<Integer, String>();
            Map<Integer, String> imgMap = new HashMap<Integer, String>();
            List<String> buff = new ArrayList<>();

            //获取第一张幻灯片
            for (int i = 0; i < ppt.getSlides().size(); i++) {
                ISlide slide = ppt.getSlides().get(i);
                //将幻灯片保存为BufferedImage对象
                BufferedImage image = slide.saveAsImage(2160, 1215);
                //将BufferedImage保存为PNG格式文件
                String imgName =  String.format("%sToImage-%d.png", filePath, i);
                ImageIO.write(image, "PNG", new File(imgName));

                String notes = slide.getNotesSlide().getNotesTextFrame().getText();
                if (StringUtils.isBlank(notes)) {
                    buff.add("NA");
                    imgMap.put(i, imgName);
                    audioMap.put(i, null);

                } else {
                    buff.add(notes);
                    String path = ttsService.ttsChange(notes, speechForm, i);
                    imgMap.put(i, imgName);
                    audioMap.put(i, path);
                }
            }
            mergeAudioAndVideo(mp4Path, imgMap, audioMap, 2160, 1215);
            ppt.dispose();
        } catch (Exception e) {
            throw new RRException("合成视频失败");
        }

        return mp4Path;

    }


    /**
     * 上传
     */
    @Override
    public String upload(MultipartFile file){
        String saveFilePath = filePath;

        String inFileName = file.getOriginalFilename();
        String saveFileName = inFileName.substring(inFileName.lastIndexOf(separate)+1);

        saveFilePath = saveFilePath + saveFileName;

        FileOutputStream out = null;
        try {
            byte[] bytes = file.getBytes();
            File newFile = new File(saveFilePath);
            if (!newFile.exists()) {
                newFile.getParentFile().mkdirs();
                newFile.createNewFile();
            }
            out = new FileOutputStream(newFile);
            out.write(bytes);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return saveFilePath;
    }

    /**
     * 下载
     */
    @Override
    public void download(String fileName, HttpServletResponse response) {
        File file = new File(fileName);

        //创建输入对象
        try (FileInputStream fis = new FileInputStream(file)) {
            // 设置相关格式
            response.setContentType("application/octet-stream; charset=UTF-8");
            //设置文件长度
            response.addHeader("Content-Length", String.valueOf(file.length()));
            //下载之后需要在请求头中放置文件名，该文件名按照ISO_8859_1编码。
            String downFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.name());
            // 设置下载后的文件名以及header
            response.addHeader("Content-disposition", "attachment;fileName=" + downFileName);
            // 创建输出对象
            try (OutputStream os = response.getOutputStream()) {
                // 常规操作
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = fis.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void mergeAudioAndVideo(String videoPath, Map<Integer, String> imgMap, Map<Integer, String> audioMap, int width, int height) throws Exception {

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoPath, width, height, 2);
        //设置视频编码层模式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        //设置视频为25帧每秒
        recorder.setFrameRate(25);
        //设置视频图像数据格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setFormat("mp4");
        recorder.setVideoBitrate(8000000);
        recorder.setAudioQuality(0);
        recorder.setAudioBitrate(1000000);

        try {
            recorder.start();
            Java2DFrameConverter converter = new Java2DFrameConverter();

            for (int k=0; k<imgMap.size(); k++) {
                //抓取音频帧
                FrameGrabber audioGrabber = new FFmpegFrameGrabber(audioMap.get(k));
                audioGrabber.start();
                long videoTime = audioGrabber.getLengthInTime() / 1000000;

                File file = new File(imgMap.get(k));
                BufferedImage image = ImageIO.read(file);

                //先录制一个音频同步的视频
                for (int i = 0; i < videoTime; i++) {
                    //一秒是25帧 所以要记录25次
                    for (int j = 0; j < 25; j++) {
                        recorder.record(converter.getFrame(image));
                    }
                }

                //然后录入音频
                Frame audioFrame = null;
                while ((audioFrame = audioGrabber.grabFrame()) != null) {
                    recorder.record(audioFrame);
                }

                audioGrabber.stop();
                audioGrabber.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最后一定要结束并释放资源

            recorder.stop();
            recorder.release();
        }

    }

}
