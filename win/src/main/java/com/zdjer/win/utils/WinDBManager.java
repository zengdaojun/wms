package com.zdjer.win.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zdjer.win.bean.RecordBO.RecordEntry;
import com.zdjer.win.bean.TransportBO.TransportEntry;
import com.zdjer.wms.utils.DBManager;

import java.io.File;
import java.util.List;

/**
 * DB管理类
 *
 * @author bipolor
 */
public class WinDBManager extends DBManager {

    //private static final String TEXT_TYPE = " TEXT";// 文本类型
    //private static final String DATE_TYPE = " DATE";// 日期类型
    //private static final String INTEGER_TYPE = " INTEGER";// INTEGER类型
    //private static final String COMMA_SEP = ",";

    //private static final String FirstDir = "windata";
    //private static final String DATABASENAME = "win.db";

    /******************************
     * 用户表
     *********************************//*
    // 用户表删除脚本
    private static final String SQL_DELETE_USER = "DROP TABLE IF EXISTS "
            + UserEntry.TABLE_NAME;
    // 用户表创建脚本
    private static final String SQL_CREATE_USER = "CREATE TABLE " + UserEntry.TABLE_NAME + "("
            + UserEntry._ID + " INTEGER PRIMARY KEY,"
            + UserEntry.COLUMN_NAME_UID + TEXT_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_PWD + TEXT_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_TOKEN + TEXT_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_CREATEDATE + DATE_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_EDITDATE + DATE_TYPE + COMMA_SEP
            + UserEntry.COLUMN_NAME_ISADMIN + INTEGER_TYPE + ");";

    // 用户初始化数据
    private static final String SQL_INIT_USER = "INSERT INTO "
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
            + "'" + YesNos.Yes.getValue() + "');";*/

    /******************************
     * 仓库记录表
     ******************************/
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
            + RecordEntry.COLUMN_NAME_ISUPLOAD + INTEGER_TYPE + ")";

    /******************************
     * 选项表
     ******************************//*
    // 选项表删除脚本
    private static final String SQL_DELETE_OPTION = "DROP TABLE IF EXISTS "
            + OptionEntry.TABLE_NAME;
    // 选项表创建脚本
    private static final String SQL_CREATE_OPTION = "CREATE TABLE " + OptionEntry.TABLE_NAME + "("
            + OptionEntry._ID + " INTEGER PRIMARY KEY,"
            + OptionEntry.COLUMN_NAME_OPTIONTYPE + INTEGER_TYPE + COMMA_SEP
            + OptionEntry.COLUMN_NAME_OPTIONVALUE + TEXT_TYPE + ")";
    // 选项表初始化
    //1 设备编号
    private static final String SQL_INIT_OPTION1 = "INSERT INTO "
            + OptionEntry.TABLE_NAME + "(" + OptionEntry.COLUMN_NAME_OPTIONTYPE + COMMA_SEP
            + OptionEntry.COLUMN_NAME_OPTIONVALUE + ") VALUES ("
            + "'" + OptionTypes.DeviceNum.getValue() + "'" + COMMA_SEP + "'');";
    //2 服务器
    private static final String SQL_INIT_OPTION2 = "INSERT INTO "
            + OptionEntry.TABLE_NAME + "(" + OptionEntry.COLUMN_NAME_OPTIONTYPE + COMMA_SEP
            + OptionEntry.COLUMN_NAME_OPTIONVALUE + ") VALUES ("
            + "'" + OptionTypes.Server.getValue() + "'" + COMMA_SEP
            + "'-1');";

    *//******************************
     * 基础数据表
     ******************************//*
    // 基础数据表删除脚本
    private static final String SQL_DELETE_DATAITEM = "DROP TABLE IF EXISTS "
            + DataItemEntry.TABLE_NAME;

    // 基础数据表创建脚本
    private static final String SQL_CREATE_DATAITEM = "CREATE TABLE "
            + DataItemEntry.TABLE_NAME + "("
            + DataItemEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
            + DataItemEntry.COLUMN_NAME_DATAID + INTEGER_TYPE + COMMA_SEP
            + DataItemEntry.COLUMN_NAME_DATAVALUE + TEXT_TYPE + COMMA_SEP
            + DataItemEntry.COLUMN_NAME_DATATYPE + INTEGER_TYPE + COMMA_SEP
            + DataItemEntry.COLUMN_NAME_PARENTID + INTEGER_TYPE + ")";*/

