package com.zdjer.win.utils;

import android.util.Log;

/**
 * Created by zdj on 16/3/23.
 */
public class SystemHelper {
    public static boolean isKT40() {
        String kt40 = "kt40";
        String model = android.os.Build.MODEL;
        Log.i("model:", model);
        return model != null && model.toLowerCase().equals(kt40);
    }
}
