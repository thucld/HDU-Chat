package vn.hdu.go2jp.hduchat.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import vn.hdu.go2jp.hduchat.listener.OnResult;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.model.data.Room;
import vn.hdu.go2jp.hduchat.model.data.User;

public class FireBaseUtil {
    private static User thisUser;
    private static FirebaseUser user;
    private static FirebaseAuth auth;
    private static DatabaseReference mDatabase;
    private static List<String> listContactID = new ArrayList<>();
    private static List<String> listRoomID = new ArrayList<>();
    private static FireBaseUtil instance;

    public static synchronized FireBaseUtil getInstance() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (instance == null) {
            instance = new FireBaseUtil();
        }
        return instance;
    }

    public static String generateSingleRoomNameById(String friendId) {
        String ownerId = user.getUid();
        return ownerId.compareTo(friendId) < 0 ? ownerId + friendId : friendId + ownerId;
    }

    //dang lam
    public void test() {
        String uId = FirebaseAuth.getInstance().getUid();

        /*
        Get list room by OnChildEventListener
         */
//        instance.getListRoomTest(new OnResult<Room>() {
//            @Override
//            public void onResult(Room room) {
//                Log.i("my_OnResultRoom",room.getTitle());
//            }
//        });
    }

    /*
    Get List Room by OnChildEventListener
    TODO hautv - constructing Get List Room by OnChildEventListener
     */
    public void getListRoom(OnResult<Room> onResult) {
        mDatabase.child("users").child(user.getUid()).child("roomsId")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.getValue() != null) {
                            String roomId = dataSnapshot.getValue(String.class);
                            getRoomInfo(roomId, room -> onResult.onResult(room));
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.getValue() != null) {
                            String roomId = dataSnapshot.getValue(String.class);
                            getRoomInfo(roomId, room -> onResult.onResult(room));
                        }
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

    public void getRoomInfo(String roomId, OnResult<Room> onResult) {
        mDatabase.child("rooms").child(roomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Room roomInfo = dataSnapshot.getValue(Room.class);
                            onResult.onResult(roomInfo);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("getRoomInfo", databaseError.toString());
                        onResult.onResult(null);
                    }
                });
    }

    public void getThisUser(final OnResult<User> onResult) {
        if (thisUser == null) {
            mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        thisUser = dataSnapshot.getValue(User.class);
                        onResult.onResult(thisUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("my_getThisUser", databaseError.getMessage());
                }
            });
        } else {
            onResult.onResult(thisUser);
        }
    }

    public boolean signOut() {
        try {
            auth.signOut();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void writeNewUser(String userId, String passWord, String email) {
        User user = new User(userId, passWord, email);
        mDatabase.child("users").child(userId).setValue(user);
    }

    public boolean isLogin() {
        return user != null;
    }

    public void signUpWithEmail(String email, String password, OnResult<Boolean> onResult) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        onResult.onResult(task.isSuccessful());
                        if (task.isSuccessful()) {
                            instance.writeNewUser(user.getUid(), password, email);
                        }
                    }
                });
    }

    public void signInWithEmail(String email, String password, OnResult<Boolean> onResult) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> onResult.onResult(task.isSuccessful()));
    }

    public void updateUser(User user, OnResult<Boolean> onResult) {
        mDatabase.child("users/" + user.getUserId()).setValue(user).addOnCompleteListener(task -> onResult.onResult(task.isSuccessful()));
    }

    public String addContact(String uId, OnResult<String> onResult) {
        mDatabase.child("users").child(user.getUid()).child("contacts").push().setValue(uId);
        mDatabase.child("users").child(uId).child("contacts").push().setValue(user.getUid());

        return createRoom(Arrays.asList(uId), roomId -> {
            if (!TextUtils.isEmpty(roomId)) {
                onResult.onResult(roomId);
            }
        });
    }

    public String createRoom(List<String> uIds, OnResult<String> onResult) {
        DatabaseReference rmref;
        String idRoom;
        if (uIds.size() < 2) {
            idRoom = user.getUid().compareTo(uIds.get(0)) < 0 ? user.getUid() + uIds.get(0) : uIds.get(0) + user.getUid();
            rmref = mDatabase.child("rooms").child(idRoom);
        } else {
            rmref = mDatabase.child("rooms").push();
            idRoom = rmref.getKey();
        }

        Room newRoom = new Room();
        newRoom.setRoomId(idRoom);
        newRoom.setTitle("new Room");

        rmref.setValue(newRoom).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onResult.onResult(idRoom);
            }
        });

        rmref.child("contacts").child(user.getUid()).setValue(true);
        for (String item : uIds) {
            rmref.child("contacts").child(item).setValue(true);
        }
        return idRoom;
    }

    public void sendMessage(String roomId, Message message, OnResult<Boolean> status) {
        mDatabase.child("rooms").child(roomId).child("messages").push().setValue(message)
                .addOnCompleteListener(task -> {
                    status.onResult(task.isSuccessful());
                    mDatabase.child("rooms").child(roomId).child("lastMessage").setValue(message);
                });
    }

    public void getListContactTest(OnResult<User> onResult) {
        mDatabase.child("users").child(user.getUid()).child("contacts")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.getKey() != null) {
                            String userId = dataSnapshot.getKey();
                            getContactsInfoTest(userId, user -> onResult.onResult(user));
                        }
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

    public void getContactsInfoTest(String userId, OnResult<User> onResult) {
        Log.e("my_userId",userId);
        mDatabase.child("users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            User userInfo = dataSnapshot.getValue(User.class);
                            onResult.onResult(userInfo);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("getContactInfo", databaseError.toString());
                        onResult.onResult(null);
                    }
                });
    }

    public void getListContact(OnResult<List<User>> onResult) {
        List<User> listUser = new ArrayList<>();
        mDatabase.child("users").child(user.getUid()).child("contacts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            HashMap mapRecord = (HashMap) dataSnapshot.getValue();

                            Iterator listKey = mapRecord.keySet().iterator();
                            while (listKey.hasNext()) {
                                String key = listKey.next().toString();
                                listContactID.add(mapRecord.get(key).toString());
                            }
                            List<String> newList = new ArrayList<>(listContactID);
                            while (listContactID.size() > 0) {
                                instance.getContactsInfo(listContactID, user -> {
                                    listUser.add(user);
                                    if (listUser.size() == newList.size()) {
                                        onResult.onResult(listUser);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        onResult.onResult(listUser);
                    }
                });

    }

//    public void getListRoom(OnResult<List<Room>> onResult) {
//        List<Room> listRoom = new ArrayList<>();
//        mDatabase.child("users").child(user.getUid()).child("roomsId")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.getValue() != null) {
//                            HashMap mapRecord = (HashMap) dataSnapshot.getValue();
//
//                            Iterator listKey = mapRecord.keySet().iterator();
//                            while (listKey.hasNext()) {
//                                String key = listKey.next().toString();
//                                listRoomID.add(mapRecord.get(key).toString());
//                            }
//                            List<String> newList = new ArrayList<>(listRoomID);
//                            while (listRoomID.size() > 0) {
//                                instance.getRoomsInfo(listRoomID, room -> {
//                                    listRoom.add(room);
//                                    if (listRoom.size() == newList.size()) {
//                                        onResult.onResult(listRoom);
//                                    }
//                                });
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        onResult.onResult(listRoom);
//                    }
//                });
//    }
//
//    public void getRoomsInfo(List<String> ids, OnResult<Room> onResult) {
//        String id = ids.remove(0);
//        mDatabase.child("rooms").child(id)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.getValue() != null) {
//                            Room roomInfo = dataSnapshot.getValue(Room.class);
//                            onResult.onResult(roomInfo);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.e("getRoomInfo", databaseError.toString());
//                        onResult.onResult(null);
//                    }
//                });
//    }

    public void getContactsInfo(List<String> ids, OnResult<User> onResult) {
        String id = ids.remove(0);
        mDatabase.child("users").child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            User userInfo = dataSnapshot.getValue(User.class);
                            onResult.onResult(userInfo);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("getContactInfo", databaseError.toString());
                        onResult.onResult(null);
                    }
                });
    }

    public void getListMessage(String roomId, OnResult<Message> onResult) {
        mDatabase.child("rooms/" + roomId + "/messages").orderByChild("timestamp").limitToLast(10)
                .addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.getValue() != null) {
                            Message message_value = dataSnapshot.getValue(Message.class);
                            onResult.onResult(message_value);
                        }
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
