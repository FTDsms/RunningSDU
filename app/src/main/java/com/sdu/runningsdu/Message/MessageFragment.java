package com.sdu.runningsdu.Message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.sdu.runningsdu.Utils.MyHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private ScheduledThreadPoolExecutor executor;

    public MessageFragment() {

    }

    private void initData() {
        myApplication = (MyApplication) getActivity().getApplication();
        User user = myApplication.getUser();
        myDAO = new MyDAO(getContext(), user.getName());

        list = new ArrayList<>();
        syncList();

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
                    // 将未读消息数设为0
                    Group group = myDAO.findGroup(groupGid);
                    group.setUnread(0);
                    myDAO.updateGroupUnread(group);
                    intent.putExtra("groupGid", groupGid);
                    startActivity(intent);
                } else {
                    // if not group, start ChatActivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    String friendSid = list.get(position).getFriend();
                    // 将未读消息数设为0
                    Friend friend = myDAO.findFriend(friendSid);
                    friend.setUnread(0);
                    myDAO.updateFriendUnread(friend);
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

    private void syncList() {
        // TODO: sort
        list.clear();
        List<Group> groups = myDAO.findAllGroup();
        for (Group group : groups) {
            List<Message> messages = myDAO.findGroupMessage(group.getGid());
            if (!messages.isEmpty()) {
                list.add(messages.get(messages.size()-1));
            }
        }
        List<Friend> friends = myDAO.findAllFriend();
        if (friends != null) {
            for(Friend friend : friends){
                List<Message> messages = myDAO.findFriendMessage(friend.getSid());
                if (!messages.isEmpty()) {
                    list.add(messages.get(messages.size()-1));
                }
            }
        }
    }

    private void initThreadPool() {
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                Log.e("runnable", "sync Message");
                DataSync.syncFriendMessage(myApplication, myDAO);
                DataSync.syncGroupMessage(myApplication, myDAO);
            }
        };
        Runnable runnable5 = new Runnable() {
            @Override
            public void run() {
                Log.e("runnable", "sync Friend & Group");
                DataSync.syncFriend(myApplication, myDAO);
                DataSync.syncGroup(myApplication, myDAO);
                DataSync.syncRequest(myApplication, myDAO);
            }
        };
        Runnable refreshList = new Runnable() {
            @Override
            public void run() {
                Log.e("runnable", "refreshList");
                syncList();
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        executor = new ScheduledThreadPoolExecutor(4);
        executor.scheduleWithFixedDelay(runnable1, 0, 1000, TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(runnable5, 0, 5000, TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(refreshList, 1000, 1000, TimeUnit.MILLISECONDS);
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
        if (!myApplication.isTest()) {
            initThreadPool();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 被销毁时关闭线程池
        if (!myApplication.isTest()) {
            executor.shutdown();
        }
    }
}
