package com.zdjer.win.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.win.R;
import com.zdjer.wms.adapter.RecordGatherAdapter;
import com.zdjer.wms.bean.RecordGatherBO;
import com.zdjer.wms.utils.BroadcastReceiverHelper;

import java.util.List;

/**
 * Created by zdj on 4/21/16.
 */
public class WIOCActivity extends BaseListActivity<RecordGatherBO> {

    protected static final int KEY_SCAN1 = 134;
    protected static final int KEY_SCAN2 = 135;
    protected static final int KEY_SCAN3 = 136;

    protected boolean isAllowAdd = true;

    //protected RecordType recordType = RecordType.in;
    protected BroadcastReceiverHelper broadcastReceiverHelper;

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {

        return null;
    }

    @Override
    protected ListView getListView() {

        return null;
    }

    @Override
    protected BaseListAdapter<RecordGatherBO> getListAdapter() {

        return new RecordGatherAdapter();
    }

    @Override
    protected List<RecordGatherBO> getListData() {

        return null;
    }

    /**
     * 开始
     * 注册扫描
     */
    @Override
    protected void onStart() {
        super.onStart();

        //注册广播接收器
        this.broadcastReceiverHelper = new BroadcastReceiverHelper(this) {
            @Override
            public void onReceive(Context context,
                                  Intent intent) {
                super.onReceive(context, intent);
                if (broadcastReceiverHelper.getIsScanBarCode()) {
                    String barcode = intent.getStringExtra("se4500");
                    handleScanBarCode(barcode);
                }
            }
        };
        broadcastReceiverHelper.registerAction();
    }

    protected void handleScanBarCode(String barcode){

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.broadcastReceiverHelper.stopScan();
    }


    /**
     * 返回
     * @param v
     */
    protected void back(View v){
        DialogHelper.getConfirmDialog(this, getString(R.string.wms_common_exit_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.in_from_left,
                        R.anim.out_to_right);
            }
        }).show();
    }

    /**
     * 显示错误条码
     */
    protected void showErrorBarCode(String barCode) {
        String message = "条形码“" + barCode + "”格式不正确。请输入或扫描13位条形码！";
        DialogHelper.getMessageDialog(this, message).show();
    }
}
