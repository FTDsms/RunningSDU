package com.sdu.runningsdu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
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
        databaseHelper = new DatabaseHelper(context, name);
        Log.d("database", "database name: " + databaseHelper.getDatabaseName());
        findTable();
        findAllUser();
    }

    //查询表格
    public void findTable() {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        Log.d("database", "table:\n");
        while (cursor.moveToNext()) {
            Log.d("database", cursor.getString(0));
        }
        db.close();
    }

    //查询所有用户
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
        }
        db.close();
        return users;
    }

    //查找用户信息
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
        Log.d("database", "find user:\n" + user.toString());
        db.close();
        return user;
    }

    //添加用户
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
        Log.d("database", "add user: "+user.getName());
    }

    //修改用户信息
    public void updateUser(User user) {

    }

    //查找所有好友
    public void findAllFriend() {

    }

    //添加好友
    public void addFriend(Friend friend) {

    }

    //删除好友
    public void deleteFriend(String sid) {

    }

    //查找所有群聊
    public void findAllGroup() {

    }

    //添加群聊
    public void addGroup(Group group) {

    }

    //退群
    public void deleteGroup(String gid) {

    }

    //查找好友消息
    public void findFriendMessage(String sid) {

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

}
