package com.zdjer.utils.view.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;

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


}
