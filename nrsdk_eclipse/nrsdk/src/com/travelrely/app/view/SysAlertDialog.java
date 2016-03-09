
package com.travelrely.app.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;
import com.travelrely.v2.util.ViewUtil;

public class SysAlertDialog extends Dialog implements OnClickListener
{
    private TextView tvTitle;
    private TextView tvDescription;

    private LinearLayout layoutDown;
    private Button btnLeft, btnRight;

    private Button btnOk;
    Context mContext;

    public SysAlertDialog(Context context)
    {
        super(context, R.style.SysDialog);
        this.mContext = context;
        setContentView(R.layout.sys_dialog);
        init();
    }

    @SuppressLint("NewApi")
    private void init()
    {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
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
    
    public void setOnClickListener(OnSysAlertClickListener listener)
    {
        onClickListener = listener;
    }

    @Override
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

    private OnSysAlertClickListener onClickListener;

    public interface OnSysAlertClickListener
    {
        public void onLeftClick(SysAlertDialog dialog);
        public void onRightClick(SysAlertDialog dialog);
        public void onOkClick(SysAlertDialog dialog);
    }
}
