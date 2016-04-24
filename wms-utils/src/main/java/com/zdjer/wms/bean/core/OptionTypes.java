package com.zdjer.wms.bean.core;

/**
 * 记录类型
 *
 * @author bipolor
 *
 */
public enum OptionTypes {
    /**
     * 设备编号
     */
    DeviceNum(1),

    /**
     * 本地
     */
    Local(2),

    /**
     * 服务器
     */
    Server1(3),

    /**
     * 服务器
     */
    Server2(4),

    /**
     * 服务器
     */
    Server(5);

    /**
     * 同步用户数据
     */
    //SyncUserData(3),

    /**
     * 同步基础数据
     */
    //SyncBaseData(3),

    /**
     * 是否使用运输
     */
    //ShowTransport(3);


    private int value;// 值

    /**
     * 构造函数
     *
     * @param value
     *            值
     */
    OptionTypes(int value) {
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
    public static OptionTypes value(int value) {
        for (OptionTypes optionType : OptionTypes.values()) {
            if (optionType.getValue() == value) {
                return optionType;
            }
        }
        return null;
    }
}
