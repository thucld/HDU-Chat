package vn.hdu.go2jp.hduchat.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.data.models.User;

public class UserDialog {
    private TextView txtv_name,txtv_id;
    private LinearLayout btn_edit, btn_home, btn_keep, btn_chat, btn_call, btn_video, friend_follow;

    private void findControl(Dialog dialog){
        txtv_name = (TextView) dialog.findViewById(R.id.name_dialog);
        txtv_id = (TextView) dialog.findViewById(R.id.uId_dialog);

        btn_edit = (LinearLayout) dialog.findViewById(R.id.btn_edit);
        btn_home = (LinearLayout) dialog.findViewById(R.id.btn_home);
        btn_keep = (LinearLayout) dialog.findViewById(R.id.btn_keep);
        btn_chat = (LinearLayout) dialog.findViewById(R.id.btn_chat);
        btn_call = (LinearLayout) dialog.findViewById(R.id.btn_call);
        btn_video = (LinearLayout) dialog.findViewById(R.id.btn_video);
        friend_follow = (LinearLayout) dialog.findViewById(R.id.friend_follow);
    }

    private void showUserAction(){
        btn_keep.setVisibility(View.VISIBLE);
        btn_home.setVisibility(View.VISIBLE);
        btn_edit.setVisibility(View.VISIBLE);
        txtv_id.setVisibility(View.VISIBLE);

        friend_follow.setVisibility(View.GONE);
        btn_chat.setVisibility(View.GONE);
        btn_call.setVisibility(View.GONE);
        btn_video.setVisibility(View.GONE);
    }

    private void showFriendAction(){
        btn_keep.setVisibility(View.GONE);
        btn_home.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);
        txtv_id.setVisibility(View.GONE);

        friend_follow.setVisibility(View.VISIBLE);
        btn_chat.setVisibility(View.VISIBLE);
        btn_call.setVisibility(View.VISIBLE);
        btn_video.setVisibility(View.VISIBLE);
    }

    public void showDialog(Activity activity, User user) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_user);

        findControl(dialog);

        txtv_name.setText(user.getUserName());
        txtv_id.setText(user.getUserId());

        if(user.getUserId().equals(FirebaseAuth.getInstance().getUid())){
            showUserAction();
            btn_keep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastUtil().showShort(dialog.getContext(),"Keep clicked");
                }
            });
            btn_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastUtil().showShort(dialog.getContext(),"Home clicked");
                }
            });
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastUtil().showShort(dialog.getContext(),"Edit clicked");
                }
            });
        }else{
            showFriendAction();
            btn_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastUtil().showShort(dialog.getContext(),"Chat clicked");
                }
            });
            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastUtil().showShort(dialog.getContext(),"Call clicked");
                }
            });
            btn_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ToastUtil().showShort(dialog.getContext(),"Video clicked");
                }
            });
        }
        dialog.show();
    }
}
