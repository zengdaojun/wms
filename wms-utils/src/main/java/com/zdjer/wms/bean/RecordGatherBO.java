package com.zdjer.wms.bean;

/**
 * Created by zdj on 16/4/19.
 */

import android.provider.BaseColumns;

/**
 * 记录汇总
 */
public class RecordGatherBO {

    private String serNo = "";  //序列号
    private int count = 0;  //数量

    /**
     * 获取序列号
     * @return
     */
    public String getSerNo() {
        return serNo;
    }

    /**
     * 设置序列号
     * @param serNo
     */
    public void setSerNo(String serNo) {
        this.serNo = serNo;
    }

    /**
     * 获得数量
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置数量
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 记录存储数据定义
     *
     * @author bipolor
     */
    public static abstract class RecordGatherEntry implements BaseColumns {
        public static final String COLUMN_NAME_SERNO = "serNo";
        public static final String COLUMN_NAME_COUNT = "count";
    }
}
