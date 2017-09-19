package com.kxf.inventorymanager.activity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kxf.inventorymanager.R;

public abstract class BaseListActivity extends BaseActivity {
    protected ListView lv_base;
    protected ProgressBar load_pb;
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        setTopTitle(getListTitle());
        initListView();
        afterInitListView();
    }

    protected void initListView() {
        lv_base = (ListView) findViewById(R.id.lv_base);
        load_pb = (ProgressBar) findViewById(R.id.load_pb);
        ListAdapter adapter = getAdapter();
        if (null != adapter){
            lv_base.setAdapter(getAdapter());
        }
    }

    protected abstract String getListTitle();
    protected abstract ListAdapter getAdapter();
    protected abstract void afterInitListView();
}
