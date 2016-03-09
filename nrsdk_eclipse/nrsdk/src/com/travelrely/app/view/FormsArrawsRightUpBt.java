package com.travelrely.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;

public class FormsArrawsRightUpBt extends RelativeLayout
{
    private ImageView imageView;

    private TextView tv_left, tv_centre, tv_right, tv_right_two;

    private ImageView ivLeft;
    private TextView tvLeft1;

    public FormsArrawsRightUpBt(Context context)
    {
        super(context);
    }

    public FormsArrawsRightUpBt(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.forms_rraws_right_up, this);
        init();
    }

    private void init()
    {
        imageView = (ImageView) findViewById(R.id.img_rraws);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_centre = (TextView) findViewById(R.id.tv_centre);
        tv_right_two = (TextView) findViewById(R.id.tv_right_two);

        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        tvLeft1 = (TextView) findViewById(R.id.tvLeft1);
    }

    public void setLeftImage(int resId)
    {
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(resId);
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

    public void setLeft1Text(int text)
    {
        tvLeft1.setVisibility(View.VISIBLE);
        tvLeft1.setText(text);
    }

    public void setLeft1Text(String text)
    {
        tvLeft1.setVisibility(View.VISIBLE);
        tvLeft1.setText(text);
    }

    public void setCentreText(int text)
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

    public void setRight2Text(String text)
    {
        tv_right_two.setText(text);
    }
    
    public void setRight2Text(int text)
    {
        tv_right_two.setText(text);
    }

    public void setRight2TextColor(int id)
    {
        tv_right_two.setTextColor(getResources().getColor(id));
    }

    public void showRight2Text()
    {
        tv_right_two.setVisibility(View.VISIBLE);
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
    //==================================
    public ImageView getImageView(){
    	return imageView;
    }
}
