package com.sdu.runningsdu.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.JavaBean.Request;
import com.sdu.runningsdu.JavaBean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/6/4.
 */

public class MyDAO {

    private Context context;

    private String name;

    private DatabaseHelper databaseHelper;

    public MyDAO(Context context, String name) {
        this.context = context;
        this.name = name;
        this.databaseHelper = DatabaseHelper.getInstance(context, name);
        Log.d("database", "database name: " + databaseHelper.getDatabaseName());
        findTable();
//        findAllUser();
    }

    /**
     * 查询所有表格
     * */
    public void findTable() {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        Log.d("database", "table:\n");
        while (cursor.moveToNext()) {
            Log.d("database", cursor.getString(0));
        }
        db.close();
    }

    /**
     * 查询是否有用户
     * */
    public boolean hasUser() {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("user",
                null, null, null, null, null, null);
        Log.w("has user", ""+cursor.getCount());
        if (cursor.getCount() > 0) {
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查询所有用户
     * */
    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("user",
                new String[]{"sid", "name", "password", "image"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            User user = new User();
            user.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setImage(cursor.getString(cursor.getColumnIndex("image")));
            Log.d("database", "find user: " + user.toString());
            users.add(user);
        }
        db.close();
        return users;
    }

    /**
     * 查找用户信息
     * */
    public User findUser(String sid) {
        User user = new User();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("user",
                new String[]{"sid", "name", "password", "image"},
                "sid = ?",
                new String[]{sid},
                null, null, null);
        while (cursor.moveToNext()) {
            user.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setImage(cursor.getString(cursor.getColumnIndex("image")));
        }
        db.close();
        return user;
    }

    /**
     * 添加用户
     * */
    public void addUser(User user) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[4];
        objects[0] = user.getSid();
        objects[1] = user.getName();
        objects[2] = user.getPassword();
        objects[3] = user.getImage();
        String sql = "insert into user(sid, name, password, image) values(?,?,?,?)";
        db.execSQL(sql, objects);
        db.close();
        Log.d("database", "add user: " + user.getName());
    }

    /**
     * 删除用户
     * */
    public void deleteUser(String sid) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        String sql = "delete from user where sid = ?";
        db.execSQL(sql, new Object[]{sid});
        db.close();
        Log.d("database", "delete user: " + sid);
    }

    //修改用户信息
    public void updateUser(User user) {

    }

    /**
     * 查询是否有该好友
     * */
    public boolean hasFriend() {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("friend",
                null, null, null, null, null, null);
        Log.w("has friend", ""+cursor.getCount());
        if (cursor.getCount() > 0) {
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查询好友信息
     */
    public Friend findFriend(String sid) {
        Friend friend = new Friend();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("friend",
                new String[]{"sid", "name", "image", "unread"},
                "sid = ?",
                new String[]{sid},
                null, null, null);
        if (cursor.moveToNext()) {
            friend.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            friend.setName(cursor.getString(cursor.getColumnIndex("name")));
            friend.setImage(cursor.getString(cursor.getColumnIndex("image")));
            friend.setUnread(cursor.getInt(cursor.getColumnIndex("unread")));
        }
        db.close();
        return friend;
    }

    /**
     * 查找所有好友
     * */
    public List<Friend> findAllFriend() {
        List<Friend> friends = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("friend",
                new String[]{"sid", "name", "image", "unread"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Friend friend = new Friend();
            friend.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            friend.setName(cursor.getString(cursor.getColumnIndex("name")));
            friend.setImage(cursor.getString(cursor.getColumnIndex("image")));
            friend.setUnread(cursor.getInt(cursor.getColumnIndex("unread")));
            friends.add(friend);
        }
        db.close();
        return friends;
    }

    /**
     * 添加好友
     * */
    public void addFriend(Friend friend) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[3];
        objects[0] = friend.getSid();
        objects[1] = friend.getName();
        objects[2] = friend.getImage();
        String sql = "insert into friend(sid, name, image) values(?,?,?)";
        db.execSQL(sql, objects);
        Log.d("database", "add friend: " + friend.getName());
        db.close();
    }

    /**
     * 批量添加好友
     * */
    public void addFriends(List<Friend> friends) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        for (Friend friend : friends) {
            Object[] objects = new Object[3];
            objects[0] = friend.getSid();
            objects[1] = friend.getName();
            objects[2] = friend.getImage();
            String sql = "insert into friend(sid, name, image) values(?,?,?)";
            db.execSQL(sql, objects);
            Log.d("database", "add friend: " + friend.getName());
        }
        db.close();
    }

    //删除好友
    public void deleteFriend(String sid) {

    }

    /**
     * 查询是否有该群组
     * */
    public boolean hasGroup() {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("group",
                null, null, null, null, null, null);
        Log.w("has group", ""+cursor.getCount());
        if (cursor.getCount() > 0) {
            return true;
        }
        db.close();
        return false;
    }

    //查找所有群聊
    public void findAllGroup() {

    }

    /**
     * 添加群聊
     * */
    public void addGroup(Group group) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[4];
        objects[0] = group.getGid();
        objects[1] = group.getName();
        objects[2] = group.getCreator();
        objects[3] = group.getImage();
        String sql = "insert into groups(gid, name, creator, image) values(?,?,?,?)";
        db.execSQL(sql, objects);
        Log.d("database", "add group: " + group.getName());
        db.close();
    }

