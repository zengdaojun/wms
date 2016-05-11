package com.zdjer.min.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.min.R;
import com.zdjer.utils.PathHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.update.UpdateHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.wms.bean.DataItemBO;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.bean.ProductBO;
import com.zdjer.wms.bean.core.OptionTypes;
import com.zdjer.wms.model.DataItemBLO;
import com.zdjer.wms.model.OptionBLO;
import com.zdjer.wms.model.ProductBLO;
import com.zdjer.wms.utils.WmsNetApiHelper;
import com.zdjer.wms.view.widget.WmsSetDeviceNumActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


public class SetActivity extends BaseActivity {
    @Bind(R.id.setting_tv_devicenum)
    TextView tvDeviceNum = null;

    private ProductBLO productBlo = null;
    private DataItemBLO dataItemBlo = null;
    private OptionBLO optionBlo = new OptionBLO();//选项操作类
    private int setDevivenumRequestCode = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set;
    }

    /**
     * 初始化数据
     */
    public void initView() {
        //获得设备编号
        String deviceNum = optionBlo.getOptionValue(OptionTypes.DeviceNum);
        tvDeviceNum.setText(deviceNum);
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.tv_set_back,
            R.id.ll_set_devicenum,
            R.id.ll_set_sync_basedata,
            R.id.ll_set_update})
    public void onClick(View v) {

        int viewId = v.getId();
        switch (viewId) {
            case R.id.tv_set_back: {
                finish();
                overridePendingTransition(R.anim.in_from_left,
                        R.anim.out_to_right);
                break;
            }
            case R.id.ll_set_devicenum: {
                Intent intent = new Intent(this,
                        WmsSetDeviceNumActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.in_from_right1,
                        R.anim.out_to_left1);
                break;
            }
            case R.id.ll_set_sync_basedata: {
                syncBaseData();
                break;
            }
            case R.id.ll_set_update:{
                String url = "http://mts.wiseiter.com/upload/version/MinAppVersion.xml";
                String saveFilePath = PathHelper.GetSDRootPath()
                        + File.separator
                        + "mindata"
                        + File.separator + "download" + File.separator;
                new UpdateHelper(this, true,saveFilePath).checkUpdate(url);
                break;
            }
        }
    }

    /**
     * 处理产品
     *
     * @param response
     * @return
     */
    private Boolean handleProduct(JSONObject response, StringBuffer stringBuffer) {
        List<ProductBO> lstProduct = new ArrayList<ProductBO>();
        try {
            if (response != null) {
                boolean flag = response.getBoolean("flag");
                if (flag) {
                    ProductBO product = null;
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = (JSONObject) jsonArray.opt(i);
                        long serverProductId = jsonData.getLong("id");
                        String productName = jsonData.getString("product_name");
                        String minModel = jsonData.getString("in_model");
                        String minSerno = jsonData.getString("in_serno");
                        String moutModel = jsonData.getString("out_model");
                        String moutSerno = jsonData.getString("out_serno");
                        product = new ProductBO();
                        product.setServerProductId(serverProductId);
                        product.setProductName(productName);
                        product.setMinModel(minModel);
                        product.setMinSerno(minSerno);
                        product.setMoutModel(moutModel);
                        product.setMoutSerno(moutSerno);
                        lstProduct.add(product);
                    }
                    if (productBlo == null) {
                        productBlo = new ProductBLO();
                    }
                    if (!productBlo.addProduct(lstProduct)) {
                        stringBuffer.append(getString(R.string.wms_common_local_save_error));
                        return false;
                    }
                } else {
                    stringBuffer.append(getString(R.string.wms_common_download_error));
                    return false;
                }
            }
        } catch (Exception e) {
            stringBuffer.append(getString(R.string.wms_common_exception));
            return false;
        }
        return true;
    }

    /**
     * 处理送货师傅
     *
     * @param response
     * @return
     */
    private boolean handleLogistPerson(JSONObject response, StringBuffer stringBuffer) {
        try {
            if (response != null) {
                boolean flag = response.getBoolean("flag");
                if (flag) {
                    DataItemBO dataItem = null;

                    JSONArray jsonArray = response.getJSONArray("data");
                    List<DataItemBO> lstDataItem = new ArrayList<DataItemBO>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = (JSONObject) jsonArray.opt(i);
                        long id = jsonData.getLong("id");
                        String value = "";
                        if (jsonData.has("text")) {
                            value = jsonData.getString("text");
                        } else if (jsonData.has("name")) {
                            value = jsonData.getString("name");
                        }
                        long parentId = 0;
                        if (jsonData.has("installTeamId")) {
                            parentId = jsonData.getLong("installTeamId");
                        }
                        dataItem = new DataItemBO();
                        dataItem.setDataId(id);
                        dataItem.setDataType(DataType.logistPerson);
                        dataItem.setDataValue(value);
                        dataItem.setParentId(parentId);
                        lstDataItem.add(dataItem);
                    }
                    if (dataItemBlo == null) {
                        dataItemBlo = new DataItemBLO();
                    }
                    if (!dataItemBlo.addDataItems(lstDataItem)) {
                        stringBuffer.append(getString(R.string.wms_common_local_save_error));
                        return false;
                    }
                } else {
                    stringBuffer.append(getString(R.string.wms_common_download_error));
                    return false;
                }
            }

        } catch (Exception e) {
            stringBuffer.append(getString(R.string.wms_common_exception));
            return false;
        }
        return true;
    }

    /**
     * 同步基础数据
     */
    private void syncBaseData() {

        showWaitDialog(R.string.wms_common_downloading);
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
        //下载产品
        WmsNetApiHelper.downProduct(ip, token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                StringBuffer stringBuffer = new StringBuffer("");
                if (!handleProduct(response, stringBuffer)) {
                    hideWaitDialog();
                    ToastHelper.showToast(stringBuffer.toString());
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

        //下载送货师傅
        WmsNetApiHelper.downLogistPerson(ip, token, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                StringBuffer stringBuffer = new StringBuffer("");
                if (!handleLogistPerson(response, stringBuffer)) {
                    hideWaitDialog();
                    ToastHelper.showToast(stringBuffer.toString());
                    return;
                } else {
                    hideWaitDialog();
                    ToastHelper.showToast(R.string.common_download_ok);
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

        /*try {
            final int NOALLOW = 1;//不允许下载
            final int NO_NET = 2;//无网络
            final int HANDLEFAILED = 3;//处理下载数据失败
            final int NET_ERROR = 4;//网络错误
            final int SYSTEMEXCEPTION = 5;//系统异常
            final int DOWNLOADOK = 6;//下载成功


            showWaitDialog("正在下载…");

            final Handler handler = new Handler() {

                @Override
                //当有消息发送出来的时候就执行Handler的这个方法
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    hideWaitDialog();
                    switch (msg.what) {
                        case NOALLOW: {
                            ToastHelper.showToast(R.string.wms_common_sync_noallowsync);
                            break;
                        }
                        case NO_NET:{
                            ToastHelper.showToast(R.string.wms_common_no_net);
                            break;
                        }
                        case HANDLEFAILED:{
                            ToastHelper.showToast(R.string.wms_common_handle_error);
                            break;
                        }
                        case NET_ERROR:{
                            String netError = getString(R.string.wms_net_error);
                            ToastHelper.showToast(netError + msg.obj);
                            break;
                        }
                        case SYSTEMEXCEPTION: {
                            ToastHelper.showToast(R.string.wms_common_exception);
                            break;
                        }
                        case DOWNLOADOK: {
                            ToastHelper.showToast(R.string.common_download_ok);
                            break;
                        }
                    }
                }
            };

            new Thread() {
                public void run() {
                    final Message msg = new Message();
                    try {
                        final String token = SPHelper.get("currToken", "");
                        String ip = SPHelper.get("ip", "");
                        if (!StringHelper.isEmpty(ip)) {
                            if (DeviceHelper.hasInternet()) {
                                //下载产品
                                WmsNetApiHelper.downProduct(ip, token, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        if (!handleProduct(response)) {
                                            msg.what = HANDLEFAILED;
                                            handler.sendMessage(msg);
                                        }
                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        msg.what = NET_ERROR;
                                        msg.obj = statusCode;
                                    }
                                });

                                //下载送货师傅
                                WmsNetApiHelper.downLogistPerson(ip, token, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        if (!handleProduct(response)) {
                                            msg.what = HANDLEFAILED;
                                            handler.sendMessage(msg);
                                        }
                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        msg.what = NET_ERROR;
                                        msg.obj = statusCode;
                                    }
                                });
                            } else {
                                msg.what = HANDLEFAILED;
                                handler.sendMessage(msg);
                            }
                        } else {
                            msg.what = NO_NET;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        msg.what = SYSTEMEXCEPTION;
                        handler.sendMessage(msg);
                    }
                }
            }.start();
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }*/
    }

    /**
     * 返回结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == setDevivenumRequestCode) {
            String deviceNum = optionBlo.getOptionValue(OptionTypes.DeviceNum);// 获取设备信息
            this.tvDeviceNum.setText(deviceNum);
        }
    }
}
