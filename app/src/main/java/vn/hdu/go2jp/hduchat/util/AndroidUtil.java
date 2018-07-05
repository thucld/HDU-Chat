package vn.hdu.go2jp.hduchat.util;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

import vn.hdu.go2jp.hduchat.application.App;

public class AndroidUtil {

    public static float density;
    public static int statusBarHeight = 0;
    private static Point displaySize = new Point();

    static {
        density = App.getInstance().getResources().getDisplayMetrics().density;
        checkDisplaySize();
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    private static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            App.applicationHandler.post(runnable);
        } else {
            App.applicationHandler.postDelayed(runnable, delay);
        }
    }

    private static void checkDisplaySize() {
        try {
            WindowManager manager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getSize(displaySize);
                }
            }
        } catch (Exception e) {
            // FileLog.e("msg", e);
        }
    }

    public static int getViewInset(View view) {
        if (view == null || Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        try {
            Field mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
            mAttachInfoField.setAccessible(true);
            Object mAttachInfo = mAttachInfoField.get(view);
            if (mAttachInfo != null) {
                Field mStableInsetsField = mAttachInfo.getClass().getDeclaredField("mStableInsets");
                mStableInsetsField.setAccessible(true);
                Rect insets = (Rect) mStableInsetsField.get(mAttachInfo);
                return insets.bottom;
            }
        } catch (Exception e) {
            // FileLog.e("tmessages", e);
        }
        return 0;
    }


}
