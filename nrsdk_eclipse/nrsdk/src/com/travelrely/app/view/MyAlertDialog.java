
package com.travelrely.app.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.travelrely.sdk.R;

/**
 * @author zhangyao
 * @version 2014年7月24日下午3:01:51
 */

public class MyAlertDialog extends Dialog{
    Context context;
    android.app.AlertDialog ad;
    TextView titleView;
    TextView messageView;
    TextView bt_left, bt_right;

    public MyAlertDialog(Context context) {
        // TODO Auto-generated constructor stub
        super(context);
        this.context = context;
        ad = new android.app.AlertDialog.Builder(context).create();
        ad.show();
        Window window = ad.getWindow();
        window.setContentView(R.layout.alert_dialog_layout);
        titleView = (TextView) window.findViewById(R.id.title);
        messageView = (TextView) window.findViewById(R.id.message);
        bt_left = (TextView) window.findViewById(R.id.bt_left);
        bt_right = (TextView) window.findViewById(R.id.bt_right);
    }

    public void setTitle(int resId)
    {
        titleView.setText(resId);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setMessage(int resId) {
        messageView.setText(resId);
    }

    public void setMessage(String message)
    {
        messageView.setText(message);
    }

    @SuppressLint("ResourceAsColor")
    public void setPositiveButton(String text, final View.OnClickListener listener)
    {
        bt_left.setText(text);
        bt_left.setOnClickListener(listener);
    }

    @SuppressLint("ResourceAsColor")
    public void setNegativeButton(String text, final View.OnClickListener listener)
    {
        bt_right.setText(text);
        bt_right.setOnClickListener(listener);

    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
    }
    
}
