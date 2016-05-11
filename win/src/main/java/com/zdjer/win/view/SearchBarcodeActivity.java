package com.zdjer.win.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.win.R;
import com.zdjer.win.adapter.SearchBarCodeAdapter;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.bean.RecordType;
import com.zdjer.win.model.RecordBLO;
import com.zdjer.win.view.helper.ScanActivity;
import com.zdjer.wms.utils.BroadcastReceiverHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Activity:历史记录
 */
public class SearchBarcodeActivity extends BaseListActivity<RecordBO>
        implements OnScrollListener, OnQueryTextListener {

    @Bind(R.id.tv_search_barcode_in)
    protected TextView tvOptionIn;

    @Bind(R.id.tv_search_barcode_out)
    protected TextView tvOptionOut;

    @Bind(R.id.sv_search_barcode_barcode)
    protected SearchView svSearch;// 查询
    private RecordBLO recordBLO = new RecordBLO();

    protected BroadcastReceiverHelper broadcastReceiverHelper;
    private boolean isAllowScan = false;

    private RecordType recordType = RecordType.in;// 类型
    private String qureyText = "";//查询字符串

    @Override
    protected int getLayoutId() {

        return R.layout.activity_search_barcode;
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {

        return (SwipeRefreshLayout) findViewById(R.id.srl_search_barcode_barcode);
    }

    @Override
    protected ListView getListView() {

        return (ListView) findViewById(R.id.lv_search_barcode_barcode);
    }

    @Override
    protected BaseListAdapter<RecordBO> getListAdapter() {

        return new SearchBarCodeAdapter();
    }

    @Override
    public void initView() {
        super.initView();

        svSearch.setOnQueryTextListener(this);
/*
        setInType();
        etTHDNum.setText("");
        etWareHouse.setText("");
        etBarCode.setText("");
        ViewHelper.Focus(etBarCode);*/
    }

    @Override
    protected List<RecordBO> getListData() {
        // 1 获得输入的入库单号
        /*String thdNum = etTHDNum.getText().toString().trim();
        String warehouse = etWareHouse.getText().toString().trim();
        if(currentPage == 0){
            setTotalCount(thdNum, warehouse);
        }

        return recordBLO.getRecordGather(RecordType.in, thdNum,
                warehouse, "",currentPage, getPageSize());*/

        return recordBLO.getRecordsLikeBarCode(recordType, qureyText, currentPage, getPageSize());
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
                if (broadcastReceiverHelper.getIsScanBarCode() && isAllowScan) {
                    String barcode = intent.getStringExtra("se4500");
                    handleScanBarCode(barcode);
                }
            }
        };
        broadcastReceiverHelper.registerAction();
    }

    /**
     * 处理扫描的条码
     */
    private void handleScanBarCode(String barCode) {
        if (barCode != null) {
            isAllowScan = false;
            ViewHelper.Focus(svSearch);
            svSearch.setQuery(barCode,false);
            loadData();
            isAllowScan = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.broadcastReceiverHelper.stopScan();
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

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            qureyText = newText;
            currentPage = 0;
            loadData();
            return true;
        } catch (Exception e) {
            Toast.makeText(SearchBarcodeActivity.this,
                    R.string.wms_common_exception, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (svSearch != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        svSearch.getWindowToken(), 0);
            }
            svSearch.clearFocus();
        }
        return true;
    }

    /**
     * 返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        //扫描条码
        if (resultCode == 10) {
            Bundle bundle = data.getExtras();
            //显示扫描到的内容
            svSearch.setQuery(bundle.getString("result"), false);
            this.handleScanBarCode(bundle.getString("result"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.tv_search_barcode_back)
    protected void back(View v) {
        SearchBarcodeActivity.this.finish();
        overridePendingTransition(R.anim.in_from_left,
                R.anim.out_to_right);
    }

    @OnClick(R.id.iv_search_barcode_scan_barcode)
    public void scanBarCode(View v) {
        try {
            Intent intent = new Intent();
            intent.setClass(SearchBarcodeActivity.this, ScanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 10);

        } catch (Exception e) {
            Toast.makeText(SearchBarcodeActivity.this, R.string.wms_common_exception,
                    Toast.LENGTH_LONG).show();
        }
    }


    @OnClick(R.id.tv_search_barcode_in)
    protected void inOptionClick(View v) {

        TextView tvOption = (TextView) v;
        recordType = RecordType.in;
        tvOptionIn.setTextColor(SearchBarcodeActivity.this
                .getResources().getColor(R.color.wms_white));
        tvOptionOut.setTextColor(SearchBarcodeActivity.this
                .getResources().getColor(R.color.wms_black));
        qureyText = svSearch.getQuery().toString();
        loadData();
    }

    @OnClick(R.id.tv_search_barcode_out)
    protected void outOptionClick(View v) {
        TextView tvOption = (TextView) v;
        recordType = RecordType.out;
        tvOptionIn.setTextColor(SearchBarcodeActivity.this
                .getResources().getColor(R.color.wms_black));
        tvOptionOut.setTextColor(SearchBarcodeActivity.this
                .getResources().getColor(R.color.wms_white));
        qureyText = svSearch.getQuery().toString();
        loadData();
    }
}
