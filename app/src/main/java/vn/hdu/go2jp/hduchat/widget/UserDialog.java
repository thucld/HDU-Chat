package vn.hdu.go2jp.hduchat.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.activity.ChatBoxActivity;
import vn.hdu.go2jp.hduchat.activity.ProfileActivity;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.data.User;
import vn.hdu.go2jp.hduchat.util.ToastUtil;

public class UserDialog {
    private TextView tvName, tvId;
    private LinearLayout btnEdit;
    private LinearLayout btnHome;
    private LinearLayout btnKeep;
    private LinearLayout btnChat;
    private LinearLayout btnCall;
    private LinearLayout btnVideo;
    private LinearLayout llFollowFriend;
    private ImageView ivAvatar;

    private void findControl(Dialog dialog) {
        tvName = dialog.findViewById(R.id.tvName);
        tvId = dialog.findViewById(R.id.tvId);

        btnEdit = dialog.findViewById(R.id.btnEdit);
        btnHome = dialog.findViewById(R.id.btnHome);
        btnKeep = dialog.findViewById(R.id.btnKeep);
        btnChat = dialog.findViewById(R.id.btnChat);
        btnCall = dialog.findViewById(R.id.btnCall);
        btnVideo = dialog.findViewById(R.id.btnVideo);
        llFollowFriend = dialog.findViewById(R.id.llFollowFriend);
        ivAvatar = dialog.findViewById(R.id.ivAvatar);
    }

    private void showUserAction() {
        btnKeep.setVisibility(View.VISIBLE);
        btnHome.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.VISIBLE);
        tvId.setVisibility(View.VISIBLE);

        llFollowFriend.setVisibility(View.GONE);
        btnChat.setVisibility(View.GONE);
        btnCall.setVisibility(View.GONE);
        btnVideo.setVisibility(View.GONE);
    }

    private void showFriendAction() {
        btnKeep.setVisibility(View.GONE);
        btnHome.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        tvId.setVisibility(View.GONE);

        llFollowFriend.setVisibility(View.VISIBLE);
        btnChat.setVisibility(View.VISIBLE);
        btnCall.setVisibility(View.VISIBLE);
        btnVideo.setVisibility(View.VISIBLE);
    }

    private String getRoomId(User user) {
        String ownerId = FirebaseAuth.getInstance().getUid();
        String friendId = user.getUserId();
        return ownerId.compareTo(friendId) < 0 ? ownerId + friendId : friendId + ownerId;
    }

    public void showDialog(Activity activity, User user) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_user);

        findControl(dialog);

        tvName.setText(user.getUserName());
        tvId.setText(user.getUserId());

        if (!TextUtils.isEmpty(user.getUserId())) {
            if (FirebaseAuth.getInstance().getUid().equals(user.getUserId())) {
                showUserAction();
                btnKeep.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), activity.getString(R.string.str_keep)));
                btnHome.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), activity.getString(R.string.str_home)));
                btnEdit.setOnClickListener(v -> {
                    activity.startActivity(new Intent(activity, ProfileActivity.class));
                    dialog.dismiss();
                });
            } else {
                showFriendAction();
                btnChat.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, ChatBoxActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConst.KEY_ROOM_ID, getRoomId(user));
                    bundle.putString(AppConst.KEY_ROOM_TITLE, user.getUserName());
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    dialog.dismiss();
                });
                btnCall.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), activity.getString(R.string.str_call)));
                btnVideo.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), activity.getString(R.string.str_video_call)));
            }
            if (!TextUtils.isEmpty(user.getAvatarPath())) {
                Glide.with(activity)
                        .load(user.getAvatarPath())
                        .into(ivAvatar);
            }
            dialog.show();
        }
    }
}
