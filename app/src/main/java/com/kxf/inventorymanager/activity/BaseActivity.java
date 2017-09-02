package com.kxf.inventorymanager.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.kxf.inventorymanager.AppConfig;
import com.kxf.inventorymanager.MyApplication;
import com.kxf.inventorymanager.entity.User;
import com.kxf.inventorymanager.utils.LogUtil;

import org.xutils.ex.DbException;

public class BaseActivity extends Activity {

    private String tag = "";
    protected Activity mActivity;
    protected Context mContext;
    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity = this;
        mContext = this;
        tag = this.getPackageName() + "." + this.getLocalClassName();
        tag = "do in " + tag + AppConfig.BASE_ACTIVITY_LOG_INFO_STRING;
        LogUtil.e(tag);
        String userName = MyApplication.getShare(LoginActivity.KEY_USER_NAME);
        try {
            user = MyApplication.db().selector(User.class).where("name", "=", userName).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e(tag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e(tag);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e(tag);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e(tag);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e(tag);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e(tag);
    }

    @Override
    public void finish() {
        super.finish();
        LogUtil.e(tag);
    }
}
