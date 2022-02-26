package com.imiyar.upload.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * NAME: vSimpleton
 * DATE: 2022/2/26
 * DESC:
 */

interface PgyUploadService {

    @Multipart
    @POST("app/upload")
    fun uploadFile(
        @Part("_api_key") key: String,
        @Part("buildName") buildName: String,
        @Part file: MultipartBody.Part
    ): Call<ResponseBody>

}