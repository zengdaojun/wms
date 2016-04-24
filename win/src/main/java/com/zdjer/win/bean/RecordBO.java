package com.zdjer.win.bean;

import android.provider.BaseColumns;

import com.zdjer.utils.DateHelper;
import com.zdjer.wms.bean.UserBO;
import com.zdjer.wms.bean.core.YesNos;

import java.io.Serializable;
import java.util.Date;

/*
 * 实体：记录
 */
public class RecordBO implements Serializable {
	
	private long recordId = 0;// 记录Id
	private String barCode = ""; // 条形码
	private String thdNum = ""; // 提货单号
	private InTypes inType = InTypes.chushi;//入库方式
	private OutTypes outType = OutTypes.lingshou;//出库方式
	private String dbdNum = "";	//调拨单号
	private long jxsNumId=0;//经销商Id
	private String jxsNum = ""; // 经销商编号
	private long warehouseId=0;//仓库货位Id
	private String warehouse = "";// 仓库货位
	private long createUserId=0;//创建人Id
	private String createUserUID="";//创建人UID
	private UserBO createUser = null; // 创建人
	private Date createDate = null; // 创建时间
	private RecordType recordType = RecordType.in;// 记录类型
	private int barCodeCount = 0;// 条码数量
	private long tranId=0;//运输Id
	private YesNos isUpload = YesNos.No;// 是否上传

	/**
	 * 构造函数
	 */
	public RecordBO() {
		
	}

	/**
	 * 获取记录Id
	 * 
	 * @return 记录Id
	 */
	public long getRecordId() {
		return recordId;
	}

	/**
	 * 设置记录Id
	 * 
	 * @param recordId
	 *            记录Id
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	/**
	 * 获得条形码
	 * 
	 * @return 条形码
	 */
	public String getBarCode() {
		return barCode;
	}

	/**
	 * 设置条形码
	 * 
	 * @param barCode
	 *            条形码
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	/**
	 * 获取提货单号
	 * 
	 * @return 条形码
	 */
	public String getThdNum() {
		return thdNum;
	}

	/**
	 * 设置提货单号
	 * 
	 * @param thdNum
	 *            提货单号
	 */
	public void setThdNum(String thdNum) {
		this.thdNum = thdNum;
	}

	/**
	 * 获得入库方式
	 * @return
	 */
	public InTypes getInType() {
		return inType;
	}

	/**
	 * 设置入库方式
	 * @param inType
	 */
	public void setInType(InTypes inType) {
		this.inType = inType;
	}

	/**
	 * 获得出库方式
	 * @return 出库方式
	 */
	public OutTypes getOutType() {
		return outType;
	}

	/**
	 * 设置出库方式
	 * @param outType 出库方式
	 */
	public void setOutType(OutTypes outType) {
		this.outType = outType;
	}

	/**
	 * 获取调拨单号
	 * @return 调拨单号
	 */
	public String getDbdNum() {
		return dbdNum;
	}

	/**
	 * 设置调拨单号
	 * @param dbdNum 调拨单号
	 */
	public void setDbdNum(String dbdNum) {
		this.dbdNum = dbdNum;
	}

	/**
	 * 获得经销商Id
	 * @return
	 */
	public long getJxsNumId() {
		return jxsNumId;
	}

	/**
	 * 设置经销商Id
	 * @param jxsNumId
	 */
	public void setJxsNumId(long jxsNumId) {
		this.jxsNumId = jxsNumId;
	}

	/**
	 * 获取经销商编号
	 * 
	 * @return 经销商编号
	 */
	public String getJxsNum() {
		return jxsNum;
	}

	/**
	 * 设置经销商编号
	 * 
	 * @param jxsNum
	 *            经销商编号
	 */
	public void setJxsNum(String jxsNum) {
		this.jxsNum = jxsNum;
	}
	
	/**
	 * 获得仓库货位Id
	 * @return
	 */
	public long getWarehouseId() {
		return warehouseId;
	}

	/**
	 * 设置仓库货位Id
	 * @param warehouseId
	 */
	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * 获得仓库
	 * 
	 * @return 仓库
	 */
	public String getWarehouse() {
		return warehouse;
	}

	/**
	 * 设置仓库
	 * 
	 * @param warehouse
	 *            仓库
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}	
	
	/**
	 * 获取创建人Id
	 * @return 创建人Id
	 */
	public long getCreateUserId() {
		return createUserId;
	}

	/**
	 * 设置创建人Id
	 * @param createUserId
	 */
	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * 获取创建人UID
	 * @return
	 */
	public String getCreateUserUID() {
		return createUserUID;
	}

	/**
	 * 设置创建人UID
	 * @param createUserUID
	 */
	public void setCreateUserUID(String createUserUID) {
		this.createUserUID = createUserUID;
	}

