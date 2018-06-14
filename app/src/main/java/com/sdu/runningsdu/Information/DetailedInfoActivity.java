package com.sdu.runningsdu.Information;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.Message.Chat.ChatActivity;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class DetailedInfoActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private CircleImageView headImage;
    private TextView userName;
    private TextView userSid;
    private Button sendMessage;
    private Button deleteFriend;

    private MyApplication myApplication;
    private MyDAO myDAO;

    private Friend friend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_info);

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
        sendMessage = findViewById(R.id.send_message);
        deleteFriend = findViewById(R.id.delete_friend);

        byte[] bytes = myDAO.findFriendImage(friend.getSid());
        headImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        userName.setText(friend.getName());
        userSid.setText(friend.getSid());

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开好友聊天界面
                Intent intent =  new Intent(DetailedInfoActivity.this, ChatActivity.class);
                intent.putExtra("friendSid", friend.getSid());
                startActivity(intent);
            }
        });

        deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除好友
            }
        });

    }

    private void initData() {
        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());

        Intent intent = getIntent();
        String sid = intent.getStringExtra("sid");

        friend = myDAO.findFriend(sid);

    }

}
