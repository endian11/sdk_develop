package com.travelrely.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;

public class FormsFinishButton extends LinearLayout
{
    private TextView textView;

    public FormsFinishButton(Context context)
    {
        super(context);
    }

    public FormsFinishButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.imagebtn, this);
        textView = (TextView) findViewById(R.id.textView1);
    }

    public void setText(int text)
    {
        textView.setText(text);
    }

    public void setText(String text)
    {
        textView.setText(text);
    }

    public String getText()
    {
        return textView.getText().toString();
    }
}
