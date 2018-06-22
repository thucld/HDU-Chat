package vn.hdu.go2jp.hduchat.util;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.hdu.go2jp.hduchat.data.models.User;

public class FireBaseUtil {
    private static FirebaseUser user;
    private static FirebaseAuth auth;
    private static DatabaseReference mDatabase;

    private static void setData() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static boolean isLogin() {
        setData();
        return user != null;
    }

    public static boolean signOut() {
        setData();
        try {
            auth.signOut();
            Log.i("my_SignOut", "SignOut Successful");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void signUpWithEmail(String email, String password) {
        setData();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i("my_SignUp", "SignUp Successful");
                        Log.i("my_User", auth.getCurrentUser().getUid());
                    }
                });
    }

    public static void signInWithEmail(String email, String password) {
        setData();
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i("my_SignIn:", "SignIn Successful");
                        Log.i("my_User", auth.getCurrentUser().getUid());
                    }
                });
    }

    public static void writeNewUser(String userId, String passWord, String userName, List<String> listRoomId, String phoneNumber, String email, List<String> contactId, String avatarPath, String note) {
        User user = new User(userId, passWord, userName, listRoomId, phoneNumber, email, contactId, avatarPath, note);
        mDatabase.child("users").child(userId).setValue(user);
    }
}
