package com.kxf.inventorymanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kxf.inventorymanager.AppConfig;
import com.kxf.inventorymanager.MyApplication;
import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.entity.User;
import com.kxf.inventorymanager.http.HttpEntity;
import com.kxf.inventorymanager.http.HttpUtils;
import com.kxf.inventorymanager.utils.FormatUtils;
import com.kxf.inventorymanager.utils.LogUtil;

import org.xutils.ex.DbException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserModifyActivity extends BaseActivity implements OnClickListener {

	public static final String DEFAULT_ROOT_NAME = "root";
	public static final String DEFAULT_ROOT_PW = "qazwsx";
	public static final String KEY_USER_MODIF_TYPE = "key_user_modif_type";
	public static final String KEY_USER_OLD = "key_user_old";
	private Button btn_join_exit = null;
	private Button btn_join_sure, btn_join_modify, btn_join_del;
	private RelativeLayout rl_join_exit, rl_join_modify, rl_join_del;
	private LinearLayout ll_join_pw, ll_join_pw_again;
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
	private int userModifType = 1;//1 add; 2 update; 3 show

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		init();
	}

	private void init() {
		userModifType = getIntent().getIntExtra(KEY_USER_MODIF_TYPE, 1);
		String userStr = getIntent().getStringExtra(KEY_USER_OLD);
		if (!TextUtils.isEmpty(userStr)){
			Gson gson = new Gson();
			userOld = gson.fromJson(userStr, User.class);
		}
		LogUtil.d("userModifType=" + userModifType);
		switch (userModifType){
			case 1:
				setTopTitle("注册");
				break;
			case 2:
				setTopTitle("信息修改");
				break;
			case 3:
				setTopTitle("用户信息");
				break;
		}

		btn_join_exit = (Button) findViewById(R.id.btn_join_exit);
		btn_join_sure = (Button) findViewById(R.id.btn_join_sure);
		btn_join_modify = (Button) findViewById(R.id.btn_join_modify);
		btn_join_del = (Button) findViewById(R.id.btn_join_del);

		et_join_name = (EditText) findViewById(R.id.et_join_name);
		et_join_pw = (EditText) findViewById(R.id.et_join_pw);
		et_join_pw_again = (EditText) findViewById(R.id.et_join_pw_again);
		et_join_qx = (Spinner) findViewById(R.id.et_join_qx);
		et_join_tel = (EditText) findViewById(R.id.et_join_tel);
		et_join_address = (EditText) findViewById(R.id.et_join_address);
		et_join_info = (EditText) findViewById(R.id.et_join_info);

		btn_join_exit.setOnClickListener(this);
		btn_join_sure.setOnClickListener(this);
		btn_join_modify.setOnClickListener(this);
		btn_join_del.setOnClickListener(this);

		rl_join_exit = (RelativeLayout) findViewById(R.id.rl_join_exit);
		rl_join_modify = (RelativeLayout) findViewById(R.id.rl_join_modify);
		rl_join_del = (RelativeLayout) findViewById(R.id.rl_join_del);
		ll_join_pw = (LinearLayout) findViewById(R.id.ll_join_pw);
		ll_join_pw_again = (LinearLayout) findViewById(R.id.ll_join_pw_again);

		switch (user.getPermissions()){
			case 1:
				data_list = new ArrayList<>();
				data_list.add(parseQX(0));
				break;

			case 2:
				data_list = new ArrayList<>();
				data_list.add(parseQX(0));
				data_list.add(parseQX(1));
				break;
		}
		//适配器
		arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		et_join_qx.setAdapter(arr_adapter);
		if (2 == userModifType || 3 == userModifType){
			String perStr = parseQX(userOld.getPermissions());
			if (!data_list.contains(perStr)){
				data_list.add(perStr);
			}
			et_join_qx.setSelection(data_list.indexOf(perStr));
			if (2 == userOld.getPermissions()){
				et_join_qx.setEnabled(false);
			}
			if (3 == userModifType){
				ll_join_pw.setVisibility(View.GONE);
				ll_join_pw_again.setVisibility(View.GONE);
			}else {
				et_join_pw.setText(userOld.getPw());
				et_join_pw_again.setText(userOld.getPw());
			}
			et_join_name.setFocusable(false);
			et_join_name.setText(userOld.getName());
			et_join_tel.setText(FormatUtils.formatText(userOld.getTel()));
			et_join_address.setText(FormatUtils.formatText(userOld.getAddress()));
			et_join_info.setText(FormatUtils.formatText(userOld.getInfo()));
		}
		if (3 == userModifType){
			et_join_pw.setFocusable(false);
			et_join_pw_again.setFocusable(false);
			et_join_qx.setEnabled(false);
			et_join_tel.setFocusable(false);
			et_join_address.setFocusable(false);
			et_join_info.setFocusable(false);

			rl_join_exit.setVisibility(View.GONE);
			if (user.getPermissions()>userOld.getPermissions()){
				rl_join_modify.setVisibility(View.VISIBLE);
				rl_join_del.setVisibility(View.VISIBLE);
			}
		}
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
				case 1001:
					Toast.makeText(UserModifyActivity.this, "修改成功", Toast.LENGTH_LONG).show();
					if (userNew.getId() == user.getId()){
						Gson gson = new Gson();
						MyApplication.saveShare(LoginActivity.KEY_USER, gson.toJson(userNew));
					}
					finish();
					break;
				case 1002:
					Toast.makeText(UserModifyActivity.this, "删除成功", Toast.LENGTH_LONG).show();
					finish();
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		LogUtil.d("v.getId()=" + v.getId());
		LogUtil.d("userModifType=" + userModifType);
		switch (v.getId()) {
			case R.id.btn_join_exit:
				finish();
				break;
			case R.id.btn_join_modify:
				Intent intent = new Intent(this, UserModifyActivity.class);
				Gson gson = new Gson();
				intent.putExtra(UserModifyActivity.KEY_USER_OLD, gson.toJson(userOld));
				intent.putExtra(UserModifyActivity.KEY_USER_MODIF_TYPE, 2);
				startActivity(intent);
				finish();
				break;
			case R.id.btn_join_sure:
				if (3 == userModifType){
					finish();
					return;
				}
				if (chickData()) {
					if (AppConfig.userCheckOnLine){
						switch (userModifType){
							case 1:
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
											Message msg = handlerBase.obtainMessage(msg_base_http_erro);
											msg.obj = heRe.getResponseMsg();
											msg.sendToTarget();
										}
									}
								}).start();
								break;
							case 2:
								new Thread(new Runnable() {

									@Override
									public void run() {
										HttpEntity<User> he = new HttpEntity<User>();
										he.setRequestCode("1003");
										userNew.setId(userOld.getId());
										he.setTs(new User[]{userNew});
										String reStr = HttpUtils.sendMsg(HttpUtils.USER_URL, he);
										Type typeOfT = new TypeToken<HttpEntity<User>>(){}.getType();
										HttpEntity<User> heRe = HttpUtils.ParseJson(he, reStr, typeOfT);
										if ("0000".equals(heRe.getResponseCode())) {
											Message msg = handler.obtainMessage(1001);
											msg.obj = "1";
											msg.sendToTarget();
										}else {
											Message msg = handlerBase.obtainMessage(msg_base_http_erro);
											msg.obj = heRe.getResponseMsg();
											msg.sendToTarget();
										}
									}
								}).start();
								break;
						}
						return;
					}
				}else {
					if (AppConfig.userCheckOnLine){
						break;
					}
				}
				Toast.makeText(UserModifyActivity.this, "注册成功", Toast.LENGTH_LONG).show();
				finish();
				break;
			case R.id.btn_join_del:
				showDialog("是否删除？", "否", null, "是", new Runnable() {
					@Override
					public void run() {
						new Thread(new Runnable() {

							@Override
							public void run() {
								HttpEntity<User> he = new HttpEntity<User>();
								he.setRequestCode("1004");
								he.setTs(new User[]{userOld});
								String reStr = HttpUtils.sendMsg(HttpUtils.USER_URL, he);
								Type typeOfT = new TypeToken<HttpEntity<User>>(){}.getType();
								HttpEntity<User> heRe = HttpUtils.ParseJson(he, reStr, typeOfT);
								if ("0000".equals(heRe.getResponseCode())) {
									Message msg = handler.obtainMessage(1002);
									msg.sendToTarget();
								}else {
									Message msg = handlerBase.obtainMessage(msg_base_http_erro);
									msg.obj = heRe.getResponseMsg();
									msg.sendToTarget();
								}
							}
						}).start();
					}
				});
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
			switch (userModifType){
				case 1:
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
					break;
			}
		}

		userNew = user;
		return true;
	}

	public static String parseQX(int permiss){
		String qx = "普通用户";
		switch (permiss){
			case 0:
				qx = "普通用户";
				break;
			case 1:
				qx = "管理员";
				break;
			case 2:
				qx = "超级管理员";
				break;
		}
		return qx;
	}
}
