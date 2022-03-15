package com.imiyar.upload.tasks

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.google.gson.Gson
import com.imiyar.upload.UploadApkExtension
import com.imiyar.upload.UploadApkPlugin
import com.imiyar.upload.api.ApiConstants
import com.imiyar.upload.api.PgyUploadService
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import okhttp3.ResponseBody
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import retrofit2.Response
import com.imiyar.upload.api.ApiFactory

class PgyUploadTask extends DefaultTask {

    @Internal
    ApplicationVariant variant

    void init(ApplicationVariant variant) {
        this.variant = variant
        description = "Upload apk to Pgyer"
        group = "uploadApk"
    }

    @TaskAction
    void action() {
        UploadApkExtension uploadApkExtension = project.extensions.findByName(UploadApkPlugin.UPLOAD_APK_EXTENSION)
        AppExtension appExtension = project.extensions.findByName(UploadApkPlugin.ANDROID_EXTENSION)

        println("############################上传蒲公英#############################")
        println("# applicationId : " + variant.getApplicationId())
        println("# versionName   : " + appExtension.defaultConfig.versionName)
        println("# versionCode   : " + appExtension.defaultConfig.versionCode)
        println("# appName       : " + uploadApkExtension.appName)
        println("##################################################################")

        File outputFile
        File directory = new File(uploadApkExtension.outputDirectory)
        if (directory.isDirectory()) {
            File[] files = directory.listFiles()
            for (File file : files) {
                if (uploadApkExtension.isOpenReinforce) {
                    if (file.getName().contains("jiagu") && file.getName().endsWith(".apk")) {
                        outputFile = file
                        break
                    }
                } else {
                    if (file.getName().endsWith(".apk")) {
                        outputFile = file
                        break
                    }
                }
            }
        }

        if (outputFile != null) {
            println "\nUpload Apk Path: ${outputFile.absolutePath}"
            PgyUploadService pgyService = ApiFactory.getInstance().create(ApiConstants.PGY_BASE_URL, PgyUploadService.class)
            Response<ResponseBody> appResponse = pgyService.uploadFile(ApiFactory.getInstance().getTextBody(uploadApkExtension.apiKey), ApiFactory.getInstance().getTextBody(uploadApkExtension.appName),
                    ApiFactory.getInstance().getFilePart("application/vnd.android.package-archive", outputFile))
                    .execute()

            String result = appResponse.body().string()
            PgyResponse response = new Gson().fromJson(result, PgyResponse.class)
            if (response != null) {
                SendMsgToDingTalkTask.setUrl(response.data.buildShortcutUrl, response.data.buildQRCodeURL)
            }
        } else {
            println("Could not found the apk file")
        }
    }

    static class PgyResponse {

        public int code
        public String message
        public PgyDetail data

        static class PgyDetail {

            public String buildShortcutUrl
            public String buildUpdated
            public String buildQRCodeURL
            public String buildVersion
            public String buildVersionNo
            public String buildIcon

        }

    }

}

