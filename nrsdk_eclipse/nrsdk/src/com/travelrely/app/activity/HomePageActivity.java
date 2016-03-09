package com.travelrely.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.igexin.sdk.PushManager;
import com.travelrely.app.activity.LoginActivity;
import com.travelrely.core.Constant;
import com.travelrely.core.ConstantValue;
import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.sdk.R;
import com.travelrely.app.activity.MyActivity;
import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.v2.db.SmsEntityDBHelper;
import com.travelrely.v2.db.TravelrelyMessageDBHelper;
import com.travelrely.v2.model.SmsEntity;
import com.travelrely.v2.response.TraMessage;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.SpUtil;
import com.travelrely.v2.util.SysUtil;

/**
  * @ClassName: HomePageActivity
  * @Description: app主页
  * @author Comsys-john
  * @date 2015-3-27 下午6:12:52
  *
  */
public class HomePageActivity extends NavigationActivity implements
        OnClickListener
{
    private Handler mHandler = new Handler();
    
    private ViewFlipper advFlipper;
    
    private LinearLayout btnContact;
    private LinearLayout btnMsg;
    private LinearLayout btnShop;
    private LinearLayout btnMy;

    // msg数量相关
    private BroadcastReceiver msgCountReceiver;
    
    ImageView msgCountImg;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        getMsgCount();
        initMsgCountReceiver();

//        Engine.getInstance().homePageAct = this;

        Engine.getInstance().startLoginService(this);

        setContentView(R.layout.home_page);

        initRootView();
        
        // startChkVersion();
        
        Engine.getInstance().getCountryInfoFromDB();
    }

    private void initRootView()
    {
        advFlipper = (ViewFlipper) findViewById(R.id.adv);
        advFlipper.setOnClickListener(this);
        
        msgCountImg = (ImageView) findViewById(R.id.count);
        
        btnContact = (LinearLayout) findViewById(R.id.btnContact);
        btnContact.setOnClickListener(this);
        btnMsg = (LinearLayout) findViewById(R.id.btnMsg);
        btnMsg.setOnClickListener(this);
        btnMy = (LinearLayout) findViewById(R.id.btnMy);
        btnMy.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        startAutoSwitchAdv();
        if (Engine.getInstance().isLogIn)
        {
            hideRight();
        	LogUtil.i("手机型号: ", android.os.Build.MANUFACTURER +" " + android.os.Build.MODEL);
    		LogUtil.i(" fingerprint:  ", android.os.Build.FINGERPRINT );
    		LogUtil.i(" connectGattFlag:  ", ConstantValue.connectGattFlag+"");
    		if (PushManager.getInstance() == null){
    			
    		}else{
    			String clientid = PushManager.getInstance().getClientid(getApplicationContext());
    			if (TextUtils.isEmpty(clientid)){
    			
    			}else{
    				
    				LogUtil.i("clientid	: ", clientid);
    			}
    		}
            if (SmsEntityDBHelper.getInstance().getUnReadCount() > 0)
            {
                msgCountImg.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            showRight();
            getNavigationBar().setRightText(R.string.login);
            getNavigationBar().hideRightImg();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stopAutoSwitchAdv();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }
    
    @Override
    protected void onRestart()
    {
        super.onRestart();
    }
    
    @Override
    public void onDestroy()
    {
        unregisterReceiver(msgCountReceiver);
        Constant.isMsgCount = false;
        Constant.isSmsCount = false;
        
        // kill App时停止NR相关服务
        Engine.getInstance().stopNR(getApplicationContext());
        super.onDestroy();
    }
    
    @Override
    public void onBackPressed()
    {
        if (Engine.getInstance().isLogIn)
        {
            moveTaskToBack(true);
        }
        else
        {
            Engine.getInstance().exit();
        }

        return;
    }

    @Override
    protected void initNavigationBar()
    {
        hideLeft();
        setTitle(R.string.home);
    }

    @Override
    public void onRightClick()
    {
        openActivity(LoginActivity.class);
    }
    
    
    
    /*点击 首页 标题上传log，不再前后台上传log
     * **/
    List<Long> times = new ArrayList<Long>();
    @Override
    public void onTitleClick() {
    	// TODO Auto-generated method stub
    	super.onTitleClick();
    	  times.add(SystemClock.uptimeMillis());
          if (times.size() == 3) {
              if (times.get(times.size()-1)-times.get(0) < 500) {//两次点击之间小于500ms
                  times.clear();
                  //上传log
                  Toast.makeText(this, "即将上传日志文件", Toast.LENGTH_SHORT).show();
                  Engine.getInstance().syncLog();
              } else {
                  times.remove(0);
              }
          }

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
		if (id == R.id.btnContact) {
			if (Engine.getInstance().isLogIn) {
			    openActivity(ContactActivity.class);
			}
			else {
			    openActivity(LoginActivity.class);
			}
		} else if (id == R.id.btnMsg) {
			if (Engine.getInstance().isLogIn) {
			    openActivity(MessageActivity.class);
			} else {
			    openActivity(LoginActivity.class);
			}
			Engine.getInstance().broadcast(IAction.SMS_CLEAR_NOTIFY);
		} else if (id == R.id.adv || id == R.id.btnMy) {
			if (Engine.getInstance().isLogIn) {
			    openActivity(MyActivity.class);
			} else {
			    openActivity(LoginActivity.class);
			}
		} else {
		}
    }
    
    private void startAutoSwitchAdv()
    {
        advFlipper.setInAnimation(this, R.anim.adv_fade_in);
        advFlipper.setOutAnimation(this, R.anim.adv_fade_out);
        advFlipper.setFlipInterval(5000);

        // 开始自动播放
//        advFlipper.startFlipping();
    }
    
    private void stopAutoSwitchAdv()
    {
        advFlipper.stopFlipping();
    }

    private void initMsgCountReceiver()
    {
        msgCountReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (IAction.MSG_COUNT_ACTION.equals(action))
                {
                    refreshMsgCount();
                }
                
                if (IAction.SMS_RECV.equals(action))
                {
                    msgCountImg.setVisibility(View.VISIBLE);
                }
                
                if (IAction.NR_OPEN_ALERT.equals(action))
                {
                    showAppAlert("提示", "旅信网络电话业务现在可以开启，请同时打开蓝牙盒子。",
                            "稍后开启", "立即开启", new OnSysAlertClickListener()
                            {
                                @Override
                                public void onRightClick(SysAlertDialog dialog)
                                {
                                    if (SysUtil.getSysSdkCode() < 18)
                                    {
                                        showShortToast("系统版本过低，无法使用旅信网络电话功能");
                                    }
                                    else
                                    {
                                        openActivity(NRAct.class);
                                    }
                                }
                                
                                @Override
                                public void onOkClick(SysAlertDialog dialog)
                                {
                                    // TODO Auto-generated method stub
                                }
                                
                                @Override
                                public void onLeftClick(SysAlertDialog dialog)
                                {
                                    // TODO Auto-generated method stub
                                }
                            });
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(IAction.MSG_COUNT_ACTION);
        filter.addAction(IAction.SMS_RECV);
        filter.addAction(IAction.NR_OPEN_ALERT);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(msgCountReceiver, filter);
    }

    private void refreshMsgCount()
    {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (Engine.getInstance().isLogIn)
                {
                    if (Constant.isMsgCount || Constant.isSmsCount)
                    {
                        msgCountImg.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        msgCountImg.setVisibility(View.GONE);
                    }
//                    iMsgCount = 0;
                }
                else
                {
                	Constant.isMsgCount = false;
                	Constant.isSmsCount = false;
                    msgCountImg.setVisibility(View.GONE);
                }
            }
        });
    }
    
    private void getMsgCount(){
        
        TravelrelyMessageDBHelper travelrelyMessageDBHelper = TravelrelyMessageDBHelper
                .getInstance();
        List<TraMessage> personMessages = travelrelyMessageDBHelper.getMessages(Engine.getInstance().getUserName(), 0);
        
        for (int i = 0; i < personMessages.size(); i++) {
            if(personMessages.get(i).getUnReadCount() != 0){
            	Constant.isMsgCount = true;
                break;
            }else{
            	Constant.isMsgCount = false;
            }
        }
        
        if(SpUtil.getNRService() == 1){
            SmsEntityDBHelper sDbHelper = SmsEntityDBHelper.getInstance();
            List<SmsEntity> tSms = sDbHelper.getMessagesLastSms();
            for (int i = 0; i < tSms.size(); i++) {
                if(tSms.get(i).getRead() != 0){
                	Constant.isSmsCount = true;
                    break;
                }else{
                	Constant.isSmsCount = false;
                }
            }
        }
    }
}
