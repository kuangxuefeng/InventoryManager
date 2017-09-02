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
import android.widget.Toast;

import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.entity.User;

public class JoinActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "JoinActivity";
	private Button btn_join_exit = null;
	private Button btn_join_sure = null;
	private EditText et_join_name = null;
	private EditText et_join_pw = null;
	private EditText et_join_pw_again = null;
	private EditText et_join_age = null;
	private EditText et_join_tel = null;
	private EditText et_join_address = null;
	private EditText et_join_info = null;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		init();
	}

	private void init() {
		btn_join_exit = (Button) findViewById(R.id.btn_join_exit);
		btn_join_sure = (Button) findViewById(R.id.btn_join_sure);
		et_join_name = (EditText) findViewById(R.id.et_join_name);
		et_join_pw = (EditText) findViewById(R.id.et_join_pw);
		et_join_pw_again = (EditText) findViewById(R.id.et_join_pw_again);
		et_join_age = (EditText) findViewById(R.id.et_join_age);
		et_join_tel = (EditText) findViewById(R.id.et_join_tel);
		et_join_address = (EditText) findViewById(R.id.et_join_address);
		et_join_info = (EditText) findViewById(R.id.et_join_info);
		btn_join_exit.setOnClickListener(this);
		btn_join_sure.setOnClickListener(this);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1000:
					long requst=-11;
					if (msg.obj.toString()!=null) {
						requst= Long.parseLong(msg.obj.toString());
					}
					Log.i(TAG, "requst=" + requst);
					if (requst > 0) {
						Toast.makeText(JoinActivity.this, "注册成功", Toast.LENGTH_LONG).show();
						//添加给第一个Activity的返回值，并设置resultCode
						Intent intent = new Intent();
						intent.putExtra("name", user.getName());
						intent.putExtra("pw", user.getPw());
						setResult(RESULT_OK, intent);
						finish();
					} else if (requst == -10) {
						et_join_name.setText(null);
						showDialogYes("您注册的用户名已存在！");
					} else if (requst == -11) {
						showDialogYes("客户端响应错误！");
					}else {
						showDialogYes("注册失败，请重试！");
					}
					break;

				default:
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_join_exit:
				finish();
				break;
			case R.id.btn_join_sure:
				if (chickData()) {
					new Thread(new Runnable() {

						@Override
						public void run() {
//							String requst = doQuery(user, "join");
//							Log.i(TAG, "requst="+requst);
//							Message message = handler.obtainMessage(1000);
//							message.obj = requst;
//							message.sendToTarget();
						}
					}).start();
				}
				break;

			default:
				break;
		}

	}

	private boolean chickData() {
		String name = et_join_name.getText().toString().trim();
		if ("".equals(name)) {
			showDialogYes("用户名不能为空");
			et_join_name.setText(null);
			return false;
		}
		String pw = et_join_pw.getText().toString().trim();
		String pw2 = et_join_pw_again.getText().toString().trim();
		if ("".equals(pw)||"".equals(pw2)) {
			showDialogYes("密码不能为空");
			et_join_pw.setText(null);
			et_join_pw_again.setText(null);
			return false;
		}
		if (!pw.equals(pw2)) {
			showDialogYes("两次密码不相同");
			et_join_pw.setText(null);
			et_join_pw_again.setText(null);
			return false;
		}
		String age = et_join_age.getText().toString().trim();
		if ("".equals(age)|| Integer.parseInt(age) <= 0
				|| Integer.parseInt(age) > 80) {
			showDialogYes("请输入合法年龄");
			et_join_age.setText(null);
			return false;
		}
		String tel = et_join_tel.getText().toString().trim();
		if ("".equals(tel)|| tel.length() != 11) {
			showDialogYes("请输入11位手机号");
			et_join_tel.setText(null);
			return false;
		}
		String address = et_join_address.getText().toString().trim();
		if ("".equals(address)) {
			showDialogYes("请输入地址");
			et_join_address.setText(null);
			return false;
		}
		String info = et_join_info.getText().toString().trim();
		user = new User();
		user.setName(name);
		user.setPw(pw);
		user.setAge(Integer.parseInt(age));
		user.setTel(tel);
		user.setAddress(address);
		user.setInfo(info);
		return true;
	}

//	/**
//	 *
//	 * @Title: query
//	 * @Description: TODO(充值查询)
//	 * @param @param user
//	 * @param @param money
//	 * @param @return 设定文件
//	 * @return String 返回类型
//	 * @throws
//	 */
//	private String query(User user, String flag) {
//		// 查询参数
//		String queryString = "user=" + JsonUtil.userToJson(user) + "&flag="
//				+ flag;
//		// url
//		String url = HttpUtil.BASE_URL + "servlet/JoinServlet?" + queryString;
//		Log.i(TAG, "url=" + url);
//		// 查询返回结果
//		try {
//			return HttpUtil.queryStringForPost(url);
//		} catch (ClientProtocolException e) {
//			Log.e(TAG, "ClientProtocolException", e);
//		} catch (IOException e) {
//			Log.e(TAG, "IOException", e);
//		}
//		return null;
//	}
//
//	private String doQuery(User user, String flag) {
//		Log.i(TAG, "doQuery......");
//		String Result_Show = null;
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpPost httppost = new HttpPost(HttpUtil.BASE_URL
//				+ "servlet/JoinServlet"+"?flag="
//				+ flag);
//		try {
//			httppost.setEntity(new StringEntity( JsonUtil.userToJson(user)));
//			HttpResponse httpres = httpclient.execute(httppost);
//
//			if (httpres.getStatusLine().getStatusCode() == 200) {
//				Log.d(TAG, "成功连接信号");
//				Result_Show = EntityUtils.toString(httpres.getEntity());
////				JSONObject JsonResult = new JSONObject(Res);
////				Result_Show = JsonResult.toString();
//				Log.d(TAG, Result_Show);
//			}
//
//		} catch (Exception e) {
//			Log.d(TAG, e.toString());
//		}
//		return Result_Show;
//	}

	/**
	 *
	 * @Title: showDialogYes
	 * @Description: TODO(显示用户只能确定的Dialog)
	 * @param @param msg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showDialogYes(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}