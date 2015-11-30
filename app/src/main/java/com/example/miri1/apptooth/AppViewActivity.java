package com.example.miri1.apptooth;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class AppViewActivity extends Activity {
    ActionBar actionBar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_view);

        DBManager dbManager = new DBManager(this);
        final SQLiteDatabase db = dbManager.getReadableDatabase();

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Delete?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            infoList.remove(position);
                            adapter.setNotifyOnChange(true);
                            db.execSQL("DELETE FROM apps WHERE id = '" + infoList.get(position).getPackageName() + "';");
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create();
                    return true;
                }
            });
        }
        appCursor.close();
        db.close();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AppView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.miri1.apptooth/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AppView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.miri1.apptooth/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