    /**
     * 批量添加群聊
     * */
    public void addGroups(List<Group> groups) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        for (Group group : groups) {
            Object[] objects = new Object[4];
            objects[0] = group.getGid();
            objects[1] = group.getName();
            objects[2] = group.getCreator();
            objects[3] = group.getImage();
            String sql = "insert into groups(gid, name, creator, image) values(?,?,?,?)";
            db.execSQL(sql, objects);
            Log.d("database", "add group: " + group.getName());
        }
        db.close();
    }

    //退群
    public void deleteGroup(String gid) {

    }

    /**
     * 查找好友最后一条消息的id
     */
    public int findLastFriendMessage(String sid) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("friendmessage",
                new String[]{"mid"},
                "sid = ?",
                new String[]{sid},
                null, null,
                "mid");
        int mid = -1;
        if (cursor.moveToLast()) {
            mid = cursor.getInt(0);
            db.close();
            return mid;
        }
        return mid;
    }

    /**
     * 添加好友消息
     */
    public void addFriendMessage(Message message) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[5];
        objects[0] = message.getMid();
        objects[1] = message.getFriend();
        objects[2] = message.getType();
        objects[3] = message.getContent();
        objects[4] = message.getTime();
        String sql = "insert into friendmessage(mid, sid, type, content, time) values(?,?,?,?,?)";
        db.execSQL(sql, objects);
        Log.d("database", "add friendmessage: " + message.getMid());
        db.close();
    }

    /**
     * 批量添加好友消息
     */
    public void addFriendMessages(List<Message> messages) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        for (Message message : messages) {
            Object[] objects = new Object[5];
            objects[0] = message.getMid();
            objects[1] = message.getFriend();
            objects[2] = message.getType();
            objects[3] = message.getContent();
            objects[4] = message.getTime();
            String sql = "insert into friendmessage(mid, sid, type, content, time) values(?,?,?,?,?)";
            db.execSQL(sql, objects);
            Log.d("database", "add friendmessage: " + message.getMid());
        }
        db.close();
    }


    //查找好友消息
    public void findFriendMessage(String sid) {

    }

    /**
     * 查找群聊最后一条消息的id
     */
    public int findLastGroupMessage(String gid) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groupmessage",
                new String[]{"mid"},
                "gid = ?",
                new String[]{gid},
                null, null,
                "mid");
        int mid = -1;
        if (cursor.moveToLast()) {
            mid = cursor.getInt(0);
            db.close();
            return mid;
        }
        return mid;
    }

    /**
     * 添加群聊消息
     */
    public void addGroupMessage(Message message) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[6];
        objects[0] = message.getMid();
        objects[1] = message.getGroup();
        objects[2] = message.getFriend();
        objects[3] = message.getType();
        objects[4] = message.getContent();
        objects[5] = message.getTime();
        String sql = "insert into groupmessage(mid, gid, sid, type, content, time) values(?,?,?,?,?,?)";
        db.execSQL(sql, objects);
        Log.d("database", "add groupmessage: " + message.getMid());
        db.close();
    }

    /**
     * 批量添加群聊消息
     */
    public void addGroupMessages(List<Message> messages) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        for (Message message : messages) {
            Object[] objects = new Object[6];
            objects[0] = message.getMid();
            objects[1] = message.getGroup();
            objects[2] = message.getFriend();
            objects[3] = message.getType();
            objects[4] = message.getContent();
            objects[5] = message.getTime();
            String sql = "insert into groupmessage(mid, gid, sid, type, content, time) values(?,?,?,?,?,?)";
            db.execSQL(sql, objects);
            Log.d("database", "add groupmessage: " + message.getMid());
        }
        db.close();
    }

    //查找群聊消息
    public void findGroupMessage(String gid) {

    }

    //添加消息
    public void addMessage(Message message) {

    }

    //删除消息
    public void deleteMessage(String mid) {

    }

    /**
     * 添加好友申请
     * */
    public void addRequest(Request request) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[6];
        objects[0] = request.getRid();
        objects[1] = request.getReceiver();
        objects[2] = request.getSender();
        objects[3] = request.getMessage();
        objects[4] = request.getTime();
        objects[5] = request.getState();
        String sql = "insert into request(rid, receiver, sender, message, time, state) values(?,?,?,?,?,?)";
        db.execSQL(sql, objects);
        Log.d("database", "add request: " + request.getRid());
        db.close();
    }

    /**
     * 批量添加好友申请
     */
    public void addRequests(List<Request> requests) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        for (Request request : requests) {
            Object[] objects = new Object[6];
            objects[0] = request.getRid();
            objects[1] = request.getReceiver();
            objects[2] = request.getSender();
            objects[3] = request.getMessage();
            objects[4] = request.getTime();
            objects[5] = request.getState();
            String sql = "insert into request(rid, receiver, sender, message, time, state) values(?,?,?,?,?,?)";
            db.execSQL(sql, objects);
            Log.d("database", "add request: " + request.getRid());
        }
        db.close();
    }

}
