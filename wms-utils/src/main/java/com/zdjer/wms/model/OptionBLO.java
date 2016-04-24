package com.zdjer.wms.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zdjer.wms.bean.OptionBO;
import com.zdjer.wms.bean.OptionBO.OptionEntry;
import com.zdjer.wms.bean.core.OptionTypes;
import com.zdjer.wms.utils.DBManager;

/**
 * 业务逻辑类:选项
 * 
 * @author bipolor
 * 
 */
public class OptionBLO {

	/**
	 * 构造函数
	 */
	public OptionBLO() {
	}

	/**
	 * 操作选项，包括添加或编辑，若操作的选项已经存在，则修改，否则添加（保证一个选项中只有一个值）
	 * 
	 * @param option
	 *            选项
	 * @return 操作成功，返回true；反之，返回false！
	 */
	public Boolean operateOption(OptionBO option) {
		String optionValue = getOptionValue(option.getOptionType());
		if (optionValue == null) {
			Log.i("OptionBLO operateOption", "addOption!");
			return addOption(option);
		} else {
			Log.i("OptionBLO operateOption", "editOption!");
			return editOption(option.getOptionType(), option.getOptionValue());
		}
	}

	/**
	 * 添加选项
	 * 
	 * @param option
	 *            选项类型
	 * @return 添加成功，返回true；反之，返回false！
	 */
	private Boolean addOption(OptionBO option) {
		try {
			SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
			ContentValues values = new ContentValues();
			values.put(OptionEntry.COLUMN_NAME_OPTIONTYPE,
					String.valueOf(option.getOptionType().getValue()));
			values.put(OptionEntry.COLUMN_NAME_OPTIONVALUE,
					String.valueOf(option.getOptionValue()));
			long newRowId = sqLiteDatabase.insert(OptionEntry.TABLE_NAME, null, values);
			sqLiteDatabase.close();
			if (newRowId != -1) {
				Log.i("OptionBLO addOption", "Success!");
				return true;
			} else {
				Log.i("OptionBLO addOption", "Faild!");
				return false;
			}
		} catch (Exception e) {
			Log.i("OptionBLO addOption", "Exception：" + e.getMessage());
		}
		return false;
	}

	/**
	 * 编辑选项，将指定的选项类型编辑为指定的值
	 * 
	 * @param optionType
	 *            选项类型，需要编辑的选项类型
	 * @param optionValue
	 *            选项值，选项类型编辑后的值
	 * @return 编辑成功，返回true；反之，返回false！
	 */
	private Boolean editOption(OptionTypes optionType, String optionValue) {
		SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
		ContentValues values = new ContentValues();
		values.put(OptionEntry.COLUMN_NAME_OPTIONVALUE, optionValue);

		String selection = OptionEntry.COLUMN_NAME_OPTIONTYPE + " = ?";
		String[] selectionArgs = { String.valueOf(optionType.getValue()) };
		int count = sqLiteDatabase.update(OptionEntry.TABLE_NAME, values, selection,
				selectionArgs);
		sqLiteDatabase.close();
		if (count == 1) {
			Log.e("OptionBLO editOption", "success!");
			return true;
		} else {
			Log.e("OptionBLO editOption", "faild!");
			return false;
		}
	}

	/**
	 * 获得选项的值
	 * 
	 * @param optionType
	 *            选项类型
	 * @return 选项对应的值
	 */
	public String getOptionValue(OptionTypes optionType) {
		SQLiteDatabase sqliteDatabase = DBManager.getDatabase();
		String[] projection = { OptionEntry.COLUMN_NAME_OPTIONVALUE };
		String selection = OptionEntry.COLUMN_NAME_OPTIONTYPE + " =?";
		String[] selectionArgs = new String[] { String.valueOf(optionType
				.getValue()) };

		Cursor cursor = sqliteDatabase.query(OptionEntry.TABLE_NAME, projection, selection,
				selectionArgs, null, null, null);
		while (cursor.moveToNext()) {
			String optionValue = cursor
					.getString(cursor
							.getColumnIndexOrThrow(OptionEntry.COLUMN_NAME_OPTIONVALUE));
			Log.e("OptionBLO getOption", "optionValue:" + optionValue);
			return optionValue;
		}
		if(!cursor.isClosed()) {
			cursor.close();
		}
		if(sqliteDatabase.isOpen()) {
			sqliteDatabase.close();
		}
		Log.e("OptionBLO getOption", "optionValue:null");
		return null;
	}
}
