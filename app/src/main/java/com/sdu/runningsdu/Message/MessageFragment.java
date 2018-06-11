package com.sdu.runningsdu.Message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.JavaBean.User;
import com.sdu.runningsdu.Message.Chat.GroupChatActivity;
import com.sdu.runningsdu.Utils.DataSync;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Message.Chat.ChatActivity;
import com.sdu.runningsdu.Utils.MyDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTDsm on 2018/4/17.
 */

public class MessageFragment extends Fragment {

//    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private RecyclerAdapter recyclerAdapter;

    private RecyclerView.LayoutManager layoutManager;

    private List<Message> list;

    private MyApplication myApplication;

    private MyDAO myDAO;

    private Thread refreshThread;

    private Thread syncThread1;

    private Thread syncThread5;

    public MessageFragment() {

    }

    private void initData() {
        myApplication = (MyApplication) getActivity().getApplication();
        User user = myApplication.getUser();
        myDAO = new MyDAO(getContext(), user.getName());

        list = new ArrayList<>();
        List<Group> groups = myDAO.findAllGroup();
        for (Group group : groups) {
            List<Message> messages = myDAO.findGroupMessage(group.getGid());
            list.add(messages.get(messages.size()-1));
        }
        List<Friend> friends = myDAO.findAllFriend();
        if (friends != null) {
            for(Friend friend : friends){
                List<Message> messages = myDAO.findFriendMessage(friend.getSid());
                list.add(messages.get(messages.size()-1));
            }
        }

    }

    private void initView() {
        recyclerView = getView().findViewById(R.id.recycle_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapter(list, getContext());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                boolean isGroup = list.get(position).isGroup();
                if (isGroup) {
                    // if is group, start GroupChatActivity
                    Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                    int groupGid = list.get(position).getGroup();
                    intent.putExtra("groupGid", groupGid);
                    startActivity(intent);
                } else {
                    // if not group, start ChatActivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    String friendSid = list.get(position).getFriend();
                    intent.putExtra("friendSid", friendSid);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(getActivity(),"onItemLongClick",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void refreshList() {
        refreshThread = new Thread((new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) { // 非阻塞过程中通过判断中断标志来退出
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break; // 阻塞过程捕获中断异常来退出，执行break跳出循环
                    }
                    // Refresh the list every second
                    getActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            recyclerAdapter.notifyDataSetChanged();
//                            Log.d("refresh", "refresh");
                        }
                    });
                }
            }
        }));
        refreshThread.start();
    }

    private void syncData() {
        syncThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) { // 非阻塞过程中通过判断中断标志来退出
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break; // 阻塞过程捕获中断异常来退出，执行break跳出循环
                    }
                    DataSync.syncFriendMessage(myApplication, myDAO);
                    DataSync.syncGroupMessage(myApplication, myDAO);
                }
            }
        });
        syncThread1.start();
        syncThread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) { // 非阻塞过程中通过判断中断标志来退出
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break; // 阻塞过程捕获中断异常来退出，执行break跳出循环
                    }
                    DataSync.syncFriend(myApplication, myDAO);
                    DataSync.syncGroup(myApplication, myDAO);
                    DataSync.syncRequest(myApplication, myDAO);
                }
            }
        });
        syncThread5.start();
    }

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        refreshList();
        if (!myApplication.isTest()) {
            syncData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //被销毁时终止线程
        refreshThread.interrupt();
        if (!myApplication.isTest()) {
            syncThread1.interrupt();
            syncThread5.interrupt();
        }
    }
}
