package com.zdjer.wms.view;

import com.loopj.android.http.AsyncHttpClient;
import com.zdjer.utils.http.AsyncHttpClientHelper;
import com.zdjer.utils.view.base.BaseApplication;

/**
 * Created by zdj on 16/3/29.
 */
public class WmsApplication extends BaseApplication {
    // 在application的onCreate中初始化

    protected static String dbName = "";
    protected static String serverIP = "";

    @Override
    public void onCreate() {
        super.onCreate();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        AsyncHttpClientHelper.setAsyncHttpClient(asyncHttpClient);
        //x.Ext.init(this);
        //x.Ext.setDebug(true); // 是否输出debug日志
    }
}
