package com.sdu.runningsdu.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sdu.runningsdu.JavaBean.User;

/**
 * Created by FTDsm on 2018/6/5.
 */

public class SysDAO {

    private Context context;

    private SysDBHelper sysDBHelper;

    public SysDAO(Context context) {
        this.context = context;
        this.sysDBHelper = SysDBHelper.getInstance(context);
        Log.i("system database", "database name:" + sysDBHelper.getDatabaseName());
        findTable();
    }

    // 查询所有表格
    public void findTable() {
        SQLiteDatabase db = this.sysDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        Log.i("system database", "table:\n");
        while (cursor.moveToNext()) {
            Log.i("system database", cursor.getString(0));
        }
        db.close();
    }

    // 保存用户
    public void saveUser(User user) {

    }

}
