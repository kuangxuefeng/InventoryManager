package com.kxf.inventorymanager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kxf.inventorymanager.BuildConfig;
import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.entity.User;

public class LoginActivity extends BaseActivity implements OnClickListener {

    private String TAG = "LoginActivity";
    private User user = null;
    private EditText et_name = null;
    private EditText et_pw = null;
    private Button btn_login = null;
    private Button btn_cancel = null;
    private Button btn_join = null;//btn_join
    private TextView tv_ver_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLogin()) {
            //启动主界面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);
        init();
    }

    private boolean isLogin() {
//		Log.i(TAG, "isLogin()");
//		AppConfig ac = (AppConfig) SharedPreferencesUtil.getObject(this, SharedPreferencesUtil.SHARE_PRE_APP_CONFIG);
//		if (ac!=null) {
//			Log.i(TAG, "isLogin()=="+ac.isLogin());
//			return ac.isLogin();
//		}
//		Log.i(TAG, "isLogin()==false");
        return false;

    }

    private void init() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_pw = (EditText) findViewById(R.id.et_pw);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_join.setOnClickListener(this);
        btn_login.setEnabled(true);
        btn_cancel.setEnabled(true);
        btn_join.setEnabled(true);
        tv_ver_info = (TextView) findViewById(R.id.tv_ver_info);
        tv_ver_info.setText("build in " + BuildConfig.buileDateTime);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                //启动主界面
//			Intent intent=new Intent(LoginActivity.this,MainActivity.class);

//			Intent intent=new Intent(LoginActivity.this,ShowChartActivity.class);
//			startActivity(intent);
//			LoginActivity.this.finish();

                btn_login.setEnabled(false);
                btn_cancel.setEnabled(false);
                btn_join.setEnabled(false);
//                checkInput();
                handler.sendEmptyMessage(1000);
                break;

            case R.id.btn_cancel:
                btn_login.setEnabled(false);
                btn_cancel.setEnabled(false);
                btn_join.setEnabled(false);
                finish();
                break;
            case R.id.btn_join:
                btn_login.setEnabled(false);
                btn_cancel.setEnabled(false);
                btn_join.setEnabled(false);

                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    /**
     * 打开的activity返回结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult()......");
        btn_login.setEnabled(true);
        btn_cancel.setEnabled(true);
        btn_join.setEnabled(true);
        Log.i(TAG, "requestCode=" + requestCode + "  resultCode=" + resultCode + "  data=" + data);
        if (data != null && requestCode == 0) {
            Log.i(TAG, "data!=null&&resultCode==RESULT_OK");
            et_name.setText(data.getStringExtra("name"));
            et_pw.setText(data.getStringExtra("pw"));
        }
    }

    ;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "msg.what=" + msg.what);
            switch (msg.what) {
                case 1000:
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    // 共享信息
//                    Log.i(TAG, "user=" + user);
//                    SharedPreferencesUtil.saveObject(user, getApplicationContext(), SharedPreferencesUtil.SHARE_PRE_USER_NAME);
//                    AppConfig ac = new AppConfig();
//                    ac.setLogin(true);
//                    Log.i(TAG, "AppConfig=" + ac);
//                    SharedPreferencesUtil.saveObject(ac, getApplicationContext(), SharedPreferencesUtil.SHARE_PRE_APP_CONFIG);
//                    Log.i(TAG, "共享信息保存成功！");
                    //启动主界面
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    btn_login.setEnabled(true);
                    btn_cancel.setEnabled(true);
                    LoginActivity.this.finish();
                    break;
                case 1001:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    showDialog("用户不存在！");
                    et_name.setText(null);
                    et_pw.setText(null);
                    btn_login.setEnabled(true);
                    btn_cancel.setEnabled(true);
                    break;
                case 1002:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    showDialog("密码错误！");
                    et_pw.setText(null);
                    btn_login.setEnabled(true);
                    btn_cancel.setEnabled(true);
                    break;
                case 1003:
                    showDialog("网络连接异常，请检查网络！");
                    btn_login.setEnabled(true);
                    btn_cancel.setEnabled(true);
                    break;
                case 1004:
                    showDialog("网络传输异常，请重试！");
                    btn_login.setEnabled(true);
                    btn_cancel.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

//    // 检查用户名和密码是否为空，和密码是否正确
//    private void checkInput() {
//        if ("".equals(et_name.getText().toString().trim())) {
//            showDialog("请输入用户名");
//            btn_login.setEnabled(true);
//            btn_cancel.setEnabled(true);
//            return;
//        }
//        if ("".equals(et_pw.getText().toString().trim())) {
//            showDialog("请输入密码");
//            btn_login.setEnabled(true);
//            btn_cancel.setEnabled(true);
//            return;
//        }
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                String result = null;
//                result = query(et_name.getText().toString().trim(), et_pw
//                        .getText().toString().trim());
//                Log.i(TAG, "result=" + result);
//                if (result != null) {
//                    if (result.equals("0")) {
//                        //登录失败,用户不存在
//                        handler.sendEmptyMessage(1001);
//                    } else if (result.equals("-1")) {
//                        //登录失败,密码错误
//                        handler.sendEmptyMessage(1002);
//                    } else {
//                        //登录成功
//                        user = JsonUtil.jsonToUser(result);
//                        Log.i(TAG, "user=" + user);
//                        handler.sendEmptyMessage(1000);
//                    }
//                }
//            }
//        }).start();
//    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

//	// 根据用户名称密码查询
//	private String query(String account, String password) {
//		// 查询参数
//		String queryString = "name=" + account + "&password=" + password;
//		// url
//		String url = HttpUtil.BASE_URL + "servlet/LoginServlet?" + queryString;
//		Log.i(TAG, "url=" + url);
//		// 查询返回结果
//		try {
//			return HttpUtil.queryStringForPost(url);
//		} catch (ClientProtocolException e) {
//			handler.sendEmptyMessage(1003);
//		} catch (IOException e) {
//			handler.sendEmptyMessage(1004);
//		}
//		return null;
//	}


}
