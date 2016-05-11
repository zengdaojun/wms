package com.zdjer.win.utils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zdjer.utils.http.AsyncHttpClientHelper;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.bean.RecordType;
import com.zdjer.win.bean.TransportBO;

/**
 * Created by zdj on 4/21/16.
 */
public class WinNetApiHelper {

    private static final String TOKEN = "/token/";

    //barcode_add_search.do

    /**
     * 获得提货单信息
     * @param ip IP
     * @param token TOKEN
     * @param thdNum 提货单号
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void searchTHDNum(String ip, String token,String thdNum,
                                        JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "barcode_add_search.do";
        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("tidanhao",thdNum);
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 获得提货单信息
     * @param ip IP
     * @param token TOKEN
     * @param thdNum 提货单号
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void searchJXSByTHDNum(String ip, String token,String thdNum,
                                        JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "tihuodan.do";
        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("tidanhao",thdNum);
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 上传仓库入库记录
     * @param ip IP
     * @param token TOKEN
     * @param recordBO RecordBO
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void uploadInRecord(String ip, String token,RecordBO recordBO,
                                      JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "barcode_add.do";

        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("barcode",recordBO.getBarCode());
        requestParams.put("shelfId",String.valueOf(recordBO.getWarehouseId()));
        requestParams.put("shelfName",recordBO.getWarehouse());
        requestParams.put("intype",String.valueOf(recordBO.getInType().getValue()));
        requestParams.put("installTeamId",String.valueOf(recordBO.getJxsNumId()));
        requestParams.put("installTeamName",recordBO.getJxsNum());
        requestParams.put("tidanhao",recordBO.getThdNum());
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 上传仓库入库记录
     * @param ip IP
     * @param token TOKEN
     * @param recordBO RecordBO
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void uploadOutRecord(String ip, String token,RecordBO recordBO,
                                       JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "barcode_out.do";

        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("barcode",recordBO.getBarCode());
        requestParams.put("shelfId",String.valueOf(recordBO.getWarehouseId()));
        requestParams.put("shelfName",recordBO.getWarehouse());
        requestParams.put("outype",String.valueOf(recordBO.getOutType().getValue()));
        requestParams.put("dbno",recordBO.getDbdNum());
        requestParams.put("installTeamId",String.valueOf(recordBO.getJxsNumId()));
        requestParams.put("installTeamName",recordBO.getJxsNum());
        requestParams.put("tidanhao",recordBO.getThdNum());
        requestParams.put("logisticsId",String.valueOf(recordBO.getTranId()));
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 上传仓库盘点记录
     * @param ip IP
     * @param token TOKEN
     * @param barcode 条码
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void uploadCheckRecord(String ip, String token,String barcode,
                                       JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "barcode_pd.do";

        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("barcode",barcode);
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 上传仓库盘点记录
     * @param ip IP
     * @param token TOKEN
     * @param recordBO 记录
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void deleteRecord(String ip, String token,RecordBO recordBO,
                                         JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "barcode_del.do";
        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("barcode",recordBO.getBarCode());
        requestParams.put("shelfId",String.valueOf(recordBO.getWarehouseId()));
        requestParams.put("shelfName",recordBO.getWarehouse());
        String type="";
        if (recordBO.getRecordType() == RecordType.in) {
            type = "0";
            requestParams.put("intype", String.valueOf(recordBO.getInType().getValue()));
        } else if (recordBO.getRecordType() == RecordType.out) {
            type = "1";
            requestParams.put("outype", String.valueOf(recordBO.getOutType().getValue()));
        } else if (recordBO.getRecordType() == RecordType.check) {
            type = "2";
        }
        requestParams.put("installTeamId", String.valueOf(recordBO.getJxsNumId()));
        requestParams.put("installTeamName", recordBO.getJxsNum());
        requestParams.put("tidanhao", recordBO.getThdNum());
        requestParams.put("type", type);
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 收货确认
     * @param ip
     * @param token
     * @param thdNum
     * @param jsonHttpResponseHandler
     */
    public static void shconfirm(String ip, String token,String thdNum,
                                    JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "receive_tidanhao.do";
        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("tidanhao",thdNum);
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 上传供应商信息
     * @param ip
     * @param token
     * @param transportBO
     * @param jsonHttpResponseHandler
     */
    public static void uploadTransport(String ip,String token,TransportBO transportBO,
                                       JsonHttpResponseHandler jsonHttpResponseHandler){
        String url=ip+TOKEN + "logistics_add.do";

        String id="";
        if(transportBO.getTranId()!=0){
            id=String.valueOf(transportBO.getTranId());
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("token", token);
        requestParams.put("id", id);
        requestParams.put("carInfoId", String
                .valueOf(transportBO.getCarNumId()));
        requestParams.put("carInfo", transportBO.getCarNum());
        requestParams.put("logisticsTeamId", String
                .valueOf(transportBO.getWuLiuId()));
        requestParams.put("logisticsPersonId", String
                .valueOf(transportBO.getDriverId()));
        requestParams.put("logisticsPerson", transportBO.getDriver());
        requestParams.put("sentDate", transportBO
                .getSendDataString());
        requestParams.put("sentTime", transportBO
                .getSendTimeString());
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }
}
