package com.zdjer.wms.bean;

import android.provider.BaseColumns;

import com.zdjer.wms.bean.core.YesNos;
import com.zdjer.utils.DateHelper;

import java.util.Date;

/**
 * 实体类：用户
 * 
 * @author bipolor
 * 
 */
public class UserBO {
	private long userId = 0;// Id
	private String uid = "";// 账号
	private String pwd = "";// 密码
	private String token="";//令牌
	private Date createDate = null;// 创建时间
	private Date editDate = null;// 编辑时间
	private YesNos isAdmin=YesNos.No;//是否为管理员

	/**
	 * 构造函数
	 */
	public UserBO() {
	}

	/**
	 * 获得用户Id
	 * 
	 * @return 用户Id
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * 设置用户Id
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * 获得账号
	 * 
	 * @return 账号
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * 设置账号
	 * 
	 * @param uid 账号
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * 获取密码
	 * 
	 * @return 密码
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * 设置密码
	 * 
	 * @param pwd 密码
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * 获取令牌
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * 设置令牌
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public Date getCreateDate() {
		if (createDate == null) {
			createDate = DateHelper.getCurrDate();
		}
		return createDate;
	}

	/**
	 * 设置创建时间
	 * 
	 * @param createDate 创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取编辑用户
	 * 
	 * @return 编辑用户
	 */
	public Date getEditDate() {
		if (editDate == null) {
			editDate = DateHelper.getCurrDate();
		}
		return editDate;
	}

	/**
	 * 设置编辑时间
	 * 
	 * @param editDate 编辑时间
	 */
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	
	/**
	 * 获取是否是管理员
	 * @return 是否是管理员 
	 */
	public YesNos getIsAdmin() {
		return isAdmin;
	}

	/**
	 * 设置是否是管理员
	 * @param isAdmin
	 */
	public void setIsAdmin(YesNos isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * 用户数据存储定义
	 * @author bipolor
	 *
	 */
	public static abstract class UserEntry implements BaseColumns {
		public static final String COLUMN_NAME_NULLABLE = null;
		public static final String TABLE_NAME = "tb_user";//表名
		public static final String COLUMN_NAME_UID = "uid";// 账号
		public static final String COLUMN_NAME_PWD = "pwd";// 密码
		public static final String COLUMN_NAME_TOKEN="token";//令牌
		public static final String COLUMN_NAME_CREATEDATE = "createDate";// 创建日期
		public static final String COLUMN_NAME_EDITDATE = "editDate";// 编辑日期
		public static final String COLUMN_NAME_ISADMIN="isAdmin";//是否是管理员

	}
}
