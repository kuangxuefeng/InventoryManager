package com.kxf.inventorymanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kxf.inventorymanager.R;

public abstract class BaseListActivity extends BaseActivity {
    protected ListView lv_base;
    protected ProgressBar load_pb;
    protected LinearLayout ll_lv_title;
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        setTopTitle(getListTopTitle());
        initListView();
        afterInitListView();
    }

    protected void initListView() {
        lv_base = (ListView) findViewById(R.id.lv_base);
        load_pb = (ProgressBar) findViewById(R.id.load_pb);
        ll_lv_title = (LinearLayout) findViewById(R.id.ll_lv_title);
        View v = getListTitleView();
        if (v != null){
            ll_lv_title.setVisibility(View.VISIBLE);
            v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            ll_lv_title.addView(v);
        }
        ListAdapter adapter = getAdapter();
        if (null != adapter){
            lv_base.setAdapter(getAdapter());
        }
    }

    protected abstract String getListTopTitle();
    protected abstract View getListTitleView();
    protected abstract ListAdapter getAdapter();
    protected abstract void afterInitListView();
}
