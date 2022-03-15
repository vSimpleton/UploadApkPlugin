package com.imiyar.upload

class UploadApkExtension {

    // -------------- 加固相关的扩展 --------------
    String reinforceUserName // 360加固的用户名
    String reinforcePassword // 360加固的密码
    String reinforceFilePath // 360加固jar包的路径
    String outputDirectory    // 加固后输出的apk目录
    boolean isOpenReinforce = true // 是否需要加固，默认为true

    // ------------ 上传蒲公英相关的扩展 ------------
    String apiKey  // API Key
    String appName // 应用名称

    // ----------- 发送钉钉消息相关的扩展 -----------
    String webHook // 钉钉机器人WebHook地址
    String secret  // 钉钉机器人secret

}