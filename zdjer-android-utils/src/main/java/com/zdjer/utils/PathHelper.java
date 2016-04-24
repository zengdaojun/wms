package com.zdjer.utils;

import android.os.Environment;

import java.io.File;

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


	// 默认存放文件下载的路径
	public final static String DEFAULT_SAVE_FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "OSChina"
			+ File.separator + "download" + File.separator;
}
