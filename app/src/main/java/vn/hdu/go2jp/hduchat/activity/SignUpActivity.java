package vn.hdu.go2jp.hduchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.base.BaseActivity;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.util.ToastUtil;

public class SignUpActivity extends BaseActivity {
private Button btnSignUp;
private EditText edEmail;
private EditText edPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSignUp = findViewById(R.id.btnSignUp);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        findViewById(R.id.edEmail);
        btnSignUp.setOnClickListener(view -> {
            getDialog().showProgressDialog(R.string.msg_log_in_wait);
            String email = edEmail.getText().toString();
            String password = edPassword.getText().toString();
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                FireBaseUtil.getInstance().signUpWithEmail(email, password, isSuccess -> {
                    if (isSuccess) {
                        getDialog().dismissDialog();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
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
