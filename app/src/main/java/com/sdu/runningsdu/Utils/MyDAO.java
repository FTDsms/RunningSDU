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
     */
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
     * 添加用户
     * @param user User对象
     */
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
     * @param sid 用户id
     */
    public void deleteUser(String sid) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        String sql = "delete from user where sid = ?";
        db.execSQL(sql, new Object[]{sid});
        db.close();
        Log.d("database", "delete user: " + sid);
    }

    /**
     * 更新用户信息
     * @param user User对象
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[4];
        objects[0] = user.getName();
        objects[1] = user.getPassword();
        objects[2] = user.getImage();
        objects[3] = user.getSid();
        String sql = "update user set name=?, password=?, image=? where sid=?";
        db.execSQL(sql, objects);
        db.close();
        Log.d("database", "update user: " + user.getName());
    }

    /**
     * 查询是否有用户
     * @return 当前是否存在用户
     */
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
     * 查找用户信息
     * @param sid 用户id
     * @return User对象
     */
    public User findUser(String sid) {
        User user = new User();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("user",
                new String[]{"sid", "name", "password", "image"},
                "sid = ?",
                new String[]{sid},
                null, null, null);
        if (cursor.moveToNext()) {
            user.setSid(cursor.getString(cursor.getColumnIndex("sid")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setImage(cursor.getString(cursor.getColumnIndex("image")));
        }
        Log.d("database", "find user: " + user.toString());
        db.close();
        return user;
    }

    /**
     * 查询所有用户
     * @return User列表
     */
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
     * 添加好友
     * @param friend Friend对象
     */
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
     * @param friends Friend列表
     */
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

    /**
     * 删除好友
     * @param sid 好友学号
     */
    public void deleteFriend(String sid) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        String sql = "delete from friend where sid = ?";
        db.execSQL(sql, new Object[]{sid});
        db.close();
        Log.d("database", "delete friend: " + sid);
    }

    /**
     * 更新好友信息
     * @param friend Friend对象
     */
    public void updateFriend(Friend friend) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[3];
        objects[0] = friend.getName();
        objects[1] = friend.getImage();
        objects[2] = friend.getSid();
        String sql = "update friend set name=?, image=? where sid=?";
        db.execSQL(sql, objects);
        db.close();
        Log.d("database", "update friend: " + friend.getName());
    }

    /**
     * 更新好友未读消息数
     * @param friend Friend对象
     */
    public void updateFriendUnread(Friend friend) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[2];
        objects[0] = friend.getUnread();
        objects[1] = friend.getSid();
        String sql = "update friend set unread=? where sid=?";
        db.execSQL(sql, objects);
        db.close();
        Log.d("database", "update friend unread: " + friend.getName() + " " + friend.getUnread());
    }

    /**
     * 查询是否有该好友
     * @param sid 好友学号
     * @return 是否有学号为sid的好友
     */
    public boolean hasFriend(String sid) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("friend",
                null,
                "sid = ?",
                new String[]{sid},
                null, null, null);
        if (cursor.getCount() > 0) {
            Log.w("has friend", ""+cursor.getCount());
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查询好友信息
     * @param sid 好友学号
     * @return Friend对象
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
            friend.setSid(sid);
            friend.setName(cursor.getString(cursor.getColumnIndex("name")));
            friend.setImage(cursor.getString(cursor.getColumnIndex("image")));
            friend.setUnread(cursor.getInt(cursor.getColumnIndex("unread")));
        }
        db.close();
        return friend;
    }

    /**
     * 查找所有好友
     * @return Friend列表
     */
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
     * 添加群聊
     * @param group Group对象
     */
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
     * @param groups Group列表
     */
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

    /**
     * 删除群聊
     * @param gid 群id
     */
    public void deleteGroup(int gid) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        String sql = "delete from groups where gid = ?";
        db.execSQL(sql, new Object[]{Integer.toString(gid)});
        db.close();
        Log.d("database", "delete group: " + gid);
    }

    /**
     * 更新群组信息
     * @param group Group对象
     */
    public void updateGroup(Group group) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[4];
        objects[0] = group.getName();
        objects[1] = group.getCreator();
        objects[2] = group.getImage();
        objects[3] = group.getGid();
        String sql = "update groups set name=?, creator=?, image=? where gid=?";
        db.execSQL(sql, objects);
        db.close();
        Log.d("database", "update group: " + group.getName());
    }

    /**
     * 更新群组未读消息数
     * @param group Group对象
     */
    public void updateGroupUnread(Group group) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[2];
        objects[0] = group.getUnread();
        objects[1] = group.getGid();
        String sql = "update groups set unread=? where gid=?";
        db.execSQL(sql, objects);
        db.close();
        Log.d("database", "update group unread: " + group.getName() + " " + group.getUnread());
    }

    /**
     * 查询是否有该群组
     * @param gid 群id
     * @return 是否有id为gid的群组
     */
    public boolean hasGroup(int gid) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groups",
                null,
                "gid = ?",
                new String[]{Integer.toString(gid)},
                null, null, null);
        Log.w("has group", ""+cursor.getCount());
        if (cursor.getCount() > 0) {
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查询群聊信息
     * @param gid 群id
     * @return Group对象
     */
    public Group findGroup(int gid) {
        Group group = new Group();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groups",
                new String[]{"gid", "name", "creator", "image", "unread"},
                null, null, null, null, null);
        if (cursor.moveToNext()) {
            group.setGid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("gid"))));
            group.setName(cursor.getString(cursor.getColumnIndex("name")));
            group.setCreator(cursor.getString(cursor.getColumnIndex("creator")));
            group.setImage(cursor.getString(cursor.getColumnIndex("image")));
            group.setUnread(cursor.getInt(cursor.getColumnIndex("unread")));
        }
        db.close();
        return group;
    }

    /**
     * 查找所有群聊
     * @return Group对象
     */
    public List<Group> findAllGroup() {
        List<Group> groups = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groups",
                new String[]{"gid", "name", "creator", "image", "unread"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Group group = new Group();
            group.setGid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("gid"))));
            group.setName(cursor.getString(cursor.getColumnIndex("name")));
            group.setCreator(cursor.getString(cursor.getColumnIndex("creator")));
            group.setImage(cursor.getString(cursor.getColumnIndex("image")));
            group.setUnread(cursor.getInt(cursor.getColumnIndex("unread")));
            groups.add(group);
        }
        db.close();
        return groups;
    }

    /**
     * 添加单个群组成员
     * @param gid
     * @param sid
     */
    public void addGroupMember(int gid, String sid) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[2];
        objects[0] = gid;
        objects[1] = sid;
        String sql = "insert into groupmember(gid, sid) values(?,?)";
        db.execSQL(sql, objects);
        Log.d("database", "add group member: " + gid + " " + sid);
        db.close();
    }

    /**
     * 添加群组成员
     * @param group Group对象
     */
    public void addGroupMembers(Group group) {
        int gid = group.getGid();
        List<String> members = group.getMembers();
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        for (String member : members) {
            Object[] objects = new Object[2];
            objects[0] = gid;
            objects[1] = member;
            String sql = "insert into groupmember(gid, sid) values(?,?)";
            db.execSQL(sql, objects);
            Log.d("database", "add group member: " + gid + " " + member);
        }
        db.close();
    }

    /**
     * 删除群组成员
     * @param gid 群id
     * @param sid 学号
     */
    public void deleteGroupMember(int gid, String sid) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        String sql = "delete from groupmember where gid=?, sid=?";
        db.execSQL(sql, new Object[]{Integer.toString(gid), sid});
        db.close();
        Log.d("database", "delete group member: " + gid);
    }

    /**
     * 是否有该群成员
     * @param gid
     * @param sid
     * @return
     */
    public boolean hasGroupMember(int gid, String sid) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groupmember",
                null,
                "gid=? and sid=?",
                new String[]{Integer.toString(gid), sid},
                null, null, null);
        if (cursor.getCount() > 0) {
            Log.w("has groupmember", ""+cursor.getCount());
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查询群组成员
     * @param gid 群id
     * @return 群号为gid的所有成员
     */
    public List<String> findGroupMember(int gid) {
        List<String> members = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groupmember",
                new String[]{"sid"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            members.add(cursor.getString(cursor.getColumnIndex("sid")));
        }
        db.close();
        return members;
    }

    /**
     * 添加好友消息
     * @param message Message对象
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
     * @param messages Message对象
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

    /**
     * 查找好友最后一条消息的id
     * @param sid 好友学号
     * @return 好友学号为sid的最后一条消息
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
     * 是否存在好友消息
     * @param message
     * @return
     */
    public boolean hasFriendMessage(Message message) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("friendmessage",
                null,
                "mid=?",
                new String[]{Integer.toString(message.getMid())},
                null, null, null);
        if (cursor.getCount() > 0) {
            Log.w("has friendmessage", ""+message.getMid());
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查找好友消息
     * @param sid 好友学号
     * @return 和好友学号为sid的所有消息
     */
    public List<Message> findFriendMessage(String sid) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("friendmessage",
                new String[]{"mid", "sid", "type", "content", "time"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int mid = Integer.parseInt(cursor.getString(cursor.getColumnIndex("mid")));
            int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex("type")));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            messages.add(new Message(mid, sid, type, content, time));
        }
        db.close();
        return messages;
    }

    /**
     * 添加群聊消息
     * @param message Message对象
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
     * @param messages Message列表
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

    /**
     * 是否存在群组消息
     * @param message
     * @return
     */
    public boolean hasGroupMessage(Message message) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groupmessage",
                null,
                "mid=?",
                new String[]{Integer.toString(message.getMid())},
                null, null, null);
        if (cursor.getCount() > 0) {
            Log.w("has groupmessage", ""+message.getMid());
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查找群聊最后一条消息的id
     * @param gid 群id
     * @return 群号为id的最后一条消息的id
     */
    public int findLastGroupMessage(int gid) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groupmessage",
                new String[]{"mid"},
                "gid = ?",
                new String[]{Integer.toString(gid)},
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
     * 查找群聊消息
     * @param gid 群id
     * @return 群聊消息
     */
    public List<Message> findGroupMessage(int gid) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("groupmessage",
                new String[]{"mid", "gid", "sid", "type", "content", "time"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int mid = Integer.parseInt(cursor.getString(cursor.getColumnIndex("mid")));
            String sid = cursor.getString(cursor.getColumnIndex("sid"));
            int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex("type")));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            messages.add(new Message(mid, gid, sid, type, content, time));
        }
        db.close();
        return messages;
    }

    /**
     * 添加好友申请
     * @param request Request对象
     */
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
     * @param requests Request列表
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

    /**
     * 更新好友申请
     * @param request Request对象
     */
    public void updateRequest(Request request) {
        SQLiteDatabase db = this.databaseHelper.getWritableDatabase();
        Object[] objects = new Object[6];
        objects[0] = request.getReceiver();
        objects[1] = request.getSender();
        objects[2] = request.getMessage();
        objects[3] = request.getTime();
        objects[4] = request.getState();
        objects[5] = request.getRid();
        String sql = "update request set receiver=?, sender=?, message=?, time=?, state=? where rid=?";
        db.execSQL(sql, objects);
        db.close();
        Log.d("database", "update request: " + request.getRid());
    }

    /**
     * 查询是否有申请
     * @param rid 申请id
     * @return 是否该id的申请
     */
    public boolean hasRequest(int rid) {
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("request",
                null,
                "rid = ?",
                new String[]{Integer.toString(rid)},
                null, null, null);
        if (cursor.getCount() > 0) {
            Log.w("has Request", ""+cursor.getCount());
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 查询所有好友请求
     * @return
     */
    public List<Request> findAllRequest() {
        List<Request> requests = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("request",
                new String[]{"rid", "receiver", "sender", "message", "time", "state"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int rid = Integer.parseInt(cursor.getString(cursor.getColumnIndex("rid")));
            String receiver = cursor.getString(cursor.getColumnIndex("receiver"));
            String sender = cursor.getString(cursor.getColumnIndex("sender"));
            String message = cursor.getString(cursor.getColumnIndex("message"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int state = Integer.parseInt(cursor.getString(cursor.getColumnIndex("state")));
            Request request = new Request(rid, receiver, sender, message, time, state);
            requests.add(request);
        }
        db.close();
        return requests;
    }

}
