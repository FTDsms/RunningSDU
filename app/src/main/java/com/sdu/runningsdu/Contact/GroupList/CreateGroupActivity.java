package com.sdu.runningsdu.Contact.GroupList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.Message.Chat.GroupChatActivity;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;
import com.sdu.runningsdu.Utils.MyHttpClient;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/6/12.
 */

public class CreateGroupActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarButton;
    private SearchView searchView;
    private RelativeLayout search;

    private ListView listView;

    private CreateGroupFriendListAdapter createGroupFriendListAdapter;
    private List<Friend> friends;

    private MyApplication myApplication;
    private MyDAO myDAO;

    private List<String> memberSid = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_group);

        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());
        friends = myDAO.findAllFriend();

        createGroupFriendListAdapter = new CreateGroupFriendListAdapter(this, R.layout.list_item_create_group, friends);

        toolbarBack = findViewById(R.id.create_group_toolbar_back);
        toolbarButton = findViewById(R.id.create_group_toolbar_button);
        searchView = findViewById(R.id.search_view);
        search = findViewById(R.id.search);
        listView = findViewById(R.id.group_list_view);
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
                memberSid.clear();
                memberSid.add(myApplication.getUser().getSid());
                for (int i=0; i<createGroupFriendListAdapter.checked.size(); ++i) {
                    if (createGroupFriendListAdapter.checked.get(i)) {
                        memberSid.add(friends.get(i).getSid());
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Group group = MyHttpClient.createGroup(myApplication.getIp(), "群聊", memberSid);
                            Intent intent = new Intent(CreateGroupActivity.this, GroupChatActivity.class);
                            intent.putExtra("groupGid", group.getGid());
                            startActivity(intent);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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


    }

}
