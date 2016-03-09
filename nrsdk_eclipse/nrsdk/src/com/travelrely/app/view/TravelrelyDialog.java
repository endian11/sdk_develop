
package com.travelrely.app.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.sdk.R;

public class TravelrelyDialog extends Dialog implements android.view.View.OnClickListener {
    Context mContext;

    LinearLayout mDialogContentLayout;

    TextView mTitle;

    LayoutInflater mLayoutInflater;

    int type;

    View root;

    public OnDialogClickListener getOnDialogClickListener() {
        return onDialogClickListener;
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    public TravelrelyDialog(Context context, int theme) {
        super(context, R.style.TravelrylyDialog);
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(getContext());
    }

    public void setDialogTitle(String title) {

        mTitle.setText(title);
    }

    public void setContents(Context context, String title, String msg, String[] strs,
            final int type, final OnDialogClickListener onDialogClickListener) {
        root = mLayoutInflater.inflate(R.layout.dialog_layout, null);
        setContentView(root);

        mDialogContentLayout = (LinearLayout)root.findViewById(R.id.dialog_content_layout);
        mTitle = (TextView)root.findViewById(R.id.dialot_title);
        setOnDialogClickListener(onDialogClickListener);
        setDialogTitle(title);
        setButtons(strs);
        this.type = type;

    }

    public void setButtons(String[] str) {
        mDialogContentLayout.removeAllViews();
        for (int i = 0; i < str.length; i++) {

            View group = mLayoutInflater.inflate(R.layout.dialog_item_tv, null);
            TextView tv = (TextView)group.findViewById(R.id.dialog_content_tv);
            tv.setText(str[i]);
            group.setTag(str[i]);
            View v = group.findViewById(R.id.dialog_content_base_line);
            mDialogContentLayout.addView(group);

            group.setOnClickListener(this);
            if (i == str.length - 1) {
                v.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (onDialogClickListener != null) {
            onDialogClickListener.onDialogItemClick(v, v.getTag().toString(), type);
        }

    }

    private OnDialogClickListener onDialogClickListener;

    public interface OnDialogClickListener {
        public void onDialogItemClick(View v, String str, int type);
    }
}
