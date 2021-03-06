package vn.hdu.go2jp.hduchat.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.hdu.go2jp.hduchat.listener.OnBackPressListener;
import vn.hdu.go2jp.hduchat.util.DialogHelper;

public abstract class BaseFragment extends Fragment implements OnBackPressListener {

    private Activity baseActivity;
    private DialogHelper dialog;
    private boolean isKeepView;
    private View contentView;

    public BaseFragment() {
        isKeepView = true;
    }

    private boolean isKeepView() {
        return isKeepView;
    }

    public void setKeepView(boolean isKeepView) {
        this.isKeepView = isKeepView;
        if (!isKeepView) {
            contentView = null;
        }
    }

    protected DialogHelper getDialog() {
        return dialog;
    }

    @Override
    public void onBackPressListener() {
        if (isVisible()) {
            handleOnBackPress();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (Activity) context;
        if (baseActivity instanceof BaseActivity) {
            dialog = ((BaseActivity) baseActivity).getDialog();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = this.contentView;
        if (contentView == null) {
            contentView = createView(inflater, container, savedInstanceState);
        }
        return contentView;
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isKeepView()) {
            contentView = view;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        baseActivity = null;
        if (dialog != null) {
            dialog.dismissDialog();
        }
    }

    protected void handleOnBackPress() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            if (fragmentManager.getBackStackEntryCount() == 0) {
                // we can finish the base activity since we have no other fragments
                baseActivity.finish();
            } else {
                fragmentManager.popBackStackImmediate();
            }
        }
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        int containerId = getId();
        ((BaseActivity) baseActivity).replaceFragment(fragment, containerId, addToBackStack);
    }

    protected void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, true);
    }

    public void addFragmentChild(Fragment childFragment, int containerId) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, childFragment);
        transaction.commit();
    }

}
