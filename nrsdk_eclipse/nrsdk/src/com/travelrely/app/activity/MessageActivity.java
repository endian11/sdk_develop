package com.travelrely.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.travelrely.app.adapter.SmsGroupListAdapter;
import com.travelrely.app.adapter.SystemMessageAdapter;
import com.travelrely.app.adapter.NavigationBaseAdapter.onRightItemClickListener;
import com.travelrely.app.view.DialogManager;
import com.travelrely.app.view.NavigationBar;
import com.travelrely.app.view.SwipeListView;
import com.travelrely.app.view.NavigationBar.OnNavigationBarClick;
import com.travelrely.core.Constant;
import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.model.ContactModel;
import com.travelrely.sdk.R;
import com.travelrely.v2.GetMsg.FetchMessage;
import com.travelrely.v2.db.ContactDBHelper;
import com.travelrely.v2.db.SmsEntityDBHelper;
import com.travelrely.v2.db.TravelrelyMessageDBHelper;
import com.travelrely.v2.model.SmsEntity;
import com.travelrely.v2.response.TraMessage;
import com.travelrely.v2.util.AESUtils;
import com.travelrely.v2.util.FileUtil;
import com.travelrely.v2.util.SpUtil;
import com.travelrely.v2.util.Utils;

public class MessageActivity extends NavigationActivity implements
        OnItemClickListener, OnNavigationBarClick, OnClickListener,
        OnItemLongClickListener, onRightItemClickListener,
        OnCheckedChangeListener
{
    public static final String intentAction = "com.travelrely.v2.fragment.MessageFragment";
    private View layout;
    private SwipeListView travel_msg_list;
    SystemMessageAdapter sAdapter;
    public static List<TraMessage> personMessages;
    private NavigationBar navigationBar;
    Context mContext;
    static String nickName;
    View alarmLayout;
    View alarmView;
    TextView alarmName;
    TextView alarmTime;
    TraMessage alarmMsg;
    List<TraMessage> alarmMessages;

    List<TraMessage> unReadMsgCount;
    String from_msg;

    List<TraMessage> listMsgTop = new ArrayList<TraMessage>();
    List<TraMessage> listMsgTopUn = new ArrayList<TraMessage>();
    List<TraMessage> allList = new ArrayList<TraMessage>();

    SwipeListView travel_msg_list_sms;
    RadioButton leftRadio;
    RadioButton rightRadio;

    SmsGroupListAdapter mSmsAdapter;
    List<SmsEntity> lMessageSms;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = this;

        layout = LayoutInflater.from(this).inflate(R.layout.layout_message,
                null);

        setContentView(layout);

        navigationBar = (NavigationBar) layout
                .findViewById(R.id.navigation_bar);
        navigationBar.setOnNavigationBarClick(this);
        travel_msg_list = (SwipeListView) layout
                .findViewById(R.id.travel_msg_list);
        travel_msg_list.setOnItemClickListener(this);
        travel_msg_list.setVisibility(View.VISIBLE);

        init();
    }

    @SuppressLint("CutPasteId")
    private void init() {
    	travel_msg_list_sms = (SwipeListView) findViewById(R.id.travel_msg_list_sms);
    	leftRadio = (RadioButton) findViewById(R.id.left_radio);
    	rightRadio = (RadioButton) findViewById(R.id.right_radio);
    	
        leftRadio.setText(R.string.travelrely);
        rightRadio.setText(R.string.tv_sms);
        leftRadio.setOnCheckedChangeListener(this);
        rightRadio.setOnCheckedChangeListener(this);

        alarmLayout = this.getLayoutInflater().inflate(
                R.layout.alarm_item_listview, null);
        setAlarm();
        alarmLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                // openActivity(SetAlarmActivity.class);
            }
        });

        personMessages = new ArrayList<TraMessage>();
        sAdapter = new SystemMessageAdapter(this, personMessages,
                travel_msg_list.getRightViewWidth());

        travel_msg_list.addHeaderView(alarmLayout);
        travel_msg_list.setAdapter(sAdapter);
        travel_msg_list.setOnItemClickListener(this);
        sAdapter.setOnRightItemClickListener(this);
        // travel_msg_list.setOnItemLongClickListener(this);

        lMessageSms = new ArrayList<SmsEntity>();
        mSmsAdapter = new SmsGroupListAdapter(this, lMessageSms,
                travel_msg_list_sms.getRightViewWidth());
        travel_msg_list_sms.setAdapter(mSmsAdapter);
        travel_msg_list_sms.setOnItemClickListener(this);
        mSmsAdapter.setOnRightItemClickListener(this);

        initReceiver();

        if (SpUtil.getNRService() != 0)
        {
            refreshSms();
        }
    }

    @Override
    protected void initNavigationBar()
    {
        super.initNavigationBar();
        setTitle(R.string.tabMsg);
        getNavigationBar().hideLeftText();
        getNavigationBar().getRightImg().setImageResource(
                R.drawable.icon_add_contact);
        getNavigationBar().getLeftImg().setImageResource(
                R.drawable.home_icon_bg);

        if (SpUtil.getNRService() != 0)
        {
            getNavigationBar().hideTitle();
            getNavigationBar().showRadioGroup();
        }
    }

    private void setAlarm()
    {
        alarmView = (View) alarmLayout.findViewById(R.id.view_alarm);
        alarmView.setVisibility(View.GONE);
        alarmName = (TextView) alarmLayout.findViewById(R.id.alarm_name);
        alarmTime = (TextView) alarmLayout.findViewById(R.id.alarm_time);
    }

    BroadcastReceiver myReceiver;

    private void initReceiver()
    {
        myReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (IAction.SMS_RECV.equals(action))
                {
                    refreshSms();
                    
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Engine.getInstance().broadcast(
                                    IAction.SMS_CLEAR_NOTIFY);
                        }
                    }, 1000);
                    
                }
                
                Bundle bundle = intent.getExtras();
                int type = 0;
                if (bundle != null)
                {
                    from_msg = bundle.getString("from");
                    type = bundle.getInt("type");
                }

                if (type == 2)
                {
                    refreshSms();
                }
                else
                {
                    refresh();
                }

            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(IAction.MSM_ACTION);
        filter.addAction(IAction.SMS_RECV);
        filter.setPriority(Integer.MAX_VALUE);
        this.registerReceiver(myReceiver, filter);
    }

    private void refresh()
    {

        personMessages.clear();
        Constant.isMsgCount = false;
        TravelrelyMessageDBHelper travelrelyMessageDBHelper = TravelrelyMessageDBHelper
                .getInstance();

        String froms = Engine.getInstance().getUserName();

        alarmMessages = travelrelyMessageDBHelper.getMessages(froms, 3);

        personMessages = travelrelyMessageDBHelper.getMessages(froms, 0);

        listMsgTop.clear();
        listMsgTopUn.clear();
        allList.clear();
        for (int i = 0; i < personMessages.size(); i++)
        {

            if (personMessages.get(i).getUnReadCount() != 0)
            {
            	Constant.isMsgCount = true;
            }
            else
            {
            	Constant.isMsgCount = false;
            }

            if (personMessages.get(i).isGroup())
            {
                if (Engine.getInstance().getIsMsgTopType(
                        personMessages.get(i).getGroup_id()) == 1)
                {
                    listMsgTop.add(personMessages.get(i));
                }
            }
            else
            {
                if (Engine.getInstance().getIsMsgTopType(
                        personMessages.get(i).getFrom()) == 1)
                {
                    listMsgTop.add(personMessages.get(i));
                }
            }

        }

        for (int i = 0; i < personMessages.size(); i++)
        {
            if (personMessages.get(i).isGroup())
            {
                if (Engine.getInstance().getIsMsgTopType(
                        personMessages.get(i).getGroup_id()) == 0)
                {
                    listMsgTopUn.add(personMessages.get(i));
                }
            }
            else
            {
                if (Engine.getInstance().getIsMsgTopType(
                        personMessages.get(i).getFrom()) == 0)
                {
                    listMsgTopUn.add(personMessages.get(i));
                }
            }
        }

        Collections.sort(listMsgTop);
        Collections.sort(listMsgTopUn);
        allList.addAll(listMsgTop);
        allList.addAll(listMsgTopUn);

        personMessages = allList;

        mainTabMsgCount();

        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                refreshRadio(leftRadio, personMessages, null);

                if (alarmMessages.size() > 0)
                {
                    alarmView.setVisibility(View.VISIBLE);
                    String[] alarmStr = null;
                    Collections.sort(alarmMessages);
                    alarmMsg = alarmMessages.get(0);
                    if (alarmMsg.getType() == 3)
                    {
                        if (alarmMsg.getContent() != null)
                        {
                            alarmStr = AESUtils.getDecryptString(
                                    alarmMsg.getContent()).split("\t");
                        }
                        alarmName.setText(alarmStr[1]);
                        alarmTime.setText(alarmStr[2]);
                    }
                }
                else
                {
                    alarmView.setVisibility(View.GONE);
                }

                sAdapter.list = personMessages;
                System.out.println("personMessages: " + personMessages);
                System.out.println("sAdapter.list in MessageFragment : -------------" + sAdapter.list);
                sAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (travel_msg_list.isShown())
        {
//            TravelService.msg_count = 0;
            FetchMessage.msg_count = 0;
            // Bundle extras = generateBunlde(uMessage);
            Bundle bundle = new Bundle();
            bundle.putInt("position", position - 1);
            bundle.putInt("message_type", 1);
            startActivityIfLogin(bundle);
        }
        else if (travel_msg_list_sms.isShown())
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("MESSAGE_SMS", lMessageSms.get(position));
            openActivity(SmsChatListAct.class, bundle);
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (leftRadio.isChecked())
        {
            refresh();
        }
        else
        {
            refreshSms();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (myReceiver != null)
            this.unregisterReceiver(myReceiver);
    }

    public void startActivityIfLogin(Bundle bundle)
    {
        if (Engine.getInstance().isLogIn)
        {
            openActivity(ChatMsgListAct.class, bundle);
        }
        else
        {
            openActivity(LoginActivity.class);
        }
    }

    @Override
    public void onRightClick()
    {

       
    }

    /**
     * 选多组联系人
     */
    public void selectContacts(int type)
    {
//        Intent intent = new Intent(this, SelectContactsToSendActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", type);
//        intent.putExtras(bundle);
//        startActivity(intent);
    	// TODO cwj
    }

    
    
    /**
     * 获得未读消息条数
     */
    public static int unReadCount(TraMessage message)
    {

        return message.getUnReadCount();
    }

    /**
     * 生成用户名
     * 
     * @param message
     * @return
     */
    public static String generateWidthName(TraMessage message)
    {
        String from = message.getWidth_user();
        ContactDBHelper contactDatabaseHelper = ContactDBHelper.getInstance();
        ContactModel contactModel = contactDatabaseHelper
                .getContactByNumberTry(message.getWidth_user());
        if (contactModel != null)
        {
            message.contactModel = contactModel;
            if (message.getFrom_type() == 3)
            {
                return message.getGroup_name();
            }
            return contactModel.getName();
        }
        else
        {

        }
        return from;
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent;
        int id = v.getId();
		if (id == R.id.bt_login) {
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else {
		}
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            final int position, long id)
    {
        // TODO Auto-generated method stub
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (position > 0)
                {
                    if (which == DialogInterface.BUTTON_POSITIVE)
                    {
                        TraMessage message = sAdapter.list.get(position - 1);
                        String from = message.getFrom();

                        boolean flag = TravelrelyMessageDBHelper.getInstance()
                                .deleteMessageList(from);

                        if (message.getUrl() != null)
                        {
                            FileUtil fileUtil = new FileUtil(
                                    MessageActivity.this);
                            fileUtil.deleteFile(message.getUrl());
                        }
                        if (flag)
                        {
                            Utils.showToast(MessageActivity.this,
                                    getResources().getString(R.string.delSucc));
                            refresh();
                        }
                    }
                    else if (which == DialogInterface.BUTTON_NEGATIVE)
                    {

                    }
                }
            }
        };
        DialogManager.createMessageDialog2(onClickListener, this, "确认删除此消息?")
                .show();
        return false;
    }

    private void mainTabMsgCount()
    {

        Intent intent = new Intent();
        intent.setAction(IAction.MSG_COUNT_ACTION);
        this.sendBroadcast(intent);
    }

    @Override
    public void onRightItemClick(View v, int position)
    {
        // TODO Auto-generated method stub
        if (leftRadio.isChecked())
        {
            showDialogDel(this, personMessages.get(position), null, position);
        }
        else if (rightRadio.isChecked())
        {
            showDialogDel(this, null, lMessageSms.get(position), position);
        }
    }

    private void showDialogDel(final Context mContext,
            final TraMessage message, final SmsEntity tMessageSms,
            final int position)
    {

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {

                    boolean flag = false;
                    if (leftRadio.isChecked())
                    {
                        flag = TravelrelyMessageDBHelper.getInstance()
                                .deleteMessageList(message.getFrom());
                        if (message.getUrl() != null)
                        {
                            FileUtil fileUtil = new FileUtil(mContext);
                            fileUtil.deleteFile(message.getUrl());
                        }
                        travel_msg_list.deleteItem(travel_msg_list
                                .getChildAt(position + 1));
                        refresh();
                    }
                    else if (rightRadio.isChecked())
                    {
                        flag = SmsEntityDBHelper.getInstance()
                                .deleteMessageSms("address",
                                        tMessageSms.getAddress());
                        travel_msg_list_sms.deleteItem(travel_msg_list_sms
                                .getChildAt(position));
                        refreshSms();
                    }

                    if (flag)
                    {
                        Toast.makeText(
                                mContext,
                                mContext.getResources().getString(
                                        R.string.delSucc), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                else if (which == DialogInterface.BUTTON_NEGATIVE)
                {

                }
            }
        };

        DialogManager.createMessageDialog2(onClickListener, mContext,
                mContext.getResources().getString(R.string.tv_del_msg_is))
                .show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        // TODO Auto-generated method stub
        if (isChecked)
        {
            if (buttonView == rightRadio)
            {
                travel_msg_list.setVisibility(View.GONE);
                travel_msg_list_sms.setVisibility(View.VISIBLE);
            }
            else if (buttonView == leftRadio)
            {
                travel_msg_list.setVisibility(View.VISIBLE);
                travel_msg_list_sms.setVisibility(View.GONE);
            }
        }
    }

    private void refreshSms()
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                lMessageSms.clear();
                Constant.isSmsCount = false;
                SmsEntityDBHelper sHelper = SmsEntityDBHelper.getInstance();
                lMessageSms = sHelper.getMessagesLastSms();
                refreshRadio(rightRadio, null, lMessageSms);
                mSmsAdapter.lSms = lMessageSms;
                mSmsAdapter.notifyDataSetChanged();

                if (Engine.getInstance().isNRRegisted)
                {
                    setGuideResId(this.getClass().getName(),
                            R.id.layout_message_frament,
                            R.drawable.sms_help_480);
                }
            }
        });
    }

    private void refreshRadio(RadioButton radio, List<TraMessage> message,
            List<SmsEntity> tSms)
    {

        Drawable drawableCount = this.getResources().getDrawable(
                R.drawable.msg_count_small);
        drawableCount.setBounds(-45, -10,
                -45 + drawableCount.getMinimumWidth(),
                -10 + drawableCount.getMinimumHeight());

        if (message != null)
        {
            for (int i = 0; i < personMessages.size(); i++)
            {
                if (personMessages.get(i).getUnReadCount() != 0)
                {
                    radio.setCompoundDrawables(null, null, drawableCount, null);
                    break;
                }
                else
                {
                    radio.setCompoundDrawables(null, null, null, null);
                }
            }
        }
        if (tSms != null)
        {
            for (int i = 0; i < lMessageSms.size(); i++)
            {
              int smsCount= SmsEntityDBHelper.getInstance().getUnReadCount(lMessageSms.get(i).getAddress());
            	
                if (lMessageSms.get(i).getRead() != SmsEntity.STATUS_READ)
                {
                	if (smsCount == 0){
                		
                	}else{
                		
                		radio.setCompoundDrawables(null, null, drawableCount, null);
                		 break;
                	}
                   
                }
                else
                {
                    radio.setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }

}
