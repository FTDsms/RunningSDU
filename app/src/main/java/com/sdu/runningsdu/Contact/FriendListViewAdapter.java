package com.sdu.runningsdu.Contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdu.runningsdu.Information.DetailedInfoActivity;
import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhouzhuo.zzletterssidebar.adapter.BaseSortListViewAdapter;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseViewHolder;

/**
 * Created by FTDsm on 2018/5/14.
 */

public class FriendListViewAdapter extends BaseSortListViewAdapter<Friend, FriendListViewAdapter.ViewHolder> {

    private Context context;

    private MyApplication myApplication;

    private MyDAO myDAO;

    public FriendListViewAdapter(Context context, List<Friend> friends) {
        super(context, friends);
        this.context = context;
        myApplication  = (MyApplication) context.getApplicationContext();
        myDAO = new MyDAO(context, myApplication.getUser().getName());
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item;
    }

    @Override
    public ViewHolder getViewHolder(final View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.friend = view.findViewById(R.id.list_item_friend);
        viewHolder.image = view.findViewById(R.id.list_item_head_image);
        viewHolder.tvName = view.findViewById(R.id.list_item_tv_name);
        return viewHolder;
    }

    @Override
    public void bindValues(ViewHolder viewHolder, final int position) {
        byte[] bytes = myDAO.findFriendImage(mDatas.get(position).getSid());
        viewHolder.image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        viewHolder.tvName.setText(mDatas.get(position).getName());
        viewHolder.friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedInfoActivity.class);
                intent.putExtra("sid", mDatas.get(position).getSid());
                context.startActivity(intent);
            }
        });
    }

    class ViewHolder extends BaseViewHolder {
        protected LinearLayout friend;
        protected CircleImageView image;
        protected TextView tvName;
    }

}
