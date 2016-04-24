package com.zdjer.win.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.utils.DateHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.AudioHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.win.R;
import com.zdjer.win.bean.OutTypes;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.bean.RecordType;
import com.zdjer.win.model.RecordBLO;
import com.zdjer.win.utils.WinNetApiHelper;
import com.zdjer.win.view.core.ScanTypes;
import com.zdjer.win.view.helper.ScanActivity;
import com.zdjer.win.view.transport.TransportListActivity;
import com.zdjer.wms.adapter.RecordGatherAdapter;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.bean.RecordGatherBO;
import com.zdjer.wms.bean.core.YesNos;
import com.zdjer.wms.view.widget.WmsSelectDataActivity;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import cz.msebera.android.httpclient.Header;

/**
 * Activity:出库记录
 */
public class WOutActivity extends WIOCActivity {

    //private TextView tvBack = null;// 返回

    //private LinearLayout llOutType = null;//出库方式(行)
    @Bind(R.id.tv_wout_outtype)
    protected TextView tvOutType;//出库方式

    @Bind(R.id.et_wout_thdnum)
    protected EditText etTHDNum;// 送货单号
    //private ImageView imgScanTHDNum = null;//扫描送货单号
    private int thdRequestCode = 1;

    @Bind(R.id.ll_wout_dbdnum)
    protected LinearLayout llDBDNum;
    @Bind(R.id.et_wout_dbdnum)
    protected EditText etDBDNum;// 调拨单号
    @Bind(R.id.v_wout_dbdNum)
    protected View vDBDNum;

    @Bind(R.id.ll_wout_jxsNum)
    protected LinearLayout llJXSNum;// 经销商信息 (行)
    @Bind(R.id.et_wout_jxsNum)
    protected EditText etJXSNum;// 经销商编号
    @Bind(R.id.v_wout_jxsNum)
    protected View vJXSNum;

    private int jxsRequestCode = 2;

    //private LinearLayout llWareHouse = null;// 库位信息 (行)
    @Bind(R.id.et_wout_warehouse)
    protected EditText etWareHouse;// 库位
    private int wareHouseRequestCode = 3;

    //private LinearLayout llTransport = null;// 运输信息 (行)
    @Bind(R.id.tv_wout_transport)
    protected TextView tvTransport;// 运输信息(显示的运输信息数据)
    private long tranId = 0;//运输信息Id

    @Bind(R.id.et_wout_barCode)
    protected EditText etBarCode; // 条形码

    //private ImageView imgScanBarcode = null;//扫描

    /**
     * 出库单对应的条码
     */
    @Bind(R.id.tv_wout_total_count)
    protected TextView tvTotalCount;// 已添加条码
    private RecordBLO recordBLO = new RecordBLO();
    private OutTypes outType = OutTypes.lingshou;

    private int keyCode = 0;
    private int browserRequestCode = 0;
    private int browserResultCode = 1;

    private String outTypeString[] = new String[]{"零售", "中心送货", "借货", "网点送货"};

