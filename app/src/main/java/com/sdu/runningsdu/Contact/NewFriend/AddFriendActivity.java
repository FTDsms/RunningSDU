package com.sdu.runningsdu.Contact.NewFriend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sdu.runningsdu.R;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class AddFriendActivity extends AppCompatActivity {

    private TextView toolbarBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);

        toolbarBack = findViewById(R.id.group_list_toolbar_back);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击返回
                finish();
            }
        });

    }

}
