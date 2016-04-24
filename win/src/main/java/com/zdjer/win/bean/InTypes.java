package com.zdjer.win.bean;

/**
 * Created by zdj on 16/1/9.
 */
public enum InTypes {
    /**
     * 中心收货
     */
    shouhuo_zx(0),

    /**
     * 网点收货
     */
    shouhuo_wd(1),

    /**
     * 直发
     */
    zhifa(2),

    /**
     * 借货
     */
    jiehuo(3),

    /**
     * 初始
     */
    chushi(4);

    private int value;// 值

    /**
     * 构造函数
     *
     * @param value
     *            值
     */
    InTypes(int value) {
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
    public static InTypes value(int value) {
        for (InTypes inType : InTypes.values()) {
            if (inType.getValue() == value) {
                return inType;
            }
        }
        return null;
    }
}