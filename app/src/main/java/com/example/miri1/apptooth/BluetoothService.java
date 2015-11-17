package com.example.miri1.apptooth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by miri1 on 2015-11-15.
 */
public class BluetoothService extends Service {
    BluetoothReceiver receiver = new BluetoothReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
