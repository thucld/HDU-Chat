package vn.hdu.go2jp.hduchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> implements View.OnClickListener {
    private static int mSelectedItem = -1;
    private Context mContext;
    private List<Message> mDataSet;
    private PostItemListener mItemListener;
    private String ownerId;

    //public MessageAdapter(Context context, List<Message> dataSet, PostItemListener itemListener) {
    public MessageAdapter(Context context, List<Message> dataSet, PostItemListener itemListener) {
        this.mContext = context;
        this.mDataSet = dataSet;
        //this.mItemListener = itemListener;
        mSelectedItem = -1;
        this.ownerId = FirebaseAuth.getInstance().getUid();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mDataSet.get(position);
        if (!ownerId.equals(message.getUserId())) {
            return 1;
        } else return 0;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView;
        if (viewType == 1) {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_user1_item, parent, false);
        } else {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_user2_item, parent, false);
        }
        return new MessageAdapter.ViewHolder(inflatedView, this.mItemListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Bind data to holder
        Message item = mDataSet.get(position);
        Date date = new Date();
        try {
            date = new Date((long) item.getTimestamp());
        } catch (Exception e) {
        }
        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.messageTextView.setText(item.getMessage());
        holder.timeTextView.setText(formatter.format(date));

        if (position != 0) {
            FireBaseUtil.getInstance().getFriendInfo(item.getUserId(), friend -> {
                if (!TextUtils.isEmpty(friend.getAvatarPath())) {
                    try {
                        holder.sender.setText(friend.getUserName());
                        Glide.with(mContext)
                                .using(new FirebaseImageLoader())
                                .load(FirebaseStorage.getInstance().getReference(friend.getAvatarPath()))
                                .centerCrop()
                                .into(holder.userImage);
                    } catch (Exception e) {
                        Log.e("my_MessageAdaptor",e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private Message getItem(int adapterPosition) {
        return mDataSet.get(adapterPosition);
    }

    @Override
    public void onClick(View view) {

    }

    public interface PostItemListener {
        void onPostClick(Message item);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView messageTextView;
        TextView timeTextView;
        TextView sender;
        ImageView messageStatus;
        ImageView userImage;

        private PostItemListener mItemListener;

        ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text);
            timeTextView = itemView.findViewById(R.id.time_text);

            userImage = itemView.findViewById(R.id.user_image);
            sender = itemView.findViewById(R.id.chat_company_reply_author);

            mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //mSelectedItem = getAdapterPosition();
            //if (mSelectedItem < 0 || mSelectedItem >= mDataSet.size()) {
            //    return;
            //}
            //Message item = getItem(mSelectedItem);
            //this.mItemListener.onPostClick(item);
            //notifyDataSetChanged();
        }
    }
}
