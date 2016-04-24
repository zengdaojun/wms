package com.zdjer.wms.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLiteDB 通用类
 * 
 * @author bipolor
 * 
 */
public class SQLiteDBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;// 版本号
	private List<String> lstCreateScript = new ArrayList<String>();// 创建数据库脚本

	/*private static SQLiteOpenHelper mInstance = null;

	public static SQLiteOpenHelper getInstance(Context context, String dbName,
											 List<String> lstCreateScript) {
		if (mInstance == null) {
			mInstance = new SQLiteDBHelper(context,dbName,lstCreateScript);
		}
		return mInstance;
	}*/

	public SQLiteDBHelper(Context context, String dbName,
			List<String> lstCreateScript) {
		super(context, dbName, null, DATABASE_VERSION);
		this.lstCreateScript = lstCreateScript;
		Log.i("SQLiteDBHelper", "SQLiteDBHelper初始化成功！");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String createSrcipt : lstCreateScript) {
			Log.i("SQLiteDBHelper", "创建数据库脚本：" + createSrcipt);
			db.execSQL(createSrcipt);// 执行数据库创建脚本
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
		if (oldVersion != DATABASE_VERSION) {
			for (String upgradeSrcipt : lstCreateScript) {
				Log.i("SQLiteDBHelper", "升级数据库脚本" + upgradeSrcipt);
				db.execSQL(upgradeSrcipt);// 执行数据库创建脚本
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}
}
