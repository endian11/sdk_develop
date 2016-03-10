package com.travelrely.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;

public class NavigationBar extends RelativeLayout implements OnClickListener
{
    private LinearLayout leftBtn;

    private LinearLayout rightBtn;

    protected TextView title;

    private RadioGroup radioGroup;
    private RadioButton radioLeft, radioRight;

    private ImageView type;

    private TextView leftText;

    private TextView rightText;

    private ImageView leftImg;
    
    private ImageView rightImg;

    public ImageView getType()
    {
        return type;
    }

    public void setType(ImageView type)
    {
        this.type = type;
    }

    private OnNavigationBarClick onNavigationBarClick;

    public View getLeftBtn()
    {
        return leftBtn;
    }

    public View getRightBtn()
    {
        return rightBtn;
    }

    public TextView getTitle()
    {
        return title;
    }

    public TextView getLeftText()
    {
        return leftText;
    }

    public TextView getRightText()
    {
        return rightText;
    }

    public ImageView getLeftImg()
    {
        return leftImg;
    }
    
    public ImageView getRightImg()
    {
        return rightImg;
    }

    public OnNavigationBarClick getOnNavigationBarClick()
    {
        return onNavigationBarClick;
    }

    public void setOnNavigationBarClick(
            OnNavigationBarClick onNavigationBarClick)
    {
        this.onNavigationBarClick = onNavigationBarClick;
    }

    public NavigationBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setLeftBackground(int res)
    {
        leftBtn.setBackgroundResource(res);
    }

    public void setRightBackground(int res)
    {
        rightBtn.setBackgroundResource(res);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        leftBtn = (LinearLayout) findViewById(R.id.left_layout);
        leftBtn.setOnClickListener(this);

        rightBtn = (LinearLayout) findViewById(R.id.right_layout);
        rightBtn.setOnClickListener(this);

        title = (TextView) findViewById(R.id.title);
        title.setOnClickListener(this);

        radioGroup = (RadioGroup) findViewById(R.id.call_radio_group);
        radioLeft = (RadioButton) findViewById(R.id.left_radio);
        radioRight = (RadioButton) findViewById(R.id.right_radio);

        type = (ImageView) findViewById(R.id.img_type);

        leftText = (TextView) findViewById(R.id.left_text);
        leftImg = (ImageView) findViewById(R.id.left_img);
        
        rightText = (TextView) findViewById(R.id.right_text);
        rightImg = (ImageView) findViewById(R.id.right_img);
    }

    public void setLeftText(String str)
    {
        leftText.setText(str);
    }

    public void setLeftText(int strId)
    {
        leftText.setText(getResources().getString(strId));
    }

    public void setTitleText(String str)
    {
        title.setText(str);
    }

    public void setTitleText(int strId)
    {
        title.setText(getResources().getString(strId));
    }

    public void setRightText(String str)
    {
        rightText.setText(str);
    }

    public void setRightText(int strId)
    {
        rightText.setText(getResources().getString(strId));
    }

    public void setTypeBackground(int imgId)
    {
        type.setBackgroundResource(imgId);
    }

    @Override
    public void onClick(View v)
    {
        if (onNavigationBarClick == null)
        {
            return;
        }

        int id = v.getId();
		if (id == R.id.left_layout) {
			onNavigationBarClick.onLeftClick();
		} else if (id == R.id.right_layout) {
			onNavigationBarClick.onRightClick();
		} else if (id == R.id.title) {
			onNavigationBarClick.onTitleClick();
		}
    }

    public void hideLeft()
    {
        leftBtn.setVisibility(View.GONE);
    }

    public void hideRight()
    {
        rightBtn.setVisibility(View.GONE);
    }

    public void hideTitle()
    {
        title.setVisibility(View.GONE);
    }

    public void showTitle()
    {
        title.setVisibility(View.VISIBLE);
    }

    public void showLeft()
    {
        leftBtn.setVisibility(View.VISIBLE);
    }

    public void showRight()
    {
        rightBtn.setVisibility(View.VISIBLE);
    }

    public void show()
    {
        setVisibility(View.VISIBLE);
    }

    public void hide()
    {
        setVisibility(View.GONE);
    }

    public void showRadioGroup()
    {
        radioGroup.setVisibility(View.VISIBLE);
    }

    public void hideRadioGroup()
    {
        radioGroup.setVisibility(View.GONE);
    }
    
    public RadioGroup getRadioGroup()
    {
        return radioGroup;
    }
    
    public void setRadioGroupText(String l, String r)
    {
        radioLeft.setText(l);
        radioRight.setText(r);
    }
    
    public void setRadioGroupText(int l, int r)
    {
        radioLeft.setText(l);
        radioRight.setText(r);
    }
    
    public RadioButton getRadioLeft()
    {
        return radioLeft;
    }
    
    public RadioButton getRadioRight()
    {
        return radioRight;
    }
    public void showType()
    {
        type.setVisibility(View.VISIBLE);
    }

    public void hideType()
    {
        type.setVisibility(View.GONE);
    }

    public void hideLeftText()
    {
        leftText.setVisibility(View.INVISIBLE);
    }

    public void showLeftText()
    {
        leftText.setVisibility(View.VISIBLE);
    }

    public void hideRightText()
    {
        leftText.setVisibility(View.INVISIBLE);
    }

    public void showRightText()
    {
        leftText.setVisibility(View.VISIBLE);
    }

    public void hideRightImg()
    {
        rightImg.setVisibility(View.GONE);
    }

    public void showRightImg()
    {
        rightImg.setVisibility(View.VISIBLE);
    }
    
    public void hideLeftImg()
    {
        leftImg.setVisibility(View.GONE);
    }

    public void showLeftImg()
    {
        leftImg.setVisibility(View.VISIBLE);
    }

    public static interface OnNavigationBarClick
    {
        public void onLeftClick();

        public void onRightClick();

        public void onTitleClick();
    }
}
