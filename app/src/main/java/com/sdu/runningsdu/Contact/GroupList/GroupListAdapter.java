package com.sdu.runningsdu.Contact.GroupList;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.Message.Chat.GroupChatActivity;
import com.sdu.runningsdu.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhouzhuo.zzletterssidebar.adapter.BaseSortListViewAdapter;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseViewHolder;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class GroupListAdapter extends ArrayAdapter<Group> {

    private int resourceId;

    private Context context;

    public GroupListAdapter(Context context, int textViewResourceId, List<Group> groups) {
        super(context, textViewResourceId, groups);
        resourceId = textViewResourceId;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Group group = getItem(position);
        View view;
        GroupListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new GroupListAdapter.ViewHolder();
            viewHolder.group = view.findViewById(R.id.list_item_group);
            viewHolder.image = view.findViewById(R.id.list_item_group_image);
            viewHolder.groupName = view.findViewById(R.id.list_item_group_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (GroupListAdapter.ViewHolder) view.getTag();
        }
        viewHolder.image.setImageResource(R.drawable.head_image);
        viewHolder.groupName.setText(group.getName());
        viewHolder.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupGid", group.getGid());
                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        LinearLayout group;
        CircleImageView image;
        TextView groupName;
    }

}
