package com.zdjer.win.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.AudioHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.win.R;
import com.zdjer.win.utils.WinNetApiHelper;
import com.zdjer.win.view.core.ScanTypes;
import com.zdjer.win.view.helper.ScanActivity;
import com.zdjer.wms.utils.BroadcastReceiverHelper;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Activity:入库
 */
public class WSHConfigActivity extends BaseActivity {

    @Bind(R.id.et_wshconfirm_barcode)
    protected EditText etThdNum;

    protected BroadcastReceiverHelper broadcastReceiverHelper;

    private int keyCode = 0;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_shconfirm;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

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
                if (broadcastReceiverHelper.getIsScanBarCode() && true) {
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
     * 退出该功能
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 134:
            case 135:
            case 136: {
                this.keyCode = keyCode;
                this.broadcastReceiverHelper.startScan();
                break;
            }
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_HOME: {
                back(null);
            }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data != null) {
                /*// 经销商选择返回结果
                if (requestCode == se && resultCode == 1) {
                    String optionValue = data.getExtras().getString("dataValue");
                    this.etThdNum.setText(optionValue);
                }
                // 入库选择仓库货位
                if (requestCode == wareHouseRequestCode && resultCode == 1) {
                    String dataValue = data.getExtras().getString("dataValue");
                    this.etWareHouse.setText(dataValue);
                }*/
            }
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Toast.makeText(WSHConfigActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 扫描
     *
     * @param v
     */
    @OnClick(R.id.iv_win_scan)
    protected void toScan(View v) {
        Intent intent = new Intent();
        intent.setClass(WSHConfigActivity.this, ScanActivity.class);
        intent.putExtra("scanType", ScanTypes.Single.getValue());

        /*Bundle bundle = new Bundle();
        bundle.putSerializable("record", record);
        intent.putExtras(bundle);
        etBarCode.setText("");
        ViewHelper.Focus(etBarCode);
        startActivityForResult(intent, scanRequestCode);*/
    }

    @OnClick(R.id.btn_wshconfirm_confirm)
    protected void confirm(View v){
        String token = SPHelper.get("token", "");
        String ip = SPHelper.get("ip", "");
        String thdNum = etThdNum.getText().toString().trim();

        if (StringHelper.isEmpty(ip)) {
            ToastHelper.showToast(R.string.wshconfirm_noallowconfirm);
            return;
        }
        if (!DeviceHelper.hasInternet()) {
            ToastHelper.showToast(R.string.wms_common_no_net);
            return;
        }

        //网络
        if(!DeviceHelper.hasInternet()){
            ToastHelper.showToast(R.string.wms_common_no_net);
        }else{
            WinNetApiHelper.shconfirm(ip, token, thdNum, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    handleSHConfirm(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable
                        throwable, JSONObject errorResponse) {
                    DialogHelper.getMessageDialog(WSHConfigActivity.this, getString(R.string.wms_net_error) + statusCode);
                }
            });
        }
    }

    /**
     * 处理收货确认
     * @param response
     */
    private void handleSHConfirm(JSONObject response){
        try {
            boolean flag = response.getBoolean("flag");
            if (flag) {
                ToastHelper.showToast(R.string.wshconfirm_confirm_success);
            }else{
                AudioHelper.openSpeaker(this, R.raw.zdjer_error1);// 提示音
                String msg = response.getString("msg");
                if(StringHelper.isEmpty(msg)){
                    DialogHelper.getMessageDialog(this,getString(R.string.wshconfirm_confirm_failed)).show();
                }else{
                    DialogHelper.getMessageDialog(this,msg).show();
                }
            }
        }catch(Exception e){
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    @OnClick(R.id.tv_wshconfirm_back)
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
}