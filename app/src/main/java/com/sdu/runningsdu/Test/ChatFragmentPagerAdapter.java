package com.sdu.runningsdu.Test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sdu.runningsdu.Message.MessageFragment;

/**
 * Created by FTDsm on 2018/4/17.
 */

public class ChatFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] pageTitles = {"消息", "好友", "群组"};

    private MessageFragment messageFragment;
    private FriendFragment friendFragment;
    private GroupFragment groupFragment;

    public ChatFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (messageFragment == null){
                    messageFragment = new MessageFragment();
                }
                return messageFragment;
            case 1:
                if (friendFragment == null){
                    friendFragment = new FriendFragment();
                }
                return friendFragment;
            case 2:
                if (groupFragment == null){
                    groupFragment = new GroupFragment();
                }
                return groupFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return pageTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }
}
