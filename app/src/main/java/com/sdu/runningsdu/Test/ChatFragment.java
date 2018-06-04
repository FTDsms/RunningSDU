package com.sdu.runningsdu.Test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdu.runningsdu.R;

/**
 * Created by FTDsm on 2018/4/16.
 */

public class ChatFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ChatFragmentPagerAdapter chatFragmentPagerAdapter;

    private TabLayout.Tab tabMessage, tabFriend, tabGroup;

    public ChatFragment() {
        // Required empty public constructor
    }

    private void initView() {
        //使用适配器将ViewPager与Fragment绑定在一起
        viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        chatFragmentPagerAdapter = new ChatFragmentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(chatFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        tabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //指定Tab位置
        tabMessage = tabLayout.getTabAt(0);
        tabFriend = tabLayout.getTabAt(1);
        tabGroup = tabLayout.getTabAt(2);
    }

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

}
