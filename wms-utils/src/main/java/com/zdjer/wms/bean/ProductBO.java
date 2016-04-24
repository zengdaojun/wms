package com.zdjer.wms.bean;

import android.provider.BaseColumns;

/*
 * 实体：产品
 */
public class ProductBO {

    private long productId = 0;// 产品Id
    private long serverProductId = 0;//服务端产品Id
    private String productName = ""; //产品名称
    private String minModel = "";//内机型号
    private String minSerno = "";//内机号
    private String moutModel = "";//外机型号
    private String moutSerno = "";//外机号

    private int incount = 0;//内机数量
    private int outcount = 0;//外机数量

    /**
     * 构造函数
     */
    public ProductBO() {

    }

    /**
     * 获得产品Id
     *
     * @return 产品Id
     */
    public long getProductId() {
        return productId;
    }

    /**
     * 设置产品Id
     *
     * @param productId 产品Id
     */
    public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * 获得服务器端产品Id
     * @return serverProductId 服务端产品Id
     */
    public long getServerProductId(){
        return serverProductId;
    }

    /**
     *设置服务端产品Id
     *
     *@param serverProductId 服务端产品Id
     */
    public void setServerProductId(long serverProductId){
        this.serverProductId = serverProductId;
    }

    /**
     * 获得产品名称
     *
     * @return 产品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置产品名称
     *
     * @param productName 产品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获得内机型号
     *
     * @return 内机型号
     */
    public String getMinModel() {
        return minModel;
    }

    /**
     * 设置内机型号
     *
     * @param minModel 内机型号
     */
    public void setMinModel(String minModel) {
        this.minModel = minModel;
    }

    /**
     * 获得内机号
     *
     * @return 内机号
     */
    public String getMinSerno() {
        return minSerno;
    }

    /**
     * 设置内机号
     *
     * @param minSerno
     */
    public void setMinSerno(String minSerno) {
        this.minSerno = minSerno;
    }

    /**
     * 获得外机型号
     *
     * @return 外机型号
     */
    public String getMoutModel() {
        return moutModel;
    }

    /**
     * 设置外机型号
     *
     * @param moutModel
     */
    public void setMoutModel(String moutModel) {
        this.moutModel = moutModel;
    }

    /**
     * 获得外机号
     *
     * @return 外机号
     */
    public String getMoutSerno() {
        return moutSerno;
    }

    /**
     * 设置外机号
     *
     * @param moutSerno 外机号
     */
    public void setMoutSerno(String moutSerno) {
        this.moutSerno = moutSerno;
    }

    /**
     * 获得外机数量
     * @return 外机数量
     */
    public int getOutcount() {
        return outcount;
    }

    /**
     * 设置外机数量
     * @param outcount 外机数量
     */
    public void setOutcount(int outcount) {
        this.outcount = outcount;
    }

    /**
     * 获得内机数量
     * @return 内机数量
     */
    public int getIncount() {
        return incount;
    }

    /**
     * 设置内机数量
     * @param incount 内机数量
     */
    public void setIncount(int incount) {
        this.incount = incount;
    }

    /**
     * 产品存储数据定义
     *
     * @author bipolor
     */
    public static abstract class ProductEntry implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = "NULL";
        public static final String TABLE_NAME = "tb_product";
        public static final String COLUMN_NAME_SERVERPRODUCTID = "serverproductid";//服务端产品Id
        public static final String COLUMN_NAME_PRODUCTNAME = "productname";// 产品名称
        public static final String COLUMN_NAME_MINMODEL = "minmodel";// 内机型号
        public static final String COLUMN_NAME_MINSERNO = "minserno";//内机号
        public static final String COLUMN_NAME_MOUTMODEL = "moutmodel";// 外机型号
        public static final String COLUMN_NAME_MOUTSERNO = "moutserno";//外机号

    }

    public boolean Compare(ProductBO product){
        if(this == product){
            return true;
        }
        return this.serverProductId == product.getServerProductId();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj instanceof ProductBO) {
            ProductBO product = (ProductBO)obj;
            if (this == product) {
                return true;
            }
            return this.serverProductId == product.getServerProductId();
        }
        return false;
    }
}
