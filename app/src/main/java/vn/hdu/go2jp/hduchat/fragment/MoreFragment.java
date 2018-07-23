package vn.hdu.go2jp.hduchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.activity.ProfileActivity;
import vn.hdu.go2jp.hduchat.activity.ScannerActivity;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

/**
 * Where to show more features.
 */
public class MoreFragment extends Fragment {

    FrameLayout btnProfile;
    CircleImageView civProfile;
    ImageView btnQrCode;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initViews(view);
        getData();
        setupEvents();
        return view;
    }

    private void initViews(View view) {
        civProfile = view.findViewById(R.id.civProfile);
        btnProfile = view.findViewById(R.id.btnProfile);
        btnQrCode = view.findViewById(R.id.btnQrCode);
    }

    private void getData() {
        FireBaseUtil.getInstance().getThisUser(user ->
                Glide.with(this.getActivity())
                        .load(user.getAvatarPath())
                        .error(R.drawable.img_user_avatar_default_3)
                        .fitCenter()
                        .into(civProfile));
    }

    private void setupEvents() {
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        btnQrCode.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            startActivity(intent);
        });
    }

}
