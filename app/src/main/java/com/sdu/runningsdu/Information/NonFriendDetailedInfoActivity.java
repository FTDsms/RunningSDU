package com.sdu.runningsdu.Information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sdu.runningsdu.Contact.NewFriend.VerificationActivity;
import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;
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

    private MyDAO myDAO;

    private Friend friend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_datail_info_non_friend);

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

        initData();

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NonFriendDetailedInfoActivity.this, VerificationActivity.class);
                intent.putExtra("sid", friend.getSid());
                startActivity(intent);
            }
        });

    }

    private void initData() {
        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());

        Intent intent = getIntent();
        final String sid = intent.getStringExtra("sid");
        Log.w("test", sid);

        if (!myApplication.isTest()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        friend = MyHttpClient.findUserBySid(myApplication.getIp(), sid);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    // 根据friend信息更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            headImage.setImageResource(R.drawable.head_image);
                            userName.setText(friend.getName());
                            userSid.setText(friend.getSid());
                        }
                    });
                }
            }).start();
        } else {
            friend = myDAO.findFriend(sid);
        }

    }

}
