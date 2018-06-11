package com.sdu.runningsdu.Message.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

    private Thread refreshThread;

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
                    currentGroup.setMessages(messages);
                    adapter.notifyDataSetChanged(); //当有新消息时，刷新ListView中的显示
                    msgListView.setSelection(messages.size()); //将ListView定位到最后一行
                    //TODO: send message
                    sendMsg(message);
                    inputText.setText(""); //清空输入框中的内容
                }
            }
        });

        msgListView.setSelection(messages.size()); //将ListView定位到最后一行

        refreshList();
    }

    private void sendMsg(Message message) {
        try {
            MyHttpClient.sendGroupMessage(myApplication.getIp()+"/",message.getGroup(), myApplication.getUser().getSid(), "0", message.getContent());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMsg() {
        myApplication = (MyApplication) getApplication();
        myDAO = new MyDAO(this, myApplication.getUser().getName());
        currentGroup = myDAO.findGroup(groupGid);
        messages = currentGroup.getMessages();
    }

    private void refreshList() {
        refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(1000);
                        DataSync.syncGroupMessage(myApplication, myDAO);
                        messages.clear();
                        messages.addAll(myDAO.findGroupMessage(groupGid));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            msgListView.setSelection(messages.size());
                        }
                    });
                }
            }
        });
        refreshThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshThread.interrupt();
    }


}
