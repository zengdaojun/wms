package com.zdjer.min.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.zdjer.min.bean.BarCodeTypes;
import com.zdjer.min.bean.MRecordBO;
import com.zdjer.min.bean.MRecordBO.MRecordEntry;
import com.zdjer.min.bean.MRecordType;
import com.zdjer.min.utils.MinDBManager;
import com.zdjer.utils.DateHelper;
import com.zdjer.wms.bean.ProductBO;
import com.zdjer.wms.bean.RecordGatherBO;
import com.zdjer.wms.model.ProductBLO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务逻辑：记录
 *
 * @author Administrator
 */
public class MRecordBLO {

    /**
     * 构造函数
     */
    public MRecordBLO() {

    }

    /**
     * 添加记录
     *
     * @param mrecord 记录
     * @return 添加成功，返回true；反之，返回false！
     */
    public Boolean addMRecord(MRecordBO mrecord) {
        SQLiteDatabase sqliteDatabase = MinDBManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put(MRecordEntry.COLUMN_NAME_BARCODE, mrecord.getBarCode());
        values.put(MRecordEntry.COLUMN_NAME_CREATEUSERID,
                mrecord.getCreateUserId());
        values.put(MRecordEntry.COLUMN_NAME_CREATEDATE,
                DateHelper.getDateString(mrecord.getCreateDate()));
        values.put(MRecordEntry.COLUMN_NAME_SENDPERSONID, mrecord.getSendPersonId());
        values.put(MRecordEntry.COLUMN_NAME_SERVERPRODUCTID, mrecord.getServerProductId());
        values.put(MRecordEntry.COLUMN_NAME_BARCODETYPE,
                String.valueOf(mrecord.getBarCodeType().getValue()));
        values.put(MRecordEntry.COLUMN_NAME_MRECORDTYPE,
                String.valueOf(mrecord.getMRecordType().getValue()));

        long result = sqliteDatabase.insert(MRecordEntry.TABLE_NAME,
                MRecordEntry.COLUMN_NAME_NULLABLE, values);
        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        if (result == -1) {
            Log.i("MRecordBLO addRecord", "failed!");
            return false;
        } else {
            Log.i("MRecordBLO addRecord", "success!");
            return true;
        }
    }

