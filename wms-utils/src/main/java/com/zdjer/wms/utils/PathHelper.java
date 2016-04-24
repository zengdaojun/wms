package com.zdjer.wms.utils;

import android.os.Environment;

/**
 * 路径通用类
 * @author bipolor
 *
 */
public class PathHelper {
	
	/*
	 * 获取SD卡根目录
	 * 
	 * @param recordType
	 */	
	public static String GetSDRootPath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String root=Environment.getExternalStorageDirectory().toString();	//SD卡目录
			return root;			
		} else {
			return null;
		}
	}
}
