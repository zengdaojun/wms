package com.zdjer.wms.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.zdjer.utils.StringHelper;
import com.zdjer.wms.bean.DataItemBO;
import com.zdjer.wms.bean.DataItemBO.DataItemEntry;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.utils.DBManager;
import com.zdjer.wms.utils.WmsNetApiHelper.SyncAPITypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务逻辑：数据
 *
 * @author bipolor
 */
public class DataItemBLO {

    /**
     * 构造函数
     */
    public DataItemBLO() {

    }

    /**
     * 添加数据项集合
     *
     * @param lstDataItem
     * @return 添加成功，返回true；反之，返回false！
     */
    public Boolean addDataItems(List<DataItemBO> lstDataItem) {
        for (DataItemBO dataItem : lstDataItem) {
            if (!isExistDataItem(dataItem.getDataType(),
                    dataItem.getDataValue())) {
                if (!addDataItem(dataItem)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 添加数据项
     *
     * @param dataItem 数据项
     * @return 添加成功，返回true；反之，返回false！
     */
    private Boolean addDataItem(DataItemBO dataItem) {
        SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put(DataItemEntry.COLUMN_NAME_DATAID,
                String.valueOf(dataItem.getDataId()));

        values.put(DataItemEntry.COLUMN_NAME_DATATYPE,
                String.valueOf(dataItem.getDataType().getValue()));
        values.put(DataItemEntry.COLUMN_NAME_DATAVALUE,
                String.valueOf(dataItem.getDataValue()));
        values.put(DataItemEntry.COLUMN_NAME_PARENTID,
                String.valueOf(dataItem.getParentId()));
        long newRowId = sqLiteDatabase.insert(DataItemEntry.TABLE_NAME, null, values);
        sqLiteDatabase.close();
        if (newRowId != -1) {
            Log.i("DataItemBLO addDataItem", "Success!");
            return true;
        } else {
            Log.i("DataItemBLO addDataItem", "Faild!");
            return false;
        }
    }

    /**
     * 通过数据类型获得指定页的记录
     *
     * @param dataType    数据类型
     * @param parentId 父Id
     * @param param 参数
     * @param currentPage 当前页
     * @param pageSize    一页的数量
     * @return 记录集合
     * @throws Exception
     */
    public List<DataItemBO> getDataItems(DataType dataType, long parentId,
            String param,int currentPage, int pageSize) {
        // 1 获得数据库
        SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {BaseColumns._ID,
                DataItemEntry.COLUMN_NAME_DATAID,
                DataItemEntry.COLUMN_NAME_DATAVALUE};



        StringBuffer sbSelection = new StringBuffer();
        sbSelection.append(DataItemEntry.COLUMN_NAME_DATATYPE + " = ?");
        sbSelection.append(" AND "+DataItemEntry.COLUMN_NAME_PARENTID + " = ?");

        String[] selectionArgs = null;

        if(!StringHelper.isEmpty(param)){
            sbSelection.append(" AND "+DataItemEntry.COLUMN_NAME_DATAVALUE + " LIKE ?");
            selectionArgs = new String[]{
                    String.valueOf(dataType.getValue()),
                    String.valueOf(parentId),
                    "%"+param+"%",
                    String.valueOf(currentPage * pageSize),
                    String.valueOf(pageSize)};

        }else{
            selectionArgs = new String[]{
                    String.valueOf(dataType.getValue()),
                    String.valueOf(parentId),
                    String.valueOf(currentPage * pageSize),
                    String.valueOf(pageSize)};
        }

        String sortOrder = DataItemEntry.COLUMN_NAME_DATAVALUE + " ASC LIMIT ?,?";

        // 3 查询
        Cursor cursor = sqLiteDatabase.query(DataItemEntry.TABLE_NAME, projection, sbSelection.toString(),
                selectionArgs, null, null, sortOrder);

        // 4 获得查询结果
        List<DataItemBO> lstDataItem = new ArrayList<DataItemBO>();
        DataItemBO dataItem = null;
        if(currentPage == 0) {
            dataItem = new DataItemBO();
            dataItem.setDataId(0);
            dataItem.setDataType(dataType);
            dataItem.setDataValue("无");
            lstDataItem.add(dataItem);
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // 获取值
            long dataItemId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(BaseColumns._ID));
            long dataId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(DataItemEntry.COLUMN_NAME_DATAID));
            String dataValue = cursor.getString(cursor
                    .getColumnIndexOrThrow(DataItemEntry.COLUMN_NAME_DATAVALUE));

            dataItem = new DataItemBO();
            dataItem.setDataItemId(dataItemId);
            dataItem.setDataId(dataId);
            dataItem.setDataType(dataType);
            dataItem.setDataValue(dataValue);
            lstDataItem.add(dataItem);
        }
        if(!cursor.isClosed()) {
            cursor.close();
        }
        sqLiteDatabase.close();


        return lstDataItem;
    }

    /**
     * 通过数据类型和数据值获得数据项
     *
     * @param dataType  数据类型
     * @param dataValue 数据值
     * @return
     * @throws Exception
     */
    public DataItemBO getDataItem(DataType dataType, String dataValue) {
        // 1 获得数据库
        SQLiteDatabase sqLiteDatabase = DBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {BaseColumns._ID,
                DataItemEntry.COLUMN_NAME_DATAID,};

        String selection = DataItemEntry.COLUMN_NAME_DATATYPE + " =? AND " +
                DataItemEntry.COLUMN_NAME_DATAVALUE + " =?";

        String[] selectionArgs = new String[]{
                String.valueOf(dataType.getValue()), dataValue};

        // 3 查询
        Cursor cursor = sqLiteDatabase.query(DataItemEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        // 4 获得查询结果
        DataItemBO dataItem = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // 获取值
            long dataItemId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(BaseColumns._ID));
            long dataId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(DataItemEntry.COLUMN_NAME_DATAID));

            dataItem = new DataItemBO();
            dataItem.setDataItemId(dataItemId);
            dataItem.setDataId(dataId);
            dataItem.setDataType(dataType);
            dataItem.setDataValue(dataValue);
        }
        if(!cursor.isClosed()) {
            cursor.close();
        }
        sqLiteDatabase.close();
        return dataItem;
    }

