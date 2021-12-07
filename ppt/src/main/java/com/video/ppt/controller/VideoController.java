package com.video.ppt.controller;

import com.video.ppt.common.utils.R;
import com.video.ppt.form.SpeechForm;
import com.video.ppt.service.FileDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private FileDealService fileDealService;


    /**
     * 合成视频
     */
    @PostMapping("/deal")
    public R save(@RequestBody SpeechForm speechForm){
        String fileName = fileDealService.getVideo(speechForm);

        return R.ok().put("orgdata", fileName);
    }

    @PostMapping("/upFile")
    public R upFile(@RequestParam("file") MultipartFile file){

        String fileName = fileDealService.upload(file);

        return R.ok().put("orgdata", fileName);
    }

    @GetMapping("/downFile")
    public R downFile(@RequestParam("fileName") String fileName, HttpServletResponse res){

        fileDealService.download(fileName, res);

        return R.ok();
    }
}
