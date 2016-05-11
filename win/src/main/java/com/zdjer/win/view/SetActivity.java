package com.zdjer.win.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.utils.PathHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.update.UpdateHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.win.R;
import com.zdjer.wms.bean.DataItemBO;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.bean.core.OptionTypes;
import com.zdjer.wms.model.DataItemBLO;
import com.zdjer.wms.model.OptionBLO;
import com.zdjer.wms.utils.WmsNetApiHelper;
import com.zdjer.wms.view.widget.WmsSetDeviceNumActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SetActivity extends BaseActivity {

    @Bind(R.id.tv_set_devicenum)
    protected TextView tvDeviceNum = null;

    private OptionBLO optionBlo = new OptionBLO();//选项操作类;
    private DataItemBLO dataItemBLO = new DataItemBLO();

    @Override
    protected int getLayoutId() {

        return R.layout.activity_set;
    }

    /**
     * 初始化数据
     */
    public void initView() {
        //获得设备编号
        String deviceNum = optionBlo.getOptionValue(com.zdjer.wms.bean.core.OptionTypes.DeviceNum);
        tvDeviceNum.setText(deviceNum);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @OnClick(R.id.tv_set_back)
    public void back(View v) {
        finish();
        overridePendingTransition(R.anim.in_from_left,
                R.anim.out_to_right);
    }

    @OnClick(
            R.id.ll_set_devicenum)
    public void setDeviceNum(View v) {

        Intent intent = new Intent(this,
                WmsSetDeviceNumActivity.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.in_from_right1,
                R.anim.out_to_left1);
    }

    @OnClick(R.id.ll_set_sync_basedata)
    public void syncBasedata(View v) {

        final String token = SPHelper.get("token", "");
        final String ip = SPHelper.get("ip", "");

        if (StringHelper.isEmpty(ip)) {
            ToastHelper.showToast(R.string.wms_common_sync_noallowsync);
            return;
        }
        if (!DeviceHelper.hasInternet()) {
            ToastHelper.showToast(R.string.wms_common_no_net);
            return;
        }

        showWaitDialog(R.string.wms_common_downloading);


        //下载经销商信息
        WmsNetApiHelper.downloadJXS(ip, token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                handleDownloadJXS(ip, token, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                hideWaitDialog();
                String netError = getString(R.string.wms_net_error);
                ToastHelper.showToast(netError + statusCode);
                return;
            }
        });
    }

    /**
     * 处理下载的经销商
     * @param ip
     * @param token
     * @param jsonObject
     */
    private void handleDownloadJXS(final String ip, final String token,JSONObject jsonObject){
        try {
            boolean flag = jsonObject.getBoolean("flag");
            if (flag) {
                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.jxsNum, jsonObject);
                if (dataItemBLO.addDataItems(lstDataItem)) {

                    //下载物流信息
                    WmsNetApiHelper.downloadWuLiu(ip, token, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            handleDownloadWuLiu(ip,token,response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            hideWaitDialog();
                            String netError = getString(R.string.wms_net_error);
                            ToastHelper.showToast(netError + statusCode);
                        }
                    });


                }else{
                    hideWaitDialog();
                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                }
            } else {
                hideWaitDialog();
                ToastHelper.showToast(R.string.wms_common_getdata_failed);
            }

        } catch (Exception e) {
            hideWaitDialog();
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 处理下载的物流
     * @param ip
     * @param token
     * @param jsonObject
     */
    private void handleDownloadWuLiu(final String ip, final String token,JSONObject jsonObject){
        try {
            boolean flag = jsonObject.getBoolean("flag");
            if (flag) {
                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.wuLiu, jsonObject);
                if (dataItemBLO.addDataItems(lstDataItem)) {

                    //下载库房信息
                    WmsNetApiHelper.downloadWareHouse(ip, token, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            handleDownloadWareHouse(ip, token, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            hideWaitDialog();
                            String netError = getString(R.string.wms_net_error);
                            ToastHelper.showToast(netError + statusCode);
                        }
                    });


                }else{
                    hideWaitDialog();
                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                }
            } else {
                hideWaitDialog();
                ToastHelper.showToast(R.string.wms_common_getdata_failed);
            }

        } catch (Exception e) {
            hideWaitDialog();
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 处理下载的库房
     * @param ip
     * @param token
     * @param jsonObject
     */
    private void handleDownloadWareHouse(final String ip, final String token,JSONObject jsonObject){
        try {
            boolean flag = jsonObject.getBoolean("flag");
            if (flag) {
                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.wareHouse, jsonObject);
                if (dataItemBLO.addDataItems(lstDataItem)) {

                    //下载车辆信息
                    WmsNetApiHelper.downloadCarNum(ip, token, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            handleDownloadCarNum(ip,token,response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            hideWaitDialog();
                            String netError = getString(R.string.wms_net_error);
                            ToastHelper.showToast(netError + statusCode);
                            return;
                        }
                    });


                }else{
                    hideWaitDialog();
                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                }
            } else {
                hideWaitDialog();
                ToastHelper.showToast(R.string.wms_common_getdata_failed);
            }

        } catch (Exception e) {
            hideWaitDialog();
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 处理下载的车辆
     * @param ip
     * @param token
     * @param jsonObject
     */
    private void handleDownloadCarNum(final String ip,final String token,JSONObject jsonObject){
        try {
            boolean flag = jsonObject.getBoolean("flag");
            if (flag) {
                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.carNum, jsonObject);
                if (dataItemBLO.addDataItems(lstDataItem)) {

                    //下载司机信息
                    WmsNetApiHelper.downloadDriver(ip, token, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            try {
                                boolean flag = response.getBoolean("flag");
                                if (flag) {
                                    List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.driver, response);
                                    if (!dataItemBLO.addDataItems(lstDataItem)) {
                                        hideWaitDialog();
                                        ToastHelper.showToast(R.string.wms_common_local_save_error);
                                    } else {
                                        hideWaitDialog();
                                        ToastHelper.showToast(R.string.common_download_ok);
                                    }
                                } else {
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.wms_common_getdata_failed);
                                }

                            } catch (Exception e) {
                                hideWaitDialog();
                                ToastHelper.showToast(R.string.wms_common_exception);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            hideWaitDialog();
                            String netError = getString(R.string.wms_net_error);
                            ToastHelper.showToast(netError + statusCode);
                        }
                    });


                }else{
                    hideWaitDialog();
                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                }
            } else {
                hideWaitDialog();
                ToastHelper.showToast(R.string.wms_common_getdata_failed);
            }

        } catch (Exception e) {
            hideWaitDialog();
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }



    @OnClick(R.id.ll_set_update)
    public void updateSoftware(View v) {
        String url = "http://mts.wiseiter.com/upload/version/WinAppVersion.xml";

        String saveFilePath = PathHelper.GetSDRootPath()
                + File.separator
                + "windata"
                + File.separator + "download" + File.separator;
        new UpdateHelper(this, true,saveFilePath).checkUpdate(url);
    }

    /**
     * 返回结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String deviceNum = optionBlo.getOptionValue(OptionTypes.DeviceNum);// 获取设备信息
            this.tvDeviceNum.setText(deviceNum);
        }
    }
}
