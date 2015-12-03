package com.example.miri1.apptooth;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class AppViewActivity extends Activity {
    ActionBar actionBar;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_view);

        DBManager dbManager = new DBManager(this);
        db = dbManager.getWritableDatabase();

        Intent intent = getIntent();

        final String deviceKey = intent.getStringExtra("address");
        String deviceName = intent.getStringExtra("name");

        actionBar = getActionBar();

        Cursor appCursor = db.rawQuery("SELECT * FROM apps WHERE deviceId = '" + deviceKey + "' ORDER BY runningTime DESC;", null);

        if (appCursor.getCount() != 0) {
            actionBar.setTitle(deviceName);
            final ArrayList<AppInfo> infoList = new ArrayList<>();

            while (appCursor.moveToNext()) {
                infoList.add(new AppInfo(appCursor.getString(1), appCursor.getString(3), appCursor.getInt(4)));
            }

            ListView listView = (ListView) findViewById(R.id.appListView);
            final CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_row_layout, infoList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PackageManager pm = getPackageManager();
                    Intent launchIntent = pm.getLaunchIntentForPackage(infoList.get(position).getPackageName());
                    startActivity(launchIntent);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AppViewActivity.this);
                    builder.setTitle("Delete?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.execSQL("DELETE FROM apps WHERE pName = '" + infoList.get(position).getPackageName() + "' and deviceId ='" + deviceKey + "';");
                            infoList.remove(position);
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
            appCursor.close();
        }
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

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
