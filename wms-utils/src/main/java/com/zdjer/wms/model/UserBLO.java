package com.zdjer.wms.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.zdjer.wms.bean.UserBO;
import com.zdjer.wms.bean.UserBO.UserEntry;
import com.zdjer.wms.bean.core.YesNos;
import com.zdjer.wms.utils.DBManager;
import com.zdjer.utils.DateHelper;
import com.zdjer.wms.utils.MD5Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务逻辑：用户
 * 
 * @author bipolor
 * 
 */
public class UserBLO {

	/**
	 * 构造函数
	 */
	public UserBLO() {
		
	}
	
	/**
	 * 通过用户的账号、密码，编辑用户的Token
	 * @param uid 用户的账号
	 * @param pwd 用户的密码
	 * @param token 用户的Token
	 * @return 编辑成功，返回true；反之，返回false！
	 */
	public Boolean update(String uid, String pwd,String token) {
		String editDateString = DateHelper.getCurrDateString();
		SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
		ContentValues values = new ContentValues();
		values.put(UserEntry.COLUMN_NAME_TOKEN, token);
		values.put(UserEntry.COLUMN_NAME_EDITDATE, editDateString);

		String selection = UserEntry.COLUMN_NAME_UID 
				+ "=? AND "
				+UserEntry.COLUMN_NAME_PWD+"=?";
		
		String[] selectionArgs = { uid,pwd };
		int count = sqLiteDatabase.update(UserEntry.TABLE_NAME, values, selection,
				selectionArgs);
		sqLiteDatabase.close();
		Log.e("UserDAO", "更新的行数为：" + count);
		return count == 1;
	}

	/**
	 * 注册
	 * 
	 * @param uid
	 *            账号
	 * @param pwd
	 *            密码
	 * @return 注册成功，返回true；反之，返回false！
	 * @throws Exception
	 */
	public Boolean registerUser(String uid, String pwd) throws Exception {
		UserBO userBo = new UserBO();
		userBo.setUid(uid);
		userBo.setPwd(MD5Helper.ConvertToMD5(pwd));
		userBo.setToken("");
		userBo.setCreateDate(DateHelper.getCurrDate());
		userBo.setEditDate(DateHelper.getCurrDate());
		return insert(userBo);
	}
	
	/**
	 * 增加用户
	 * 
	 * @param userBo
	 *            用户对象
	 * @return 增加成功，返回true；反之，返回false！
	 * @throws Exception
	 */
	public Boolean insert(UserBO userBo) throws Exception {
		try {
			SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();

			ContentValues values = new ContentValues();
			values.put(UserEntry.COLUMN_NAME_UID, userBo.getUid());
			values.put(UserEntry.COLUMN_NAME_PWD, userBo.getPwd());
			values.put(UserEntry.COLUMN_NAME_TOKEN, userBo.getToken());
			String createDateString = DateHelper.getDateString(userBo
					.getCreateDate());
			String editDateString = DateHelper.getDateString(userBo
					.getEditDate());
			values.put(UserEntry.COLUMN_NAME_CREATEDATE, createDateString);
			values.put(UserEntry.COLUMN_NAME_EDITDATE, editDateString);
			values.put(UserEntry.COLUMN_NAME_ISADMIN,
					String.valueOf(userBo.getIsAdmin().getValue()));

			long newRowId = sqLiteDatabase.insert(UserEntry.TABLE_NAME, null, values);
			sqLiteDatabase.close();
			Log.i("UserDAO insert", "增加的用户的ID为" + newRowId);
			return newRowId != -1;
		} catch (Exception e) {
			Log.i("UserDAO insert", "增加异常" + e.getMessage());
		}
		return false;
	}

