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

    // 登录
    public static String login(String url, String sid, String password) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody body = RequestBody.create(JSON, json);
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(url+"/login")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 添加好友
    public static String addFriendRequest(String url, String receiver, String sender) throws  IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("receiver", receiver)
                .add("sender", sender)
                .build();
        Request request = new Request.Builder()
                .url(url+"/saveRequest")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 查询发送者的请求
    public static void findRequestBySender() {

    }

    // 接受好友请求
    public static void agreeRequest() {

    }

    // 拒绝好友请求
    public static void rejectRequest() {

    }

    // 查找好友
    public static void findFriend(String url, String sid) {

    }

    // 查询对话
    public static void findMessage() {

    }

    // 发送消息
    public static void sendMessage() {

    }

    // 创建群组
    public static void createGroup() {

    }

    // 查找群组
    public static void findGroup() {

    }

    // 发送群消息
    public static void sendGroupMessage() {

    }

    // 邀请加群
    public static void inviteGroup() {

    }

    // 退群
    public static void exitGroup() {

    }

    // 移出群聊
    public static void moveOutOfGroup() {

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
