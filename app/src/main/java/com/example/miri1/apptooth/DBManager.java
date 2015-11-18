package com.example.miri1.apptooth;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by miri1 on 2015-11-17.
 */
public class DBManager extends SQLiteOpenHelper{
    private static final String DB_NAME = "BTList.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_DEVICE = "devices";
    private static final String TABLE_APPS = "apps";

    private static final String KEY_DEVICE_ID = "id";
    private static final String KEY_DEVICE_NAME = "name";

    private static final String KEY_APP_ID = "id";
    private static final String KEY_DEVICE_ID_FK = "deviceId";
    private static final String KEY_APP_NAME = "name";
    private static final String KEY_APP_TIME = "runningTime";

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DEVICE_TABLE = "CREATE TABLE" + TABLE_DEVICE +
                "(" +
                    KEY_DEVICE_ID + " INTEGER PRIMARY KEY," +
                    KEY_DEVICE_NAME + " TEXT" +
                ")";

        String CREATE_APP_TABLE = " CREATE TABLE" + TABLE_APPS +
                "(" +
                    KEY_APP_ID + " INTEGER PRIMARY KEY," +
                    KEY_DEVICE_ID_FK + " INTEGER REFERENCE, " +
                    KEY_APP_NAME + " TEXT," +
                    KEY_APP_TIME + " INTEGER" +
                ")";

        db.execSQL(CREATE_DEVICE_TABLE);
        db.execSQL(CREATE_APP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_DEVICE);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_APPS);
            onCreate(db);
        }
    }
}
