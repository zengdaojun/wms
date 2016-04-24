package com.zdjer.win.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.utils.DateHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.AudioHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.win.R;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.bean.RecordType;
import com.zdjer.win.model.RecordBLO;
import com.zdjer.win.utils.WinNetApiHelper;
import com.zdjer.win.view.core.ScanTypes;
import com.zdjer.win.view.helper.ScanActivity;
import com.zdjer.wms.adapter.RecordGatherAdapter;
import com.zdjer.wms.bean.RecordGatherBO;
import com.zdjer.wms.bean.core.YesNos;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Activity:入库
 */
public class WCheckActivity extends WIOCActivity {

    @Bind(R.id.et_wcheck_barcode)
    protected EditText etBarCode = null; // 条形码

    @Bind(R.id.tv_wcheck_total_count)
    protected TextView tvTotalCount = null;// 已添加条码
    private RecordBLO recordBLO = new RecordBLO();// 记录业务逻辑类

    private int browserRequestCode = 0;
    private int browserResultCode = 1;

    private boolean isAllowScan = false;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_wcheck;
    }

    /**
     * 获得刷新布局
     *
     * @return
     */
    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {

        return (SwipeRefreshLayout) findViewById(R.id.srl_wcheck_barcode);
    }

    /**
     * 获得列表控件
     *
     * @return
     */
    @Override
    protected ListView getListView() {

        return (ListView) findViewById(R.id.lv_wcheck_barcode);
    }

    /**
     * 获得列表适配器
     *
     * @return
     */
    @Override
    protected BaseListAdapter getListAdapter() {

        return new RecordGatherAdapter();
    }

    /**
     * 初始化控件
     *
     * @throws Exception
     */
    public void initView() {
        super.initView();

        etBarCode.setText("");
        ViewHelper.Focus(etBarCode);
    }

    @Override
    protected List<RecordGatherBO> getListData() {

        if (currentPage == 0) {
            setTotalCount();
        }
        return recordBLO.getRecordGather(RecordType.check, "", "", "", currentPage, getPageSize());
    }

    /**
     * 返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            //扫描条码
            if (requestCode == 3 && resultCode == 10) {
                loadData();
                etBarCode.setText("");
            }

            if (requestCode == browserRequestCode && resultCode == browserResultCode) {
                super.currentPage = 0;
                super.loadData();
            }

            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Toast.makeText(WCheckActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 134://中间
            case 135://左边
            case 136: {
                if (isAllowScan) {
                    this.broadcastReceiverHelper.startScan();
                }
                break;
            }
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_HOME: {
                //返回
                this.back(null);
            }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 列表项点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecordGatherBO recordGatherBO = baseListAdapter.getItem(position);
        if (recordGatherBO != null) {
            Intent intent = new Intent(this, RecordActivity.class);
            intent.putExtra("recordType", RecordType.check.getValue());
            intent.putExtra("serNo", recordGatherBO.getSerNo());
            intent.putExtra("count", recordGatherBO.getCount());
            startActivityForResult(intent, browserRequestCode);
        }
    }

    /**
     * 设置添加的数量
     */
    private void setTotalCount() {

        // 1 获得输入的入库单号
        int totalCount = recordBLO.getRecordsTotalCount(RecordType.check, "",
                "", "");

        if (totalCount == 0) {
            tvTotalCount.setVisibility(View.INVISIBLE);
        } else {
            tvTotalCount.setVisibility(View.VISIBLE);

            StringBuffer sbText = new StringBuffer();
            sbText.append(String.format("已扫条码%d", totalCount));
            tvTotalCount.setText(sbText.toString());
        }
    }

    /**
     * 硬件扫描
     */
    private void hardScanBarCode(String barCode) {
        if (barCode != null) {
            etBarCode.setText(barCode);
            addBarCode();
        }
    }

    /**
     * 添加条码
     */
    private void addBarCode() {
        try {
            // 1 验证输入的合法性
            if (!isValidate(true)) {
                return;
            }

            // 2 添加
            SharedPreferences sharedPreferences = getSharedPreferences(
                    "win", MODE_PRIVATE);
            long userId = sharedPreferences.getLong("currUserId", 0);
            String uid = sharedPreferences.getString("currUid", "");
            String token = sharedPreferences.getString("currToken", "");
            Boolean isOnline = sharedPreferences.getBoolean("isonline", false);
            String ip = sharedPreferences.getString("ip", "");

            String barCode = etBarCode.getText().toString().trim();
            Date createDate = DateHelper.getCurrDate();

            final RecordBO recordBO = new RecordBO();
            recordBO.setBarCode(barCode);
            recordBO.setCreateUserId(userId);
            recordBO.setCreateUserUID(uid);
            recordBO.setCreateDate(createDate);
            recordBO.setRecordType(RecordType.check);

            recordBLO.convertToRecord(recordBO);//转换
            if (StringHelper.isEmpty(ip)) {
                //添加到本地
                if (recordBLO.addRecord(recordBO)) {
                    AudioHelper.openSpeaker(this, R.raw.zdjer_ok);// 提示音
                    ToastHelper.showToast(R.string.wms_common_save_success);

                } else {
                    AudioHelper.openSpeaker(this, R.raw.zdjer_error1);// 提示音
                    DialogHelper.getMessageDialog(this, getString(R.string.wms_common_save_failed)).show();
                }
            } else {
                //网络
                if (!DeviceHelper.hasInternet()) {
                    ToastHelper.showToast(R.string.wms_common_no_net);
                } else {
                    WinNetApiHelper.uploadCheckRecord(ip, token, barCode, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            handleUploadCheckRecord(recordBO, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONObject errorResponse) {
                            DialogHelper.getMessageDialog(WCheckActivity.this, getString(R.string.wms_net_error) + statusCode);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Toast.makeText(WCheckActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 处理上传记录
     *
     * @param recordBO
     * @param response
     */
    private void handleUploadCheckRecord(RecordBO recordBO, JSONObject response) {
        try {
            boolean flag = response.getBoolean("flag");
            if (flag) {
                recordBO.setIsUpload(YesNos.Yes);
            }
            if (flag && recordBLO.addRecord(recordBO)) {
                AudioHelper.openSpeaker(this, R.raw.zdjer_ok);// 提示音
                ToastHelper.showToast(R.string.wms_common_save_success);

            } else {
                AudioHelper.openSpeaker(this, R.raw.zdjer_error1);// 提示音
                String msg = response.getString("msg");
                if (StringHelper.isEmpty(msg)) {
                    DialogHelper.getMessageDialog(this, getString(R.string.wms_common_save_failed)).show();
                } else {
                    DialogHelper.getMessageDialog(this, msg).show();
                }

            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 验证输入是否有效
     *
     * @return true:有效；false:无效
     */
    private Boolean isValidate(Boolean isValidateBarCode) {
        try {
            if (isValidateBarCode) {

                // 3 条形码
                String barCode = etBarCode.getText().toString().trim();
                // 3.1 未输入
                if (barCode.length() == 0) {
                    Toast.makeText(WCheckActivity.this, R.string.record_barcode_null,
                            Toast.LENGTH_SHORT).show();
                    ViewHelper.Focus(etBarCode);
                    return false;
                }

                // 3.3 已存在
                if (recordBLO == null) {
                    recordBLO = new RecordBLO();
                }
                if (recordBLO.isExist(RecordType.check, barCode)) {
                    Toast.makeText(WCheckActivity.this, R.string.record_barcode_exist,
                            Toast.LENGTH_SHORT).show();
                    AudioHelper.openSpeaker(this, R.raw.zdjer_error2);// 提示音
                    AudioHelper.startVibrator(this);
                    etBarCode.setText("");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            Toast.makeText(WCheckActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

    /**
     * 返回
     */
    @OnClick(R.id.tv_wcheck_back)
    protected void back(View v) {
        super.back(v);
    }

    @OnClick(R.id.tv_wcheck_clear)
    protected void clear(View v) {
        if (recordBLO.deleteRecord(RecordType.check)) {
            ToastHelper.showToast(R.string.wms_common_clear_success);
            loadData();
        } else {
            DialogHelper.getMessageDialog(this, getString(R.string.wms_common_clear_failed));
        }
    }

    @OnClick(R.id.btn_wcheck_add_barcode)
    protected void toAddBarCode(View v) {
        addBarCode();
    }

    /**
     * 扫条码
     *
     * @author bipolor
     */
    @OnClick(R.id.iv_wcheck_scan_barcode)
    public void onClick(View v) {
        try {
            if (!isValidate(false)) {
                return;
            }

            // 2 添加
            SharedPreferences sharedPreferences = getSharedPreferences(
                    "win", MODE_PRIVATE);
            long userId = sharedPreferences.getLong("currUserId", 0);
            String uid = sharedPreferences.getString("currUid", "");
            String barCode = etBarCode.getText().toString().trim();
            Date createDate = DateHelper.getCurrDate();

            RecordBO record = new RecordBO();
            record.setBarCode(barCode);
            record.setCreateUserId(userId);
            record.setCreateUserUID(uid);
            record.setCreateDate(createDate);
            record.setRecordType(RecordType.check);

            Intent intent = new Intent();
            intent.setClass(WCheckActivity.this, ScanActivity.class);
            intent.putExtra("scanType", ScanTypes.Mutil.getValue());

            Bundle bundle = new Bundle();
            bundle.putSerializable("record", record);
            intent.putExtras(bundle);
            etBarCode.setText("");
            ViewHelper.Focus(etBarCode);
            startActivityForResult(intent, 3);

        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }
}