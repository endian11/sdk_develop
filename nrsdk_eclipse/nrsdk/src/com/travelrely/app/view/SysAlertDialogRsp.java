
package com.travelrely.app.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.core.util.ViewUtil;
import com.travelrely.sdk.R;

public class SysAlertDialogRsp extends Dialog implements OnClickListener
{
    private TextView tvTitle;
    private TextView tvDescription;

    private LinearLayout layoutDown;
    private Button btnLeft, btnRight;

    private Button btnOk;
    Context mContext;
    private EditText mCheckBox;

    public SysAlertDialogRsp(Context context)
    {
        super(context, R.style.SysDialog);
        this.mContext = context;
        setContentView(R.layout.sys_dialog_rsp);
        init();
    }

    @SuppressLint("NewApi")
    private void init()
    {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
       
        
        mCheckBox = (EditText) findViewById(R.id.check_box);
 

        
        
        layoutDown = (LinearLayout) findViewById(R.id.layoutDowm);
        
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(this);

        btnRight = (Button) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);
    }

    /**
     * @return 用户输入的字符串
     */
    public String GetUserInputStr(){
    	return mCheckBox.getText().toString();
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
    
    public void setOnClickListener(OnSysAlertRspClickListener l)
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
			if (onClickListener != null)
			 {
			     onClickListener.onOkClick(this);
			 }
		} else {
		}
    }

    private OnSysAlertRspClickListener onClickListener;

    public interface OnSysAlertRspClickListener
    {
        public void onLeftClick(SysAlertDialogRsp dialog);
        public void onRightClick(SysAlertDialogRsp dialog);
        public void onOkClick(SysAlertDialogRsp dialog);
    }
}
