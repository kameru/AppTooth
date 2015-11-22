package com.example.miri1.apptooth;

/**
 * Created by miri1 on 2015-11-19.
 */
public class AppInfo {
    private String packageName;
    private String appName;
    private int runningTime;

    public AppInfo(String packageName, int runningTime) {
        this.packageName = packageName;
        this.runningTime = runningTime;
    }

    public AppInfo(String packageName, String appName, int runningTime) {
        this.packageName = packageName;
        this.appName = appName;
        this.runningTime = runningTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void addRunningTime(){
        runningTime++;
    }
}
