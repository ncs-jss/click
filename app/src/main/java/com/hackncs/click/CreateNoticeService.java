package com.hackncs.click;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface CreateNoticeService {


    @Multipart
    @POST("notice_create/")
    Call<JSONObject> upload(
            @HeaderMap Map<String, String> headers,
            @PartMap Map<String, RequestBody> params,
            @Part MultipartBody.Part file
    );


    @POST("notice_create/")
    Call<JSONObject> createNotice(@HeaderMap Map<String, String> headers, @Body NoticeModel model);
}
