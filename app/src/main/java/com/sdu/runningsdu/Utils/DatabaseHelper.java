package com.sdu.runningsdu.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

/**
 * Created by FTDsm on 2018/6/4.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "xxx.db";
    private static final int DB_VERSION = 1;
    private static DatabaseHelper databaseHelper;

    public DatabaseHelper(Context context, String name) {
        super(context, name, null, DB_VERSION);
        if(Build.VERSION.SDK_INT >= 11){
            getWritableDatabase().enableWriteAheadLogging();
        }
    }

    /**
     * 单例模式
     * @param context
     * @param name
     * @return DatabaseHelper
     */
    public static synchronized DatabaseHelper getInstance(Context context, String name) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context, name);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String userSQL = "create table if not exists user " +
                "(sid varchar(20) primary key, " +
                "name varchar(255), " +
                "password varchar(255), " +
                "imagePath varchar(255), " +
                "image blob)";
        sqLiteDatabase.execSQL(userSQL);
        Log.d("database", "create table user");

        String friendSQL = "create table if not exists friend " +
                "(sid varchar(20) primary key, " +
                "name varchar(255), " +
                "imagePath varchar(255), " +
                "unread integer, " +
                "image blob)";
        sqLiteDatabase.execSQL(friendSQL);
        Log.d("database", "create table friend");

        String groupSQL = "create table if not exists groups " +
                "(gid integer primary key, " +
                "name varchar(255), " +
                "creator varchar(255), " +
                "imagePath varchar(255), " +
                "unread varchar(255), " +
                "image blob)";
        sqLiteDatabase.execSQL(groupSQL);
        Log.d("database", "create table groups");

        String groupMemberSQL = "create table if not exists groupmember " +
                "(gid integer, " +
                "sid varchar(20)," +
                "primary key(gid, sid) )";
        sqLiteDatabase.execSQL(groupMemberSQL);
        Log.d("database", "create table groupmember");

        String friendMessageSQL = "create table if not exists friendmessage " +
                "(mid integer primary key, " +
                "sid varchar(20), " +
                "type integer, " +  /* 0接收 1发送 */
                "content varchar(255), " +
                "time timestamp)";
        sqLiteDatabase.execSQL(friendMessageSQL);
        Log.d("database", "create table friendmessage");

        String groupMessageSQL = "create table if not exists groupmessage " +
                "(mid integer primary key, " +
                "gid integer, " +
                "sid varchar(20), " +
                "type integer, " +
                "content varchar(255), " +
                "time timestamp)";
        sqLiteDatabase.execSQL(groupMessageSQL);
        Log.d("database", "create table groupmessage");

        String requestSQL = "create table if not exists request " +
                "(rid integer primary key, " +
                "receiver varchar(20), " +
                "sender varchar(20), " +
                "message varchar(255), " +
                "time timestamp, " +
                "state integer)";
        sqLiteDatabase.execSQL(requestSQL);
        Log.d("database", "create table request");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String userSQL = "drop table if exists user";
        sqLiteDatabase.execSQL(userSQL);
        String friendSQL = "drop table if exists friend";
        sqLiteDatabase.execSQL(friendSQL);
        String groupSQL = "drop table if exists group";
        sqLiteDatabase.execSQL(groupSQL);
        String groupMemberSQL = "drop table if exists groupmember";
        sqLiteDatabase.execSQL(groupMemberSQL);
        String friendMessageSQL = "drop table if exists friendmessage";
        sqLiteDatabase.execSQL(friendMessageSQL);
        String groupMessageSQL = "drop table if exists groupmessage";
        sqLiteDatabase.execSQL(groupMessageSQL);
        String requestSQL = "drop table if exists request";
        sqLiteDatabase.execSQL(requestSQL);
        onCreate(sqLiteDatabase);
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
