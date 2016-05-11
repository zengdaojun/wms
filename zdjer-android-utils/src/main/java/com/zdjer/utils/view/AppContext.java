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

    public static final int PAGE_SIZE = 20;//默认分页大小

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



}
