package vn.hdu.go2jp.hduchat.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;
import java.util.HashMap;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.activity.ChatBoxActivity;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.listener.OnResult;
import vn.hdu.go2jp.hduchat.model.data.Friend;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.model.data.Room;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.widget.NotificationBroadcastReceiver;

public class ChatService extends Service {

    private static final String KEY_REPLY = "key_reply";
    private static final String KEY_VIEW = "key_view";

    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private long[] patternVibrate = {500, 500, 500};
    private long timestamp;

    private String uId = FirebaseAuth.getInstance().getUid();
    private FireBaseUtil fireBaseUtil = FireBaseUtil.getInstance();
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private HashMap<String, ChildEventListener> roomsListener;

    public ChatService() {
    }

    @Override
    public void onCreate() {
        timestamp = new Date().getTime();
        roomsListener = new HashMap<>();

        listenerForRoom();
        //listenerForSingleRoom();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void listenerForRoom() {
        fireBaseUtil.getListRoom(new OnResult<Room>() {
            @Override
            public void onResult(Room room) {
                String roomId = room.getRoomId();
                DatabaseReference referenceRoom = firebaseDatabase.child("messages/" + roomId);

                if (!roomsListener.containsKey(roomId)) {
                    roomsListener.put(roomId, getRoomListener(room));
                    referenceRoom.orderByChild("timestamp").startAt(timestamp).addChildEventListener(roomsListener.get(roomId));
                }
            }
        });
    }

    private ChildEventListener getRoomListener(Room room) {
        String roomShowing = ChatBoxActivity.onChatBoxRoom;
        String roomId = room.getRoomId();
        String roomTitle = room.getTitle();

        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                String senderId = message.getUserId();
                String content = message.getMessage();
                timestamp = (long) message.getTimestamp() - 1;

                if (!roomId.equals(roomShowing) && !uId.equals(senderId)) {
                    fireBaseUtil.getFriendInfo(senderId, new OnResult<Friend>() {
                        @Override
                        public void onResult(Friend friend) {
                            pushNotification(getApplicationContext(), roomId, roomTitle, content, friend);
                        }
                    });
                }
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
        };
    }

//    public void listenerForSingleRoom() {
//        fireBaseUtil.getThisUser(user -> {
//            if (user != null && !TextUtils.isEmpty(user.getUserId()) && user.getContacts() != null) {
//                fireBaseUtil.getListContact(new OnResult<User>() {
//                    @Override
//                    public void onResult(User user) {
//                        String singleRoomId = FireBaseUtil.getInstance().generateSingleRoomNameById(user.getUserId());
//
//                        DatabaseReference referenceRoom = FirebaseDatabase.getInstance().getReference().child("rooms/" + singleRoomId + "/messages");
//                        referenceRoom.orderByChild("timestamp").startAt(timestamp)
//                                .addChildEventListener(new ChildEventListener() {
//                                    @Override
//                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                        Message message = dataSnapshot.getValue(Message.class);
//                                        Log.i("my_message", "\n" + message.getUserId() + uId);
//                                        if (!singleRoomId.equals(ChatBoxActivity.onChatBoxRoom) && !uId.equals(message.getUserId())) {
//                                            pushNotification(getApplicationContext(), singleRoomId, user.getUserName(), message);
//                                        }
//                                        timestamp = (long) message.getTimestamp() - 1;
//                                    }
//
//                                    @Override
//                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                                        referenceRoom.removeEventListener(this);
//                                    }
//
//                                    @Override
//                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                    }
//                });
//            }
//        });
//    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void pushNotification(Context context, String roomId, String roomTitle, String content, Friend friend) {
        Intent resultIntent;
        PendingIntent pendingIntent, pendingIntentView;

        resultIntent = new Intent();
        resultIntent.putExtra(AppConst.KEY_ROOM_ID, roomId);
        resultIntent.putExtra(AppConst.KEY_ROOM_TITLE, roomTitle);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            resultIntent.setClass(this, NotificationBroadcastReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, roomId.hashCode(), resultIntent.setAction(KEY_REPLY), PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntentView = PendingIntent.getBroadcast(this, roomId.hashCode(), resultIntent.setAction(KEY_VIEW), PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            resultIntent.setClass(this, ChatBoxActivity.class);
            pendingIntent = PendingIntent.getActivity(this, roomId.hashCode(), resultIntent.setAction(KEY_REPLY), PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntentView = PendingIntent.getActivity(this, roomId.hashCode(), resultIntent.setAction(KEY_VIEW), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        //Notification Action with RemoteInput instance added.
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY).setLabel("Enter text here!").build();
        Notification.Action replyAction = new Notification.Action.Builder(android.R.drawable.sym_action_chat, "REPLY", pendingIntent)
                .addRemoteInput(remoteInput)
                //.setAllowGeneratedReplies(true)
                .build();
        Notification.Action viewAction = new Notification.Action.Builder(android.R.drawable.sym_action_chat, "VIEW", pendingIntentView).build();

        Log.e("my_avatar",friend.getAvatarPath());

        Notification.Builder notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.icon_status)
                .setContentTitle(roomTitle)
                .setContentText(content)
                .setLights(Color.WHITE, 500, 500)
                .setVibrate(patternVibrate)
                .setSound(alarmSound)
                .addAction(viewAction)
                .addAction(replyAction)
                //.setContentIntent(pendingIntent)
                .setLargeIcon(getBitmap(context, friend.getAvatarPath()))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.setColor(ContextCompat.getColor(context, R.color.green)).setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        wakeUp();

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

    private Bitmap getBitmap(Context context, String avatarPath) {
        try {
            return Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReference(avatarPath))
                    .asBitmap()
                    .centerCrop()
                    .into(100, 100)
                    .get();
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}