package com.sdu.runningsdu.Contact.GroupList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import java.util.List;

import me.zhouzhuo.zzletterssidebar.ZzLetterSideBar;
import me.zhouzhuo.zzletterssidebar.interf.OnLetterTouchListener;

/**
 * Created by FTDsm on 2018/6/12.
 */

public class CreateGroupActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarButton;
    private SearchView searchView;
    private RelativeLayout search;

    private ListView listView;
    private ZzLetterSideBar sideBar;
    private TextView dialog;


    private CreateGroupFriendListAdapter createGroupFriendListAdapter;
    private List<Friend> friends;

    private MyApplication myApplication;
    private MyDAO myDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_group);

        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());
        friends = myDAO.findAllFriend();

        createGroupFriendListAdapter = new CreateGroupFriendListAdapter(this, friends);
        createGroupFriendListAdapter.updateListView(friends);

        toolbarBack = findViewById(R.id.create_group_toolbar_back);
        toolbarButton = findViewById(R.id.create_group_toolbar_button);
        searchView = findViewById(R.id.search_view);
        search = findViewById(R.id.search);
        sideBar = findViewById(R.id.sidebar);
        dialog = findViewById(R.id.tv_dialog);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(createGroupFriendListAdapter);

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回
                finish();
            }
        });
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建群聊

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        sideBar.setLetterTouchListener(listView, createGroupFriendListAdapter, dialog, new OnLetterTouchListener() {
            @Override
            public void onLetterTouch(String letter, int position) {

            }

            @Override
            public void onActionUp() {

            }
        });

    }

}
