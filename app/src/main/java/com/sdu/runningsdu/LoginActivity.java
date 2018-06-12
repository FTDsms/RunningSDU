package com.sdu.runningsdu;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.JavaBean.Request;
import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.Utils.DataSync;
import com.sdu.runningsdu.Utils.MD5;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;
import com.sdu.runningsdu.Utils.MyHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity permission request.
     */
    private static final int[] REQUEST_CODE = new int[]{0x1, 0x2, 0x3, 0x4, 0x5};

    /**
     * Permission List
     * */
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //存储权限
            Manifest.permission.READ_CONTACTS, //读取联系人
            Manifest.permission.READ_PHONE_STATE, //读取手机状态
            Manifest.permission.ACCESS_FINE_LOCATION, //定位权限
            Manifest.permission.ACCESS_COARSE_LOCATION, //定位权限
            Manifest.permission.CAMERA}; //相机权限
    private List<String> permissionList;

    /**
     * UI references.
     * */
    private EditText sidView;
    private EditText passwordView;
    private View mLoginFormView;
    private View mProgressView;
    private Button loginButton;
    private TextView forgetPassword;
    private TextView register;
    private Button testButton;
    private TextView testText;

    /**
     * Application & Database Access Object
     * */
    private MyApplication myApplication;

    private MyDAO myDAO;

    /**
     * 申请权限
     * */
    private void requestForPermissions() {
        permissionList = new ArrayList<>();
        // 判断哪些权限未授予
        // 检查权限 GRANTED---授权  DINIED---拒绝
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        // 请求未授予权限
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

    }

    /**
     * 初始化数据库
     * */
    private void initDatabase() throws IOException {
        User user = myApplication.getUser();
        myDAO = new MyDAO(LoginActivity.this, user.getName());
        myDAO.findTable();
//        myDAO.deleteUser(user.getSid()); // 删除用户
        if (!myDAO.hasUser() || !myDAO.findUser(user.getSid()).getSid().equals(user.getSid())) {
            // if user not exists, add user
            myDAO.addUser(user);
        } else {
            // if user exists, update user info
            myDAO.updateUser(user);
        }
    }

    /**
     * 同步数据
     * */
    private void syncData() throws IOException, JSONException {
        DataSync.syncFriend(myApplication, myDAO);
        DataSync.syncGroup(myApplication, myDAO);
        DataSync.syncRequest(myApplication, myDAO);
        DataSync.syncFriendMessage(myApplication, myDAO);
        DataSync.syncGroupMessage(myApplication, myDAO);
    }

    private void changeBackground() {
        LinearLayout linearLayout = findViewById(R.id.login_activity);
//        linearLayout.getBackground().setAlpha(255);
        Resources resources = LoginActivity.this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.background_login);
//        drawable.setAlpha(200);
        linearLayout.setBackground(drawable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //TODO: 权限申请
        requestForPermissions();
        myApplication = (MyApplication) getApplication();

        // Set up the login form.
        sidView = (EditText) findViewById(R.id.sid);
        passwordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        forgetPassword = findViewById(R.id.forget_password);
        register = findViewById(R.id.register);
        testButton = findViewById(R.id.test_button);
        testText = findViewById(R.id.test_text);

        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sidView.setText(sp.getString("sid", null));
        passwordView.setText(sp.getString("password", null));

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        String sid = "201500301132";
//                        String password = "000000";
                        // 从输入栏获取用户名和密码 MD5加密
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sidView.setError(null);
                                passwordView.setError(null);
                            }
                        });
                        String sid = sidView.getText().toString();
                        String password = passwordView.getText().toString();
                        password = MD5.md5(password, sid);
                        if (TextUtils.isEmpty(sid)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sidView.setError("学号不能为空");
                                }
                            });
                            return;
                        }
                        if (TextUtils.isEmpty(password)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    passwordView.setError("密码不能为空");
                                }
                            });
                            return;
                        }
                        if (!isSidValid(sid)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sidView.setError("学号位数错误");
                                }
                            });
                            return;
                        }
                        User user = new User();
                        try {
                            SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                            sp.edit()
                                    .putString("sid", sidView.getText().toString())
                                    .putString("password", passwordView.getText().toString())
                                    .apply();
                            String response = MyHttpClient.login(myApplication.getIp(), sid, password);
                            Log.w("test", response);
                            JSONObject json = new JSONObject(response);

                            String flag = json.optString("flag");
                            if (!flag.equals("true")) {
                                // failure
                                String msg = json.optString("msg");
                                if (msg.equals("wrong password")) {
                                    // 密码错误，请检查密码
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                        passwordView.setError("密码错误，请检查密码");
                                        }
                                    });
                                    return;
                                }
                                if (msg.equals("no such student")) {
                                    // 用户不存在，请先注册
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                        passwordView.setError("用户不存在，请先注册");
                                        }
                                    });
                                    return;
                                }
                            } else {
                                // success
                                // 显示进度条
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showProgress(true);
                                    }
                                });

                                JSONObject userObject = json.optJSONObject("obj");
                                user.setSid(userObject.optString("sid"));
                                user.setName(userObject.optString("name"));
                                user.setPassword(userObject.optString("password"));
                                user.setImage(userObject.optString("image"));
