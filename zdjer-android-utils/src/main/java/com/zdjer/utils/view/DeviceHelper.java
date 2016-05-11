package com.zdjer.utils.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zdjer.utils.view.base.BaseApplication;

import java.io.File;

/**
 * Created by zdj on 16/4/12.
 */
public class DeviceHelper {
    public static float dpToPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160F);
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) BaseApplication.getContextExt().getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    /**
     * 是否有网络
     * @return 有网络，返回true；反之，返回false！
     */
    public static boolean hasInternet() {
        return ((ConnectivityManager) BaseApplication.getContextExt().getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists())
            return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获得VersionCode
     * @param packageName
     * @return
     */
    public static int getVersionCode(String packageName) {
        int versionCode = 0;
        try {
            versionCode = BaseApplication.getContextExt().getPackageManager()
                    .getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    /*@SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }*/

    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            return dm.density;
        } catch(Exception ex) {

        }
        return 1.0f;
    }

}
