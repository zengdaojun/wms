package com.zdjer.win.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.utils.DateHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.win.R;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.model.RecordBLO;
import com.zdjer.wms.bean.DataItemBO;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.bean.core.OptionTypes;
import com.zdjer.wms.model.DataItemBLO;
import com.zdjer.wms.model.OptionBLO;
import com.zdjer.wms.utils.WmsNetApiHelper;
import com.zdjer.wms.view.widget.WmsSetDeviceNumActivity;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SetActivity extends BaseActivity {

    @Bind(R.id.tv_set_devicenum)
    protected TextView tvDeviceNum = null;

    private OptionBLO optionBlo = null;//选项操作类;
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

        String token = SPHelper.get("token", "");
        String ip = SPHelper.get("ip", "");

        if (StringHelper.isEmpty(ip)) {
            ToastHelper.showToast(R.string.wms_common_sync_noallowsync);
            return;
        }
        if (!DeviceHelper.hasInternet()) {
            ToastHelper.showToast(R.string.wms_common_no_net);
            return;
        }

        showWaitDialog(R.string.wms_common_downloading);

        if (!StringHelper.isEmpty(ip)) {
            if (DeviceHelper.hasInternet()) {
                //下载经销商信息
                WmsNetApiHelper.downloadJXS(ip, token, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            boolean flag = response.getBoolean("flag");
                            if(flag){
                                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.jxsNum,response);
                                if(!dataItemBLO.addDataItems(lstDataItem)){
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                                    return;
                                }
                            }else{
                                hideWaitDialog();
                                ToastHelper.showToast(R.string.wms_common_getdata_failed);
                                return;
                            }

                        }catch (Exception e){
                            hideWaitDialog();
                            ToastHelper.showToast(R.string.wms_common_exception);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        hideWaitDialog();
                        String netError = getString(R.string.wms_net_error);
                        ToastHelper.showToast(netError + statusCode);
                        return;
                    }
                });

                //下载物流信息
                WmsNetApiHelper.downloadWuLiu(ip, token, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            boolean flag = response.getBoolean("flag");
                            if(flag){
                                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.wuLiu,response);
                                if(!dataItemBLO.addDataItems(lstDataItem)){
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                                    return;
                                }
                            }else{
                                hideWaitDialog();
                                ToastHelper.showToast(R.string.wms_common_getdata_failed);
                                return;
                            }

                        }catch (Exception e){
                            hideWaitDialog();
                            ToastHelper.showToast(R.string.wms_common_exception);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        hideWaitDialog();
                        String netError = getString(R.string.wms_net_error);
                        ToastHelper.showToast(netError + statusCode);
                        return;
                    }
                });

                //下载库房信息
                WmsNetApiHelper.downloadWareHouse(ip, token, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            boolean flag = response.getBoolean("flag");
                            if (flag) {
                                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.wareHouse, response);
                                if (!dataItemBLO.addDataItems(lstDataItem)) {
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                                    return;
                                }
                            } else {
                                hideWaitDialog();
                                ToastHelper.showToast(R.string.wms_common_getdata_failed);
                                return;
                            }

                        } catch (Exception e) {
                            hideWaitDialog();
                            ToastHelper.showToast(R.string.wms_common_exception);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        hideWaitDialog();
                        String netError = getString(R.string.wms_net_error);
                        ToastHelper.showToast(netError + statusCode);
                        return;
                    }
                });

                //下载车辆信息
                WmsNetApiHelper.downloadCarNum(ip, token, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            boolean flag = response.getBoolean("flag");
                            if (flag) {
                                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.carNum, response);
                                if (!dataItemBLO.addDataItems(lstDataItem)) {
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                                    return;
                                }
                            } else {
                                hideWaitDialog();
                                ToastHelper.showToast(R.string.wms_common_getdata_failed);
                                return;
                            }

                        } catch (Exception e) {
                            hideWaitDialog();
                            ToastHelper.showToast(R.string.wms_common_exception);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        hideWaitDialog();
                        String netError = getString(R.string.wms_net_error);
                        ToastHelper.showToast(netError + statusCode);
                        return;
                    }
                });

                //下载司机信息
                WmsNetApiHelper.downloadDriver(ip, token, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            boolean flag = response.getBoolean("flag");
                            if(flag){
                                List<DataItemBO> lstDataItem = dataItemBLO.convertToDataItem(DataType.driver,response);
                                if(!dataItemBLO.addDataItems(lstDataItem)){
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.wms_common_local_save_error);
                                    return;
                                }else{
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.common_download_ok);
                                    return;
                                }
                            }else{
                                hideWaitDialog();
                                ToastHelper.showToast(R.string.wms_common_getdata_failed);
                                return;
                            }

                        }catch (Exception e){
                            hideWaitDialog();
                            ToastHelper.showToast(R.string.wms_common_exception);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        hideWaitDialog();
                        String netError = getString(R.string.wms_net_error);
                        ToastHelper.showToast(netError + statusCode);
                        return;
                    }
                });
            } else {
                hideWaitDialog();
                ToastHelper.showToast(R.string.wms_common_no_net);
                return;
            }
        } else {
            hideWaitDialog();
            ToastHelper.showToast(R.string.wms_common_sync_noallowsync);
            return;
        }
    }

    @OnClick(R.id.ll_set_exportall)
    public void exportAll(View v) {

        try {
            final int NODATA = 0;
            final int OK = 1;
            final int FAILD = 2;
            final int EXCEPTION = 3;
            showWaitDialog(R.string.wms_common_exporting);

            final Handler handler = new Handler() {
                @Override
                //当有消息发送出来的时候就执行Handler的这个方法
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    hideWaitDialog();
                    switch (msg.what) {
                        case NODATA: {
                            ToastHelper.showToast(R.string.win_set_nodata);
                            break;
                        }
                        case OK: {
                            ToastHelper.showToast(R.string.wms_common_export_success);
                            break;
                        }
                        case FAILD: {
                            ToastHelper.showToast(R.string.wms_common_export_faild);
                            break;
                        }
                        case EXCEPTION: {
                            ToastHelper.showToast(R.string.wms_common_exception);
                            break;
                        }
                    }

                }
            };

            new Thread() {
                public void run() {
                    Message msg = new Message();
                    try {

                        RecordBLO recordBlo = new RecordBLO();
                        List<RecordBO> lstRecord = recordBlo.getRecords();
                        if (lstRecord.size() == 0) {
                            msg.what = NODATA;
                            handler.sendMessage(msg);
                            return;
                        }
                        if (recordBlo.exportRecordsToTxt(lstRecord)) {
                            msg.what = OK;
                            handler.sendMessage(msg);
                        } else {
                            msg.what = FAILD;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        msg.what = EXCEPTION;
                        handler.sendMessage(msg);
                    }
                }
            }.start();
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    @OnClick(R.id.ll_set_export_with_date)
    public void exportWithDate(View v) {

        try {
            LayoutInflater inflater = LayoutInflater
                    .from(this);
            View viewSetDate = inflater.inflate(
                    R.layout.activity_export_setdate, null);
            AlertDialog.Builder builderExit = new AlertDialog.Builder(
                    this, R.style.dialog);
            builderExit.setIcon(R.drawable.dialog_logo);
            builderExit.setTitle("起止日期");
            builderExit.setView(viewSetDate);

            // 开始日期
            final TextView tvStartDate = (TextView) viewSetDate
                    .findViewById(R.id.exportsetdate_tv_startdate);
            // 结束日期
            final TextView tvEndDate = (TextView) viewSetDate
                    .findViewById(R.id.exportsetdate_tv_enddate);

            // 确定
            builderExit.setPositiveButton(R.string.common_confirm,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            try {
                                // 获得开始日期
                                String startDateTemp = tvStartDate
                                        .getText().toString();
                                if (startDateTemp.length() == 0) {
                                    ToastHelper.showToast(R.string.win_set_startdate_lengtherror);
                                    return;
                                }
                                String startDateString = String.format(
                                        "%s %s:%s:%s", startDateTemp, 0,
                                        0, 0);
                                Date startDate = DateHelper
                                        .getDate(startDateString);
                                String endDateTemp = tvEndDate
                                        .getText().toString();
                                if (endDateTemp.length() == 0) {
                                    ToastHelper.showToast(R.string.win_set_enddate_lengtherror);
                                    return;
                                }
                                // 获得结束日期
                                String endDateString = String.format(
                                        "%s %s:%s:%s", endDateTemp, 23, 59, 59);

                                Date endDate = DateHelper
                                        .getDate(endDateString);

                                RecordBLO recordBlo = new RecordBLO();

                                showWaitDialog(R.string.wms_common_exporting);
                                List<RecordBO> lstRecord = recordBlo.getRecords(startDate, endDate);
                                if (lstRecord.size() == 0) {
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.win_set_nodata);
                                    return;
                                }
                                if (recordBlo.exportRecordsToTxt(lstRecord)) {
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.wms_common_export_success);
                                } else {
                                    hideWaitDialog();
                                    ToastHelper.showToast(R.string.wms_common_export_faild);
                                }
                            } catch (Exception e) {
                                ToastHelper.showToast(R.string.wms_common_exception);
                            }
                        }
                    });
            // 取消
            builderExit.setNegativeButton(R.string.common_cancle,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });

            Calendar currCalendar = DateHelper.getCurrCalendar();
            final int year = currCalendar.get(Calendar.YEAR);
            final int month = currCalendar.get(Calendar.MONTH);
            final int day = currCalendar.get(Calendar.DAY_OF_MONTH);

            // 开始日期聚焦
            tvStartDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new DatePickerDialog(SetActivity.this
                            , R.style.dialog,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    String startDateString = String.format(
                                            "%d-%d-%d", year,
                                            monthOfYear + 1, dayOfMonth);
                                    tvStartDate.setText(startDateString);
                                }
                            }, year, month, day);
                    dialog.show();
                }
            });

            // 结束日期聚焦
            tvEndDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new DatePickerDialog(
                            SetActivity.this, R.style.dialog,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    String endDateString = String.format(
                                            "%d-%d-%d", year,
                                            monthOfYear + 1, dayOfMonth);
                                    tvEndDate.setText(endDateString);
                                }
                            }, year, month, day);
                    dialog.show();
                }
            });

            builderExit.create();
            builderExit.show();
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    @OnClick(R.id.ll_set_update)
    public void updateSoftware(View v) {
        DialogHelper.getMessageDialog(this, "敬请期待");
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
