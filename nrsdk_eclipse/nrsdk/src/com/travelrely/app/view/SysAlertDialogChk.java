
package com.travelrely.app.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.core.util.ViewUtil;
import com.travelrely.sdk.R;

public class SysAlertDialogChk extends Dialog implements OnClickListener
{
    private TextView tvTitle;
    private TextView tvDescription;

    private LinearLayout layoutDown;
    private Button btnLeft, btnRight;

    private Button btnOk;
    Context mContext;
    private CheckBox mCheckBox;

    public SysAlertDialogChk(Context context)
    {
        super(context, R.style.SysDialog);
        this.mContext = context;
        setContentView(R.layout.sys_dialog_chk);
        init();
    }

    @SuppressLint("NewApi")
    private void init()
    {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
       
        
        mCheckBox = (CheckBox) findViewById(R.id.check_box);
        mCheckBox.setChecked(false);
        if (mCheckBox.isChecked() == false){
        	 btnOk.setClickable(false);
        }
        //给CheckBox设置事件监听  
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                // TODO Auto-generated method stub  
                if(isChecked){  
                	btnOk.setClickable(true);  
                }else{  
                	btnOk.setClickable(false); 
                }  
            }  
        });  

        
        
        layoutDown = (LinearLayout) findViewById(R.id.layoutDowm);
        
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(this);

        btnRight = (Button) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);
    }

    public void setTitle(String title)
    {
        if (TextUtils.isEmpty(title))
        {
            tvTitle.setVisibility(View.GONE);
        }
        else
        {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
    }

    public void setTitle(int title)
    {
        tvTitle.setText(mContext.getResources().getString(title));
    }

    @SuppressLint("NewApi")
    public void setMessage(String msg)
    {
        if(msg == null || msg.equals("")){
            tvDescription.setVisibility(View.GONE);
            tvTitle.setPadding(0, ViewUtil.dip2px(mContext, 12), 0, ViewUtil.dip2px(mContext, 12));
        }else{
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(msg);
        }
    }

    public void setMessage(int msg)
    {
        if(msg == 0){
            tvDescription.setVisibility(View.GONE);
            tvTitle.setPadding(0, ViewUtil.dip2px(mContext, 12), 0, ViewUtil.dip2px(mContext, 12));
        }else{
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(mContext.getResources().getString(msg));
        }
    }

    public String getMessage()
    {
        return tvDescription.getText().toString().trim();
    }

    public void setLeft(String msg)
    {
        layoutDown.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.GONE);
        btnLeft.setText(msg);
    }
    
    public void setLeft(int msg)
    {
        layoutDown.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.GONE);
        btnLeft.setText(mContext.getResources().getString(msg));
    }

    public void setRight(String msg)
    {
        layoutDown.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.GONE);
        btnRight.setText(msg);
    }
    
    public void setRight(int msg)
    {
        layoutDown.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.GONE);
        btnRight.setText(mContext.getResources().getString(msg));
    }

    public void setOk(String msg)
    {
        layoutDown.setVisibility(View.GONE);
        btnOk.setVisibility(View.VISIBLE);
        btnOk.setText(msg);
    }
    
    public void setOnClickListener(OnSysAlertChkClickListener l)
    {
        onClickListener = l;
    }

    @SuppressLint("ResourceAsColor") @Override
    public void onClick(View v)
    {
        dismiss();

        int id = v.getId();
		if (id == R.id.btnLeft) {
			if (onClickListener != null)
			{
			    onClickListener.onLeftClick(this);
			}
		} else if (id == R.id.btnRight) {
			if (onClickListener != null)
			{
			    onClickListener.onRightClick(this);
			}
		} else if (id == R.id.btnOk) {
			if (mCheckBox.isChecked()){//如果用户勾选的话
				
				 if (onClickListener != null)
			     {
			         onClickListener.onOkClick(this);
			     }
			}else{
			}
		} else {
		}
    }

    private OnSysAlertChkClickListener onClickListener;

    public interface OnSysAlertChkClickListener
    {
        public void onLeftClick(SysAlertDialogChk dialog);
        public void onRightClick(SysAlertDialogChk dialog);
        public void onOkClick(SysAlertDialogChk dialog);
    }
}
