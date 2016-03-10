package com.travelrely.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;

import com.travelrely.core.nrs.Engine;
import com.travelrely.sdk.R;

public class Keyboard extends LinearLayout implements OnClickListener,
		OnLongClickListener
{
	CallNumberItem num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8,
			num_9, num_0, num_jing, num_xing;

	public Keyboard(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		num_1 = (CallNumberItem) findViewById(R.id.num_1);
		num_2 = (CallNumberItem) findViewById(R.id.num_2);
		num_3 = (CallNumberItem) findViewById(R.id.num_3);
		num_4 = (CallNumberItem) findViewById(R.id.num_4);
		num_5 = (CallNumberItem) findViewById(R.id.num_5);
		num_6 = (CallNumberItem) findViewById(R.id.num_6);
		num_7 = (CallNumberItem) findViewById(R.id.num_7);
		num_8 = (CallNumberItem) findViewById(R.id.num_8);
		num_9 = (CallNumberItem) findViewById(R.id.num_9);
		num_0 = (CallNumberItem) findViewById(R.id.num_0);
		num_xing = (CallNumberItem) findViewById(R.id.num_xing);
		num_jing = (CallNumberItem) findViewById(R.id.num_jing);

		init();
	}

	private void init()
	{
		num_1.setNum("1");
		num_2.setNum("2");
		num_3.setNum("3");
		num_4.setNum("4");
		num_5.setNum("5");
		num_6.setNum("6");
		num_7.setNum("7");
		num_8.setNum("8");
		num_9.setNum("9");
		num_0.setNum("0");
		num_xing.setNum("*");
		num_jing.setNum("#");

		num_1.setZiMu("");
		num_2.setZiMu("ABC");
		num_3.setZiMu("DEF");
		num_4.setZiMu("GHI");
		num_5.setZiMu("JKL");
		num_6.setZiMu("MNO");
		num_7.setZiMu("PQRS");
		num_8.setZiMu("TUV");
		num_9.setZiMu("WXYZ");
		num_0.setZiMu("+");

		num_xing.setZiMu("");
		num_jing.setZiMu("");

		num_1.setOnClickListener(this);
		num_0.setOnLongClickListener(this);
		num_2.setOnClickListener(this);
		num_3.setOnClickListener(this);
		num_4.setOnClickListener(this);
		num_5.setOnClickListener(this);
		num_6.setOnClickListener(this);
		num_7.setOnClickListener(this);
		num_8.setOnClickListener(this);
		num_9.setOnClickListener(this);
		num_0.setOnClickListener(this);
		num_xing.setOnClickListener(this);
		num_jing.setOnClickListener(this);

		num_xing.setOnLongClickListener(this);
		num_jing.setOnLongClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}

	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.num_1) {
			Engine.getInstance().sendKeyVal(getContext(), '1');
		} else if (id == R.id.num_2) {
			Engine.getInstance().sendKeyVal(getContext(), '2');
		} else if (id == R.id.num_3) {
			Engine.getInstance().sendKeyVal(getContext(), '3');
		} else if (id == R.id.num_4) {
			Engine.getInstance().sendKeyVal(getContext(), '4');
		} else if (id == R.id.num_5) {
			Engine.getInstance().sendKeyVal(getContext(), '5');
		} else if (id == R.id.num_6) {
			Engine.getInstance().sendKeyVal(getContext(), '6');
		} else if (id == R.id.num_7) {
			Engine.getInstance().sendKeyVal(getContext(), '7');
		} else if (id == R.id.num_8) {
			Engine.getInstance().sendKeyVal(getContext(), '8');
		} else if (id == R.id.num_9) {
			Engine.getInstance().sendKeyVal(getContext(), '9');
		} else if (id == R.id.num_0) {
			Engine.getInstance().sendKeyVal(getContext(), '0');
		} else if (id == R.id.num_xing) {
			Engine.getInstance().sendKeyVal(getContext(), '*');
		} else if (id == R.id.num_jing) {
			Engine.getInstance().sendKeyVal(getContext(), '#');
		}
	}

	@Override
	public boolean onLongClick(View v)
	{
		int id = v.getId();
		if (id == R.id.num_0) {
			Engine.getInstance().sendKeyVal(getContext(), '+');
		} else if (id == R.id.num_jing) {
		} else if (id == R.id.num_xing) {
		}
		return true;
	}
}
