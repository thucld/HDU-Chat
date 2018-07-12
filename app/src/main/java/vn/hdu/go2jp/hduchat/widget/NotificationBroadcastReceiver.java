package vn.hdu.go2jp.hduchat.widget;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;

import com.google.firebase.auth.FirebaseAuth;

import vn.hdu.go2jp.hduchat.activity.ChatBoxActivity;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static final String KEY_VIEW = "key_view";
    private final String KEY_REPLY = "key_reply";
    private String uId = FirebaseAuth.getInstance().getUid();

    @Override
    public void onReceive(Context context, Intent intent) {
        getReplyNotification(context, intent);
    }

    private void getReplyNotification(Context context, Intent intent) {
        if (KEY_REPLY.equals(intent.getAction())) {
            String roomId = intent.getStringExtra(AppConst.KEY_ROOM_ID);
            String title = intent.getStringExtra(AppConst.KEY_ROOM_TITLE);
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            if (remoteInput != null) {
                String content = remoteInput.getString(KEY_REPLY);
                Message msg = new Message(uId, content, UserType.SELF, Status.SENT);
                FireBaseUtil.getInstance().sendMessage(roomId, msg, isSuccess -> {
                    if (isSuccess) {
                    }
                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancel(roomId.hashCode());
                });
            }
        }
        if (KEY_VIEW.equals(intent.getAction())) {
            String roomId = intent.getStringExtra(AppConst.KEY_ROOM_ID);
            String title = intent.getStringExtra(AppConst.KEY_ROOM_TITLE);
            Intent intentView = new Intent(context, ChatBoxActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.KEY_ROOM_ID, roomId);
            bundle.putString(AppConst.KEY_ROOM_TITLE, title);
            intentView.putExtras(bundle);
            context.startActivity(intentView);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(roomId.hashCode());
        }
    }
}
