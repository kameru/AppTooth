package com.example.miri1.apptooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by miri1 on 2015-11-14.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent runActivity = new Intent(context, MainActivity.class);
        Intent runService = new Intent(context, BluetoothService.class);

        if(action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
            runActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(runActivity);
            context.startService(runService);
        }

        if(action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
            context.stopService(runService);
        }
    }
}
