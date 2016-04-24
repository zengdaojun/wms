package com.zdjer.utils.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by zdj on 16/4/11.
 */
public class AppConfigHelper {

    private final static String APP_CONFIG = "config";
    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";


    private Context context;
    private static AppConfigHelper appConfig;

    public static AppConfigHelper getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfigHelper();
            appConfig.context = context;
        }
        return appConfig;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Properties getProperties() {
        FileInputStream fileInputStream = null;
        Properties properties = new Properties();
        try {
            //读取app_config目录下的config
            File dirAppConfig = context.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fileInputStream = new FileInputStream(dirAppConfig.getPath() + File.separator + APP_CONFIG);
            properties.load(fileInputStream);
        } catch (Exception e) {

        } finally {
            try {
                fileInputStream.close();
            } catch (Exception e) {

            }
        }
        return properties;
    }

    public String getProperty(String key) {
        Properties properties = getProperties();
        return (properties != null) ? properties.getProperty(key) : null;
    }

    private void storeProperties(Properties properties) {
        FileOutputStream fileOutputStream = null;
        try {
            File dirAppConfig = context.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File appConfig = new File(dirAppConfig, APP_CONFIG);
            fileOutputStream = new FileOutputStream(appConfig);

            properties.store(fileOutputStream, null);
            fileOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {

            }
        }
    }

    public void setProperties(Properties properties) {
        Properties propertiesTemp = getProperties();
        propertiesTemp.putAll(properties);
        storeProperties(propertiesTemp);
    }

    public void setProperty(String key, String value) {
        Properties properties = getProperties();
        properties.setProperty(key, value);
        storeProperties(properties);
    }

    public void removePropertys(String... key) {
        Properties properties = getProperties();
        for (String k : key) {
            properties.remove(k);
        }
        storeProperties(properties);
    }

}
