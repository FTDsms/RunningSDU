package com.sdu.runningsdu.Contact.NewFriend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.Message.RecyclerAdapter;
import com.sdu.runningsdu.R;

import java.util.List;

/**
 * Created by FTDsm on 2018/6/10.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    List<Friend> friends;

    private Context context;

    public SearchResultAdapter(List<Friend> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SearchResultAdapter.ViewHolder holder, int position) {
        // 将数据填充到具体的view中
        // 刷新列表操作
        holder.icon.setImageResource(R.drawable.head_image);
        holder.name.setText(friends.get(position).getName());
        holder.sid.setText(friends.get(position).getSid());

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position1 = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position1);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position1 = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, position1);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name;
        private TextView sid;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            sid = itemView.findViewById(R.id.item_message);
        }
    }

    /**
     * 定义接口回调
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private RecyclerAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(RecyclerAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
