package com.zdjer.min.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.min.R;
import com.zdjer.min.bean.BarCodeTypes;
import com.zdjer.min.bean.MRecordBO;
import com.zdjer.min.bean.MRecordType;
import com.zdjer.min.model.MRecordBLO;
import com.zdjer.min.utils.MinNetApiHelper;
import com.zdjer.utils.DateHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.AudioHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.wms.adapter.RecordGatherAdapter;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.bean.ProductBO;
import com.zdjer.wms.bean.RecordGatherBO;
import com.zdjer.wms.model.ProductBLO;
import com.zdjer.wms.utils.BroadcastReceiverHelper;
import com.zdjer.wms.view.widget.WmsSelectDataActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Activity:网点入库
 */
public class MIOCActivity extends BaseListActivity<RecordGatherBO> {

    @Bind(R.id.tv_mioc_title)
    protected TextView tvTitle;//标题

    @Bind(R.id.tv_mioc_sendperson)
    protected TextView tvSendperson;// 送货师傅

    @Bind(R.id.ll_mioc_sendperson)
    protected LinearLayout llSendperson;//送货师傅组
    @Bind(R.id.v_mioc_sendperson_line)
    protected View vSendpersonLine;//送货师傅分割线

    private int sendPersonRequestCode = 1;
    private int sendPersonResultCode = 1;

    private int browserRequestCode = 2;
    private int browserResultCode = 1;

    private long sendPersonId = 0;//送货师傅Id
    private long createUserId = 0;//创建人

    @Bind(R.id.et_mioc_barcode)
    protected EditText etBarCode;//条码

    private MRecordBLO mrecordBlo = new MRecordBLO();
    private ProductBLO productBlo = new ProductBLO();

    private BroadcastReceiverHelper broadcastReceiverHelper;
    private Boolean isAllowScan = true;

    private MRecordType mrecordType = MRecordType.in;


    @Override
    protected int getLayoutId() {

        return R.layout.activity_mioc;
    }

    /**
     * 获得刷新布局
     *
     * @return
     */
    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {

        return (SwipeRefreshLayout) findViewById(R.id.srl_mioc_barcode);
    }

