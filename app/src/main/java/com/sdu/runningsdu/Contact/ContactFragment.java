package com.sdu.runningsdu.Contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdu.runningsdu.Contact.GroupList.GroupListActivity;
import com.sdu.runningsdu.Contact.SubPage.LabelActivity;
import com.sdu.runningsdu.Contact.NewFriend.NewFriendActivity;
import com.sdu.runningsdu.Contact.SubPage.SubscriptionActivity;
import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.DataSync;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import java.util.List;

import me.zhouzhuo.zzletterssidebar.ZzLetterSideBar;
import me.zhouzhuo.zzletterssidebar.interf.OnLetterTouchListener;


/**
 * Created by FTDsm on 2018/5/14.
 */

public class ContactFragment extends Fragment {

    private ListView listView;
    private ZzLetterSideBar sideBar;
    private TextView dialog;

    private FriendListViewAdapter friendListViewAdapter;

    private List<Friend> friends;

    private LinearLayout newFriend;
    private LinearLayout groupChat;
    private LinearLayout label;
    private LinearLayout subscription;
    private TextView tvFoot;

    private MyApplication myApplication;

    private MyDAO myDAO;

    private Thread refreshThread;

    public ContactFragment() {

    }

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,container,false);
    }

    private void initData() {
        myApplication = (MyApplication) getActivity().getApplication();
        myDAO = new MyDAO(getContext(), myApplication.getUser().getName());
        friends = myDAO.findAllFriend();

        friendListViewAdapter = new FriendListViewAdapter(getActivity(), friends);
        friendListViewAdapter.updateListView(friends);
    }

    private void initView() {
        sideBar = (ZzLetterSideBar) getView().findViewById(R.id.sidebar);
        dialog = (TextView) getView().findViewById(R.id.tv_dialog);
        listView = (ListView) getView().findViewById(R.id.list_view);
        listView.setAdapter(friendListViewAdapter);

        // header
        View header = LayoutInflater.from(getContext()).inflate(R.layout.list_item_head, null);
        newFriend = header.findViewById(R.id.new_friend);
        groupChat = header.findViewById(R.id.group_chat);
        label = header.findViewById(R.id.label);
        subscription = header.findViewById(R.id.subscription);
        listView.addHeaderView(header);

        newFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                startActivity(intent);
            }
        });
        groupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GroupListActivity.class);
                startActivity(intent);
            }
        });
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LabelActivity.class);
                startActivity(intent);
            }
        });
        subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
                startActivity(intent);
            }
        });

        // footer
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.list_item_foot, null);
        tvFoot = (TextView) footer.findViewById(R.id.tv_foot);
        listView.addFooterView(footer, null, false);

        tvFoot.setText(friends.size() + "位联系人");

        sideBar.setLetterTouchListener(listView, friendListViewAdapter, dialog, new OnLetterTouchListener() {
            @Override
            public void onLetterTouch(String letter, int position) {

            }

            @Override
            public void onActionUp() {

            }
        });



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
                    DataSync.syncFriend(myApplication, myDAO);
                    DataSync.syncFriendImage(myApplication, myDAO);
                    friends.clear();
                    friends.addAll(myDAO.findAllFriend());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            friendListViewAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        refreshThread.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
//        if (!myApplication.isTest()) {
//            initThread();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!myApplication.isTest()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DataSync.syncFriend(myApplication, myDAO);
                    DataSync.syncFriendImage(myApplication, myDAO);
                    friends.clear();
                    friends.addAll(myDAO.findAllFriend());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            friendListViewAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (!myApplication.isTest()) {
//            refreshThread.interrupt();
//        }
    }

}
