package com.imiyar.upload.tasks

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.google.gson.Gson
import com.imiyar.upload.UploadApkExtension
import com.imiyar.upload.UploadApkPlugin
import com.imiyar.upload.api.ApiConstants
import com.imiyar.upload.api.ApiFactory
import com.imiyar.upload.api.SendDingTalkService
import okhttp3.ResponseBody
import org.apache.commons.codec.binary.Base64
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import retrofit2.Response

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class SendMsgToDingTalkTask extends DefaultTask {

    @Internal
    ApplicationVariant variant
    static String mShortCutUrl
    static String mQRCodeURL

    void init(ApplicationVariant variant) {
        this.variant = variant
        description = "Send message to DingTalk"
        group = "uploadApk"
    }

    @TaskAction
    void action() {
        UploadApkExtension uploadApkExtension = project.extensions.findByName(UploadApkPlugin.UPLOAD_APK_EXTENSION)
        AppExtension appExtension = project.extensions.findByName(UploadApkPlugin.ANDROID_EXTENSION)

        Link link = new Link()
        link.picUrl = mQRCodeURL
        link.messageUrl = "http://www.pgyer.com/" + mShortCutUrl
        link.title = uploadApkExtension.appName + "正式版"
        link.text = "版本${appExtension.defaultConfig.versionName}"

        DingTalkRequest request = new DingTalkRequest(link, "link")

        SendDingTalkService dingTalkService = ApiFactory.getInstance().create(ApiConstants.DING_TALK_BASE_URL, SendDingTalkService.class)
        Response<ResponseBody> appResponse = dingTalkService.sendMsgToDingTalk(uploadApkExtension.webHook, request)
                .execute()

        println("\nDingTalkMsgResponse:" + new Gson().toJson(appResponse.body().string()))
    }

    static void setUrl(String shortCutUrl, String qrUrl) {
        mShortCutUrl = shortCutUrl
        mQRCodeURL = qrUrl
    }

    static class DingTalkRequest {
        String msgtype
        Link link

        DingTalkRequest(Link link, String msgtype) {
            this.link = link
            this.msgtype = msgtype
        }
    }

    static class Link {
        String picUrl
        String messageUrl
        String title
        String text
    }

}