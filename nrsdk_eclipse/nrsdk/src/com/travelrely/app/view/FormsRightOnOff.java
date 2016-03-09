package com.travelrely.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.travelrely.sdk.R;

public class FormsRightOnOff extends LinearLayout{
    
    public ToggleButton tButton;
    TextView tvLeft;
    public boolean isCheck;

    public FormsRightOnOff(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.forms_right_on_off, this);
        init();
    }

    private void init(){
        
        tButton = (ToggleButton)findViewById(R.id.toggleButton1);
        tvLeft = (TextView)findViewById(R.id.tv_left);
    }
    
    public void setTextLeft(String str){
        tvLeft.setText(str);
    }
    
    public void setTextLeft(int id){
        tvLeft.setText(getResources().getString(id));
    }

    public void setToggleBackground(int id){
        tButton.setBackgroundResource(id);
    }
    
    @SuppressLint("NewApi")
    public void setBackgroundTg(int id){
        
        tButton.setBackground(getResources().getDrawable(id));
    }
}
