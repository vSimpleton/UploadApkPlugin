package com.imiyar.upload

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.imiyar.upload.tasks.PgyUploadTask
import com.imiyar.upload.tasks.ReinforceTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class UploadApkPlugin implements Plugin<Project> {

    static final String ANDROID_EXTENSION = "android"
    static final String UPLOAD_APK_EXTENSION = "uploadApk"

    @Override
    void apply(Project project) {
        // 创建 uploadApk 扩展
        project.extensions.create(UPLOAD_APK_EXTENSION, UploadApkExtension.class)

        // 当在build.gradle中配置好UploadApkExtension的属性后，如果直接通过uploadApkExtension.getXxx()是无法获取得到值的
        // 所以需要调用project.afterEvaluate，该闭包会在gradle配置完成后回调，即解析完build.gradle文件后回调
        project.afterEvaluate {
            // AppExtension是Android插件创建的扩展
            AppExtension androidExtension = project.extensions.findByName(ANDROID_EXTENSION)
            // 获取apk包的变体，applicationVariants默认有debug跟release两种变体
            androidExtension.applicationVariants.all { ApplicationVariant variant ->
                if (variant.name.equalsIgnoreCase("release")) {
                    ReinforceTask reinforceTask = project.tasks.create("reinforceRelease", ReinforceTask)
                    reinforceTask.init(variant)

                    PgyUploadTask pgyUploadTask = project.tasks.create("pgyUploadRelease", PgyUploadTask)
                    pgyUploadTask.init(variant)

                    // 修改task的依赖关系
                    variant.getAssembleProvider().get().dependsOn(project.getTasks().findByName("clean"))
                    reinforceTask.dependsOn(variant.getAssembleProvider().get())
                    pgyUploadTask.dependsOn(reinforceTask)

                }
            }
        }
    }

}