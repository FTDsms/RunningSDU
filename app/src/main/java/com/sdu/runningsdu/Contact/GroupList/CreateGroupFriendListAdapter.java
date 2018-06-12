package com.sdu.runningsdu.Contact.GroupList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseViewHolder;

/**
 * Created by FTDsm on 2018/6/12.
 */

public class CreateGroupFriendListAdapter extends ArrayAdapter<Friend> {

    private int resourceId;

    List<Boolean> checked;

    public CreateGroupFriendListAdapter(Context context, int textViewResourceId, List<Friend> friends) {
        super(context, textViewResourceId, friends);
        resourceId = textViewResourceId;
        checked = new ArrayList<>();
        for (int i=0; i<friends.size(); ++i) {
            checked.add(false);
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Friend friend = getItem(position);
        View view;
        CreateGroupFriendListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new CreateGroupFriendListAdapter.ViewHolder();
            viewHolder.friend = view.findViewById(R.id.list_item_friend);
            viewHolder.image = view.findViewById(R.id.list_item_head_image);
            viewHolder.tvName = view.findViewById(R.id.list_item_tv_name);
            viewHolder.checkBox = view.findViewById(R.id.list_item_checkbox);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (CreateGroupFriendListAdapter.ViewHolder) view.getTag();
        }
        viewHolder.image.setImageResource(R.drawable.head_image);
        viewHolder.tvName.setText(friend.getName());
        viewHolder.friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked.set(position, isChecked);
            }
        });
        return view;
    }

    class ViewHolder {
        RelativeLayout friend;
        CircleImageView image;
        TextView tvName;
        CheckBox checkBox;
    }

}
