package com.video.ppt.form;

import lombok.Data;

@Data
public class SpeechForm {
    private static final long serialVersionUID = 1L;

    /* 合成语音渠道类型 */
    private String chnlType;    // 1：百度
    /* 语速 */
    private String speed;
    /* 音调 */
    private String pitch;
    /* 音量 */
    private String volume;
    /* 语音类型 */
    private String voiceType;
    /* 文件名 */
    private String fileName;
}