    @Override
    protected int getLayoutId() {

        return R.layout.activity_wout;
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {

        return (SwipeRefreshLayout) findViewById(R.id.srl_wout_barcode);
    }

    @Override
    protected ListView getListView() {

        return (ListView) findViewById(R.id.lv_wout_barcode);
    }

    @Override
    protected BaseListAdapter<RecordGatherBO> getListAdapter() {

        return new RecordGatherAdapter();
    }

    @Override
    public void initView() {
        super.initView();

        setOutType();

        etTHDNum.setText("");
        etJXSNum.setText("");
        etWareHouse.setText("");
        etBarCode.setText("");
        ViewHelper.Focus(etBarCode);
    }

    @Override
    protected List<RecordGatherBO> getListData() {
        // 1 获得输入的入库单号
        String thdNum = etTHDNum.getText().toString().trim();
        String warehouse = etWareHouse.getText().toString().trim();
        String dbdNum = etDBDNum.getText().toString().trim();
        if(currentPage == 0){
            setTotalCount(thdNum, warehouse,dbdNum);
        }

        return recordBLO.getRecordGather(RecordType.out, thdNum,
                warehouse, dbdNum, currentPage, getPageSize());
    }

    /**
     * 选择后返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data != null) {
                //送货单
                if (requestCode == thdRequestCode && resultCode == 10) {
                    String barcode = data.getExtras().getString("barcode");
                    this.etTHDNum.setText(barcode);
                    currentPage = 0;
                    super.loadData();
                }
                // 经销商选择返回结果
                if (requestCode == jxsRequestCode && resultCode == 1) {
                    String optionValue = data.getExtras().getString("dataValue");
                    this.etJXSNum.setText(optionValue);
                    currentPage = 0;
                    super.loadData();
                }
                // 仓库货位选择返回结果
                if (requestCode == wareHouseRequestCode && resultCode == 1) {
                    String optionValue = data.getExtras().getString("dataValue");
                    this.etWareHouse.setText(optionValue);
                    currentPage = 0;
                    super.loadData();
                }
                // 运输信息选择
                if (requestCode == 4 && resultCode == 1) {
                    tranId = data.getExtras().getLong("tranId");
                    String transportInfo = data.getExtras().getString("transportInfo");
                    this.tvTransport.setText(transportInfo);
                }
            } else {
                //扫描条码
                if (requestCode == 5 && resultCode == 10) {
                    currentPage = 0;
                    super.loadData();
                    etBarCode.setText("");
                }
            }

            if (requestCode == browserRequestCode && resultCode == browserResultCode) {
                super.currentPage = 0;
                super.loadData();
            }
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Toast.makeText(WOutActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
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
     * 返回
     */
    @OnClick(R.id.tv_wout_back)
    protected void back(View v) {
        super.back(v);
    }

    /**
     * 出库方式
     *
     * @author bipolor
     */
    @OnClick(R.id.ll_wout_outtype)
    protected void selectOutType(View v) {
        Builder builderExit = new AlertDialog.Builder(WOutActivity.this,
                R.style.dialog);
        builderExit.setIcon(R.drawable.dialog_logo);
        builderExit.setTitle("选择出库方式");
        builderExit.setItems(outTypeString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        outType = OutTypes.lingshou;
                        break;
                    case 1:
                        outType = OutTypes.zxsonghuo;
                        break;
                    case 2:
                        outType = OutTypes.jiehuo;
                        break;
                    case 3:
                        outType = OutTypes.wdsonghuo;
                        break;
                }
                setOutType();
                dialog.dismiss();
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
        builderExit.create();
        builderExit.show();
    }

    /**
     * 提货单、仓库货位值改变
     *
     * @param v
     */
    @OnFocusChange({R.id.et_wout_thdnum,
            R.id.et_wout_dbdnum,
            R.id.et_wout_warehouse,
            R.id.tv_wout_transport})
    protected void toReresh(View v) {

        super.loadData();

        if(v.getId() == R.id.et_wout_thdnum){
            String thdNum = etTHDNum.getText().toString().trim();
            searchJXSByTHDNum(thdNum);
        }
    }


    @OnClick(R.id.iv_wout_scan_thdnum)
    public void scanTHDNum(View v) {
        try {
            Intent intent = new Intent();
            intent.setClass(WOutActivity.this, ScanActivity.class);
            intent.putExtra("scanType", ScanTypes.Single.getValue());
            etTHDNum.setText("");
            ViewHelper.Focus(etTHDNum);
            startActivityForResult(intent, thdRequestCode);
        } catch (Exception e) {
            Toast.makeText(WOutActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.ll_wout_jxsNum)
    public void selectJXS(View v) {
        try {
            if (outType == OutTypes.jiehuo) {
                return;
            }
            Intent intent = new Intent(WOutActivity.this,
                    WmsSelectDataActivity.class);
            intent.putExtra("dataType", DataType.jxsNum.getValue());
            intent.putExtra("dataTitle", getResources().getString(R.string.wout_jxsNum));
            WOutActivity.this.startActivityForResult(intent, jxsRequestCode);
            overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
        } catch (Exception e) {
            Toast.makeText(WOutActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.ll_wout_warehouse)
    public void selectWareHouse(View v) {
        try {
            Intent intent = new Intent(WOutActivity.this,
                    WmsSelectDataActivity.class);
            intent.putExtra("dataType", DataType.wareHouse.getValue());
            intent.putExtra("dataTitle", getResources().getString(R.string.record_warehouse));
            WOutActivity.this.startActivityForResult(intent, wareHouseRequestCode);
            overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
        } catch (Exception e) {
            Toast.makeText(WOutActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.ll_wout_transport)
    public void selectTransport(View v) {
        try {
            Intent intent = new Intent(WOutActivity.this,
                    TransportListActivity.class);
            WOutActivity.this.startActivityForResult(intent, 4);
            overridePendingTransition(R.anim.in_from_right,
                    R.anim.out_to_left);
        } catch (Exception e) {
            Toast.makeText(WOutActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.iv_wout_scan_barcode)
    protected void scanBarCode(View v){
        try {
            if (!isValidate(false)) {
                return;
            }

            // 获取用户Id
            long userId = SPHelper.get("userId", (long) 0);
            String uid = SPHelper.get("uid", "");

            String thdNum = etTHDNum.getText().toString().trim();
            String dbdNum = etDBDNum.getText().toString().trim();
            String jxsNum = etJXSNum.getText().toString().trim();
            String wareHouse = etWareHouse.getText().toString().trim();
            String barCode = etBarCode.getText().toString().trim();
            Date createDate = DateHelper.getCurrDate();

            RecordBO record = new RecordBO();
            record.setBarCode(barCode);
            record.setThdNum(thdNum);
            record.setOutType(outType);
            record.setDbdNum(dbdNum);
            record.setJxsNum(jxsNum);
            record.setWarehouse(wareHouse);
            record.setTranId(tranId);
            record.setCreateUserId(userId);
            record.setCreateUserUID(uid);
            record.setCreateDate(createDate);
            record.setRecordType(RecordType.out);

            Intent intent = new Intent();
            intent.setClass(WOutActivity.this, ScanActivity.class);
            intent.putExtra("scanType", ScanTypes.Mutil.getValue());

            Bundle bundle = new Bundle();
            bundle.putSerializable("record", record);
            intent.putExtras(bundle);
            etBarCode.setText("");
            ViewHelper.Focus(etBarCode);
            startActivityForResult(intent, 4);
        } catch (Exception e) {
            Toast.makeText(WOutActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_wout_add_barcode)
    protected void toAddBarCode(View v){
        addBarCode();
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
            intent.putExtra("recordType", RecordType.out.getValue());
            intent.putExtra("serNo", recordGatherBO.getSerNo());
            intent.putExtra("count", recordGatherBO.getCount());
            intent.putExtra("thdNum", this.etTHDNum.getText().toString().trim());
            intent.putExtra("dbNum", this.etDBDNum.getText().toString().trim());
            intent.putExtra("wareHouse", this.etWareHouse.getText().toString().trim());
            startActivityForResult(intent, browserRequestCode);
        }
    }



    /**
     * 硬件扫描
     */
    @Override
    protected void handleScanBarCode(String barCode) {
        try {
            if (barCode != null) {
                if (keyCode == 134) {
                    ViewHelper.Focus(etBarCode);
                    etBarCode.setText(barCode);
                    addBarCode();
                } else if (keyCode == 135 || keyCode == 136) {
                    etTHDNum.setText(barCode);
                    ViewHelper.Focus(etBarCode);
                }
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 设置出库方式
     */
    private void setOutType() {
        tvOutType.setText(outTypeString[outType.getValue()]);
        if (outType == OutTypes.lingshou) {
            llDBDNum.setVisibility(View.GONE);
            vDBDNum.setVisibility(View.GONE);
            etDBDNum.setText("");

            llJXSNum.setVisibility(View.GONE);
            vJXSNum.setVisibility(View.GONE);
            etJXSNum.setText("");
        } else if (outType == OutTypes.zxsonghuo) {
            llDBDNum.setVisibility(View.VISIBLE);
            vDBDNum.setVisibility(View.VISIBLE);

            llJXSNum.setVisibility(View.VISIBLE);
            vJXSNum.setVisibility(View.VISIBLE);

        } else if (outType == OutTypes.jiehuo) {
            llDBDNum.setVisibility(View.GONE);
            vDBDNum.setVisibility(View.GONE);
            etDBDNum.setText("");

            llJXSNum.setVisibility(View.VISIBLE);
            vJXSNum.setVisibility(View.VISIBLE);
        } else if (outType == OutTypes.wdsonghuo) {
            llDBDNum.setVisibility(View.GONE);
            vDBDNum.setVisibility(View.GONE);
            etDBDNum.setText("");

            llJXSNum.setVisibility(View.VISIBLE);
            vJXSNum.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 设置添加的数量
     */
    private void setTotalCount(String thdNum,String wareHouse,String dbdNum) {

        // 1 获得输入的入库单号
        int totalCount = recordBLO.getRecordsTotalCount(RecordType.out, thdNum,
                wareHouse,dbdNum);

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
     * @return
     */
    private void searchJXSByTHDNum(String thdNum) {
        String token = SPHelper.get("token", "");
        String ip = SPHelper.get("ip", "");
        final StringBuffer stringBuffer = new StringBuffer();
        if (!StringHelper.isEmpty(ip) && DeviceHelper.hasInternet()) {

            WinNetApiHelper.searchJXSByTHDNum(ip, token, thdNum, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            handleSearchJXSByTHDNum(response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONObject errorResponse) {
                        }
                    }
            );
        }
    }

    /**
     * 设置添加的数量
     */

    private void handleSearchJXSByTHDNum(JSONObject response) {
        try {

            boolean flag = response.getBoolean("flag");
            if (flag) {

                String jxsNum = response.getString("msg");
                if(!StringHelper.isEmpty(jxsNum)){
                    etJXSNum.setText(jxsNum);
                }
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 添加条码
     *
     * @return
     */
    private void addBarCode() {
        try {
            // 1 验证输入的合法性
            if (!isValidate(true)) {
                return;
            }

            // 2 添加
            if (recordBLO == null) {
                recordBLO = new RecordBLO();
            }
            long userId = SPHelper.get("userId", (long) 0);
            String uid = SPHelper.get("uid", "");
            String token = SPHelper.get("token", "");
            String ip = SPHelper.get("ip", "");

            String thdNum = etTHDNum.getText().toString().trim();
            String dbdNum = etDBDNum.getText().toString().trim();
            String jxsNum = etJXSNum.getText().toString().trim();
            String wareHouse = etWareHouse.getText().toString().trim();
            String barCode = etBarCode.getText().toString().trim();
            Date createDate = DateHelper.getCurrDate();

            final RecordBO recordBO = new RecordBO();
            recordBO.setBarCode(barCode);
            recordBO.setThdNum(thdNum);
            recordBO.setOutType(outType);
            recordBO.setDbdNum(dbdNum);
            recordBO.setJxsNum(jxsNum);
            recordBO.setWarehouse(wareHouse);
            recordBO.setTranId(tranId);
            recordBO.setCreateUserId(userId);
            recordBO.setCreateUserUID(uid);
            recordBO.setCreateDate(createDate);
            recordBO.setRecordType(RecordType.out);

            recordBLO.convertToRecord(recordBO);//转换
            if(StringHelper.isEmpty(ip)){
                //添加到本地
                if(recordBLO.addRecord(recordBO)){
                    AudioHelper.openSpeaker(this, R.raw.zdjer_ok);// 提示音
                    ToastHelper.showToast(R.string.wms_common_save_success);

                }else{
                    AudioHelper.openSpeaker(this, R.raw.zdjer_error1);// 提示音
                    DialogHelper.getMessageDialog(this,getString(R.string.wms_common_save_failed)).show();
                }
            }else {
                //网络
                if(!DeviceHelper.hasInternet()){
                    ToastHelper.showToast(R.string.wms_common_no_net);
                }else{
                    WinNetApiHelper.uploadOutRecord(ip, token, recordBO, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            handleUploadOutRecord(recordBO, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONObject errorResponse) {
                            DialogHelper.getMessageDialog(WOutActivity.this, getString(R.string.wms_net_error) + statusCode);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Toast.makeText(WOutActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 处理上传记录
     * @param recordBO
     * @param response
     */
    private void handleUploadOutRecord(RecordBO recordBO,JSONObject response){
        try {
            boolean flag = response.getBoolean("flag");
            if (flag) {
                recordBO.setIsUpload(YesNos.Yes);
            }
            if(flag && recordBLO.addRecord(recordBO)){
                AudioHelper.openSpeaker(this, R.raw.zdjer_ok);// 提示音
                ToastHelper.showToast(R.string.wms_common_save_success);

            }else{
                AudioHelper.openSpeaker(this, R.raw.zdjer_error1);// 提示音
                String msg = response.getString("msg");
                if(StringHelper.isEmpty(msg)){
                    DialogHelper.getMessageDialog(this,getString(R.string.wms_common_save_failed)).show();
                }else{
                    DialogHelper.getMessageDialog(this,msg).show();
                }

            }
        }catch(Exception e){
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 验证输入是否有效
     *
     * @return
     */
    private Boolean isValidate(Boolean isValidateBarCode) {
        String thdNum = etTHDNum.getText().toString().trim();
        try {
            // 1 送货单号
            if (outType == OutTypes.zxsonghuo) {
                if (thdNum.length() == 0) {
                    Toast.makeText(WOutActivity.this,
                            R.string.wout_thdNum_lengtherror, Toast.LENGTH_LONG)
                            .show(); // 聚焦提货单号
                    ViewHelper.Focus(etTHDNum);
                    return false;
                }
            }

            //2 经销商编号
            if (outType == OutTypes.zxsonghuo ||
                    outType == OutTypes.wdsonghuo ||
                    outType == OutTypes.jiehuo) {
                String jxsNum = etJXSNum.getText().toString().trim();
                if (jxsNum.length() == 0) {
                    Toast.makeText(WOutActivity.this, R.string.wout_jxsNum_lengtherror,
                            Toast.LENGTH_LONG).show();
                    ViewHelper.Focus(etJXSNum);
                    return false;
                }
            }

            if (isValidateBarCode) {

                // 3 条形码
                String barCode = etBarCode.getText().toString().trim();
                // 3.1 未输入
                if (barCode.length() == 0) {
                    Toast.makeText(WOutActivity.this, R.string.record_barcode_null,
                            Toast.LENGTH_SHORT).show();
                    ViewHelper.Focus(etBarCode);
                    return false;
                }

                // 3.3 已存在
                if (recordBLO.isExist(RecordType.out, barCode)) {
                    Toast.makeText(WOutActivity.this, R.string.record_barcode_exist,
                            Toast.LENGTH_SHORT).show();
                    AudioHelper.openSpeaker(this, R.raw.zdjer_error2);// 提示音
                    AudioHelper.startVibrator(this);
                    etBarCode.setText("");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            Toast.makeText(WOutActivity.this, "系统出错，请联系系统管理员！",
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
