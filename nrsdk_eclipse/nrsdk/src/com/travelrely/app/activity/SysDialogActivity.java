package com.travelrely.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.app.view.SmsEntityItem;
import com.travelrely.sdk.R;

public class SysDialogActivity extends Activity implements OnClickListener
{
    private TextView tvTitle;
    private TextView tvDescription;
    
    private LinearLayout layoutDown;
    private Button btnLeft, btnRight;
    
    private Button btnOk;
    
    private String title = "";
    private String description = "";
    private String left = "";
    private String right = "";
    private int TYPE;
    private String ACT_NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sys_dialog);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
        	title = bundle.getString("TITLE");
        	description = bundle.getString("DESCRIPTION");
        	left = bundle.getString("LEFT");
        	right = bundle.getString("RIGHT");
        	TYPE = bundle.getInt("TYPE");
        	ACT_NAME = bundle.getString("ACT_NAME");
        }

        init();
    }

    private void init()
    {
    	tvTitle = (TextView) findViewById(R.id.tvTitle);
    	tvTitle.setText(title);
    	
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(description);
        
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        layoutDown = (LinearLayout) findViewById(R.id.layoutDowm);
        if (left == null || left.equals("")
        		|| right == null || right.equals(""))
        {
        	layoutDown.setVisibility(View.GONE);
        	btnOk.setVisibility(View.VISIBLE);
        }

        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(this);
        btnLeft.setText(left);
        
        btnRight = (Button) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(this);
        btnRight.setText(right);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
		if (id == R.id.btnLeft) {
			if(TYPE == SmsEntityItem.TYPE){
			    
			}
			finish();
		} else if (id == R.id.btnRight) {
			finish();
		} else if (id == R.id.btnOk) {
			okClick();
		} else {
		}
    }
    
    private void okClick()
    {
        Intent intent;
        
        if (ACT_NAME != null && ACT_NAME.equals("HomePageActivity"))
        {
            intent = new Intent(this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        
        if (ACT_NAME != null && ACT_NAME.equals("LoginActivity"))
        {
            intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }
        
        finish();
    }
}
