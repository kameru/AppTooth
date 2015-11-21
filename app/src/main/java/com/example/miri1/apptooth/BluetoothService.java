package com.example.miri1.apptooth;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;

import java.util.HashMap;

/**
 * Created by miri1 on 2015-11-15.
 */
public class BluetoothService extends IntentService {
    private HashMap<String,AppInfo> info;
    private ActivityManager activityManager;
    private Boolean running = true;
    private String launcher;

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

//        setdata
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Thread mThread = new Thread() {
            @Override
            public void run() {
                while (running){
                    long startTime = System.currentTimeMillis();
                    activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
                    String packageName = activityManager.getRunningAppProcesses().get(0).processName;
                    if(!info.containsKey(packageName) && (!packageName.equals("com.example.miri1.apptooth") || !packageName.equals(launcher))){
                        info.put(packageName,new AppInfo(packageName));
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
        super.onDestroy();
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
}
