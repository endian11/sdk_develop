package com.travelrely.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;

public class FormsArrawsRightCentreBt extends LinearLayout
{
    private ImageView imageView;
    private TextView tv_left, tv_centre, tv_right, tvRightTwo;

    public FormsArrawsRightCentreBt(Context context)
    {
        super(context);
    }

    public FormsArrawsRightCentreBt(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.forms_rraws_right_centre, this);

        init();
    }

    private void init()
    {
        imageView = (ImageView) findViewById(R.id.img_rraws);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_centre = (TextView) findViewById(R.id.tv_centre);
        tvRightTwo = (TextView) findViewById(R.id.tv_right_two);
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

    public void hideCentreText()
    {
        tv_centre.setVisibility(View.GONE);
    }

    public void showCentreText()
    {
        tv_centre.setVisibility(View.VISIBLE);
    }

    public TextView getRightTextView()
    {
        return tv_right;
    }
    
    public void setRightText(int text)
    {
        tv_right.setText(text);
    }

    public void setRightText(String text)
    {
        tv_right.setText(text);
    }

    public String getRightText()
    {
        return tv_right.getText().toString();
    }
    
    public void setRightHint(int text)
    {
        tv_right.setHint(text);
    }

    public void setRightHint(String text)
    {
        tv_right.setHint(text);
    }

    public void setRightTextColor(int id)
    {
        tv_right.setTextColor(id);
    }

    public void hideRightText()
    {
        tv_right.setVisibility(View.GONE);
    }

    public void showRightText()
    {
        tv_right.setVisibility(View.VISIBLE);
    }

    public void setRight2Text(int text)
    {
        tvRightTwo.setText(text);
    }

    public void setRight2Text(String text)
    {
        tvRightTwo.setText(text);
    }

    public void hideRight2Text()
    {
        tvRightTwo.setVisibility(View.GONE);
    }

    public void showRight2Text()
    {
        tvRightTwo.setVisibility(View.VISIBLE);
    }

    public void hideImage()
    {
        imageView.setVisibility(View.GONE);
    }

    public void showImage()
    {
        imageView.setVisibility(View.VISIBLE);
    }
}
