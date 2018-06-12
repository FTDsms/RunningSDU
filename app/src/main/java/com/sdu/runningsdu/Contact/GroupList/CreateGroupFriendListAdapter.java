package com.sdu.runningsdu.Contact.GroupList;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhouzhuo.zzletterssidebar.adapter.BaseSortListViewAdapter;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseViewHolder;

/**
 * Created by FTDsm on 2018/6/12.
 */

public class CreateGroupFriendListAdapter extends BaseSortListViewAdapter<Friend, CreateGroupFriendListAdapter.ViewHolder> {

    private Context context;

    public CreateGroupFriendListAdapter(Context context, List<Friend> friends) {
        super(context, friends);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_create_group;
    }

    @Override
    public ViewHolder getViewHolder(final View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.friend = view.findViewById(R.id.list_item_friend);
        viewHolder.image = view.findViewById(R.id.list_item_head_image);
        viewHolder.tvName = view.findViewById(R.id.list_item_tv_name);
        viewHolder.checkBox = view.findViewById(R.id.list_item_checkbox);
        return null;
    }

    @Override
    public void bindValues(ViewHolder viewHolder, final int position) {
        viewHolder.image.setImageResource(R.drawable.head_image);
        viewHolder.tvName.setText(mDatas.get(position).getName());
        viewHolder.friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class ViewHolder extends BaseViewHolder {
        protected LinearLayout friend;
        protected CircleImageView image;
        protected TextView tvName;
        protected CheckBox checkBox;
    }

}
