package com.zdjer.wms.utils;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zdjer.utils.DateHelper;
import com.zdjer.wms.bean.DataItemBO.DataItemEntry;
import com.zdjer.wms.bean.OptionBO.OptionEntry;
import com.zdjer.wms.bean.ProductBO;
import com.zdjer.wms.bean.ProductBO.ProductEntry;
import com.zdjer.wms.bean.UserBO.UserEntry;
import com.zdjer.wms.bean.core.OptionTypes;
import com.zdjer.wms.bean.core.YesNos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * DB管理类
 *
 * @author bipolor
 */
public abstract class DBManager {

    protected static final String TEXT_TYPE = " TEXT";// 文本类型
    protected static final String DATE_TYPE = " DATE";// 日期类型
    protected static final String INTEGER_TYPE = " INTEGER";// INTEGER类型
    protected static final String COMMA_SEP = ",";

    protected static String FirstDir = "wmsdata";

    /******************************
     * 用户表
     *********************************/
    // 用户表删除脚本
    protected static final String SQL_DELETE_USER = "DROP TABLE IF EXISTS "
            + UserEntry.TABLE_NAME;
    // 用户表创建脚本
    protected static final String SQL_CREATE_USER = "CREATE TABLE " + UserEntry.TABLE_NAME + "("
            + UserEntry._ID + " INTEGER PRIMARY KEY,"
            + UserEntry.COLUMN_NAME_UID + TEXT_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_PWD + TEXT_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_TOKEN + TEXT_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_CREATEDATE + DATE_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_EDITDATE + DATE_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_ISADMIN + INTEGER_TYPE + ");";

    // 用户初始化数据
    protected static final String SQL_INIT_USER = "INSERT INTO "
            + UserEntry.TABLE_NAME + "("
            + UserEntry.COLUMN_NAME_UID + COMMA_SEP
            + UserEntry.COLUMN_NAME_PWD + COMMA_SEP
            + UserEntry.COLUMN_NAME_TOKEN + COMMA_SEP
            + UserEntry.COLUMN_NAME_CREATEDATE + COMMA_SEP
            + UserEntry.COLUMN_NAME_EDITDATE + COMMA_SEP
            + UserEntry.COLUMN_NAME_ISADMIN + ") VALUES ("
            + "'ckgl'" + COMMA_SEP
            + "'" + MD5Helper.ConvertToMD5("ckgl&lgkc") + "'" + COMMA_SEP
            + "''" + COMMA_SEP
            + "'" + DateHelper.getCurrDateString() + "'" + COMMA_SEP
            + "'" + DateHelper.getCurrDateString() + "'" + COMMA_SEP
            + "'" + YesNos.Yes.getValue() + "');";

    /******************************
     * 仓库记录表
     ******************************//*
    // 记录表删除脚本
    private static final String SQL_DELETE_RECORD = "DROP TABLE IF EXISTS "
            + RecordEntry.TABLE_NAME;

    // 记录表创建脚本
    private static final String SQL_CREATE_RECORD = "CREATE TABLE " + RecordEntry.TABLE_NAME + "("
            + RecordEntry._ID + " INTEGER PRIMARY KEY,"
            + RecordEntry.COLUMN_NAME_BARCODE + TEXT_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_THDNUM + TEXT_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_IOTYPE + INTEGER_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_DBDNUM + TEXT_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_JXSNUMID + INTEGER_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_JXSNUM + TEXT_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_WAREHOUSEID + INTEGER_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_WAREHOUSE + TEXT_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_CREATEUSERID + INTEGER_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_CREATEUSERUID + TEXT_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_CREATEDATE + DATE_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_RECORDTYPE + INTEGER_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_TRANID + INTEGER_TYPE + COMMA_SEP
            + RecordEntry.COLUMN_NAME_ISUPLOAD + INTEGER_TYPE + ")";*/

