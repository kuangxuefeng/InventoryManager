package com.kxf.inventorymanager.activity;

import android.os.Message;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

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

public class UserListActivity extends BaseListActivity {
    @Override
    protected String getListTitle() {
        return "用户列表";
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
                    final User[] us = heRe.getTs();
                    UserListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<HashMap<String, String>> data = new ArrayList();
                            if (null != us && us.length > 0){
                                for (User u : us){
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", u.getName());
                                    map.put("permiss", u.getPermissions() + "");
                                    data.add(map);
                                }
                            }
                            String[] from = new String[]{"name", "permiss"};
                            int[] to = new int[]{R.id.tv_name_user_item, R.id.tv_permiss_user_item};
                            ListAdapter adapter = new SimpleAdapter(UserListActivity.this, data, R.layout.user_item_list, from, to);
                            lv_base.setAdapter(adapter);
                            lv_base.deferNotifyDataSetChanged();
                            load_pb.setVisibility(View.GONE);
                        }
                    });
                }else {
                    Message msg = handlerBase.obtainMessage(1005);
                    msg.obj = heRe.getResponseMsg();
                    msg.sendToTarget();
                }
            }
        }).start();
    }
}
