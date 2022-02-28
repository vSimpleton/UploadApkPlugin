package com.imiyar.upload.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

/**
 * NAME: vSimpleton
 * DATE: 2022/2/26
 * DESC:
 */

public interface PgyUploadService {

    @Multipart
    @POST("app/upload")
    Call<ResponseBody> uploadFile(@Part("_api_key") RequestBody key,
                                  @Part("buildName") RequestBody buildName,
                                  @Part MultipartBody.Part file);

}