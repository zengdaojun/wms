package com.zdjer.utils.update;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zdjer.utils.R;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.XMLHelper;
import com.zdjer.utils.http.AsyncHttpClientHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.dialog.DialogHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zdj on 4/25/16.
 */
public class UpdateHelper {

    private Context context;
    private UpdateBean updateBean;//更新用实体
    private boolean isShowDiag = false;
    private Dialog dialog;
    private String saveFilePath;
    private String saveFileName;
    // 更新应用版本标记
    private static final int UPDATE_TOKEN = 0x29;
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 0x31;
    private String message = "检测到本程序有新版本发布，建议您更新！";
    // 下载应用的进度条
    private ProgressBar progressBar;
    // 进度条的当前刻度值
    private int curProgress;
    // 用户是否取消下载
    private boolean isCancel;

    public UpdateHelper(Context context, boolean isShowDiag,String saveFilePath) {
        this.context = context;
        this.isShowDiag = isShowDiag;
        this.saveFilePath = saveFilePath;
    }

    /**
     * 是否有新版本
     * @return
     */
    public boolean haveNewVersion() {
        if (this.updateBean == null) {
            return false;
        }
        boolean haveNew = false;
        int curVersionCode = DeviceHelper.getVersionCode(context.getPackageName());
        if (curVersionCode < updateBean.getAndroidBean()
                .getVersionCode()) {
            haveNew = true;
        }
        return haveNew;
    }

    /**
     * 检查更新
     * @param updateUrl
     */
    public void checkUpdate(String updateUrl) {
        if (isShowDiag) {
            showCheckDialog();
        }
        if(!DeviceHelper.hasInternet()){
            //网络连接失败，请检查网络设置
            DialogHelper.getMessageDialog(context,
                    context.getResources().getString(R.string.zdjer_network_error));
        }else {
            AsyncHttpClientHelper.get(updateUrl, asyncHttpResponseHandler);
        }
    }

    /**
     * 下载更新信息文件
     */
    private AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            hideCheckDialog();
            updateBean = XMLHelper.toBean(UpdateBean.class,
                    new ByteArrayInputStream(arg2));

            //检查
            onCheck();
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            hideCheckDialog();
            if (isShowDiag) {
                DialogHelper.getMessageDialog(context, "网络异常，无法获取新版本信息").show();
            }
        }
    };

    /**
     * 检查
     */
    private void onCheck() {
        if (haveNewVersion()) {
            showUpdateInfo();
        } else {
            if (isShowDiag) {
                DialogHelper.getMessageDialog(context, "已经是新版本了").show();
            }
        }
    }

    /**
     * 显示版本检查对话框
     */
    private void showCheckDialog() {
        if (dialog == null) {
            dialog = DialogHelper.getWaitDialog(context, "正在获取新版本信息...");
        }
        dialog.show();
    }

    /**
     * 隐藏版本检查对话框
     */
    private void hideCheckDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }



    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TOKEN: {
                    progressBar.setProgress(curProgress);
                    break;
                }

                case INSTALL_TOKEN: {
                    installApp();
                    break;
                }
            }
        }
    };

    private void showUpdateInfo() {
        if (updateBean == null) {
            return;
        }
        DialogHelper.getConfirmDialog(context,
                "软件版本更新", updateBean.getAndroidBean().getUpdateLog(),
                "下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                },
                "以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        ).show();
    }

    /**
     * 显示下载进度对话框
     */
    private void showDownloadDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.zdjer_download_notification, null);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_download);
        AlertDialog.Builder builder = DialogHelper.getAlertDialog(context);
        builder.setTitle("软件版本更新");
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isCancel = true;
            }
        });
        dialog = builder.create();
        dialog.show();
        downloadApp();
    }



    /**
     * 下载新版本应用
     */
    private void downloadApp() {

        /*String downloadUrl = updateBean.getAndroidBean().getDownloadUrl();
        saveFileName = saveFilePath + getSaveFileName(downloadUrl);
        AsyncHttpClientHelper.get(downloadUrl, new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                FileOutputStream fileOutputStream = null;
                try {

                    File filePath = new File(saveFilePath);
                    if (!filePath.exists()) {
                        filePath.mkdir();
                    }
                    fileOutputStream = new FileOutputStream(new File(saveFileName));
                    fileOutputStream.write(responseBody);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    installApp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastHelper.showToast("网络错误" + statusCode);
            }


            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                //super.onProgress(bytesWritten, totalSize);
                if(isCancel) {
                    onFinish();
                    //break;
                }
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                progressBar.setProgress(count);
            }
        });*/




        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                HttpURLConnection conn = null;
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String downloadUrl = updateBean.getAndroidBean().getDownloadUrl();
                        //String downloadUrl = "http://mts.wiseiter.com/upload/version/win.apk";
                        String fileName = getSaveFileName(downloadUrl);
                        saveFileName = saveFilePath + fileName;//"win.apk";//
                        url = new URL(downloadUrl);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        long fileLength = conn.getContentLength();
                        inputStream = conn.getInputStream();
                        if (inputStream == null) {
                            throw new RuntimeException("stream is null");
                        }
                        File filePath = new File(saveFilePath);
                        if (!filePath.exists()) {
                            filePath.mkdir();
                        }

                        File apkFile = new File(saveFilePath, fileName);

                        fileOutputStream = new FileOutputStream(apkFile);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        long readedLength = 0l;
                        while ((len = inputStream.read(buffer)) != -1) {
                            // 用户点击“取消”按钮，下载中断
                            if (isCancel) {
                                break;
                            }
                            fileOutputStream.write(buffer, 0, len);
                            readedLength += len;
                            curProgress = (int) (((float) readedLength / fileLength) * 100);
                            handler.sendEmptyMessage(UPDATE_TOKEN);
                            if (readedLength >= fileLength) {
                                dialog.dismiss();
                                // 下载完毕，通知安装
                                handler.sendEmptyMessage(INSTALL_TOKEN);
                                break;
                            }
                        }
                    }
                    fileOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 获取保存文件的文件名
     * @param downloadUrl
     * @return
     */
    private String getSaveFileName(String downloadUrl) {
        if (StringHelper.isEmpty(downloadUrl)) {
            return "";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        DeviceHelper.installAPK(context, apkfile);
    }
}