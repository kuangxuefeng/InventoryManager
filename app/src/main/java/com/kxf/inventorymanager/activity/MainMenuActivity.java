package com.kxf.inventorymanager.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karics.library.zxing.android.CaptureActivity;
import com.kxf.inventorymanager.R;

/**
 * Created by kxf on 2017/9/2.
 */
public class MainMenuActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_in, btn_out, btn_query, btn_ver, btn_user;
    private static final int REQUEST_CODE_SCAN = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }

    private void init() {
        btn_in = (Button) findViewById(R.id.btn_in);
        btn_out = (Button) findViewById(R.id.btn_out);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_ver = (Button) findViewById(R.id.btn_ver);
        btn_user = (Button) findViewById(R.id.btn_user);

        btn_in.setOnClickListener(this);
        btn_out.setOnClickListener(this);
        btn_query.setOnClickListener(this);
        btn_ver.setOnClickListener(this);
        btn_user.setOnClickListener(this);

        if (null != user && user.getPermissions() > 0){
            btn_user.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        setAllBtnEnabled(false);
        Intent intent;
        switch (v.getId()){
            case R.id.btn_in:
                intent = new Intent(this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.btn_out:
                break;
            case R.id.btn_query:
                break;
            case R.id.btn_ver:
                break;
            case R.id.btn_user:
                intent = new Intent(this, JoinActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
        setAllBtnEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(CaptureActivity.DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(CaptureActivity.DECODED_BITMAP_KEY);
                Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setAllBtnEnabled(boolean enabled){
        btn_in.setEnabled(enabled);
        btn_out.setEnabled(enabled);
        btn_query.setEnabled(enabled);
        btn_ver.setEnabled(enabled);
        btn_user.setEnabled(enabled);
    }
}
