package com.zdjer.win.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.zdjer.utils.DateHelper;
import com.zdjer.win.bean.TransportBO;
import com.zdjer.win.bean.TransportBO.TransportEntry;
import com.zdjer.win.utils.SyncHelper;
import com.zdjer.win.utils.SyncHelper.SyncAPITypes;
import com.zdjer.win.utils.WinDBManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 业务逻辑：运输信息
 * 
 * @author bipolor
 * 
 */
public class TransportBLO {

	/**
	 * 构造函数
	 */
	public TransportBLO() {
		
	}

	/**
	 * 添加运输信息
	 * 
	 * @return 添加成功，返回true；反之，返回false！
	 * @throws Exception
	 */
	public Boolean addTransport(TransportBO transport) throws Exception {
		try {
			SQLiteDatabase db = WinDBManager.getDatabase();

			ContentValues values = new ContentValues();
			values.put(TransportEntry.COLUMN_NAME_TRANID, transport.getTranId());
			values.put(TransportEntry.COLUMN_NAME_WULIUID, transport.getWuLiuId());
			values.put(TransportEntry.COLUMN_NAME_WULIU, transport.getWuLiu());
			values.put(TransportEntry.COLUMN_NAME_CARNUMID, transport.getCarNumId());
			values.put(TransportEntry.COLUMN_NAME_CARNUM, transport.getCarNum());
			values.put(TransportEntry.COLUMN_NAME_DRIVERID, transport.getDriverId());
			values.put(TransportEntry.COLUMN_NAME_DRIVER, transport.getDriver());
			values.put(TransportEntry.COLUMN_NAME_SENDDATE,
					DateHelper.getDateString(transport.getSendDate()));
			long newRowId = db.insert(TransportEntry.TABLE_NAME, null, values);
			db.close();
			return newRowId != -1;
		} catch (Exception e) {
			Log.i("UserDAO insert", "增加异常" + e.getMessage());
		}
		return false;
	}
	
	/**
	 * 同步物流信息
	 * @param transport
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	public Boolean syncTransport(String ip,TransportBO transport,String token,String msg) throws Exception {

		// 1.1 允许联网(判断输入的账号和密码是否正确)
		List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();		
		String id="";
		if(transport.getTranId()!=0){
			id=String.valueOf(transport.getTranId());
		}
		lstNameValue.add(new BasicNameValuePair("token", token));
		lstNameValue.add(new BasicNameValuePair("id", id));
		lstNameValue.add(new BasicNameValuePair("carInfoId", String
				.valueOf(transport.getCarNumId())));
		lstNameValue.add(new BasicNameValuePair("carInfo", transport.getCarNum()));		
		lstNameValue.add(new BasicNameValuePair("logisticsTeamId", String
				.valueOf(transport.getWuLiuId())));
		lstNameValue.add(new BasicNameValuePair("logisticsPersonId", String
				.valueOf(transport.getDriverId())));
		lstNameValue.add(new BasicNameValuePair("logisticsPerson", transport.getDriver()));
		lstNameValue.add(new BasicNameValuePair("sentDate", transport
				.getSendDataString()));
		lstNameValue.add(new BasicNameValuePair("sentTime", transport
				.getSendTimeString()));

		String url=ip+SyncAPITypes.valueSting(SyncAPITypes.operateWuLiu);
		String operateWuLiuResonse = SyncHelper.getResponse(
				url,
				lstNameValue);
		JSONObject jsonOperateWuLiu = new JSONObject(operateWuLiuResonse);
		boolean flag= jsonOperateWuLiu.getBoolean("flag");
		if(flag){
			long tranId = jsonOperateWuLiu.getLong("msg");
			transport.setTranId(tranId);
		}else{
			msg=jsonOperateWuLiu.getString("msg");
		}
		return flag;
	}
	
	/**
	 * 编辑运输信息
	 * @param transport
	 * @return
	 */
	public Boolean editTransport(TransportBO transport) {
		SQLiteDatabase db = WinDBManager.getDatabase();
		ContentValues values = new ContentValues();
		
		values.put(TransportEntry.COLUMN_NAME_TRANID, transport.getTranId());
		values.put(TransportEntry.COLUMN_NAME_WULIUID, transport.getWuLiuId());
		values.put(TransportEntry.COLUMN_NAME_WULIU, transport.getWuLiu());
		values.put(TransportEntry.COLUMN_NAME_CARNUM, transport.getCarNumId());
		values.put(TransportEntry.COLUMN_NAME_CARNUM, transport.getCarNum());
		values.put(TransportEntry.COLUMN_NAME_DRIVERID, transport.getDriverId());
		values.put(TransportEntry.COLUMN_NAME_DRIVER, transport.getDriver());
		values.put(TransportEntry.COLUMN_NAME_SENDDATE,
				DateHelper.getDateString(transport.getSendDate()));

		String selection = BaseColumns._ID + " = ?";
		String[] selectionArgs = { String.valueOf(transport.getTransportId()) };
		int count = db.update(TransportEntry.TABLE_NAME, values, selection,
				selectionArgs);
		db.close();
		Log.e("TransportBLO", "更新的行数为：" + count);
		return count == 1;
	}
	
