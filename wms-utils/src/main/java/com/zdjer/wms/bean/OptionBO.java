package com.zdjer.wms.bean;

import android.provider.BaseColumns;

import com.zdjer.wms.bean.core.OptionTypes;

/**
 * 实体类：设备
 * 
 * @author bipolor
 * 
 */
public class OptionBO {
	private long optionId=0;//选项Id
	private String optionValue="";//选项值
	private OptionTypes optionType=OptionTypes.DeviceNum;//
	
	/**
	 * 构造函数
	 */
	public OptionBO(){
		
	}
	
	/**
	 * 获得设备Id
	 * @return 设备Id
	 */
	public long getOptionId() {
		return optionId;
	}
	
	/**
	 * 选项Id
	 * @param optionId 设备Id
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}
	
	/**
	 * 获得选项值
	 * @return
	 */
	public String getOptionValue() {
		return optionValue;
	}

	/**
	 * 设置选项
	 * @param optionValue
	 */
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	/**
	 * 获得选项类型
	 * @return
	 */
	public OptionTypes getOptionType() {
		return optionType;
	}

	/**
	 * 设置选项类型
	 * @param optionType
	 */
	public void setOptionType(OptionTypes optionType) {
		this.optionType = optionType;
	}
	
	/**
	 * 设备数据存储定义
	 * @author bipolor
	 *
	 */
	public static abstract class OptionEntry implements BaseColumns{
		public static final String COLUMN_NAME_NULLABLE=null;
		public static final String TABLE_NAME="tb_option";//表名
		public static final String COLUMN_NAME_OPTIONVALUE="optionValue";//选项值
		public static final String COLUMN_NAME_OPTIONTYPE="optionType";//选项类型
	}
}
