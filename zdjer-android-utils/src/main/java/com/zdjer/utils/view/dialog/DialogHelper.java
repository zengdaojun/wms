package com.zdjer.utils.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zdjer.utils.R;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.DeviceHelper;

/**
 * @author zdj
 */
public class DialogHelper {

    /**
     * 获得一个等待对话框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context,R.style.zdjer_dialog);
        if (!StringHelper.isEmpty(message)) {
            progressDialog.setMessage(message);
        }
        return progressDialog;
    }

    /**
     * 创建访问对话框
     * @param context 上下文
     * @param message 提示
     * @return
     */
    public static Dialog getWaitDialog(Context context, String message){

        final Dialog dialog = new Dialog(context, R.style.zdjer_dialog);
        dialog.setContentView(R.layout.zdjer_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        float width = DeviceHelper.getScreenWidth();
        lp.width = (int)(0.6 * width);

        TextView textView = (TextView) dialog.findViewById(R.id.tv_dialog_message);
        if(!StringHelper.isEmpty(message)){
            textView.setText(message);
        }
        return dialog;
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     *
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getAlertDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.zdjer_text_confirm, onClickListener);
        return builder;
    }


    public static AlertDialog.Builder getMessageDialog(Context context, String message) {
        return getMessageDialog(context, message, null);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getAlertDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(R.string.zdjer_text_confirm, onClickListener);
        builder.setNegativeButton(R.string.zdjer_text_cancel, null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onOkClickListener, DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getAlertDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.zdjer_text_confirm, onOkClickListener);
        builder.setNegativeButton(R.string.zdjer_text_cancel, onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String title,String message,
                                                       String okString, DialogInterface.OnClickListener onOkClickListener,
                                                       String calcelString,DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getAlertDialog(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okString, onOkClickListener);
        builder.setNegativeButton(calcelString, onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getAlertDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton(R.string.zdjer_text_cancel, null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getAlertDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton(R.string.zdjer_text_cancel, null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }

    /**
     * @param context
     * @return
     */
    public static AlertDialog.Builder getAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.zdjer_dialog);
        return builder;
    }
}
