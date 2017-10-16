package com.kxf.inventorymanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.entity.User;
import com.kxf.inventorymanager.http.HttpEntity;
import com.kxf.inventorymanager.http.HttpUtils;
import com.kxf.inventorymanager.utils.LogUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserListActivity extends BaseListActivity implements AdapterView.OnItemClickListener {
    private User[] us;
    private boolean isNeedUpdate = false;
    @Override
    protected String getListTopTitle() {
        return "用户列表";
    }

    @Override
    protected View getListTitleView() {
        View v = LayoutInflater.from(this).inflate(R.layout.user_item_list, null);
        ((TextView)v.findViewById(R.id.tv_name_user_item)).setText("用户姓名");
        ((TextView)v.findViewById(R.id.tv_permiss_user_item)).setText("权限");
        v.setBackgroundColor(Color.rgb(192,192,192));
        return v;
    }

    @Override
    protected ListAdapter getAdapter() {
        return null;
    }

    @Override
    protected void afterInitListView() {
        load_pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpEntity<User> he = new HttpEntity();
                he.setRequestCode("1002");
                he.setTs(new User[]{user});
                String str = HttpUtils.sendMsg(HttpUtils.USER_URL, he);
                LogUtil.d("str=" + str);
                Type typeOfT = new TypeToken<HttpEntity<User>>(){}.getType();
                HttpEntity<User> heRe = HttpUtils.ParseJson(he, str, typeOfT);

                if ("0000".equals(heRe.getResponseCode())){
                    us = heRe.getTs();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<HashMap<String, String>> data = new ArrayList();
                            if (null != us && us.length > 0){
                                for (User u : us){
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", u.getName());
                                    map.put("permiss", UserModifyActivity.parseQX(u.getPermissions()));
                                    data.add(map);
                                }
                            }
                            String[] from = new String[]{"name", "permiss"};
                            int[] to = new int[]{R.id.tv_name_user_item, R.id.tv_permiss_user_item};
                            ListAdapter adapter = new SimpleAdapter(mActivity, data, R.layout.user_item_list, from, to);
                            lv_base.setAdapter(adapter);
                            lv_base.deferNotifyDataSetChanged();
                            load_pb.setVisibility(View.GONE);
                            lv_base.setOnItemClickListener(UserListActivity.this);
                        }
                    });
                }else {
                    Message msg = handlerBase.obtainMessage(msg_base_http_erro);
                    msg.obj = heRe.getResponseMsg();
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User u = us[position];
        Gson gson = new Gson();
        Intent intent = new Intent(this, UserModifyActivity.class);
        intent.putExtra(UserModifyActivity.KEY_USER_MODIF_TYPE, 3);
        intent.putExtra(UserModifyActivity.KEY_USER_OLD, gson.toJson(u));
        isNeedUpdate = true;
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedUpdate){
            afterInitListView();
        }
    }
}
