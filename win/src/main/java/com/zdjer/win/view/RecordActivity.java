package com.zdjer.win.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.AudioHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.win.R;
import com.zdjer.win.adapter.RecordAdapter;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.bean.RecordType;
import com.zdjer.win.model.RecordBLO;
import com.zdjer.win.utils.WinNetApiHelper;
import com.zdjer.wms.bean.core.YesNos;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class RecordActivity extends BaseListActivity<RecordBO> {

    private int resultCode = 1;//

    @Bind(R.id.tv_record_title)
    TextView tvTitle = null;//标题

    private RecordBLO recordBLO = new RecordBLO();
    private RecordType recordType = RecordType.in;
    private long sendPersonId = 0;
    private String serNo = "";
    private int count = 0;
    private String thdNum = "";
    private String dbNum = "";
    private long tranId = 0;


    @Override
    protected int getLayoutId() {

        return R.layout.activity_record;
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        recordType = RecordType.value(intent.getIntExtra("recordType", 0));
        serNo = intent.getStringExtra("serNo");
        count = intent.getIntExtra("count",0);
        thdNum = intent.getStringExtra("thdNum");
        dbNum = intent.getStringExtra("dbNum");
        tranId = intent.getLongExtra("tranId", (long) 0);
        tvTitle.setText(serNo);
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {

        return (SwipeRefreshLayout) findViewById(R.id.srl_record_barcode);
    }

    @Override
    protected ListView getListView() {

        return (ListView) findViewById(R.id.lv_record_barcode);
    }

    @Override
    protected BaseListAdapter getListAdapter() {

        return new RecordAdapter();
    }

    @Override
    protected List getListData() {

        return recordBLO.getRecords(recordType, serNo, thdNum, dbNum, currentPage, getPageSize());
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
        final RecordBO recordBO = baseListAdapter.getItem(position);
        if (recordBO != null) {

            String message = "您确定要删除条形码“" + recordBO.getBarCode()
                    + "”吗？";

            DialogHelper.getConfirmDialog(this, message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    String token = SPHelper.get("token", "");
                    String ip = SPHelper.get("ip", "");

                    if(StringHelper.isEmpty(ip)){
                        //添加到本地
                        if(recordBLO.deleteRecord(recordBO.getRecordId())){
                            AudioHelper.openSpeaker(RecordActivity.this, R.raw.zdjer_ok);// 提示音
                            ToastHelper.showToast(R.string.wms_common_delete_success);
                            loadData();
                        }else{
                            AudioHelper.openSpeaker(RecordActivity.this, R.raw.zdjer_error1);// 提示音
                            DialogHelper.getMessageDialog(RecordActivity.this,getString(R.string.wms_common_delete_faild)).show();
                        }
                    }else {
                        //网络
                        recordBLO.convertToRecord(recordBO);//转换
                        if(!DeviceHelper.hasInternet()){
                            ToastHelper.showToast(R.string.wms_common_no_net);
                        }else{
                            WinNetApiHelper.deleteRecord(ip, token, recordBO, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    handleDeleteRecord(recordBO, response);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable
                                        throwable, JSONObject errorResponse) {
                                    DialogHelper.getMessageDialog(RecordActivity.this, getString(R.string.wms_net_error) + statusCode);
                                }
                            });
                        }
                    }
                }
            }).show();
        }
    }

    /**
     * 处理删除记录
     * @param recordBO
     * @param response
     */
    private void handleDeleteRecord(RecordBO recordBO,JSONObject response){
        try {
            boolean flag = response.getBoolean("flag");
            if (flag) {
                recordBO.setIsUpload(YesNos.Yes);
            }
            if(flag && recordBLO.deleteRecord(recordBO.getRecordId())){
                AudioHelper.openSpeaker(this, R.raw.zdjer_ok);// 提示音
                ToastHelper.showToast(R.string.wms_common_delete_success);
                loadData();

            }else{
                AudioHelper.openSpeaker(this, R.raw.zdjer_error1);// 提示音
                String msg = response.getString("msg");
                if(StringHelper.isEmpty(msg)){
                    DialogHelper.getMessageDialog(this,getString(R.string.wms_common_delete_faild)).show();
                }else{
                    DialogHelper.getMessageDialog(this,msg).show();
                }

            }
        }catch(Exception e){
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    @OnClick({R.id.tv_record_back})
    public void back(View v) {

        this.setResult(resultCode);
        this.finish();
    }
}
