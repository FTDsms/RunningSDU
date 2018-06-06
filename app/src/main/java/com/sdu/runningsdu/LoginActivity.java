package com.sdu.runningsdu;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
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
import com.sdu.runningsdu.JavaBean.User;
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
    private static final int REQUEST_READ_CONTACTS = 0x0;

    private static final int REQUEST_STORAGE = 0x1;

    // UI references.
    private EditText sidView;
    private EditText passwordView;
    private View mLoginFormView;
    private View mProgressView;
    private Button loginButton;
    private TextView forgetPassword;
    private TextView register;
    private Button testButton;
    private TextView testText;


    private MyApplication myApplication;

    private MyDAO myDAO;

    //初始化默认数据
    private User initData() {
        User user = new User("201500301182", "邵明山", "000000", null);
//        User user = new User();
//        user.setName("邵明山");

        List<Group> groups = new ArrayList<>();
        Group group = new Group("创新实训");
        List<String> members = new ArrayList<>();
        members.add("邵明山");
        members.add("焦方锴");
        members.add("叶蕴盈");
        group.setMembers(members);
        List<Message> groupMessages = new ArrayList<>();
        for (int i=0; i<members.size(); ++i) {
            Message message;
            if (members.get(i).equals(user.getName())) {
                message = new Message(true, "创新实训", members.get(i), Message.TYPE_SENT, "test", "15:00");
            } else {
                message = new Message(true, "创新实训", members.get(i), Message.TYPE_RECEIVED, "test", "15:00");
            }
            groupMessages.add(message);
        }
        group.setMessages(groupMessages);
        groups.add(group);
        user.setGroups(groups);

        List<Friend> friends = new ArrayList<>();
        Friend friend1 = new Friend("焦方锴");
        List<Message> messages1 = new ArrayList<>();
        for(int j=0; j<10; ++j) {
            Message message1 = new Message(false, friend1.getNickname(), Message.TYPE_RECEIVED, "test"+(j+1), "21:07");
            messages1.add(message1);
        }
        friend1.setMessages(messages1);
        friends.add(friend1);

        Friend friend2 = new Friend("叶蕴盈");
        List<Message> messages2 = new ArrayList<>();
        for(int j=0; j<10; ++j) {
            Message message2 = new Message(false, friend2.getNickname(), Message.TYPE_RECEIVED, "test"+(j+1), "21:07");
            messages2.add(message2);
        }
        friend2.setMessages(messages2);
        friends.add(friend2);

        Friend friend;
        for(int i=0; i<10; ++i) {
            friend = new Friend("test"+(i+1));
            List<Message> messages = new ArrayList<>();
            for(int j=0; j<10; ++j) {
                Message message = new Message(false, friend.getNickname(), Message.TYPE_RECEIVED, "test"+(j+1), "21:07");
                messages.add(message);
            }
            friend.setMessages(messages);
            friends.add(friend);
        }

        user.setFriends(friends);
        return user;
    }

    // 申请权限
    private void requestForPermissions() {
        //检查权限
        //存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //已经禁止提示了
                Toast.makeText(LoginActivity.this, "您已禁止存储权限，需要重新开启。", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
            }
        } else {
            // have permission
        }
        //读取联系人
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
    }

    //初始化数据库
    private void initDatabase() {
        User user = myApplication.getUser();
        myDAO = new MyDAO(LoginActivity.this, user.getName());
//        myDAO.deleteUser(user.getSid()); // 删除用户
        if (!myDAO.hasUser() || !myDAO.findUser(user.getSid()).getSid().equals(user.getSid())) {
            // if user not exists, add user
            myDAO.addUser(user);
        } else {
            // if user exists, print user information
            Log.i("user: ", myDAO.findUser(user.getSid()).toString());
        }
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
        register = findViewById(R.id.register);
        testButton = findViewById(R.id.test_button);
        testText = findViewById(R.id.test_text);

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
//                                User user = initData();
                                myApplication.setUser(user);

                                // 创建数据库
                                initDatabase();

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
                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock. uptimeMillis()-threshold)) {
                    if (testButton.getVisibility() == View.GONE) {
                        Toast.makeText(LoginActivity.this, "Open the test mode", Toast.LENGTH_LONG).show();
                        testButton.setVisibility(View.VISIBLE);
                        testText.setVisibility(View.VISIBLE);
                    }
                    if (testButton.getVisibility() == View.VISIBLE) {
                        Toast.makeText(LoginActivity.this, "Close the test mode", Toast.LENGTH_LONG).show();
                        testButton.setVisibility(View.GONE);
                        testText.setVisibility(View.GONE);
                    }
                }
            }
        });

        testButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                User user = new User();
            }
        });

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
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_STORAGE:
                if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                }else{
                    //用户拒绝授权
                }
                break;
            case REQUEST_READ_CONTACTS:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }

}

