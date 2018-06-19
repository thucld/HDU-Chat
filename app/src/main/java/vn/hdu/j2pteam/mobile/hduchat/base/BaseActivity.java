package vn.hdu.j2pteam.mobile.hduchat.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import vn.hdu.j2pteam.mobile.hduchat.utils.DialogUtils;

/**
 * Created by Admin on 7/17/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private DialogUtils mDialogHelper;

    public DialogUtils getDialoger() {
        return mDialogHelper;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogHelper = new DialogUtils(this);
    }

    @Override
    public void onBackPressed() {
        handleOnBackPress();
    }

    private void handleOnBackPress() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof OnBackPressListener) {
                OnBackPressListener onBackPressListener = (OnBackPressListener) fragment;
                onBackPressListener.onBackPressListener();
            }
        }
    }

    public void replaceFragment(Fragment fragment, int containerId, boolean handleBackPress) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, fragment);
        if (handleBackPress) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, int containerId) {
        replaceFragment(fragment, containerId, true);
    }

}
