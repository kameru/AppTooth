package com.example.miri1.apptooth;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBManager dbManager = new DBManager(this);
        final SQLiteDatabase db = dbManager.getWritableDatabase();

        Intent intent = getIntent();

        final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        actionBar = getActionBar();
        ArrayList<String> DeviceName = new ArrayList<>();
        final ArrayList<DeviceInfo> deviceList = new ArrayList<>();

        if(device == null) {
            final Cursor deviceCursor = db.rawQuery("SELECT * FROM devices;", null);
            while (deviceCursor.moveToNext()) {
                DeviceName.add(deviceCursor.getString(1));
                deviceList.add(new DeviceInfo(deviceCursor.getString(0),deviceCursor.getString(1)));
            }

            final ListView listView = (ListView) findViewById(R.id.deviceListView);
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, DeviceName);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startAppList(deviceList.get(position).getDeviceID(), deviceList.get(position).getDeviceName());
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Delete?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.execSQL("DELETE FROM apps WHERE id = '" + deviceList.get(position).getDeviceID() + "';");
                            db.execSQL("DELETE FROM devices WHERE deviceId = '" + deviceList.get(position).getDeviceID() + "';");
                            deviceList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
            deviceCursor.close();
        }
        if(device != null) {
            String deviceKey = device.getAddress();
            String deviceName = device.getName();
            startAppList(deviceKey,deviceName);
        }
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void startAppList(String key, String name) {
        Intent startAppList = new Intent(this, AppViewActivity.class);
        startAppList.putExtra("name",name);
        startAppList.putExtra("address", key);
        startActivity(startAppList);
    }
}
