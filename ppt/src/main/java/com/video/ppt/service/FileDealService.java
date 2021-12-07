package com.video.ppt.service;

import com.video.ppt.form.SpeechForm;
import com.spire.presentation.Presentation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileDealService {

    /**
     * 合成视频
     */
    String getVideo(SpeechForm speechForm);


    String upload(MultipartFile file);

    void download(String file, HttpServletResponse response);
}
