package vn.hdu.go2jp.hduchat.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public ToastUtil() {
    }

    public void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
