package com.sdu.runningsdu;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by FTDsm on 2018/6/3.
 */

public class MyHttpClient {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public MyHttpClient() {

    }

    public static String login(String url, String sid, String password) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody body = RequestBody.create(JSON, json);
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String post(String url, String json) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String get(String url) throws IOException{
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

}
