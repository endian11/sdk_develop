package com.travelrely.app.view;

import android.content.Context;
import android.text.ClipboardManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/** 
 * 
 * @author zhangyao
 * @version 2014年7月23日下午6:27:25
 */

public class BaseLinearLayout extends LinearLayout{

    public Context mContext;
    
    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }
    
    public void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

}
