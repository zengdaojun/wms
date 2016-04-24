package com.zdjer.min.view;

import android.content.Intent;

import com.zdjer.wms.view.WmsLoginActivity;

/**
 * Activity:登录
 */
public class LoginActivity extends WmsLoginActivity {

    /**
     * 登录成功
     */
    protected void loginSuccess() {
        Intent intent = new Intent(LoginActivity.this,
                MenuActivity.class);
        startActivity(intent);
    }
}
