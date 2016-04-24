package com.zdjer.min.view;

import com.zdjer.min.utils.MinDBManager;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.wms.view.WmsApplication;

/**
 * Created by zdj on 16/3/29.
 */
public class MinApplication extends WmsApplication {
    // 在application的onCreate中初始化
    @Override
    public void onCreate() {

        super.onCreate();

        SPHelper.init("min");
        serverIP = "http://mts.wiseiter.com/serverwd.do";

        MinDBManager.createDatbase(getContextExt());
    }
}
