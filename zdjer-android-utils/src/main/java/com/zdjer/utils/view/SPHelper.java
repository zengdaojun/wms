package com.zdjer.utils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

/**
 * Created by zdj on 16/4/13.
 */
public class SPHelper {

    private static String SP_NAME = "ZDJER_NAME";
    private static Context context;
    private static SharedPreferences sharedPreferences;

    private static boolean isAtLeastGB;

    static {
        //大于2.3
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            isAtLeastGB = true;
        }
    }

    public static void init(String spName){
        SP_NAME = spName;
        sharedPreferences = getPreferences();
    }

    /**
     * 设置上下文
     *
     * @param context
     */
    public static void setContext(Context context) {
        SPHelper.context = context;
    }

    /**
     * 获得SharedPreferences
     *
     * @return SharedPreferences
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        return getPreferences(SP_NAME);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static SharedPreferences getPreferences(String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /**
     * 设置Editor
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, int value) {
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        apply(editor);
    }

    /**
     * 设置Editor
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, long value) {
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        apply(editor);
    }

    /**
     * 设置Editor
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, boolean value) {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    /**
     * 设置Editor
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, String value) {
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        apply(editor);
    }

    /**
     * 保存Editor
     *
     * @param editor
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(Editor editor) {
        if (isAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }


    /**
     * 获得Int值
     *
     * @param key      键
     * @param defValue 值
     * @return Int值
     */
    public static int get(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * 获得Int值
     *
     * @param key      键
     * @param defValue 值
     * @return Int值
     */
    public static long get(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * 获得boolean值
     *
     * @param key      键
     * @param defValue 值
     * @return boolean值
     */
    public static boolean get(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 获得String值
     *
     * @param key      键
     * @param defValue 值
     * @return String值
     */
    public static String get(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }
}
