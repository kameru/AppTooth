package com.example.miri1.apptooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by miri1 on 2015-11-14.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        ContentValues values = new ContentValues();

        Intent runActivity = new Intent(context, MainActivity.class);
        Intent runService = new Intent(context, BluetoothService.class);

        DBManager dbManager = new DBManager(context);
        SQLiteDatabase db = dbManager.getWritableDatabase();
        Cursor cursor;

        if(action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            String address = device.getAddress();
            String name = device.getName();

            cursor = db.rawQuery("SELECT * FROM devices WHERE id = '" + address + "';",null);

            cursor.moveToFirst();
            if(cursor.getCount() == 0) {
                values.put("id", address);
                values.put("name", name);
                db.insert("devices", null, values);
            }

            runService.putExtra("address",address);

            runActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(runActivity);
            context.startService(runService);
        }

        if(action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
            context.stopService(runService);
        }
    }
}
