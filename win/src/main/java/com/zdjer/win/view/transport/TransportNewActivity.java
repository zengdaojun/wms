package com.zdjer.win.view.transport;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.utils.DateHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.win.R;
import com.zdjer.win.bean.TransportBO;
import com.zdjer.win.model.TransportBLO;
import com.zdjer.win.utils.WinNetApiHelper;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.view.widget.WmsSelectDataActivity;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Activity:出库运输信息选择
 */
public class TransportNewActivity extends BaseActivity {

    @Bind(R.id.transport_new_tv_wuliu)
    protected TextView tvWuLiu;// 物流
    @Bind(R.id.transport_new_tv_carnum)
    protected TextView tvCarNum;// 车牌号
    @Bind(R.id.transport_new_tv_driver)
    protected TextView tvDriver;// 司机
    @Bind(R.id.transport_new_tv_senddate)
    protected TextView tvSendDate;// 发车日期
    @Bind(R.id.transport_new_tv_sendtime)
    protected TextView tvSendTime;// 发车时间

    private Calendar calendar = null;
    private TransportBLO transportBlo = new TransportBLO();//运输信息业务逻辑

    @Override
    protected int getLayoutId() {

        return R.layout.activity_transport_new;
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
     * 返回
     *
     * @param v
     */
    @OnClick(R.id.transport_new_tv_back)
    public void onBack(View v) {
        try {
            TransportNewActivity.this.finish();
            overridePendingTransition(R.anim.in_from_left,
                    R.anim.out_to_right);
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 保存
     *
     * @author bipolor
     */
    @OnClick(R.id.transport_new_tv_save)
    public void onSave(View v) {
        try {
            if (isValidate()) {

                long wuLiuId = Long.parseLong(tvWuLiu.getTag().toString());
                String wuLiu = tvWuLiu.getText().toString();

                long carNumId = Long.parseLong(tvCarNum.getTag() != null ? tvCarNum.getTag().toString() : "0");
                String carNum = tvCarNum.getText().toString();

                long driverId = Long.parseLong(tvDriver.getTag() != null ? tvDriver.getTag().toString() : "0");
                String driver = tvDriver.getText().toString();

                String sendDateString = tvSendDate.getText().toString().trim() + " " +
                        tvSendTime.getText().toString() + ":0";
                Date sendDate = DateHelper.getDate(sendDateString);

                final TransportBO transportBO = new TransportBO();
                transportBO.setWuLiuId(wuLiuId);
                transportBO.setWuLiu(wuLiu);
                transportBO.setCarNumId(carNumId);
                transportBO.setCarNum(carNum);
                transportBO.setDriverId(driverId);
                transportBO.setDriver(driver);
                transportBO.setSendDate(sendDate);
                if (transportBlo == null) {
                    transportBlo = new TransportBLO();
                }
                //同步物流数据到服务端

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
                showWaitDialog("正在添加…");

                //下载经销商信息
                WinNetApiHelper.uploadTransport(ip, token, transportBO,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        handleUploadTransport(transportBO,response);
                        hideWaitDialog();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        hideWaitDialog();
                        String netError = getString(R.string.wms_net_error);
                        ToastHelper.showToast(netError + statusCode);
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.wms_common_exception, Toast.LENGTH_LONG).show();
        }
    }

    private void handleUploadTransport(TransportBO transportBO,JSONObject jsonObject){
        try {
            boolean flag = jsonObject.getBoolean("flag");
            if (flag) {
                long tranId = jsonObject.getLong("msg");
                transportBO.setTranId(tranId);
                //添加物流数据到本地
                if (transportBlo.addTransport(transportBO)) {
                    Toast.makeText(TransportNewActivity.this,
                            R.string.wms_common_save_success, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TransportNewActivity.this,
                            R.string.wms_common_save_failed, Toast.LENGTH_LONG).show();
                }
                TransportNewActivity.this.setResult(1, null);
                TransportNewActivity.this.finish();
                overridePendingTransition(R.anim.in_from_left,
                        R.anim.out_to_right);

            } else {
                ToastHelper.showToast(jsonObject.getString("msg"));
            }
        }catch (Exception e){
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 验证是否通过
     *
     * @return
     */
    private boolean isValidate() {
        // 1 物流
        String wuliu = tvWuLiu.getText().toString();
        if (wuliu.length() == 0) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.transport_wuliu_hint, Toast.LENGTH_LONG).show();
            ViewHelper.Focus(tvWuLiu);
            return false;
        }
        // 2 发货日期
        String sendDate = tvSendDate.getText().toString();
        if (sendDate.length() == 0) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.transport_senddate_hint, Toast.LENGTH_LONG).show();
            ViewHelper.Focus(tvSendDate);
            return false;
        }
        // 3 发货时间
        String sendtime = tvSendTime.getText().toString();
        if (sendtime.length() == 0) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.transport_sendtime_hint, Toast.LENGTH_LONG).show();
            ViewHelper.Focus(tvSendTime);
            return false;
        }
        return true;
    }

