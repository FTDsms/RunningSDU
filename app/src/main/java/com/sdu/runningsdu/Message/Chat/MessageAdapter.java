package com.sdu.runningsdu.Message.Chat;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdu.runningsdu.JavaBean.Message;
import com.sdu.runningsdu.R;
import com.sdu.runningsdu.Utils.MyApplication;
import com.sdu.runningsdu.Utils.MyDAO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by FTDsm on 2018/5/30.
 */

public class MessageAdapter extends ArrayAdapter<Message>{

    private int resourceId;

    private MyApplication myApplication;

    private MyDAO myDAO;

    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        myApplication = (MyApplication) context.getApplicationContext();
        myDAO = new MyDAO(context, myApplication.getUser().getName());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        View view;
        MessageAdapter.ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new MessageAdapter.ViewHolder();
            viewHolder.leftLayout = view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = view.findViewById(R.id.left_msg);
            viewHolder.leftImage = view.findViewById(R.id.left_image);
            viewHolder.rightMsg = view.findViewById(R.id.right_msg);
            viewHolder.rightImage = view.findViewById(R.id.right_image);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (MessageAdapter.ViewHolder) view.getTag();
        }
        if(message.getType() == Message.TYPE_RECEIVED) {
            //如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(message.getContent());
            byte[] bytes = myDAO.findFriendImage(message.getFriend());
            viewHolder.leftImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        } else if(message.getType() == Message.TYPE_SENT) {
            //如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(message.getContent());
            byte[] bytes = myDAO.findUserImage(myApplication.getUser().getSid());
            viewHolder.rightImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
        return view;
    }

    class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        CircleImageView leftImage;
        CircleImageView rightImage;
    }

}
