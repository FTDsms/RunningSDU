package com.sdu.runningsdu.Contact.NewFriend;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Request;
import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.Message.RecyclerAdapter;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.DataSync;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/6/6.
 */

public class NewFriendActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarButton;

    private RecyclerView requestRecyclerView;

    private NewFriendListAdapter newFriendListAdapter;

    private RecyclerView.LayoutManager layoutManager;

    private List<Request> requests = new ArrayList<>();

    private MyApplication myApplication;

    private MyDAO myDAO;

    private Thread refreshThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_friend);

        initData();

        requestRecyclerView = findViewById(R.id.new_friend_recycler_view);
        requestRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(this);
        requestRecyclerView.setLayoutManager(layoutManager);

        newFriendListAdapter = new NewFriendListAdapter(requests, this);
        requestRecyclerView.setAdapter(newFriendListAdapter);
        newFriendListAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        toolbarBack = findViewById(R.id.new_friend_toolbar_back);
        toolbarButton = findViewById(R.id.new_friend_toolbar_button);
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
                Intent intent = new Intent(NewFriendActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

//        if (!myApplication.isTest()) {
//            initThread();
//        }

    }

    private void initData() {
        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());
        requests = myDAO.findAllRequest();
    }

    private void initThread() {
        refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break; // 阻塞过程捕获中断异常来退出，执行break跳出循环
                    }
                    DataSync.syncRequest(myApplication, myDAO);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            newFriendListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        refreshThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!myApplication.isTest()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DataSync.syncRequest(myApplication, myDAO);
                    requests.clear();
                    requests.addAll(myDAO.findAllRequest());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            newFriendListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (!myApplication.isTest()) {
//            refreshThread.interrupt();
//        }
    }
}
