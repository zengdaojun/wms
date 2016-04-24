package com.zdjer.wms.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.zdjer.utils.DateHelper;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.AppHelper;
import com.zdjer.utils.view.DeviceHelper;
import com.zdjer.utils.view.SPHelper;
import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.utils.view.dialog.DialogHelper;
import com.zdjer.wms.utils.R;
import com.zdjer.wms.bean.OptionBO;
import com.zdjer.wms.bean.UserBO;
import com.zdjer.wms.bean.core.OptionTypes;
import com.zdjer.wms.utils.MD5Helper;
import com.zdjer.wms.utils.WmsNetApiHelper;
import com.zdjer.wms.model.OptionBLO;
import com.zdjer.wms.model.UserBLO;
import com.zdjer.wms.view.user.WmsEditpwdActivity;
import com.zdjer.wms.view.user.WmsRegisterActivity;
import com.zdjer.wms.view.user.WmsResetpwdAvtivity;
import com.zdjer.wms.view.widget.WmsSetDeviceNumActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Activity:登录
 */
public class WmsLoginActivity extends BaseActivity {

    private RadioGroup rgServer = null;//在线状态

    private EditText etUid = null;// 账号
    private EditText etPwd = null;// 密码

    private Button btLogin = null;// 登录

    private CheckBox cbSavepwd = null;// 记住密码
    private TextView tvResetpwd = null;// 忘记密码
    private TextView tvEditpwd = null;// 修改密码

    private Button btnToRegister = null; // 注册

    //private SharedPreferences sharedPreferences = null;
    private UserBLO userBlo = new UserBLO();// 用户业务逻辑
    private UserBO user = new UserBO();
    private OptionBLO optionBlo = new OptionBLO();// 选项

    /**
     * 获得布局的Id
     *
     * @return
     */
    protected int getLayoutId() {

        return R.layout.wms_activity_login;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            *//*if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            if(this.getIsKT40()){
                //判断设备是否授权
                StringBuffer msg = new StringBuffer();
                if (!isGrant(msg)) {
                    Toast.makeText(LoginActivity.this, msg.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }*//*

            *//*UmengUpdateAgent.setUpdateOnlyWifi(false);
            UmengUpdateAgent.update(this);

            // 1.创建数据库
            DBManager.createDatbase(this);*//*

            // 2.加载布局
            *//*setContentView(R.wms_activity_login.activity_login);

            Settings.System.putInt(getContentResolver(), "status_bar_disabled", 1);

            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }*//*

            // 4.初始化View
            //initView();

        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }*/

    /**
     * 初始化数据
     *
     * @throws Exception
     */
    @Override
    public void initView() {
        //在线状态
        rgServer = (RadioGroup) findViewById(R.id.rg_wms_login_server);

        // Uid
        etUid = (EditText) findViewById(R.id.et_wms_login_uid);
        // Pwd
        etPwd = (EditText) findViewById(R.id.et_wms_login_pwd);
        // 登陆按钮
        btLogin = (Button) findViewById(R.id.btn_wms_login);
        btLogin.setOnClickListener(this);

        cbSavepwd = (CheckBox) findViewById(R.id.cb_wms_savepwd);

        tvResetpwd = (TextView) findViewById(R.id.tv_wms_resetpwd);
        tvResetpwd.setOnClickListener(this);

        // 修改密码
        tvEditpwd = (TextView) findViewById(R.id.tv_wms_editpwd);
        tvEditpwd.setOnClickListener(this);

        // 注册
        btnToRegister = (Button) findViewById(R.id.btn_wms_register);
        btnToRegister.setOnClickListener(this);

        // 1.初始化用户信息
        Boolean isSavepwd = SPHelper.get("isSavepwd", false);// 是否保存密码
        cbSavepwd.setChecked(isSavepwd);
        // 1.1 记住密码，则将用户信息初始化到界面中
        if (isSavepwd) {
            // 获得用户名和密码
            String uid = SPHelper.get("uid", "");
            String pwd = SPHelper.get("pwd", "");
            int server = SPHelper.get("server", 0);
            etUid.setText(uid);
            etPwd.setText(pwd);
            rgServer.check(server);
        }
    }


