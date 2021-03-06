package com.example.miri1.apptooth;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by miri1 on 2015-11-15.
 */
public class BluetoothService extends IntentService {
    private HashMap<String,AppInfo> info;
    private ActivityManager activityManager;
    private Boolean running = true;
    private String launcher;
    private String deviceId;

    SQLiteDatabase db;
    DBManager dbManager;

    public BluetoothService() {
        super("BluetoothService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        launcher = resolveInfo.activityInfo.packageName;
        info = new HashMap<>();

        dbManager = new DBManager(this);
        db = dbManager.getWritableDatabase();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        deviceId = intent.getStringExtra("address");
        setData();

        final Thread mThread = new Thread() {
            @Override
            public void run() {
                while (running){
                    long startTime = System.currentTimeMillis();
                    activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
                    String packageName = activityManager.getRunningAppProcesses().get(0).processName;
                    if (!info.containsKey(packageName) &&
                            !packageName.equals("com.example.miri1.apptooth") &&
                            !packageName.equals(launcher)  &&
                            !packageName.equals("com.android.bluetooth")) {
                        info.put(packageName, new AppInfo(packageName,0));
                        info.get(packageName).setAppName(getAppName(packageName));
                    }

                    if(info.containsKey(packageName))
                        info.get(packageName).addRunningTime();

                    long runTime = System.currentTimeMillis() - startTime;
                    SystemClock.sleep(1000-runTime);
                }
            }
        };
        mThread.run();
    }

    @Override
    public void onDestroy() {
        running = false;
        Collection value = info.values();
        Iterator itr = value.iterator();
        Cursor cursor = null;
        String sql;

        while(itr.hasNext()){
            AppInfo appInfo = (AppInfo) itr.next();
            cursor = db.rawQuery("SELECT * FROM apps " +
                    "WHERE pName = '"+ appInfo.getPackageName()+ "' and deviceId = '"+ deviceId + "';",null );
            cursor.moveToFirst();
            if(cursor.getCount() != 0)
            {
                sql = "UPDATE apps SET runningTime = '" + appInfo.getRunningTime() +
                        "' WHERE pName = '" + appInfo.getPackageName() + "' and deviceId = '"+ deviceId +"';";
                db.execSQL(sql);
            }
            else{
                ContentValues values = new ContentValues();
                values.put("pName", appInfo.getPackageName());
                values.put("deviceId", deviceId);
                values.put("name", appInfo.getAppName());
                values.put("runningTime", appInfo.getRunningTime());
                db.insert("apps",null,values);
            }
        }
        super.onDestroy();

        if(cursor != null) {
            cursor.close();
        }
    }

    public String getAppName(String packageName) {
        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    public void setData() {
        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM apps " +
                "WHERE deviceId = '" + deviceId + "';", null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            AppInfo appInfo = new AppInfo(cursor.getString(1), cursor.getInt(4));
            appInfo.setAppName(cursor.getString(3));
        }
        cursor.close();
    }
}
