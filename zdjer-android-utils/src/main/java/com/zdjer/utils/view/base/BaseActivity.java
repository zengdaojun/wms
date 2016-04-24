package com.zdjer.utils.view.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;

import com.zdjer.utils.R;
import com.zdjer.utils.view.AppHelper;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.utils.view.dialog.IDialog;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity implements IDialog, IBaseActivityView,OnClickListener {

    private boolean isVisible = false;
    private Dialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);

            if (getLayoutId() != 0) {
                setContentView(getLayoutId());
            }

            AppHelper.addActivity(this);

            isVisible = true;

            //通过注解的方式绑定控件
            ButterKnife.bind(this);

            initView();

            initData();
        }catch(Exception e){

            Log.i("E",e.getMessage());

        }
    }

    /**
     * 获得布局的Id
     * @return
     */
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 隐藏等待对话框
     */
    @Override
    public void hideWaitDialog() {

        if (isVisible && waitDialog != null) {
            try {
                waitDialog.dismiss();
                waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 显示等待对话框
     *
     * @return
     */
    @Override
    public Dialog showWaitDialog() {
        return showWaitDialog(R.string.zdjer_loading);
    }

    /**
     * 显示等待对话框
     *
     * @param resId
     * @return
     */
    @Override
    public Dialog showWaitDialog(int resId) {
        return showWaitDialog(getString(resId));
    }

    /**
     * 显示等等对话框
     *
     * @param message
     * @return
     */
    @Override
    public Dialog showWaitDialog(String message) {
        if (isVisible) {
            if (waitDialog == null) {
                waitDialog = DialogHelper.getWaitDialog(this, message);
            }
            if (waitDialog != null) {
                //waitDialog.setMessage(message);
                waitDialog.show();
            }
            return waitDialog;
        }
        return null;
    }
}
