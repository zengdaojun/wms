package com.zdjer.min.view;


import android.content.Intent;
import android.view.View;

import com.zdjer.min.R;
import com.zdjer.min.bean.MRecordType;
import com.zdjer.utils.view.base.BaseActivity;

import butterknife.OnClick;

/**
 * 主菜单
 */
public class MenuActivity extends BaseActivity {

    @Override
    protected int getLayoutId(){
        return R.layout.activity_menu;
    }

    @Override
    public void initView() {}

    @Override
    public void initData() {}

    @Override
    @OnClick({R.id.tv_menu_back,
            R.id.ll_menu_in,R.id.ll_menu_out,
            R.id.ll_menu_check,R.id.ll_menu_set})
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.tv_menu_back:{
                finish();
                overridePendingTransition(R.anim.in_from_left,
                        R.anim.out_to_right);
                break;
            }
            case R.id.ll_menu_in:{
                //入库
                Intent intent = new Intent(this,
                        MIOCActivity.class);
                intent.putExtra("mrecordType", MRecordType.in.getValue());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            }
            case R.id.ll_menu_out:{
                //出库
                Intent intent = new Intent(this,
                        MIOCActivity.class);
                intent.putExtra("mrecordType", MRecordType.out.getValue());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            }
            case R.id.ll_menu_check:{
                //盘点
                Intent intent = new Intent(this,
                        MIOCActivity.class);
                intent.putExtra("mrecordType", MRecordType.check.getValue());
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            }
            case R.id.ll_menu_set:{
                //设置
                Intent intent = new Intent(this,
                        SetActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                break;
            }
        }
    }
}
