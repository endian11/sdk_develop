package com.travelrely.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.travelrely.sdk.R;

public class FormsArrawsRightButton extends LinearLayout
{
    private ImageView imageView;
    public TextView tv_left, tv_centre, tv_right, tv_right_two;
    ToggleButton tButton;

    RelativeLayout view;

    public FormsArrawsRightButton(Context context)
    {
        super(context);
    }

    public FormsArrawsRightButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.forms_rraws_right_button, this);
        init();
    }

    private void init()
    {
        imageView = (ImageView) findViewById(R.id.img_rraws);
        tv_left = (TextView) findViewById(R.id.tv_coutent);
        tv_centre = (TextView) findViewById(R.id.tv_centre);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right_two = (TextView) findViewById(R.id.tv_right2);
        view = (RelativeLayout) findViewById(R.id.formsArrawsRightButton);
        tButton = (ToggleButton) findViewById(R.id.toggleButton1);
    }

    // /**
    // * 设置图片资源
    // */
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

    public String getLeftText()
    {
        return tv_left.getText().toString();
    }

    public void setLeftTextColor(int color)
    {
        tv_left.setTextColor(getResources().getColor(color));
    }

    public void setCentreText(int text)
    {
        tv_centre.setText(text);
    }

    public void setRightText(int text)
    {
        showRightText();
        tv_right.setText(text);
    }

    public void setRightText(String text)
    {
        showRightText();
        tv_right.setText(text);
    }

    public void setRightHint(int text)
    {
        showRightText();
        tv_right.setHint(text);
    }

    public void setRightHint(String text)
    {
        showRightText();
        tv_right.setHint(text);
    }

    public void hideCentreText()
    {
        tv_centre.setVisibility(View.GONE);
    }
    
    public void showCentreText()
    {
        tv_centre.setVisibility(View.VISIBLE);
    }

    public void hideRightText()
    {
        tv_right.setVisibility(View.GONE);
    }
    
    public void showRightText()
    {
        tv_right.setVisibility(View.VISIBLE);
    }

    public void hideImg()
    {
        imageView.setVisibility(View.GONE);
    }
    
    public void showImg()
    {
        imageView.setVisibility(View.VISIBLE);
    }

    public void setRightTextColor(int color)
    {
        tv_right.setTextColor(getResources().getColor(color));
    }

    public void setRight2TextColor(int color)
    {
        tv_right_two.setTextColor(getResources().getColor(color));
    }

    public String getRightText()
    {
        return tv_right.getText().toString();
    }

    public void setRight2Text(String text)
    {
        tv_right_two.setText(text);
    }

    public void showRight2Text()
    {
        tv_right_two.setVisibility(View.VISIBLE);
    }

    public void setLeftTextLeftCompoundDrawable(int resId)
    {
        Drawable d = getResources().getDrawable(resId);
        d.setBounds(0, 0, 100, 100); // 必须设置图片大小，否则不显示
        tv_left.setCompoundDrawables(d, null, null, null);
    }

    public void setBtBackground(int id)
    {
        view.setBackgroundResource(id);
    }
    
    public void showRightToggleButton(){
        tButton.setVisibility(View.VISIBLE);
    }
    
    public void hidRightToggleButton(){
        tButton.setVisibility(View.GONE);
    }
    
    public ToggleButton getRightToggleButton(){
        return tButton;
    }
    
    public ImageView getImageView(){
        return imageView;
    }
}
