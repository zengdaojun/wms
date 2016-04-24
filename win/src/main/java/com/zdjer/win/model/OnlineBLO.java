package com.zdjer.win.model;

import com.zdjer.wms.model.DataItemBLO;

/**
 * 业务类：在线
 *
 * @author bipolor
 */
public class OnlineBLO {

    /**
     * 获得令牌
     *
     * @return
     */
    public String getToken(String uid, String pwd) {
        return "10716-10717-f379eaf3c831b04de153469d1bec345e";
    }

    /**
     * 是否连接
     *
     * @return
     */
    public String isConnect1(String url) {
        return null;
    }

    /**
     * 同步数据
     *
     * @return
     */
    public void syncData(String ip, String token) throws Exception {

        //1.基础数据
        DataItemBLO dataItemBlo = new DataItemBLO();
        dataItemBlo.downloadBaseDataToLocal(ip, token);

        //2.记录数据
        RecordBLO recordBlo = new RecordBLO();
        recordBlo.syncRecords(ip, token);
    }
}
