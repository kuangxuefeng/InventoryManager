package com.kxf.inventorymanager.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.karics.library.zxing.android.CaptureActivity;
import com.kxf.inventorymanager.MyApplication;
import com.kxf.inventorymanager.entity.Commodity;
import com.kxf.inventorymanager.http.HttpEntity;
import com.kxf.inventorymanager.http.HttpUtils;
import com.kxf.inventorymanager.utils.FormatUtils;
import com.kxf.inventorymanager.utils.LogUtil;

import java.lang.reflect.Type;
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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtil.d("msg=" + msg);
            switch (msg.what){
                case 1010:
                    String content = (String) msg.obj;
                    Toast.makeText(mContext, content + " 入库成功！", Toast.LENGTH_SHORT).show();
                    break;
            }
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
        LogUtil.i("data=" + data);
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(CaptureActivity.DECODED_CONTENT_KEY);
//                Bitmap bitmap = data.getParcelableExtra(CaptureActivity.DECODED_BITMAP_KEY);
                Toast.makeText(this, content, Toast.LENGTH_SHORT).show();

                doUploadNet(content);
            }
        }
    }

    private void doUploadNet(final String content){
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpEntity<Commodity> he = new HttpEntity<Commodity>();
                he.setRequestCode("1001");
                Commodity com = new Commodity();
                com.setUserId(user.getId());
                com.setQcode(content);
                com.setHmsS(FormatUtils.getTimeByFormat(FormatUtils.FORMAT_COMMODITY_HMSS));
                com.setYmd(FormatUtils.getTimeByFormat(FormatUtils.FORMAT_COMMODITY_YMD));
                he.setTs(new Commodity[]{com});
                String reStr = HttpUtils.sendMsg(HttpUtils.COMMODITY_URL, he);
                Type typeOfT = new TypeToken<HttpEntity<Commodity>>(){}.getType();
                HttpEntity<Commodity> heRe = HttpUtils.ParseJson(he, reStr, typeOfT);
                if ("0000".equals(heRe.getResponseCode())) {
                    Message msg = handler.obtainMessage(1010);
                    msg.obj = content;
                    msg.sendToTarget();
                }else {
                    Message msg = handlerBase.obtainMessage(msg_base_http_erro);
                    if (null != heRe.getTs() && heRe.getTs().length>0){
                        msg.obj = heRe.getResponseMsg() + "，原入库员：" + heRe.getTs()[0].getUserId();
                    }else {
                        msg.obj = heRe.getResponseMsg();
                    }
                    msg.sendToTarget();
                }
            }
        }).start();
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
                intent = new Intent(this,
                        CommodityQueryActivity.class);
                startActivity(intent);
                break;
            case 3:
                break;
            case 4:
                intent = new Intent(this, UserMenuActivity.class);
                startActivity(intent);
                break;
        }
    }
}
