package com.example.miri1.apptooth;

/**
 * Created by miri1 on 2015-11-23.
 */
public class DeviceInfo {
    String deviceID;
    String deviceName;

    public DeviceInfo(String deviceID, String deviceName) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
