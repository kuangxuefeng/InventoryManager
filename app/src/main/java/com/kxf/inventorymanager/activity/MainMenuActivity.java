package com.kxf.inventorymanager.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.karics.library.zxing.android.CaptureActivity;
import com.kxf.inventorymanager.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kxf on 2017/9/2.
 */
public class MainMenuActivity extends BaseMenuActivity implements BaseMenuActivity.OnItemClickListener {
    private static final int REQUEST_CODE_SCAN = 1000;
    private List<String> itemTitles;
    private Runnable backRun = new Runnable() {
        @Override
        public void run() {
            showDialog("是否退出？", "否", null, "是", new Runnable() {
                @Override
                public void run() {
                    MyApplication.saveShare(LoginActivity.KEY_USER_ISLOGIN, "");
                    finish();
                }
            });
        }
    };

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_menu);
//        init();
//    }


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            backRun.run();
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected List<BaseItem> getBaseItem() {
        itemTitles = new ArrayList();
        itemTitles.add("入库");
        itemTitles.add("出库");
        itemTitles.add("查询");
        itemTitles.add("版本");
        if (null != user && user.getPermissions() > 0){
            itemTitles.add("用户管理");
        }
        List<BaseItem> ls = new ArrayList<>();
        for (String title : itemTitles){
            BaseItem bi = new BaseItem().setId(itemTitles.indexOf(title)).setTitle(title).setListener(this);
            ls.add(bi);
        }
        return ls;
    }

    @Override
    protected String getMenuTitle() {
        return "欢迎使用本系统";
    }

    @Override
    protected void afterInitMenu() {
        setRightInfo("退出登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backRun.run();
            }
        });
    }

    @Override
    public void onClick(View v, BaseItem bi) {
        Intent intent;
        switch (bi.getId()){
            case 0:
                intent = new Intent(this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                intent = new Intent(this, UserMenuActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
