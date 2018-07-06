package vn.hdu.go2jp.hduchat.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.activity.ChatBoxActivity;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.data.User;
import vn.hdu.go2jp.hduchat.util.ToastUtil;

public class UserDialog {
    private TextView txtv_name, txtv_id;
    private LinearLayout btn_edit, btn_home, btn_keep, btn_chat, btn_call, btn_video, friend_follow;

    private void findControl(Dialog dialog) {
        txtv_name = dialog.findViewById(R.id.name_dialog);
        txtv_id = dialog.findViewById(R.id.uId_dialog);

        btn_edit = dialog.findViewById(R.id.btn_edit);
        btn_home = dialog.findViewById(R.id.btn_home);
        btn_keep = dialog.findViewById(R.id.btn_keep);
        btn_chat = dialog.findViewById(R.id.btn_chat);
        btn_call = dialog.findViewById(R.id.btn_call);
        btn_video = dialog.findViewById(R.id.btn_video);
        friend_follow = dialog.findViewById(R.id.friend_follow);
    }

    private void showUserAction() {
        btn_keep.setVisibility(View.VISIBLE);
        btn_home.setVisibility(View.VISIBLE);
        btn_edit.setVisibility(View.VISIBLE);
        txtv_id.setVisibility(View.VISIBLE);

        friend_follow.setVisibility(View.GONE);
        btn_chat.setVisibility(View.GONE);
        btn_call.setVisibility(View.GONE);
        btn_video.setVisibility(View.GONE);
    }

    private void showFriendAction() {
        btn_keep.setVisibility(View.GONE);
        btn_home.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);
        txtv_id.setVisibility(View.GONE);

        friend_follow.setVisibility(View.VISIBLE);
        btn_chat.setVisibility(View.VISIBLE);
        btn_call.setVisibility(View.VISIBLE);
        btn_video.setVisibility(View.VISIBLE);
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

        txtv_name.setText(user.getUserName());
        txtv_id.setText(user.getUserId());

        if (user.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
            showUserAction();
            btn_keep.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), "Keep clicked"));
            btn_home.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), "Home clicked"));
            btn_edit.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), "Edit clicked"));
        } else {
            showFriendAction();
            btn_chat.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ChatBoxActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AppConst.KEY_ROOM_ID, getRoomId(user));
                bundle.putString(AppConst.KEY_ROOM_TITLE, user.getUserName());
                intent.putExtras(bundle);
                activity.startActivity(intent);
            });
            btn_call.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), "Call clicked"));
            btn_video.setOnClickListener(v -> new ToastUtil().showShort(dialog.getContext(), "Video clicked"));
        }
        dialog.show();
    }
}
