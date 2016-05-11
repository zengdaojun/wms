package com.zdjer.utils.view.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.AppConfigHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;

import java.util.UUID;

/**
 * Created by zdj on 16/4/11.
 */
public class BaseApplication extends Application {

    static Context context;
    static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resources = context.getResources();

        SPHelper.setContext(context);
        ToastHelper.setContext(context);
    }

    /**
     * 获得上下文
     *
     * @return BaseApplication
     */
    public static synchronized BaseApplication getContextExt() {
        return (BaseApplication) context;
    }

    /**
     * 获得资源
     *
     * @return Resources
     */
    public static Resources getResourcesExt() {
        return resources;
    }


    /**
     * 获得String
     *
     * @param id 资源Id
     * @return String
     */
    public static String getStringExt(int id) {
        return resources.getString(id);
    }

    /**
     * 获得String
     *
     * @param id   资源Id
     * @param args 参数
     * @return String
     */
    public static String getStringExt(int id, Object... args) {
        return resources.getString(id, args);
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    public String getProperty(String key) {
        return AppConfigHelper.getAppConfig(this).getProperty(key);
    }

    public void setProperty(String key, String value) {
        AppConfigHelper.getAppConfig(this).setProperty(key, value);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfigHelper.CONF_APP_UNIQUEID);
        if (StringHelper.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfigHelper.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }
}
