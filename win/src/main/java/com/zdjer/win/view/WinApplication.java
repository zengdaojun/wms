package com.zdjer.win.view;

import com.zdjer.utils.view.SPHelper;
import com.zdjer.win.utils.WinDBManager;
import com.zdjer.wms.view.WmsApplication;

/**
 * Created by zdj on 16/3/29.
 */
public class WinApplication extends WmsApplication {
    // 在application的onCreate中初始化
    @Override
    public void onCreate() {
        super.onCreate();

        SPHelper.init("win");
        serverIP = "http://mts.wiseiter.com/serverwl.do";

        WinDBManager.createDatbase(getContextExt());

        //x.Ext.init(this);
        //x.Ext.setDebug(true); // 是否输出debug日志
    }
}
