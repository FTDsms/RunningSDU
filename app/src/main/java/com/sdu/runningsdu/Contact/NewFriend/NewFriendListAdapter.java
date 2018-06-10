package com.sdu.runningsdu.Contact.NewFriend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Request;
import com.sdu.runningsdu.Message.RecyclerAdapter;
import com.sdu.runningsdu.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by FTDsm on 2018/6/8.
 */

public class NewFriendListAdapter extends RecyclerView.Adapter<NewFriendListAdapter.ViewHolder> {

    private List<Request> requests;

    private Context context;

    public NewFriendListAdapter(List<Request> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_friend_request, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewFriendListAdapter.ViewHolder holder, int position) {
        //将数据填充到具体的view中
        //刷新列表操作
        holder.icon.setImageResource(R.drawable.head_image);
        holder.name.setText(requests.get(position).getSender());
        holder.message.setText(requests.get(position).getMessage());
        holder.state.setText(requests.get(position).getState());

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
        return requests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView icon;
        private TextView name;
        private TextView message;
        private TextView state;
        private Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            message = itemView.findViewById(R.id.item_message);
            state = itemView.findViewById(R.id.item_state);
            button = itemView.findViewById(R.id.item_button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
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
