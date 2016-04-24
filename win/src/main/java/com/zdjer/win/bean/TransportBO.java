package com.zdjer.win.bean;

import android.annotation.SuppressLint;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 实体：运输信息
 */
@SuppressLint("SimpleDateFormat")
public class TransportBO implements Comparable<TransportBO> {
	private long transportId=0;//运输信息Id
	private long tranId = 0;// 运输信息Id(服务端)
	private long wuLiuId=0;//物流Id	
	private String wuLiu="";//物流
	private long carNumId=0;//车牌号Id
	private String carNum = "";// 车牌号
	private long driverId=0;//司机Id
	private String driver = "";// 司机
	private Date sendDate = null;// 发货时间
	private String sendDataString="";//发货日期字符串
	private String sendTimeString="";//发货时间字符串

	/**
	 * 构造函数
	 */
	public TransportBO() {
		
	}
	
	/**
	 * 获得运输信息Id
	 * @return
	 */
	public long getTransportId() {
		return transportId;
	}


	/**
	 * 设置运输信息Id
	 * @param transportId
	 */
	public void setTransportId(long transportId) {
		this.transportId = transportId;
	}



	/**
	 * 获取运输Id(服务端)
	 * 
	 * @return 运输信息Id
	 */
	public long getTranId() {
		return tranId;
	}

	/**
	 * 设置运输信息Id(服务端)
	 * 
	 * @param tranId
	 *            运输信息Id
	 */
	public void setTranId(long tranId) {
		this.tranId = tranId;
	}
	
	/**
	 * 获得物流Id
	 * @return
	 */
	public long getWuLiuId() {
		return wuLiuId;
	}

	/**
	 * 设置物流Id
	 * @param wuLiuId
	 */
	public void setWuLiuId(long wuLiuId) {
		this.wuLiuId = wuLiuId;
	}

	/**
	 * 获得物流
	 * @return
	 */
	public String getWuLiu() {
		return wuLiu;
	}

	/**
	 * 设置物流
	 * @param wuLiu
	 */
	public void setWuLiu(String wuLiu) {
		this.wuLiu = wuLiu;
	}
	
	/**
	 * 获得车牌号Id
	 * @return
	 */
	public long getCarNumId() {
		return carNumId;
	}

	/**
	 * 设置车牌号Id
	 * @param carNumId
	 */
	public void setCarNumId(long carNumId) {
		this.carNumId = carNumId;
	}

	/**
	 * 获取车牌号
	 * 
	 * @return
	 */
	public String getCarNum() {
		return carNum;
	}

	/**
	 * 设置车牌号
	 * 
	 * @param carNum
	 */
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	/**
	 * 获得司机Id
	 * @return
	 */
	public long getDriverId() {
		return driverId;
	}

	/**
	 * 设置司机Id
	 * @param driverId
	 */
	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	/**
	 * 获取司机
	 * 
	 * @return
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * 设置司机
	 * 
	 * @param driver
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * 获得发送时间
	 * @return
	 */
	public Date getSendDate() {		
		return sendDate;
	}

	/**
	 * 设置发送时间
	 * @param sendDate
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	
	/**
	 * 获取发送日期字符串
	 * @return
	 */
	public String getSendDataString() {
		if(sendDate!=null){
			SimpleDateFormat sdfCurrDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			sendDataString=sdfCurrDateFormat.format(sendDate);
		}
		return sendDataString;
	}

	/**
	 * 获得发送时间字符串
	 * @return
	 */
	public String getSendTimeString() {
		if(sendDate!=null){
			SimpleDateFormat sdfCurrDateFormat = new SimpleDateFormat(
					"HH:mm");
			sendTimeString=sdfCurrDateFormat.format(sendDate);
		}
		return sendTimeString;
	}

	/**
	 * 获得运输显示信息
	 * @return
	 */
	public String getTransportInfo(){
		StringBuffer sbTransportInfo=new StringBuffer("");
		if(wuLiu.length()!=0){
			sbTransportInfo.append(wuLiu);
		}
		if(carNum.length()!=0){
			sbTransportInfo.append(","+carNum);
		}
		if(driver.length()!=0){
			sbTransportInfo.append(","+driver);
		}
		if(sendTimeString.length()!=0){
			sbTransportInfo.append("("+sendTimeString+")");
		}
		return sbTransportInfo.toString();
	}

	public int compareTo(TransportBO transport) {
		if(this.getTranId()> transport.getTranId()){
			return 1;
		}else if(this.getTranId() == transport.getTranId()){
			return 0;
		}else{
			return -1;
		}

	}


	/**
	 * 运输信息存储数据定义
	 * 
	 * @author bipolor
	 * 
	 */
	public static abstract class TransportEntry implements BaseColumns {
		public static final String COLUMN_NAME_NULLABLE = "NULL";
		public static final String TABLE_NAME = "tb_transport";
		public static final String COLUMN_NAME_TRANID="tranid";//运输信息Id(服务端)
		public static final String COLUMN_NAME_WULIUID="wuliuid";//物流Id
		public static final String COLUMN_NAME_WULIU="wuliu";//物流
		public static final String COLUMN_NAME_CARNUMID="carnumid";//车牌号Id
		public static final String COLUMN_NAME_CARNUM="carnum";//车牌号
		public static final String COLUMN_NAME_DRIVERID="driverid";//司机Id
		public static final String COLUMN_NAME_DRIVER="driver";//司机
		public static final String COLUMN_NAME_SENDDATE="senddate";//发货时间

	}
}
