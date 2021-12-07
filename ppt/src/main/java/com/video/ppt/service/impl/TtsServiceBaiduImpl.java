package com.video.ppt.service.impl;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.video.ppt.common.factory.SpeechFactory;
import com.video.ppt.form.SpeechForm;
import com.video.ppt.service.TtsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service("ttsService")
public class TtsServiceBaiduImpl implements TtsService {

    @Value("${aipSpeech.connectionTimeout}")
    private int CONNECTION_TIMEOUT;
    @Value("${aipSpeech.socketTimeout}")
    private int SOCKET_TIMEOUT;
    @Value("${file.filePath}")
    public String filePath;

    @Override
    public String ttsChange(String buff, SpeechForm speechForm, int num) {
        //设置APPID/AK/SK

        String fileName = filePath + num + ".mp3";

        // 初始化一个AipSpeech
        AipSpeech client = SpeechFactory.getBaiduInstance();

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(CONNECTION_TIMEOUT);
        client.setSocketTimeoutInMillis(SOCKET_TIMEOUT);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 设置可选参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", speechForm.getSpeed());
        options.put("pit", speechForm.getPitch());
        options.put("vol", speechForm.getVolume());
        options.put("per", speechForm.getVoiceType());

        // 调用接口
        TtsResponse res = client.synthesis(buff, "zh", 1, options);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }

        return fileName;

    }
}