    /**
     * 判断基础数据项是否存在
     *
     * @param dataType  数据类型
     * @param dataValue 数据值
     * @return
     * @throws Exception
     */
    public boolean isExistDataItem(DataType dataType, String dataValue) {
        return getDataItem(dataType, dataValue) != null;
    }

    /***************************数据同步相关**************************/
    /**
     * 同步基础数据
     *
     * @param ip    IP
     * @param token 令牌
     * @throws Exception
     */
    public void downloadBaseDataToLocal(String ip, String token) throws Exception {

        /*DataItemBLO dataItemBlo = new DataItemBLO();
        List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
        lstNameValue.add(new BasicNameValuePair("token", token));

        // 经销商数据下载
        String urlJXS = ip + SyncAPITypes.valueSting(SyncAPITypes.getJxs);
        String jxsResonse = SyncHelper.getResponse(urlJXS,
                lstNameValue);
        List<DataItemBO> lstJXSNum = convertToDataItem(DataTypes.jxsNum,
                jxsResonse);
        dataItemBlo.addDataItems(lstJXSNum);

        // 经物流数据下载
        String urlWuLiu = ip + SyncAPITypes.valueSting(SyncAPITypes.getWuLiu);
        String wuLiuResonse = SyncHelper.getResponse(
                urlWuLiu, lstNameValue);
        List<DataItemBO> lstWuLiu = convertToDataItem(DataTypes.wuLiu,
                wuLiuResonse);
        dataItemBlo.addDataItems(lstWuLiu);

        // 获得库房
        String urlWareHouse = ip +
                SyncAPITypes.valueSting(SyncAPITypes.getWareHouse);
        String wareHouseResonse = SyncHelper.getResponse(urlWareHouse, lstNameValue);
        List<DataItemBO> lstWareHouse = convertToDataItem(
                DataTypes.wareHouse, wareHouseResonse);
        dataItemBlo.addDataItems(lstWareHouse);

        // 获得车辆
        String urlCarNum = ip +
                SyncAPITypes.valueSting(SyncAPITypes.getCarNum);
        String carNumResonse = SyncHelper.getResponse(urlCarNum, lstNameValue);
        List<DataItemBO> lstCarNum = convertToDataItem(DataTypes.carNum,
                carNumResonse);
        dataItemBlo.addDataItems(lstCarNum);

        // 获得司机
        String urlDriver = ip + SyncAPITypes.valueSting(SyncAPITypes.getDriver);
        String driverResonse = SyncHelper.getResponse(
                urlDriver, lstNameValue);
        List<DataItemBO> lstDriver = convertToDataItem(DataTypes.driver,
                driverResonse);
        dataItemBlo.addDataItems(lstDriver);*/
    }

