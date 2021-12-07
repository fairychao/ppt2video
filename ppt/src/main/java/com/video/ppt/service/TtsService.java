package com.video.ppt.service;

import com.video.ppt.form.SpeechForm;

import java.util.List;

public interface TtsService {
    /**
     * 转换
     */
    String ttsChange(String buff, SpeechForm speechForm, int num);
}
