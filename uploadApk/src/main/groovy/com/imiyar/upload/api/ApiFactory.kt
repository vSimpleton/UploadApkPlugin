package com.imiyar.upload.api

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiFactory {

    private val mClient: OkHttpClient by lazy { newClient() }

    fun <T> create(baseUrl: String, clazz: Class<T>): T =
        Retrofit.Builder().baseUrl(baseUrl).client(mClient)
            .addConverterFactory(GsonConverterFactory.create()).build().create(clazz)

    private fun newClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
    }.build()

    fun getFilePart(mediaType: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "file", file.absoluteFile.name, RequestBody.create(
                MediaType.parse(mediaType), file
            )
        )
    }

}