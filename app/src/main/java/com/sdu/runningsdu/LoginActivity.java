package com.sdu.runningsdu;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
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

import static android.Manifest.permission.READ_CONTACTS;

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
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

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

    //发送登录请求
    private User sendRequest() {
        User user = new User();
        String sid = "201500301132";
        String password = "000000";
        String response;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sid", sid);
            jsonObject.put("password", password);
//            jsonObject.put("password", MD5.md5(password));
            Log.e("test", jsonObject.toString());

            response = MyHttpClient.login(myApplication.getIp(), sid, password);
            Log.e("test", response);

            JSONObject json = new JSONObject(response);
            String flag = json.getString("flag");
            if (flag.equals("true")) {

            }
            Log.e("test", json.getString("flag"));
            Log.e("test", json.optString("flag"));

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    //初始化数据库
    private void initDatabase() {
        User user = myApplication.getUser();
        myDAO = new MyDAO(LoginActivity.this, user.getName());
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
        Resources resources = LoginActivity.this.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.background_login);
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
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                attemptLogin();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        //TODO: 从输入栏获取用户名和密码 MD5加密
                        String sid = "201500301132";
                        String password = "000000";
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
                                    //TODO 密码错误 请检查密码
                                    return;
                                }
                                if (msg.equals("no such student")) {
                                    //TODO 用户不存在 请先注册
                                    return;
                                }
                            } else {
                                // success
                                JSONObject userObject = json.optJSONObject("obj");
                                user.setSid(userObject.optString("sid"));
                                user.setName(userObject.optString("name"));
                                user.setPassword(userObject.optString("password"));
                                user.setImage(userObject.optString("image"));
//                                User user1 = sendRequest();
//                                User user = initData();
                                myApplication.setUser(user);

                                // 创建数据库
                                initDatabase();

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

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

//        showProgress(true);

    }

//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
//    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

