package com.zdjer.wms.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.zdjer.wms.bean.ProductBO;
import com.zdjer.wms.bean.ProductBO.ProductEntry;
import com.zdjer.wms.utils.DBManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务逻辑类：产品
 * Created by zdj on 16/1/9.
 */
public class ProductBLO {

    /**
     * 将从服务器中获得的产品信息添加到产品表中，若已经存在，则不添加
     *
     * @param lstProduct 产品集合
     * @return 添加成功，返回ture；反之，返回false
     */
    public Boolean addProduct(List<ProductBO> lstProduct) {
        for (ProductBO product : lstProduct) {
            if (!isExistProduct(product.getServerProductId())) {
                if (!addProduct(product)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 添加产品型号
     *
     * @param product 产品型号
     * @return 添加成功，返回true；反之，返回false
     */
    private Boolean addProduct(ProductBO product) {
        SQLiteDatabase db = DBManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductBO.ProductEntry.COLUMN_NAME_SERVERPRODUCTID,
                String.valueOf(product.getServerProductId()));
        values.put(ProductBO.ProductEntry.COLUMN_NAME_PRODUCTNAME,
                product.getProductName());
        values.put(ProductBO.ProductEntry.COLUMN_NAME_MINMODEL,
                product.getMinModel());
        values.put(ProductBO.ProductEntry.COLUMN_NAME_MINSERNO,
                product.getMinSerno());
        values.put(ProductBO.ProductEntry.COLUMN_NAME_MOUTMODEL,
                product.getMoutModel());
        values.put(ProductBO.ProductEntry.COLUMN_NAME_MOUTSERNO,
                product.getMoutSerno());

        long newRowId = db.insert(ProductBO.ProductEntry.TABLE_NAME, null, values);
        db.close();
        if (newRowId != -1) {
            Log.i("ProductBO addProduct", "Success!");
            return true;
        } else {
            Log.i("ProductBO addProduct", "Faild!");
            return false;
        }
    }

    /**
     * 判断产品是否存在
     *
     * @param serverProductId 服务端的产品Id
     * @return 若存在，返回true；反之，返回false。
     */
    private Boolean isExistProduct(long serverProductId) {
        return getProductByServerProductId(serverProductId) != null;
    }

    /**
     * 通过服务端的产品Id获得产品信息
     *
     * @param serverProductId
     * @return 产品信息
     */
    public ProductBO getProductByServerProductId(long serverProductId) {
        // 1 获得数据库
        SQLiteDatabase db = DBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {BaseColumns._ID,
                ProductBO.ProductEntry.COLUMN_NAME_PRODUCTNAME};

        String selection = ProductBO.ProductEntry.COLUMN_NAME_SERVERPRODUCTID + "=?";

        String[] selectionArgs = new String[]{String.valueOf(serverProductId)};

        // 3 查询
        Cursor cursor = db.query(ProductBO.ProductEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        // 4 获取结果
        ProductBO product = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            product = new ProductBO();
            long productId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(BaseColumns._ID));
            String productName = cursor.getString(cursor
                    .getColumnIndexOrThrow(ProductBO.ProductEntry.COLUMN_NAME_PRODUCTNAME));

            product.setProductId(productId);
            product.setServerProductId(serverProductId);
            product.setProductName(productName);
            break;
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        db.close();
        return product;
    }

    /**
     * @param serno
     * @return
     */
    public ProductBO getProduct(String serno) {
        // 1 获得数据库
        SQLiteDatabase db = DBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {ProductEntry.COLUMN_NAME_SERVERPRODUCTID,
                ProductEntry.COLUMN_NAME_MINSERNO,
                ProductEntry.COLUMN_NAME_MOUTSERNO};

        String selection = ProductEntry.COLUMN_NAME_MINSERNO + "=? OR "
                + ProductEntry.COLUMN_NAME_MOUTSERNO + "=?";

        String[] selectionArgs = new String[]{serno, serno};

        String sortOrder = ProductEntry.COLUMN_NAME_SERVERPRODUCTID + " DESC";

        // 3 查询
        Cursor cursor = db.query(ProductEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        // 4 获取结果
        ProductBO product = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            product = new ProductBO();
            long serverProductId = cursor.getLong(cursor.
                    getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_SERVERPRODUCTID));
            String minSerno = cursor.getString(cursor.
                    getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_MINSERNO));
            String moutSerno = cursor.getString(cursor.
                    getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_MOUTSERNO));
            product.setServerProductId(serverProductId);
            product.setMinSerno(minSerno);
            product.setMoutSerno(moutSerno);
            return product;
        }
        db.close();
        return null;
    }

