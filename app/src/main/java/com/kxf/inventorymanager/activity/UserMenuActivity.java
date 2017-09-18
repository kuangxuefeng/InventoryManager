package com.kxf.inventorymanager.activity;

import android.view.View;

import com.kxf.inventorymanager.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class UserMenuActivity extends BaseMenuActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_menu);
//    }

    @Override
    protected List<BaseItem> getBaseItem() {
        List<BaseItem> bis = new ArrayList<>();
        for (int i = 0;i<7;i++){
            bis.add(new BaseItem().setId(i).setTitle("menu item" + i).setListener(new OnItemClickListener() {
                @Override
                public void onClick(View v, BaseItem bi) {
                    LogUtil.d("bi=" + bi);
                }
            }));
        }
        return bis;
    }

    @Override
    protected String getMenuTitle() {
        return "用户管理";
    }

    @Override
    protected void afterInitMenu() {

    }
}
