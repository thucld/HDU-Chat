package vn.hdu.go2jp.hduchat.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.ChooseContactAdapter;
import vn.hdu.go2jp.hduchat.base.BaseActivity;
import vn.hdu.go2jp.hduchat.model.data.User;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.util.ToastUtil;

public class CreateRoomActivity extends BaseActivity {

    private ImageButton btnBack;
    private Button btnCreateRoom;
    private AppCompatEditText edtSearch;
    private ImageView ivSearchIcon;
    private ImageView ivRemoveText;
    private TextView tvFriendNumber;
    private RecyclerView rvContacts;
    private HorizontalScrollView hsvFriendsThumb;
    private LinearLayout llFriendsThumb;
    private ChooseContactAdapter adapter;
    private List<User> users;
    private List<String> thumbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        initData();
        initViews();
        setupEvents();
        getData();
    }

    private void initData() {
        users = new ArrayList<>();
        thumbUsers = new ArrayList<>();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        tvFriendNumber = findViewById(R.id.tvFoundFriendNumber);
        edtSearch = findViewById(R.id.edtSearch);
        ivSearchIcon = findViewById(R.id.ivSearchIcon);
        ivRemoveText = findViewById(R.id.ivRemoveText);
        rvContacts = findViewById(R.id.rvNewChatContacts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        adapter = new ChooseContactAdapter(getApplicationContext(), users, this::onItemChosenChange);
        rvContacts.setLayoutManager(layoutManager);
        rvContacts.setAdapter(adapter);
        hsvFriendsThumb = findViewById(R.id.hsvFriendsThumb);
        llFriendsThumb = findViewById(R.id.llFriendsThumb);
    }

    private void setupEvents() {
        edtSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                boolean isEmpty = TextUtils.isEmpty(edtSearch.getText());
                ivRemoveText.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                ivSearchIcon.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                int resColor = isEmpty ? R.color.gray_athens : R.color.malachite;
                edtSearch.getBackground().mutate().setColorFilter(getResources().getColor(resColor), PorterDuff.Mode.SRC_ATOP);
            }
        });
        ivRemoveText.setOnClickListener(ivRemoveText -> edtSearch.setText(""));
        btnBack.setOnClickListener(back -> back());
        btnCreateRoom.setOnClickListener(create -> createRoom());
    }

    private void getData() {
        FireBaseUtil.getInstance().getListContact(user -> {
            this.users.add(user);
            adapter.notifyDataSetChanged();
            tvFriendNumber.setText(getString(R.string.str_num_of_friend, this.users.size()));
        });
    }

    private void onItemChosenChange(User user) {
        if (null == adapter.chosenFriends.get(user.getUserId())) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_cancelling_avatar, llFriendsThumb, true);
            CircleImageView civAvatar = view.findViewById(R.id.civAvatar);
            TextView tvName = view.findViewById(R.id.tvFriendName);
            tvName.setText(user.getUserName());
            civAvatar.setId(View.generateViewId());
            tvName.setId(View.generateViewId());
            Glide.with(getApplicationContext())
                    .load(user.getAvatarPath())
                    .into(civAvatar);
            view.setId(View.generateViewId());
            view.setOnClickListener(v -> removeFriend(user));
            adapter.chosenFriends.put(user.getUserId(), user);
            thumbUsers.add(user.getUserId());
        } else {
            removeFriend(user);
        }
        if (adapter.chosenFriends.isEmpty()) {
            hsvFriendsThumb.setVisibility(View.GONE);
            btnCreateRoom.setText(R.string.str_chat);
            btnCreateRoom.setTextColor(getResources().getColor(R.color.green_pastel));
            ((RelativeLayout.LayoutParams) rvContacts.getLayoutParams()).addRule(RelativeLayout.ABOVE, R.id.btnCreateRoom);
            llFriendsThumb.removeAllViews();
        } else {
            hsvFriendsThumb.setVisibility(View.VISIBLE);
            btnCreateRoom.setText(getString(R.string.str_chat_with_value, adapter.chosenFriends.size()));
            btnCreateRoom.setTextColor(getResources().getColor(R.color.white));
            ((RelativeLayout.LayoutParams) rvContacts.getLayoutParams()).addRule(RelativeLayout.ABOVE, R.id.hsvFriendsThumb);
        }
    }

    private void removeFriend(User user) {
        String id = user.getUserId();
        adapter.chosenFriends.remove(id);
        for (int index = 0; index < thumbUsers.size(); ++index) {
            if (id.equals(thumbUsers.get(index))) {
                llFriendsThumb.removeViewAt(index);
                thumbUsers.remove(index);
                break;
            }
        }
    }

    private void back() {
        new ToastUtil().showShort(getApplicationContext(), "back");
        this.onBackPressed();
        this.finish();
    }

    private void createRoom() {
        new ToastUtil().showShort(getApplicationContext(), "Create Room");
    }

}
