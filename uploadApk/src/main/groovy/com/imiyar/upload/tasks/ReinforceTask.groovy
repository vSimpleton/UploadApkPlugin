package com.imiyar.upload.tasks

import com.android.build.gradle.api.ApplicationVariant
import com.android.builder.model.SigningConfig
import com.imiyar.upload.UploadApkExtension
import com.imiyar.upload.UploadApkPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

class ReinforceTask extends DefaultTask {

    @Internal
    ApplicationVariant variant

    void init(ApplicationVariant variant) {
        this.variant = variant
        description = "Reinforce Release Apk"
        group = "uploadApk"
    }

    @TaskAction
    void action() {
        UploadApkExtension uploadApkExtension = project.extensions.findByName(UploadApkPlugin.UPLOAD_APK_EXTENSION)
        // 获取签名信息，以便后面进行重签名
        SigningConfig signingConfig = variant.signingConfig

        // 调用命令行工具执行360加固的登录操作
        project.exec { ExecSpec spec ->
            spec.commandLine(
                    "java", "-jar", uploadApkExtension.reinforceFilePath,
                    "-login", uploadApkExtension.reinforceUserName, uploadApkExtension.reinforcePassword)
        }

        // 调用命令行工具执行360加固的获取签名信息操作
        if (signingConfig) {
            project.exec { ExecSpec spec ->
                spec.commandLine("java", "-jar", uploadApkExtension.reinforceFilePath,
                        "-importsign", signingConfig.storeFile.absolutePath, signingConfig.storePassword,
                        signingConfig.keyAlias, signingConfig.keyPassword)
            }
        }

        // 调用命令行工具执行360加固的加固操作
        project.exec { ExecSpec spec ->
            spec.commandLine("java", "-jar", uploadApkExtension.reinforceFilePath,
                    "-jiagu", uploadApkExtension.inputFilePath, uploadApkExtension.outputFilePath, "-autosign")
        }
    }

}