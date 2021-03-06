package com.kxf.inventorymanager.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kxf.inventorymanager.AppConfig;
import com.kxf.inventorymanager.MyApplication;
import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.entity.User;
import com.kxf.inventorymanager.utils.FormatUtils;
import com.kxf.inventorymanager.utils.LogUtil;


public class BaseActivity extends Activity {

    private String tag = "";
    protected Activity mActivity;
    protected Context mContext;
    protected User user;
    protected AlertDialog dialog;

    protected ImageView top_iv_left_icon;
    protected TextView top_tv_title, top_tv_right;

    private boolean isNeedAdapta = true;
    public static final int msg_base_http_erro = 2000;
    protected final Handler handlerBase = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.d("msg.what=" + msg.what);
            switch (msg.what) {
                case msg_base_http_erro:
                    String str = (String) msg.obj;
                    showDialogYes(str);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity = this;
        mContext = this;
        tag = this.getPackageName() + "." + this.getLocalClassName();
        tag = "do in " + tag + AppConfig.BASE_ACTIVITY_LOG_INFO_STRING;
        LogUtil.e(tag);
        updateLoginUser();
    }

    protected void updateLoginUser(){
        String userStr = MyApplication.getShare(LoginActivity.KEY_USER);
        Gson gson = new Gson();
        user = gson.fromJson(userStr, User.class);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initTitle();
    }

    private void initTitle() {
        LogUtil.d("initTitle()");
        top_iv_left_icon = (ImageView) findViewById(R.id.top_iv_left_icon);
        LogUtil.d("initTitle() top_iv_left_icon=" + top_iv_left_icon);
        if (null == top_iv_left_icon){
            return;
        }
        top_tv_title = (TextView) findViewById(R.id.top_tv_title);
        top_tv_right = (TextView) findViewById(R.id.top_tv_right);
        setLeftClick(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e(tag);
        if (null != dialog && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
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

    protected void showDialog(final String msg, final String leftButtonTitle, final Runnable leftRun, final String rightButtonTitle, final Runnable rightRun) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null != dialog && dialog.isShowing()){
                    dialog.dismiss();
                    dialog = null;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(FormatUtils.FormatStringLen(msg, 50, FormatUtils.ALIGN.CENTER, " ")).setCancelable(false).setTitle("提示");
                if (!TextUtils.isEmpty(leftButtonTitle)){
                    builder.setPositiveButton(leftButtonTitle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(null != leftRun){
                                mActivity.runOnUiThread(leftRun);
                            }
                        }
                    });
                }
                if (!TextUtils.isEmpty(rightButtonTitle)){
                    builder.setNeutralButton(rightButtonTitle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(null != rightRun){
                                mActivity.runOnUiThread(rightRun);
                            }
                        }
                    });
                }
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    protected void showDialogYes(String msg){
        showDialog(msg, "确定", null, null, null);
    }

    protected void setLeftClick(final Runnable run){
        if (null != top_iv_left_icon){
            top_iv_left_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.runOnUiThread(run);
                }
            });
        }
    }

    protected void setRightInfo(String text, View.OnClickListener listener){
        if (null != top_tv_right){
            top_tv_right.setText(text);
            top_tv_right.setOnClickListener(listener);
            autoMatchFont(top_tv_right);
        }
    }

    protected void setTopTitle(String text){
        if (null != top_tv_title){
            top_tv_title.setText(text);
        }
    }

    protected void autoMatchFont(final TextView view){
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                float vWidth = v.getWidth();
                LogUtil.d("vWidth=" + vWidth);
                TextPaint paint = view.getPaint();
                String text = view.getText().toString();
                float textLen = paint.measureText(text);
                LogUtil.d("textLen=" + textLen);
                float oldSize = view.getTextSize();
                LogUtil.d("oldSize=" + oldSize);
                if (textLen != vWidth && isNeedAdapta){
                    isNeedAdapta = false;
                    float size = vWidth * oldSize / textLen;
                    LogUtil.d("size=" + size);
                    view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                }
            }
        });
    }
}
