# ppt2video

## ppt转换为video
- 功能说明
  - 1、当前TTS服务只实现了百度服务 
  - 2、TTS转换ppt备注的内容为语音 
  - 3、将PPT和语音合成视频
  - 4、ppt为后端；pptweb为前端
  - 5、ppt转图片采用两个方案，spire方案和poi方案

## 前端
- 前端可独立部署，也可和后台整合部署 
- 使用方法： 
  - A、本地调试： 
     - 1）npm run dev 运行 
     - 2）浏览器输入http://localhost:8001/ 
  - B、 正式环境编译
      - npm run build

## 后端
- 使用方法： 
  - 修改 application.yml 百度tts相关参数，可通过 https://ai.baidu.com/tech/speech/tts_online 注册获取 
    - baiduSpeech: 
     appId: 
     apiKey: 
     secretKey:

## 参考操作界面
![image](https://user-images.githubusercontent.com/20513651/145004731-d5694ddd-0e2e-4e67-9be6-a7f64279d3ea.png)
