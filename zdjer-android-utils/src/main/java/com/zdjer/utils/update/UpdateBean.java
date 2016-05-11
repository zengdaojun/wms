package com.zdjer.utils.update;

/**
 * Created by zdj on 16/4/28.
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 更新实体类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年11月10日 下午12:56:27
 */
@SuppressWarnings("serial")
@XStreamAlias("update")
public class UpdateBean implements Serializable {

    @XStreamAlias("android")
    private AndroidBean androidBean;

    /**
     * 获得Android更新信息类
     * @return
     */
    public AndroidBean getAndroidBean() {
        return androidBean;
    }

    /**
     * 设置Android更新信息类
     * @param androidBean
     */
    public void setAndroidBean(AndroidBean androidBean) {
        this.androidBean = androidBean;
    }
}