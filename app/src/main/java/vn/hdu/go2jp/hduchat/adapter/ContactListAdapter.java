package vn.hdu.go2jp.hduchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.model.data.User;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<User> mDataSet;
    private PostItemListener mItemListener;
    private static int mSelectedItem = -1;

    public ContactListAdapter(Context context, List<User> dataSet, PostItemListener itemListener) {
        this.mContext = context;
        this.mDataSet = dataSet;
        this.mItemListener = itemListener;
        mSelectedItem = -1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else return 0;
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView;
        if (viewType == 1) {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact_current_user, parent, false);
        } else {
            inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact_list, parent, false);
        }
        return new ContactListAdapter.ViewHolder(inflatedView, this.mItemListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User item = mDataSet.get(position);
        if (TextUtils.isEmpty(item.getNote())) {
            holder.tvNote.setVisibility(View.GONE);
        }
        holder.tvName.setText(item.getUserName());
//        int resId = R.drawable.ic_files_bad;
//        if (ItemRepository.isGood(this.mInsRecordId, item.getId())) {
//            resId = R.drawable.ic_files_good;
//        }
//        holder.itemIconDevice.setImageDrawable(mContext.getResources().getDrawable(resId));
//        String imageURI = item.getPreview();
//        if (!TextUtils.isEmpty(imageURI)) {
//            if (item.isLocalPreview()) {
//                Glide.with(mContext).load(new File(imageURI))
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.ic_advanced_more_vf_1280x720_24p)
//                                .error(R.drawable.ic_notify_image_empty))
//                        .into(holder.imgDevice);
//            } else {
//                Glide.with(mContext).load(imageURI)
//                        .apply(new RequestOptions()
//                                .placeholder(R.drawable.ic_advanced_more_vf_1280x720_24p)
//                                .error(R.drawable.ic_notify_image_empty))
//                        .into(holder.imgDevice);
//            }
//        } else {
//            holder.imgDevice.setImageResource(R.drawable.ic_notify_image_empty);
//            holder.imgDevice.setBackgroundColor(mContext.getResources().getColor(R.color.color_gray_transparent));
//        }
//        holder.tvItemName.setText(item.getName());
//        try {
//            holder.tvNumOfItem.setText(String.valueOf(item.getNumOfPosition()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        setColor(holder, position);
    }

//    private void setColor(ViewHolder holder, int position) {
//        if (position == mSelectedItem) {
//            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorItemSelected));
//        } else {
//            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_grey_100));
//        }
//    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private User getItem(int adapterPosition) {
        return mDataSet.get(adapterPosition);
    }

    @Override
    public void onClick(View view) {

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imAvatar;
        TextView tvName;
        TextView tvNote;
        private PostItemListener mItemListener;

        ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            imAvatar = itemView.findViewById(R.id.imAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvNote = itemView.findViewById(R.id.tvNote);
            mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mSelectedItem = getAdapterPosition();
            if (mSelectedItem < 0 || mSelectedItem >= mDataSet.size()) {
                return;
            }
            User item = getItem(mSelectedItem);
            this.mItemListener.onPostClick(item);
            notifyDataSetChanged();
        }
    }

    public interface PostItemListener {
        void onPostClick(User item);
    }
}
