package com.kxf.inventorymanager.activity;

import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.kxf.inventorymanager.R;

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
        List<HashMap<String, String>> data = new ArrayList();
        String[] from = new String[]{};
        int[] to = new int[]{};
        ListAdapter adapter = new SimpleAdapter(this, data, R.layout.user_item_list, from, to);
        return adapter;
    }

    @Override
    protected void afterInitListView() {
    }
}
