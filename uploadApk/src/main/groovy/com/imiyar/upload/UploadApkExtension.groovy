package com.imiyar.upload

import org.gradle.api.Project

class UploadApkExtension {

    // -------------- 加固相关的扩展 --------------
    String reinforceUserName // 360加固的用户名
    String reinforcePassword // 360加固的密码
    String reinforceFilePath // 360加固jar包的路径
    String inputFilePath     // 需要加固的apk路径
    String outputFilePath    // 加固后输出的apk路径
    boolean isOpenReinforce = true // 是否需要加固，默认为true

    static UploadApkExtension getConfig(Project project) {
        UploadApkExtension config = project.getExtensions().findByType(UploadApkExtension.class)
        if (config == null) {
            config = new UploadApkExtension()
        }
        return config
    }

}