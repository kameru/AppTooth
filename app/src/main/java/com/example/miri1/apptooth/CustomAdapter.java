package com.example.miri1.apptooth;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by miri1 on 2015-11-22.
 */
public class CustomAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<AppInfo> appInfo;

    public CustomAdapter(Context context, int resource, ArrayList<AppInfo> appInfo) {
        super(context, resource, appInfo);
        this.context = context;
        this.resource = resource;
        this.appInfo = appInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        Drawable icon = null;

        try {
            icon = context.getPackageManager().getApplicationIcon(appInfo.get(position).getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView nameView = (TextView) convertView.findViewById(R.id.textView);
        TextView timeView = (TextView) convertView.findViewById(R.id.textView2);
        ImageView iconView = (ImageView) convertView.findViewById(R.id.imageView);

        nameView.setText(appInfo.get(position).getAppName());
        timeView.setText(appInfo.get(position).getRunningTime() + "s");
        iconView.setImageDrawable(icon);

        return convertView;
    }



}