    /******************************
     * 选项表
     ******************************/
    // 选项表删除脚本
    protected static final String SQL_DELETE_OPTION = "DROP TABLE IF EXISTS "
            + OptionEntry.TABLE_NAME;
    // 选项表创建脚本
    protected static final String SQL_CREATE_OPTION = "CREATE TABLE " + OptionEntry.TABLE_NAME + "("
            + OptionEntry._ID + " INTEGER PRIMARY KEY,"
            + OptionEntry.COLUMN_NAME_OPTIONTYPE + INTEGER_TYPE + COMMA_SEP
            + OptionEntry.COLUMN_NAME_OPTIONVALUE + TEXT_TYPE + ")";
    // 选项表初始化
    //1 设备编号
    protected static final String SQL_INIT_OPTION1 = "INSERT INTO "
            + OptionEntry.TABLE_NAME + "(" + OptionEntry.COLUMN_NAME_OPTIONTYPE + COMMA_SEP
            + OptionEntry.COLUMN_NAME_OPTIONVALUE + ") VALUES ("
            + "'" + OptionTypes.DeviceNum.getValue() + "'" + COMMA_SEP + "'');";
    //2 服务器
    protected static final String SQL_INIT_OPTION2 = "INSERT INTO "
            + OptionEntry.TABLE_NAME + "(" + OptionEntry.COLUMN_NAME_OPTIONTYPE + COMMA_SEP
            + OptionEntry.COLUMN_NAME_OPTIONVALUE + ") VALUES ("
            + "'" + OptionTypes.Server.getValue() + "'" + COMMA_SEP
            + "'-1');";

    /******************************
     * 基础数据表
     ******************************/
    // 基础数据表删除脚本
    protected static final String SQL_DELETE_DATAITEM = "DROP TABLE IF EXISTS "
            + DataItemEntry.TABLE_NAME;

    // 基础数据表创建脚本
    protected static final String SQL_CREATE_DATAITEM = "CREATE TABLE "
            + DataItemEntry.TABLE_NAME + "("
            + DataItemEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
            + DataItemEntry.COLUMN_NAME_DATAID + INTEGER_TYPE + COMMA_SEP
            + DataItemEntry.COLUMN_NAME_DATAVALUE + TEXT_TYPE + COMMA_SEP
            + DataItemEntry.COLUMN_NAME_DATATYPE + INTEGER_TYPE + COMMA_SEP
            + DataItemEntry.COLUMN_NAME_PARENTID + INTEGER_TYPE + ")";

    /******************************
     * 产品表
     ******************************/
    // 产品表删除脚本
    private static final String SQL_DELETE_PRODUCT = "DROP TABLE IF EXISTS "
            + ProductBO.ProductEntry.TABLE_NAME;

    // 产品表创建脚本
    private static final String SQL_CREATE_PRODUCT = "CREATE TABLE "
            + ProductEntry.TABLE_NAME + "("
            + ProductEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
            + ProductEntry.COLUMN_NAME_SERVERPRODUCTID + INTEGER_TYPE + COMMA_SEP
            + ProductEntry.COLUMN_NAME_PRODUCTNAME + TEXT_TYPE + COMMA_SEP
            + ProductEntry.COLUMN_NAME_MINMODEL + TEXT_TYPE + COMMA_SEP
            + ProductEntry.COLUMN_NAME_MINSERNO + TEXT_TYPE + COMMA_SEP
            + ProductEntry.COLUMN_NAME_MOUTMODEL + TEXT_TYPE + COMMA_SEP
            + ProductEntry.COLUMN_NAME_MOUTSERNO + TEXT_TYPE + ")";


    // 数据库名（含有路径）getExternalStorageDirectory
    public static String DATABASE_NAME = PathHelper.GetSDRootPath()
            + File.separator + FirstDir + File.separator + "wms.db";

