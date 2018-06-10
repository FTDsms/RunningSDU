package com.sdu.runningsdu.Contact.GroupList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class GroupListActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarButton;

    private ListView groupListView;

    private TextView foot;

    private GroupListAdapter groupListAdapter;

    private List<Group> groups = new ArrayList<>();

    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group_list);

        initData();

        toolbarBack = findViewById(R.id.group_list_toolbar_back);
        toolbarButton = findViewById(R.id.group_list_toolbar_button);
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
                // 打开添加好友Activity
//                Intent intent = new Intent(NewFriendActivity.this, xxx.class);
            }
        });

        groupListView = findViewById(R.id.group_list_view);

        View footer = LayoutInflater.from(this).inflate(R.layout.list_item_foot, null);
        foot = footer.findViewById(R.id.tv_foot);
        groupListView.addFooterView(footer, null, false);
        foot.setText(groups.size() + "个群聊");

    }

    private void initData() {
        myApplication = (MyApplication) getApplication();
        groups = myApplication.getUser().getGroups();
        groupListAdapter = new GroupListAdapter(this, groups);
        groupListAdapter.updateListView(groups);
    }

}
