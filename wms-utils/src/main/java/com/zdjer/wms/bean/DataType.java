package com.zdjer.wms.bean;

/**
 * 数据类型
 *
 * @author bipolor
 *
 */
public enum DataType {
    /**
     * 仓库货位
     */
    wareHouse(1),

    /**
     * 物流公司
     */
    wuLiu(2),

    /**
     * 司机
     */
    driver(3),

    /**
     * 经销商
     */
    jxsNum(4),

    /**
     * 车牌号
     */
    carNum(5),

    /**
     *工人师傅
     */
    logistPerson(6);

    private int value;// 值

    /**
     * 构造函数
     *
     * @param value
     *            值
     */
    DataType(int value) {
        this.value = value;
    }

    /**
     * 获取值
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * 通过值获得记录类型枚举
     *
     * @param value
     *            值
     * @return 记录类型枚举
     */
    public static DataType value(int value) {
        for (DataType dataType : DataType.values()) {
            if (dataType.getValue() == value) {
                return dataType;
            }
        }
        return null;
    }
}
