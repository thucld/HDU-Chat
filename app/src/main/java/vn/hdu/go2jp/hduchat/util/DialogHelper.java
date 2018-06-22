package vn.hdu.go2jp.hduchat.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import vn.hdu.go2jp.hduchat.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class DialogHelper {
    private final AlertDialog alertDialog;
    private final ProgressDialog progressDialog;

    public DialogHelper(Context context) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }

    public void showOkDialog(int messageId) {
        showOkDialog(progressDialog.getContext().getString(messageId));
    }

    private void showOkDialog(String message) {
        showOkDialog("", message);
    }

    private void showOkDialog(String title, String message) {
        showOkDialog(title, message, (dialogInterface, i) -> dialogInterface.dismiss());
    }

    public void showOkDialog(String message, DialogInterface.OnClickListener clickListener) {
        showOkDialog("", message, clickListener);
    }

    private void showOkDialog(String title, String message, DialogInterface.OnClickListener clickListener) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(BUTTON_POSITIVE, alertDialog.getContext().getString(R.string.ok), clickListener);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void showOkDialog(String title, String message,
                             DialogInterface.OnClickListener positiveClick,
                             DialogInterface.OnClickListener negativeClick) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        Context context = alertDialog.getContext();
        alertDialog.setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), negativeClick);
        alertDialog.setButton(BUTTON_POSITIVE, context.getString(R.string.ok), positiveClick);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void showProgressDialog(int messageId) {
        showProgressDialog(progressDialog.getContext().getString(messageId));
    }

    private void showProgressDialog(String message) {
        showProgressDialog("", message);
    }

    private void showProgressDialog(String title, String message) {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dismissDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

}
