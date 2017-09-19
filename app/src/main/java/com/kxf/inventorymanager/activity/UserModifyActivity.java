package com.kxf.inventorymanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.kxf.inventorymanager.AppConfig;
import com.kxf.inventorymanager.MyApplication;
import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.entity.User;
import com.kxf.inventorymanager.http.HttpEntity;
import com.kxf.inventorymanager.http.HttpUtils;
import com.kxf.inventorymanager.utils.LogUtil;

import org.xutils.ex.DbException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserModifyActivity extends BaseActivity implements OnClickListener {

	public static final String DEFAULT_ROOT_NAME = "root";
	public static final String DEFAULT_ROOT_PW = "qazwsx";
	private Button btn_join_exit = null;
	private Button btn_join_sure = null;
	private EditText et_join_name = null;
	private EditText et_join_pw = null;
	private EditText et_join_pw_again = null;
	private Spinner et_join_qx = null;
	private EditText et_join_tel = null;
	private EditText et_join_address = null;
	private EditText et_join_info = null;
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	private User userNew;
	private User userOld;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		setTopTitle("注册");
		init();
	}

	private void init() {
		btn_join_exit = (Button) findViewById(R.id.btn_join_exit);
		btn_join_sure = (Button) findViewById(R.id.btn_join_sure);
		et_join_name = (EditText) findViewById(R.id.et_join_name);
		et_join_pw = (EditText) findViewById(R.id.et_join_pw);
		et_join_pw_again = (EditText) findViewById(R.id.et_join_pw_again);
		et_join_qx = (Spinner) findViewById(R.id.et_join_qx);
		et_join_tel = (EditText) findViewById(R.id.et_join_tel);
		et_join_address = (EditText) findViewById(R.id.et_join_address);
		et_join_info = (EditText) findViewById(R.id.et_join_info);
		btn_join_exit.setOnClickListener(this);
		btn_join_sure.setOnClickListener(this);
		switch (user.getPermissions()){
			case 1:
				data_list = new ArrayList<>();
				data_list.add("普通用户");
				break;

			case 2:
				data_list = new ArrayList<>();
				data_list.add("普通用户");
				data_list.add("管理员");
				break;
		}
		//适配器
		arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		et_join_qx.setAdapter(arr_adapter);
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
					LogUtil.d("requst=" + requst);
					if (requst > 0) {
						Toast.makeText(UserModifyActivity.this, "注册成功", Toast.LENGTH_LONG).show();
						//添加给第一个Activity的返回值，并设置resultCode
						Intent intent = new Intent();
						intent.putExtra("name", userNew.getName());
						intent.putExtra("pw", userNew.getPw());
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
				case 1005:
					String str = (String) msg.obj;
					showDialogYes(str);
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
					if (AppConfig.userCheckOnLine){
						new Thread(new Runnable() {

							@Override
							public void run() {
								HttpEntity<User> he = new HttpEntity<User>();
								he.setRequestCode("1001");
								he.setTs(new User[]{userNew});
								String reStr = HttpUtils.sendMsg(HttpUtils.USER_URL, he);
                                Type typeOfT = new TypeToken<HttpEntity<User>>(){}.getType();
								HttpEntity<User> heRe = HttpUtils.ParseJson(he, reStr, typeOfT);
								if ("0000".equals(heRe.getResponseCode())) {
									Message msg = handler.obtainMessage(1000);
									msg.obj = "1";
									msg.sendToTarget();
								}else {
									Message msg = handler.obtainMessage(1005);
									msg.obj = heRe.getResponseMsg();
									msg.sendToTarget();
								}
							}
						}).start();
						return;
					}
				}
				Toast.makeText(UserModifyActivity.this, "注册成功", Toast.LENGTH_LONG).show();
				finish();
				break;

			default:
				break;
		}

	}

	private boolean chickData() {
		User user;
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
		int pos = et_join_qx.getSelectedItemPosition();
		LogUtil.e("pos=" + pos);
		String tel = et_join_tel.getText().toString().trim();
		if ("".equals(tel)|| tel.length() != 11) {
			showDialogYes("请输入11位手机号");
			et_join_tel.setText(null);
			return false;
		}
		String address = et_join_address.getText().toString().trim();
//		if ("".equals(address)) {
//			showDialogYes("请输入地址");
//			et_join_address.setText(null);
//			return false;
//		}
		String info = et_join_info.getText().toString().trim();
		user = new User();
		user.setName(name);
		user.setPw(pw);
		user.setTel(tel);
		user.setAddress(address);
		user.setInfo(info);
		user.setPermissions(pos);

		if (!AppConfig.userCheckOnLine){
			User u = null;
			try {
				u = MyApplication.db().selector(User.class).where("name", "=", name).findFirst();
			} catch (DbException e) {
				e.printStackTrace();
			}
			if (null != u){
				showDialogYes("用户名已存在");
				et_join_name.setText(null);
				return false;
			}
			try {
				MyApplication.db().save(user);
			} catch (DbException e) {
				e.printStackTrace();
			}
		}

		userNew = user;
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
}
