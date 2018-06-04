package com.sdu.runningsdu.Contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;

import java.util.ArrayList;
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
    private TextView tvFoot;

    public ContactFragment() {

    }

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,container,false);
    }

    private void initData() {
        friends = new ArrayList<Friend>();
        friends.add(new Friend("AAA"));
        friends.add(new Friend("BBB"));
        friends.add(new Friend("CCC"));
        friends.add(new Friend("DDD"));
        friends.add(new Friend("EEE"));
        friends.add(new Friend("FFF"));
        friends.add(new Friend("GGG"));
        friends.add(new Friend("XXX"));
        friends.add(new Friend("YYY"));
        friends.add(new Friend("ZZZ"));
        friends.add(new Friend("ABC"));
        friends.add(new Friend("赵"));
        friends.add(new Friend("钱"));
        friends.add(new Friend("孙"));
        friends.add(new Friend("李"));
        friends.add(new Friend("周"));
        friends.add(new Friend("吴"));
        friends.add(new Friend("郑"));
        friends.add(new Friend("王"));
        friendListViewAdapter = new FriendListViewAdapter(getContext(), friends);
        friendListViewAdapter.updateListView(friends);
    }

    private void initView() {
        sideBar = (ZzLetterSideBar) getView().findViewById(R.id.sidebar);
        dialog = (TextView) getView().findViewById(R.id.tv_dialog);
        listView = (ListView) getView().findViewById(R.id.list_view);
        listView.setAdapter(friendListViewAdapter);

        //optional
        View header = LayoutInflater.from(getContext()).inflate(R.layout.list_item_head, null);
        listView.addHeaderView(header);

        //optional
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.list_item_foot, null);
        tvFoot = (TextView) footer.findViewById(R.id.tv_foot);
        listView.addFooterView(footer);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

}