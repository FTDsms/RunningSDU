package com.sdu.runningsdu.Contact.GroupList;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhouzhuo.zzletterssidebar.adapter.BaseSortListViewAdapter;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseViewHolder;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class GroupListAdapter extends BaseSortListViewAdapter<Group, GroupListAdapter.ViewHolder> {

    public GroupListAdapter(Context context, List<Group> groups) {
        super(context, groups);
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_group;
    }

    @Override
    public GroupListAdapter.ViewHolder getViewHolder(View view) {
        GroupListAdapter.ViewHolder viewHolder = new GroupListAdapter.ViewHolder();
        viewHolder.image = view.findViewById(R.id.list_item_group_image);
        viewHolder.groupName = view.findViewById(R.id.list_item_group_image);
        return viewHolder;
    }

    @Override
    public void bindValues(GroupListAdapter.ViewHolder viewHolder, int position) {
        //TODO viewHolder.image.setImageBitmap();
        viewHolder.image.setImageResource(R.drawable.head_image);
        viewHolder.groupName.setText(mDatas.get(position).getName());
    }

    class ViewHolder extends BaseViewHolder {
        protected CircleImageView image;
        protected TextView groupName;
    }

}
