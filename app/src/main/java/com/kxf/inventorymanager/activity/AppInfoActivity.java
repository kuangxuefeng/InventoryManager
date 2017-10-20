package com.kxf.inventorymanager.activity;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.kxf.inventorymanager.BuildConfig;
import com.kxf.inventorymanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppInfoActivity extends BaseListActivity {

    @Override
    protected String getListTopTitle() {
        return "应用信息";
    }

    @Override
    protected View getListTitleView() {
        return null;
    }

    @Override
    protected ListAdapter getAdapter() {
        List<HashMap<String, String>> data = new ArrayList();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", "有效期");
        map.put("value", "永久");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("name", "应用名称");
        map.put("value", getString(R.string.app_name));
        data.add(map);
        map = new HashMap<String, String>();
        map.put("name", "版本名称");
        map.put("value", BuildConfig.VERSION_NAME);
        data.add(map);
        map = new HashMap<String, String>();
        map.put("name", "版本号");
        map.put("value", BuildConfig.VERSION_CODE+"");
        data.add(map);
        map = new HashMap<String, String>();
        map.put("name", "编译时间");
        map.put("value", BuildConfig.buileDateTime);
        data.add(map);
        map = new HashMap<String, String>();
        map.put("name", "联系作者qq");
        map.put("value", "1024883177");
        data.add(map);
        String[] from = new String[]{"name", "value"};
        int[] to = new int[]{R.id.tv_name_user_item, R.id.tv_permiss_user_item};
        ListAdapter adapter = new SimpleAdapter(mActivity, data, R.layout.user_item_list, from, to);
        return adapter;
    }

    @Override
    protected void afterInitListView() {

    }
}
