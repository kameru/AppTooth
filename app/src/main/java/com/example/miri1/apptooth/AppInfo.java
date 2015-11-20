package com.example.miri1.apptooth;

import android.app.ActivityManager;

/**
 * Created by miri1 on 2015-11-19.
 */
public class AppInfo {
    private ActivityManager.RunningServiceInfo info;
    private int runningTime;

    public AppInfo(ActivityManager.RunningServiceInfo info, int runningTime) {
        this.info = info;
        this.runningTime = runningTime;
    }

    public ActivityManager.RunningServiceInfo getInfo() {
        return info;
    }

    public void setInfo(ActivityManager.RunningServiceInfo info) {
        this.info = info;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }
}