    /**
     * 获得列表控件
     *
     * @return
     */
    @Override
    protected ListView getListView() {

        return (ListView) findViewById(R.id.lv_mioc_barcode);
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
     * 初始化视图
     */
    @Override
    public void initView() {
        super.initView();

        mrecordType = MRecordType.value(getIntent().getIntExtra("mrecordType", 0));

        if (mrecordType == MRecordType.in) {
            tvTitle.setText(getString(R.string.min_title));

            llSendperson.setVisibility(View.VISIBLE);
            vSendpersonLine.setVisibility(View.VISIBLE);

        } else if (mrecordType == MRecordType.out) {
            tvTitle.setText(getString(R.string.mout_title));

            llSendperson.setVisibility(View.VISIBLE);
            vSendpersonLine.setVisibility(View.VISIBLE);

        } else if (mrecordType == MRecordType.check) {
            tvTitle.setText(getString(R.string.mcheck_title));

            llSendperson.setVisibility(View.GONE);
            vSendpersonLine.setVisibility(View.GONE);
        }
        createUserId = SPHelper.get("createUserId", (long) 0);
    }

    @Override
    protected List<RecordGatherBO> getListData() {

        return mrecordBlo.getRecordGather(mrecordType, sendPersonId, currentPage, getPageSize());
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
            ViewHelper.Focus(etBarCode);
            etBarCode.setText(barCode);
            addBarCode();
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


    /**
     * 返回
     */
    @OnClick(R.id.tv_mioc_back)
    protected void back(View v) {
        DialogHelper.getConfirmDialog(this, getString(R.string.wms_common_exit_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(R.anim.in_from_left,
                                R.anim.out_to_right);
                    }
                }).show();
    }

    /**
     * 提交
     */
    @OnClick(R.id.tv_mioc_submit)
    protected void submit(View v) {

        //通过用户Id，记录类型获得全部记录
        final List<MRecordBO> lstMRecord = mrecordBlo.getMRecords(mrecordType, sendPersonId);

        if (lstMRecord.size() == 0) {
            ToastHelper.showToast(R.string.wms_common_submit_no_data);
            return;
        }

        StringBuffer stringBuffer = getMBarCodeMessage(lstMRecord);

        DialogHelper.getConfirmDialog(this,
                stringBuffer.toString() + getString(R.string.wms_common_submit_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String token = SPHelper.get("token", "");
                        String ip = SPHelper.get("ip", "");// 明文
                        if (StringHelper.isEmpty(ip)) {
                            ToastHelper.showToast(R.string.wms_common_sync_noallowsync);
                            return;
                        }
                        if (!DeviceHelper.hasInternet()) {
                            ToastHelper.showToast(R.string.wms_common_no_net);
                            return;
                        }

                        showWaitDialog(R.string.wms_common_submiting);
                        String barcodes = mrecordBlo.convertMRecord(lstMRecord);

                        MinNetApiHelper.uploadInMRecord(ip, token, sendPersonId, barcodes, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                try {
                                    boolean flag = response.getBoolean("flag");
                                    if (flag && mrecordBlo.deleteMRecord(mrecordType, sendPersonId)) {
                                        AudioHelper.openSpeaker(MIOCActivity.this, R.raw.zdjer_ok);// 提示音
                                        ToastHelper.showToast(R.string.wms_common_submit_success);

                                    } else {
                                        AudioHelper.openSpeaker(MIOCActivity.this, R.raw.zdjer_error1);// 提示音
                                        String msg = response.getString("msg");
                                        if (StringHelper.isEmpty(msg)) {
                                            DialogHelper.getMessageDialog(MIOCActivity.this, getString(R.string.wms_common_submit_faild)).show();
                                        } else {
                                            DialogHelper.getMessageDialog(MIOCActivity.this, msg).show();
                                        }

                                    }
                            }

                            catch(
                            Exception e
                            )

                            {
                                ToastHelper.showToast(R.string.wms_common_submit_faild);
                            }

                            hideWaitDialog();
                        }

                        @Override
                        public void onFailure ( int statusCode, Header[] headers, Throwable
                        throwable, JSONObject errorResponse){
                            hideWaitDialog();
                            DialogHelper.getMessageDialog(MIOCActivity.this, getString(R.string.wms_net_error) + statusCode);
                        }
                    }

                    );

                    currentPage=0;

                    loadData();
                    }
                }).show();
    }

    /**
     * 送货师傅点击
     *
     * @author bipolor
     */
    @OnClick(R.id.ll_mioc_sendperson)
    protected void selectSendPerson() {
        Intent intent = new Intent(MIOCActivity.this,
                WmsSelectDataActivity.class);
        intent.putExtra("dataType", DataType.logistPerson.getValue());
        intent.putExtra("dataTitle",
                getResources().getString(R.string.mio_send_person));
        startActivityForResult(intent, sendPersonRequestCode);
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
    }

    @OnClick(R.id.btn_mioc_add_barcode)
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
            Intent intent = new Intent(this, MRecordActivity.class);
            intent.putExtra("mrecordType", mrecordType.getValue());
            intent.putExtra("serNo", recordGatherBO.getSerNo());
            intent.putExtra("sendPersonId", sendPersonId);
            startActivityForResult(intent, browserRequestCode);
        }
    }

    /**
     * 选择后返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == sendPersonRequestCode && resultCode == sendPersonResultCode) {
                if (data == null) {
                    return;
                }
                this.sendPersonId = data.getExtras().getLong("dataId");
                String dataValue = data.getExtras().getString("dataValue");
                this.tvSendperson.setText(dataValue);
                super.currentPage = 0;
                super.loadData();
            }
            if (requestCode == browserRequestCode && resultCode == browserResultCode) {
                super.currentPage = 0;
                super.loadData();
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 添加条码
     */
    private void addBarCode() {
        //验证
        if (!isValidate()) {
            return;
        }

        String barCode = etBarCode.getText().toString().trim();
        //获取条码的前五位，以查找该条码所属的产品及内外机属性
        String serno = barCode.substring(0, 5);
        if (productBlo == null) {
            productBlo = new ProductBLO();
        }
        ProductBO product = productBlo.getProduct(serno);
        if (product == null) {
            //查找条码所属的产品，若不存在则提示
            ToastHelper.showToast(String.format("条形码 '%s' 不属于任何产品型号，请同步产品型号信息后，再次扫描！", barCode));
            return;
        }
        BarCodeTypes barCodeType = BarCodeTypes.in;
        if (serno.equals(product.getMinSerno())) {
            barCodeType = BarCodeTypes.in;

        } else if (serno.equals(product.getMoutSerno())) {
            barCodeType = BarCodeTypes.out;
        }
        if (mrecordBlo == null) {
            mrecordBlo = new MRecordBLO();
        }
        if (mrecordBlo.isExistBarcode(mrecordType, barCode)) {
            ToastHelper.showToast(R.string.wms_record_barcode_exist);
            return;
        }


        Date createDate = DateHelper.getCurrDate();

        MRecordBO mrecord = new MRecordBO();
        mrecord.setBarCode(barCode);
        mrecord.setServerProductId(product.getServerProductId());
        mrecord.setCreateUserId(createUserId);
        mrecord.setCreateDate(createDate);
        mrecord.setSendPersonIdId(sendPersonId);
        mrecord.setMRecordType(mrecordType);
        mrecord.setBarCodeType(barCodeType);

        if (mrecordBlo.addMRecord(mrecord)) {
            ToastHelper.showToast(R.string.wms_common_save_success);
        } else {
            DialogHelper.getConfirmDialog(this, getString(R.string.wms_common_save_failed), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            return;
        }
        currentPage = 0;
        loadData();
    }

    /**
     * 通过serno从产品集合中查找相应的产品
     *
     * @param lstProduct
     * @param serno
     * @return
     */
    private ProductBO getProductInListProduct(List<ProductBO> lstProduct, String serno) {
        if (lstProduct == null || serno == null) {
            return null;
        }
        for (ProductBO product : lstProduct) {
            if (product.getMinSerno().equals(serno) || product.getMoutSerno().equals(serno)) {
                return product;
            }
        }
        if (productBlo == null) {
            productBlo = new ProductBLO();
        }
        ProductBO productBO = productBlo.getProduct(serno);
        if (productBO != null) {
            lstProduct.add(productBO);
        }
        return productBO;
    }


    /**
     * 获取提交时的条码信息
     *
     * @param lstMRecord 记录
     */
    private StringBuffer getMBarCodeMessage(List<MRecordBO> lstMRecord) {

        List<ProductBO> lstProduct = new ArrayList<ProductBO>();

        for (MRecordBO mrecord : lstMRecord) {
            ProductBO product = new ProductBO();
            product.setServerProductId(mrecord.getServerProductId());
            //产品名
            if (lstProduct.contains(product)) {
                ProductBO productTemp = lstProduct.get(lstProduct.indexOf(product));
                if (mrecord.getBarCodeType() == BarCodeTypes.in) {
                    productTemp.setIncount(productTemp.getIncount() + 1);
                } else if (mrecord.getBarCodeType() == BarCodeTypes.out) {
                    productTemp.setOutcount(productTemp.getOutcount() + 1);
                }
            } else {
                if (mrecord.getBarCodeType() == BarCodeTypes.in) {
                    product.setIncount(1);
                } else if (mrecord.getBarCodeType() == BarCodeTypes.out) {
                    product.setOutcount(1);
                }
                lstProduct.add(product);
            }
        }

        //内外机数量不一致的产品
        List<ProductBO> lstProductDiffInOut = new ArrayList<ProductBO>();
        for (ProductBO productTemp : lstProduct) {
            if (productTemp.getIncount() != productTemp.getOutcount()) {
                if (productBlo == null) {
                    productBlo = new ProductBLO();
                }
                ProductBO product = productBlo.getProductByServerProductId(productTemp.getServerProductId());
                if (product != null) {
                    productTemp.setProductName(product.getProductName());
                }
                lstProductDiffInOut.add(productTemp);
            }
        }

        StringBuffer sbMessage = new StringBuffer();
        if (lstProductDiffInOut.size() > 0) {
            sbMessage.append(lstProductDiffInOut.size());
            sbMessage.append("个产品型号的内外机数量不一致，详细:\n");
            for (ProductBO product : lstProductDiffInOut) {
                sbMessage.append(product.getProductName());
                sbMessage.append(String.format(":内机 %d 个；外机 %d 个 \n", product.getIncount(), product.getOutcount()));
            }
        }
        return sbMessage;
    }

    /**
     * 验证输入是否有效
     *
     * @return true:有效；false:无效
     */
    private Boolean isValidate() {
        if (sendPersonId == -1) {
            ToastHelper.showToast(R.string.mio_sendperson_null);
            return false;
        }
        // 3 条形码
        String barCode = etBarCode.getText().toString().trim();
        // 3.1 未输入
        if (barCode.length() == 0) {
            ToastHelper.showToast(R.string.wms_record_barcode_null);
            return false;
        }
        return true;
    }
}
