package com.travelrely.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;

public class FormsArrawsRightDownBt extends LinearLayout
{
    private ImageView imageView;
    private TextView tv_left, tv_centre, tv_right, tvRight2;

    public FormsArrawsRightDownBt(Context context)
    {
        super(context);
    }

    public FormsArrawsRightDownBt(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.forms_rraws_right_down, this);

        init();
    }

    private void init()
    {
        imageView = (ImageView) findViewById(R.id.img_rraws);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_centre = (TextView) findViewById(R.id.tv_centre);
        tvRight2 = (TextView) findViewById(R.id.tv_right_two);
    }

    // 设置图片资源
    public void setImageResource(int resId)
    {
        imageView.setImageResource(resId);
    }

    /**
     * 设置显示的文字
     */
    public void setLeftText(int text)
    {
        tv_left.setText(text);
    }
    
    public void setLeftText(String text)
    {
        tv_left.setText(text);
    }

    public void setCentreText(int text)
    {
        tv_centre.setText(text);
    }
    
    public void setCentreText(String text)
    {
        tv_centre.setText(text);
    }

    public void setRightText(int text)
    {
        tv_right.setText(text);
    }

    public void setRightText(String text)
    {
        tv_right.setText(text);
    }
    
    public void setRightHint(int text)
    {
        tv_right.setHint(text);
    }
    
    public void setRightHint(String text)
    {
        tv_right.setHint(text);
    }

    public String getRightText()
    {
        return tv_right.getText().toString();
    }

    public void setRightTextColor(int id)
    {
        tv_right.setTextColor(getResources().getColor(id));
    }

    public void setRight2Text(int text)
    {
        tvRight2.setText(text);
    }
    
    public void setRight2Text(String text)
    {
        tvRight2.setText(text);
    }

    public void setRight2TextColor(int id)
    {
        tvRight2.setTextColor(getResources().getColor(id));
    }

    public void showRight2Text()
    {
        tvRight2.setVisibility(View.VISIBLE);
    }
    
    public String getRight2Text()
    {
        return tvRight2.getText().toString();
    }

    public void hideImage()
    {
        imageView.setVisibility(View.GONE);
    }

    public void showImage()
    {
        imageView.setVisibility(View.VISIBLE);
    }

    public void hideRightText()
    {
        tv_right.setVisibility(View.GONE);
    }

    public void showRightText()
    {
        tv_right.setVisibility(View.VISIBLE);
    }

    public void hideCentreText()
    {
        tv_centre.setVisibility(View.GONE);
    }

    public void showCentreText()
    {
        tv_centre.setVisibility(View.VISIBLE);
    }
}
