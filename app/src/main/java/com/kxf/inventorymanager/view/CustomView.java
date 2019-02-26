package com.kxf.inventorymanager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by lilei on 2016/12/7.
 */

public class CustomView extends View {
    private Context mContext;

    public CustomView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void wrongSide() {
        Toast.makeText(mContext, "wrongSide", Toast.LENGTH_SHORT).show();
    }
}
