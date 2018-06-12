package com.sdu.runningsdu.Information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by FTDsm on 2018/6/10.
 */

public class UserInfoActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarButton;

    private RelativeLayout setHeadImage;
    private CircleImageView head_image;

    private RelativeLayout getUserName;
    private TextView name;

    private RelativeLayout getUserSid;
    private TextView sid;

    private RelativeLayout getUserQRCode;

    private RelativeLayout getUserMore;

    private RelativeLayout getUserAddress;

    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info);

        myApplication = (MyApplication) getApplication();
        User user = myApplication.getUser();

        toolbarBack = findViewById(R.id.user_info_toolbar_back);
        toolbarButton = findViewById(R.id.user_info_toolbar_button);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击返回
                finish();
            }
        });
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setHeadImage = findViewById(R.id.set_user_image);
        head_image = findViewById(R.id.user_image);
        getUserName = findViewById(R.id.get_user_name);
        name = findViewById(R.id.user_name);
        getUserSid = findViewById(R.id.get_user_sid);
        sid = findViewById(R.id.user_sid);
        getUserQRCode = findViewById(R.id.user_qrcode);
        getUserMore = findViewById(R.id.user_more);
        getUserAddress = findViewById(R.id.user_address);

        head_image.setImageResource(R.drawable.head_image);
        name.setText(user.getName());
        sid.setText(user.getSid());

        setHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击上传头像

            }
        });

    }

}