	/**
	 * 通过账号获得用户
	 * 
	 * @param uid
	 *            账号
	 * @return 若账号已存在，返回true；反之，返回false！
	 * @throws ParseException
	 */
	public UserBO getUser(String uid) throws ParseException {
		// /TODO:同步用户数据到本地
		List<UserBO> lstUser = query(uid);
		if (lstUser.size() > 0) {
			return lstUser.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过账号和密码获得用户
	 * 
	 * @param uid
	 *            账号
	 * @param pwd
	 *            密码
	 * @return 用户
	 * @throws ParseException
	 */
	public UserBO getUser(String uid, String pwd) throws ParseException {
		List<UserBO> lstUser = query(uid, pwd);
		if (lstUser.size() > 0) {
			return lstUser.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过账号、是否为管理员获得用户
	 * 
	 * @param uid
	 *            账号
	 * @param isAdmin
	 *            是否是管理员
	 * @return 若存在，返回用户对象；反之，返回null！
	 * @throws ParseException
	 */
	public UserBO getUser(String uid, YesNos isAdmin) throws ParseException {
		List<UserBO> lstUser = query(uid, isAdmin);
		if (lstUser.size() > 0) {
			return lstUser.get(0);
		} else {
			return null;
		}
	}

	/**
	 * ͨ通过用户Id获得用户
	 * 
	 * @param userId
	 *            用户
	 * @return 用户对象
	 * @throws ParseException
	 */
	public UserBO getUserByUserId(long userId) throws ParseException {
		List<UserBO> lstUser = query(userId);
		if (lstUser.size() > 0) {
			return lstUser.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 重置密码
	 * 
	 * @param userId
	 *            用户Id
	 * @return 重置成功，返回true；反之，返回false！
	 */
	public Boolean resetPwd(long userId) {
		String pwd = MD5Helper.ConvertToMD5("000000");
		return update(userId, pwd);
	}

	/**
	 * 编辑用户
	 * @param userId 用户Id
	 * @param pwd 密码
	 * @return 编辑成功，返回true；反之，返回false！
	 */
	private Boolean update(long userId, String pwd) {
		String editDateString = DateHelper.getCurrDateString();
		SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
		ContentValues values = new ContentValues();
		values.put(UserEntry.COLUMN_NAME_PWD, pwd);
		values.put(UserEntry.COLUMN_NAME_EDITDATE, editDateString);

		String selection = BaseColumns._ID + " = ?";
		String[] selectionArgs = { String.valueOf(userId) };
		int count = sqLiteDatabase.update(UserEntry.TABLE_NAME, values, selection,
				selectionArgs);
		sqLiteDatabase.close();
		Log.e("UserDAO", "更新的行数为：" + count);
		return count == 1;
	}

	/**
	 * 修改密码
	 * 
	 * @param userId
	 *            用户Id
	 * @param newPwd
	 *            新密码
	 * @return 修改成功，返回true；反之，返回false！
	 */
	public Boolean editPwd(long userId, String newPwd) {
		newPwd = MD5Helper.ConvertToMD5(newPwd);
		return update(userId, newPwd);
	}

	/**
	 * 判断账号是否存在
	 * 
	 * @param uid
	 *            账号
	 * @return 若账号已存在，返回true；反之，返回false！
	 * @throws ParseException
	 */
	public Boolean isExist(String uid) throws ParseException {
		List<UserBO> lstUser = query(uid);
		return lstUser.size() > 0;
	}

	/**
	 * 查询数据
	 * 
	 * @return 获得用户对象集合
	 * @throws ParseException
	 */
	private List<UserBO> query(String uid, String pwd) throws ParseException {
		SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
		String[] projection = { BaseColumns._ID, 
				/*UserEntry.COLUMN_NAME_UID,
				UserEntry.COLUMN_NAME_PWD,*/ 
				UserEntry.COLUMN_NAME_TOKEN,
				UserEntry.COLUMN_NAME_CREATEDATE,
				UserEntry.COLUMN_NAME_EDITDATE, 
				UserEntry.COLUMN_NAME_ISADMIN };
		String selection = UserEntry.COLUMN_NAME_UID + " =? AND "
				+ UserEntry.COLUMN_NAME_PWD + "=?";
		
		String[] selectionArgs = new String[] { uid, pwd };
		String sortOrder = UserEntry.COLUMN_NAME_UID + " DESC";

		Cursor cursor = sqLiteDatabase.query(UserEntry.TABLE_NAME, projection, selection,
				selectionArgs, null, null, sortOrder);

		List<UserBO> lstUser = new ArrayList<UserBO>();
		UserBO userBo = null;
		while (cursor.moveToNext()) {

			long userId = cursor.getLong(cursor
					.getColumnIndexOrThrow(BaseColumns._ID));
			/*String uid1 = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_UID));
			String pwd1 = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_PWD));*/
			String token=cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_TOKEN));
			String createDateString = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_CREATEDATE));
			String editDateString = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_EDITDATE));
			int isAdmin = cursor.getInt(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_ISADMIN));
			Log.i("query userId", "" + userId);
			Log.i("query uid", uid);
			Log.i("query pwd", pwd);
			Log.i("query token",token);
			Log.i("query createDateString", createDateString);
			Log.i("query editDateString", editDateString);
			Log.i("query isAdmin", "" + isAdmin);

			userBo = new UserBO();
			userBo.setUserId(userId);
			userBo.setUid(uid);
			userBo.setPwd(pwd);
			userBo.setToken(token);
			userBo.setCreateDate(DateHelper.getDate(createDateString));
			userBo.setEditDate(DateHelper.getDate(editDateString));
			userBo.setIsAdmin(YesNos.value(isAdmin));
			lstUser.add(userBo);
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		sqLiteDatabase.close();
		return lstUser;
	}

