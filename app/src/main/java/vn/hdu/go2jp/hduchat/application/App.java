package vn.hdu.go2jp.hduchat.application;

import android.app.Application;
import android.os.Handler;

import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    private static App instance;
    public static volatile Handler applicationHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        instance = this;
        applicationHandler = new Handler(getInstance().getMainLooper());
    }

    public static App getInstance() {
        return instance;
    }
}
