package com.kxf.inventorymanager.activity;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class UtilsActivity extends BaseMenuActivity implements BaseMenuActivity.OnItemClickListener {

    private ArrayList<String> itemTitles;

    @Override
    protected List<BaseItem> getBaseItem() {
        menuType = MENU_TYPE.SINGLE_LINE;
        itemTitles = new ArrayList<>();
        itemTitles.add("身份证识别");
        List<BaseItem> bis = new ArrayList<>();
        for (String title : itemTitles){
            BaseItem bi = new BaseItem().setId(itemTitles.indexOf(title)).setTitle(title).setListener(this);
            bis.add(bi);
        }
        return bis;
    }

    @Override
    protected String getMenuTitle() {
        return "工具";
    }

    @Override
    protected void afterInitMenu() {
    }

    @Override
    public void onClick(View v, BaseItem bi) {
        Intent intent;
        switch (bi.getId()){
            case 0:
                intent = new Intent(this, IdScanActivity.class);
                startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }
}
