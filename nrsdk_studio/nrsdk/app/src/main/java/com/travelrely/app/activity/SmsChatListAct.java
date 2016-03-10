package com.travelrely.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;

import com.travelrely.app.adapter.SmsChatListAdapter;
import com.travelrely.app.view.PullDownListView;
import com.travelrely.app.view.PullDownListView.OnRefreshListener;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.util.TimeUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.model.ContactModel;
import com.travelrely.model.ContactModel.TagNumber;
import com.travelrely.sdk.R;
import com.travelrely.app.db.ContactDBHelper;
import com.travelrely.app.db.SmsEntityDBHelper;
import com.travelrely.v2.model.SmsEntity;

/**
 * @author zhangyao
 * @version 2014年7月14日下午12:05:57
 */

public class SmsChatListAct extends NavigationActivity implements
        OnTouchListener, OnClickListener
{
    PullDownListView pListView;
    TextView bt_send;
    EditText ed_body;

    SmsChatListAdapter adapter;
    List<SmsEntity> lMessageSms;
    SmsEntity messageSms;
    int msgCount = 10;

    public static int SEND_SMSING_STATE = 0; // 发送短信状态码，解决后台一次只能发送一条短信的问题，
    // 0=默认初始化， 1=正在发送中， 2=发送结束
    public static String from;
    BroadcastReceiver myReceiver;
    String msgBody;
    int type;
    SmsEntity sendMessageSms;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_chat_sms);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            messageSms = (SmsEntity) bundle.getSerializable("MESSAGE_SMS");
            type = bundle.getInt("TYPE");
            
            from = messageSms.getAddress();
        }

        init();
    }

    private void init()
    {
    	
    	pListView = (PullDownListView) findViewById(R.id.travel_msg_list);
    	bt_send = (TextView) findViewById(R.id.bt_send);
    	ed_body = (EditText) findViewById(R.id.ed_message);
    	
        lMessageSms = new ArrayList<SmsEntity>();

        pListView.setonRefreshListener(onRefreshListener);

        pListView.setOnTouchListener(this);
        bt_send.setOnClickListener(this);

        refresh();

        initReceiver();
    }

    @Override
    protected void initNavigationBar()
    {
        getNavigationBar().hideLeftText();
        getNavigationBar().hideRightText();
        getNavigationBar().getRightImg().setBackgroundResource(
                R.drawable.person_msg_right_bt_bg);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (v == pListView)
        {
            if (event.getAction() == MotionEvent.ACTION_MOVE)
            {
                hideKeyBoard();
            }
        }
        return false;
    }

    @Override
    public void onClick(View v)
    {
        if (v == bt_send)
        {
            if (Utils.isNetworkAvailable(this))
            {
                if (Engine.getInstance().isNRRegisted)
                {
                    String str = ed_body.getText().toString();
                    if (TextUtils.isEmpty(str.trim()))
                    {
                        showShortToast(getResources().getString(
                                R.string.enterContent));
                        return;
                    }

                    if (str.length() > 1000)
                    {
                        showShortToast("短息长度不能超过1000字符");
                        return;
                    }
                    
                    if (SEND_SMSING_STATE == 1)
                    {
                        showShortToast(R.string.tv_send_smsing);
                    }
                    else
                    {
                        sendData();
                    }
                }
                else
                {
                    Engine.getInstance().showSysDialogAct(
                            this,
                            getResources().getString(R.string.tv_hint),
                            getResources().getString(
                                    R.string.tv_un_reg_noroamiing_body), "",
                            "", 0, "");
                }
            }
            else
            {
                showShortToast(R.string.no_net);
            }
        }
    }

    private void refresh()
    {
        if (messageSms != null)
        {
            SmsEntityDBHelper sMessageDBHelper = SmsEntityDBHelper.getInstance();
            messageSms.setRead(SmsEntity.STATUS_READ);
            sMessageDBHelper.update(messageSms);
            final List<SmsEntity> lMessageSms = sMessageDBHelper
                    .getMessagesSms(messageSms.getAddress(), msgCount);
            Collections.reverse(lMessageSms);

            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    setTitle(messageSms.getNickName());

                    adapter = new SmsChatListAdapter(
                            SmsChatListAct.this, lMessageSms);
                    pListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pListView.setSelection(adapter.lMessageSms.size() - 1);

                }
            });
        }
    }

    private void sendData()
    {
        String date = TimeUtil.getDateString(System.currentTimeMillis(),
                TimeUtil.dateFormat2);

        sendMessageSms = new SmsEntity();
        sendMessageSms.setAddress(messageSms.getAddress());
        sendMessageSms.setPerson(Engine.getInstance().getUserName());
        sendMessageSms.setBody(ed_body.getText().toString());
        sendMessageSms.setDir(SmsEntity.DIR_OUTGOING);
        sendMessageSms.setBodyType(SmsEntity.TYPE_TEXT);
        sendMessageSms.setStatus(SmsEntity.STATUS_PENDING);
        if (messageSms.getNickName() != null)
        {
            sendMessageSms.setNickName(messageSms.getNickName());
        }
        sendMessageSms.setDate(date);
        SmsEntityDBHelper mHelper = SmsEntityDBHelper.getInstance();
        long id = mHelper.addMessageSms(sendMessageSms);
        Engine.getInstance().sendSMS(this, id);

        ed_body.setText("");
        refresh();
    }

    PullDownListView.OnRefreshListener onRefreshListener = new OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
            // TODO Auto-generated method stub
            new AsyncTask<Void, Void, Void>()
            {
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    msgCount += 5;
                    refresh();
                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    adapter.notifyDataSetChanged();
                    pListView.onRefreshComplete();
                }

            }.execute();
        }
    };

    public void onRightClick()
    {
        ContactDBHelper dHelper = ContactDBHelper.getInstance();
        ContactModel contactModel = dHelper.getContactByNumber(
                messageSms.getAddress(), "value");

        if (contactModel != null)
        {
            startContactDetail(contactModel);
        }
        else
        {
            ContactModel contactModel2 = new ContactModel();
            List<TagNumber> tagNumbers = new ArrayList<TagNumber>();
            TagNumber tNumber = new TagNumber();
            tNumber.setValue(messageSms.getAddress());
            tagNumbers.add(tNumber);
            contactModel2.setPhoneNumList(tagNumbers);
            startContactDetail(contactModel2);
        }
    };

    private void startContactDetail(ContactModel cModel)
    {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("contact", cModel);
//        bundle.putInt("TYPE", 1);
//        openActivity(ContactDetailActivity.class, bundle,
//                MessageFragment.intentAction);
    }

    private void regReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IAction.REFRESH_UI);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(myReceiver, filter);
    }

    private void initReceiver()
    {
        myReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                refresh();
            }
        };
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        regReceiver();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (myReceiver != null)
        {
            unregisterReceiver(myReceiver);
        }
        SEND_SMSING_STATE = 0;
    }
}
