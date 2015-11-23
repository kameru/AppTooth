package com.example.miri1.apptooth;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class AppViewActivity extends Activity {
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_view);

        DBManager dbManager = new DBManager(this);
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Intent intent = getIntent();

        String deviceKey = intent.getStringExtra("address");
        String deviceName = intent.getStringExtra("name");

        actionBar = getActionBar();

        Cursor appCursor = db.rawQuery("SELECT * FROM apps WHERE deviceId = '" + deviceKey + "' ORDER BY runningTime DESC;", null);

        if (appCursor.getCount() != 0) {
            actionBar.setTitle(deviceName);
            final ArrayList<AppInfo> infoList = new ArrayList<>();

            while (appCursor.moveToNext()) {
                infoList.add(new AppInfo(appCursor.getString(0), appCursor.getString(2), appCursor.getInt(3)));
            }

            ListView listView = (ListView) findViewById(R.id.appListView);
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
        appCursor.close();
        db.close();
    }
}
