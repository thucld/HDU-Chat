package vn.hdu.go2jp.hduchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.hdu.j2p.mobile.hduchat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatBoxFragment extends Fragment {


    public ChatBoxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_box, container, false);
    }

}