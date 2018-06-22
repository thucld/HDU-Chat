package vn.hdu.go2jp.hduchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.util.ToastUtil;


public class LoginActivity extends AppCompatActivity {
    private EditText edEmail;
    private EditText edPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvents();
    }

    private void initEvents() {
        btnSignIn.setOnClickListener(view -> {
            String email = edEmail.getText().toString();
            String password = edPassword.getText().toString();
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                FireBaseUtil.getInstance().signInWithEmail(email, password, isSuccess -> {
                    if (isSuccess) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        new ToastUtil().showLong(getApplicationContext(), getString(R.string.log_in_fail));
                    }
                });
            }
        });
    }

    private void initView() {
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
    }
}
