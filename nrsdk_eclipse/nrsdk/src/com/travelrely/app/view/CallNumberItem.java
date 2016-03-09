package com.travelrely.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;

public class CallNumberItem extends LinearLayout
{
    TextView num;
    TextView ziMu;

    public CallNumberItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        num = (TextView) findViewById(R.id.num);
        ziMu = (TextView) findViewById(R.id.zimu);
    }

    public void setNum(String str)
    {
        num.setText(str);
    }

    public void setZiMu(String str)
    {
        ziMu.setText(str);
    }

    public void hideZiMu()
    {
        ziMu.setVisibility(View.GONE);
        LinearLayout.LayoutParams pm = (LinearLayout.LayoutParams) num
                .getLayoutParams();
        pm.weight = 3;
    }
}