//                                User user = initUser();
                                myApplication.setUser(user);

                                // 创建数据库
                                initDatabase();

                                // 同步数据
                                syncData();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

        forgetPassword.setOnClickListener(new OnClickListener() {
            int clickTimes = 3; // 点击次数
            int threshold = 500;
            long[] mHits = new long[clickTimes];
            @Override
            public void onClick(View view) {
                if (testButton.getVisibility() == View.GONE) {
                    Toast.makeText(LoginActivity.this, "Open the test mode", Toast.LENGTH_LONG).show();
                    testButton.setVisibility(View.VISIBLE);
                    testText.setVisibility(View.VISIBLE);
                } else if (testButton.getVisibility() == View.VISIBLE) {
                    Toast.makeText(LoginActivity.this, "Close the test mode", Toast.LENGTH_LONG).show();
                    testButton.setVisibility(View.GONE);
                    testText.setVisibility(View.GONE);
                }

//                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
//                mHits[mHits.length-1] = SystemClock.uptimeMillis();
//                if (mHits[0] >= (SystemClock. uptimeMillis()-threshold)) {
//                    if (testButton.getVisibility() == View.GONE) {
//                        Toast.makeText(LoginActivity.this, "Open the test mode", Toast.LENGTH_LONG).show();
//                        testButton.setVisibility(View.VISIBLE);
//                        testText.setVisibility(View.VISIBLE);
//                    }
//                    if (testButton.getVisibility() == View.VISIBLE) {
//                        Toast.makeText(LoginActivity.this, "Close the test mode", Toast.LENGTH_LONG).show();
//                        testButton.setVisibility(View.GONE);
//                        testText.setVisibility(View.GONE);
//                    }
//                }
            }
        });

        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sidView.setText("20150030");
                passwordView.setText("000000");
            }
        });

        testButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 初始化数据
                            User user = initUser();
                            myApplication.setUser(user);
                            myApplication.setTest(true);

                            // 创建数据库
                            initDatabase();

                            // 写入测试数据库
                            initData();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });

    }

    // 初始化用户
    private User initUser() {
        return new User("201500301182", "邵明山", MD5.md5("000000", "201500301182"), null);
    }

    // 写入测试数据库
    private void initData() {
        User user = myApplication.getUser();
        // 初始化好友
        List<Friend> friends = new ArrayList<>();
        friends.add(new Friend("201500301132", "焦方锴", null));
        friends.add(new Friend("201500302002","叶蕴盈", null));
        for(int i=0; i<10; ++i) {
            friends.add(new Friend("test"+(i+1), "test"+(i+1), null));
        }
        for (Friend friend : friends) {
            if (!myDAO.hasFriend(friend.getSid())) {
                myDAO.addFriend(friend);
            } else {
                myDAO.updateFriend(friend);
            }
        }

        // 初始化好友消息
        int fmid = 0;
        for (Friend friend : friends) {
            List<Message> messages = new ArrayList<>();
            for(int j=0; j<10; ++j) {
                Message message = new Message(fmid, friend.getSid(), Message.TYPE_RECEIVED, "test"+(j+1), "21:07");
                if (!myDAO.hasFriendMessage(message)) {
                    myDAO.addFriendMessage(message);
                }
                messages.add(message);
                fmid ++;
            }
            friend.setUnread(friend.getUnread()+messages.size()); //设置未读消息
            myDAO.updateFriendUnread(friend);
        }

        // 初始化群组
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1,"创新实训", user.getSid(), null));
        for (Group group : groups) {
            if (!myDAO.hasGroup(group.getGid())) {
                myDAO.addGroup(group);
            } else {
                myDAO.updateGroup(group);
            }
        }

        // 初始化群组成员
        for (Group group : groups) {
            List<String> members = new ArrayList<>();
            members.add("201500301182");
            members.add("201500301132");
            members.add("201500302002");
            for (String member : members) {
                if (!myDAO.hasGroupMember(group.getGid(), member)) {
                    myDAO.addGroupMember(group.getGid(), member);
                }
            }
        }

        // 初始化群组消息
        int gmid = 0;
        for (Group group : groups) {
            List<Message> messages = new ArrayList<>();
            List<String> members = myDAO.findGroupMember(group.getGid());
            for (String member: members) {
                Message message;
                if (member.equals(user.getSid())) {
                    message = new Message(gmid, user.getSid(), Message.TYPE_SENT, "test", "16:27");
                } else {
                    message = new Message(gmid, member, Message.TYPE_RECEIVED, "test", "16:32");
                }
                messages.add(message);
                if (!myDAO.hasGroupMessage(message)) {
                    myDAO.addGroupMessage(message);
                }
                gmid ++;
            }
            group.setUnread(group.getUnread()+messages.size()); //设置未读消息
            myDAO.updateGroupUnread(group);
        }

        // 初始化好友请求
        List<Request> requests = new ArrayList<>();
        Request request1 = new Request(-1, user.getSid(), "20150030xxx1", "你好，很高兴认识你", "time", 0);
        Request request2 = new Request(-2, user.getSid(), "20150030xxx2", "你好，很高兴认识你", "time", 0);
        requests.add(request1);
        requests.add(request2);
        for (Request request : requests) {
            if (!myDAO.hasRequest(request.getRid())) {
                myDAO.addRequest(request);
            } else {
                myDAO.updateRequest(request);
            }
        }

    }

    private boolean isSidValid(String sid) {
        return sid.length() == 12;
    }

    private boolean isPasswordValid(String password) {
        // TODO: check is password is valid
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Callback received when a PERMISSIONS request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                for (int i=0; i<grantResults.length; ++i) {
                    if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        // 用户拒绝授权
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            // 用户已经禁止提示，提示开启
                            Toast.makeText(this, "您已禁止权限，需要重新开启。", Toast.LENGTH_SHORT).show();
                        } else {
                            // 重新申请权限
                            requestForPermissions();
                        }
                    }
                }
                break;
        }
    }

}

