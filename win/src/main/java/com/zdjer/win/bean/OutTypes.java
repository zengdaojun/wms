package com.zdjer.win.bean;

/**
 * Created by zdj on 16/1/9.
 */
public enum OutTypes {
    /**
     * 零售
     */
    lingshou(0),

    /**
     * 中心送货
     */
    zxsonghuo(1),

    /**
     * 借货
     */
    jiehuo(2),

    /**
     * 网点送货
     */
    wdsonghuo(3);

    private int value;// 值

    /**
     * 构造函数
     *
     * @param value
     *            值
     */
    OutTypes(int value) {
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
    public static OutTypes value(int value) {
        for (OutTypes inType : OutTypes.values()) {
            if (inType.getValue() == value) {
                return inType;
            }
        }
        return null;
    }
}