package vn.hdu.go2jp.hduchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.model.data.User;

public class ChooseContactAdapter extends RecyclerView.Adapter<ChooseContactAdapter.ContactViewHolder> {
    private Context context;
    private List<User> dataSet;
    private static int selectedItem = -1;
    private ChooseContactAdapter.ItemListener itemListener;
    public HashMap<String, User> chosenFriends;

    public ChooseContactAdapter(Context context, List<User> dataSet, ChooseContactAdapter.ItemListener itemListener) {
        this.context = context;
        this.dataSet = dataSet;
        this.itemListener = itemListener;
        this.chosenFriends = new HashMap<>();
        selectedItem = -1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else return 0;
    }

    @NonNull
    @Override
    public ChooseContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView;
        inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_choose_contact_fragment, parent, false);
        return new ChooseContactAdapter.ContactViewHolder(inflatedView, this.itemListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ChooseContactAdapter.ContactViewHolder holder, int position) {
        User user = dataSet.get(position);
        if (user != null) {
            Glide.with(context)
                    .load(user.getAvatarPath())
                    .into(holder.civAvatar);
            holder.tvName.setText(user.getUserName());
            boolean isChosen = null != chosenFriends.get(user.getUserId());
            holder.cbChosen.setChecked(isChosen);
            if (isChosen) {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.alabaster));
            } else {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private User getItem(int position) {
        return dataSet.get(position);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView civAvatar;
        TextView tvName;
        CheckBox cbChosen;
        private ChooseContactAdapter.ItemListener itemListener;

        ContactViewHolder(View itemView, ChooseContactAdapter.ItemListener itemListener) {
            super(itemView);
            civAvatar = itemView.findViewById(R.id.civAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            this.itemListener = itemListener;
            cbChosen = itemView.findViewById(R.id.cbChosen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedItem = getAdapterPosition();
            if (selectedItem < 0 || selectedItem >= dataSet.size()) {
                return;
            }
            User user = getItem(selectedItem);
            this.itemListener.onItemClick(user);
            notifyDataSetChanged();
        }
    }

    public interface ItemListener {
        void onItemClick(User user);
    }
}
