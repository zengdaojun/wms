package com.zdjer.min.bean;

/**
 * Created by zdj on 16/1/9.
 */
public enum MRecordType {

    /**
     * 无
     */
    none(0),

    /**
     * 入库
     */
    in(1),

    /**
     * 出库
     */
    out(2),

    /**
     * 盘点
     */
    check(3);



    private static final String IN = "IN";
    private static final String OUT = "OUT";
    private static final String CHECK = "CHECK";

    private int value;// 值

    /**
     * 构造函数
     *
     * @param value
     *            值
     */
    MRecordType(int value) {
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
    public static MRecordType value(int value) {
        for (MRecordType mrecordType : MRecordType.values()) {
            if (mrecordType.getValue() == value) {
                return mrecordType;
            }
        }
        return null;
    }

    /**
     * 获得枚举的字符串
     *
     * @param mrecordType
     * @return 枚举的字符串
     */
    public static String valueString(MRecordType mrecordType) {
        if (mrecordType == MRecordType.in) {
            return IN;
        } else if (mrecordType == MRecordType.out) {
            return OUT;
        } else if(mrecordType == MRecordType.check){
            return CHECK;
        }
        return null;
    }
}