package com.sdu.runningsdu.Message.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sdu.runningsdu.Information.GroupChatInfoActivity;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.Utils.DataSync;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;
import com.sdu.runningsdu.Utils.MyHttpClient;
import com.sdu.runningsdu.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by FTDsm on 2018/6/3.
 */

public class GroupChatActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarTitle;
    private TextView toolbarUser;

    private ListView msgListView;
    private EditText inputText;
    private Button send;

    private GroupMessageAdapter adapter;

    private List<Message> messages = new ArrayList<>();
    private MyApplication myApplication;
    private int groupGid;
    private Group currentGroup;

    private MyDAO myDAO;

    private ScheduledThreadPoolExecutor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_group);

        Intent intent = getIntent();
        groupGid = intent.getIntExtra("groupGid", -1);

        initMsg(); //初始化消息数据

        toolbarBack = findViewById(R.id.chat_toolbar_back);
        toolbarTitle = findViewById(R.id.chat_toolbar_title);
        toolbarUser = findViewById(R.id.chat_toolbar_group);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击返回
                finish();
            }
        });
        toolbarTitle.setText(currentGroup.getName());
        toolbarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击查看群聊信息
                Intent intent1 = new Intent(GroupChatActivity.this, GroupChatInfoActivity.class);
                intent1.putExtra("groupGid", groupGid);
                startActivity(intent1);
            }
        });

        adapter = new GroupMessageAdapter(GroupChatActivity.this, R.layout.msg_item_group, messages, myApplication);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        msgListView = findViewById(R.id.mas_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Message message = new Message(-1, groupGid, null,  Message.TYPE_SENT, content, "22:09");
                    messages.add(message);
                    adapter.notifyDataSetChanged(); //当有新消息时，刷新ListView中的显示
                    msgListView.setSelection(messages.size()); //将ListView定位到最后一行
                    if (!myApplication.isTest()) {
                        sendMsg(message);
                    }
                    inputText.setText(""); //清空输入框中的内容
                }
            }
        });

        msgListView.setSelection(messages.size()); //将ListView定位到最后一行

        if (!myApplication.isTest()) {
            initThreadPool();
        }

    }

    private void sendMsg(final Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyHttpClient.sendGroupMessage(myApplication.getIp()+"/",message.getGroup(), myApplication.getUser().getSid(), "0", "", "", message.getContent());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initMsg() {
        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());
        currentGroup = myDAO.findGroup(groupGid);
        messages = myDAO.findGroupMessage(currentGroup.getGid());
    }

    private void initThreadPool() {
        Runnable refreshList = new Runnable() {
            @Override
            public void run() {
                Log.e("runnable", "refreshList");
                DataSync.syncGroupMessage(myApplication, myDAO);
                messages.clear();
                messages.addAll(myDAO.findGroupMessage(groupGid));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        executor = new ScheduledThreadPoolExecutor(4);
        executor.scheduleWithFixedDelay(refreshList, 1000, 1000, TimeUnit.MILLISECONDS);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 被销毁时关闭线程池
        if (!myApplication.isTest()) {
            executor.shutdown();
        }
        // 将未读消息数设为0
        Group group = myDAO.findGroup(groupGid);
        group.setUnread(0);
        myDAO.updateGroupUnread(group);
    }


}
