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
import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class AddFriendActivity extends AppCompatActivity {

    private TextView toolbarBack;

    private RelativeLayout search;

    private SearchView searchView;

    private TextView mySid;

    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);

        myApplication = (MyApplication) getApplication();
        User user = myApplication.getUser();

        toolbarBack = findViewById(R.id.add_friend_toolbar_back);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击返回
                finish();
            }
        });

        searchView = findViewById(R.id.search_view);
        search = findViewById(R.id.search);
        mySid = findViewById(R.id.my_sid);

        mySid.setText("我的学号："+user.getSid());

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFriendActivity.this, SearchResultActivity.class);
                startActivity(intent);
            }
        });

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(AddFriendActivity.this, SearchResultActivity.class);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

}
