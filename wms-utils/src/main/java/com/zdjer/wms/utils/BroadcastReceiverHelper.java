package com.zdjer.wms.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by zdj on 16/4/10.
 */
public class BroadcastReceiverHelper extends BroadcastReceiver {

    private String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";//接受广播
    private String START_SCAN_ACTION = "com.geomobile.se4500barcode";//调用扫描广播
    private String STOP_SCAN_ACTION = "com.geomobile.se4500barcode.poweroff";//结束扫描
    private Context context;
    private Boolean isScanBarCode = false;
    private BroadcastReceiverHelper broadcastReceiverHelper;

    public Boolean getIsScanBarCode() {
        return isScanBarCode;
    }



    public BroadcastReceiverHelper(Context context) {
        this.context = context;
        this.broadcastReceiverHelper = this;
    }

    /**
     * 注册
     */
    public void registerAction() {
        //注册系统广播  接受扫描到的数据
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECE_DATA_ACTION);
        this.context.registerReceiver(broadcastReceiverHelper, intentFilter);
    }

    /**
     * 发送广播  调用系统扫描
     */
    public void startScan() {
        Intent intent = new Intent();
        intent.setAction(START_SCAN_ACTION);
        this.context.sendBroadcast(intent, null);
    }

    /**
     * 发送广播 停止扫描
     */
    public void stopScan() {
        Intent intent = new Intent();
        intent.setAction(STOP_SCAN_ACTION);
        this.context.sendBroadcast(intent);
        this.context.unregisterReceiver(broadcastReceiverHelper);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(RECE_DATA_ACTION)) {
            isScanBarCode = true;
        }
    }
}
