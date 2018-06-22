package vn.hdu.go2jp.hduchat.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.hdu.go2jp.hduchat.base.OnResult;

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

    public static void signInWithEmail(String email, String password, OnResult<Boolean> onResult) {
        setData();
//        auth.signInWithEmailAndPassword(email, password)
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        Log.i("LOGIN:", "Login successful");
//                        onResult.onResult(true);
//                    }
//                });
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() == true) {
                    onResult.onResult(true);
                } else {
                    onResult.onResult(false);
                }
            }
        });
    }

}
