package com.zdjer.wms.view.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.wms.utils.R;
import com.zdjer.wms.view.helper.ActivityHelper;
import com.zdjer.wms.model.UserBLO;

import java.text.ParseException;

/**
 * Activity:注册
 */
public class WmsRegisterActivity extends BaseActivity {

    private TextView tvBack; // 返回

    private EditText etRegisteUid = null;// 账号
    private EditText etRegistePwd = null;// 密码
    private EditText etRegisteConfirmPwd = null;// 确认密码

    private Button btnRegiste = null;// 注册
    private UserBLO userBlo = null;// 用户业务逻辑

    @Override
    protected int getLayoutId() {

        return R.layout.wms_activity_registe;
    }

    /**
     * 初始化控件
     */
    public void initView() {
        tvBack = (TextView) findViewById(R.id.tv_wms_registe_back);
        tvBack.setOnClickListener(this);

        btnRegiste = (Button) findViewById(R.id.btn_wms_registe_registe);
        btnRegiste.setOnClickListener(this);

        etRegisteUid = (EditText) findViewById(R.id.et_wms_registe_uid);
        etRegisteUid.setText("");
        ViewHelper.Focus(etRegisteUid);

        etRegistePwd = (EditText) findViewById(R.id.et_wms_registe_pwd);
        etRegistePwd.setText("");

        etRegisteConfirmPwd = (EditText) findViewById(R.id.et_wms_registe_confirm_pwd);
        etRegisteConfirmPwd.setText("");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        try {
            int viewId = v.getId();
            if (viewId == R.id.tv_wms_registe_back) {
                finish();
            } else if (viewId == R.id.btn_wms_registe_registe) {
                registe();
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 注册
     */
    private void registe() throws Exception {
        // 1 验证数据合法性
        if (!isValidate()) {
            return;
        }

        // 2 添加用户
        String uid = etRegisteUid.getText().toString().trim();
        String pwd = etRegistePwd.getText().toString();
        if (userBlo == null) {
            userBlo = new UserBLO();
        }
        if (userBlo.registerUser(uid, pwd)) {// 2.1 注册成功
            ToastHelper.showToast(R.string.wms_registe_success);
            etRegisteUid.setText("");
            ViewHelper.Focus(etRegisteUid);
            etRegistePwd.setText("");
            etRegisteConfirmPwd.setText("");

        } else {// 2.2 注册失败
            ToastHelper.showToast(R.string.wms_registe_faild);
        }
    }

    /**
     * 验证输入是否有效
     *
     * @return 输入有效，返回true；反之，返回false！
     * @throws ParseException
     */
    private Boolean isValidate() throws ParseException {
        // 1 账号少于4位
        String uid = etRegisteUid.getText().toString().trim();
        if (uid.length() != 4) {
            ToastHelper.showToast(R.string.wms_registe_uid_lengtherror);
            ActivityHelper.Focus(etRegisteUid);
            return false;
        }

        // 2 密码少于6位
        String pwd = etRegistePwd.getText().toString();
        if (pwd.length() < 6) {
            ToastHelper.showToast(R.string.wms_registe_pwd_lengtherror);
            ViewHelper.Focus(etRegistePwd);
            return false;
        }

        // 3 确认密码少于6位
        String confirmpwd = etRegisteConfirmPwd.getText().toString();
        if (confirmpwd.length() < 6) {
            ToastHelper.showToast(R.string.wms_registe_confirmpwd_lengtherror);
            ViewHelper.Focus(etRegisteConfirmPwd);
            return false;
        }
        // 4 判断密码和确认密码是否一致
        if (!pwd.equals(confirmpwd)) {
            ToastHelper.showToast(R.string.wms_registe_pwd_nosame);
            ViewHelper.Focus(etRegisteConfirmPwd);
            return false;
        }

        // 5 判断账号是否存在
        if (userBlo == null) {
            userBlo = new UserBLO();
        }
        if (userBlo.isExist(uid)) {
            ToastHelper.showToast(R.string.wms_registe_uid_existed);
            ViewHelper.Focus(etRegisteUid);
            return false;
        }
        return true;
    }
}
