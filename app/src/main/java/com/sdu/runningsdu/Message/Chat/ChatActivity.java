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

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.MyApplication;
import com.sdu.runningsdu.MyHttpClient;
import com.sdu.runningsdu.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/4/18.
 */

public class ChatActivity extends AppCompatActivity {

    private TextView toolbarBack;
    private TextView toolbarTitle;
    private TextView toolbarUser;

    private ListView msgListView;
    private EditText inputText;
    private Button send;

    private MessageAdapter adapter;

    private List<Message> messages = new ArrayList<>();
    private MyApplication myApplication;
    private String friendName;
    private Friend currentFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.actitivy_chat);

        Intent intent = getIntent();
        friendName = intent.getStringExtra("friendName");

        initMsg(); //初始化消息数据

        toolbarBack = findViewById(R.id.chat_toolbar_back);
        toolbarTitle = findViewById(R.id.chat_toolbar_title);
        toolbarUser = findViewById(R.id.chat_toolbar_user);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击返回
                finish();
            }
        });
        toolbarTitle.setText(friendName);
        toolbarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击查看好友信息

            }
        });

        adapter = new MessageAdapter(ChatActivity.this, R.layout.msg_item, messages);
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        msgListView = findViewById(R.id.mas_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Message message = new Message(false, friendName, Message.TYPE_SENT, content, "22:09");
                    messages.add(message);
                    currentFriend.setMessages(messages);
                    adapter.notifyDataSetChanged(); //当有新消息时，刷新ListView中的显示
                    msgListView.setSelection(messages.size()); //将ListView定位到最后一行
                    //TODO: send message
//                    sendMsg(content);
                    inputText.setText(""); //清空输入框中的内容
                }
            }
        });

        msgListView.setSelection(messages.size()); //将ListView定位到最后一行
    }

    private void sendMsg(String message) {
        try {
            String response = MyHttpClient.login(myApplication.getIp()+"/","", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMsg() {
        myApplication = (MyApplication) getApplication();
        List<Friend> friends = myApplication.getUser().getFriends();
        for (Friend friend : friends) {
            if (friend.getNickname().equals(friendName)) {
                currentFriend = friend;
                messages = friend.getMessages();
            }
        }

//        Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
//        msgList.add(msg1);
//        Msg msg2 = new Msg("Hello. Who is that?", Msg.TYPE_SENT);
//        msgList.add(msg2);
//        Msg msg3 = new Msg("This is Tom. Nice talking to you.", Msg.TYPE_RECEIVED);
//        msgList.add(msg3);
    }

}
