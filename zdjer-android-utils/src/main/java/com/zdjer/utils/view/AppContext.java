package com.zdjer.utils.view;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.base.BaseApplication;

import java.util.UUID;

/**
 * 全局应用程序类
 * <p/>
 * Created by zdj on 16/4/11.
 */
public class AppContext extends BaseApplication {

    public static final int PAGE_SIZE = 5;//默认分页大小

    private static AppContext appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    private void init() {
        //AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        //PersistentCookieStore persistentCookieStore = new PersistentCookieStore(this);

        //asyncHttpClient.setCookieStore(persistentCookieStore);
        //AsyncHttpClientHelper.setAsyncHttpClient(asyncHttpClient);
        //AsyncHttpClientHelper.setCookie(AsyncHttpClientHelper.getCookie(this));
    }

    public static AppContext getInstance() {
        return appContext;
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