	/**
	 * 查询数据
	 * 
	 * @return 获得用户对象集合
	 * @throws ParseException
	 */
	public List<UserBO> query(String uid, YesNos isAdmin) throws ParseException {
		SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
		String[] projection = { BaseColumns._ID, 
				/*UserEntry.COLUMN_NAME_UID,*/
				UserEntry.COLUMN_NAME_PWD, 
				UserEntry.COLUMN_NAME_TOKEN,
				UserEntry.COLUMN_NAME_CREATEDATE,
				UserEntry.COLUMN_NAME_EDITDATE/*, 
				UserEntry.COLUMN_NAME_ISADMIN*/ };
		
		String selection = UserEntry.COLUMN_NAME_UID + " =? AND "
				+ UserEntry.COLUMN_NAME_ISADMIN + "=?";
		String[] selectionArgs = new String[] { uid,
				String.valueOf(isAdmin.getValue()) };
		String sortOrder = UserEntry.COLUMN_NAME_UID + " DESC";

		Cursor cursor = sqLiteDatabase.query(UserEntry.TABLE_NAME, projection, selection,
				selectionArgs, null, null, sortOrder);

		List<UserBO> lstUser = new ArrayList<UserBO>();
		UserBO userBo = null;
		while (cursor.moveToNext()) {

			long userId = cursor.getLong(cursor
					.getColumnIndexOrThrow(BaseColumns._ID));
			/*String uid1 = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_UID));*/
			String pwd = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_PWD));
			String token=cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_TOKEN));
			String createDateString = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_CREATEDATE));
			String editDateString = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_EDITDATE));
			/*int isAdmin1 = cursor.getInt(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_ISADMIN));*/
			Log.i("query userId", "" + userId);
			Log.i("query uid", uid);
			Log.i("query pwd", pwd);
			Log.i("query createDateString", createDateString);
			Log.i("query editDateString", editDateString);
			Log.i("query isAdmin", "" + isAdmin);

			userBo = new UserBO();
			userBo.setUserId(userId);
			userBo.setUid(uid);
			userBo.setPwd(pwd);
			userBo.setToken(token);
			userBo.setCreateDate(DateHelper.getDate(createDateString));
			userBo.setEditDate(DateHelper.getDate(editDateString));
			/*userBo.setIsAdmin(YesNos.value(isAdmin1));*/
			userBo.setIsAdmin(isAdmin);
			lstUser.add(userBo);
		}
		if(!cursor.isClosed()){
			cursor.close();
		}
		sqLiteDatabase.close();
		return lstUser;
	}

	/**
	 * 查询
	 * 
	 * @return 用户集合
	 * @throws ParseException
	 */
	public List<UserBO> query(String uid) throws ParseException {
		SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
		String[] projection = { BaseColumns._ID, 
				/*UserEntry.COLUMN_NAME_UID,*/
				UserEntry.COLUMN_NAME_PWD, 
				UserEntry.COLUMN_NAME_TOKEN,
				UserEntry.COLUMN_NAME_CREATEDATE,
				UserEntry.COLUMN_NAME_EDITDATE, 
				UserEntry.COLUMN_NAME_ISADMIN };
		String selection = UserEntry.COLUMN_NAME_UID + " =?";
		String[] selectionArgs = new String[] { uid };
		String sortOrder = UserEntry.COLUMN_NAME_UID + " DESC";

		Cursor cursor = sqLiteDatabase.query(UserEntry.TABLE_NAME, projection, selection,
				selectionArgs, null, null, sortOrder);

		List<UserBO> lstUser = new ArrayList<UserBO>();
		UserBO userBo = null;
		while (cursor.moveToNext()) {

			long userId = cursor.getLong(cursor
					.getColumnIndexOrThrow(BaseColumns._ID));
			/*String uid1 = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_UID));*/
			String pwd = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_PWD));
			String token=cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_TOKEN));
			String createDateString = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_CREATEDATE));
			String editDateString = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_EDITDATE));
			int isAdmin = cursor.getInt(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_ISADMIN));
			Log.i("query userId", "" + userId);
			Log.i("query uid", uid);
			Log.i("query pwd", pwd);
			Log.i("query createDateString", createDateString);
			Log.i("query editDateString", editDateString);
			Log.i("query isAdmin", "" + isAdmin);

			userBo = new UserBO();
			userBo.setUserId(userId);
			userBo.setUid(uid);
			userBo.setPwd(pwd);
			userBo.setToken(token);
			userBo.setCreateDate(DateHelper.getDate(createDateString));
			userBo.setEditDate(DateHelper.getDate(editDateString));
			userBo.setIsAdmin(YesNos.value(isAdmin));
			lstUser.add(userBo);
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		sqLiteDatabase.close();
		return lstUser;
	}

	/**
	 * 查询
	 * 
	 * @return 用户集合
	 * @throws ParseException
	 */
	public List<UserBO> query(long userId) throws ParseException {
		SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
		String[] projection = { 
				/*BaseColumns._ID,*/ 
				UserEntry.COLUMN_NAME_UID,
				UserEntry.COLUMN_NAME_PWD, 
				UserEntry.COLUMN_NAME_TOKEN,
				UserEntry.COLUMN_NAME_CREATEDATE,
				UserEntry.COLUMN_NAME_EDITDATE, 
				UserEntry.COLUMN_NAME_ISADMIN };
		String selection = BaseColumns._ID + " =?";
		
		String[] selectionArgs = new String[] { String.valueOf(userId) };
		String sortOrder = UserEntry.COLUMN_NAME_UID + " DESC";

		Cursor cursor = sqLiteDatabase.query(UserEntry.TABLE_NAME, projection, selection,
				selectionArgs, null, null, sortOrder);

		List<UserBO> lstUser = new ArrayList<UserBO>();
		UserBO userBo = null;
		while (cursor.moveToNext()) {
			/*long userId1 = cursor.getLong(cursor
					.getColumnIndexOrThrow(BaseColumns._ID));*/
			String uid = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_UID));
			String pwd = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_PWD));
			String token=cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_TOKEN));
			String createDateString = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_CREATEDATE));
			String editDateString = cursor.getString(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_EDITDATE));
			int isAdmin = cursor.getInt(cursor
					.getColumnIndexOrThrow(UserEntry.COLUMN_NAME_ISADMIN));
			Log.i("query userId", "" + userId);
			Log.i("query uid", uid);
			Log.i("query pwd", pwd);
			Log.i("query createDateString", createDateString);
			Log.i("query editDateString", editDateString);
			Log.i("query isAdmin", "" + isAdmin);

			userBo = new UserBO();
			userBo.setUserId(userId);
			userBo.setUid(uid);
			userBo.setPwd(pwd);
			userBo.setToken(token);
			userBo.setCreateDate(DateHelper.getDate(createDateString));
			userBo.setEditDate(DateHelper.getDate(editDateString));
			userBo.setIsAdmin(YesNos.value(isAdmin));
			lstUser.add(userBo);
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		sqLiteDatabase.close();
		return lstUser;
	}
}
