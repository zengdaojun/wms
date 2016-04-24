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
import com.zdjer.wms.bean.core.YesNos;
import com.zdjer.wms.utils.MD5Helper;
import com.zdjer.wms.model.UserBLO;

import java.text.ParseException;

/**
 * Dialog 重置密码
 *
 * @author bipolor
 */
public class WmsResetpwdAvtivity extends BaseActivity {

    private TextView tv_back = null;

    private EditText etUid = null;// 账号
    private EditText etManagerUid = null;// 管理员
    private EditText etManagerPwd = null;// 密码

    private Button btnReset = null;// 重置

    private UserBLO userBlo = null;// 用户业务逻辑
    private UserBO editUser = null;// 须修改的用户

    /**
     * 获得布局的Id
     *
     * @return
     */
    protected int getLayoutId() {

        return R.layout.wms_activity_resetpwd;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        tv_back = (TextView) findViewById(R.id.tv_wms_resetpwd_back);
        tv_back.setOnClickListener(this);

        etUid = (EditText) findViewById(R.id.et_wms_resetpwd_uid);
        etUid.setText("");

        etManagerUid = (EditText) findViewById(R.id.et_wms_resetpwd_manageruid);
        etManagerUid.setText("");

        etManagerPwd = (EditText) findViewById(R.id.et_wms_resetpwd_managerpwd);
        etManagerPwd.setText("");

        btnReset = (Button) findViewById(R.id.btn_wms_resetpwd_reset);
        btnReset.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        try {
            int viewId = v.getId();
            if (viewId == R.id.tv_wms_resetpwd_back) {
                finish();
            } else if (viewId == R.id.btn_wms_resetpwd_reset) {
                reset();
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    // 重置
    private void reset() throws Exception {
        //1 验证数据输入合法性
        if (!isValidate()) {
            return;
        }
        //2 重置密码
        if (userBlo == null) {
            userBlo = new UserBLO();
        }
        if (userBlo.resetPwd(editUser.getUserId())) {
            ToastHelper.showToast(R.string.wms_resetpwd_reset_success);
            finish();
        } else {
            ToastHelper.showToast(R.string.wmd_resetpwd_reset_faild);
        }

    }

    /**
     * 验证数据输入是否合法
     *
     * @return
     * @throws NotFoundException
     * @throws ParseException
     */
    private Boolean isValidate() throws Exception {
        // 1 账号少于4位
        String uid = etUid.getText().toString();
        if (uid.length() != 4) {
            ToastHelper.showToast(R.string.wms_resetpwd_uid_lengtherror);
            ViewHelper.Focus(etUid);
            return false;
        }
        // 2 管理员输入少于4位
        String managerUid = etManagerUid.getText().toString();
        if (managerUid.length() != 4) {
            ToastHelper.showToast(R.string.wms_resetpwd_manageruid_lengtherror);
            ViewHelper.Focus(etManagerUid);
            return false;
        }
        // 3 管理员密码输入少于4位
        String managerPwd = etManagerPwd.getText().toString();
        if (managerPwd.length() < 6) {
            ToastHelper.showToast(R.string.wms_resetpwd_managerpwd_lengtherror);
            ViewHelper.Focus(etManagerPwd);
            return false;
        }
        // 4 判断账号是否存在
        if (userBlo == null) {
            userBlo = new UserBLO();
        }
        editUser = userBlo.getUser(uid);
        if (editUser == null) {
            ToastHelper.showToast(R.string.wms_resetpwd_uid_noexist);
            ViewHelper.Focus(etUid);
            return false;
        }
        // 5.判断管理员账号是否存在
        if (userBlo == null) {
            userBlo = new UserBLO();
        }
        UserBO managerUser = userBlo.getUser(managerUid, YesNos.Yes);
        if (managerUser == null) {
            ToastHelper.showToast(R.string.wms_resetpwd_manageruid_noexist);
            return false;
        }

        // 6 管理员密码不正确
        managerPwd = MD5Helper.ConvertToMD5(managerPwd);
        if (!managerPwd.equals(managerUser.getPwd())) {
            ToastHelper.showToast(R.string.wms_resetpwd_managerpwd_error);
            return false;
        }
        return true;
    }
}
