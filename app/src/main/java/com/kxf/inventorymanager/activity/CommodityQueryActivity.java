package com.kxf.inventorymanager.activity;

import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
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

public class CommodityQueryActivity extends BaseListActivity implements AdapterView.OnItemClickListener {
    private Commodity[] coms;

    @Override
    protected String getListTopTitle() {
        return "查询";
    }

    @Override
    protected View getListTitleView() {
        View v = LayoutInflater.from(this).inflate(R.layout.user_item_list, null);
        ((TextView)v.findViewById(R.id.tv_name_user_item)).setText("二维码");
        ((TextView)v.findViewById(R.id.tv_permiss_user_item)).setText("时间");
        v.setBackgroundColor(Color.rgb(192,192,192));
        return v;
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
                HttpEntity<Commodity> he = new HttpEntity();
                he.setRequestCode("1002");
                Commodity com = new Commodity();
                com.setUserId(user.getId());
                he.setTs(new Commodity[]{com});
                String str = HttpUtils.sendMsg(HttpUtils.COMMODITY_URL, he);
                LogUtil.d("str=" + str);
                Type typeOfT = new TypeToken<HttpEntity<Commodity>>(){}.getType();
                HttpEntity<Commodity> heRe = HttpUtils.ParseJson(he, str, typeOfT);

                if ("0000".equals(heRe.getResponseCode())){
                    coms = heRe.getTs();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<HashMap<String, String>> data = new ArrayList();
                            if (null != coms && coms.length > 0){
                                for (Commodity c : coms){
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("qcode", c.getQcode());
                                    map.put("time", FormatUtils.FormatTime(c.getYmd() + c.getHmsS(), FormatUtils.FORMAT_COMMODITY_YMD + FormatUtils.FORMAT_COMMODITY_HMSS, FormatUtils.FORMAT_COMMODITY_SHOW));
                                    data.add(map);
                                }
                            }
                            String[] from = new String[]{"qcode", "time"};
                            int[] to = new int[]{R.id.tv_name_user_item, R.id.tv_permiss_user_item};
                            ListAdapter adapter = new SimpleAdapter(mActivity, data, R.layout.user_item_list, from, to);
                            lv_base.setAdapter(adapter);
                            lv_base.deferNotifyDataSetChanged();
                            load_pb.setVisibility(View.GONE);
                            lv_base.setOnItemClickListener(CommodityQueryActivity.this);
                        }
                    });
                }else {
                    Message msg = handlerBase.obtainMessage(msg_base_http_erro);
                    msg.obj = heRe.getResponseMsg();
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
}
