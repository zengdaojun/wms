package com.zdjer.win.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.zdjer.win.bean.InTypes;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.bean.RecordType;
import com.zdjer.win.model.RecordBLO;
import com.zdjer.win.utils.WinNetApiHelper;
import com.zdjer.win.view.core.ScanTypes;
import com.zdjer.win.view.helper.ScanActivity;
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
 * Activity:入库
 */
public class WInActivity extends WIOCActivity {

    @Bind(R.id.tv_win_intype)
    protected TextView tvInType;//入库方式

    private String intypeString[] = new String[]{
            "中心收货",
            "网点收货",
            "直发",
            "借货",
            "初始"};
    private InTypes inType = InTypes.shouhuo_zx;

    @Bind(R.id.et_win_thdnum)
    protected EditText etTHDNum; // 入库单号

    @Bind(R.id.ll_win_jxsnum)
    protected LinearLayout llJXSNum;// 经销商信息 (行)
    @Bind(R.id.et_win_jxsNum)
    protected EditText etJXSNum;// 经销商编号
    @Bind(R.id.v_win_jxs)
    protected View viewJXS;
    private int jxsNumRequestCode = 1;

    @Bind(R.id.et_win_warehouse)
    protected EditText etWareHouse;// 仓库货位(存放选择后的值)
    private int wareHouseRequestCode = 2;

    @Bind(R.id.et_win_barcode)
    protected EditText etBarCode; // 条形码
    @Bind(R.id.iv_win_scan)
    protected ImageView imgScan;//扫描
    private int scanRequestCode = 3;

    @Bind(R.id.tv_win_scaned_total)
    protected TextView tvTotalCount;// 已添加条码
    private RecordBLO recordBLO = new RecordBLO();// 记录业务逻辑类

    private int keyCode = 0;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_win;
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {

        return (SwipeRefreshLayout) findViewById(R.id.srl_win_barcode);
    }

    @Override
    protected ListView getListView() {

        return (ListView) findViewById(R.id.lv_win_barcode);
    }

    @Override
    protected BaseListAdapter<RecordGatherBO> getListAdapter() {

        return new RecordGatherAdapter();
    }

    @Override
    public void initView() {
        super.initView();

        setInType();
        etTHDNum.setText("");
        etWareHouse.setText("");
        etBarCode.setText("");

        ViewHelper.Focus(etBarCode);
    }

    @Override
    protected List<RecordGatherBO> getListData() {
        // 1 获得输入的入库单号
        String thdNum = etTHDNum.getText().toString().trim();
        if(currentPage == 0){
            setTotalCount(thdNum, "");
        }

        return recordBLO.getRecordGather(RecordType.in, thdNum,"",currentPage, getPageSize());
    }


    /**
     * 返回
     */
    @OnClick(R.id.tv_win_back)
    protected void back(View v) {
        super.back(v);
    }

    /**
     * 选择入库方式
     */
    @OnClick(R.id.ll_win_intype)
    public void selectInType(View v) {
        Builder builderExit = new AlertDialog.Builder(WInActivity.this,
                R.style.dialog);
        builderExit.setIcon(R.drawable.dialog_logo);
        builderExit.setTitle("选择入库方式");
        builderExit.setItems(intypeString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        inType = InTypes.shouhuo_zx;
                        break;
                    case 1:
                        inType = InTypes.shouhuo_wd;
                        break;
                    case 2:
                        inType = InTypes.zhifa;
                        break;
                    case 3:
                        inType = InTypes.jiehuo;
                        break;
                    case 4:
                        inType = InTypes.chushi;
                        break;

                }
                setInType();
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
    @OnFocusChange(R.id.et_win_thdnum)
    protected void toReresh(View v) {
        super.loadData();
    }

    /**
     * 选择经销商编号
     *
     * @param v
     */
    @OnClick(R.id.ll_win_jxsnum)
    protected void selectJXS(View v) {
        Intent intent = new Intent(WInActivity.this,
                WmsSelectDataActivity.class);
        intent.putExtra("dataType", DataType.jxsNum.getValue());
        intent.putExtra("dataTitle", getResources().getString(R.string.wout_jxsNum));
        WInActivity.this.startActivityForResult(intent, jxsNumRequestCode);
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
    }

    /**
     * 选择仓库货位
     *
     * @param v
     */
    @OnClick(R.id.ll_win_warehouse)
    protected void selectWareHouse(View v) {
        Intent intent = new Intent(WInActivity.this,
                WmsSelectDataActivity.class);
        intent.putExtra("dataType", DataType.wareHouse.getValue());
        intent.putExtra("dataTitle",
                getResources().getString(R.string.record_warehouse));
        startActivityForResult(intent, wareHouseRequestCode);
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
    }

    /**
     * 扫描
     *
     * @param v
     */
    @OnClick(R.id.iv_win_scan)
    protected void toScan(View v) {
        if (!isValidate(false)) {
            return;
        }

        // 2 添加
        SharedPreferences sharedPreferences = getSharedPreferences(
                "win", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("currUserId", 0);
        String uid = sharedPreferences.getString("currUid", "");

        String thdNum = etTHDNum.getText().toString().trim();
        String jxsNum = etJXSNum.getText().toString().trim();
        String wareHouse = etWareHouse.getText().toString().trim();
        String barCode = etBarCode.getText().toString().trim();
        Date createDate = DateHelper.getCurrDate();

        RecordBO record = new RecordBO();
        record.setBarCode(barCode);
        record.setThdNum(thdNum);
        record.setInType(inType);
        record.setJxsNum(jxsNum);
        record.setWarehouse(wareHouse);
        record.setCreateUserId(userId);
        record.setCreateUserUID(uid);
        record.setCreateDate(createDate);
        record.setRecordType(RecordType.in);

        Intent intent = new Intent();
        intent.setClass(WInActivity.this, ScanActivity.class);
        intent.putExtra("scanType", ScanTypes.Mutil.getValue());

        Bundle bundle = new Bundle();
        bundle.putSerializable("record", record);
        intent.putExtras(bundle);
        etBarCode.setText("");
        ViewHelper.Focus(etBarCode);
        startActivityForResult(intent, scanRequestCode);
    }

    /**
     * 添加条码
     *
     * @param v
     */
    @OnClick(R.id.btn_win_add_barcode)
    protected void toAddBarCode(View v) {
        if(isAllowAdd) {
            isAllowAdd = false;
            addBarCode();
        }else{
            ToastHelper.showToast(R.string.wms_common_dealing);
        }
    }

    private int browserRequestCode = 0;
    private int browserResultCode = 1;

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
            intent.putExtra("recordType", RecordType.in.getValue());
            intent.putExtra("serNo", recordGatherBO.getSerNo());
            intent.putExtra("count", recordGatherBO.getCount());
            intent.putExtra("thdNum", this.etTHDNum.getText().toString().trim());
            startActivityForResult(intent, browserRequestCode);
        }
    }

