package vn.hdu.go2jp.hduchat.util;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseUtil {
    private static FirebaseUser user;
    private static FirebaseAuth auth;

    private static void setData() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public static boolean isLogin() {
        setData();
        return user != null;
    }

    public static boolean signOut() {
        setData();
        try {
            auth.signOut();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void signInWithEmail(String email, String password) {
        setData();
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i("LOGIN:", "Login successful");
                    }
                });
    }
}
