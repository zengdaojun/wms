package com.zdjer.utils.http;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Locale;

/**
 * Created by zdj on 16/4/13.
 * <p/>
 * 说明：使用该类时，需要初始化asyncHttpClient，可一次初始化，一直使用
 */
public class AsyncHttpClientHelper {

    private static AsyncHttpClient asyncHttpClient;

    /**
     * 获得AsyncHttpClient
     *
     * @return AsyncHttpClient
     */
    public static AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }

    /**
     * 设置AsyncHttpClient
     *
     * @param asyncHttpClient AsyncHttpClient
     */
    public static void setAsyncHttpClient(AsyncHttpClient asyncHttpClient) {
        AsyncHttpClientHelper.asyncHttpClient = asyncHttpClient;
        AsyncHttpClientHelper.asyncHttpClient.setTimeout(11000);
        AsyncHttpClientHelper.asyncHttpClient.addHeader("Accept-Language", Locale.getDefault().toString());
        //AsyncHttpClientHelper.asyncHttpClient.addHeader("Host", HOST);
        //AsyncHttpClientHelper.asyncHttpClient.addHeader("Connection", "Keep-Alive");
       /* AsyncHttpClientHelper.asyncHttpClient.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);*/

        //AsyncHttpClientHelper.asyncHttpClient(ApiClientHelper.getUserAgent(AppContext.getInstance()));
    }

    /**
     * 取消所有的请求
     *
     * @param context 上下文
     */
    public static void cancelAll(Context context) {
        asyncHttpClient.cancelRequests(context, true);
    }


    /**
     * Get请求
     *
     * @param url                      apiURL
     * @param asyncHttpResponseHandler AsyncHttpResponseHandler
     */
    public static void get(String url, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        asyncHttpClient.get(url, asyncHttpResponseHandler);
        Log.d("Api", new StringBuilder("DELETE ").append(url).toString());
    }

    /**
     * Get请求
     *
     * @param url                      apiURL
     * @param binaryHttpResponseHandler BinaryHttpResponseHandler
     */
    public static void get(String url, BinaryHttpResponseHandler binaryHttpResponseHandler) {
        asyncHttpClient.get(url, binaryHttpResponseHandler);
        Log.d("Api", new StringBuilder("DELETE ").append(url).toString());
    }

    /**
     * Get请求
     *
     * @param url                      apiURL
     * @param requestParams            请求参数
     * @param asyncHttpResponseHandler AsyncHttpResponseHandler
     */
    public static void get(String url, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        asyncHttpClient.get(url, requestParams, asyncHttpResponseHandler);
        Log.d("Api", new StringBuilder("GET ").append(url).append("&")
                .append(requestParams).toString());
    }

    /**
     * Get请求
     *
     * @param url                      apiURL
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void get(String url, JsonHttpResponseHandler jsonHttpResponseHandler) {
        asyncHttpClient.get(url, jsonHttpResponseHandler);
        Log.d("Api", new StringBuilder("DELETE ").append(url).toString());
    }

    /**
     * Get请求
     *
     * @param url                      apiURL
     * @param requestParams            请求参数
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void get(String url, RequestParams requestParams, JsonHttpResponseHandler jsonHttpResponseHandler) {
        asyncHttpClient.get(url, requestParams, jsonHttpResponseHandler);
        Log.i("Api", new StringBuilder("GET ").append(url).append("&")
                .append(requestParams).toString());
    }

    /**
     * Post请求
     *
     * @param url                      apiURL
     * @param asyncHttpResponseHandler AsyncHttpResponseHandler
     */
    public static void post(String url, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        asyncHttpClient.post(url, asyncHttpResponseHandler);
        Log.i("Api", new StringBuilder("POST ").append(url).toString());
    }

    /**
     * Post请求
     *
     * @param url                      apiURL
     * @param requestParams            请求参数
     * @param asyncHttpResponseHandler AsyncHttpResponseHandler
     */
    public static void post(String url, RequestParams requestParams,
                            AsyncHttpResponseHandler asyncHttpResponseHandler) {
        asyncHttpClient.post(url, requestParams, asyncHttpResponseHandler);
        Log.d("Api", new StringBuilder("POST ").append(requestParams).append("&")
                .append(requestParams).toString());
    }

    /**
     * Post请求
     *
     * @param url                      apiURL
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void post(String url, JsonHttpResponseHandler jsonHttpResponseHandler) {
        asyncHttpClient.post(url, jsonHttpResponseHandler);
        Log.d("Api", new StringBuilder("POST ").append(url).toString());
    }

    /**
     * Post请求
     *
     * @param url                      apiURL
     * @param requestParams            请求参数
     * @param jsonHttpResponseHandler JsonHttpResponseHandler
     */
    public static void post(String url, RequestParams requestParams,
                            JsonHttpResponseHandler jsonHttpResponseHandler) {
        asyncHttpClient.post(url, requestParams, jsonHttpResponseHandler);
        Log.d("Api", new StringBuilder("POST ").append(requestParams).append("&")
                .append(requestParams).toString());
    }
}