    /******************************
     * 运输表
     ******************************/
    // 运输表删除脚本
    private static final String SQL_DELETE_TRANSPORT = "DROP TABLE IF EXISTS "
            + TransportEntry.TABLE_NAME;

    // 运输表创建脚本
    private static final String SQL_CREATE_TRANSPORT = "CREATE TABLE "
            + TransportEntry.TABLE_NAME + "("
            + TransportEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
            + TransportEntry.COLUMN_NAME_TRANID + INTEGER_TYPE + COMMA_SEP
            + TransportEntry.COLUMN_NAME_WULIUID + INTEGER_TYPE + COMMA_SEP
            + TransportEntry.COLUMN_NAME_WULIU + TEXT_TYPE + COMMA_SEP
            + TransportEntry.COLUMN_NAME_CARNUMID + INTEGER_TYPE + COMMA_SEP
            + TransportEntry.COLUMN_NAME_CARNUM + TEXT_TYPE + COMMA_SEP
            + TransportEntry.COLUMN_NAME_DRIVERID + INTEGER_TYPE + COMMA_SEP
            + TransportEntry.COLUMN_NAME_DRIVER + TEXT_TYPE + COMMA_SEP
            + TransportEntry.COLUMN_NAME_SENDDATE + DATE_TYPE + ")";

    // 数据库名（含有路径）getExternalStorageDirectory
    /*public static final String DATABASE_NAME = PathHelper.GetSDRootPath()
            + File.separator + FirstDir + File.separator + "win.db";
*/
    /**
     * 构造函数
     */
    public WinDBManager() {
    }

    /**
     * 创建数据库
     *
     * @param context 上下文
     */
    public static void createDatbase(Context context) {
        try {
            /*File file = new File(DATABASE_NAME);// 判断数据库路径是否存在
            if (!file.getParentFile().exists()) {// 不存在
                file.getParentFile().mkdirs();// 创建
            }*/

            FirstDir = "windata";

            DATABASE_NAME = com.zdjer.utils.PathHelper.GetSDRootPath()
                    + File.separator + FirstDir + File.separator + "win.db";

            File file = new File(DATABASE_NAME);// 判断数据库路径是否存在
            if (!file.getParentFile().exists()) {// 不存在
                file.getParentFile().mkdirs();// 创建
            }

            List<String> lstCreateScript = DBManager.getCreateScript();

            /*lstCreateScript.add(SQL_DELETE_USER);//用户表
            lstCreateScript.add(SQL_CREATE_USER);//用户表
            lstCreateScript.add(SQL_INIT_USER);//用户初始化*/
            lstCreateScript.add(SQL_DELETE_RECORD);//记录表
            lstCreateScript.add(SQL_CREATE_RECORD);//记录表
            /*lstCreateScript.add(SQL_DELETE_OPTION);//选项表
            lstCreateScript.add(SQL_CREATE_OPTION);//选项表
            lstCreateScript.add(SQL_INIT_OPTION1);//选项表初始化
            lstCreateScript.add(SQL_INIT_OPTION2);//选项表初始化
            lstCreateScript.add(SQL_DELETE_DATAITEM);//数据项表
            lstCreateScript.add(SQL_CREATE_DATAITEM);//数据项表*/
            lstCreateScript.add(SQL_DELETE_TRANSPORT);//运输表
            lstCreateScript.add(SQL_CREATE_TRANSPORT);//运输表


            SQLiteOpenHelper sqliteOpenHelper = new SQLiteDBHelper(context,
                    DATABASE_NAME, lstCreateScript);
            SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();
            if (db == null) {
                Log.e("DBManager", DATABASE_NAME + "创建不成功！");
            } else {
                db.close();
                Log.e("DBManager", DATABASE_NAME + "创建成功！");
            }
            Log.i("DBManager", "设备初始化成功！");
        } catch (Exception e) {
            Log.e("DBManager", DATABASE_NAME + "创建不成功！");
        }
    }

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


