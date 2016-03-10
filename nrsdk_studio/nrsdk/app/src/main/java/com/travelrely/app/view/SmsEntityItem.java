package com.travelrely.app.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.travelrely.app.adapter.SmsChatListAdapter;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.util.TimeUtil;
import com.travelrely.sdk.R;
import com.travelrely.app.db.SmsEntityDBHelper;
import com.travelrely.v2.model.SmsEntity;

/**
 * @author zhangyao
 * @version 2014年7月15日下午5:51:49
 */

public class SmsEntityItem extends BaseLinearLayout implements OnClickListener
{
    public static final String TRA_MESSAGE_SMS = "TRA_MESSAGE_SMS";

    SmsEntity messageSms;
    int position;
    SmsChatListAdapter mAdapter;

    View left, right, time;
    TextView tvLeft, tvRight, tvTime;
    ImageView img_sms_state;

    public static final int TYPE = 9999;

    public SmsEntityItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void init(SmsEntity tMessageSms, int mPosition,
            SmsChatListAdapter adapter)
    {
        this.messageSms = tMessageSms;
        this.position = mPosition;
        this.mAdapter = adapter;

        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        time = findViewById(R.id.layout_msg_sms_time);
        
        tvTime = (TextView) findViewById(R.id.tv_time);

        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setOnClickListener(this);
        tvRight = (TextView) findViewById(R.id.tvRight);
        tvRight.setOnClickListener(this);

        img_sms_state = (ImageView) findViewById(R.id.img_sms_state);
        img_sms_state.setVisibility(View.INVISIBLE);
        img_sms_state.setOnClickListener(this);

        setUI();

        setTag(tMessageSms);
    }

    private void setUI()
    {
        setDate();

        if (messageSms.getDir() == SmsEntity.DIR_INCOMING)
        {
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.GONE);
            if (messageSms.isText())
            {
                tvLeft.setText(messageSms.getBody().toString());
            }
        }
        else
        {
            left.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
            if (messageSms.isText())
            {
                tvRight.setText(messageSms.getBody().toString());
            }
            setSendState(messageSms);
        }
    }

    private void setDate()
    {
        String data_time = messageSms.getDate();
        try
        {
            if (data_time != null)
            {
                // data_time = Utils.timeFormat(message.getTime(),Utils.hm);
                data_time = TimeUtil.getChatTime(messageSms.getDate(), TimeUtil.dateFormat2);
                tvTime.setText(data_time);
                time.setVisibility(View.VISIBLE);
            }
            else
            {
                time.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void showDialog(final String[] items)
    {
        new AlertDialog.Builder(mContext).setTitle("消息选项")
                .setItems(items, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int item)
                    {
                        switch (item)
                        {
                            case 0:
                                copy(messageSms.getBody(), mContext);
                                Toast.makeText(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.textCopied),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                forward();
                                break;
                            case 2:
                                if (delMsg())
                                {
                                    Intent intent = new Intent();
                                    // intent.setAction(MessageListsSMSActivity.RECEIVER_ACTION);
                                    intent.setAction(IAction.MSM_ACTION);
                                    mContext.sendBroadcast(intent);
                                    Toast.makeText(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.delSucc),
                                            Toast.LENGTH_SHORT).show();
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }).show();// 显示对话框
    }

    @Override
    public void onClick(View v)
    {
        String[] str = new String[] { getResources().getString(R.string.copy),
                getResources().getString(R.string.forward),
                getResources().getString(R.string.delete) };
        int id = v.getId();
		if (id == R.id.tvLeft) {
			showDialog(str);
		} else if (id == R.id.tvRight) {
			showDialog(str);
		} else if (id == R.id.img_sms_state) {
			if (Engine.getInstance().isNRRegisted)
			{
			    showSendDialod();
			}
			else
			{
			    Engine.getInstance().showSysDialogAct(
			            mContext,
			            getResources().getString(R.string.tv_hint),
			            getResources().getString(
			                    R.string.tv_un_reg_noroamiing_body), "",
			            "", 0, "");
			}
		} else {
		}
    }

    public boolean delMsg()
    {
        SmsEntityDBHelper sMessageDBHelper = SmsEntityDBHelper.getInstance();
        return sMessageDBHelper.deleteMessageSms("id",
                String.valueOf(messageSms.getId()));
    }

    public void forward()
    {
    }

    private void setSendState(SmsEntity tSms)
    {
        switch (tSms.getStatus())
        {
            case SmsEntity.STATUS_FAILED:
                img_sms_state.setVisibility(View.VISIBLE);
                break;
                
            default:
                img_sms_state.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void retransmission(SmsEntity trSms)
    {
        Engine.getInstance().sendSMS(mContext, trSms.getId());

        SmsEntityDBHelper sMessageDBHelper = SmsEntityDBHelper.getInstance();
        sMessageDBHelper.update(trSms);
    }

    private void showSendDialod()
    {
        final MyAlertDialog myDialog = new MyAlertDialog(mContext);
        myDialog.setTitle("提示");
        myDialog.setMessage("是否重发");
        myDialog.setPositiveButton("是", new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                retransmission(messageSms);
                myDialog.dismiss();
            }
        });
        myDialog.setNegativeButton("否", new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                myDialog.dismiss();
            }
        });
    }
}