    /*public static String getDataBasePath() {
        if(dataBaseName.length()==0){
            dataBaseName=PathHelper.GetSDRootPath()
                    + File.separator + FirstDir + File.separator + "wms.db";
        }
        return dataBaseName;
    }
*/
    protected static List<String> getCreateScript(){
        List<String> lstCreateScript = new ArrayList<String>();
        lstCreateScript.add(SQL_DELETE_USER);//用户表
        lstCreateScript.add(SQL_CREATE_USER);//用户表
        lstCreateScript.add(SQL_INIT_USER);//用户初始化
        //lstCreateScript.add(SQL_DELETE_RECORD);//记录表
        //lstCreateScript.add(SQL_CREATE_RECORD);//记录表
        lstCreateScript.add(SQL_DELETE_OPTION);//选项表
        lstCreateScript.add(SQL_CREATE_OPTION);//选项表
        lstCreateScript.add(SQL_INIT_OPTION1);//选项表初始化
        lstCreateScript.add(SQL_INIT_OPTION2);//选项表初始化
        lstCreateScript.add(SQL_DELETE_DATAITEM);//数据项表
        lstCreateScript.add(SQL_CREATE_DATAITEM);//数据项表
        lstCreateScript.add(SQL_DELETE_PRODUCT);//产品表
        lstCreateScript.add(SQL_CREATE_PRODUCT);//产品表
        //lstCreateScript.add(SQL_DELETE_TRANSPORT);//运输表
        //lstCreateScript.add(SQL_CREATE_TRANSPORT);//运输表
        return lstCreateScript;
    }

    /**
     * 创建数据库
     *
     * @param context 上下文
     */
    /*public static void createDatbase(Context context) {
        try {
            List<String> lstCreateScript = getCreateScript();


            SQLiteOpenHelper sqliteOpenHelper = new SQLiteDBHelper(context,
                    dataBaseName, lstCreateScript);
            SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
            if (db == null) {
                Log.e("DBManager", dataBaseName + "创建不成功！");
            } else {
                db.close();
                Log.e("DBManager", dataBaseName + "创建成功！");
            }
            Log.i("DBManager", "设备初始化成功！");
        } catch (Exception e) {
            Log.e("DBManager", dataBaseName + "创建不成功！");
        }
    }
*/
    /**
     * 创建数据库
     *
     * @param context 上下文
     */
    /*public static void createDatbase(Context context,String dbName) {
        try {
            *//*File file = new File(DATABASE_NAME);// 判断数据库路径是否存在
            if (!file.getParentFile().exists()) {// 不存在
                file.getParentFile().mkdirs();// 创建
            }*//*
            dataBaseName=PathHelper.GetSDRootPath()
                    + File.separator + FirstDir + File.separator + dbName;

            List<String> lstCreateScript = new ArrayList<String>();
            lstCreateScript.add(SQL_DELETE_USER);//用户表
            lstCreateScript.add(SQL_CREATE_USER);//用户表
            lstCreateScript.add(SQL_INIT_USER);//用户初始化
            //lstCreateScript.add(SQL_DELETE_RECORD);//记录表
            //lstCreateScript.add(SQL_CREATE_RECORD);//记录表
            lstCreateScript.add(SQL_DELETE_OPTION);//选项表
            lstCreateScript.add(SQL_CREATE_OPTION);//选项表
            lstCreateScript.add(SQL_INIT_OPTION1);//选项表初始化
            lstCreateScript.add(SQL_INIT_OPTION2);//选项表初始化
            lstCreateScript.add(SQL_DELETE_DATAITEM);//数据项表
            lstCreateScript.add(SQL_CREATE_DATAITEM);//数据项表
            //lstCreateScript.add(SQL_DELETE_TRANSPORT);//运输表
            //lstCreateScript.add(SQL_CREATE_TRANSPORT);//运输表


            SQLiteOpenHelper sqliteOpenHelper = new SQLiteDBHelper(context,
                    dataBaseName, lstCreateScript);
            SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
            if (db == null) {
                Log.e("DBManager", dataBaseName + "创建不成功！");
            } else {
                db.close();
                Log.e("DBManager", dataBaseName + "创建成功！");
            }
            Log.i("DBManager", "设备初始化成功！");
        } catch (Exception e) {
            Log.e("DBManager", dataBaseName + "创建不成功！");
        }
    }*/

    /**
     * 获取数据库
     *
     * @return 数据库对象
     */
    public static SQLiteDatabase getDatabase() {
        File file = new File(DATABASE_NAME);
        SQLiteDatabase db = null;
        if (file.exists()) {
            db = SQLiteDatabase.openOrCreateDatabase(file, null);
            Log.i("DBManager", "数据库获取成功！");
        }
        return db;
    }
}