	/**
	 * 获得查询项目
	 * 
	 * @return 查询项目
	 */
	private String[] getProjection() {
		String[] projection = { BaseColumns._ID,
				TransportEntry.COLUMN_NAME_TRANID,
				TransportEntry.COLUMN_NAME_WULIUID,
				TransportEntry.COLUMN_NAME_WULIU,
				TransportEntry.COLUMN_NAME_CARNUMID,
				TransportEntry.COLUMN_NAME_CARNUM,
				TransportEntry.COLUMN_NAME_DRIVERID, 
				TransportEntry.COLUMN_NAME_DRIVER,
				TransportEntry.COLUMN_NAME_SENDDATE};
		return projection;
	}

	/**
	 * 通过游标获得运输信息
	 * 
	 * @param cursor
	 *            游标
	 * @return 记录
	 * @throws ParseException
	 */
	public TransportBO getTransport(Cursor cursor) throws ParseException {
		// 获取值
		long transportId = cursor.getLong(cursor
				.getColumnIndexOrThrow(BaseColumns._ID));
		long tranId = cursor.getLong(cursor
				.getColumnIndexOrThrow(TransportEntry.COLUMN_NAME_TRANID));
		long wuLiuId = cursor.getLong(cursor
				.getColumnIndexOrThrow(TransportEntry.COLUMN_NAME_WULIUID));
		String wuLiu = cursor.getString(cursor
				.getColumnIndexOrThrow(TransportEntry.COLUMN_NAME_WULIU));
		long carNumId = cursor.getLong(cursor
				.getColumnIndexOrThrow(TransportEntry.COLUMN_NAME_CARNUMID));
		String carNum = cursor.getString(cursor
				.getColumnIndexOrThrow(TransportEntry.COLUMN_NAME_CARNUM));
		long driverId = cursor.getLong(cursor
				.getColumnIndexOrThrow(TransportEntry.COLUMN_NAME_DRIVERID));
		String driver = cursor.getString(cursor
				.getColumnIndexOrThrow(TransportEntry.COLUMN_NAME_DRIVER));
		String driveDateString = cursor.getString(cursor
				.getColumnIndexOrThrow(TransportEntry.COLUMN_NAME_SENDDATE));

		TransportBO transport = new TransportBO();
		transport.setTransportId(transportId);
		transport.setTranId(tranId);
		transport.setWuLiuId(wuLiuId);
		transport.setWuLiu(wuLiu);
		transport.setCarNumId(carNumId);
		transport.setCarNum(carNum);
		transport.setDriverId(driverId);
		transport.setDriver(driver);
		if (driveDateString != null) {
			transport.setSendDate(DateHelper.getDate(driveDateString));
		}
		return transport;
	}
	
