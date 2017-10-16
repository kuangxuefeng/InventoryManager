package com.kxf.inventorymanager.activity;

import android.content.Intent;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserMenuActivity extends BaseMenuActivity implements BaseMenuActivity.OnItemClickListener {
    private List<String> itemTitles;
    private boolean isNeedUpdate = false;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_menu);
//    }

    @Override
    protected List<BaseItem> getBaseItem() {
        menuType = MENU_TYPE.SINGLE_LINE;
        itemTitles = new ArrayList<>();
        itemTitles.add("用户添加");
        itemTitles.add("用户查询");
        itemTitles.add("信息修改");
        List<BaseItem> bis = new ArrayList<>();
        for (String title : itemTitles){
            BaseItem bi = new BaseItem().setId(itemTitles.indexOf(title)).setTitle(title).setListener(this);
            bis.add(bi);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedUpdate){
            updateLoginUser();
            isNeedUpdate = false;
        }
    }

    @Override
    public void onClick(View v, BaseItem bi) {
        Intent intent;
        switch (bi.getId()){
            case 0:
                intent = new Intent(this, UserModifyActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, UserListActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, UserModifyActivity.class);
                Gson gson = new Gson();
                intent.putExtra(UserModifyActivity.KEY_USER_OLD, gson.toJson(user));
                intent.putExtra(UserModifyActivity.KEY_USER_MODIF_TYPE, 2);
                isNeedUpdate = true;
                startActivity(intent);
                break;
        }
    }
}
