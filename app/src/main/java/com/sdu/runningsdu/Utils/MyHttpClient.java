package com.sdu.runningsdu.Utils;

import android.util.Log;

import com.sdu.runningsdu.JavaBean.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 登录
     * */
    public static String login(String url, String sid, String password) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .add("password", password)
                .build();
        Log.d("FormBody", formBody.toString());
        Request request = new Request.Builder()
                .url(url+"/login")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 更新经纬度
     * */
    public static String updateLocation(String url, String sid, String longitude, String latitude) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .add("longitude", longitude)
                .add("latitude", latitude)
                .build();
        Log.d("FormBody", formBody.toString());
        Request request = new Request.Builder()
                .url(url+"/updateLocation")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 通过学号查找用户
     * */
    public static Friend findUserBySid(String url, String sid) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findUserBySid")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return null;
        } else {
            Friend friend = new Friend();
            JSONObject obj = jsonObject.getJSONObject("obj");
            friend.setSid(obj.optString("sid"));
            friend.setName(obj.optString("name"));
            friend.setImage(obj.optString("image"));
            return friend;
        }
    }

    /**
     * 查询收到的请求
     * */
    public static List<com.sdu.runningsdu.JavaBean.Request> findReceivedRequest(String url, String receiver) throws IOException, JSONException {
        List<com.sdu.runningsdu.JavaBean.Request> requests = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("receiver", receiver)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findRequestByReceiver")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONArray json = new JSONArray(response.body().string());
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            int rid = Integer.parseInt(obj.optString("rid"));
            String sender = obj.optString("sender");
            String message = obj.optString("message");
            String time = obj.optString("time");
            int state = Integer.parseInt(obj.optString("state"));
            com.sdu.runningsdu.JavaBean.Request r = new com.sdu.runningsdu.JavaBean.Request(rid, receiver, sender, message, time, state);
            requests.add(r);
        }
        return requests;
    }

    // 查询发送的请求
    public static String findSentRequest(String url, String sender) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sender", sender)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findRequestBySender")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 添加好友
     * */
    public static String addFriendRequest(String url, String receiver, String sender) throws IOException {
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

    // 同意好友请求
    public static String agreeRequest(String url, String rid) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("rid", rid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/agreeRequest")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 拒绝好友请求
    public static String rejectRequest(String url, String rid) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("rid", rid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/disagreeRequest")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 查找我的好友
     * */
    public static List<Friend> findMyFriend(String url, String sid) throws IOException, JSONException {
        List<Friend> friends = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/getFriendsUserinfo")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONArray json = new JSONArray(response.body().string());
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            String fsid = obj.optString("sid");
            String name = obj.optString("name");
            String image = obj.optString("image");
            Friend friend = new Friend(fsid, name, image);
            friends.add(friend);
        }
        return friends;
    }

    /**
     * 查询好友对话
     * */
    public static String findMessage(String url, int mid, String myName, String friendName) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("coid", Integer.toString(mid))
                .add("receiver", myName)
                .add("sender", friendName)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findConversations")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 发送消息
    public static String sendMessage(String url, String receiver, String sender, String category, String content) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("receiver", receiver)
                .add("sender", sender)
                .add("category", category)
                .add("content", content)
                .build();
        Request request = new Request.Builder()
                .url(url+"/addConversations")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 创建群组
    public static String createGroup(String url, String name, String members, String image) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("members", members)
                .add("image", image)
                .build();
        Request request = new Request.Builder()
                .url(url+"/createGroup")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 通过群号查找群组
     * */
    public static String findGroupByGid(String url, String gid) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gid", gid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findGroupByGid")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 查找自己加入的群组
     * */
    public static String findMyGroup(String url, String sid) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findGroupsBySid")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 查找群消息
     * */
    public static String findGroupMessage(String url, String gid, int mid) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gid", gid)
                .add("gnid", Integer.toString(mid))
                .build();
        Request request = new Request.Builder()
                .url(url+"/findGroupnotesByGidEquals")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 发送群消息
    public static String sendGroupMessage(String url, String gid, String sid, String category, String content) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gid", gid)
                .add("sid", sid)
                .add("category", category)
                .add("content", content)
                .build();
        Request request = new Request.Builder()
                .url(url+"/addGroupnote")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 邀请加群
    public static String inviteGroup(String url, String sid, String gid) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .add("gid", gid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/invite")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 退群/移出群聊
    public static String exitGroup(String url, String sid, String gid) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .add("gid", gid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/exitGroup")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    // 解散群组
    public static String cancelGroup(String url, String gid) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gid", gid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/cancelGroup")
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
