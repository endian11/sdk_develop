package com.travelrely.app.activity;

import com.travelrely.sdk.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class NRCallAct extends ViewStackActivity implements OnClickListener
{
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setActiveView(R.layout.act_nr_call);
    }

    @Override
    public int[] getViewIds()
    {
        return new int[] { R.layout.act_nr_call, R.layout.act_nr_dial };
    }

    @Override
    public void OnViewCreated(int id, View view)
    {
        if (id == R.layout.act_nr_call)
        {
            ImageView ivPad = (ImageView) view.findViewById(R.id.ivPad);
            ivPad.setOnClickListener(this);
        }
        else if (id == R.layout.act_nr_dial)
        {
            TextView tvBack = (TextView) view.findViewById(R.id.tvBack);
            tvBack.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
		if (id == R.id.tvHangUp) {
		} else if (id == R.id.tvAnswer) {
		} else if (id == R.id.ivPad) {
			getWindow().getDecorView().setBackgroundResource(R.drawable.nr_call_bg);
			setActiveView(R.layout.act_nr_dial);
		} else if (id == R.id.tvBack) {
			getWindow().getDecorView().setBackgroundResource(R.drawable.nr_call_act_bg);
			setActiveView(R.layout.act_nr_call);
		} else {
		}
    }
}