    /**
     * 返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data != null) {
                // 经销商选择返回结果
                if (requestCode == jxsNumRequestCode && resultCode == 1) {
                    String optionValue = data.getExtras().getString("dataValue");
                    this.etJXSNum.setText(optionValue);
                }
                // 入库选择仓库货位
                if (requestCode == wareHouseRequestCode && resultCode == 1) {
                    String dataValue = data.getExtras().getString("dataValue");
                    this.etWareHouse.setText(dataValue);
                    loadData();
                }
            } else {
                //扫描条码
                if (requestCode == scanRequestCode && resultCode == 10) {
                    loadData();
                    etBarCode.setText("");
                }
            }

            if (requestCode == browserRequestCode && resultCode == browserResultCode) {
                super.currentPage = 0;
                super.loadData();
            }
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Toast.makeText(WInActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 退出该功能
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KEY_SCAN1:
            case KEY_SCAN2:
            case KEY_SCAN3: {
                this.keyCode = keyCode;
                break;
            }
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_HOME: {
                back(null);
                break;
            }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 根据设置的入库方式设置界面
     */
    private void setInType() {
        tvInType.setText(intypeString[inType.getValue()]);
        if (inType == InTypes.jiehuo) {
            llJXSNum.setVisibility(View.VISIBLE);
            viewJXS.setVisibility(View.VISIBLE);
        } else {
            llJXSNum.setVisibility(View.GONE);
            viewJXS.setVisibility(View.GONE);
            etJXSNum.setText("");
        }
    }

