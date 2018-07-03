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
import java.util.List;

import vn.hdu.go2jp.hduchat.base.OnResult;
import vn.hdu.go2jp.hduchat.data.models.User;

public class FireBaseUtil {
    private static FirebaseUser user;
    private static FirebaseAuth auth;
    private static DatabaseReference mDatabase;
    private static ArrayList<User> listUserInfo;
    private static ArrayList<String> listFriendID = null;
    private static FireBaseUtil instance;

    public static synchronized FireBaseUtil getInstance() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listUserInfo = new ArrayList<User>();

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

    public static void signUpWithEmail(String email, String password, OnResult<Boolean> onResult) {
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

    public static void signInWithEmail(String email, String password, OnResult<Boolean> onResult) {
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

//    public static void getListContact() {
//        mDatabase.child("users").child(user.getUid()).child("contacts")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.getValue() != null) {
//                            HashMap mapRecord = (HashMap) dataSnapshot.getValue();
//                            Iterator listKey = mapRecord.keySet().iterator();
//                            while (listKey.hasNext()) {
//                                String key = listKey.next().toString();
//                                listFriendID.add(mapRecord.get(key).toString());
//                            }
//                            getContactsInfo(0);
//                        } else {
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//    }

    /**
     * Truy cap bang user lay thong tin id nguoi dung
     */
//    private static ArrayList<User> dataContacts = null;
//
//    private static void getContactsInfo(final int index) {
//        if (index == listFriendID.size()) {
//            //save list friend
//
//        } else {
//            final String id = listFriendID.get(index);
//            mDatabase.child("user").child(id)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.getValue() != null) {
//                                User userInfo = new User();
//                                HashMap mapUserInfo = (HashMap) dataSnapshot.getValue();
//                                userInfo.setEmail((String) mapUserInfo.get("email"));
//                                userInfo.setUserId((String) mapUserInfo.get("userId"));
//                                userInfo.setAvatarPath((String) mapUserInfo.get("avatarPath"));
//                                userInfo.setNote((String) mapUserInfo.get("note"));
//                                userInfo.setPhoneNumber((String) mapUserInfo.get("phoneNumber"));
//                                userInfo.setUserName((String) mapUserInfo.get("userName"));
//                                dataContacts.add(userInfo);
//                            }
//                            getContactsInfo(index + 1);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//        }
//    }
    public static void getListContact(OnResult<List<User>> onResult) {
        List<User> userList = new ArrayList<>();
        mDatabase.child("users").child(user.getUid()).child("contacts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            HashMap mapRecord = (HashMap) dataSnapshot.getValue();
                            //Log.i("my_HashMap", mapRecord.toString());
                            int d= mapRecord.size();
//                            while ()
                            for (Object contactId : mapRecord.values()) {
                                getContactsInfo(contactId.toString(), userInfo -> {
                                    userList.add(userInfo);
                                });
                            }
                            Log.i("my_userList", String.valueOf(userList.size()));
                            onResult.onResult(userList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        onResult.onResult(userList);
                    }
                });

    }

    public static void getContactsInfo(String uId, OnResult<User> onResult) {
        mDatabase.child("users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void getListRoom(OnResult<ArrayList<String>> onResult) {
        ArrayList<String> listRooms = new ArrayList<>();
        mDatabase.child("users").child(user.getUid()).child("roomsId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap mapRoom = (HashMap) dataSnapshot.getValue();
                for (Object roomId : mapRoom.values()) {
                    listRooms.add(roomId.toString());
                }
                onResult.onResult(listRooms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static void test() {
//        getListContact();
    }
}