    /**
     * 选择物流公司
     *
     * @author bipolor
     */
    @OnClick(R.id.transport_new_ll_wuliu)
    public void onSelectWuLiu(View v) {
        try {
            Intent intent = new Intent(TransportNewActivity.this,
                    WmsSelectDataActivity.class);
            intent.putExtra("dataType", DataType.wuLiu.getValue());
            intent.putExtra("dataTitle",
                    getResources().getString(R.string.transport_wuliu));
            TransportNewActivity.this.startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
        } catch (Exception e) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.wms_common_exception, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 选择车牌号
     * @param v
     */
    @OnClick(R.id.transport_new_ll_carnum)
    public void onSelectCarNum(View v) {
        try {
            if (tvWuLiu.getTag() == null) {
                Toast.makeText(TransportNewActivity.this,
                        R.string.transport_wuliu_hint, Toast.LENGTH_LONG).show();
                return;
            }
            long wuLiuId = Long.parseLong(tvWuLiu.getTag().toString());

            Intent intent = new Intent(TransportNewActivity.this,
                    WmsSelectDataActivity.class);
            intent.putExtra("parentId", wuLiuId);
            intent.putExtra("dataType", DataType.carNum.getValue());
            intent.putExtra("dataTitle",
                    getResources().getString(R.string.transport_carnum));
            TransportNewActivity.this.startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
        } catch (Exception e) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.wms_common_exception, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 选择司机信息
     *
     * @author bipolor
     */
    @OnClick(R.id.transport_new_ll_driver)
    public void onSelectDriver(View v) {
        try {
            if (tvWuLiu.getTag() == null) {
                Toast.makeText(TransportNewActivity.this,
                        R.string.transport_wuliu_hint, Toast.LENGTH_LONG).show();
                return;
            }
            long wuLiuId = Long.parseLong(tvWuLiu.getTag().toString());

            Intent intent = new Intent(TransportNewActivity.this,
                    WmsSelectDataActivity.class);
            intent.putExtra("parentId", wuLiuId);
            intent.putExtra("dataType", DataType.driver.getValue());
            intent.putExtra("dataTitle",
                    getResources().getString(R.string.transport_driver));
            TransportNewActivity.this.startActivityForResult(intent, 3);
            overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
        } catch (Exception e) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.wms_common_exception, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 选择发车日期
     *
     * @author bipolor
     */
    @OnClick(R.id.transport_new_ll_senddate)
    public void onSelectSendDate(View v) {
        try {
            if (calendar == null) {
                calendar = DateHelper.getCurrCalendar();
            }
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            Dialog dialog = new DatePickerDialog(TransportNewActivity.this,
                    R.style.dialog, new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    String sendDateString = String.format(
                            "%d-%d-%d", year, monthOfYear + 1,
                            dayOfMonth);

                    tvSendDate.setText(sendDateString);
                }
            }, year, month, day);
            dialog.show();
        } catch (Exception e) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.wms_common_exception, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 选择发货时间
     *
     * @author bipolor
     */
    @OnClick(R.id.transport_new_ll_sendtime)
    public void onSelectTime(View v) {
        try {
            if (calendar == null) {
                calendar = DateHelper.getCurrCalendar();
            }
            final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            final int minute = calendar.get(Calendar.MINUTE);
            Dialog dialog = new TimePickerDialog(TransportNewActivity.this,
                    R.style.dialog,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view,
                                              int hourOfDay, int minute) {
                            String sendTimeString = String.format("%d:%d",
                                    hourOfDay, minute);

                            tvSendTime.setText(sendTimeString);
                        }
                    }, hourOfDay, minute, true);
            dialog.show();
        } catch (Exception e) {
            Toast.makeText(TransportNewActivity.this,
                    R.string.wms_common_exception, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 选择后返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        long dataId = data.getExtras().getLong("dataId");
        String dataValue = data.getExtras().getString("dataValue");
        // 物流选择返回结果
        if (requestCode == 1 && resultCode == 1) {
            tvWuLiu.setTag(dataId);
            tvWuLiu.setText(dataValue);
        }
        // 车牌号选择返回结果
        if (requestCode == 2 && resultCode == 1) {
            tvCarNum.setTag(dataId);
            tvCarNum.setText(dataValue);
        }
        // 司机选择返回结果
        if (requestCode == 3 && resultCode == 1) {
            tvDriver.setTag(dataId);
            tvDriver.setText(dataValue);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
