package com.zdjer.wms.view.user;

import android.content.res.Resources.NotFoundException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.wms.utils.R;
import com.zdjer.wms.bean.UserBO;
import com.zdjer.wms.utils.MD5Helper;
import com.zdjer.wms.model.UserBLO;
import com.zdjer.wms.view.helper.ActivityHelper;

import java.text.ParseException;

/**
 * Dialog 修改密码
 *
 * @author bipolor
 */
public class WmsEditpwdActivity extends BaseActivity {

    private TextView tv_back = null;

    private EditText etUid = null;// 账号
    private EditText etOldPwd = null;// 密码
    private EditText etNewPwd = null;// 新密码
    private EditText etConfirmNewPwd = null;// 新密码

    private Button btnConfirm = null;// 确定

    private UserBLO userBlo = null;// 用户业务逻辑
    private UserBO editUser = null;// 须修改的用户

    @Override
    protected int getLayoutId() {

        return R.layout.wms_activity_editpwd;
    }

    // 初始化控件
    public void initView() {

        tv_back = (TextView) findViewById(R.id.tv_wms_editpwd_back);
        tv_back.setOnClickListener(this);

        etUid = (EditText) findViewById(R.id.et_wms_editpwd_uid);
        etUid.setText("");

        etOldPwd = (EditText) findViewById(R.id.et_wms_editpwd_oldpwd);
        etOldPwd.setText("");

        etNewPwd = (EditText) findViewById(R.id.et_wms_editpwd_newpwd);
        etNewPwd.setText("");

        etConfirmNewPwd = (EditText) findViewById(R.id.et_wms_editpwd_confirm_newpwd);
        etConfirmNewPwd.setText("");

        //确定
        btnConfirm = (Button) findViewById(R.id.btn_wms_editpwd_confirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        try {
            int viewId = v.getId();
            if (viewId == R.id.tv_wms_editpwd_back) {
                finish();
            } else if (viewId == R.id.btn_wms_editpwd_confirm) {
                confirm();
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    /**
     * 确定
     */
    private void confirm() throws Exception{
        //1 验证输入的合法性
        if (!isValidate()) {
            return;
        }

        // 2 修改密码
        String newPwd = etNewPwd.getText().toString();
        if (userBlo == null) {
            userBlo = new UserBLO();
        }
        if (editUser == null) {
            return;
        }
        if (userBlo.editPwd(editUser.getUserId(), newPwd)) {
            ToastHelper.showToast(R.string.wms_editpwd_success);
            finish();
        } else {
            ToastHelper.showToast(R.string.wms_editpwd_faild);
        }
    }

    // 验证输入是否有效
    private Boolean isValidate() throws NotFoundException, ParseException {
        // 1 账号少于4位
        String uid = etUid.getText().toString();
        if (uid.length() != 4) {
            ToastHelper.showToast(R.string.wms_editpwd_uid_lengtherror);
            ViewHelper.Focus(etUid);
            return false;
        }

        // 2 密码少于6位
        String oldPwd = etOldPwd.getText().toString();
        if (oldPwd.length() < 6) {
            ToastHelper.showToast(R.string.wms_editpwd_oldpwd_lengtherror);
            ViewHelper.Focus(etOldPwd);
            return false;
        }

        // 3 新密码少于6位
        String newPwd = etNewPwd.getText().toString();
        if (newPwd.length() < 6) {
            ToastHelper.showToast(R.string.wms_editpwd_newpwd_lengtherror);
            ViewHelper.Focus(etNewPwd);
            return false;
        }

        // 4 确认密码少于6位
        String confirmNewpwd = etConfirmNewPwd.getText().toString();
        if (confirmNewpwd.length() < 6) {
            ToastHelper.showToast(R.string.wms_editpwd_confirmnewpwd_lengtherror);
            ViewHelper.Focus(etConfirmNewPwd);
            return false;
        }

        // 7 两次密码输入不一致
        if (!newPwd.equals(confirmNewpwd)) {
            ToastHelper.showToast(R.string.wms_editpwd_newpwd_nosame);
            ViewHelper.Focus(etConfirmNewPwd);
            return false;
        }

        // 8 判断用户是否存在
        if (userBlo == null) {
            userBlo = new UserBLO();
        }
        editUser = userBlo.getUser(uid);
        if (editUser == null) {
            ToastHelper.showToast(R.string.wms_editpwd_uid_noexist);
            ActivityHelper.Focus(etUid);
            return false;
        }

        // 9 判断旧密码是否正确
        if (!MD5Helper.ConvertToMD5(oldPwd).equals(editUser.getPwd())) {
            ToastHelper.showToast(R.string.wms_editpwd_oldpwd_error);
            ActivityHelper.Focus(etOldPwd);
            return false;
        }
        return true;
    }
}
