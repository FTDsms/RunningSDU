package com.sdu.runningsdu.Information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class ChatInfoActivity extends AppCompatActivity {

    private TextView toolbarBack;

    private CircleImageView headImage;
    private TextView userName;
    private ImageView addMember;
    private ImageView deleteMember;

    private MyApplication myApplication;
    private MyDAO myDAO;

    private Friend friend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_info);

        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());
        Intent intent = getIntent();
        String sid = intent.getStringExtra("friendSid");
        friend = myDAO.findFriend(sid);

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
        headImage.setImageResource(R.drawable.head_image);
        userName.setText(friend.getName());

    }

}