    /**
     * @param minSerNo
     * @param moutSerNo
     * @return
     */
    public ProductBO getProduct(String minSerNo,String moutSerNo) {
        // 1 获得数据库
        SQLiteDatabase db = DBManager.getDatabase();

        // 2 设置查询参数
        String[] projection = {ProductEntry.COLUMN_NAME_SERVERPRODUCTID,
                ProductEntry.COLUMN_NAME_MINSERNO,
                ProductEntry.COLUMN_NAME_MOUTSERNO};

        String selection = ProductEntry.COLUMN_NAME_MINSERNO + "=? AND "
                + ProductEntry.COLUMN_NAME_MOUTSERNO + "=?";

        String[] selectionArgs = new String[]{minSerNo, moutSerNo};

        String sortOrder = ProductEntry.COLUMN_NAME_SERVERPRODUCTID + " DESC";

        // 3 查询
        Cursor cursor = db.query(ProductEntry.TABLE_NAME, projection, selection,
                selectionArgs, null, null, null);

        // 4 获取结果
        ProductBO product = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            product = new ProductBO();
            long serverProductId = cursor.getLong(cursor.
                    getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_SERVERPRODUCTID));
            String minSerno = cursor.getString(cursor.
                    getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_MINSERNO));
            String moutSerno = cursor.getString(cursor.
                    getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_MOUTSERNO));
            product.setServerProductId(serverProductId);
            product.setMinSerno(minSerno);
            product.setMoutSerno(moutSerno);
            return product;
        }
        cursor.close();
        db.close();
        return null;
    }


   /* *//**
     * 下载产品型号
     *
     * @param ip    IP
     * @param token TOKEN
     * @throws Exception
     *//*
    public void downloadProductToLocal(String ip, String token) throws Exception {
        List<NameValuePair> lstNameValue = new ArrayList<NameValuePair>();
        lstNameValue.add(new BasicNameValuePair("token", token));
        String url = ip + SyncHelper.SyncAPITypes.valueSting(SyncHelper.SyncAPITypes.getProductList);
        String response = SyncHelper.getResponse(url,
                lstNameValue);
        addProduct(convertToProduct(response));
    }*/

   /* *//**
     * 将从服务器中获得的产品信息添加到产品表中，若已经存在，则不添加
     *
     * @param response 产品字符串
     * @return 添加成功，返回ture；反之，返回false
     *//*
    public Boolean addProduct(JSONObject response) throws Exception{
        for (ProductBO product : convertToProduct(response)) {
            if (!isExistProduct(product.getServerProductId())) {
                if (!addProduct(product)) {
                    return false;
                }
            }
        }
        return true;
    }*/

    /**
     * 转换基础数据
     *
     * @param jsonObject 产品字符串
     * @return 基础数据集合
     * @throws JSONException
     *//*
    private List<ProductBO> convertToProduct(JSONObject jsonObject) throws JSONException {

        List<ProductBO> lstProduct = new ArrayList<ProductBO>();
        if(jsonObject!=null) {
            boolean flag = jsonObject.getBoolean("flag");
            if (flag) {
                ProductBO product = null;
                JSONArray jsonDatas = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonDatas.length(); i++) {
                    JSONObject jsonData = (JSONObject) jsonDatas.opt(i);
                    long serverProductId = jsonData.getLong("id");
                    String productName = jsonData.getString("product_name");
                    String minModel = jsonData.getString("in_model");
                    String minSerno = jsonData.getString("in_serno");
                    String moutModel = jsonData.getString("out_model");
                    String moutSerno = jsonData.getString("out_serno");
                    product = new ProductBO();
                    product.setServerProductId(serverProductId);
                    product.setProductName(productName);
                    product.setMinModel(minModel);
                    product.setMinSerno(minSerno);
                    product.setMoutModel(moutModel);
                    product.setMoutSerno(moutSerno);
                    lstProduct.add(product);
                }
            }
        }
        return lstProduct;
    }*/



    /**
     * 转换基础数据
     *
     * @param productString 产品字符串
     * @return 基础数据集合
     * @throws JSONException
     */
    private List<ProductBO> convertToProduct(String productString) throws JSONException {

        List<ProductBO> lstProduct = new ArrayList<ProductBO>();
        if(productString!=null) {
            JSONObject jsonProduct = new JSONObject(productString);
            boolean flag = jsonProduct.getBoolean("flag");
            if (flag) {
                ProductBO product = null;
                JSONArray jsonDatas = jsonProduct.getJSONArray("data");
                for (int i = 0; i < jsonDatas.length(); i++) {
                    JSONObject jsonData = (JSONObject) jsonDatas.opt(i);
                    long serverProductId = jsonData.getLong("id");
                    String productName = jsonData.getString("product_name");
                    String minModel = jsonData.getString("in_model");
                    String minSerno = jsonData.getString("in_serno");
                    String moutModel = jsonData.getString("out_model");
                    String moutSerno = jsonData.getString("out_serno");
                    product = new ProductBO();
                    product.setServerProductId(serverProductId);
                    product.setProductName(productName);
                    product.setMinModel(minModel);
                    product.setMinSerno(minSerno);
                    product.setMoutModel(moutModel);
                    product.setMoutSerno(moutSerno);
                    lstProduct.add(product);
                }
            }
        }
        return lstProduct;
    }
}
