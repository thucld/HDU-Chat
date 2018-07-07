package vn.hdu.go2jp.hduchat.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import vn.hdu.go2jp.hduchat.base.OnResult;
import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;
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

    public static void getThisUser(final OnResult<User> onResult) {
        if (thisUser == null) {
            mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    thisUser = dataSnapshot.getValue(User.class);
                    onResult.onResult(thisUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    onResult.onResult(thisUser);
                }
            });
        } else {
            onResult.onResult(thisUser);
        }
    }

    public static synchronized FireBaseUtil getInstance() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (instance == null) {
            instance = new FireBaseUtil();
        }
        return instance;
    }

    public boolean isLogin() {
        return user != null;
    }

    public static boolean signOut() {
        try {
            auth.signOut();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void signUpWithEmail(String email, String password, OnResult<Boolean> onResult) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        onResult.onResult(task.isSuccessful());
                        if (task.isSuccessful()) {
                            writeNewUser(user.getUid(), password, null, null, null, email, null, null, null);
                        }
                    }
                });
    }

    public void signInWithEmail(String email, String password, OnResult<Boolean> onResult) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        onResult.onResult(task.isSuccessful());
                    }
                });
    }

    public static void writeNewUser(String userId, String passWord, String userName, List<String> listRoomId, String phoneNumber, String email, List<String> contactId, String avatarPath, String note) {
        User user = new User(userId, passWord, userName, listRoomId, phoneNumber, email, contactId, avatarPath, note);
        mDatabase.child("users").child(userId).setValue(user);
    }

    public static void addContact(String uId) {
        mDatabase.child("users").child(user.getUid()).child("contacts").push().setValue(uId);
    }

    public static void sendMessage(String roomId, Message message, OnResult<Boolean> status) {
        mDatabase.child("rooms").child(roomId).child("messages").push().setValue(message)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        status.onResult(task.isSuccessful());
                        mDatabase.child("rooms").child(roomId).child("lastMessage").setValue(message);
                    }
                });
    }

    public void getListContact(OnResult<List<User>> onResult) {
        List<User> listUser = new ArrayList<>();
        mDatabase.child("users").child(user.getUid()).child("contacts")
                .addValueEventListener(new ValueEventListener() {
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
                                instance.getContactInfo(listContactID, user -> {
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

//    public void getListContacts(OnResult<List<User>> onResult){
//        mDatabase.child("users").child(user.getUid()).child("contacts")
//                .addChildEventListener(listenerContacts);
//    }

    public static void getListRoom(OnResult<List<Room>> onResult) {
        List<Room> listRoom = new ArrayList<>();
        mDatabase.child("users").child(user.getUid()).child("roomsId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            HashMap mapRecord = (HashMap) dataSnapshot.getValue();

                            Iterator listKey = mapRecord.keySet().iterator();
                            while (listKey.hasNext()) {
                                String key = listKey.next().toString();
                                listRoomID.add(mapRecord.get(key).toString());
                            }
                            List<String> newList = new ArrayList<>(listRoomID);
                            while (listRoomID.size() > 0) {
                                getRoomInfo(listRoomID, room -> {
                                    listRoom.add(room);
                                    if (listRoom.size() == newList.size()) {
                                        onResult.onResult(listRoom);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onResult.onResult(listRoom);
                    }
                });
    }

    public void getContactInfo(List<String> ids, OnResult<User> onResult) {
        String id = ids.remove(0);
        mDatabase.child("users").child(id).addValueEventListener(new ValueEventListener() {
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

    public static void getRoomInfo(List<String> ids, OnResult<Room> onResult) {
        String id = ids.remove(0);
        mDatabase.child("rooms").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
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


    public static void test() {
        String uId = FirebaseAuth.getInstance().getUid();
        sendMessage("roomtest", new Message("Test function", new Date(), UserType.SELF, Status.SENT), new OnResult<Boolean>() {
            @Override
            public void onResult(Boolean aBoolean) {
                if(aBoolean){
                    Log.i("my_sendMessage","Successful");
                }
            }
        });
    }
}