    /**
     * 同步基础数据
     *
     * @param ip          IP
     * @param token       令牌
     * @param syncAPIType API接口类型
     * @param dataType    数据类型
     * @throws Exception
     */
    public void downloadBaseDataToLocal(String ip, String token, SyncAPITypes syncAPIType, DataType dataType) throws Exception {
        /*List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
        lstNameValue.add(new BasicNameValuePair("token", token));
        // 经销商数据下载
        String urlAPI = ip + SyncAPITypes.valueSting(syncAPIType);
        String dataItemString = SyncHelper.getResponse(urlAPI,
                lstNameValue);
        List<DataItemBO> lstDataItem = convertToDataItem(dataType,
                dataItemString);
        addDataItems(lstDataItem);*/

        String urlAPI = ip + SyncAPITypes.valueSting(syncAPIType);
    }

    /**
     * 转换基础数据
     *
     * @param dataType 数据类型
     * @return 基础数据集合
     * @throws JSONException
     */
    public List<DataItemBO> convertToDataItem(DataType dataType, JSONObject response) throws JSONException {

        List<DataItemBO> lstDataItem = new ArrayList<DataItemBO>();
        if(response == null || !response.getBoolean("flag")){
            return lstDataItem;
        }else{
            DataItemBO dataItem = null;
            JSONArray jsonArray = response.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = (JSONObject) jsonArray.get(i);
                long id = jsonData.getLong("id");
                String value = "";
                if (jsonData.has("text")) {
                    value = jsonData.getString("text");
                } else if (jsonData.has("name")) {
                    value = jsonData.getString("name");
                }
                long parentId = 0;
                if (jsonData.has("installTeamId")) {
                    parentId = jsonData.getLong("installTeamId");
                }
                dataItem = new DataItemBO();
                dataItem.setDataId(id);
                dataItem.setDataType(dataType);
                dataItem.setDataValue(value);
                dataItem.setParentId(parentId);
                lstDataItem.add(dataItem);
            }
            return lstDataItem;
        }
    }

    /**
     * 同步仓库出入库的基础数据
     *
     * @param ip    IP
     * @param token 令牌
     * @throws Exception
     */
    public void downloadWIOBaseDataToLocal(String ip, String token) throws Exception {
        // 经销商数据下载
        downloadBaseDataToLocal(ip, token, SyncAPITypes.getJxs, DataType.jxsNum);

        // 经物流数据下载
        downloadBaseDataToLocal(ip, token, SyncAPITypes.getWuLiu, DataType.wuLiu);

        // 获得库房
        downloadBaseDataToLocal(ip, token, SyncAPITypes.getWareHouse, DataType.wareHouse);

        // 获得车辆
        downloadBaseDataToLocal(ip, token, SyncAPITypes.getCarNum, DataType.carNum);

        // 获得司机
        downloadBaseDataToLocal(ip, token, SyncAPITypes.getDriver, DataType.driver);
    }
}
