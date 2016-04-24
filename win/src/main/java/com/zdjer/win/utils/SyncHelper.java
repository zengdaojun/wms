package com.zdjer.win.utils;

import android.util.Log;

import com.zdjer.utils.PathHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SyncHelper {

    private static final String FirstDir = "windata";

    // 数据库名（含有路径）
    public static final String LOGPATH = PathHelper.GetSDRootPath()
            + File.separator + FirstDir + File.separator + "win.log";


    /**
     * 获得相应的数据
     *
     * @param url
     * @param lstNameValue
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getResponse(String url, List<NameValuePair> lstNameValue) throws Exception {
        Log.i("URL", url);
        TxtDBHelper.setDataFilePath(LOGPATH);
        TxtDBHelper.WriteData("请求：" + url + "?" + getNameValueString(lstNameValue));
        HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(lstNameValue, HTTP.UTF_8));

        HttpResponse response = new DefaultHttpClient().execute(request);
        String responseString = null;
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND) {
            HttpEntity httpEntity = response.getEntity();
            responseString = EntityUtils.toString(httpEntity);
        }
        TxtDBHelper.WriteData("响应：" + responseString);
        return responseString;
    }

    /**
     * 获得IP
     *
     * @param server
     * @return
     * @throws Exception
     */
    public static String getIP(String server) throws Exception {
        List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
        String url = SyncAPITypes.valueSting(SyncAPITypes.getIP);
        String getIPResonse = SyncHelper.getResponse(
                url,
                lstNameValue);
        JSONObject jsonGetIP = new JSONObject(
                getIPResonse);
        JSONArray jsonDatas = jsonGetIP.getJSONArray("data");
        for (int i = 0; i < jsonDatas.length(); i++) {
            JSONObject jsonData = (JSONObject) jsonDatas.opt(i);
            String name = jsonData.getString("name");
            String ip = jsonData.getString("ip");
            if ("1".equals(server) && "服务器一".equals(name)) {
                return ip;
            }
            if ("2".equals(server) && "服务器二".equals(name)) {
                return ip;
            }
        }
        return "";
    }

    /**
     * 获得是否授权
     *
     * @param phoneNumber
     * @param deviceID
     * @param deviceType
     * @param msg
     * @return
     * @throws Exception
     */
    public static boolean isGrant(String phoneNumber,String deviceID,String deviceType,StringBuffer msg) throws Exception {
        String user = "261";
        if(user == null || user.length()==0){
            user = "261";
        }
        if(phoneNumber == null || phoneNumber.length()==0){
            phoneNumber = "0";
        }
        if(deviceID == null || deviceID.length()==0){
            deviceID = "0";
        }
        if(deviceType == null || deviceType.length()==0){
            deviceType = "0";
        }

        List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
        lstNameValue.add(new BasicNameValuePair("c", "261"));
        lstNameValue.add(new BasicNameValuePair("l", user));
        lstNameValue.add(new BasicNameValuePair("m", phoneNumber));
        lstNameValue.add(new BasicNameValuePair("d", deviceID));
        lstNameValue.add(new BasicNameValuePair("t", deviceType));
        String url = SyncAPITypes.valueSting(SyncAPITypes.vst);
        String grantResonse = SyncHelper.getResponse(
                url,
                lstNameValue);
        Log.i("grantResonse",grantResonse);
        JSONObject jsonBrant = new JSONObject(
                grantResonse);
        boolean flag = jsonBrant.getBoolean("flag");
        if (!flag) {
            msg.append(jsonBrant.getString("msg"));
        }
        return flag;
    }

    private static String getNameValueString(List<NameValuePair> lstNameValue) {
        String nameValueString = "";
        for (int i = 0; i < lstNameValue.size(); i++) {
            NameValuePair nameValue = lstNameValue.get(i);
            nameValueString = nameValueString + nameValue.getName() + "=" + nameValue.getValue();
            if (i < lstNameValue.size() - 1) {
                nameValueString = nameValueString + "&";
            }
        }
        return nameValueString;
    }

    /**
     * @author bipolor
     */
    public enum SyncAPITypes {
        /**
         * 入库
         */
        addInBarCode(1),

        /**
         * 出库
         */
        addOutBarCode(2),

        /**
         * 维护物流
         */
        operateWuLiu(3),

        /**
         * 获得经销商
         */
        getJxs(4),

        /**
         * 获得库房
         */
        getWareHouse(5),

        /**
         * 获得物流
         */
        getWuLiu(6),

        /**
         * 获得车辆
         */
        getCarNum(7),

        /**
         * 获得司机
         */
        getDriver(8),

        /**
         * 登录
         */
        login(9),

        /**
         * 删除记录
         */
        deleteBarcode(10),

        /**
         * 获得IP
         */
        getIP(11),

        /**
         * 查询出库未入库的信息
         */
        searchBarcodeIn(12),
        /**
         * 提货单查询经销商
         */
        searchTihuodan(13),
        /**
         * 查询经销商名称
         */
        searchInstallTeam(14),

        /**
         * 盘点
         */
        panDian(15),

        /**
         * 访问
         */
        vst(16);

        /**
         * 获得产品列表
         *//*
        getProductList(15),

        *//**
         * 获得工人师傅
         *//*
        getLogistPerson(16),

        *//**
         * 网点入库
         *//*
        productEnter(17),

        *//**
         * 网点出库
         *//*
        productOut(18),

        *//**
         * 网点盘点
         *//*
        productCheck(19);*/

        private static final String TOKEN = "/token/";
        private static final String ADDINBARCODE = TOKEN + "barcode_add.do";// 扫码入库
        private static final String ADDOUTBARCODE = TOKEN + "barcode_out.do";// 扫码出库
        private static final String OPERATEWULIU = TOKEN + "logistics_add.do";// 物流新增和编辑
        private static final String GETJXS = TOKEN + "supteam.do";// 获取经销商
        private static final String GETWAREHOUSE = TOKEN + "getwh.do";// 获取仓库货位
        private static final String GETWULIU = TOKEN + "logisticsteam.do";// 获取物流
        private static final String GETCARNUM = TOKEN + "logisticscar.do";// 获取车牌号
        private static final String GETDRIVER = TOKEN + "logisticsperson.do";// 获取司机
        private static final String LOGIN = TOKEN + "login.do";//登录
        private static final String DELETEBARCODE = TOKEN + "barcode_del.do";//删除记录
        private static final String GETIP = "http://mts.wiseiter.com/serverwl.do";//获得IP
        private static final String SEARCHBARCODEIN = TOKEN + "barcode_add_search.do";// 经销商入库
        private static final String SEARCHTIHUODAN = TOKEN + "tihuodan.do";// 提货单
        private static final String SEARCHINSTALLTEAM = TOKEN + "get_install_team_name.do";// 提货单
        private static final String PANDIAN = TOKEN + "barcode_pd.do"; //盘点
        private static final String VST = "http://mts.wiseiter.com/vst.do";//授权设备

        private int value;// 值

        /**
         * 构造函数
         *
         * @param value 值
         */
        SyncAPITypes(int value) {
            this.value = value;
        }

        /**
         * 获取值
         *
         * @return
         */
        public int getValue() {
            return value;
        }

        /**
         * 通过值获得记录类型枚举
         *
         * @param value 值
         * @return 记录类型枚举
         */
        public static SyncAPITypes value(int value) {
            for (SyncAPITypes syncAPIType : SyncAPITypes.values()) {
                if (syncAPIType.getValue() == value) {
                    return syncAPIType;
                }
            }
            return null;
        }

        /**
         * 获得枚举的字符串
         *
         * @param syncAPIType 同步接口类型
         * @return 枚举的字符串
         */
        public static String valueSting(SyncAPITypes syncAPIType) {
            if (syncAPIType == SyncAPITypes.addInBarCode) {
                return ADDINBARCODE;
            } else if (syncAPIType == SyncAPITypes.addOutBarCode) {
                return ADDOUTBARCODE;
            } else if (syncAPIType == SyncAPITypes.operateWuLiu) {
                return OPERATEWULIU;
            } else if (syncAPIType == SyncAPITypes.getJxs) {
                return GETJXS;
            } else if (syncAPIType == SyncAPITypes.getWareHouse) {
                return GETWAREHOUSE;
            } else if (syncAPIType == SyncAPITypes.getWuLiu) {
                return GETWULIU;
            } else if (syncAPIType == SyncAPITypes.getCarNum) {
                return GETCARNUM;
            } else if (syncAPIType == SyncAPITypes.getDriver) {
                return GETDRIVER;
            } else if (syncAPIType == SyncAPITypes.login) {
                return LOGIN;
            } else if (syncAPIType == SyncAPITypes.deleteBarcode) {
                return DELETEBARCODE;
            } else if (syncAPIType == SyncAPITypes.getIP) {
                return GETIP;
            } else if (syncAPIType == SyncAPITypes.searchBarcodeIn) {
                return SEARCHBARCODEIN;
            } else if (syncAPIType == SyncAPITypes.searchTihuodan) {
                return SEARCHTIHUODAN;
            } else if (syncAPIType == SyncAPITypes.searchInstallTeam) {
                return SEARCHINSTALLTEAM;
            } else if (syncAPIType == SyncAPITypes.panDian){
                return PANDIAN;
            } else if (syncAPIType == SyncAPITypes.vst){
                return VST;
            }
            /*else if (syncAPIType == SyncAPITypes.getProductList) {
                return GETPRODUCTLIST;
            } else if (syncAPIType == SyncAPITypes.getLogistPerson) {
                return GETLOGISTPERSON;
            } else if (syncAPIType == SyncAPITypes.productEnter) {
                return PRODUCTENTER;
            } else if (syncAPIType == SyncAPITypes.productOut) {
                return PRODUCTOUT;
            } else if (syncAPIType == SyncAPITypes.productCheck) {
                return PRODUCTCHECK;
            }*/
            return null;
        }
    }
}