	/**
	 * 获取创建人
	 * 
	 * @return 创建人
	 */
	public UserBO getCreateUser() {
		if (createUser == null) {
			createUser = new UserBO();
		}
		return createUser;
	}

	/**
	 * 设置创建人
	 * 
	 * @param createUser 创建人
	 */
	public void setCreateUser(UserBO createUser) {
		this.createUser = createUser;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public Date getCreateDate() {
		if (createDate == null) {
			createDate = DateHelper.getCurrDate();
		}
		return createDate;
	}

	/**
	 * 设置创建时间
	 * 
	 * @param createDate
	 *            创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取记录类型
	 * 
	 * @return 记录类型
	 */
	public RecordType getRecordType() {
		return recordType;
	}

	/**
	 * 设置记录类型
	 * 
	 * @param recordType
	 *            记录类型
	 */
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}

	/**
	 * 获得条码数量
	 * 
	 * @return 条码数量
	 */
	public int getBarCodeCount() {
		return barCodeCount;
	}

	/**
	 * 设置条码数量
	 * 
	 * @param barCodeCount
	 *            条码数量
	 */
	public void setBarCodeCount(int barCodeCount) {
		this.barCodeCount = barCodeCount;
	}
	
	/**
	 * 获得运输Id
	 * @return
	 */
	public long getTranId() {
		return tranId;
	}

	/**
	 * 设置运输Id
	 * @param tranId
	 */
	public void setTranId(long tranId) {
		this.tranId = tranId;
	}

	/**
	 * 获取是否上传
	 * 
	 * @return
	 */
	public YesNos getIsUpload() {
		return isUpload;
	}

	/**
	 * 设置是否上传
	 * 
	 * @param isUpload
	 */
	public void setIsUpload(YesNos isUpload) {
		this.isUpload = isUpload;
	}

	/**
	 * 提货单类
	 * 
	 * @author bipolor
	 * 
	 */
	public class ThdNumBO {
		private long recordId = 0;// 记录Id
		private String thdNum = null;// 提货单号
		private int barCodeCount = 0;// 条码数量
		
		/**
		 * 构造函数
		 */
		public ThdNumBO() {

		}

		/**
		 * 获得记录Id
		 * 
		 * @return 记录Id
		 */
		public long getRecordId() {
			return recordId;
		}

		/**
		 * 设置记录Id
		 * 
		 * @param recordId
		 *            记录Id
		 */
		public void setRecordId(long recordId) {
			this.recordId = recordId;
		}

		/**
		 * 获得 提货单号
		 * 
		 * @return 提货单号
		 */
		public String getThdNum() {
			return thdNum;
		}

		/**
		 * 设置 提货单号
		 * 
		 * @param thdNum
		 *            提货单号
		 */
		public void setThdNum(String thdNum) {
			this.thdNum = thdNum;
		}

		/**
		 * 获得条码数量
		 * 
		 * @return 条码数量
		 */
		public int getBarCodeCount() {
			return barCodeCount;
		}

		/**
		 * 设置条码数量
		 * 
		 * @param barCodeCount
		 *            条码数量
		 */
		public void setBarCodeCount(int barCodeCount) {
			this.barCodeCount = barCodeCount;
		}

	}
	

	/**
	 * 记录存储数据定义
	 * 
	 * @author bipolor
	 * 
	 */
	public static abstract class RecordEntry implements BaseColumns {
		public static final String COLUMN_NAME_NULLABLE = "NULL";
		public static final String TABLE_NAME = "tb_record";
		public static final String COLUMN_NAME_BARCODE = "barcode";// 条码
		public static final String COLUMN_NAME_THDNUM = "thdnum";// 提货单号
		public static final String COLUMN_NAME_IOTYPE = "iotype";//出入库方式
		public static final String COLUMN_NAME_DBDNUM = "dbdnum";	//调拨单号
		public static final String COLUMN_NAME_JXSNUMID="jxsnumid";//经销商Id
		public static final String COLUMN_NAME_JXSNUM = "jxsnum";// 经销商编号
		public static final String COLUMN_NAME_WAREHOUSEID="warehouseid";//仓库货位Id
		public static final String COLUMN_NAME_WAREHOUSE = "warehouse";// 仓库
		public static final String COLUMN_NAME_CREATEUSERID = "createuserid";// 创建人Id
		public static final String COLUMN_NAME_CREATEUSERUID="createuseruid";//	创建人UID
		public static final String COLUMN_NAME_CREATEDATE = "createdate";// 创建时间
		public static final String COLUMN_NAME_RECORDTYPE = "recordtype";//记录类型
		public static final String COLUMN_NAME_TRANID="tranid";//运输Id
		public static final String COLUMN_NAME_ISUPLOAD="isupload";//是否上传
		
		public static final String COLUMN_NAME_BARCODECOUNT = "barcodecount";// 条码数量(不保存数据库)

	}
}
