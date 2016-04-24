package com.zdjer.win.view.core;

/**
 * Created by zdj on 16/1/9.
 */
public enum ScanTypes {
    /*
     * 是
     */
    Single(1),

    /*
     * 否
     */
    Mutil(2);

    private int value;//值

    /**
     * 构造函数
     * @param value 值
     */
    ScanTypes(int value) {
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
    public static ScanTypes value(int value) {
        for (ScanTypes scanType : ScanTypes.values()) {
            if (scanType.getValue() == value) {
                return scanType;
            }
        }
        return null;
    }
}
