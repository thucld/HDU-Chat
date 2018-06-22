package vn.hdu.go2jp.hduchat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import vn.hdu.go2jp.hduchat.fragment.ChatBoxFragment;
import vn.hdu.go2jp.hduchat.fragment.ChatListFragment;
import vn.hdu.go2jp.hduchat.fragment.ContactsFragment;
import vn.hdu.go2jp.hduchat.fragment.MoreFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> fragments, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        if (position >= 0 && position <= fragments.size()) {
            return fragments.get(position);
        } else {
            return null;
        }
//        switch (position) {
//            case 0:
//                return new ChatListFragment();
//            case 1:
//                return new ChatBoxFragment();
//            case 2:
//                return new ContactsFragment();
//            case 3:
//                return new MoreFragment();
//            default:
//                return null;
//        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
