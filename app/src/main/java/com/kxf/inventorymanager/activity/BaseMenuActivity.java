package com.kxf.inventorymanager.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kxf.inventorymanager.R;
import com.kxf.inventorymanager.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.kxf.inventorymanager.activity.BaseMenuActivity.MENU_TYPE.DOUBLE_LINE;

public abstract class BaseMenuActivity extends BaseActivity {

    private List<BaseItem> baseItems;
    private List<Button> btnItems;
    private boolean hasExtra = true;
    protected MENU_TYPE menuType = DOUBLE_LINE;
    public enum MENU_TYPE{
        SINGLE_LINE, DOUBLE_LINE
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_menu);
        initMenu();
        afterInitMenu();
    }

    protected void initMenu() {
        baseItems = getBaseItem();
        setTopTitle(getMenuTitle());

        LinearLayout ll_content = (LinearLayout) findViewById(R.id.ll_content);

        ll_content.removeAllViews();
        if (null == baseItems || baseItems.size()<1){
            return;
        }

        btnItems = new ArrayList<>();
        switch (menuType){
            case SINGLE_LINE:
                for (BaseItem bi : baseItems){
                    addItem(ll_content, bi, null);
                }
                break;
            case DOUBLE_LINE:
                if (baseItems.size()%2==0){
                    hasExtra = false;
                }
                int numLlItem = baseItems.size() / 2;
                for (int i=0;i<numLlItem;i++){
                    addItem(ll_content, baseItems.get(2*i), baseItems.get(2*i+1));
                }

                if (hasExtra){
                    addItem(ll_content, baseItems.get(baseItems.size()-1), null);
                }
                break;
        }
    }

    protected void addItem(ViewGroup parent, final BaseItem bi1, final BaseItem bi2){
        View v = LayoutInflater.from(this).inflate(R.layout.activity_base_menu, null);
        LinearLayout ll_item = (LinearLayout) v.findViewById(R.id.ll_item);
        LinearLayout ll_content = (LinearLayout) v.findViewById(R.id.ll_content);
        Button btn_item1 = (Button) v.findViewById(R.id.btn_item1);
        Button btn_item2 = (Button) v.findViewById(R.id.btn_item2);

        if (null != bi1){
            btn_item1.setText(bi1.getTitle());
            btn_item1.setOnClickListener(bi1.getAndDoListener());
            btn_item1.setVisibility(View.VISIBLE);
            btnItems.add(btn_item1);
        }

        if (null != bi2){
            btn_item2.setText(bi2.getTitle());
            btn_item2.setOnClickListener(bi2.getAndDoListener());
            btn_item2.setVisibility(View.VISIBLE);
            btnItems.add(btn_item2);
        }
        ll_content.removeAllViews();
        ll_item.setVisibility(View.VISIBLE);
        parent.addView(ll_item);
    }

    protected void setAllBtnEnabled(boolean enabled){
        if (null == btnItems || btnItems.size() < 1){
            return;
        }
        for (Button btn : btnItems){
            btn.setEnabled(enabled);
        }
    }

    protected abstract List<BaseItem> getBaseItem();
    protected abstract String getMenuTitle();
    protected abstract void afterInitMenu();

    public class BaseItem{
        private int id;
        private String title;
        private OnItemClickListener listener;

        public View.OnClickListener getAndDoListener() {
            View.OnClickListener viewListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    LogUtil.d("onClick title=" + title);
                    if (null != listener){
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setAllBtnEnabled(false);
                                listener.onClick(v, BaseItem.this);
                                setAllBtnEnabled(true);
                            }
                        });
                    }
                }
            };
            return viewListener;
        }

        public String getTitle() {
            return title;
        }

        public BaseItem setTitle(String title) {
            this.title = title;
            return this;
        }

        public OnItemClickListener getListener() {
            return listener;
        }

        public BaseItem setListener(OnItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        public int getId() {
            return id;
        }

        public BaseItem setId(int id) {
            this.id = id;
            return this;
        }

        @Override
        public String toString() {
            return "BaseItem{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", listener=" + listener +
                    '}';
        }
    }

    public interface OnItemClickListener {
        void onClick(View v, BaseItem bi);
    }
}
