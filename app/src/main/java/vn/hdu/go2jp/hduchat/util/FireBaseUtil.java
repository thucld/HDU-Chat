package vn.hdu.go2jp.hduchat.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.hdu.go2jp.hduchat.base.OnResult;

/**
 * Connect and manipulate FireBase APIs.
 */
public class FireBaseUtil {

    private static FireBaseUtil instance;
    private static FirebaseUser user;
    private static FirebaseAuth auth;

    private FireBaseUtil() {
    }

    public static synchronized  FireBaseUtil getInstance() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (instance == null) {
            instance = new FireBaseUtil();
        }
        return instance;
    }

    public boolean isLogin() {
        return user != null;
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

    public void signInWithEmail(String email, String password, OnResult<Boolean> onResult) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onResult.onResult(true);
                    } else {
                        onResult.onResult(false);
                    }
                });

//        auth.signInWithEmailAndPassword(email, password)
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        Log.i("LOGIN:", "Login successful");
//                        onResult.onResult(true);
//                    }
//                });

    }

}
