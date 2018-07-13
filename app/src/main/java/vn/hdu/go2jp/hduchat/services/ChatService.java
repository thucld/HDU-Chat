package vn.hdu.go2jp.hduchat.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.activity.ChatBoxActivity;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.model.data.Room;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.widget.NotificationBroadcastReceiver;

public class ChatService extends Service {

    private static final String KEY_REPLY = "key_reply";
    private static final String KEY_VIEW = "key_view";
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private String uId = FirebaseAuth.getInstance().getUid();
    private FireBaseUtil fireBaseUtil = FireBaseUtil.getInstance();
    private long[] patternVibrate = {500, 500, 500};
    private long timestamp;

    public ChatService() {
    }

    @Override
    public void onCreate() {
        timestamp = new Date().getTime();
        listenerForRoom();
        listenerForSingleRoom();
    }

    public void listenerForRoom() {
        fireBaseUtil.getListRoom(rooms -> {
            for (Room room : rooms) {
                DatabaseReference referenceRoom = FirebaseDatabase.getInstance().getReference().child("rooms/" + room.getRoomId() + "/messages");
                referenceRoom.orderByChild("timestamp").startAt(timestamp)
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Message message = dataSnapshot.getValue(Message.class);
                                Log.i("my_message", "\n" + message.getUserId() + uId);
                                if (!room.getRoomId().equals(ChatBoxActivity.onChatBoxRoom) && !uId.equals(message.getUserId())) {
                                    pushNotification(getApplicationContext(), room.getRoomId(), room.getTitle(), message);
                                }
                                timestamp = (long) message.getTimestamp() - 1;
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
        });
    }

    public void listenerForSingleRoom() {
        fireBaseUtil.getThisUser(user -> {
            if (user != null && !TextUtils.isEmpty(user.getUserId()) && user.getContacts() != null) {
                String ownerId = user.getUserId();
                for (String friendId : user.getContacts().values()) {
                    String singleRoomId = ownerId.compareTo(friendId) < 0 ? ownerId + friendId : friendId + ownerId;

                    DatabaseReference referenceRoom = FirebaseDatabase.getInstance().getReference().child("rooms/" + singleRoomId + "/messages");
                    referenceRoom.orderByChild("timestamp").startAt(timestamp)
                            .addChildEventListener(new ChildEventListener() {

                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Message message = dataSnapshot.getValue(Message.class);
                                    Log.i("my_message", "\n" + message.getUserId() + uId);
                                    if (!singleRoomId.equals(ChatBoxActivity.onChatBoxRoom) && !uId.equals(message.getUserId())) {
                                        pushNotification(getApplicationContext(), singleRoomId, user.getUserName(), message);
                                    }
                                    timestamp = (long) message.getTimestamp() - 1;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void pushNotification(Context context, String roomId, String roomTitle, Message message) {

        Intent resultIntent = new Intent(this, NotificationBroadcastReceiver.class);
        resultIntent.setAction(KEY_REPLY);
        resultIntent.putExtra(AppConst.KEY_ROOM_ID, roomId);
        resultIntent.putExtra(AppConst.KEY_ROOM_TITLE, roomTitle);

        Intent resultIntentView = new Intent(this, NotificationBroadcastReceiver.class);
        resultIntentView.setAction(KEY_VIEW);
        resultIntentView.putExtra(AppConst.KEY_ROOM_ID, roomId);
        resultIntentView.putExtra(AppConst.KEY_ROOM_TITLE, roomTitle);

//        TaskStackBuilder taskStack = TaskStackBuilder.create(this);
//        taskStack.addParentStack(ChatBoxActivity.class);
//        taskStack.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(this, roomId.hashCode(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntentView = PendingIntent.getBroadcast(this, roomId.hashCode(), resultIntentView, PendingIntent.FLAG_UPDATE_CURRENT);

        //Notification Action with RemoteInput instance added.
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY).setLabel("Enter text here!").build();
        Notification.Action replyAction = new Notification.Action.Builder(android.R.drawable.sym_action_chat, "REPLY", resultPendingIntent)
                .addRemoteInput(remoteInput)
                //.setAllowGeneratedReplies(true)
                .build();
        Notification.Action viewAction = new Notification.Action.Builder(android.R.drawable.sym_action_chat, "VIEW", resultPendingIntentView).build();

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(roomTitle)
                .setContentText(message.getMessage())
                .setLights(Color.BLUE, 500, 500)
                .setColor(ContextCompat.getColor(context, R.color.green))
                .setVibrate(patternVibrate)
                .setSound(alarmSound)
                .addAction(viewAction)
                .addAction(replyAction)
                .setContentIntent(resultPendingIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                //.setLargeIcon(null) set USER_IMAGE in here, (Bitmap) <!-- TODO: Tuanter - add Glide Bitmap Image of User -->
                .setTicker("Notification Ticker")
                .setAutoCancel(true)
                .build();

        wakeUp();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(roomId.hashCode(), notification);
    }

    private void wakeUp() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
        wl.acquire(2000);
        PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
        wl_cpu.acquire(2000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}