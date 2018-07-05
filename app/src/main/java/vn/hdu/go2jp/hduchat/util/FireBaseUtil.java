package vn.hdu.go2jp.hduchat.util;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import vn.hdu.go2jp.hduchat.base.OnResult;

public class FireBaseUtil {
    private static User thisUser;
    private static FirebaseUser user;
    private static FirebaseAuth auth;
    private static DatabaseReference mDatabase;
    private static List<String> listContactID = new ArrayList<>();
    private static List<String> listRoomID = new ArrayList<>();
    private static FireBaseUtil instance;

    public static User getThisUser() {
        if (thisUser == null) {
            List<String> id = new ArrayList<>();
            id.add(user.getUid());
            getContactInfo(id, new OnResult<User>() {
                @Override
                public void onResult(User user) {
                    thisUser = user;
                }
            });
        }
        return thisUser;
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

    public static boolean isLogin() {
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

    public static void getListContact(OnResult<List<User>> onResult) {
        List<User> listUser = new ArrayList<>();
        mDatabase.child("users").child(user.getUid()).child("contacts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
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
                                getContactInfo(listContactID, user -> {
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

    public static void getContactInfo(List<String> ids, OnResult<User> onResult) {
        String id = ids.remove(0);
        mDatabase.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User userInfo = new User();
                    HashMap mapUserInfo = (HashMap) dataSnapshot.getValue();
                    userInfo.setEmail((String) mapUserInfo.get("email"));
                    userInfo.setUserId((String) mapUserInfo.get("userId"));
                    userInfo.setAvatarPath((String) mapUserInfo.get("avatarPath"));
                    userInfo.setNote((String) mapUserInfo.get("note"));
                    userInfo.setPhoneNumber((String) mapUserInfo.get("phoneNumber"));
                    userInfo.setUserName((String) mapUserInfo.get("userName"));
                    Log.i("my_mapUserInfo", mapUserInfo.toString());
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
                    Room roomInfo = new Room();
                    HashMap mapRoomInfo = (HashMap) dataSnapshot.getValue();
                    roomInfo.setRoomId((String) mapRoomInfo.get("roomId"));
                    roomInfo.setTitle((String) mapRoomInfo.get("title"));
                    roomInfo.setLastMessage((String) mapRoomInfo.get("lastMessage"));
                    roomInfo.setContacts((HashMap<String, String>) mapRoomInfo.get("contacts"));
                    roomInfo.setMessages((HashMap<String, Message>) mapRoomInfo.get("messages"));
                    Log.i("my_mapUserInfo", mapRoomInfo.toString());
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
//        getListContact();
        getListRoom(new OnResult<List<Room>>() {
            @Override
            public void onResult(List<Room> rooms) {
                for (Room room : rooms) {
                    Log.i("my_room_getListRoom", room.getRoomId());
                }
            }
        });
    }
}
