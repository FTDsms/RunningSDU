package com.sdu.runningsdu.Contact.NewFriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.ClearEditText;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyHttpClient;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by FTDsm on 2018/6/11.
 */

public class VerificationActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toobarButton;
    private ClearEditText clearEditText;

    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verification);

        myApplication = (MyApplication) getApplication();
        Intent intent = getIntent();
        final String sid = intent.getStringExtra("sid");

        toolbarBack = findViewById(R.id.verification_toolbar_back);
        toobarButton = findViewById(R.id.verification_toolbar_button);
        clearEditText = findViewById(R.id.verification_message);

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击返回
                finish();
            }
        });
        toobarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!myApplication.isTest()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 发送好友请求
                                MyHttpClient.addFriendRequest(myApplication.getIp(), sid, myApplication.getUser().getSid(), clearEditText.getText().toString());
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Toast.makeText(VerificationActivity.this, "已发送验证申请，等待对方通过", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VerificationActivity.this, "已发送验证申请，等待对方通过\n"+clearEditText.getText().toString(), Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });



    }

}
