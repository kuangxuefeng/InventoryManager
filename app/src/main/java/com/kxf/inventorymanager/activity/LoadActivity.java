package com.kxf.inventorymanager.activity;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.kxf.inventorymanager.R;

public class LoadActivity extends BaseActivity {
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_LOAD_CODE = "key_load_code";
    private ProgressBar load_pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        initView();
        doLoad();
    }

    private void doLoad() {
        int code = getIntent().getIntExtra(KEY_LOAD_CODE, 0);
        switch (code){
            case 10000:
                break;
        }
    }

    private void initView() {
        load_pb = (ProgressBar) findViewById(R.id.load_pb);
        String title = getIntent().getStringExtra(KEY_TITLE);
        setTopTitle(title);
    }
}
