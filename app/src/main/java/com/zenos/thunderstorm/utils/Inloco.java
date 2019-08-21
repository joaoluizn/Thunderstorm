package com.zenos.thunderstorm.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.inlocomedia.android.core.permissions.PermissionResult;
import com.inlocomedia.android.core.permissions.PermissionsListener;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.InLocoEngagementOptions;

import java.util.HashMap;

public class Inloco {


    private final static String[] REQUIRED_PERMISSIONS = { Manifest.permission.ACCESS_FINE_LOCATION };
    private static final String TAG = "Inloco";


    public static void initInlocoSDK(Context context, String appID, Boolean logFlag){
        InLocoEngagementOptions options = InLocoEngagementOptions.getInstance(context);

        // The App ID you obtained in the dashboard
        options.setApplicationId(appID);

        // Verbose mode; enables SDK logging, defaults to true.
        // Remember to set to false in production builds.
        options.setLogEnabled(logFlag);

        //Initialize the SDK
        InLocoEngagement.init(context, options);
    }


    public static void initInlocoRequestPermission(final Activity activity){
        final boolean askIfDenied = true; // Will prompt the user if he has previously denied the permission

        InLocoEngagement.requestPermissions(activity, REQUIRED_PERMISSIONS, askIfDenied, new PermissionsListener() {

            @Override
            public void onPermissionRequestCompleted(final HashMap<String, PermissionResult> authorized) {
                if (authorized.get(Manifest.permission.ACCESS_FINE_LOCATION).isAuthorized()) {
                    // Permission enabled
                    Log.d("Inloco", "Permission Granted");
                }
            }
        });
    }

}