	/**
	 * 通过模糊查询获得运输信息的总数量
	 * @param likeData
	 * @return
	 */
	public int getTransportsTotalCountLike(String likeData){
		// 1 获得数据库
		SQLiteDatabase db = WinDBManager.getDatabase();

		// 2 设置查询参数
		String sql = "SELECT COUNT(" + BaseColumns._ID + ") FROM "
				+ TransportEntry.TABLE_NAME + " WHERE "
				+ TransportEntry.COLUMN_NAME_WULIU + " like ? OR "
				+ TransportEntry.COLUMN_NAME_CARNUM + " like ? OR "
				+ TransportEntry.COLUMN_NAME_DRIVER + " like ?";

		String[] selectionArgs = new String[] {
				"%" + likeData + "%",
				"%" + likeData + "%", 
				"%" + likeData + "%"};

		// 3 查询
		Cursor cursor = db.rawQuery(sql, selectionArgs);

		// 4 获得查询结果
		int count = 0;
		for (cursor.moveToFirst(); !cursor.isAfterLast();) {
			count = cursor.getInt(0);
			break;
		}
		cursor.close();
		db.close();
		return count+1;
	}
	
	/**
	 * 通过模糊查询获得运输信息的分页数据
	 * @param likeData 关键字
	 * @param currentPage 当前页
	 * @param pageSize 一页的数据行数
	 * @return 分页数据集合
	 * @throws ParseException 
	 */
	public List<TransportBO> getTransportsLike(String likeData, int currentPage,
			int pageSize) throws ParseException {
		// 1 获得数据库
		SQLiteDatabase db = WinDBManager.getDatabase();

		// 2 设置查询参数
		String[] projection = getProjection();

		String selection = TransportEntry.COLUMN_NAME_WULIU + " like ? OR "
				+ TransportEntry.COLUMN_NAME_CARNUM + " like ? OR "
				+ TransportEntry.COLUMN_NAME_DRIVER + " like ?";

		String sortOrder = TransportEntry.COLUMN_NAME_WULIU + "  ASC LIMIT ?,?";

		pageSize-=1;

		String[] selectionArgs = new String[] { "%" + likeData + "%",
				"%" + likeData + "%", "%" + likeData + "%",
				String.valueOf((currentPage - 1) * pageSize),
				String.valueOf(pageSize) };

		// 3 查询
		Cursor cursor = db.query(TransportEntry.TABLE_NAME, projection,
				selection, selectionArgs, null, null, sortOrder);

		// 4 获得查询结果
		List<TransportBO> lstTransport = new ArrayList<TransportBO>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			// 获取值
			lstTransport.add(getTransport(cursor));
		}
		TransportBO transportNone = new TransportBO();
		transportNone.setTranId(0);
		transportNone.setWuLiu("无");
		lstTransport.add(transportNone);

		Collections.sort(lstTransport);
		Collections.sort(lstTransport);
		cursor.close();
		db.close();
		return lstTransport;
	}

	/**
	 * 通过运输信息Id获得运输信息
	 * @param transportId 运输信息Id
	 * @return 运输信息对象
	 * @throws ParseException
	 */
	public TransportBO getTransportByTransportId(long transportId) throws ParseException{
		if(transportId==0){
			return null;
		}
		// 1 获得数据库
		SQLiteDatabase db = WinDBManager.getDatabase();

		// 2 设置查询参数
		String[] projection = getProjection();

		String selection = TransportEntry._ID + " =?";

		String[] selectionArgs = new String[] { String.valueOf(transportId)};

		// 3 查询
		Cursor cursor = db.query(TransportEntry.TABLE_NAME, projection,
				selection, selectionArgs, null, null, null);

		// 4 获得查询结果
		TransportBO transport=null;
		for (cursor.moveToFirst(); !cursor.isAfterLast();) {
			// 获取值
			transport= getTransport(cursor);
			break;
		}
		cursor.close();
		db.close();
		return transport;
	}
}

	