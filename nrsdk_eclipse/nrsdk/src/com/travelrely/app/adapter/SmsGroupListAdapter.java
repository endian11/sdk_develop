package com.travelrely.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.travelrely.sdk.R;
import com.travelrely.v2.db.SmsEntityDBHelper;
import com.travelrely.v2.model.SmsEntity;
import com.travelrely.v2.util.TimeUtil;


public class SmsGroupListAdapter extends NavigationBaseAdapter
{
    public List<SmsEntity> lSms;
    Context mContext;
    private LayoutInflater mInflater;
    private int mRightWidth = 0;

    public SmsGroupListAdapter(Context context, List<SmsEntity> mList,
            int rightWidth)
    {
        this.mContext = context;
        this.lSms = mList;
        this.mRightWidth = rightWidth;

        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return lSms.size();
    }

    @Override
    public Object getItem(int position)
    {
        return lSms.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ListHoder listHoder = new ListHoder();
        SmsEntity tMessageSms = lSms.get(position);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.message_sms_adapter, null);
            listHoder.address = (TextView) convertView
                    .findViewById(R.id.tv_name);
            listHoder.date = (TextView) convertView.findViewById(R.id.time);
            listHoder.body = (TextView) convertView.findViewById(R.id.body);
            listHoder.smsCount = (TextView) convertView
                    .findViewById(R.id.tv_msg_count);
            listHoder.item_left = convertView.findViewById(R.id.left_layout);
            listHoder.item_right = convertView.findViewById(R.id.right_layout);

            convertView.setTag(listHoder);
        }
        else
        {
            listHoder = (ListHoder) convertView.getTag();
        }

        String time = null;
        try
        {
            time = TimeUtil.getChatTime(tMessageSms.getDate(), TimeUtil.dateFormat2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String name = tMessageSms.getNickName();
        if (name != null)
        {
            listHoder.address.setText(name);
        }
        else
        {
            listHoder.address.setText(tMessageSms.getAddress());
        }
        listHoder.date.setText(time);
        listHoder.body.setText(getBody(tMessageSms));

        LinearLayout.LayoutParams lp1 = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        listHoder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
                LayoutParams.MATCH_PARENT);
        listHoder.item_right.setLayoutParams(lp2);

        listHoder.item_right.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mListener != null)
                {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        if (tMessageSms.getRead() != SmsEntity.STATUS_READ)
        {
			int smsCount= SmsEntityDBHelper.getInstance().getUnReadCount(tMessageSms.getAddress());
        	if (smsCount == 0){
        		
        	}else{
        		
        		listHoder.smsCount.setVisibility(View.VISIBLE);
        		listHoder.smsCount.setText(String.valueOf(smsCount));
        	}
        }
        else
        {
            listHoder.smsCount.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public class ListHoder
    {
        TextView address = null;
        TextView person = null;
        TextView date = null;
        TextView body = null;
        TextView smsCount = null;
        View item_left;
        View item_right;
    }

    private String getBody(SmsEntity tSms)
    {
        String body = "";
        
        if (tSms.getDir() == SmsEntity.DIR_INCOMING)
        {
            return tSms.getBody();
        }
        
        switch (tSms.getStatus())
        {
            case SmsEntity.STATUS_FAILED:
                body = "["
                        + mContext.getResources().getString(
                                R.string.tv_send_sms_error) + "]"
                        + tSms.getBody();
                break;
                
            default:
                body = tSms.getBody();
                break;
        }
        return body;
    }
}
