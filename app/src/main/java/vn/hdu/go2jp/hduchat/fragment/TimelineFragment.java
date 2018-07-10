package vn.hdu.go2jp.hduchat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.hdu.go2jp.hduchat.R;

/**
 * Where to show Timeline.
 */
public class TimelineFragment extends Fragment {

    //    TextView tvTest;
    View llEmptyTimeline;
    boolean isEmpty = true;

    public TimelineFragment() {
        // Required layout_empty_room public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        llEmptyTimeline = view.findViewById(R.id.llEmptyTimeline);
//        tvTest = view.findViewById(R.id.tvTest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (llEmptyTimeline != null /*&& tvTest != null*/) {
            llEmptyTimeline.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
//            tvTest.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
    }
}
