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

import java.util.ArrayList;

import vn.hdu.go2jp.hduchat.base.OnResult;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class ChatService extends Service {

    public ChatService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        FireBaseUtil.getInstance().getListRoom(new OnResult<ArrayList<String>>() {
            @Override
            public void onResult(ArrayList<String> listRoom) {
                for (String roomId : listRoom) {
                    FirebaseDatabase.getInstance().getReference().child("rooms").child(roomId).child("messages")
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
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}