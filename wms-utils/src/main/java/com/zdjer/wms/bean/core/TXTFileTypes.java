package com.zdjer.wms.bean.core;

/**
 * Created by zdj on 16/3/8.
 */

/**
 * @author bipolor
 */
public enum TXTFileTypes {
    /*
     * 入库记录
     */
    yy(1),

    /*
     * 出库记录
     */
    ks(2);

    private static final String YY = "YY";// 带有库位信息
    private static final String KS = "KS";// 不带库位信息

    private int value;// 值

    /**
     * 构造函数
     *
     * @param value 值
     */
    TXTFileTypes(int value) {
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
     * @param value 值
     * @return 记录类型枚举
     */
    public static TXTFileTypes value(int value) {
        for (TXTFileTypes txtFileType : TXTFileTypes.values()) {
            if (txtFileType.getValue() == value) {
                return txtFileType;
            }
        }
        return null;
    }

    /**
     * 获得枚举的字符串
     *
     * @param txtFileType
     * @return 枚举的字符串
     */
    public static String valueSting(TXTFileTypes txtFileType) {
        if (txtFileType == TXTFileTypes.yy) {
            return YY;
        } else if (txtFileType == TXTFileTypes.ks) {
            return KS;
        }
        return null;
    }
}
