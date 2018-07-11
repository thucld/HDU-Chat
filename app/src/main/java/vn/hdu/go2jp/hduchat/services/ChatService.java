package vn.hdu.go2jp.hduchat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.hdu.go2jp.hduchat.listener.OnResult;
import vn.hdu.go2jp.hduchat.model.data.Room;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class ChatService extends Service {

    private FireBaseUtil fireBaseUtil = FireBaseUtil.getInstance();

    public ChatService() {

    }

    @Override
    public void onCreate() {
        listenerForRoom();
        listenerForSingleRoom();
    }

    public void listenerForRoom() {
        fireBaseUtil.getListRoom(new OnResult<List<Room>>() {
            @Override
            public void onResult(List<Room> rooms) {
                for (Room room : rooms) {
                    FirebaseDatabase.getInstance().getReference().child("rooms").child(room.getRoomId()).child("messages")
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Log.i("my_message", "\n" + room + ":\n" + dataSnapshot.getValue().toString());
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
                    FirebaseDatabase.getInstance().getReference().child("rooms/" + singleRoom + "/messages")
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Log.i("my_message", "\n" + singleRoom + ":\n" + dataSnapshot.getValue().toString());
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
