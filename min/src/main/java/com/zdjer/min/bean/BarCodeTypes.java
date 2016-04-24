package com.zdjer.min.bean;

/**
 * Created by zdj on 16/1/9.
 */
public enum BarCodeTypes {
    /**
     * 内机
     */
    in(1),

    /**
     * 外机
     */
    out(2);



    private static final String IN = "IN";
    private static final String OUT = "OUT";

    private int value;// 值

    /**
     * 构造函数
     *
     * @param value
     *            值
     */
    BarCodeTypes(int value) {
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
    public static BarCodeTypes value(int value) {
        for (BarCodeTypes mrecordType : BarCodeTypes.values()) {
            if (mrecordType.getValue() == value) {
                return mrecordType;
            }
        }
        return null;
    }

    /**
     * 获得枚举的字符串
     *
     * @param barCodeType
     * @return 枚举的字符串
     */
    public static String valueString(BarCodeTypes barCodeType) {
        if (barCodeType == BarCodeTypes.in) {
            return IN;
        } else if (barCodeType == BarCodeTypes.out) {
            return OUT;
        }
        return null;
    }
}