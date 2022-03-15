package com.imiyar.upload.api;

import com.imiyar.upload.tasks.SendMsgToDingTalkTask;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * NAME: vSimpleton
 * DATE: 2022/3/1
 * DESC:
 */

public interface SendDingTalkService {

    @POST("robot/send")
    Call<ResponseBody> sendMsgToDingTalk(
            @Query("access_token") String accessToken,
            @Body SendMsgToDingTalkTask.DingTalkRequest request
    );

}