    /**
     * 处理服务IP(该函数不处理任何逻辑错误)
     *
     * @param hashMapIP
     * @return
     */
    private void handleServerIP(HashMap<OptionTypes, String> hashMapIP) {
        OptionBO option = null;
        if (optionBlo == null) {
            optionBlo = new OptionBLO();
        }

        for (OptionTypes optionType : hashMapIP.keySet()) {
            String optionValue = hashMapIP.get(optionType);

            option = new OptionBO();
            option.setOptionType(optionType);
            option.setOptionValue(optionValue);
            optionBlo.operateOption(option);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        try {
            int viewId = v.getId();
            if (viewId == R.id.btn_wms_login) {
                login();
            } else if (viewId == R.id.tv_wms_resetpwd) {
                resetPwd();
            } else if (viewId == R.id.tv_wms_editpwd) {
                editPwd();
            } else if (viewId == R.id.btn_wms_register) {
                registe();
            }
        } catch (Exception e) {
            ToastHelper.showToast(R.string.wms_common_exception);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int timeout = SPHelper.get("timeout", 0);
        int screenBrightness = SPHelper.get("screenBrightness", 0);
        if (timeout != 0) {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, timeout);
        }
        if (screenBrightness != 0) {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightness);
        }
    }


    /**
     * 登录
     *
     * @author bipolor
     */
    private void login() throws Exception {

        showWaitDialog("登录中…");
        // 1 验证输入合法性
        if (!isValidate()) {
            return;
        }

        // 2 验证用户名和密码正确性
        final String uid = etUid.getText().toString().trim();
        final String pwd = etPwd.getText().toString();

        if (DeviceHelper.hasInternet()) {
            //获取IP地址(只是希望更新一下服务的IP)
            WmsNetApiHelper.getServerIP(WmsApplication.serverIP, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    HashMap<OptionTypes, String> hashMapIP = new HashMap<OptionTypes, String>();

                    try {
                        if (response.getBoolean("flag")) {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

                                String name = jsonObject.getString("name");
                                String ip = jsonObject.getString("ip");
                                if ("服务器一".equals(name)) {
                                    hashMapIP.put(OptionTypes.Server1, ip);
                                } else if ("服务器二".equals(name)) {
                                    hashMapIP.put(OptionTypes.Server2, ip);
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handleServerIP(hashMapIP);
                    hideWaitDialog();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    hideWaitDialog();
                }
            });
        }

        // 3 设置选择的服务器，并在SharedPreferences 设置Server及IP
        if (saveServerAndIP() == false) {
            return;
        }

        String ip = SPHelper.get("ip", "");
        if (!StringHelper.isEmpty(ip)) {
            //联网
            WmsNetApiHelper.login(ip, uid, pwd, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    handleLogin(response, uid, pwd);
                    hideWaitDialog();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    hideWaitDialog();
                }
            });

        }
        UserBO user = userBlo.getUser(uid, MD5Helper.ConvertToMD5(pwd));
        if (user == null) {
            ToastHelper.showToast(R.string.wms_login_failed);
            ViewHelper.Focus(etUid);
        } else {
            int timeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            int screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 40 * 60 * 1000);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255 / 2);

            SPHelper.put("timeout", timeout);
            SPHelper.put("screenBrightness", screenBrightness);

            // 5 记住密码
            if (cbSavepwd.isChecked()) {
                SPHelper.put("isSavepwd", true);
            }

            // 6保存当前用户信息
            SPHelper.put("userId", user.getUserId());// 用户Id
            SPHelper.put("uid", uid);// 用户Uid
            SPHelper.put("pwd", pwd);// 用户密码
            SPHelper.put("token", user.getToken());// 令牌
            SPHelper.put("isAdmin", user.getIsAdmin().getValue());// 是否是管理员

            if (optionBlo == null) {
                optionBlo = new OptionBLO();
            }
            String deviceNum = optionBlo.getOptionValue(OptionTypes.DeviceNum);// 获取设备信息

            if (deviceNum.length() == 0) {
                // 7.1 跳转页面
                Intent intent = new Intent(WmsLoginActivity.this,
                        WmsSetDeviceNumActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
            } else {
                loginSuccess();
            }
        }
    }

    /**
     * 处理登录
     *
     * @param response
     * @param uid
     * @param pwd
     */
    private void handleLogin(JSONObject response, String uid, String pwd) {
        try {
            boolean flag = response.getBoolean("flag");
            if (flag) {
                //1.1.1 在线登录成功
                String token = response.getString("token");
                //判断账号在本地是否存在，若存在则修改，若不存在，则添加
                if (userBlo.isExist(uid)) {
                    //存在，则修改，修改成功，则获取用户信息
                    if (!userBlo.update(uid, MD5Helper.ConvertToMD5(pwd), token)) {
                        ToastHelper.showToast("更新用户出错");
                        return;
                    }
                } else {
                    //不存在，则添加，添加成功，则获取用户信息
                    UserBO userBo = new UserBO();
                    userBo.setUid(uid);
                    userBo.setPwd(MD5Helper.ConvertToMD5(pwd));
                    userBo.setToken(token);
                    userBo.setCreateDate(DateHelper.getCurrDate());
                    userBo.setEditDate(DateHelper.getCurrDate());
                    if (!userBlo.insert(userBo)) {
                        ToastHelper.showToast("本地添加用户出错");
                        return;
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    // 重置密码
    private void resetPwd() {
        // 1 跳转页面
        Intent intent = new Intent(WmsLoginActivity.this,
                WmsResetpwdAvtivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
    }

    // 修改密码
    private void editPwd() {
        // 1 跳转页面
        Intent intent = new Intent(WmsLoginActivity.this,
                WmsEditpwdActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
    }

    // 注册
    private void registe() {
        // 1 跳转页面
        Intent intent = new Intent(WmsLoginActivity.this,
                WmsRegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);
    }

    /**
     * 登录成功
     */
    protected void loginSuccess() {

    }

    /**
     * 保存Server和IP
     *
     * @return bool
     * @throws Exception
     */
    private Boolean saveServerAndIP() throws Exception {
        int checkRadioButtonId = rgServer.getCheckedRadioButtonId();
        String ip = "";
        if (R.id.rg_wms_login_local == checkRadioButtonId) {
            ip = "";
        } else {
            if (optionBlo == null) {
                optionBlo = new OptionBLO();
            }
            if (R.id.rg_wms_login_server1 == checkRadioButtonId) {
                ip = optionBlo.getOptionValue(OptionTypes.Server1);
            } else if (R.id.rg_wms_login_server2 == checkRadioButtonId) {
                ip = optionBlo.getOptionValue(OptionTypes.Server2);
            }
            if (StringHelper.isEmpty(ip)) {
                ToastHelper.showToast("无法获取服务器IP");
                return false;
            }
        }
        SPHelper.put("server", checkRadioButtonId);
        SPHelper.put("ip", ip);
        return true;
    }

    /**
     * 验证输入是否有效
     *
     * @return 输入有效，返回true；反之，返回false！
     * @throws ParseException
     */
    private Boolean isValidate() throws ParseException {
        // 账号不够4位
        String uid = etUid.getText().toString().trim();
        if (uid.length() == 0) {

            Toast.makeText(WmsLoginActivity.this, R.string.wms_login_uid_lengtherror,
                    Toast.LENGTH_LONG).show();
            ViewHelper.Focus(etUid);
            return false;
        }

        // 密码小于6位
        String pwd = etPwd.getText().toString();
        if (pwd.length() == 0) {
            ToastHelper.showToast(R.string.wms_login_pwd_lengtherror);
            ViewHelper.Focus(etPwd);
            return false;
        }
        return true;
    }


    /**
     * 退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            DialogHelper.getConfirmDialog(this, "您确定要退出本程序吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppHelper.appExit();
                }
            }).show();
            return true;
        }
        return false;
    }

    /**
     * 返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String deviceNum = optionBlo.getOptionValue(OptionTypes.DeviceNum);// 获取设备信息
            if (deviceNum.length() != 0) {
                loginSuccess();
            }
        }
    }

    /**
     * 是否授权
     *
     * @param msg 错误消息
     * @return
     * @throws Exception
     */
    private boolean isGrant(StringBuffer msg) throws Exception {
        //return true;
        String phoneNumber = "";
        String deviceID = "";
        String deviceType = "Android_" + Build.VERSION.SDK_INT;

        Context applicationContext = this.getApplicationContext();

        final TelephonyManager telephonyManager = (TelephonyManager) applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            deviceID = telephonyManager.getDeviceId();
            phoneNumber = telephonyManager.getLine1Number();
            if (StringHelper.isEmpty(phoneNumber)) {
                phoneNumber = telephonyManager.getSubscriberId();
            }
        }

        if (StringHelper.isEmpty(deviceID)) {
            try {
                deviceID = (String) Build.class.getDeclaredField("SERIAL").get(Build.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (StringHelper.isEmpty(deviceID)) {
                deviceID = Settings.Secure.getString(applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return WmsNetApiHelper.isGrant(phoneNumber, deviceID, deviceType, msg);
    }
}
