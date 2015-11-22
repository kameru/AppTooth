package com.example.miri1.apptooth;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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
            Cursor appCursor = db.rawQuery("SELECT * FROM apps WHERE deviceId = '" + deviceKey + "';", null);

            deviceCursor.moveToFirst();
            appCursor.moveToFirst();
            if (deviceCursor.getCount() != 0 && appCursor.getCount() != 0) {
                String deviceName = deviceCursor.getString(1);

                actionBar.setTitle(deviceName);

                ArrayList<String> appList = new ArrayList<>();

                while (appCursor.moveToNext()) {
                    appList.add(appCursor.getString(2));
                }


                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, appList);
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapter);
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
