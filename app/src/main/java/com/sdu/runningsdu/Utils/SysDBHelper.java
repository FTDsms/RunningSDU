package com.sdu.runningsdu.Utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by FTDsm on 2018/6/5.
 */

public class SysDBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "sys.db";
    private static final int DB_VERSION = 1;
    private static SysDBHelper sysDBHelper;

    public SysDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SysDBHelper getInstance(Context context) {
        // 单例模式
        if (sysDBHelper == null) {
            sysDBHelper = new SysDBHelper(context);
        }
        return sysDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String userSQL = "create table if not exists user " +
                " (sid varchar(20) primary key, " +
                "name varchar(255), " +
                "password varchar(255), " +
                "image varchar(255), " +
                "latest integer)";
        sqLiteDatabase.execSQL(userSQL);
        Log.d("database", "create table user");

        String imageSQL = "create table if not exists image " +
                "(id varchar(20) primary key, " +
                "path varchar(255), " +
                "password varchar(255), " +
                "image varchar(255))";
        sqLiteDatabase.execSQL(imageSQL);
        Log.d("database", "create table image");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String userSQL = "drop table if exists user";
        sqLiteDatabase.execSQL(userSQL);
        String imageSQL = "drop table if exists image";
        sqLiteDatabase.execSQL(userSQL);
    }
}
