package vn.hdu.go2jp.hduchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        checkSignInState();
        findViewById(R.id.btnSignUp).setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnSignIn).setOnClickListener(view -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }

    private void checkSignInState() {
        if (FireBaseUtil.getInstance().isLogin()) {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
