package com.zdjer.win.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.zdjer.utils.DateHelper;
import com.zdjer.utils.PathHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.win.bean.InTypes;
import com.zdjer.win.bean.OutTypes;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.bean.RecordBO.RecordEntry;
import com.zdjer.win.bean.RecordType;
import com.zdjer.win.bean.TXTFileTypes;
import com.zdjer.win.utils.SyncHelper;
import com.zdjer.win.utils.SyncHelper.SyncAPITypes;
import com.zdjer.win.utils.TxtDBHelper;
import com.zdjer.win.utils.WinDBManager;
import com.zdjer.wms.bean.DataItemBO;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.bean.RecordGatherBO;
import com.zdjer.wms.bean.core.OptionTypes;
import com.zdjer.wms.bean.core.YesNos;
import com.zdjer.wms.model.DataItemBLO;
import com.zdjer.wms.model.OptionBLO;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 业务逻辑：记录
 *
 * @author Administrator
 */
public class RecordBLO {

    private static final String FirstDir = "wmsdata";
    private static final String Dev1 = ".001";
    private static final String DataLinePre = "1111";// 文件中数据行的前4位

    /**
     * 构造函数
     */
    public RecordBLO() {

    }

    /**
     * 添加记录到本地数据库
     *
     * @param isOnline 是否在线
     * @param record   记录
     * @param ip       IP
     * @param token    令牌
     * @param msg      消息
     * @return 添加成功，返回true；反之，返回false！
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    public Boolean addRecord(Boolean isOnline, RecordBO record, String ip, String token, StringBuffer msg) {
        try {
            // 1 转换数据，获得选择的内容的Id
            convertToRecord(record);

            // 2 若允许联网，则上传记录数据
            YesNos isUpload = YesNos.No;
            if (isOnline == true) {
                // 不同的记录类型调用的接口不一样
                if (record.getRecordType() == RecordType.in) {
                    // 入库
                    List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
                    lstNameValue.add(new BasicNameValuePair("token", token));
                    lstNameValue.add(new BasicNameValuePair("barcode", record
                            .getBarCode()));
                    lstNameValue.add(new BasicNameValuePair("shelfId", String
                            .valueOf(record.getWarehouseId())));
                    lstNameValue.add(new BasicNameValuePair("shelfName", record
                            .getWarehouse()));
                    lstNameValue.add(new BasicNameValuePair("intype",
                            String.valueOf(record.getInType().getValue())));
                    lstNameValue.add(new BasicNameValuePair("installTeamId",
                            String.valueOf(record.getJxsNumId())));
                    lstNameValue.add(new BasicNameValuePair("installTeamName",
                            record.getJxsNum()));
                    lstNameValue.add(new BasicNameValuePair("tidanhao", record
                            .getThdNum()));
                    String url = ip + SyncAPITypes.valueSting(SyncAPITypes.addInBarCode);
                    String addInBarCodeResonse = SyncHelper.getResponse(
                            url,
                            lstNameValue);
                    JSONObject jsonAddInBarCode = new JSONObject(
                            addInBarCodeResonse);
                    boolean flag = jsonAddInBarCode.getBoolean("flag");
                    if (flag) {
                        isUpload = YesNos.Yes;
                    } else {
                        msg.append(jsonAddInBarCode.getString("msg"));
                        return false;
                    }
                } else if (record.getRecordType() == RecordType.out) {
                    //
                    List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
                    lstNameValue.add(new BasicNameValuePair("token", token));
                    lstNameValue.add(new BasicNameValuePair("barcode", record
                            .getBarCode()));
                    lstNameValue.add(new BasicNameValuePair("shelfId", String
                            .valueOf(record.getWarehouseId())));
                    lstNameValue.add(new BasicNameValuePair("shelfName", record
                            .getWarehouse()));
                    lstNameValue.add(new BasicNameValuePair("outype",
                            String.valueOf(record.getOutType().getValue())));
                    lstNameValue.add(new BasicNameValuePair("dbno", record
                            .getDbdNum()));
                    lstNameValue.add(new BasicNameValuePair("installTeamId",
                            String.valueOf(record.getJxsNumId())));
                    lstNameValue.add(new BasicNameValuePair("installTeamName",
                            record.getJxsNum()));
                    lstNameValue.add(new BasicNameValuePair("tidanhao", record
                            .getThdNum()));
                    lstNameValue.add(new BasicNameValuePair("logisticsId",
                            String.valueOf(record.getTranId())));

                    String url = ip + SyncAPITypes
                            .valueSting(SyncAPITypes.addOutBarCode);
                    String addOutBarCodeResonse = SyncHelper
                            .getResponse(url,
                                    lstNameValue);
                    JSONObject jsonAddOutBarCode = new JSONObject(
                            addOutBarCodeResonse);
                    boolean flag = jsonAddOutBarCode.getBoolean("flag");
                    if (flag) {
                        isUpload = YesNos.Yes;
                    } else {
                        msg.append(jsonAddOutBarCode.getString("msg"));
                        return false;
                    }
                } else if (record.getRecordType() == RecordType.check) {
                    //
                    List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
                    lstNameValue.add(new BasicNameValuePair("token", token));
                    lstNameValue.add(new BasicNameValuePair("barcode", record
                            .getBarCode()));

                    String url = ip + SyncAPITypes
                            .valueSting(SyncAPITypes.panDian);
                    String addOutBarCodeResonse = SyncHelper
                            .getResponse(url,
                                    lstNameValue);
                    JSONObject jsonAddOutBarCode = new JSONObject(
                            addOutBarCodeResonse);
                    boolean flag = jsonAddOutBarCode.getBoolean("flag");
                    Log.i("pandian", String.valueOf(flag));
                    if (flag) {
                        isUpload = YesNos.Yes;
                    } else {
                        msg.append(jsonAddOutBarCode.getString("msg"));
                        return false;
                    }
                }
            }
            record.setIsUpload(isUpload);
            // 3 将数据保存到本地
            SQLiteDatabase db = WinDBManager.getDatabase();
            ContentValues values = new ContentValues();
            values.put(RecordEntry.COLUMN_NAME_BARCODE, record.getBarCode());
            values.put(RecordEntry.COLUMN_NAME_THDNUM, record.getThdNum());
            if (record.getRecordType() == RecordType.in) {
                values.put(RecordEntry.COLUMN_NAME_IOTYPE,
                        String.valueOf(record.getInType().getValue()));
            } else if (record.getRecordType() == RecordType.out) {
                values.put(RecordEntry.COLUMN_NAME_IOTYPE,
                        String.valueOf(record.getOutType().getValue()));
            }
            values.put(RecordEntry.COLUMN_NAME_DBDNUM, record.getDbdNum());
            values.put(RecordEntry.COLUMN_NAME_JXSNUMID, record.getJxsNumId());
            values.put(RecordEntry.COLUMN_NAME_JXSNUM, record.getJxsNum());
            values.put(RecordEntry.COLUMN_NAME_WAREHOUSEID,
                    record.getWarehouseId());
            values.put(RecordEntry.COLUMN_NAME_WAREHOUSE, record.getWarehouse());
            values.put(RecordEntry.COLUMN_NAME_CREATEUSERID,
                    record.getCreateUserId());
            values.put(RecordEntry.COLUMN_NAME_CREATEUSERUID,
                    record.getCreateUserUID());
            values.put(RecordEntry.COLUMN_NAME_CREATEDATE,
                    DateHelper.getDateString(record.getCreateDate()));
            values.put(RecordEntry.COLUMN_NAME_RECORDTYPE,
                    String.valueOf(record.getRecordType().getValue()));
            values.put(RecordEntry.COLUMN_NAME_TRANID, record.getTranId());
            values.put(RecordEntry.COLUMN_NAME_ISUPLOAD,
                    String.valueOf(record.getIsUpload().getValue()));

            long newRowId = db.insert(RecordEntry.TABLE_NAME,
                    RecordEntry.COLUMN_NAME_NULLABLE, values);
            db.close();
            if (newRowId == -1) {
                Log.i("RecordBLO addRecord", "faild!");
                return false;
            } else {
                Log.i("RecordBLO addRecord", "success!");
                return true;
            }
        } catch (Exception e) {
            Log.i("RecordBLO addRecord exception", e.getMessage());
        }
        return false;
    }

    /**
     * 添加记录到本地数据库
     *
     * @param record 记录
     * @return 添加成功，返回true；反之，返回false！
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    public Boolean addRecord(RecordBO record) {
        // 3 将数据保存到本地
        SQLiteDatabase sqliteDatabase = WinDBManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_NAME_BARCODE, record.getBarCode());
        values.put(RecordEntry.COLUMN_NAME_THDNUM, record.getThdNum());
        if (record.getRecordType() == RecordType.in) {
            values.put(RecordEntry.COLUMN_NAME_IOTYPE,
                    String.valueOf(record.getInType().getValue()));
        } else if (record.getRecordType() == RecordType.out) {
            values.put(RecordEntry.COLUMN_NAME_IOTYPE,
                    String.valueOf(record.getOutType().getValue()));
        }
        values.put(RecordEntry.COLUMN_NAME_DBDNUM, record.getDbdNum());
        values.put(RecordEntry.COLUMN_NAME_JXSNUMID, record.getJxsNumId());
        values.put(RecordEntry.COLUMN_NAME_JXSNUM, record.getJxsNum());
        values.put(RecordEntry.COLUMN_NAME_WAREHOUSEID,
                record.getWarehouseId());
        values.put(RecordEntry.COLUMN_NAME_WAREHOUSE, record.getWarehouse());
        values.put(RecordEntry.COLUMN_NAME_CREATEUSERID,
                record.getCreateUserId());
        values.put(RecordEntry.COLUMN_NAME_CREATEUSERUID,
                record.getCreateUserUID());
        values.put(RecordEntry.COLUMN_NAME_CREATEDATE,
                DateHelper.getDateString(record.getCreateDate()));
        values.put(RecordEntry.COLUMN_NAME_RECORDTYPE,
                String.valueOf(record.getRecordType().getValue()));
        values.put(RecordEntry.COLUMN_NAME_TRANID, record.getTranId());
        values.put(RecordEntry.COLUMN_NAME_ISUPLOAD,
                String.valueOf(record.getIsUpload().getValue()));

        long newRowId = sqliteDatabase.insert(RecordEntry.TABLE_NAME,
                RecordEntry.COLUMN_NAME_NULLABLE, values);
        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        if (newRowId == -1) {
            Log.i("RecordBLO addRecord", "faild!");
            return false;
        } else {
            Log.i("RecordBLO addRecord", "success!");
            return true;
        }
    }


    /**
     * 转换记录
     *
     * @param record
     * @return
     */
    public void convertToRecord(RecordBO record) {
        DataItemBLO dataItemBlo = new DataItemBLO();
        // 经销商编号
        if (record.getJxsNum().length() > 0) {
            // 获取该基础数据
            DataItemBO dataItem = dataItemBlo.getDataItem(DataType.jxsNum,
                    record.getJxsNum());
            if (dataItem != null) {
                record.setJxsNumId(dataItem.getDataId());
            }
        }
        // 仓库货位
        if (record.getWarehouse().length() > 0) {
            // 获取该基础数据
            DataItemBO dataItem = dataItemBlo.getDataItem(DataType.wareHouse,
                    record.getWarehouse());
            if (dataItem != null) {
                record.setWarehouseId(dataItem.getDataId());
            }
        }
    }

