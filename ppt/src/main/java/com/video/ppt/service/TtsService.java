package com.video.ppt.service;

import com.video.ppt.form.SpeechForm;


public interface TtsService {
    /**
     * 转换
     */
    void ttsChange(String buff, SpeechForm speechForm, String fileName);
}
