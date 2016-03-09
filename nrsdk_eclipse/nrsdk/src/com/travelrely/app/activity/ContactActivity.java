package com.travelrely.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.travelrely.app.view.CustomProgressDialog;
import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.core.nrs.App;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.IntentMsg;
import com.travelrely.core.nrs.nr.util.ActivityCollector;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.LogUtil;
import com.travelrely.core.util.UIHelper;
import com.travelrely.sdk.R;

public class ContactActivity extends FragmentActivity
{
    private TabHost mTabHost;
    private TabWidget mTabWidget;

    private LinearLayout dialPadTab;

    private static final String TAB_CONTACTS = "contacts";
    private static final String TAB_CALL_LOG = "call_log";
    private static final String TAB_DIAL_PAD = "dial_pad";
   
    
    private int idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LOGManager.d("ContactActivity onCreate");
        super.onCreate(savedInstanceState);
        
        idx = getIntent().getIntExtra("FRAGMENT_IDX", 0);
        
        Engine.getInstance().addActivity(this);
        ActivityCollector.addActivity(this);

        setContentView(R.layout.contact_activity);

        initView();
        
        registerReceiver(mReceiver, makeFilter());
    }

    private void initView()
    {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);

        mTabHost.setup();

        dialPadTab = createTab(R.drawable.dial_pad, R.string.tabMe);
        mTabHost.addTab(createTabSpec(TAB_DIAL_PAD, dialPadTab,
                R.id.dialPadFragment));

        // 每个选项卡均分屏幕宽度
        int count = mTabWidget.getChildCount();
        int avgPx = UIHelper.getScreenWidth(this) / count;
        for (int i = 0; i < count; i++)
        {
            mTabWidget.getChildAt(i).getLayoutParams().width = avgPx;
        }

        // 设置默认初始页
        showTab(idx);

        // 设置tab的监听器
        mTabHost.setOnTabChangedListener(new OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tag)
            {
                if (tag.equalsIgnoreCase(TAB_CONTACTS))
                {
                    //showTab(0);
                    //contactFragment.onShow(true);
                }
                else if (tag.equalsIgnoreCase(TAB_CALL_LOG))
                {
                    //showTab(1);
                }
                else if (tag.equalsIgnoreCase(TAB_DIAL_PAD))
                {
                    //showTab(2);
                }
            }
        });
    }

    private LinearLayout createTab(int resId, int stringId)
    {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.tab_indicator, mTabWidget, false);
        ImageView ivImage = (ImageView) layout.findViewById(R.id.tabImage);
        ivImage.setBackgroundResource(resId);

        TextView tvText = (TextView) layout.findViewById(R.id.tabText);
        tvText.setText(stringId);
        tvText.setVisibility(View.GONE);
        return layout;
    }

    private TabHost.TabSpec createTabSpec(String tag, LinearLayout layout,
            int resId)
    {
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(layout);
        tabSpec.setContent(resId);
        return tabSpec;
    }

    private void showTab(int index)
    {
        mTabHost.setCurrentTab(index);
        switch (index)
        {
            case 0:
                //contactFragment.onShow(true);
                break;

            case 1:
                break;

            case 2:
                break;

            default:
                break;
        }

    }

    @Override
    protected void onStart()
    {
        LOGManager.d("ContactActivity onStart");
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        LOGManager.d("ContactActivity onResume");
        super.onResume();
    }
    
    @Override
    protected void onPause()
    {
        LOGManager.d("ContactActivity onPause");
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        LOGManager.d("ContactActivity onStop");
        super.onStop();
        Engine.getInstance().syncForegroundThread();
    }

    @Override
    protected void onRestart()
    {
        LOGManager.d("ContactActivity onRestart");
        super.onRestart();
        if (Engine.getInstance().isForeground == false)
        {
            Engine.getInstance().isForeground = true;
            Engine.getInstance().startLoginService(this);
        }
    }

    @Override
    protected void onDestroy()
    {
        if (mAlertDialog != null)
        {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        
        if (mWaitDialog != null)
        {
            mWaitDialog.dismiss();
            mWaitDialog = null;
        }
        
        unregisterReceiver(mReceiver);
        
        ActivityCollector.removeActivity(this); 
        
        LOGManager.d("ContactActivity onDestroy");
        super.onDestroy();
    }
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {            
            onBroadcastReceived(context, intent);
        }
    };
    
    protected void onBroadcastReceived(Context context, Intent intent)
    {
        if (!ActivityCollector.getEndActivity().equals(this))
        {
            return;
        }
        
        final String action = intent.getAction();
        LogUtil.i(this.getClass().getSimpleName(), "action = " + action);
        
        if (IAction.BLE_CONN_EXPIRE.equals(action))
        {
            alertWait(false, "");
            showAppAlert("提示",
                    "请检查蓝牙配件是否有电，SIM卡是否放好", "关闭");
        }
        
        if (IAction.NR_START_REG.equals(action))
        {
            alertWait(true, "正在注册...");
            //加保护，防止出现异常之后，一直是正在注册界面，影响用户体验
            App.getMainThreadHandler().postDelayed((new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					alertWait(false, "");
				}
			}),10000);
        }
        
        if (IAction.NR_REG_SUCC.equals(action))
        {
            alertWait(false, "");
            showAppAlert("",
               "旅信网络电话服务已经启动，祝您使用愉快！", "关闭");
        }
        
        if (IAction.NR_REG_FAIL.equals(action))
        {
            alertWait(false, "");
            showAppAlert("提示",
                "旅信网络电话服务启动失败，请再次尝试", "关闭");
        }
        
        if (IAction.NR_REG_EXPIRE.equals(action))
        {
            alertWait(false, "");
            showAppAlert("提示",
                "网络连接不稳定，请您检查网络连接后再次尝试", "关闭");
        }
        
        if (IAction.BOX_MISS_MATCH.equals(action))
        {
            alertWait(false, "");
//            showAppAlert("提示",
//                "蓝牙设备连接异常，请到旅信网络电话内重新扫描并配对蓝牙设备。", "关闭");
        }
        
        if (IAction.BOX_NO_SIM.equals(action))
        {
            alertWait(false, "");
            showAppAlert("提示",
                    "蓝牙配件中未插入sim卡，请插入sim卡后使用", "关闭");
        }
        
        if (IAction.BOX_SIM_INIT_FAIL.equals(action))
        {
            alertWait(false, "");
            showAppAlert("提示",
                    "请检查蓝牙配件是否有电，SIM卡是否放好", "关闭");
        }
        
        if (IAction.BOX_SIM_CHANGED.equals(action))
        {
            alertWait(false, "");
            showAppAlert("提示",
                "蓝牙配件中的sim卡更换，请到 我－旅信网络电话 重启业务", "关闭");
        }
        
        if (IAction.NOTIFY_POP_DLG.equals(action))//通知弹框
        {
            showAppAlert("提示",
                    intent.getStringExtra(IntentMsg.INTENT_STR_MSG), "确定");
        }
    }
    
    private static IntentFilter makeFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(IAction.BLE_CONN_EXPIRE);
        
        intentFilter.addAction(IAction.BOX_FOUND);
        intentFilter.addAction(IAction.BOX_NOT_FOUND);
        intentFilter.addAction(IAction.BOX_MATCH_SUCC);
        intentFilter.addAction(IAction.BOX_MATCH_FAIL);
        
        intentFilter.addAction(IAction.NR_CLOSED);
        intentFilter.addAction(IAction.NR_START_REG);
        intentFilter.addAction(IAction.NR_REG_SUCC);
        intentFilter.addAction(IAction.NR_REG_FAIL);
        intentFilter.addAction(IAction.NR_REG_EXPIRE);

        intentFilter.addAction(IAction.BOX_MISS_MATCH);
        intentFilter.addAction(IAction.NOTIFY_POP_DLG);
        
        intentFilter.addAction(IAction.BOX_NO_SIM);
        intentFilter.addAction(IAction.BOX_SIM_INIT_FAIL);
        intentFilter.addAction(IAction.BOX_SIM_AUTH_FAIL);
        intentFilter.addAction(IAction.BOX_SIM_SYNC_FAIL);
        intentFilter.addAction(IAction.BOX_SIM_CHANGED);
        
        intentFilter.addAction(IAction.BOX_CIPHER_KEY_SAVED);
        intentFilter.addAction(IAction.USER_VERIFY_SUCC);
        intentFilter.addAction(IAction.USER_VERIFY_FAIL);
        
        return intentFilter;
    }
    
    private SysAlertDialog mAlertDialog;
    private CustomProgressDialog mWaitDialog = null;
    
    protected void showAppAlert(String title, String des, String ok)
    {
        // 提醒之后就不再提醒
        if (mAlertDialog == null)
        {
            mAlertDialog = new SysAlertDialog(this);
        }
        
        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(des);
        mAlertDialog.setOk(ok);
        mAlertDialog.setOnClickListener(null);
        mAlertDialog.show();
    }
    
    protected void showAppAlert(boolean cancel, String title, String des, String ok, OnSysAlertClickListener l)
    {
        // 提醒之后就不再提醒
        if (mAlertDialog == null)
        {
            mAlertDialog = new SysAlertDialog(this);
        }
        
        mAlertDialog.setCancelable(cancel);
        mAlertDialog.setCanceledOnTouchOutside(cancel);
        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(des);
        mAlertDialog.setOk(ok);
        mAlertDialog.setOnClickListener(l);
        mAlertDialog.show();
    }
    
    protected void showAppAlert(String title, String des, String le, String ri, OnSysAlertClickListener l)
    {
        // 提醒之后就不再提醒
        if (mAlertDialog == null)
        {
            mAlertDialog = new SysAlertDialog(this);
        }
        
        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(des);
        mAlertDialog.setLeft(le);
        mAlertDialog.setRight(ri);
        mAlertDialog.setOnClickListener(l);
        mAlertDialog.show();
    }
    
    protected void alertWait(boolean show, String msg)
    {
        // 提醒之后就不再提醒
        if (mWaitDialog == null)
        {
            mWaitDialog = CustomProgressDialog.createDialog(this);
            mWaitDialog.setCancelable(false);
        }
        
        if (show)
        {
            mWaitDialog.setMessage(msg);
            mWaitDialog.show();
        }
        else
        {
            mWaitDialog.dismiss();
        }
    }
}
