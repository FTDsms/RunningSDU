package com.sdu.runningsdu.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public static final MediaType PNG = MediaType.parse("image/png");

    public MyHttpClient() {

    }

    /**
     * 登录
     * @param url
     * @param sid
     * @param password
     * @return
     * @throws IOException
     */
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
     * @param url
     * @param sid
     * @param longitude
     * @param latitude
     * @return
     * @throws IOException
     */
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
     * @param url
     * @param sid
     * @return
     * @throws IOException
     * @throws JSONException
     */
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
            JSONObject obj = jsonObject.getJSONObject("obj");
            String name = obj.optString("name");
            String image = obj.optString("image");
            Friend friend = new Friend(sid, name, image);
            return friend;
        }
    }

    /**
     * 查询收到的请求
     * @param url
     * @param receiver
     * @return
     * @throws IOException
     * @throws JSONException
     */
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

    /**
     * 查询发送的请求
     * @param url
     * @param sender
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<com.sdu.runningsdu.JavaBean.Request> findSentRequest(String url, String sender) throws IOException, JSONException {
        List<com.sdu.runningsdu.JavaBean.Request> requests = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sender", sender)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findRequestBySender")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONArray json = new JSONArray(response.body().string());
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            int rid = Integer.parseInt(obj.optString("rid"));
            String receiver = obj.optString("receiver");
            String message = obj.optString("message");
            String time = obj.optString("time");
            int state = Integer.parseInt(obj.optString("state"));
            com.sdu.runningsdu.JavaBean.Request r = new com.sdu.runningsdu.JavaBean.Request(rid, receiver, sender, message, time, state);
            requests.add(r);
        }
        return requests;
    }

    /**
     * 添加好友
     * @param url
     * @param receiver
     * @param sender
     * @param message
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static boolean addFriendRequest(String url, String receiver, String sender, String message) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("receiver", receiver)
                .add("sender", sender)
                .add("message", message)
                .build();
        Request request = new Request.Builder()
                .url(url+"/saveRequest")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 同意好友请求
     * @param url
     * @param rid
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static boolean agreeRequest(String url, int rid) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("rid", Integer.toString(rid))
                .build();
        Request request = new Request.Builder()
                .url(url+"/agreeRequest")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    /**.
     * 拒绝好友请求
     * @param url
     * @param rid
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static boolean rejectRequest(String url, int rid) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("rid", Integer.toString(rid))
                .build();
        Request request = new Request.Builder()
                .url(url+"/disagreeRequest")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 查找我的好友
     * @param url
     * @param sid
     * @return
     * @throws IOException
     * @throws JSONException
     */
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
     * @param url
     * @param mid
     * @param myName
     * @param friendName
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<Message> findFriendMessage(String url, int mid, String myName, String friendName) throws IOException, JSONException {
        List<Message> messages = new ArrayList<>();
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

        JSONArray json = new JSONArray(response.body().string());
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            int mmid = Integer.parseInt(obj.optString("coid"));
            String sender = obj.optString("sender");
            String receiver = obj.optString("receiver");
            String content = obj.optString("content");
            String time = obj.optString("time");
            String friend;
            int type;
            if (sender.equals(myName)) {
                friend = receiver;
                type = Message.TYPE_SENT;
            } else {
                friend = sender;
                type = Message.TYPE_RECEIVED;
            }
            Message message = new Message(mmid, friend, type, content, time);
            messages.add(message);
        }
        return messages;
    }

    /**
     * 发送好友消息
     * @param url
     * @param receiver
     * @param sender
     * @param category
     * @param content
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static boolean sendFriendMessage(String url, String receiver, String sender, String category, String content) throws IOException, JSONException {
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
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 创建群组
     * @param url
     * @param name
     * @param members
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static Group createGroup(String url, String name, List<String> members) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
//        JSONArray jsonArray = new JSONArray();
//        for (int i=0; i<members.size(); ++i) {
//            jsonArray.put(members.get(i));
//        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(members.get(0));
        for (int i=1; i<members.size(); ++i) {
            stringBuffer.append(",");
            stringBuffer.append(members.get(i));
        }
//        Log.d("StringBuffer", stringBuffer.toString());
        FormBody formBody = new FormBody.Builder()
                .add("name", name)
//                .add("members", jsonArray.toString())
                .add("members", stringBuffer.toString())
                .build();
        Request request = new Request.Builder()
                .url(url+"/createGroup")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        int gid = Integer.parseInt(jsonObject.optString("gid"));
        String image = jsonObject.optString("image");
        return new Group(gid, name, members.get(0), image);
    }

    /**
     * 通过群号查找群组
     * @param url
     * @param gid
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static Group findGroupByGid(String url, int gid) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gid", Integer.toString(gid))
                .build();
        Request request = new Request.Builder()
                .url(url+"/findGroupByGid")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return null;
        } else {
            JSONObject obj = jsonObject.getJSONObject("obj");
            JSONArray jsonArray = obj.optJSONArray("members");
            List<String> members = new ArrayList<>();
            for (int i=0; i<jsonArray.length(); ++i) {
                members.add(jsonArray.getString(i));
            }
            String name = obj.optString("name");
            String creator = obj.optString("creator");
            String image = obj.optString("image");
            Group group = new Group(gid, name, creator, image);
            group.setMembers(members);
            return group;
        }
    }

    /**
     * 查找自己加入的群组
     * @param url
     * @param sid
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<Group> findMyGroup(String url, String sid) throws IOException, JSONException {
        List<Group> groups = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findGroupsBySid")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONArray jsonObject = new JSONArray(response.body().string());
        for (int i=0; i<jsonObject.length(); ++i) {
            JSONObject json = jsonObject.optJSONObject(i);
            JSONObject wechat = json.optJSONObject("wechat");
            int gid = Integer.parseInt(wechat.optString("gid"));
            String name = wechat.optString("name");
            String creator = wechat.optString("creator");
            String image = wechat.optString("image");
            Group group = new Group(gid, name, creator, image);
            JSONArray memberInfos = json.optJSONArray("memberInfos");
            List<String> members = new ArrayList<>();
            for (int j=0; j<memberInfos.length(); ++j) {
                JSONObject memberinfo = memberInfos.optJSONObject(j);
                members.add(memberinfo.optString("sid"));
            }
            group.setMembers(members);
            groups.add(group);
        }
        return groups;
    }

    /**
     * 查找群消息
     * @param url
     * @param gid
     * @param mid
     * @param myName
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<Message> findGroupMessage(String url, int gid, int mid, String myName) throws IOException, JSONException {
        List<Message> messages = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gid", Integer.toString(gid))
                .add("gnid", Integer.toString(mid))
                .build();
        Request request = new Request.Builder()
                .url(url+"/findGroupnotesByGidEquals")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONArray json = new JSONArray(response.body().string());
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            int gnid = Integer.parseInt(obj.optString("gnid"));
            String sid = obj.optString("sid");
            String content = obj.optString("content");
            String time = obj.optString("time");
            int type;
            if (sid.equals(myName)) {
                type = Message.TYPE_SENT;
            } else {
                type = Message.TYPE_RECEIVED;
            }
            Message message = new Message(gnid, gid, sid, type, content, time);
            messages.add(message);
        }
        return messages;
    }

    /**
     * 发送群消息
     * @param url
     * @param gid
     * @param sid
     * @param category
     * @param content
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static boolean sendGroupMessage(String url, int gid, String sid, String category ,String audio, String photo, String content) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gid", Integer.toString(gid))
                .add("sid", sid)
                .add("category", category)
                .add("audio", audio)
                .add("photo", photo)
                .add("content", content)
                .build();
        Request request = new Request.Builder()
                .url(url+"/addGroupnote")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 邀请加群
     * @param url
     * @param sid
     * @param gid
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static boolean inviteGroup(String url, String sid, int gid) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .add("gid", Integer.toString(gid))
                .build();
        Request request = new Request.Builder()
                .url(url+"/invite")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 退群 & 移出群聊
     * @param url
     * @param sid
     * @param gid
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static boolean exitGroup(String url, String sid, int gid) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .add("gid", Integer.toString(gid))
                .build();
        Request request = new Request.Builder()
                .url(url+"/exitGroup")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 解散群组
     * @param url
     * @param gid
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static boolean cancelGroup(String url, int gid) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("gid", Integer.toString(gid))
                .build();
        Request request = new Request.Builder()
                .url(url+"/cancelGroup")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        String flag = jsonObject.optString("flag");
        if (!flag.equals("true")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过姓名查找用户
     * @param url
     * @param name
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<Friend> findUserByName(String url, String name) throws IOException, JSONException {
        List<Friend> friends = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .build();
        Request request = new Request.Builder()
                .url(url+"/findUsersByName")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONArray json = new JSONArray(response.body().string());
        for (int i=0; i<json.length(); ++i) {
            JSONObject obj = json.optJSONObject(i);
            String sid = obj.optString("sid");
            String image = obj.optString("image");
            Friend friend = new Friend(sid, name, image);
            friends.add(friend);
        }
        return friends;
    }

    /**
     * 下载图片
     * @param url
     * @param imagePath 头像路径
     * @return
     * @throws IOException
     */
    public static Bitmap downloadImage(String url, String imagePath) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"/show?fileName="+imagePath)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        InputStream is = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
//        byte[] bytes = response.body().bytes();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }


    public static String uploadImage(String url, String sid, Bitmap bitmap) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        FormBody formBody = new FormBody.Builder()
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url+"/uploadImage")
                .post(formBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().toString());
        String image = jsonObject.optString("image");
        return image;
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
