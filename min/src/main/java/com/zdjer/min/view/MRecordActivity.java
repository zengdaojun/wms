package com.zdjer.min.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zdjer.min.R;
import com.zdjer.min.adapter.MRecordAdapter;
import com.zdjer.min.bean.MRecordBO;
import com.zdjer.min.bean.MRecordType;
import com.zdjer.min.model.MRecordBLO;
import com.zdjer.utils.view.AppHelper;
import com.zdjer.utils.view.AudioHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.utils.view.dialog.DialogHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MRecordActivity extends BaseListActivity<MRecordBO> {

    private int resultCode = 1;//

    @Bind(R.id.tv_mrecord_title)
    TextView tvTitle = null;//标题

    private MRecordBLO mrecordBLO = new MRecordBLO();
    private MRecordType mrecordType = MRecordType.in;
    private long sendPersonId = 0;
    private String serNo = "";

    @Override
    protected int getLayoutId() {

        return R.layout.activity_mrecord;
    }

    @Override
    public void initView(){
        super.initView();
        Intent intent = getIntent();
        mrecordType = MRecordType.value(intent.getIntExtra("mrecordType", 0));
        serNo = intent.getStringExtra("serNo");
        sendPersonId = intent.getLongExtra("sendPersonId", 0);

        tvTitle.setText(serNo);
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {

        return (SwipeRefreshLayout) findViewById(R.id.srl_mrecord_barcode);
    }

    @Override
    protected ListView getListView() {

        return (ListView) findViewById(R.id.lv_mrecord_barcode);
    }

    @Override
    protected BaseListAdapter getListAdapter() {

        return new MRecordAdapter();
    }

    @Override
    protected List getListData() {

        return mrecordBLO.getMRecords(mrecordType, serNo, sendPersonId, currentPage, getPageSize());
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
        final MRecordBO mrecordBO = baseListAdapter.getItem(position);
        if (mrecordBO != null) {

            String message = "您确定要删除条形码“" + mrecordBO.getBarCode()
                    + "”吗？";

            DialogHelper.getConfirmDialog(this, message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //添加到本地
                    if(mrecordBLO.deleteMRecord(mrecordBO.getMRecordId())){
                        AudioHelper.openSpeaker(MRecordActivity.this, R.raw.zdjer_ok);// 提示音
                        loadData();
                        ToastHelper.showToast(R.string.wms_common_delete_success);
                    }else{
                        AudioHelper.openSpeaker(MRecordActivity.this, R.raw.zdjer_error1);// 提示音
                        DialogHelper.getMessageDialog(MRecordActivity.this,getString(R.string.wms_common_delete_faild)).show();
                    }
                    loadData();
                }
            }).show();
        }
    }

    @Override
    @OnClick({R.id.tv_mrecord_back})
    public void onClick(View v) {

        try {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.tv_mrecord_back: {
                    this.setResult(resultCode);
                    AppHelper.finishActivity();
                    break;
                }
            }
        } catch (Exception e) {

            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }
}
