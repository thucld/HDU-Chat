package vn.hdu.go2jp.hduchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.base.BaseActivity;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.util.ToastUtil;

public class LoginActivity extends BaseActivity {
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkSignInState();
        initViews();
        setupEvents();
    }

    private void checkSignInState() {
        if (FireBaseUtil.getInstance().isLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initViews() {
        edtEmail = findViewById(R.id.edEmail);
        edtPassword = findViewById(R.id.edPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
    }

    private void setupEvents() {
        btnSignIn.setOnClickListener(view -> {
            getDialog().showProgressDialog(R.string.msg_log_in_wait);
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                FireBaseUtil.getInstance().signInWithEmail(email, password, isSuccess -> {
                    if (isSuccess) {
                        getDialog().dismissDialog();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        getDialog().dismissDialog();
                        new ToastUtil().showLong(getApplicationContext(), getString(R.string.log_in_fail));
                    }
                });
            } else {
                getDialog().dismissDialog();
            }
        });
    }
}
