package com.example.miri1.apptooth;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        final SQLiteDatabase db = dbManager.getReadableDatabase();

        Intent intent = getIntent();

        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        actionBar = getActionBar();
        ArrayList<String> DeviceName = new ArrayList<>();
        final ArrayList<DeviceInfo> deviceList = new ArrayList<>();

        if(device == null) {
            Cursor deviceCursor = db.rawQuery("SELECT * FROM devices;", null);
            while (deviceCursor.moveToNext()) {
                DeviceName.add(deviceCursor.getString(1));
                deviceList.add(new DeviceInfo(deviceCursor.getString(0),deviceCursor.getString(1)));
            }

            ListView listView = (ListView) findViewById(R.id.deviceListView);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, DeviceName);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startAppList(deviceList.get(position).getDeviceID(), deviceList.get(position).getDeviceName());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAppList(String key, String name) {
        Intent startAppList = new Intent(this, AppViewActivity.class);
        startAppList.putExtra("name",name);
        startAppList.putExtra("address", key);
        startActivity(startAppList);
    }
}
