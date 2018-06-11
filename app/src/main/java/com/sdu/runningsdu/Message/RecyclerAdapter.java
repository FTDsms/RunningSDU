package com.sdu.runningsdu.Message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdu.runningsdu.JavaBean.Friend;
import com.sdu.runningsdu.JavaBean.Group;
import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by FTDsm on 2018/5/12.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Message> list;
    private Context context;
    private MyDAO myDAO;
    private MyApplication myApplication;

    public RecyclerAdapter(List<Message> list, Context context) {
        this.list = list;
        this.context = context;
        myApplication = (MyApplication) context.getApplicationContext();
        myDAO = new MyDAO(context, myApplication.getUser().getName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //将数据填充到具体的view中
        //刷新列表操作
        //TODO: user head image
        holder.icon.setImageResource(R.drawable.head_image);
        if (list.get(position).isGroup()) {
            // if is group, set group name and latest message
            holder.name.setText(myDAO.findGroup(list.get(position).getGroup()).getName());
            if (list.get(position).getType() == Message.TYPE_SENT) {
                //if message is sent, just set text without name
                holder.message.setText(list.get(position).getContent());
            } else {
                //if message is received, set text with friend name
                holder.message.setText(list.get(position).getFriend()+": "+list.get(position).getContent());
            }
            int unread = myDAO.findGroup(list.get(position).getGroup()).getUnread();
            if (unread > 99) {
                holder.badge.setBadgeText("99+");
            } else {
                holder.badge.setBadgeNumber(unread);
            }
        } else {
            // if not group, set friend name and latest message
            holder.name.setText(list.get(position).getFriend());
            holder.message.setText(list.get(position).getContent());
            int unread = myDAO.findFriend(list.get(position).getFriend()).getUnread();
            if (unread > 99) {
                holder.badge.setBadgeText("99+");
            } else {
                holder.badge.setBadgeNumber(unread);
            }
        }
        holder.time.setText(list.get(position).getTime());
//        holder.badge.setBadgeNumber(1);
//        holder.badge.setBadgeText("99+");

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
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name;
        private TextView message;
        private TextView time;
        private Badge badge;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_image);
            name = (TextView) itemView.findViewById(R.id.item_name);
            message = (TextView) itemView.findViewById(R.id.item_message);
            time = (TextView) itemView.findViewById(R.id.item_time);
            badge = new QBadgeView(context).bindTarget(itemView.findViewById(R.id.item));
            badge.setBadgeGravity(Gravity.END | Gravity.BOTTOM);
            badge.setBadgeTextSize(12, true);
            badge.setGravityOffset(15, 0, true); //设置外边距
//            badge.setBadgePadding(6, true); //设置内边距
            badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                @Override
                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                    if (dragState == STATE_SUCCEED) {
                        if (list.get(getAdapterPosition()).isGroup()) {
                            Group group = myDAO.findGroup(list.get(getAdapterPosition()).getGroup());
                            group.setUnread(0);
                            myDAO.updateGroupUnread(group);
                        } else {
                            Friend friend = myDAO.findFriend(list.get(getAdapterPosition()).getFriend());
                            friend.setUnread(0);
                            myDAO.updateFriendUnread(friend);
                        }

                    }
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

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


}
