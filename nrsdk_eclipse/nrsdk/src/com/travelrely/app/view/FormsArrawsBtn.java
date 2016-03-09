package com.travelrely.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;

public class FormsArrawsBtn extends LinearLayout
{
    private ImageView imageView, arrow_right;

    private TextView tv_left, tv_centre;

    public FormsArrawsBtn(Context context)
    {
        super(context);
    }

    public FormsArrawsBtn(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.forms_rraws_btn, this);
        init();
    }

    private void init()
    {
        imageView = (ImageView) findViewById(R.id.img_rraws);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_centre = (TextView) findViewById(R.id.tv_centre);
        arrow_right = (ImageView) findViewById(R.id.arrow_right);
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
    public void setTextLeft(int text)
    {
        tv_left.setText(text);
    }

    public void setTextLeft(String text)
    {
        tv_left.setText(text);
    }
    
    public String getTextLeft()
    {
        return tv_left.getText().toString();
    }

    public void setTextCentre(int text)
    {
        tv_centre.setText(text);
    }
    
    public void setTextCentre(String text)
    {
        tv_centre.setText(text);
    }

    public void setHideImage()
    {
        imageView.setVisibility(View.GONE);
    }

    public void setShowImage()
    {
        imageView.setVisibility(View.VISIBLE);
    }

    public void setHideTextCentre()
    {
        tv_centre.setVisibility(View.GONE);
    }

    public void setShowTextCentre()
    {
        tv_centre.setVisibility(View.VISIBLE);
    }
    
    public void setRightImgDrawable(int id){
        
        arrow_right.setImageResource(id);
    }
}
