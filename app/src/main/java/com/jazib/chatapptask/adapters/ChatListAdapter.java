package com.jazib.chatapptask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jazib.chatapptask.R;
import com.jazib.chatapptask.models.Message;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jazib on 1/24/2015.
 */
public class ChatListAdapter extends BaseAdapter {

    private String mUserId;
    private Context mContext;
    private ArrayList<Message> mMessages = new ArrayList<Message>();
    private static int ROW_LEFT = 0;
    private static int ROW_RIGHT = 1;

    public ChatListAdapter(Context context, String userId) {
        this.mUserId = userId;
        this.mContext = context;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessages.get(position);
        return message.getUserId().equals(mUserId) ? ROW_LEFT : ROW_RIGHT;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Message getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        int type = getItemViewType(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (type == ROW_LEFT) {
                convertView = inflater.inflate(R.layout.row_msg_left, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.row_msg_right, parent, false);
            }
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvMessage = (TextView) convertView.findViewById(R.id.tvMsg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Message message = mMessages.get(position);
        holder.tvName.setText(message.getUserName());
        holder.tvMessage.setText(message.getMessage());
        return convertView;
    }

    public void addItem(Message message) {
        mMessages.add(message);
        notifyDataSetChanged();
    }

    public void addAllItems(List<Message> messages) {
        mMessages.clear();
        mMessages.addAll(messages);
        notifyDataSetChanged();
    }

    final class ViewHolder {
        public TextView tvName;
        public TextView tvMessage;
    }

}