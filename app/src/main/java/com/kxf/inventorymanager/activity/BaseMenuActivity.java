package com.kxf.inventorymanager.activity;

import android.os.Bundle;
import android.view.View;

import com.kxf.inventorymanager.R;

import java.util.List;

public abstract class BaseMenuActivity extends BaseActivity {

    private List<BaseItem> baseItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_menu);
        initMenu();
    }

    protected void initMenu() {
        baseItems = getBaseItem();
        setTopTitle(getMenuTitle());
        
    }

    protected abstract List<BaseItem> getBaseItem();
    protected abstract String getMenuTitle();

    public class BaseItem{
        private String title;
        private View.OnClickListener listener;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public View.OnClickListener getListener() {
            return listener;
        }

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public String toString() {
            return "BaseItem{" +
                    "title='" + title + '\'' +
                    ", listener=" + listener +
                    '}';
        }
    }
}
