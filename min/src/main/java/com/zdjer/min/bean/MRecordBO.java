package com.zdjer.min.bean;

import android.provider.BaseColumns;

import com.zdjer.utils.DateHelper;

import java.util.Date;

/*
 * 实体：记录
 */
public class MRecordBO {

    private long mrecordId = 0;// 记录Id
    private String barCode = ""; // 条形码
    private long createUserId = 0;//创建人Id
    private Date createDate = null; // 创建时间
    private long sendPersonId = 0;//送货人Id
    private BarCodeTypes barCodeType = BarCodeTypes.in;//条码类型
    private long serverProductId = 0;//服务端产品Id
    private MRecordType mrecordType = MRecordType.in;// 记录类型

    private boolean isMatch = false;//辅助属性：是否匹配

    /**
     * 构造函数
     */
    public MRecordBO() {

    }

    /**
     * 获取记录Id
     *
     * @return 记录Id
     */
    public long getMRecordId() {
        return mrecordId;
    }

    /**
     * 设置记录Id
     *
     * @param mrecordId 记录Id
     */
    public void setRecordId(long mrecordId) {
        this.mrecordId = mrecordId;
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
     * @param barCode 条形码
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * 获取创建人Id
     *
     * @return 创建人Id
     */
    public long getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置创建人Id
     *
     * @param createUserId
     */
    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
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
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获得送货Id
     *
     * @return
     */
    public long getSendPersonId() {
        return this.sendPersonId;
    }

    /**
     * 设置送货人Id
     *
     * @param sendPersonId
     */
    public void setSendPersonIdId(long sendPersonId) {
        this.sendPersonId = sendPersonId;
    }

    /**
     * 获得服务端产品Id
     *
     * @return 服务端产品Id
     */
    public long getServerProductId() {
        return serverProductId;
    }

    /**
     * 设置服务端产品Id
     *
     * @param serverProductId 服务端产品Id
     */
    public void setServerProductId(long serverProductId) {
        this.serverProductId = serverProductId;
    }

    /**
     * 获得条码类型
     *
     * @return 条码类型
     */
    public BarCodeTypes getBarCodeType() {
        return barCodeType;
    }

    /**
     * 设置条码类型
     *
     * @param barCodeType 条码类型
     */
    public void setBarCodeType(BarCodeTypes barCodeType) {
        this.barCodeType = barCodeType;
    }

    /**
     * 获取记录类型
     *
     * @return 记录类型
     */
    public MRecordType getMRecordType() {
        return mrecordType;
    }

    /**
     * 设置记录类型
     *
     * @param mrecordType 记录类型
     */
    public void setMRecordType(MRecordType mrecordType) {
        this.mrecordType = mrecordType;
    }

    /**
     * 获得是否匹配
     *
     * @return
     */
    public boolean getIsMatch() {
        return isMatch;
    }

    /**
     * 设置是否匹配
     *
     * @param isMatch
     */
    public void setIsMatch(boolean isMatch) {
        this.isMatch = isMatch;
    }

    /**
     * 记录存储数据定义
     *
     * @author bipolor
     */
    public static abstract class MRecordEntry implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = "NULL";
        public static final String TABLE_NAME = "tb_mrecord";
        public static final String COLUMN_NAME_BARCODE = "barcode";// 条码
        public static final String COLUMN_NAME_CREATEUSERID = "createuserid";// 创建人Id
        public static final String COLUMN_NAME_CREATEDATE = "createdate";// 创建时间
        public static final String COLUMN_NAME_SENDPERSONID = "sendpersonid";//送货人Id
        public static final String COLUMN_NAME_BARCODETYPE = "barcodetype";//条码类型
        public static final String COLUMN_NAME_SERVERPRODUCTID = "serverproductid";//服务端产品Id
        public static final String COLUMN_NAME_MRECORDTYPE = "mrecordtype";//记录类型
    }
}