    /**
     * 删除记录
     *
     * @param isOnline 是否在线
     * @param record   记录对象
     * @param ip       IP
     * @param token    令牌
     * @param msg      错误消息
     * @return 删除成功，返回true；反之，返回false！
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    public Boolean deleteRecord(Boolean isOnline, RecordBO record, String ip, String token, StringBuffer msg) throws Exception {
        // /TODO:需添加联网时的处理
        // 2 若允许联网，则上传记录数据
        if (isOnline == true) {

            // 1 转换数据，获得选择的内容的Id
            convertToRecord(record);

            String type = "";
            List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
            lstNameValue.add(new BasicNameValuePair("token", token));
            lstNameValue.add(new BasicNameValuePair("barcode", record
                    .getBarCode()));
            lstNameValue.add(new BasicNameValuePair("shelfId", String
                    .valueOf(record.getWarehouseId())));
            lstNameValue.add(new BasicNameValuePair("shelfName", record
                    .getWarehouse()));

            if (record.getRecordType() == RecordType.in) {
                type = "0";
                lstNameValue.add(new BasicNameValuePair("intype",
                        String.valueOf(record.getInType().getValue())));
            } else if (record.getRecordType() == RecordType.out) {
                type = "1";
                lstNameValue.add(new BasicNameValuePair("outype",
                        String.valueOf(record.getOutType().getValue())));
            } else if (record.getRecordType() == RecordType.check) {
                type = "2";
            }


            lstNameValue.add(new BasicNameValuePair("installTeamId",
                    String.valueOf(record.getJxsNumId())));
            lstNameValue.add(new BasicNameValuePair("installTeamName",
                    record.getJxsNum()));
            lstNameValue.add(new BasicNameValuePair("tidanhao", record
                    .getThdNum()));
            lstNameValue.add(new BasicNameValuePair("type", type));
            String url = ip + SyncAPITypes.valueSting(SyncAPITypes.deleteBarcode);
            String deleteBarcodeResonse = SyncHelper.getResponse(
                    url, lstNameValue);
            JSONObject jsonDeleteBarcode = new JSONObject(
                    deleteBarcodeResonse);
            boolean flag = jsonDeleteBarcode.getBoolean("flag");
            if (flag == false) {

                msg.append(jsonDeleteBarcode.getString("msg"));
                return false;
            }
        }
        SQLiteDatabase db = WinDBManager.getDatabase();
        String whereCaluse = BaseColumns._ID + " =?";
        String[] whereArgs = new String[]{String.valueOf(record.getRecordId())};
        int affectRow = db.delete(RecordEntry.TABLE_NAME, whereCaluse,
                whereArgs);
        db.close();
        if (affectRow == 0) {
            Log.i("RecordBLO deleteRecordByRecordId", "faild");
            return false;
        } else {
            Log.i("RecordBLO deleteRecordByRecordId", "faild");
            return true;
        }
    }

    /**
     * 删除记录
     *
     * @param isOnline 是否在线
     * @param record   记录对象
     * @param ip       IP
     * @param token    令牌
     * @param msg      错误消息
     * @return 删除成功，返回true；反之，返回false！
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    public Boolean deleteRecord1(Boolean isOnline, RecordBO record, String ip, String token, StringBuffer msg) throws Exception {
        // /TODO:需添加联网时的处理
        // 2 若允许联网，则上传记录数据
        if (isOnline == true) {

            // 1 转换数据，获得选择的内容的Id
            convertToRecord(record);

            String type = "";
            List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
            lstNameValue.add(new BasicNameValuePair("token", token));
            lstNameValue.add(new BasicNameValuePair("barcode", record
                    .getBarCode()));
            lstNameValue.add(new BasicNameValuePair("shelfId", String
                    .valueOf(record.getWarehouseId())));
            lstNameValue.add(new BasicNameValuePair("shelfName", record
                    .getWarehouse()));

            if (record.getRecordType() == RecordType.in) {
                type = "0";
                lstNameValue.add(new BasicNameValuePair("intype",
                        String.valueOf(record.getInType().getValue())));
            } else if (record.getRecordType() == RecordType.out) {
                type = "1";
                lstNameValue.add(new BasicNameValuePair("outype",
                        String.valueOf(record.getOutType().getValue())));
            } else if (record.getRecordType() == RecordType.check) {
                type = "2";
            }


            lstNameValue.add(new BasicNameValuePair("installTeamId",
                    String.valueOf(record.getJxsNumId())));
            lstNameValue.add(new BasicNameValuePair("installTeamName",
                    record.getJxsNum()));
            lstNameValue.add(new BasicNameValuePair("tidanhao", record
                    .getThdNum()));
            lstNameValue.add(new BasicNameValuePair("type", type));
            String url = ip + SyncAPITypes.valueSting(SyncAPITypes.deleteBarcode);
            String deleteBarcodeResonse = SyncHelper.getResponse(
                    url, lstNameValue);
            JSONObject jsonDeleteBarcode = new JSONObject(
                    deleteBarcodeResonse);
            boolean flag = jsonDeleteBarcode.getBoolean("flag");
            if (flag == false) {

                msg.append(jsonDeleteBarcode.getString("msg"));
                return false;
            }
        }
        SQLiteDatabase db = WinDBManager.getDatabase();
        String whereCaluse = BaseColumns._ID + " =?";
        String[] whereArgs = new String[]{String.valueOf(record.getRecordId())};
        int affectRow = db.delete(RecordEntry.TABLE_NAME, whereCaluse,
                whereArgs);
        db.close();
        if (affectRow == 0) {
            Log.i("RecordBLO deleteRecordByRecordId", "faild");
            return false;
        } else {
            Log.i("RecordBLO deleteRecordByRecordId", "faild");
            return true;
        }
    }

    /**
     * 删除记录
     *
     * @param recordId 记录Id
     * @return 删除成功，返回true；反之，返回false！
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    public Boolean deleteRecord(long recordId) {

        SQLiteDatabase sqliteDatabase = WinDBManager.getDatabase();
        String whereCaluse = BaseColumns._ID + " =?";
        String[] whereArgs = new String[]{String.valueOf(recordId)};

        int affectRow = sqliteDatabase.delete(RecordEntry.TABLE_NAME, whereCaluse,
                whereArgs);

        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        if (affectRow == 0) {
            Log.i("RecordBLO deleteRecord", "faild");
            return false;
        } else {
            Log.i("RecordBLO deleteRecord", "success");
            return true;
        }
    }

    /**
     * 删除记录
     *
     * @param recordType 记录类型
     * @return 删除成功，返回true；反之，返回false！
     */
    @SuppressLint("LongLogTag")
    public Boolean deleteRecord(RecordType recordType){

        SQLiteDatabase sqLiteDatabase = WinDBManager.getDatabase();
        String whereCaluse = RecordEntry.COLUMN_NAME_RECORDTYPE + " = ?";
        String[] whereArgs = new String[]{
                String.valueOf(recordType.getValue())};

        int affectRow = sqLiteDatabase.delete(RecordEntry.TABLE_NAME, whereCaluse,
                whereArgs);
        if(sqLiteDatabase.isOpen()){
            sqLiteDatabase.close();
        }
        if (affectRow == 0) {
            Log.i("RecordBLO deleteRecord", "faild");
            return false;
        } else {
            Log.i("RecordBLO deleteRecord", "success");
            return true;
        }
    }

