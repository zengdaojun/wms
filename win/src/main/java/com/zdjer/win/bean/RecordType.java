package com.zdjer.win.bean;

/**
 * Created by zdj on 16/3/8.
 */
/**
 * 记录类型
 *
 * @author bipolor
 *
 */
public enum RecordType {
    /*
     * 入库记录
     */
    in(1),

    /*
     * 出库记录
     */
    out(2),

    /**
     * 盘点记录
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
    RecordType(int value) {
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
    public static RecordType value(int value) {
        for (RecordType recordType : RecordType.values()) {
            if (recordType.getValue() == value) {
                return recordType;
            }
        }
        return null;
    }

    /**
     * 获得枚举的字符串
     *
     * @param recordType
     * @return 枚举的字符串
     */
    public static String valueString(RecordType recordType) {
        if (recordType == RecordType.in) {
            return IN;
        } else if (recordType == RecordType.out) {
            return OUT;
        } else if (recordType == RecordType.check) {
            return CHECK;
        }
        return null;
    }
}
