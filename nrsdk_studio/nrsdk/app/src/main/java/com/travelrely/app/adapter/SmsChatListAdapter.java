package com.travelrely.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.travelrely.app.view.SmsEntityItem;
import com.travelrely.sdk.R;
import com.travelrely.app.db.SmsEntityDBHelper;
import com.travelrely.v2.model.SmsEntity;


public class SmsChatListAdapter extends BaseAdapter
{
    public static List<SmsEntity> lMessageSms;
    Context mContext;
    LayoutInflater lInflater;

    public SmsChatListAdapter(Context context, List<SmsEntity> list)
    {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.lMessageSms = list;

        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return lMessageSms.size();
    }

    @Override
    public Object getItem(int position)
    {
        return lMessageSms.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = lInflater.inflate(R.layout.sms_entity_item,
                    null);
        }

        SmsEntity messageSms = lMessageSms.get(position);
        messageSms.setRead(SmsEntity.STATUS_READ);
        SmsEntityDBHelper.getInstance().update(messageSms);

        SmsEntityItem mSmsItem = (SmsEntityItem) convertView;
        mSmsItem.mContext = mContext;
        mSmsItem.init(messageSms, position, this);

        return convertView;
    }
}
