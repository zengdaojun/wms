package com.zdjer.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * 文件数据库通用类
 */
public class TxtDBHelper {

	private static String dataFilePath = ""; // 数据文件路径
	
	/*
	 * 获得数据文件路径
	 */
	public static String getDataFilePath() {
		return dataFilePath;
	}

	/*
	 * 设置数据文件路径
	 */
	public static void setDataFilePath(String dataFilePath) {
		TxtDBHelper.dataFilePath = dataFilePath;
	}

	/**
	 * 写入数据
	 * 
	 * @param data
	 *            数据
	 * @return 写入成功，返回true；反之，返回false!
	 * @throws Exception
	 */
	public static Boolean WriteData(String data){

		File file = new File(dataFilePath);// 文件路径

		if (!file.getParentFile().exists()) {// 判断父目录是否存在
			file.getParentFile().mkdirs();// 不存在创建
		}

		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream(file, true));
			out.println(data);
		} catch (Exception e) {
			Log.i("异常","系统异常！");
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return false;
	}

	/**
	 * 写入数据
	 * 
	 * @param lstData
	 *            数据集合
	 * @return 写入成功，返回true；反之，返回false!
	 * @throws Exception
	 */
	public static Boolean WriteDatas(List<String> lstData) throws Exception {

		File file = new File(dataFilePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(file.exists()){
			file.delete();
		}

		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream(file, true));
			for (String data : lstData) {
				out.println(data);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return true;
	}

	/**
	 * ��ȡ����ļ��е�ȫ�����
	 * 
	 * @return ȫ����ݼ���
	 * @throws Exception
	 */
	public static List<String> ReadDatas()
			throws Exception {

		File file = new File(dataFilePath);// ����File�����

		if (!file.getParentFile().exists()) {// ���ļ��в�����
			file.getParentFile().mkdirs();// �����ļ���
		}if(!file.exists()){
			file.createNewFile();
		}
			
		Scanner scan = null; // ɨ������
		try {
			scan = new Scanner(new FileInputStream(file)); // ʵ��
			List<String> lstData = new ArrayList<String>();
			while (scan.hasNext()) {
				lstData.add(scan.next().toString());
			}
			return lstData;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (scan != null) {
				scan.close();// �رմ�ӡ��
			}
		}
		return null;
	}
}
