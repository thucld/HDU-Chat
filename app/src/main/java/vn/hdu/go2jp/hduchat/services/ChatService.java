package vn.hdu.go2jp.hduchat.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.activity.ChatBoxActivity;
import vn.hdu.go2jp.hduchat.activity.MainActivity;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.listener.OnResult;
import vn.hdu.go2jp.hduchat.model.data.Room;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class ChatService extends Service {

    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    RemoteViews contentView;
    private FireBaseUtil fireBaseUtil = FireBaseUtil.getInstance();
    private long[] patternVibrate = {500, 500, 500};

    public ChatService() {
    }

    @Override
    public void onCreate() {
        contentView = new RemoteViews(getPackageName(), R.layout.layout_notification);
        listenerForRoom();
        listenerForSingleRoom();
    }

    public void listenerForRoom() {
        fireBaseUtil.getListRoom(new OnResult<List<Room>>() {
            @Override
            public void onResult(List<Room> rooms) {
                for (Room room : rooms) {
                    DatabaseReference referenceRoom = FirebaseDatabase.getInstance().getReference().child("rooms/" + room.getRoomId() + "/messages");
                    referenceRoom.orderByChild("timestamp").startAt(new Date().getTime())
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Message message = dataSnapshot.getValue(Message.class);
                                    Log.i("my_message", "\n" + room + ":\n" + message.getMessage());
                                    pushNotification(getApplicationContext(), room.getRoomId(), room.getTitle(), message);
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Log.i("my_message", "onChildChanged");
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                    Log.i("my_message", "onChildRemoved");
                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Log.i("my_message", "onChildMoved");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.i("my_message", "onCancelled");
                                }
                            });
                }
            }

            ;
        });
    }

    public void listenerForSingleRoom() {
        fireBaseUtil.getThisUser(user -> {
            if (user != null && !TextUtils.isEmpty(user.getUserId()) && user.getContacts() != null) {
                String ownerId = user.getUserId();
                for (String friendId : user.getContacts().values()) {
                    String singleRoom = ownerId.compareTo(friendId) < 0 ? ownerId + friendId : friendId + ownerId;
                    DatabaseReference referenceRoom = FirebaseDatabase.getInstance().getReference().child("rooms/" + singleRoom + "/messages");
                    referenceRoom.orderByChild("timestamp").startAt(new Date().getTime())
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Message message = dataSnapshot.getValue(Message.class);
                                    Log.i("my_message", "\n" + singleRoom + ":\n" + message);
                                    pushNotification(getApplicationContext(), singleRoom, user.getUserName(), message);
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }
        });
    }

    private void pushNotification(Context context, String roomId, String roomTitle, Message message) {
        contentView.setImageViewResource(R.id.image, R.drawable.icon);
        contentView.setTextViewText(R.id.title, roomTitle);
        contentView.setTextViewText(R.id.text, message.getMessage());
        //contentView.setOnClickPendingIntent(R.id.btnReply, PendingIntent.getBroadcast(this, 0, new Intent(), 0));

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContent(contentView)
                .setAutoCancel(true)
                .setLights(Color.BLUE, 500, 500)
                .setVibrate(patternVibrate)
                .setSound(alarmSound);

        wakeUp();
        Intent resultIntent = new Intent(this, ChatBoxActivity.class);
        resultIntent.putExtra(AppConst.KEY_ROOM_ID, roomId);
        resultIntent.putExtra(AppConst.KEY_ROOM_TITLE, roomTitle);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(roomId.hashCode(), notification.build());
    }

    private void wakeUp() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
        wl.acquire(2000);
        PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
        wl_cpu.acquire(2000);
    }

    private void onClickReply() {
        Log.i("my_onClickReply", "Reply clicked");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
