package com.zdjer.wms.view.widget;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.wms.utils.R;
import com.zdjer.wms.model.OptionBLO;
import com.zdjer.wms.bean.OptionBO;
import com.zdjer.wms.bean.core.OptionTypes;

/**
 * Created by zdj on 16/1/9.
 */
public class WmsSetDeviceNumActivity extends BaseActivity {

    private TextView tvBack = null; // 返回
    private TextView tvSave = null;// 保存
    private EditText etDeviceNum = null;// 设备编号

    private OptionBLO optionBlo = new OptionBLO();//选项操作类

    /**
     * 获得布局的Id
     *
     * @return
     */
    protected int getLayoutId() {

        return R.layout.wms_activity_set_devicenum;
    }

    @Override
    public void initView() {
        tvBack = (TextView) findViewById(R.id.tv_wms_set_devicenum_back);// 返回
        tvBack.setOnClickListener(this);

        tvSave = (TextView) findViewById(R.id.tv_wms_set_devicenum_save);
        tvSave.setOnClickListener(this);

        etDeviceNum = (EditText) findViewById(R.id.et_wms_set_devicenum);

        //获得设备编号
        String deviceNum = optionBlo.getOptionValue(OptionTypes.DeviceNum);
        etDeviceNum.setText(deviceNum);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();
        if (viewId == R.id.tv_wms_set_devicenum_back) {
            back();
        } else if (viewId == R.id.tv_wms_set_devicenum_save) {
            save();
        }
    }

    /**
     * 返回
     *
     * @author bipolor
     */
    private void back() {
        try {
            finish();
            overridePendingTransition(R.anim.in_from_left,
                    R.anim.out_to_right);
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 验证数据是否合法
     *
     * @return bool
     */
    private Boolean isValidate() {
        // 设备编号
        String deviceNum = etDeviceNum.getText().toString();
        if (deviceNum.length() != 3) {
            Toast.makeText(WmsSetDeviceNumActivity.this,
                    R.string.setting_devicenum_lengtherror,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 保存
     *
     * @author bipolor
     */
    private void save() {
        try {
            if (isValidate()) {
                // 获得值，并保存
                String deviceNum = etDeviceNum.getText().toString()
                        .trim();
                OptionBO option = new OptionBO();
                option.setOptionType(OptionTypes.DeviceNum);
                option.setOptionValue(deviceNum);

                if (!optionBlo.operateOption(option)) {
                    Toast.makeText(WmsSetDeviceNumActivity.this,
                            R.string.common_edit_failed, Toast.LENGTH_LONG).show();
                }
                finish();
                overridePendingTransition(R.anim.in_from_left,
                        R.anim.out_to_right);
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }
}
