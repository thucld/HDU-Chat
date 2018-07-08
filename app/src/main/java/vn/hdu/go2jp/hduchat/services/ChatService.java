package vn.hdu.go2jp.hduchat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.hdu.go2jp.hduchat.base.OnResult;
import vn.hdu.go2jp.hduchat.model.data.Room;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class ChatService extends Service {

    public ChatService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        FireBaseUtil fireBaseUtil = FireBaseUtil.getInstance();
        fireBaseUtil.getListRoom(new OnResult<List<Room>>() {
            @Override
            public void onResult(List<Room> rooms) {
                    for (Room roomId : rooms) {
                        FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId.getRoomId()).child("messages")
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        Log.i("my_message", "\n" + roomId + ":\n" + dataSnapshot.getValue().toString());
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
            };
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
