package com.zdjer.min.utils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zdjer.min.bean.MRecordType;
import com.zdjer.utils.http.AsyncHttpClientHelper;
import com.zdjer.wms.utils.PathHelper;

import java.io.File;

public class MinNetApiHelper {

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
    private static final String GETIP = "http://mts.wiseiter.com/serverwd.do";//获得IP
    private static final String SEARCHBARCODEIN = TOKEN + "barcode_add_search.do";// 经销商入库
    private static final String SEARCHTIHUODAN = TOKEN + "tihuodan.do";// 提货单
    private static final String SEARCHINSTALLTEAM = TOKEN + "get_install_team_name.do";// 提货单
    private static final String GETPRODUCTLIST = TOKEN + "prod_list.do";//获得产品列表
    private static final String GETLOGISTPERSON = TOKEN + "logist_person.do";//获得工人师傅
    private static final String PRODUCTENTER = TOKEN + "prod_enter.do";//入库提交
    private static final String PRODUCTOUT = TOKEN + "prod_out.do";//出库提交
    private static final String PRODUCTCHECK = TOKEN + "prod_check.do";//盘点提交



    private static final String FirstDir = "windata";

    // 数据库名（含有路径）
    public static final String LOGPATH = PathHelper.GetSDRootPath()
            + File.separator + FirstDir + File.separator + "win.log";

    /**
     * 上传网点入库记录
     * @param ip IP
     * @param token TOKEN
     * @param sendPersonId 送货师傅Id
     * @param barcode 条码组成的字符串
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void uploadMRecord(MRecordType mrecordType, String ip, String token,
                                       long sendPersonId,String barcode,
                                       JsonHttpResponseHandler jsonHttpResponseHandler){


        if(mrecordType == MRecordType.in){
            uploadInMRecord(ip,token,sendPersonId,barcode,jsonHttpResponseHandler);
        }else if(mrecordType == MRecordType.out){
            uploadOutMRecord(ip, token, sendPersonId, barcode, jsonHttpResponseHandler);
        }else if(mrecordType == MRecordType.check){
            uploadCheckMRecord(ip, token, barcode, jsonHttpResponseHandler);
        }
    }

    /**
     * 上传网点入库记录
     * @param ip IP
     * @param token TOKEN
     * @param sendPersonId 送货师傅Id
     * @param barcode 条码组成的字符串
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    private static void uploadInMRecord(String ip, String token,
                                       long sendPersonId,String barcode,
                                       JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "prod_enter.do";
        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("personId",String.valueOf(sendPersonId));
        requestParams.put("barcode",barcode);
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 上传网点出库记录
     * @param ip IP
     * @param token TOKEN
     * @param sendPersonId 送货师傅Id
     * @param barcode 条码组成的字符串
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    private static void uploadOutMRecord(String ip, String token,
                                        long sendPersonId,String barcode,
                                        JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "prod_out.do";
        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("personId",String.valueOf(sendPersonId));
        requestParams.put("barcode",barcode);
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }

    /**
     * 上传网点盘点记录
     * @param ip IP
     * @param token TOKEN
     * @param barcode 条码组成的字符串
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    private static void uploadCheckMRecord(String ip, String token,
                                          String barcode,
                                          JsonHttpResponseHandler jsonHttpResponseHandler){

        String url=ip+TOKEN + "prod_check.do";
        RequestParams requestParams = new RequestParams();
        requestParams.put("token",token);
        requestParams.put("barcode",barcode);
        AsyncHttpClientHelper.get(url, requestParams, jsonHttpResponseHandler);
    }





    /*List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
			lstNameValue.add(new BasicNameValuePair("type","new"));
			lstNameValue.add(new BasicNameValuePair("user",uid));
			lstNameValue.add(new BasicNameValuePair("pwd",pwd));

			// 登录输入响应
			String url=ip+SyncAPITypes.valueSting(SyncAPITypes.login);
			String loginResonse = SyncHelper.getResponse(url, lstNameValue);
			JSONObject jsonLogin = new JSONObject(loginResonse);
			boolean flag= jsonLogin.getBoolean("flag");
			if(flag){
				//1.1.1 在线登录成功
				String token = jsonLogin.getString("token");
				Log.i("token", "token=" + token);
				//判断账号在本地是否存在，若存在则修改，若不存在，则添加
				if(isExist(uid)){
					//存在，则修改，修改成功，则获取用户信息
					if(update(uid,MD5Helper.ConvertToMD5(pwd),token)){
						user=getUser(uid, MD5Helper.ConvertToMD5(pwd));
					}
				}else{
					//不存在，则添加，添加成功，则获取用户信息
					UserBO userBo = new UserBO();
					userBo.setUid(uid);
					userBo.setPwd(MD5Helper.ConvertToMD5(pwd));
					userBo.setToken(token);
					userBo.setCreateDate(DateHelper.getCurrDate());
					userBo.setEditDate(DateHelper.getCurrDate());
					if(insert(userBo)){
						user=getUser(uid, MD5Helper.ConvertToMD5(pwd));
					}
				}
			}else{
				user=getUser(uid, MD5Helper.ConvertToMD5(pwd));
				if(user== null || user.getToken().length()==0){
					user=null;
				}
			}*/

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
        /*String user = "261";
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
        return flag;*/
        return false;
    }

   /* private static String getNameValueString(List<NameValuePair> lstNameValue) {
        String nameValueString = "";
        for (int i = 0; i < lstNameValue.size(); i++) {
            NameValuePair nameValue = lstNameValue.get(i);
            nameValueString = nameValueString + nameValue.getName() + "=" + nameValue.getValue();
            if (i < lstNameValue.size() - 1) {
                nameValueString = nameValueString + "&";
            }
        }
        return nameValueString;
    }*/

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
            } else if (syncAPIType == SyncAPITypes.searchBarcodeIn) {
                return SEARCHBARCODEIN;
            } else if (syncAPIType == SyncAPITypes.searchTihuodan) {
                return SEARCHTIHUODAN;
            } else if (syncAPIType == SyncAPITypes.searchInstallTeam) {
                return SEARCHINSTALLTEAM;
            } /*else if (syncAPIType == SyncAPITypes.panDian){
                return PANDIAN;
            } else if (syncAPIType == SyncAPITypes.vst){
                return VST;
            }*/
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