    /**
     * 硬件扫描
     */
    @Override
    protected void handleScanBarCode(String barCode) {
        try {
            if (!StringHelper.isEmpty(barCode)) {
                //isAllowScan = false;
                if (this.keyCode == KEY_SCAN1) {
                    etBarCode.setText(barCode);
                    if(isAllowAdd) {
                        isAllowAdd = false;
                        addBarCode();
                    }else{
                        ToastHelper.showToast(R.string.wms_common_dealing);
                    }
                } else if (keyCode == KEY_SCAN2 || keyCode == KEY_SCAN3) {
                    etTHDNum.setText(barCode);
                }
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 设置添加的数量
     */

    private void setTotalCount(String thdNum,String warehouse) {

        // 1 获得输入的入库单号
        int totalCount = recordBLO.getRecordsTotalCount(RecordType.in, thdNum,"");

        if (totalCount == 0) {
            tvTotalCount.setVisibility(View.INVISIBLE);
        } else {
            tvTotalCount.setVisibility(View.VISIBLE);

            StringBuffer sbText = new StringBuffer();
            sbText.append(String.format("已扫条码%d", totalCount));
            tvTotalCount.setText(sbText.toString());
        }
        searchTHDNum(thdNum);
    }

    /**
     * @return
     */
    private void searchTHDNum(String thdNum) {
        String token = SPHelper.get("token", "");
        String ip = SPHelper.get("ip", "");
        final StringBuffer stringBuffer = new StringBuffer();
        if (!StringHelper.isEmpty(ip) && DeviceHelper.hasInternet()) {

            WinNetApiHelper.searchTHDNum(ip, token, thdNum, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            handleSearchTHDNum(response);
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

    private void handleSearchTHDNum(JSONObject response) {
        try {

            boolean flag = response.getBoolean("flag");
            if (flag) {
                tvTotalCount.setVisibility(View.VISIBLE);

                StringBuffer sbText = new StringBuffer(tvTotalCount.getText());
                if (sbText.length() > 0) {
                    sbText.append(";");
                }
                sbText.append("发货总数:" + response.getString("totalSize"));
                sbText.append(",");
                sbText.append("未扫条码:" + response.getString("msg"));
                tvTotalCount.setText(sbText.toString());
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 添加条码
     */
    private void addBarCode() {
        Log.i("addBarCode",String.valueOf(isAllowAdd));
        try {
            // 1 验证输入的合法性
            if (!isValidate(true)) {
                return;
            }

            // 2 添加
            long userId = SPHelper.get("userId", (long) 0);
            String uid = SPHelper.get("uid", "");
            String token = SPHelper.get("token", "");
            String ip = SPHelper.get("ip", "");

            String thdNum = etTHDNum.getText().toString().trim();
            String jxsNum = etJXSNum.getText().toString().trim();
            String wareHouse = etWareHouse.getText().toString().trim();
            String barCode = etBarCode.getText().toString().trim();
            Date createDate = DateHelper.getCurrDate();

            final RecordBO recordBO = new RecordBO();
            recordBO.setBarCode(barCode);
            recordBO.setThdNum(thdNum);
            recordBO.setInType(inType);
            recordBO.setJxsNum(jxsNum);
            recordBO.setWarehouse(wareHouse);
            recordBO.setCreateUserId(userId);
            recordBO.setCreateUserUID(uid);
            recordBO.setCreateDate(createDate);
            recordBO.setRecordType(RecordType.in);

            recordBLO.convertToRecord(recordBO);//转换
            if(StringHelper.isEmpty(ip)){
                //添加到本地
                if(recordBLO.addRecord(recordBO)){
                    isAllowAdd = true;
                    AudioHelper.openSpeaker(this, R.raw.zdjer_ok);// 提示音
                    ToastHelper.showToast(R.string.wms_common_save_success);
                    loadData();

                }else{
                    isAllowAdd = true;
                    AudioHelper.openSpeaker(this, R.raw.zdjer_error1);// 提示音
                        DialogHelper.getMessageDialog(this,getString(R.string.wms_common_save_failed)).show();
                }
            }else {
                //网络
                if(!DeviceHelper.hasInternet()){
                    ToastHelper.showToast(R.string.wms_common_no_net);
                    isAllowAdd = true;
                }else{
                    WinNetApiHelper.uploadInRecord(ip,token,recordBO,new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            handleUploadInRecord(recordBO, response);
                        }

                        /*public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            DialogHelper.getMessageDialog(WInActivity.this, getString(R.string.wms_net_error) + statusCode);
                            isAllowAdd = true;
                        }*/

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONObject errorResponse) {
                            DialogHelper.getMessageDialog(WInActivity.this, getString(R.string.wms_net_error) + statusCode);
                            isAllowAdd = true;
                        }
                    });
                }
            }
        } catch (Exception e) {
            Toast.makeText(WInActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 处理上传记录
     * @param recordBO
     * @param response
     */
    private void handleUploadInRecord(RecordBO recordBO,JSONObject response){
        try {
            boolean flag = response.getBoolean("flag");
            if (flag) {
                recordBO.setIsUpload(YesNos.Yes);
            }
            if(flag && recordBLO.addRecord(recordBO)){
                isAllowAdd = true;
                AudioHelper.openSpeaker(this, R.raw.zdjer_ok);// 提示音
                ToastHelper.showToast(R.string.wms_common_save_success);
                loadData();

            }else{
                isAllowAdd = true;
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
     * @return true:有效；false:无效
     */
    private Boolean isValidate(Boolean isValidateBarCode) {
        try {
            // 1 提货单号
            if (etTHDNum.getText().toString().trim().length() == 0) {
                Toast.makeText(WInActivity.this, R.string.win_thdNum_lengtherror,
                        Toast.LENGTH_LONG).show();
                ViewHelper.Focus(etTHDNum);
                return false;
            }

            //2 经销商编号
            if (inType == InTypes.jiehuo) {
                if (etJXSNum.getText().toString().trim().length() == 0) {
                    Toast.makeText(WInActivity.this, R.string.win_jxsNum_lengtherror,
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
                    Toast.makeText(WInActivity.this, R.string.record_barcode_null,
                            Toast.LENGTH_SHORT).show();
                    ViewHelper.Focus(etBarCode);
                    return false;
                }

                Log.i("isValidate","isExist"+barCode);
                if (recordBLO.isExist(RecordType.in, barCode)) {
                    Toast.makeText(WInActivity.this, R.string.record_barcode_exist,
                            Toast.LENGTH_SHORT).show();
                    AudioHelper.openSpeaker(this, R.raw.zdjer_error2);// 提示音
                    etBarCode.setText("");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            Toast.makeText(WInActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }
}