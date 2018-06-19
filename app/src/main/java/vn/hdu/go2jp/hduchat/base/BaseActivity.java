package vn.hdu.go2jp.hduchat.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import vn.hdu.go2jp.hduchat.listener.OnBackPressListener;
import vn.hdu.go2jp.hduchat.util.DialogHelper;

public class BaseActivity extends AppCompatActivity {

    private DialogHelper dialog;

    public DialogHelper getDialog() {
        return dialog;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new DialogHelper(this);
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
