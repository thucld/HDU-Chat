package vn.hdu.go2jp.hduchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;

public class MessageAdapter extends BaseAdapter {

    private ArrayList<Message> messages;
    private Context context;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public MessageAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        Message message = messages.get(position);
        ViewHolder1 holder1;
        ViewHolder2 holder2;

        if (message.getUserType() == UserType.SELF) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user1_item, null, false);
                holder1 = new ViewHolder1();

                holder1.messageTextView = v.findViewById(R.id.message_text);
                holder1.timeTextView = v.findViewById(R.id.time_text);
                holder1.sender = v.findViewById(R.id.chat_company_reply_author);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();
            }

//            holder1.messageTextView.setText(Emoji.replaceEmoji(message.getMessageText(), holder1.messageTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16)));
            holder1.messageTextView.setText(message.getMessage());
            holder1.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getTime()));
            holder1.sender.setText(message.getUserId());

        } else if (message.getUserType() == UserType.OTHER) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user2_item, null, false);

                holder2 = new ViewHolder2();

                holder2.messageTextView = v.findViewById(R.id.message_text);
                holder2.timeTextView = v.findViewById(R.id.time_text);
                holder2.messageStatus = v.findViewById(R.id.user_reply_status);
                holder2.sender = v.findViewById(R.id.chat_company_reply_author);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();

            }

//            holder2.messageTextView.setText(Emoji.replaceEmoji(message.getMessage(), holder2.messageTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(16) ));
            //holder2.messageTextView.setText(message.getMessageText());
            holder2.messageTextView.setText(message.getMessage());
            holder2.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getTime()));

            if (message.getStatus() == Status.DELIVERED) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_tick));
            } else if (message.getStatus() == Status.SENT) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_single_tick));
            }
        }
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getUserType().ordinal();
    }

    private class ViewHolder1 {
        TextView messageTextView;
        TextView timeTextView;
        TextView sender;
    }

    private class ViewHolder2 {
        ImageView messageStatus;
        TextView messageTextView;
        TextView timeTextView;
        TextView sender;
    }
}