    /**
     * 通过网点记录类型、创建人获得分页记录
     *
     * @param mRecordType  网点记录类型
     * @param serNo        产品序列号
     * @param currentPage  当前页
     * @param pageSize     一页的数量
     * @return 记录
     * @throws ParseException
     */
    public List<MRecordBO> getMRecords(MRecordType mRecordType, String serNo,long sendPersonId,
                                       int currentPage, int pageSize){
        // 1 获得数据库
        SQLiteDatabase sqLiteDatabase = MinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {
                MRecordEntry._ID,
                MRecordEntry.COLUMN_NAME_BARCODE};

        StringBuffer sbSelection = new StringBuffer();
        sbSelection.append(MRecordBO.MRecordEntry.COLUMN_NAME_MRECORDTYPE + " = ?");
        sbSelection.append(" AND SUBSTR(" + MRecordEntry.COLUMN_NAME_BARCODE + ",1,5) = ?");

        String[] selectionArgs = null;
        if(mRecordType != MRecordType.check){
            sbSelection.append(" AND "+MRecordBO.MRecordEntry.COLUMN_NAME_SENDPERSONID + " = ?");

            selectionArgs = new String[]{
                    String.valueOf(mRecordType.getValue()),
                    serNo,
                    String.valueOf(sendPersonId),
                    String.valueOf(currentPage * pageSize),
                    String.valueOf(pageSize)};
        }else{
            selectionArgs = new String[]{
                    String.valueOf(mRecordType.getValue()),
                    serNo,
                    String.valueOf(currentPage * pageSize),
                    String.valueOf(pageSize)};
        }

        String sortOrder = MRecordEntry._ID + " DESC  LIMIT ?,?";

        // 3 查询
        Cursor cursor = sqLiteDatabase.query(MRecordBO.MRecordEntry.TABLE_NAME, projection, sbSelection.toString(),
                selectionArgs, null, null, sortOrder);

        // 4 获取结果
        List<MRecordBO> lstMRecord = new ArrayList<MRecordBO>();
        MRecordBO mrecord = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mrecord = new MRecordBO();
            long recordId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MRecordEntry._ID));
            String barCode = cursor.getString(cursor
                    .getColumnIndexOrThrow(MRecordEntry.COLUMN_NAME_BARCODE));

            mrecord.setRecordId(recordId);
            mrecord.setBarCode(barCode);
            lstMRecord.add(mrecord);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
        return lstMRecord;
    }

    /**
     * 通过网点记录类型、创建人Id、送货人Id获得记录
     *
     * @param mRecordType  网点记录类型
     * @param sendPersonId 送货人Id
     * @return 网点记录集合
     * @throws ParseException
     */
    public int getMRecordTotalCount(MRecordType mRecordType, long sendPersonId) {
        // 1 获得数据库
        SQLiteDatabase sqliteDatabase = MinDBManager.getDatabase();

        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT COUNT(" + BaseColumns._ID + ") FROM "
                + MRecordEntry.TABLE_NAME);
        sbSql.append(" WHERE " + MRecordEntry.COLUMN_NAME_MRECORDTYPE + " = ?");
        if (sendPersonId!=0) {
            sbSql.append(String.format(" AND %s = '%d'", MRecordEntry.COLUMN_NAME_SENDPERSONID, sendPersonId));
        }

        String[] selectionArgs = new String[]{
                String.valueOf(mRecordType.getValue())};

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
     * 通过网点记录类型、创建人Id、送货人Id获得记录
     *
     * @param mRecordType  网点记录类型
     * @param sendPersonId 送货人Id
     * @return 网点记录集合
     * @throws ParseException
     */
    public List<MRecordBO> getMRecords(MRecordType mRecordType, long sendPersonId) {
        // 1 获得数据库
        SQLiteDatabase sqliteDatabase = MinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {BaseColumns._ID,
                MRecordEntry.COLUMN_NAME_BARCODE,
                MRecordEntry.COLUMN_NAME_BARCODETYPE,
                MRecordEntry.COLUMN_NAME_SERVERPRODUCTID};

        StringBuffer sbSelection = new StringBuffer();
        sbSelection.append(MRecordBO.MRecordEntry.COLUMN_NAME_MRECORDTYPE + " = ?");

        if(sendPersonId!=0){
            sbSelection.append(String.format(" AND %s = '%d'",MRecordBO.MRecordEntry.COLUMN_NAME_SENDPERSONID,sendPersonId));
        }
        String[] selectionArgs = new String[]{
                String.valueOf(mRecordType.getValue())};

        // 3 查询
        Cursor cursor = sqliteDatabase.query(MRecordBO.MRecordEntry.TABLE_NAME, projection, sbSelection.toString(),
                selectionArgs, null, null, null);

        // 4 获取结果
        List<MRecordBO> lstMRecord = new ArrayList<MRecordBO>();

        MRecordBO mrecord = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mrecord = new MRecordBO();
            long recordId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(BaseColumns._ID));
            String barCode = cursor.getString(cursor
                    .getColumnIndexOrThrow(MRecordEntry.COLUMN_NAME_BARCODE));
            BarCodeTypes barcodeType = BarCodeTypes.value(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MRecordEntry.COLUMN_NAME_BARCODETYPE)));
            long serverProductId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MRecordEntry.COLUMN_NAME_SERVERPRODUCTID));

            mrecord.setRecordId(recordId);
            mrecord.setBarCode(barCode);
            mrecord.setBarCodeType(barcodeType);
            mrecord.setServerProductId(serverProductId);
            mrecord.setSendPersonIdId(sendPersonId);

            lstMRecord.add(mrecord);
        }
        if(!cursor.isClosed()) {
            cursor.close();
        }
        if(sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }

        ProductBLO productBlo = new ProductBLO();

        //处理条码所属的产品
        for (MRecordBO mrecordBOTemp1 : lstMRecord) {

            if (mrecordBOTemp1.getIsMatch() == false) {
                String serno = mrecordBOTemp1.getBarCode().substring(0, 5);
                for (MRecordBO mRecordBOTemp2 : lstMRecord) {
                    if (mRecordBOTemp2.getIsMatch() == false &&
                            mRecordBOTemp2.getMRecordId() != mrecordBOTemp1.getMRecordId()) {
                        String sernoTemp = mRecordBOTemp2.getBarCode().substring(0, 5);

                        ProductBO product = null;
                        if (mrecordBOTemp1.getBarCodeType() == BarCodeTypes.in) {
                            product = productBlo.getProduct(serno, sernoTemp);
                        } else if (mrecordBOTemp1.getBarCodeType() == BarCodeTypes.out) {
                            product = productBlo.getProduct(sernoTemp, serno);
                        }

                        if (product != null) {
                            mrecordBOTemp1.setServerProductId(product.getServerProductId());
                            mRecordBOTemp2.setServerProductId(product.getServerProductId());

                            mrecordBOTemp1.setIsMatch(true);
                            mRecordBOTemp2.setIsMatch(true);
                            break;
                        }
                    }
                }
            }
        }
        return lstMRecord;
    }

    /**
     * 删除记录
     *
     * @param mrecordId 创建人Id
     * @return 删除成功，返回true；反之，返回false
     */
    public Boolean deleteMRecord(long mrecordId) {
        SQLiteDatabase sqliteDatabase = MinDBManager.getDatabase();
        String whereCaluse = MRecordEntry._ID + " =?";
        String[] whereArgs = new String[]{String.valueOf(String.valueOf(mrecordId))};
        int affectRow = sqliteDatabase.delete(MRecordEntry.TABLE_NAME, whereCaluse,
                whereArgs);
        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        if (affectRow == 0) {
            Log.i("MRecordBLO deleteMRecord", "failed");
            return false;
        } else {
            Log.i("MRecordBLO deleteMRecord", "success");
            return true;
        }
    }

    /**
     * 删除记录
     *
     * @param mRecordType  记录类型
     * @param sendPersonId 送货师傅
     * @return 删除成功，返回true；反之，返回false
     */
    public Boolean deleteMRecord(MRecordType mRecordType, long sendPersonId) {
        SQLiteDatabase sqliteDatabase = MinDBManager.getDatabase();


        StringBuffer sbWhereCaluse = new StringBuffer();
        sbWhereCaluse.append(MRecordBO.MRecordEntry.COLUMN_NAME_MRECORDTYPE + " = ?");

        String[] whereArgs = null;
        if(mRecordType != MRecordType.check){
            sbWhereCaluse.append(" AND "+MRecordBO.MRecordEntry.COLUMN_NAME_SENDPERSONID + " = ?");

            whereArgs = new String[]{
                    String.valueOf(mRecordType.getValue()),
                    String.valueOf(sendPersonId)};
        }else{
            whereArgs = new String[]{
                    String.valueOf(mRecordType.getValue())};
        }

        int affectRow = sqliteDatabase.delete(MRecordEntry.TABLE_NAME, sbWhereCaluse.toString(),
                whereArgs);
        if(sqliteDatabase.isOpen()){
            sqliteDatabase.close();
        }
        if (affectRow == 0) {
            Log.i("MRecordBLO deleteMRecord", "faild");
            return false;
        } else {
            Log.i("MRecordBLO deleteMRecord", "success");
            return true;
        }
    }

    /**
     * @param lstMRecord
     * @return
     */
    public String convertMRecord(List<MRecordBO> lstMRecord) {
        StringBuffer sbBarCode = new StringBuffer();
        for (int i = 0; i < lstMRecord.size(); i++) {
            if (i > 0) {
                sbBarCode.append("-");
            }
            MRecordBO mrecord = lstMRecord.get(i);

            sbBarCode.append(mrecord.getBarCode());
            sbBarCode.append("|");
            sbBarCode.append(mrecord.getServerProductId());
            sbBarCode.append("|");
            if(mrecord.getBarCodeType() == BarCodeTypes.in){
                sbBarCode.append("0");
            }else if(mrecord.getBarCodeType() == BarCodeTypes.out){
                sbBarCode.append("1");
            }
        }
        return sbBarCode.toString();
    }

    /**
     * 判断条码是否存在
     *
     * @param mRecordType 网点记录类型
     * @param barcode     条码
     * @return 存在，返回true；反之，返回false
     */
    public Boolean isExistBarcode(MRecordType mRecordType, String barcode) {
        // 1 获得数据库
        SQLiteDatabase sqliteDatabase = MinDBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {BaseColumns._ID};

        String selection = MRecordEntry.COLUMN_NAME_MRECORDTYPE + "=? AND "
                + MRecordEntry.COLUMN_NAME_BARCODE + "=?";

        String[] selectionArgs = new String[]{String.valueOf(mRecordType.getValue()), barcode};

        // 3 查询
        Cursor cursor = sqliteDatabase.query(MRecordEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            return true;
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }
        return false;
    }

    /**
     * 获得记录汇总
     *
     * @param mrecordType  记录类型
     * @param sendPersonId 送货师傅
     * @param currentPage  当前页
     * @param pageSize     一页大小
     * @return 记录汇总集合
     */
    public List<RecordGatherBO> getRecordGather(MRecordType mrecordType, long sendPersonId,
                                                  int currentPage, int pageSize) {
        // 1 获得数据库
        SQLiteDatabase db = MinDBManager.getDatabase();

        // 2 设置查询参数
        String[] columns = {
                "SUBSTR(barcode,1,5) AS " + RecordGatherBO.RecordGatherEntry.COLUMN_NAME_SERNO,
                "COUNT(*) AS " + RecordGatherBO.RecordGatherEntry.COLUMN_NAME_COUNT};


        StringBuffer sbSelection = new StringBuffer();
        sbSelection.append(MRecordEntry.COLUMN_NAME_MRECORDTYPE + " = ?");

        if(sendPersonId!=0) {
            sbSelection.append(String.format(" AND %s = '%d'",MRecordEntry.COLUMN_NAME_SENDPERSONID,sendPersonId));
        }

        String[] selectionArgs = new String[]{
                String.valueOf(mrecordType.getValue()),
                String.valueOf(currentPage * pageSize),
                String.valueOf(pageSize)};

        String groupBy = RecordGatherBO.RecordGatherEntry.COLUMN_NAME_SERNO;
        String orderBy = MRecordEntry._ID + " DESC  LIMIT ?,?";

        // 3 查询
        Cursor cursor = db.query(MRecordBO.MRecordEntry.TABLE_NAME, columns, sbSelection.toString(),
                selectionArgs, groupBy, null, orderBy);

        // 4 获取结果
        List<RecordGatherBO> lstMRecordGather = new ArrayList<RecordGatherBO>();
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
        db.close();
        return lstMRecordGather;
    }
}
