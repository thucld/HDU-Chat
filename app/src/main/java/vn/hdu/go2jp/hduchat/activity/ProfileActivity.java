package vn.hdu.go2jp.hduchat.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.listener.OnResult;
import vn.hdu.go2jp.hduchat.model.data.User;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class ProfileActivity extends AppCompatActivity {

    public User user;
    public boolean changeProfile = false;

    private ImageView btnBack;
    private CircleImageView civAvatar;
    private TextView tvUserName;
    private TextView tvEmail;
    private LinearLayout btnHome;
    private CheckBox cbShareProfile;
    private TextView tvDisplayName;
    private TextView tvStatus;
    private CheckBox cbAddById;
    private TextView titleQRCode;
    private TextView btnBirthDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        setUser();
        setEvents();
        setData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        civAvatar = findViewById(R.id.civAvatar);
        tvUserName = findViewById(R.id.username);
        tvEmail = findViewById(R.id.tvEmail);
        btnHome = findViewById(R.id.btnHome);
        cbShareProfile = findViewById(R.id.cbShareProfile);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        tvStatus = findViewById(R.id.tvStatus);
        cbAddById = findViewById(R.id.cbAddByID);
        titleQRCode = findViewById(R.id.titleQrCode);
        btnBirthDay = findViewById(R.id.btnBirthDay);
    }

    private void setUser() {
        FireBaseUtil.getInstance().getThisUser(new OnResult<User>() {
            @Override
            public void onResult(User user_) {
                user = user_;
            }
        });
    }

    private void setEvents() {
        btnBack.setOnClickListener(click -> finish());
    }

    @SuppressLint("ResourceAsColor")
    private void setData() {
        tvUserName.setText(user.getUserId());
        tvEmail.setText(user.getEmail());
        tvDisplayName.setText(user.getUserName());
        tvDisplayName.setTextColor(R.color.blue_steel);
        if (!TextUtils.isEmpty(user.getAvatarPath())) {
            Glide.with(this)
                    .load(user.getAvatarPath())
                    .into(civAvatar);
        }
    }

    private void updateProfile() {
        // thuoc tinh userId dang su dung lam dinh danh nen ko doi truong nay
//        user.setUserName("testerChange");
//        changeProfile = true;

        if (changeProfile) {
            FireBaseUtil.getInstance().updateUser(user, new OnResult<Boolean>() {
                @Override
                public void onResult(Boolean isSuccess) {
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "Update profile success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update profile failure", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateProfile();
    }

    // TODO add feature Edit Profile
}
