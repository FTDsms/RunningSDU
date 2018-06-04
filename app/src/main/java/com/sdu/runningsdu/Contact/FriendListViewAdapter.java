package com.sdu.runningsdu.Contact;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;

import java.util.List;

import me.zhouzhuo.zzletterssidebar.adapter.BaseSortListViewAdapter;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseViewHolder;

/**
 * Created by FTDsm on 2018/5/14.
 */

public class FriendListViewAdapter extends BaseSortListViewAdapter<Friend, FriendListViewAdapter.ViewHolder> {

    public FriendListViewAdapter(Context context, List<Friend> friends) {
        super(context, friends);
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvName = (TextView) view.findViewById(R.id.list_item_tv_name);
        return viewHolder;
    }

    @Override
    public void bindValues(ViewHolder viewHolder, int position) {
        viewHolder.tvName.setText(mDatas.get(position).getNickname());
    }

    class ViewHolder extends BaseViewHolder {
        protected TextView tvName;
    }

}
