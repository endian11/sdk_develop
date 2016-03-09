
package com.travelrely.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SListView extends ListView {
    private static final int MAX_SCROLL = 200;
    private static final float SCROLL_RATIO = 1.0f;// 阻尼系数

    private Context mContext;

    public SListView(Context context) {
        super(context);
        mContext = context;
    }

    public SListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public SListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        int newDeltaY = deltaY;
        int delta = (int) (deltaY * SCROLL_RATIO);
        if (delta != 0)
            newDeltaY = delta;
        return super.overScrollBy(deltaX, newDeltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, MAX_SCROLL, isTouchEvent);
    }
}
