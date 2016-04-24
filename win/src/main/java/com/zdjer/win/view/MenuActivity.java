package com.zdjer.win.view;

import android.content.Intent;
import android.view.View;

import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.win.R;

import butterknife.OnClick;

/**
 * 主菜单
 */
public class MenuActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {

        return R.layout.activity_menu;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.tv_menu_back,
            R.id.ll_menu_in, R.id.ll_menu_out,
            R.id.ll_menu_check, R.id.ll_menu_search_barcode,
            R.id.ll_menu_search_thdnum, R.id.ll_menu_set})
    public void onClick(View v) {
        try {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.tv_menu_back: {
                    finish();
                    overridePendingTransition(R.anim.in_from_left,
                            R.anim.out_to_right);
                    break;
                }
                case R.id.ll_menu_in: {
                    //入库
                    Intent intent = new Intent(this,
                            WInActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    break;
                }
                case R.id.ll_menu_out: {
                    //出库
                    Intent intent = new Intent(this,
                            WOutActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    break;
                }
                case R.id.ll_menu_shconfirm: {
                    //收货确认
                    Intent intent = new Intent(this,
                            WSHConfigActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    break;
                }
                case R.id.ll_menu_check: {
                    //盘点
                    Intent intent = new Intent(this,
                            WCheckActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    break;
                }
                case R.id.ll_menu_search_barcode: {
                    Intent intent = new Intent(this,
                            SearchBarcodeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    break;
                }
                case R.id.ll_menu_search_thdnum: {
                    Intent intent = new Intent(this,
                            SearchThdNumActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    break;
                }
                case R.id.ll_menu_set: {
                    //盘点
                    Intent intent = new Intent(this,
                            SetActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    break;
                }
            }
        }catch (Exception e){
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }
}
