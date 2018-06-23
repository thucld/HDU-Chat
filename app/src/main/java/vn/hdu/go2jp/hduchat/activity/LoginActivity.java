package vn.hdu.go2jp.hduchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.base.OnResult;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;


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
        if (FireBaseUtil.isLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                if (!"".equals(email) && !"".equals(password)) {
                    FireBaseUtil.signInWithEmail(email, password, new OnResult<Boolean>() {
                        @Override
                        public void onResult(Boolean aBoolean) {
                            if (aBoolean) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //message log in fail
                            }
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
    }
}