    /**
     * 删除记录
     *
     * @return 删除成功，返回true；反之，返回false！
     */
    @SuppressLint("LongLogTag")
    public Boolean deleteAllRecord(){
        SQLiteDatabase sqLiteDatabase = null;
        try {
            String sql = String.format("DELETE FROM %s", RecordEntry.TABLE_NAME);
            sqLiteDatabase = WinDBManager.getDatabase();
            sqLiteDatabase.execSQL(sql);
        }catch (Exception e){
            return false;
        }finally {
            if (sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
        return true;

    }

    /**
     * 根据记录类型和条码查询精确查找
     *
     * @param recordType 记录类型
     * @param barCode    条码
     * @return 记录集合
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    private List<RecordBO> getRecordsByBarCode(RecordType recordType,
                                               String barCode) throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = getProjection();

        String selection = RecordEntry.COLUMN_NAME_RECORDTYPE + " = ? " + " AND "
                + RecordEntry.COLUMN_NAME_BARCODE + " = ?";
        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()), barCode};

        String sortOrder = RecordEntry.COLUMN_NAME_BARCODE + " DESC";

        // 3 查询
        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);

        // 4 获取结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            lstRecord.add(getRecord(cursor));
        }
        db.close();
        cursor.close();
        Log.i("RecordBLO getRecordsByBarCode", "" + lstRecord.size());
        return lstRecord;
    }

    /**
     * 获得查询项目
     *
     * @return 查询项目
     */
    private String[] getProjection() {
        String[] projection = {BaseColumns._ID,
                RecordEntry.COLUMN_NAME_BARCODE,
                RecordEntry.COLUMN_NAME_THDNUM,
                RecordEntry.COLUMN_NAME_IOTYPE,
                RecordEntry.COLUMN_NAME_DBDNUM,
                RecordEntry.COLUMN_NAME_JXSNUMID,
                RecordEntry.COLUMN_NAME_JXSNUM,
                RecordEntry.COLUMN_NAME_WAREHOUSEID,
                RecordEntry.COLUMN_NAME_WAREHOUSE,
                RecordEntry.COLUMN_NAME_CREATEUSERID,
                RecordEntry.COLUMN_NAME_CREATEUSERUID,
                RecordEntry.COLUMN_NAME_CREATEDATE,
                RecordEntry.COLUMN_NAME_RECORDTYPE,
                RecordEntry.COLUMN_NAME_TRANID,
                RecordEntry.COLUMN_NAME_ISUPLOAD};
        return projection;
    }

    /**
     * 通过游标获得记录
     *
     * @param cursor 游标
     * @return 记录
     * @throws ParseException
     */
    public RecordBO getRecord(Cursor cursor) throws ParseException {
        // 获取值
        long recordId = cursor.getLong(cursor
                .getColumnIndexOrThrow(BaseColumns._ID));
        String barCode = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_BARCODE));
        String thdNum = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_THDNUM));
        int ioType = cursor.getInt(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_IOTYPE));
        String dbdNum = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_DBDNUM));
        long jxsNumId = cursor.getLong(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_JXSNUMID));
        String jxsNum = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_JXSNUM));
        long wareHouseId = cursor.getLong(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_WAREHOUSEID));
        String warehouse = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_WAREHOUSE));
        long createUserId = cursor.getLong(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEUSERID));
        String createUserUID = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEUSERUID));
        String createDateString = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEDATE));
        int recordTypeValue = cursor.getInt(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_RECORDTYPE));
        long tranId = cursor.getLong(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_TRANID));
        int isUpload = cursor.getInt(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_ISUPLOAD));

        RecordBO record = new RecordBO();
        record.setRecordId(recordId);
        record.setBarCode(barCode);
        record.setThdNum(thdNum);
        RecordType recordType = RecordType.value(recordTypeValue);
        if (recordType == RecordType.in) {
            record.setInType(InTypes.value(ioType));
        } else if (recordType == RecordType.out) {
            record.setOutType(OutTypes.value(ioType));
        }
        record.setDbdNum(dbdNum);
        record.setJxsNumId(jxsNumId);
        record.setJxsNum(jxsNum);
        record.setWarehouseId(wareHouseId);
        record.setWarehouse(warehouse);
        record.setCreateUserId(createUserId);
        record.setCreateUserUID(createUserUID);
        record.setCreateDate(DateHelper.getDate(createDateString));
        record.setRecordType(recordType);
        record.setTranId(tranId);
        record.setIsUpload(YesNos.value(isUpload));
        return record;
    }

    /**
     * 通过游标获得记录
     *
     * @param cursor 游标
     * @return 记录
     * @throws ParseException
     */
    public RecordBO getRecord2(Cursor cursor) throws ParseException {
        // 获取值
        long recordId = cursor.getLong(cursor
                .getColumnIndexOrThrow(BaseColumns._ID));
        String barCode = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_BARCODE));
        String thdNum = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_THDNUM));
        int ioType = cursor.getInt(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_IOTYPE));
        String dbdNum = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_DBDNUM));
        String jxsNum = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_JXSNUM));
        String warehouse = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_WAREHOUSE));
        String createUserUID = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEUSERUID));
        String createDateString = cursor.getString(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEDATE));
        int recordTypeValue = cursor.getInt(cursor
                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_RECORDTYPE));

        RecordBO record = new RecordBO();
        record.setRecordId(recordId);
        record.setBarCode(barCode);
        record.setThdNum(thdNum);
        RecordType recordType = RecordType.value(recordTypeValue);
        if (recordType == RecordType.in) {
            record.setInType(InTypes.value(ioType));
        } else if (recordType == RecordType.out) {
            record.setOutType(OutTypes.value(ioType));
        }
        record.setDbdNum(dbdNum);
        record.setJxsNum(jxsNum);
        record.setWarehouse(warehouse);
        record.setCreateUserUID(createUserUID);
        record.setCreateDate(DateHelper.getDate(createDateString));
        record.setRecordType(recordType);
        return record;
    }

    /**
     * 通过记录类型和条码判断条码是否存在
     *
     * @param recordType 记录类型
     * @param barCode    条形码
     * @return
     * @throws Exception
     */
    public Boolean isExist(RecordType recordType, String barCode)
            throws Exception {
        List<RecordBO> lstRecord = getRecordsByBarCode(recordType, barCode);
        if (lstRecord.size() > 0) {
            Log.i("RecordBLO isExist", "success!");
            return true;
        } else {
            Log.i("RecordBLO isExist", "failed");
            return false;
        }
    }

    /**
     * 通过记录类型、条形码模糊查询获得记录
     *
     * @param recordType 记录类型
     * @param barCode    条形码
     * @return 记录集合
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    public List<RecordBO> getRecordsLikeBarCode(RecordType recordType,
                                                String barCode) throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = getProjection();

        String selection = RecordEntry.COLUMN_NAME_RECORDTYPE + " =?" + " AND "
                + RecordEntry.COLUMN_NAME_BARCODE + " like ?";
        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()), "%" + barCode + "%"};

        String sortOrder = RecordEntry.COLUMN_NAME_BARCODE + " DESC";

        // 3 查询
        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);

        // 4 获取结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            lstRecord.add(getRecord(cursor));
        }
        cursor.close();
        db.close();
        Log.i("RecordBLO getRecordsLikeBarCode", "" + lstRecord.size());
        return lstRecord;
    }

    /**
     * 通过记录类型、条形码模糊查询，获得符合条件的总数量
     *
     * @param recordType 记录类型
     * @param barCode    条形码
     * @return 条形码总数
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    public int getRecordsTotalCountLikeBarCode(RecordType recordType,
                                               String barCode) throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String sql = "SELECT COUNT(" + RecordEntry.TABLE_NAME + "."
                + BaseColumns._ID + ") FROM " + RecordEntry.TABLE_NAME
                + " WHERE " + RecordEntry.COLUMN_NAME_RECORDTYPE + " =? AND "
                + RecordEntry.COLUMN_NAME_BARCODE + " like ?";

        Log.i("sql", sql);

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()), "%" + barCode + "%"};

        // 3 查询
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        // 4 获得查询结果
        int count = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        Log.i("RecordBLO getRecordsTotalCountLikeBarCode", "" + count);
        return count;
    }

    /**
     * 通过记录类型、条形码模糊查询获得指定页的记录
     *
     * @param recordType  记录类型
     * @param barCode     条形码
     * @param currentPage 当前页
     * @param pageSize    一页的数量
     * @return 记录集合
     * @throws Exception
     */
    public List<RecordBO> getRecordsLikeBarCode(RecordType recordType,
                                                String barCode, int currentPage, int pageSize) {

        try {
            // 1 获得数据库
            SQLiteDatabase db = WinDBManager.getDatabase();

            // 2 设置查询参数
            String[] projection = getProjection();

            String selection = RecordEntry.COLUMN_NAME_RECORDTYPE + " =?" + " AND "
                    + RecordEntry.COLUMN_NAME_BARCODE + " like ?";

            String sortOrder = RecordEntry.COLUMN_NAME_BARCODE + "  ASC LIMIT ?,?";

            String[] selectionArgs = new String[]{
                    String.valueOf(recordType.getValue()), "%" + barCode + "%",
                    String.valueOf((currentPage - 1) * pageSize),
                    String.valueOf(pageSize)};

            // 3 查询
            Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, selection,
                    selectionArgs, null, null, sortOrder);

            // 4 获得查询结果
            List<RecordBO> lstRecord = new ArrayList<RecordBO>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                // 获取值
                lstRecord.add(getRecord(cursor));
            }
            cursor.close();
            db.close();
            return lstRecord;
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 通过记录类型、提货单号和货位号查询记录
     *
     * @param recordType 记录类型
     * @return 记录集合
     * @throws Exception
     */
    public int getRecordsTotalCount(RecordType recordType) throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String sql = "SELECT COUNT(" + BaseColumns._ID + ") FROM "
                + RecordEntry.TABLE_NAME + " WHERE "
                + RecordEntry.COLUMN_NAME_RECORDTYPE + " =?";

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue())};

        // 3 查询
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        // 4 获得查询结果
        int count = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
            count = cursor.getInt(0);
            break;
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 通过记录类型、提货单号和货位号查询记录
     *
     * @param recordType 记录类型
     * @param thdNum     提货单号
     * @param warehouse  仓库货位
     * @return 记录集合
     * @throws Exception
     */
    /*public int getRecordsTotalCount(RecordType recordType, String thdNum,
                                    String warehouse) throws Exception {
        if (thdNum == null || thdNum.length() == 0) {
            return 0;
        }
        if (warehouse == null || warehouse.length() == 0) {
            return getRecordsTotalCountByThdNum(recordType, thdNum);
        } else {
            return getRecordsTotalCountByThdNumAndWarehouse(recordType, thdNum,
                    warehouse);
        }
    }*/

    /**
     * 通过记录类型、提货单号精确查找符合条件的记录总数
     *
     * @param recordType 记录类型
     * @param thdNum     提货单号
     * @return 记录总数
     */
    private int getRecordsTotalCountByThdNum(RecordType recordType,
                                             String thdNum) {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String sql = "SELECT COUNT(" + BaseColumns._ID + ") FROM "
                + RecordEntry.TABLE_NAME + " WHERE "
                + RecordEntry.COLUMN_NAME_RECORDTYPE + " =? AND "
                + RecordEntry.COLUMN_NAME_THDNUM + " =?";

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()), thdNum};

        // 3 查询
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        // 4 获得查询结果
        int count = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
            count = cursor.getInt(0);
            break;
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 通过记录类型、提货单号、货位号精确查找符合条件的记录总数
     *
     * @param recordType 记录类型
     * @param thdNum     提货单号
     * @param warehouse  货位号
     * @return 记录总数
     */
    private int getRecordsTotalCountByThdNumAndWarehouse(
            RecordType recordType, String thdNum, String warehouse) {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String sql = "SELECT COUNT(" + BaseColumns._ID + ") FROM "
                + RecordEntry.TABLE_NAME + " WHERE "
                + RecordEntry.COLUMN_NAME_RECORDTYPE + " =? AND "
                + RecordEntry.COLUMN_NAME_THDNUM + " =? AND "
                + RecordEntry.COLUMN_NAME_WAREHOUSE + " =?";

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()), thdNum, warehouse};

        // 3 查询
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        // 4 获得查询结果
        int count = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
            count = cursor.getInt(0);
            break;
        }
        cursor.close();
        db.close();
        return count;


    }

    /**
     * 通过记录类型、提货单号和货位号查询记录
     *
     * @param recordType 记录类型
     * @param thdNum     提货单号
     * @param warehouse  仓库货位
     * @param tranId     物流Id
     * @return 记录集合
     * @throws Exception
     */
    public int getRecordsTotalCount(RecordType recordType, String thdNum,
                                    String warehouse, long tranId) throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT COUNT(" + BaseColumns._ID + ") FROM "
                + RecordEntry.TABLE_NAME + " WHERE "
                + RecordEntry.COLUMN_NAME_RECORDTYPE + " =?");
        if ((thdNum == null || thdNum.length() == 0)
                && (warehouse == null || warehouse.length() == 0)
                && tranId == 0) {
            return 0;
        }

        if (thdNum != null && thdNum.length() > 0) {
            sbSql.append(" AND " + RecordEntry.COLUMN_NAME_THDNUM + "='" +
                    thdNum + "'");
        }

        if (warehouse != null && warehouse.length() > 0) {
            sbSql.append(" AND " + RecordEntry.COLUMN_NAME_WAREHOUSE + "='"
                    + warehouse + "'");
        }

        if (tranId != 0) {
            sbSql.append(" AND " + RecordEntry.COLUMN_NAME_TRANID + " = '" +
                    String.valueOf(tranId) + "'");
        }
        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue())};

        // 3 查询
        Cursor cursor = db.rawQuery(sbSql.toString(), selectionArgs);

        // 4 获得查询结果
        int count = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
            count = cursor.getInt(0);
            break;
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 通过记录类型、提货单号、货位号精确查找符合条件的分页记录数据
     *
     * @param recordType  记录类型
     * @param thdNum      提货单号
     * @param warehouse   货位号
     * @param tranId      物流Id
     * @param currentPage 当前页
     * @param pageSize    一页数据行数
     * @return 符合条件的一页数据
     * @throws Exception
     */
    public List<RecordBO> getRecords(
            RecordType recordType, String thdNum,
            String warehouse, long tranId,
            int currentPage, int pageSize) {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {BaseColumns._ID,
                RecordEntry.COLUMN_NAME_BARCODE};

        // 2 设置查询参数
        StringBuffer sbSelection = new StringBuffer();
        sbSelection.append(RecordEntry.COLUMN_NAME_RECORDTYPE + "=?");
        if ((thdNum == null || thdNum.length() == 0)
                && (warehouse == null || warehouse.length() == 0)
                && tranId == 0) {
            return new ArrayList<RecordBO>();
        }

        if (thdNum != null && thdNum.length() > 0) {
            sbSelection.append(" AND " + RecordEntry.COLUMN_NAME_THDNUM + "='" +
                    thdNum + "'");
        }

        if (warehouse != null && warehouse.length() > 0) {
            sbSelection.append(" AND " + RecordEntry.COLUMN_NAME_WAREHOUSE + "='"
                    + warehouse + "'");
        }

        if (tranId != 0) {
            sbSelection.append(" AND " + RecordEntry.COLUMN_NAME_TRANID + " = '" +
                    String.valueOf(tranId) + "'");
        }

        String sortOrder = RecordEntry._ID + " DESC  LIMIT ?,?";
        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()),
                String.valueOf((currentPage - 1) * pageSize),
                String.valueOf(pageSize)};

        // 3 查询
        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection,
                sbSelection.toString(), selectionArgs, null, null, sortOrder);

        // 4 获取结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        RecordBO record = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // 获取值
            long recordId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(BaseColumns._ID));
            String barCode = cursor.getString(cursor
                    .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_BARCODE));
            record = new RecordBO();
            record.setRecordId(recordId);
            record.setBarCode(barCode);
            lstRecord.add(record);
        }
        cursor.close();
        db.close();
        return lstRecord;
    }

    /**
     * 通过记录类型、提货单号、货位号精确查找分页记录
     *
     * @param recordType  记录类型
     * @param currentPage 当前页
     * @param pageSize    一页数据行数
     * @return 记录集合
     * @throws ParseException
     * @throws Exception
     */
    public List<RecordBO> getRecords(RecordType recordType, int currentPage, int pageSize) throws ParseException {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = getProjection();
        String selection = RecordEntry.COLUMN_NAME_RECORDTYPE + " =?";
        String sortOrder = RecordEntry._ID + " DESC  LIMIT ?,?";

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()),
                String.valueOf((currentPage - 1) * pageSize),
                String.valueOf(pageSize)};

        // 3 查询
        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);

        // 4 获取结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            lstRecord.add(getRecord(cursor));
        }
        cursor.close();
        db.close();
        return lstRecord;
    }


    /**
     * 通过记录类型、提货单号、货位号精确查找分页记录
     *
     * @param recordType  记录类型
     * @param thdNum      提货单号
     * @param warehouse   仓库货位
     * @param currentPage 当前页
     * @param pageSize    一页数据行数
     * @return 记录集合
     * @throws ParseException
     * @throws Exception
     */
    public List<RecordBO> getRecords(RecordType recordType, String thdNum,
                                     String warehouse, int currentPage, int pageSize) throws ParseException {
        if (thdNum == null || thdNum.length() == 0) {
            return new ArrayList<RecordBO>();
        }
        if (warehouse == null || warehouse.length() == 0) {
            return getRecordsByThdNum(recordType, thdNum, currentPage, pageSize);
        } else {
            return getRecordsByThdNumAndWarehouse(recordType, thdNum,
                    warehouse, currentPage, pageSize);
        }
    }

    /**
     * 通过记录类型、提货单号精确查找符合条件的分页记录数据
     *
     * @param recordType  记录类型
     * @param thdNum      提货单号
     * @param currentPage 当前页
     * @param pageSize    一页数据行数
     * @return 符合条件的一页数据
     * @throws ParseException
     * @throws Exception
     */
    private List<RecordBO> getRecordsByThdNum(RecordType recordType,
                                              String thdNum, int currentPage, int pageSize) throws ParseException {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = getProjection();
        String selection = RecordEntry.COLUMN_NAME_RECORDTYPE + " =? AND "
                + RecordEntry.COLUMN_NAME_THDNUM + " =?";
        String sortOrder = RecordEntry._ID + " DESC  LIMIT ?,?";

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()), thdNum,
                String.valueOf((currentPage - 1) * pageSize),
                String.valueOf(pageSize)};

        // 3 查询
        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);

        // 4 获取结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            lstRecord.add(getRecord(cursor));
        }
        cursor.close();
        db.close();
        return lstRecord;
    }

    /**
     * 通过记录类型、提货单号、货位号精确查找符合条件的分页记录数据
     *
     * @param recordType  记录类型
     * @param thdNum      提货单号
     * @param warehouse   货位号
     * @param currentPage 当前页
     * @param pageSize    一页数据行数
     * @return 符合条件的一页数据
     * @throws ParseException
     * @throws Exception
     */
    private List<RecordBO> getRecordsByThdNumAndWarehouse(
            RecordType recordType, String thdNum, String warehouse,
            int currentPage, int pageSize) throws ParseException {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = getProjection();

        String selection = RecordEntry.COLUMN_NAME_RECORDTYPE + " =? AND "
                + RecordEntry.COLUMN_NAME_THDNUM + " =? AND "
                + RecordEntry.COLUMN_NAME_WAREHOUSE + " =?";

        String sortOrder = RecordEntry._ID + " DESC  LIMIT ?,?";

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()), thdNum, warehouse,
                String.valueOf((currentPage - 1) * pageSize),
                String.valueOf(pageSize)};

        // 3 查询
        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);

        // 4 获取结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            lstRecord.add(getRecord(cursor));
        }
        cursor.close();
        db.close();
        return lstRecord;
    }

    /**
     * 通过单号查询记录
     *
     * @param thdNum 单号
     * @return 记录 集合
     * @throws Exception
     *//*
    public List<RecordBO> getRecords(String thdNum) throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {BaseColumns._ID,
                RecordEntry.COLUMN_NAME_BARCODE,
                RecordEntry.COLUMN_NAME_IOTYPE,
                RecordEntry.COLUMN_NAME_DBDNUM,
                RecordEntry.COLUMN_NAME_JXSNUM,
                RecordEntry.COLUMN_NAME_WAREHOUSE,
                RecordEntry.COLUMN_NAME_CREATEUSERUID,
                RecordEntry.COLUMN_NAME_CREATEDATE,
                RecordEntry.COLUMN_NAME_RECORDTYPE,
                RecordEntry.COLUMN_NAME_ISUPLOAD};

        String selection = RecordEntry.COLUMN_NAME_THDNUM + "=?";
        String[] selectionArgs = new String[]{String.valueOf(thdNum)};

        // 3 查询
        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        // 4 获取查询结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        RecordBO record = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            // 获取值
            long recordId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(BaseColumns._ID));
            String barCode = cursor.getString(cursor
                    .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_BARCODE));
            int ioTypeValue = cursor.getInt(cursor.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_IOTYPE));
            String jxsNum = cursor.getString(cursor
                    .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_JXSNUM));
            String warehouse = cursor.getString(cursor
                    .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_WAREHOUSE));
            String createUserUID = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEUSERUID));
            String createDateString = cursor.getString(cursor
                    .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEDATE));
            int recordTypeValue = cursor.getInt(cursor
                    .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_RECORDTYPE));
            int isUpload = cursor.getInt(cursor
                    .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_ISUPLOAD));

            record = new RecordBO();
            record.setRecordId(recordId);
            record.setBarCode(barCode);
            record.setThdNum(thdNum);
            RecordType recordType = RecordType.value(recordTypeValue);
            if (recordType == RecordType.in) {
                record.setInType(InTypes.value(ioTypeValue));
            } else if (recordType == RecordType.out) {
                record.setOutType(OutTypes.value(ioTypeValue));
            }
            record.setJxsNum(jxsNum);
            record.setWarehouse(warehouse);
            record.setCreateUserUID(createUserUID);
            record.setCreateDate(DateHelper.getDate(createDateString));
            record.setRecordType(recordType);
            record.setIsUpload(YesNos.value(isUpload));
            lstRecord.add(record);
        }
        cursor.close();
        db.close();
        return lstRecord;
    }*/

    /**
     * 通过记录类型、提货单号和货位号查询记录
     *
     * @param thdNum     提货单号
     * @return 数量
     * @throws Exception
     */
    public int getRecordsTotalCount(String thdNum) {

        // 1 获得数据库
        SQLiteDatabase sqliteDatabase = WinDBManager.getDatabase();

        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT COUNT(" + BaseColumns._ID + ") FROM "
                + RecordEntry.TABLE_NAME);
        sbSql.append(" WHERE " + RecordEntry.COLUMN_NAME_THDNUM + " = ?");


        String[] selectionArgs = new String[]{thdNum};

        // 3 查询
        Cursor cursor = sqliteDatabase.rawQuery(sbSql.toString(), selectionArgs);

        // 4 获得查询结果
        int count = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
            count = cursor.getInt(0);
            break;
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        return count;
    }

    /**
     * 通过单号查询记录
     *
     * @param thdNum 单号
     * @return 记录 集合
     * @throws Exception
     */
    public List<RecordBO> getRecords(String thdNum, int currentPage, int pageSize) {
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        SQLiteDatabase sqliteDatabase=null;
        Cursor cursor = null;
        try {
            // 1 获得数据库
            sqliteDatabase = WinDBManager.getDatabase();

            // 2 设置查询参数
            String[] projection = {BaseColumns._ID,
                    RecordEntry.COLUMN_NAME_BARCODE,
                    RecordEntry.COLUMN_NAME_IOTYPE,
                    RecordEntry.COLUMN_NAME_DBDNUM,
                    RecordEntry.COLUMN_NAME_JXSNUM,
                    RecordEntry.COLUMN_NAME_WAREHOUSE,
                    RecordEntry.COLUMN_NAME_CREATEUSERUID,
                    RecordEntry.COLUMN_NAME_CREATEDATE,
                    RecordEntry.COLUMN_NAME_RECORDTYPE,
                    RecordEntry.COLUMN_NAME_ISUPLOAD};

            String selection = RecordEntry.COLUMN_NAME_THDNUM + " = ?";
            String[] selectionArgs = new String[]{thdNum,
                    String.valueOf(currentPage * pageSize),
                    String.valueOf(pageSize)};

            String sortOrder = RecordEntry._ID + " DESC  LIMIT ?,?";

            // 3 查询
            cursor = sqliteDatabase.query(RecordEntry.TABLE_NAME, projection, selection,
                    selectionArgs, null, null, sortOrder);

            // 4 获取查询结果
            RecordBO record = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                // 获取值
                long recordId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(BaseColumns._ID));
                String barCode = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_BARCODE));
                int ioTypeValue = cursor.getInt(cursor.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_IOTYPE));
                String jxsNum = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_JXSNUM));
                String warehouse = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_WAREHOUSE));
                String createUserUID = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEUSERUID));
                String createDateString = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEDATE));
                int recordTypeValue = cursor.getInt(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_RECORDTYPE));
                int isUpload = cursor.getInt(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_ISUPLOAD));

                record = new RecordBO();
                record.setRecordId(recordId);
                record.setBarCode(barCode);
                record.setThdNum(thdNum);
                RecordType recordType = RecordType.value(recordTypeValue);
                if (recordType == RecordType.in) {
                    record.setInType(InTypes.value(ioTypeValue));
                } else if (recordType == RecordType.out) {
                    record.setOutType(OutTypes.value(ioTypeValue));
                }
                record.setJxsNum(jxsNum);
                record.setWarehouse(warehouse);
                record.setCreateUserUID(createUserUID);
                record.setCreateDate(DateHelper.getDate(createDateString));
                record.setRecordType(recordType);
                record.setIsUpload(YesNos.value(isUpload));
                lstRecord.add(record);
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
            if (sqliteDatabase.isOpen()) {
                sqliteDatabase.close();
            }
            return lstRecord;
        }catch(Exception e){
            lstRecord.clear();
            return lstRecord;
        }finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            if (sqliteDatabase.isOpen()) {
                sqliteDatabase.close();
            }
        }
    }

    /**
     * 获得通过提货单号模糊查询记录的记录总数
     *
     * @param thdNum 提货单号
     * @return 记录总数
     * @throws Exception
     */
    public int getRecordsTotalCountLikeThdNum(String thdNum) throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String sql = "SELECT COUNT(DISTINCT(" + RecordEntry.COLUMN_NAME_THDNUM
                + ")) FROM " + RecordEntry.TABLE_NAME + " WHERE "
                + RecordEntry.COLUMN_NAME_THDNUM + " like ?";

        Log.i("sql", sql);

        String[] selectionArgs = new String[]{"%" + thdNum + "%"};

        // 3 查询
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        // 4 获得查询结果
        int count = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 通过提货单号模糊查询记录
     *
     * @param thdNum      提货单号
     * @param currentPage 当前页
     * @param pageSize    一页的数量
     * @return
     * @throws Exception
     */
    public List<RecordBO> getRecordsLikeThdNum(String thdNum, int currentPage,
                                               int pageSize) {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String sql = "SELECT " + RecordEntry.COLUMN_NAME_THDNUM + ",count("
                + BaseColumns._ID + ") as "
                + RecordEntry.COLUMN_NAME_BARCODECOUNT + " FROM "
                + RecordEntry.TABLE_NAME + " WHERE "
                + RecordEntry.COLUMN_NAME_THDNUM + " like ?" + " GROUP BY "
                + RecordEntry.COLUMN_NAME_THDNUM + " ORDER BY "
                + RecordEntry.COLUMN_NAME_THDNUM + " ASC LIMIT ?,?";

        Log.i("sql", sql);

        String[] selectionArgs = new String[]{"%" + thdNum + "%",
                String.valueOf((currentPage - 1) * pageSize),
                String.valueOf(pageSize)};

        // 3 查询
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        // 4 获得查询结果
        List<RecordBO> lstThdNum = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String thdNumTemp = cursor.getString(cursor
                    .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_THDNUM));

            int barCodeCount = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_BARCODECOUNT));

            Log.i("query thdNum", thdNumTemp);
            Log.i("query barCodeCount", "" + barCodeCount);

            RecordBO record = new RecordBO();
            record.setThdNum(thdNumTemp);
            record.setBarCodeCount(barCodeCount);
            lstThdNum.add(record);
        }
        cursor.close();
        db.close();
        return lstThdNum;
    }

    /**
     * 获得全部记录
     *
     * @return
     * @throws Exception
     */
    public List<RecordBO> getRecords() throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String sql = "SELECT " + RecordEntry.TABLE_NAME + "." + BaseColumns._ID + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_BARCODE + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_THDNUM + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_IOTYPE + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_DBDNUM + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_JXSNUM + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_WAREHOUSE + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_CREATEUSERID + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_CREATEDATE + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_RECORDTYPE + ","
                + RecordEntry.COLUMN_NAME_CREATEUSERUID
                + " FROM " + RecordEntry.TABLE_NAME + " ORDER BY "
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_THDNUM
                + "," + RecordEntry.COLUMN_NAME_BARCODE + " ASC";

        Log.i("sql", sql);
        // 3 查询
        Cursor cursor = db.rawQuery(sql, null);
        // 4 获得查询结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            lstRecord.add(getRecord2(cursor));
        }
        cursor.close();
        db.close();
        return lstRecord;
    }

    /**
     * 通过开始时间和结束时间获得获得记录
     *
     * @return
     */
    public List<RecordBO> getRecords(Date startDate, Date endDate)
            throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String sql = "SELECT " + RecordEntry.TABLE_NAME + "." + BaseColumns._ID + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_BARCODE + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_THDNUM + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_IOTYPE + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_DBDNUM + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_JXSNUM + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_WAREHOUSE + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_CREATEUSERID + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_CREATEDATE + ","
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_RECORDTYPE + ","
                + RecordEntry.COLUMN_NAME_CREATEUSERUID
                + " FROM " + RecordEntry.TABLE_NAME + " WHERE "
                + RecordEntry.TABLE_NAME + "."
                + RecordEntry.COLUMN_NAME_CREATEDATE
                + " BETWEEN ? AND ? ORDER BY "
                + RecordEntry.TABLE_NAME + "." + RecordEntry.COLUMN_NAME_THDNUM
                + "," + RecordEntry.COLUMN_NAME_BARCODE + " ASC";

        Log.i("sql", sql);
        String[] selectionArgs = new String[]{
                DateHelper.getDateString(startDate),
                DateHelper.getDateString(endDate)};
        // 3 查询
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        // 4 获得查询结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            lstRecord.add(getRecord2(cursor));
        }
        cursor.close();
        db.close();
        return lstRecord;
    }

    /**
     * 导出记录到文本文件
     *
     * @return
     */
    public Boolean exportRecordsToTxt(List<RecordBO> lstRecord)
            throws Exception {
        Map<String, List<String>> mpRecord = getRecordMap(lstRecord);
        Set<String> mpRecordKeySet = mpRecord.keySet();
        for (String filePath : mpRecordKeySet) {
            List<String> lstRecordString = mpRecord.get(filePath);
            TxtDBHelper.setDataFilePath(filePath);
            if (!TxtDBHelper.WriteDatas(lstRecordString)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获得全部记录的Map格式，其中Key存放该条记录应存放的文件名，Value为改记录的字符串格式
     *
     * @return 记录的Map格式
     * @throws Exception
     */
    private Map<String, List<String>> getRecordMap(List<RecordBO> lstRecord)
            throws Exception {
        // 获得当前设备的编号
        OptionBLO optionBlo = new OptionBLO();
        String deviceNum = optionBlo.getOptionValue(OptionTypes.DeviceNum);
        if (deviceNum == null) {
            throw new Exception();
        }
        Map<String, List<String>> mpRecord = new HashMap<String, List<String>>();

        for (RecordBO record : lstRecord) {
            // yy文件
            String yyFlePathTemp = convertToFilePath(TXTFileTypes.yy, record,
                    deviceNum);
            String yyRecordStringTemp = convertToRecordString(TXTFileTypes.yy,
                    record);
            if (!mpRecord.containsKey(yyFlePathTemp)) {
                mpRecord.put(yyFlePathTemp, new ArrayList<String>());
            }
            mpRecord.get(yyFlePathTemp).add(yyRecordStringTemp);

            // ks文件
            String ksFlePathTemp = convertToFilePath(TXTFileTypes.ks, record,
                    deviceNum);
            String ksRecordStringTemp = convertToRecordString(TXTFileTypes.ks,
                    record);
            if (!mpRecord.containsKey(ksFlePathTemp)) {
                mpRecord.put(ksFlePathTemp, new ArrayList<String>());
            }
            mpRecord.get(ksFlePathTemp).add(ksRecordStringTemp);
        }
        return mpRecord;
    }

    /**
     * 获得记录存放的文件名
     *
     * @param txtFileType   文件类型
     * @param record        记录
     * @param currDeviceNum 当前的设备编号
     * @return 文件名字符串
     * @throws Exception
     */
    private String convertToFilePath(TXTFileTypes txtFileType, RecordBO record,
                                     String currDeviceNum) throws Exception {
        // 一级根目录
        String root = PathHelper.GetSDRootPath();

        // 创建的年月日yyyyMMdd
        String createYearMonthDay = DateHelper.getDateStringEx(
                record.getCreateDate(), "yyyyMMdd");

        String dataFilePath = String.format("%s%s%s%s%s%s%s%s%s%s", root,
                File.separator, FirstDir, File.separator, TXTFileTypes
                        .valueSting(txtFileType), RecordType
                        .valueString(record.getRecordType()), record
                        .getCreateUser().getUid(), currDeviceNum,
                createYearMonthDay, Dev1);
        return dataFilePath;
    }

    /**
     * 获得记录字符串(登录帐号（四位数）+提货单号（八位数）+日期时间+条形码)
     * 如：3333615703722014/09/2910:46:104G50444010009
     *
     * @param txtFileType 文件类型
     * @param record      记录对象
     * @return 记录字符串
     */
    private String convertToRecordString(TXTFileTypes txtFileType,
                                         RecordBO record) {
        if (record == null) {
            return null;
        } else {
            StringBuilder sbRecordString = null;
            sbRecordString = new StringBuilder();
            sbRecordString.append(DataLinePre);
            sbRecordString.append(record.getThdNum());
            sbRecordString.append(DateHelper.getDateStringEx(record
                    .getCreateDate()));
            sbRecordString.append(record.getBarCode());
            if (txtFileType == TXTFileTypes.yy) {
                sbRecordString.append(record.getWarehouse());
            }
            return sbRecordString.toString();
        }
    }

    /*************************** 记录同步相关 **************************/
    /**
     * 同步记录
     *
     * @param token
     * @param record
     * @throws Exception
     */
    public boolean syncRecords(String token, RecordBO record) throws Exception {
        if (record == null) {
            return false;
        }
        SyncAPITypes syncAPIType = SyncAPITypes.addInBarCode;
        if (record.getRecordType() == RecordType.in) {
            syncAPIType = SyncAPITypes.addInBarCode;
        } else if (record.getRecordType() == RecordType.out) {
            syncAPIType = SyncAPITypes.addOutBarCode;
        }
        List<NameValuePair> lstNameValue = convertInRecord(token, record);
        String addInResult = SyncHelper.getResponse(
                SyncAPITypes.valueSting(syncAPIType), lstNameValue);
        JSONObject jsonDataItem = new JSONObject(addInResult);
        return jsonDataItem.getBoolean("flag");
    }

    /**
     * 同步记录
     *
     * @throws Exception
     */
    public void syncRecords(String ip, String token) throws Exception {
        // 1.上传入库记录
        // 1.1获得入库记录
        List<RecordBO> lstInRecord = getRecordsByIsUpload(RecordType.in, YesNos.No);
        for (RecordBO record : lstInRecord) {
            // 1.2 转换参数
            List<NameValuePair> lstNameValue = convertInRecord(token, record);
            String url = ip + SyncAPITypes.valueSting(SyncAPITypes.addInBarCode);
            String addInResult = SyncHelper.getResponse(
                    url,
                    lstNameValue);
            JSONObject jsonDataItem = new JSONObject(addInResult);
            boolean flag = jsonDataItem.getBoolean("flag");
            if (flag == true) {
                // 1.3 修改记录，设置为已上传
                editRecordToUpload(record.getRecordId());
            }
        }

        // 2.上传出库记录
        List<RecordBO> lstOutRecord = getRecordsByIsUpload(RecordType.out, YesNos.No);
        for (RecordBO record : lstOutRecord) {
            // 1.2 转换参数
            List<NameValuePair> lstNameValue = convertOutRecord(token, record);
            String url = ip + SyncAPITypes.valueSting(SyncAPITypes.addInBarCode);
            String addInResult = SyncHelper.getResponse(
                    url,
                    lstNameValue);
            JSONObject jsonDataItem = new JSONObject(addInResult);
            boolean flag = jsonDataItem.getBoolean("flag");
            if (flag == true) {
                // 1.3 修改记录，设置为已上传
                editRecordToUpload(record.getRecordId());
            }
        }
    }

    /**
     * 根据记录类型和是否上传精确查找
     *
     * @param recordType 记录类型
     * @param isUpload   是否上传
     * @return 记录集合
     * @throws Exception
     */
    @SuppressLint("LongLogTag")
    private List<RecordBO> getRecordsByIsUpload(RecordType recordType,
                                                YesNos isUpload) throws Exception {
        // 1 获得数据库
        SQLiteDatabase db = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = getProjection();

        String selection = RecordEntry.COLUMN_NAME_RECORDTYPE + " =?" + " AND "
                + RecordEntry.COLUMN_NAME_ISUPLOAD + " =?";
        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()),
                String.valueOf(isUpload.getValue())};

        String sortOrder = RecordEntry.COLUMN_NAME_BARCODE + " DESC";

        // 3 查询
        Cursor cursor = db.query(RecordEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);

        // 4 获取结果
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            lstRecord.add(getRecord(cursor));
        }
        cursor.close();
        db.close();
        Log.i("RecordBLO getRecordsByBarCode", "" + lstRecord.size());
        return lstRecord;
    }

    /**
     * 转换出库记录的同步参数
     *
     * @param token  令牌
     * @param record 出库记录
     * @return List<NameValuePair>
     */
    private List<NameValuePair> convertOutRecord(String token, RecordBO record) {
        List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
        lstNameValue.add(new BasicNameValuePair("token", token));
        lstNameValue
                .add(new BasicNameValuePair("barcode", record.getBarCode()));
        lstNameValue.add(new BasicNameValuePair("thdnum", record.getThdNum()));
        lstNameValue.add(new BasicNameValuePair("jxsnumid", String
                .valueOf(record.getJxsNumId())));
        lstNameValue.add(new BasicNameValuePair("jxsnum", record.getJxsNum()));
        lstNameValue.add(new BasicNameValuePair("warehouseid", String
                .valueOf(record.getWarehouseId())));
        lstNameValue.add(new BasicNameValuePair("warehouse", record
                .getWarehouse()));
        lstNameValue.add(new BasicNameValuePair("createuserid", String
                .valueOf(record.getCreateUserId())));
        lstNameValue.add(new BasicNameValuePair("createuseruid", record
                .getCreateUserUID()));
        return lstNameValue;
    }

    /**
     * 转换入库记录的同步参数
     *
     * @param token  令牌
     * @param record 入库记录
     * @return
     */
    private List<NameValuePair> convertInRecord(String token, RecordBO record) {
        List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
        lstNameValue.add(new BasicNameValuePair("token", token));
        lstNameValue
                .add(new BasicNameValuePair("barcode", record.getBarCode()));
        lstNameValue.add(new BasicNameValuePair("thdnum", record.getThdNum()));
        lstNameValue.add(new BasicNameValuePair("intype", String.valueOf(record.getInType())));
        lstNameValue.add(new BasicNameValuePair("jxsnumid", String
                .valueOf(record.getJxsNumId())));
        lstNameValue.add(new BasicNameValuePair("jxsnum", record.getJxsNum()));
        lstNameValue.add(new BasicNameValuePair("warehouseid", String
                .valueOf(record.getWarehouseId())));
        lstNameValue.add(new BasicNameValuePair("warehouse", record
                .getWarehouse()));
        lstNameValue.add(new BasicNameValuePair("createuserid", String
                .valueOf(record.getCreateUserId())));
        lstNameValue.add(new BasicNameValuePair("createuseruid", record
                .getCreateUserUID()));
        return lstNameValue;
    }

    /**
     * 编辑记录为已上传
     *
     * @param recordId 记录Id
     * @return 编辑成功，返回true；反之，返回false！
     */
    private boolean editRecordToUpload(long recordId) {
        SQLiteDatabase db = WinDBManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_NAME_ISUPLOAD, String.valueOf(YesNos.Yes.getValue()));

        String selection = RecordEntry._ID
                + "=?";

        String[] selectionArgs = {String.valueOf(recordId)};
        int count = db.update(RecordEntry.TABLE_NAME, values, selection,
                selectionArgs);
        db.close();
        Log.e("RecordDAO", "editRecordToUpload更新的行数为：" + count);
        return count == 1;
    }

    /**
     * 通过记录类型、提货单号和货位号查询记录
     *
     * @param recordType 记录类型
     * @param thdNum     提货单号
     * @param dbdNum 调拨单
     * @return 记录集合
     * @throws Exception
     */
    public int getRecordsTotalCount(RecordType recordType, String thdNum,String dbdNum) {
        if ((recordType == RecordType.in || recordType == RecordType.out)
                && StringHelper.isEmpty(thdNum)) {
            return 0;
        }

        // 1 获得数据库
        SQLiteDatabase sqliteDatabase = WinDBManager.getDatabase();

        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT COUNT(" + BaseColumns._ID + ") FROM "
                + RecordEntry.TABLE_NAME);
        sbSql.append(" WHERE " + RecordEntry.COLUMN_NAME_RECORDTYPE + " = ?");
        if (!StringHelper.isEmpty(thdNum)) {
            sbSql.append(String.format(" AND %s = '%s'", RecordEntry.COLUMN_NAME_THDNUM, thdNum));
        }
        if (!StringHelper.isEmpty(dbdNum)) {
            sbSql.append(String.format(" AND %s = '%s'", RecordEntry.COLUMN_NAME_DBDNUM, dbdNum));
        }

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue())};

        // 3 查询
        Cursor cursor = sqliteDatabase.rawQuery(sbSql.toString(), selectionArgs);

        // 4 获得查询结果
        int count = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); ) {
            count = cursor.getInt(0);
            break;
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        return count;
    }

    /**
     * 获得记录汇总
     *
     * @param recordType  记录类型
     * @param thdNum      提货单号
     * @param currentPage 当前页
     * @param pageSize    一页大小
     * @return 记录汇总集合
     */
    public List<RecordGatherBO> getRecordGather(RecordType recordType, String thdNum,
                                                String dbdNum, int currentPage, int pageSize) {
        List<RecordGatherBO> lstMRecordGather = new ArrayList<RecordGatherBO>();
        if ((recordType == RecordType.in || recordType == RecordType.out)
                && StringHelper.isEmpty(thdNum)) {
            return lstMRecordGather;
        }
        // 1 获得数据库
        SQLiteDatabase sqliteDatabase = WinDBManager.getDatabase();

        // 2 设置查询参数
        String[] columns = {
                String.format("SUBSTR(%s,1,5) AS %s", RecordEntry.COLUMN_NAME_BARCODE,
                        RecordGatherBO.RecordGatherEntry.COLUMN_NAME_SERNO),
                String.format("COUNT(*) AS %s", RecordGatherBO.RecordGatherEntry.COLUMN_NAME_COUNT)};

        StringBuffer sbSelection = new StringBuffer();
        sbSelection.append(RecordEntry.COLUMN_NAME_RECORDTYPE + " = ?");
        if (!StringHelper.isEmpty(thdNum)) {
            sbSelection.append(String.format(" AND %s = '%s'", RecordEntry.COLUMN_NAME_THDNUM, thdNum));
        }
        if (!StringHelper.isEmpty(dbdNum)) {
            sbSelection.append(String.format(" AND %s = '%s'", RecordEntry.COLUMN_NAME_DBDNUM, dbdNum));
        }

        String[] selectionArgs = new String[]{
                String.valueOf(recordType.getValue()),
                String.valueOf(currentPage * pageSize),
                String.valueOf(pageSize)};

        String groupBy = RecordGatherBO.RecordGatherEntry.COLUMN_NAME_SERNO;
        String orderBy = RecordGatherBO.RecordGatherEntry.COLUMN_NAME_SERNO + " DESC LIMIT ?,?";


        // 3 查询
        Cursor cursor = sqliteDatabase.query(RecordBO.RecordEntry.TABLE_NAME, columns, sbSelection.toString(),
                selectionArgs, groupBy, null, orderBy);

        // 4 获取结果
        RecordGatherBO mrecordGather = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mrecordGather = new RecordGatherBO();
            String serNo = cursor.getString(cursor
                    .getColumnIndexOrThrow(RecordGatherBO.RecordGatherEntry.COLUMN_NAME_SERNO));
            int count = cursor.getInt(cursor
                    .getColumnIndexOrThrow(RecordGatherBO.RecordGatherEntry.COLUMN_NAME_COUNT));

            mrecordGather.setSerNo(serNo);
            mrecordGather.setCount(count);
            lstMRecordGather.add(mrecordGather);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        return lstMRecordGather;
    }

    /**
     * 通过记录类型、提货单号、货位号精确查找符合条件的分页记录数据
     *
     * @param recordType  记录类型
     * @param serNo       提货单号
     * @param thdNum      提货单号
     * @param dbdNum      调拨单号
     * @param currentPage 当前页
     * @param pageSize    一页数据行数
     * @return 符合条件的一页数据
     * @throws Exception
     */
    public List<RecordBO> getRecords(
            RecordType recordType, String serNo, String thdNum, String dbdNum, int currentPage, int pageSize)

    {
        List<RecordBO> lstRecord = new ArrayList<RecordBO>();
        SQLiteDatabase sqliteDatabase = null;
        Cursor cursor = null;
        try {
            if (StringHelper.isEmpty(serNo)) {
                return lstRecord;
            }

            if ((recordType == RecordType.in || recordType == RecordType.out) &&
                    StringHelper.isEmpty(thdNum)) {
                return lstRecord;
            }

            // 1 获得数据库
            sqliteDatabase = WinDBManager.getDatabase();

            // 2 设置查询参数
            String[] cloumns = {BaseColumns._ID,
                    RecordEntry.COLUMN_NAME_BARCODE,
                    RecordEntry.COLUMN_NAME_THDNUM,
                    RecordEntry.COLUMN_NAME_IOTYPE,
                    RecordEntry.COLUMN_NAME_DBDNUM,
                    RecordEntry.COLUMN_NAME_JXSNUMID,
                    RecordEntry.COLUMN_NAME_JXSNUM,
                    RecordEntry.COLUMN_NAME_WAREHOUSEID,
                    RecordEntry.COLUMN_NAME_WAREHOUSE,
                    RecordEntry.COLUMN_NAME_CREATEUSERID,
                    RecordEntry.COLUMN_NAME_CREATEUSERUID,
                    RecordEntry.COLUMN_NAME_CREATEDATE,
                    RecordEntry.COLUMN_NAME_RECORDTYPE,
                    RecordEntry.COLUMN_NAME_TRANID,
                    RecordEntry.COLUMN_NAME_ISUPLOAD};

            // 2 设置查询参数
            StringBuffer sbSelection = new StringBuffer();

            sbSelection.append(RecordEntry.COLUMN_NAME_RECORDTYPE + " = ?");
            sbSelection.append(String.format(" AND SUBSTR(%s,1,5) = ?", RecordEntry.COLUMN_NAME_BARCODE));
            if (!StringHelper.isEmpty(thdNum)) {
                sbSelection.append(String.format(" AND %s = '%s'", RecordEntry.COLUMN_NAME_THDNUM, thdNum));
            }
            if (!StringHelper.isEmpty(dbdNum)) {
                sbSelection.append(String.format(" AND %s = '%s'", RecordEntry.COLUMN_NAME_DBDNUM, dbdNum));
            }

            String sortOrder = RecordEntry._ID + " DESC  LIMIT ?,?";
            String[] selectionArgs = new String[]{
                    String.valueOf(recordType.getValue()),
                    serNo,
                    String.valueOf(currentPage * pageSize),
                    String.valueOf(pageSize)};

            // 3 查询
            cursor = sqliteDatabase.query(RecordEntry.TABLE_NAME, cloumns,
                    sbSelection.toString(), selectionArgs, null, null, sortOrder);

            // 4 获取结果

            RecordBO recordBO = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                long recordId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(BaseColumns._ID));
                String barCode = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_BARCODE));
                String thdNumTemp = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_THDNUM));
                int ioType = cursor.getInt(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_IOTYPE));
                String dbdNumTemp = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_DBDNUM));
                long jxsNumId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_JXSNUMID));
                String jxsNum = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_JXSNUM));
                long wareHouseId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_WAREHOUSEID));
                String wareHouseTemp = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_WAREHOUSE));
                long createUserId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEUSERID));
                String createUserUID = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEUSERUID));
                String createDateString = cursor.getString(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_CREATEDATE));
                int recordTypeValue = cursor.getInt(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_RECORDTYPE));
                long tranId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_TRANID));
                int isUpload = cursor.getInt(cursor
                        .getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_ISUPLOAD));

                RecordBO record = new RecordBO();
                record.setRecordId(recordId);
                record.setBarCode(barCode);
                record.setThdNum(thdNumTemp);
                RecordType recordTypeTemp = RecordType.value(recordTypeValue);
                if (recordType == RecordType.in) {
                    record.setInType(InTypes.value(ioType));
                } else if (recordType == RecordType.out) {
                    record.setOutType(OutTypes.value(ioType));
                }
                record.setDbdNum(dbdNumTemp);
                record.setJxsNumId(jxsNumId);
                record.setJxsNum(jxsNum);
                record.setWarehouseId(wareHouseId);
                record.setWarehouse(wareHouseTemp);
                record.setCreateUserId(createUserId);
                record.setCreateUserUID(createUserUID);
                record.setCreateDate(DateHelper.getDate(createDateString));
                record.setRecordType(recordType);
                record.setTranId(tranId);
                record.setIsUpload(YesNos.value(isUpload));

                lstRecord.add(record);
            }

            return lstRecord;

        } catch (Exception e) {
            return new ArrayList<RecordBO>();
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            if (sqliteDatabase.isOpen()) {
                sqliteDatabase.close();
            }
        }
    }
}
