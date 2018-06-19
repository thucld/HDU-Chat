package vn.hdu.j2pteam.mobile.hduchat.base;

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

import vn.hdu.j2pteam.mobile.hduchat.utils.DialogUtils;


/**
 * Created by Admin on 7/17/2017.
 */

public abstract class BaseFragment extends Fragment implements OnBackPressListener {

    private Activity baseActivity;
    private DialogUtils mDialogHelper;
    private boolean mIsKeepView;
    private View mContentView;

    public BaseFragment() {
        mIsKeepView = true;
    }

    private boolean isKeepView() {
        return mIsKeepView;
    }

    public void setKeepView(boolean mIsKeepView) {
        this.mIsKeepView = mIsKeepView;
        if (!mIsKeepView) {
            mContentView = null;
        }
    }

    protected DialogUtils getDialoger() {
        return mDialogHelper;
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
            mDialogHelper = ((BaseActivity) baseActivity).getDialoger();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = mContentView;
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
            mContentView = view;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        baseActivity = null;
        if (mDialogHelper != null) {
            mDialogHelper.dismissDialog();
        }
    }

    protected void handleOnBackPress() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            // we can finish the base activity since we have no other fragments
            baseActivity.finish();
        } else {
            fragmentManager.popBackStackImmediate();
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
