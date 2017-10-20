package com.kxf.inventorymanager.activity;

import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.karics.library.zxing.encode.CodeCreator;
import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.entity.Commodity;
import com.kxf.inventorymanager.http.HttpEntity;
import com.kxf.inventorymanager.http.HttpUtils;
import com.kxf.inventorymanager.utils.FormatUtils;
import com.kxf.inventorymanager.utils.LogUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommodityShowActivity extends BaseListActivity {
    public static final String KEY_COMMODITY_SHOW = "key_commodity_show";
    private Commodity commodity;
    private RelativeLayout rl_btn;
    private Button btn_out;
    private ImageView iv_qcode, iv_codebar;
    private TextView tv_code_title;
    @Override
    protected String getListTopTitle() {
        rl_btn = (RelativeLayout) findViewById(R.id.rl_btn);
        iv_qcode = (ImageView) findViewById(R.id.iv_qcode);
        iv_codebar = (ImageView) findViewById(R.id.iv_codebar);
        tv_code_title = (TextView) findViewById(R.id.tv_code_title);
        btn_out = (Button) findViewById(R.id.btn_out);
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outCom(commodity);
            }
        });
        return "详细信息";
    }

    @Override
    protected View getListTitleView() {
        return null;
    }

    @Override
    protected ListAdapter getAdapter() {
        commodity = new Gson().fromJson(getIntent().getStringExtra(KEY_COMMODITY_SHOW), Commodity.class);
        return null;
    }

    @Override
    protected void afterInitListView() {
        if (null != commodity){
            if (0== commodity.getState()){
                rl_btn.setVisibility(View.VISIBLE);
            }else {
                rl_btn.setVisibility(View.GONE);
            }
            try {
                WindowManager wm1 = this.getWindowManager();
                int width = wm1.getDefaultDisplay().getWidth();
                int height = wm1.getDefaultDisplay().getHeight();
                tv_code_title.setText("" + commodity.getQcode());
                tv_code_title.setVisibility(View.VISIBLE);
                try {
                    iv_codebar.setImageBitmap(CodeCreator.createCode(commodity.getQcode(), BarcodeFormat.CODE_128, width/3*2, (width/3*2)/4));
                    iv_codebar.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                iv_qcode.setImageBitmap(CodeCreator.createCode(commodity.getQcode(), BarcodeFormat.QR_CODE, width/5*4, width/5*4));
                iv_qcode.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<HashMap<String, String>> data = new ArrayList();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", "号码");
            map.put("value", commodity.getQcode());
            data.add(map);
            map = new HashMap<String, String>();
            map.put("name", "名称");
            map.put("value", FormatUtils.formatText(commodity.getName()));
            data.add(map);
            map = new HashMap<String, String>();
            map.put("name", "状态");
            map.put("value", 0== commodity.getState()? "已入库":"已出库");
            data.add(map);
            map = new HashMap<String, String>();
            map.put("name", "时间");
            map.put("value", FormatUtils.FormatTime(commodity.getYmd() + commodity.getHmsS(), FormatUtils.FORMAT_COMMODITY_YMD + FormatUtils.FORMAT_COMMODITY_HMSS, FormatUtils.FORMAT_COMMODITY_SHOW1));
            data.add(map);
            map = new HashMap<String, String>();
            map.put("name", "入库员");
            map.put("value", commodity.getUserId()+"");
            data.add(map);
            String[] from = new String[]{"name", "value"};
            int[] to = new int[]{R.id.tv_name_user_item, R.id.tv_permiss_user_item};
            ListAdapter adapter = new SimpleAdapter(mActivity, data, R.layout.user_item_list, from, to);
            lv_base.setAdapter(adapter);
            lv_base.deferNotifyDataSetChanged();
            lv_base.setEnabled(false);
        }
    }

    private void outCom(final Commodity com){
        load_pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpEntity<Commodity> he = new HttpEntity();
                he.setRequestCode("1003");
                com.setState(1);
                he.setTs(new Commodity[]{com});
                String str = HttpUtils.sendMsg(HttpUtils.COMMODITY_URL, he);
                LogUtil.d("str=" + str);
                Type typeOfT = new TypeToken<HttpEntity<Commodity>>(){}.getType();
                HttpEntity<Commodity> heRe = HttpUtils.ParseJson(he, str, typeOfT);

                if ("0000".equals(heRe.getResponseCode())){
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commodity.setState(1);
                            Toast.makeText(mContext, "出库成功！", Toast.LENGTH_SHORT).show();
                            afterInitListView();
                        }
                    });
                }else {
                    Message msg = handlerBase.obtainMessage(msg_base_http_erro);
                    msg.obj = heRe.getResponseMsg();
                    msg.sendToTarget();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        load_pb.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
}
