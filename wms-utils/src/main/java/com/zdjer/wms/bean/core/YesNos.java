package com.zdjer.wms.bean.core;

/**
 * Created by zdj on 16/1/9.
 */
public enum YesNos {
    /*
     * 是
     */
    Yes(1),

    /*
     * 否
     */
    No(2);

    private int value;//值

    /**
     * 构造函数
     * @param value 值
     */
    YesNos(int value) {
        this.value = value;
    }

    /**
     * 获取值
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * 通过值获得记录类型枚举
     * @param value 值
     * @return 记录类型枚举
     */
    public static YesNos value(int value) {
        for (YesNos yesNo : YesNos.values()) {
            if (yesNo.getValue() == value) {
                return yesNo;
            }
        }
        return null;
    }
}
