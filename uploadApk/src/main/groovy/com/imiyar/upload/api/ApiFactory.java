package com.imiyar.upload.api;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ApiFactory {

    private static volatile ApiFactory mInstance;

    private ApiFactory() {

    }

    public static ApiFactory getInstance() {
        if (mInstance == null) {
            synchronized (ApiFactory.class) {
                if (mInstance == null) {
                    mInstance = new ApiFactory();
                }
            }
        }
        return mInstance;
    }

    public <T> T create(String baseUrl, Class<T> clazz) {
        return new Retrofit.Builder().baseUrl(baseUrl).client(newClient()).addConverterFactory(GsonConverterFactory.create()).build().
                create(clazz);
    }

    private OkHttpClient newClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        return builder.build();
    }

    public MultipartBody.Part getFilePart(String mediaType, File file) {
        return MultipartBody.Part.createFormData("file", file.getAbsoluteFile().getName(),
                RequestBody.create(MediaType.parse(mediaType), file));
    }

    public RequestBody getTextBody(String text) {
        return RequestBody.create(MediaType.parse("text/plain"), text);
    }

}