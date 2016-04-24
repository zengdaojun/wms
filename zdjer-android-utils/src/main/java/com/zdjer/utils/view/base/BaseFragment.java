package com.zdjer.utils.view.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdjer.utils.R;
import com.zdjer.utils.view.AppContext;
import com.zdjer.utils.view.dialog.IDialog;

/**
 * Created by zdj on 16/4/12.
 */
public class BaseFragment extends Fragment implements View.OnClickListener, IBaseFragmentView {

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;

    protected LayoutInflater layoutInflater;

    public AppContext getApplication() {
        return (AppContext) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.layoutInflater = layoutInflater;

        super.onCreateView(layoutInflater, container, savedInstanceState);


        View view = this.layoutInflater.inflate(getLayoutId(), container, false);


        // 2 初始化组件
        //initializeComponent();

        // 3 初始化数据
        //initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return this.layoutInflater.inflate(resId, null);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void hideWaitDialog() {
        Activity activity = this.getActivity();
        if (activity instanceof IDialog) {
            ((IDialog) activity).hideWaitDialog();
        }
    }

    protected Dialog showWaitDialog() {
        return showWaitDialog(R.string.zdjer_loading);
    }

    protected Dialog showWaitDialog(int resid) {
        Activity activity = getActivity();
        if (activity instanceof IDialog) {
            return ((IDialog) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected Dialog showWaitDialog(String str) {
        Activity activity = getActivity();
        if (activity instanceof IDialog) {
            return ((IDialog) activity).showWaitDialog(str);
        }
        return null;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
