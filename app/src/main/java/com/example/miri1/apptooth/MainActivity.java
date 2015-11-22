package com.example.miri1.apptooth;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBManager dbManager = new DBManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Intent intent = getIntent();

        String deviceKey = intent.getStringExtra("address");

        actionBar = getSupportActionBar();

        if(deviceKey != null) {
            Cursor deviceCursor = db.rawQuery("SELECT * FROM devices WHERE id = '" + deviceKey + "';", null);
            Cursor appCursor = db.rawQuery("SELECT * FROM apps WHERE deviceId = '" + deviceKey + "' ORDER BY runningTime DESC;", null);

            deviceCursor.moveToFirst();
            appCursor.moveToFirst();
            if (deviceCursor.getCount() != 0 && appCursor.getCount() != 0) {
                String deviceName = deviceCursor.getString(1);

                actionBar.setTitle(deviceName);

                final ArrayList<String> appList = new ArrayList<>();
                final ArrayList<AppInfo> infoList = new ArrayList<>();

                while (appCursor.moveToNext()) {
                    appList.add(appCursor.getString(2));
                    infoList.add(new AppInfo(appCursor.getString(0), appCursor.getString(2), appCursor.getInt(3)));
                }

                ListView listView = (ListView) findViewById(R.id.listView);
                CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_row_layout, infoList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PackageManager pm = getPackageManager();
                        Intent launchIntent = pm.getLaunchIntentForPackage(infoList.get(position).getPackageName());
                        startActivity(launchIntent);
                    }
                });
            }
        }
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
}
