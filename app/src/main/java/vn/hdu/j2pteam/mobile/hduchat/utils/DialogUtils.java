package vn.hdu.j2pteam.mobile.hduchat.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Admin on 7/19/2017.
 */

public class DialogUtils {
    private final AlertDialog mAlertDialog;
    private final ProgressDialog mProgressDialog;

    public DialogUtils(Context context) {
        mAlertDialog = new AlertDialog.Builder(context).create();
        mAlertDialog.setCancelable(false);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
    }

//    public void showOkDialog(int messageId) {
//        showOkDialog(mProgressDialog.getContext().getString(messageId));
//    }

//    public void showOkDialog(String message) {
//        showOkDialog("", message);
//    }

//    public void showOkDialog(String title, String message) {
//        showOkDialog(title, message, (dialogInterface, i) -> dialogInterface.dismiss());
//    }
//
//    public void showOkDialog(String message, DialogInterface.OnClickListener clickListener) {
//        showOkDialog("", message, clickListener);
//    }
//
//    public void showOkDialog(String title, String message, DialogInterface.OnClickListener clickListener) {
//        if (mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
//        mAlertDialog.setTitle(title);
//        mAlertDialog.setMessage(message);
//        mAlertDialog.setButton(BUTTON_POSITIVE, Constants.OK, clickListener);
//        if (!mAlertDialog.isShowing()) {
//            mAlertDialog.show();
//        }
//    }
//
//    public void showOkDialog(String title, String message,
//                             DialogInterface.OnClickListener positiveClick,
//                             DialogInterface.OnClickListener negativeClick) {
//        if (mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
//        mAlertDialog.setTitle(title);
//        mAlertDialog.setMessage(message);
//        mAlertDialog.setButton(BUTTON_NEGATIVE, Constants.CANCEL, negativeClick);
//        mAlertDialog.setButton(BUTTON_POSITIVE, Constants.OK, positiveClick);
//        if (!mAlertDialog.isShowing()) {
//            mAlertDialog.show();
//        }
//    }

    public void showProgressDialog(int messageId) {
        showProgressDialog(mProgressDialog.getContext().getString(messageId));
    }

    public void showProgressDialog(String message) {
        showProgressDialog("", message);
    }

    public void showProgressDialog(String title, String message) {
        if (mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismissDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

}
