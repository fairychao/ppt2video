package com.video.ppt.common.factory;

import com.baidu.aip.speech.AipSpeech;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpeechFactory implements InitializingBean {
    @Value("${baiduSpeech.appId}")
    private String BAIDU_APPID;
    @Value("${baiduSpeech.apiKey}")
    private String BAIDU_APIKEY;
    @Value("${baiduSpeech.secretKey}")
    private String BAIDU_SECRETKEY;

    private static volatile AipSpeech baiduInstance = null;

    @Override
    public void afterPropertiesSet() {
        try {
            if (baiduInstance == null) {
                baiduInstance = new AipSpeech(BAIDU_APPID, BAIDU_APIKEY, BAIDU_SECRETKEY);
                System.out.println(" init baiduInstance");
            }
        } catch (Exception e) {
        }
    }

    public static AipSpeech getBaiduInstance() {
        return baiduInstance;
    }
}
