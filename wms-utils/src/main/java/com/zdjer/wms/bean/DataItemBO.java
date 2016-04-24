package com.zdjer.wms.bean;

import android.provider.BaseColumns;

/**
 * 实体类：数据(各类基础数据)
 * 
 * @author bipolor
 * 
 */
public class DataItemBO implements Comparable<DataItemBO> {
	private long dataItemId=0;//数据Id
	private long dataId=0;//数据Id
	private String dataValue="";//数据值
	private DataType dataType=DataType.wareHouse;//数据类型
	private long parentId=0;//父数据Id

	/**
	 * 构造函数
	 */
	public DataItemBO(){
		
	}
	
	/**
	 * 获取数据项Id
	 * @return 数据项Id
	 */
	public long getDataItemId() {
		return dataItemId;
	}

	/**
	 * 设置数据项Id
	 * @param dataItemId
	 */
	public void setDataItemId(long dataItemId) {
		this.dataItemId = dataItemId;
	}

	/**
	 * 获得dataId
	 * @return dataId
	 */
	public long getDataId() {
		return dataId;
	}

	/**
	 * 设置数据Id
	 * @param dataId 数据Id
	 */
	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	/**
	 * 获得数据值
	 * @return 数据值
	 */
	public String getDataValue() {
		return dataValue;
	}

	/**
	 * 设置数据值
	 * @param dataValue 数据值
	 */
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	/**
	 * 获得数据类型
	 * @return 数据类型
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * 设置数据类型
	 * @param dataType 数据类型
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * 获取父数据Id
	 * @return
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * 设置父数据Id
	 * @param parentId
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 比较
	 * @param dataItem
	 * @return
	 */
	public int compareTo(DataItemBO dataItem) {
		if(this.getDataId()> dataItem.getDataId()){
			return 1;
		}else if(this.getDataId() == dataItem.getDataId()){
			return 0;
		}else{
			return -1;
		}
	}

	/**
	 * 设备数据存储定义
	 * @author bipolor
	 *
	 */
	public static abstract class DataItemEntry implements BaseColumns{
		public static final String COLUMN_NAME_NULLABLE=null;
		public static final String TABLE_NAME="tb_dataitem";//表名
		public static final String COLUMN_NAME_DATAID="dataid";//id
		public static final String COLUMN_NAME_DATAVALUE="datavalue";//选项值
		public static final String COLUMN_NAME_DATATYPE="datatype";//选项类型
		public static final String COLUMN_NAME_PARENTID="parentid";//父数据Id
	}
}
