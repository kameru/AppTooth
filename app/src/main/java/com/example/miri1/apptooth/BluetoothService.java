package com.example.miri1.apptooth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by miri1 on 2015-11-15.
 */
public class BluetoothService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
