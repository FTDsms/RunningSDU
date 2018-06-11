package com.sdu.runningsdu.Information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyHttpClient;

import org.json.JSONException;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by FTDsm on 2018/6/10.
 */

public class NonFriendDetailedInfoActivity extends AppCompatActivity{

    private TextView toolbarBack;
    private CircleImageView headImage;
    private TextView userName;
    private TextView userSid;
    private Button addFriend;

    private MyApplication myApplication;

    private Friend friend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_datail_info_non_friend);

        initData();

        toolbarBack = findViewById(R.id.detail_info_toolbar_back);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击返回
                finish();
            }
        });

        headImage = findViewById(R.id.user_image);
        userName = findViewById(R.id.user_name);
        userSid = findViewById(R.id.user_sid);
        addFriend = findViewById(R.id.add_friend);

        try {
            headImage.setImageResource(R.drawable.head_image);
            userName.setText(friend.getName());
            userSid.setText(friend.getSid());
        } catch (NullPointerException e) {
            // Maybe network disconnect
            // ignore
        }

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 发送好友请求
                            MyHttpClient.addFriendRequest(myApplication.getIp(), friend.getSid(), myApplication.getUser().getSid());
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    private void initData() {
        myApplication = (MyApplication) getApplication();

        Intent intent = getIntent();
        final String sid = intent.getStringExtra("sid");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    friend = MyHttpClient.findUserBySid(myApplication.getIp(), sid);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
