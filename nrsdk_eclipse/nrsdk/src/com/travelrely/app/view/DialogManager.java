
package com.travelrely.app.view;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.travelrely.model.Area;
import com.travelrely.sdk.R;

public class DialogManager {
    public static DatePickerDialog createDateDialog(OnDateSetListener onDateSetListener,
            Context context, int year, int monthOfYear, int dayOfMonth) {

        // Calendar calendar=Calendar.getInstance();
        // calendar.
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, onDateSetListener, year,
                monthOfYear, dayOfMonth);
        // DatePickerDialog(this,onDateSetListener,2011,7,20);
        return datePickerDialog;
    }

    public static AlertDialog createSpinnerDialog(
            final OnSpinnerSelectListener onSpinnerSelectListener, Context context,
            List<String> list) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);

        for (String str : list) {
            adapter.add(str);
        }
        listView.setAdapter(adapter);
        listView.setCacheColorHint(0x00000000);
        listView.setBackgroundDrawable(null);
        listView.setBackgroundColor(Color.WHITE);

        builder.setView(listView);
        alertDialog = builder.create();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                alertDialog.dismiss();
                if (onSpinnerSelectListener != null) {

                    onSpinnerSelectListener.onSelected(arg2);
                    alertDialog.dismiss();
                }
            }
        });
        // alertDialog.show();
        setDialogWidth(alertDialog, context);

        return alertDialog;

    }

    public static AlertDialog createAreaSpinnerDialog(
            final OnSpinnerSelectListener onSpinnerSelectListener, Context context, List<Area> list) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);

        for (Area area : list) {
            adapter.add(area.name);
        }
        listView.setAdapter(adapter);
        listView.setCacheColorHint(0x00000000);
        listView.setBackgroundDrawable(null);
        listView.setBackgroundColor(Color.WHITE);

        builder.setView(listView);
        alertDialog = builder.create();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                alertDialog.dismiss();
                if (onSpinnerSelectListener != null) {

                    onSpinnerSelectListener.onSelected(arg2);
                    alertDialog.dismiss();
                }
            }
        });
        // alertDialog.show();
        setDialogWidth(alertDialog, context);

        return alertDialog;

    }

    private static void setDialogWidth(final AlertDialog alertDialog, Context context) {
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()) * 3 / 4; // 设置宽度
        lp.height = (int) (display.getWidth()) * 2 / 4;
        alertDialog.getWindow().setAttributes(lp);
    }

    public static AlertDialog createMessageDialog2(final OnClickListener onClickListener,
            Context context, String msg) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(context.getResources().getString(R.string.Ensure), onClickListener);
        builder.setNegativeButton(context.getResources().getString(R.string.Cancel), onClickListener);

        alertDialog = builder.create();

        // alertDialog.show();
        setDialogWidth(alertDialog, context);

        return alertDialog;

    }

    public static AlertDialog createMessageDialog(final OnClickListener onClickListener,
            Context context, String msg, String ok, String cancel) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(ok, onClickListener);
        builder.setNegativeButton(cancel, onClickListener);

        alertDialog = builder.create();

        // alertDialog.show();
        setDialogWidth(alertDialog, context);

        return alertDialog;

    }

    public static AlertDialog getDialog(Context context, String title, String msg, String[] strs,
            final int type,
            final OnDialogClickListener onDialogClickListener) {
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);
        builder.setMessage(msg);
        builder.setTitle(title);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 30, 30, 20);
        layout.setBackgroundColor(context.getResources().getColor(R.color.app_bg));
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        int width = display.getWidth();
        builder.setView(layout);
        alertDialog = builder.create();
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int position = (Integer) v.getTag();
                if (onDialogClickListener != null) {
                    onDialogClickListener.onButtonClick(type, position);
                }
                alertDialog.dismiss();

            }
        };
        for (int i = 0; i < strs.length; i++) {
            Button button = (Button) layoutInflater.inflate(R.layout.white_blue_btn, null);
            button.setMinWidth((int) (width * 0.75f));
            button.setText(strs[i]);
            button.setOnClickListener(onClickListener);
            button.setTag(i);
            layout.addView(button, layoutParams);

        }

        return alertDialog;
    }

    public interface OnDialogClickListener {

        public void onButtonClick(int type, int position);

    }

    public static AlertDialog createMessageDialog(final OnClickListener onClickListener,
            Context context, String msg) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(context.getResources().getString(R.string.Ensure), onClickListener);

        alertDialog = builder.create();

        // alertDialog.show();
        setDialogWidth(alertDialog, context);

        return alertDialog;

    }

    public static interface OnSpinnerSelectListener {

        public void onSelected(int postion);
    }

    public static interface OnCallTransferClickListener {
        public void onSet();

        public void onClear();

    }

    public static interface OnMedialClickListener {
        public void onSelectGallery();

        public void onSelectCarma();

        public void onSelectLocation();

    }

    public static interface OnPaySelectorListener {
        public void onSelectWebPay();

        public void onSelectSoftPay();

    }

}
