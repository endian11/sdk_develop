
package com.travelrely.app.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.travelrely.core.util.Utils;
import com.travelrely.sdk.R;

public class SysListAlert extends Dialog implements OnItemClickListener
{
    private int id;
    private ListView listView;

    public SysListAlert(Context context, int id, String[] items, String[] items2, String exit,
            OnListAlertClickListener listener)
    {
        super(context, R.style.listAlertStyle);

        this.id = id;
        this.onClickListener = listener;

        setContentView(R.layout.sys_list_alert);

        listView = (ListView) findViewById(R.id.content_list);
        listView.setOnItemClickListener(this);

        AlertAdapter adapter = new AlertAdapter(context, items, items2, exit);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);

        // set a large value put it in bottom        
        Window w = getWindow();
        w.setWindowAnimations(R.style.listAlertStyle);
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        onWindowAttributesChanged(lp);
        setCanceledOnTouchOutside(true);
    }

    private OnListAlertClickListener onClickListener;

    public interface OnListAlertClickListener
    {
        public void onListAlertClick(int id, int which);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        dismiss();

        if (onClickListener == null)
        {
            return;
        }

        onClickListener.onListAlertClick(this.id, position);
        listView.requestFocus();
    }
}

class AlertAdapter extends BaseAdapter
{
    public static final int TYPE_UP_BUTTON = 1;
    public static final int TYPE_DOWN_BUTTON = 2;
    public static final int TYPE_CENTER_BUTTON = 3;
    public static final int TYPE_EXIT = 4;

    private List<String> items;
    private List<String> items2;
    private int[] types;

    private Context context;

    public AlertAdapter(Context context, String[] items, String[] itm2, String exit)
    {
        if (items == null || items.length == 0)
        {
            this.items = new ArrayList<String>();
        }
        else
        {
            this.items = Utils.stringsToList(items);
        }

        if (itm2 == null || itm2.length == 0)
        {
            this.items2 = new ArrayList<String>();
        }
        else
        {
            this.items2 = Utils.stringsToList(itm2);
        }

        this.types = new int[this.items.size() + 1];
        this.context = context;

        if (exit == null || exit.equals(""))
        {
            exit = context.getString(R.string.cancel);
        }

        this.items.add(exit);
        this.items2.add(exit);

        types[this.items.size() - 1] = TYPE_EXIT;
        types[this.items.size() - 2] = TYPE_DOWN_BUTTON;
        types[0] = TYPE_UP_BUTTON;
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return super.isEnabled(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final String textString = this.items.get(position);
        int type = types[position];
        ViewHolder holder;
        if (convertView == null
                || ((ViewHolder) convertView.getTag()).type != type)
        {
            holder = new ViewHolder();
            if (type == TYPE_UP_BUTTON)
            {
                convertView = View.inflate(context,
                        R.layout.alert_dialog_list_up, null);
                holder.viewLine = convertView.findViewById(R.id.view_line);
                if (items.size() <= 2) {
                    convertView.setBackgroundDrawable(context.getResources().getDrawable(
                            R.drawable.sys_dialog_shape_bg));
                    holder.viewLine.setVisibility(View.GONE);
                } else {
                    convertView.setBackgroundDrawable(context.getResources().getDrawable(
                            R.drawable.shape_up_bg));
                    holder.viewLine.setVisibility(View.VISIBLE);
                }
            }
            else if (type == TYPE_DOWN_BUTTON)
            {
                convertView = View.inflate(context,
                        R.layout.alert_dialog_list_down, null);
            }
            else if (type == TYPE_EXIT)
            {
                convertView = View.inflate(context,
                        R.layout.alert_dialog_list_exit, null);
            }
            else
            {
                convertView = View.inflate(context,
                        R.layout.alert_dialog_list_center, null);
            }

            holder.text = (TextView) convertView.findViewById(R.id.popup_text);
            holder.textBelow = (TextView) convertView.findViewById(R.id.popup_text_below);
            if (items2 != null && items2.size() > 1) {
                holder.textBelow.setVisibility(View.VISIBLE);
            } else {
                holder.textBelow.setVisibility(View.GONE);
                holder.text.setPadding(0, 20, 0, 20);
            }
            if (type == 4) {
                holder.textBelow.setVisibility(View.GONE);
            }
            holder.type = type;
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(textString);
        if (items2 != null) {
            if (items2.size() > 1) {
                holder.textBelow.setText(this.items2.get(position));
            }
        }
        return convertView;
    }

    static class ViewHolder
    {
        TextView text;
        TextView textBelow;
        View viewLine;
        int type;
    }
}
