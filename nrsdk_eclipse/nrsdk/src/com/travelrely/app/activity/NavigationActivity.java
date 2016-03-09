
package com.travelrely.app.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.travelrely.app.view.CustomProgressDialog;
import com.travelrely.app.view.NavigationBar;
import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysAlertDialogChk;
import com.travelrely.app.view.SysAlertDialogRsp;
import com.travelrely.app.view.NavigationBar.OnNavigationBarClick;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.app.view.SysAlertDialogChk.OnSysAlertChkClickListener;
import com.travelrely.app.view.SysAlertDialogRsp.OnSysAlertRspClickListener;
import com.travelrely.core.App;
import com.travelrely.core.IAction;
import com.travelrely.core.IntentMsg;
import com.travelrely.core.nr.util.ActivityCollector;
import com.travelrely.sdk.R;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.PreferencesUtil;
import com.travelrely.v2.util.Utils;

public class NavigationActivity extends BaseActivity implements
        OnNavigationBarClick
{

    private int guideResourceId = 0;
    private int layout_id;
    private String className;
    protected NavigationBar navigationBar;
    
    private SysAlertDialog mAlertDialog;
    private SysAlertDialogChk mAlertDialogchk;
    private SysAlertDialogRsp mAlertDialogrsp;
    private CustomProgressDialog mWaitDialog = null;

    protected void initNavigationBar()
    {

    }

    public NavigationBar getNavigationBar()
    {
        return navigationBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerReceiver(mReceiver, makeFilter());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        navigationBar = (NavigationBar) findViewById(R.id.navigation_bar);
        navigationBar.setOnNavigationBarClick(this);
        navigationBar.setLeftText(R.string.back);
        navigationBar.hideLeftText();
        initNavigationBar();
    }

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
        navigationBar = (NavigationBar) findViewById(R.id.navigation_bar);
        navigationBar.setOnNavigationBarClick(this);
        navigationBar.setLeftText(R.string.back);
        navigationBar.hideLeftText();
        initNavigationBar();
    }

    public void hideRight()
    {
        navigationBar.hideRight();
    }
    
    public void showRight()
    {
        navigationBar.showRight();
    }

    public void setTitle(int resId)
    {
        navigationBar.setTitleText(resId);
    }

    public void setTitle(String str)
    {
        navigationBar.setTitleText(str);
    }

    public void hideLeft()
    {
        navigationBar.hideLeft();
    }

    public void setTypeBackground(int imgId)
    {
        navigationBar.setTypeBackground(imgId);
    }

    public void setLeftText(String str)
    {
        navigationBar.setLeftText(str);
    }

    public void setLeftText(int id)
    {
        navigationBar.setLeftText(id);
    }

    public void setRightText(String str)
    {
        navigationBar.setRightText(str);
    }

    public void setRightText(int id)
    {
        navigationBar.setRightText(id);
    }

    @Override
    public void onLeftClick()
    {
        hideKeyBoard();

        onBackPressed();
    }

    @Override
    public void onTitleClick()
    {

    }

    @Override
    public void onRightClick()
    {

    }

    @Override
    protected void onDestroy()
    {
        if (mAlertDialog != null)
        {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        if (mAlertDialogchk != null)
        {
            mAlertDialogchk.dismiss();
            mAlertDialogchk = null;
        }
        
        if (mWaitDialog != null)
        {
            mWaitDialog.dismiss();
            mWaitDialog = null;
        }
        
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //addGuideImage();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void addGuideImage() {
        View view = getWindow().getDecorView().findViewById(layout_id);// 查找通过setContentView上的根布局
        if (view == null)
            return;
        if (activityIsGuided()) {
            // 引导过了
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {
            final FrameLayout frameLayout = (FrameLayout) viewParent;
            if (guideResourceId != 0) {
                final ImageView guideImage = new ImageView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                guideImage.setLayoutParams(params);
                guideImage.setScaleType(ScaleType.FIT_XY);
                guideImage.setImageResource(guideResourceId);
                guideImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frameLayout.removeView(guideImage);
                        setIsGuided(true);
                    }
                });
                frameLayout.addView(guideImage);// 添加引导图片
            }
        }
    }

    /**
     * @param context
     * @return false：没有引导 true：引导过
     */
    public boolean activityIsGuided() {
        if (className == null || "".equalsIgnoreCase(className))
            return false;

        if (PreferencesUtil.getSharedPreferencesBoolean(this, PreferencesUtil.PUBLIC_PRERENCES,
                className)) {
            return true;
        }
        return false;
    }

    public void setIsGuided(boolean isShow) {
        if (className == null || "".equalsIgnoreCase(className))
            return;

        PreferencesUtil.setSharedPreferences(this, PreferencesUtil.PUBLIC_PRERENCES, className,
                isShow);
    }

    protected void setGuideResId(String calss, int layoutId, int resId) {
        this.className = calss + Utils.getVersion(this);
        this.layout_id = layoutId;
        this.guideResourceId = resId;
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
        if (IAction.BOX_MATCH_FAIL.equals(action)){//配对失败
        	alertWait(false, "");
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
        
        intentFilter.addAction(IAction.NR_CLOSE_SUCC);
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
        
        intentFilter.addAction(IAction.OP_VSIM);
        
        return intentFilter;
    }
    
    protected void showAppAlert(String title, String des, String ok)
    {
        // 提醒之后就不再提醒
        if (mAlertDialog == null)
        {
            mAlertDialog = new SysAlertDialog(this);
        }
        
        mAlertDialog.setCancelable(false);
        mAlertDialog.setCanceledOnTouchOutside(false);
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
    
    protected void showAppAlert(String title, String des, String le, String ri, OnSysAlertRspClickListener l)
    {
        // 提醒之后就不再提醒
        if (mAlertDialogrsp == null)
        {
        	mAlertDialogrsp = new SysAlertDialogRsp(this);
        }
        
        mAlertDialogrsp.setCancelable(false);
        mAlertDialogrsp.setCanceledOnTouchOutside(false);
        mAlertDialogrsp.setTitle(title);
        mAlertDialogrsp.setMessage(des);
        mAlertDialogrsp.setLeft(le);
        mAlertDialogrsp.setRight(ri);
        mAlertDialogrsp.setOnClickListener(l);
        mAlertDialogrsp.show();
    }
    protected void showAppAlert(String title, String des, String le, String ri, OnSysAlertClickListener l)
    {
    	// 提醒之后就不再提醒
    	if (mAlertDialog == null)
    	{
    		mAlertDialog = new SysAlertDialog(this);
    	}
    	
    	mAlertDialog.setCancelable(false);
    	mAlertDialog.setCanceledOnTouchOutside(false);
    	mAlertDialog.setTitle(title);
    	mAlertDialog.setMessage(des);
    	mAlertDialog.setLeft(le);
    	mAlertDialog.setRight(ri);
    	mAlertDialog.setOnClickListener(l);
    	mAlertDialog.show();
    }
    
    //第一次安装 登陆 漫游资费提示框
    protected void showAppAlertchk(final String title, final String des, final String ok,  final OnSysAlertChkClickListener l)
    {
    	
    	
    	// 提醒之后就不再提醒
    	if (mAlertDialogchk == null )
    	{
    	
			mAlertDialogchk = new SysAlertDialogChk(NavigationActivity.this);
				
    	}
    	mAlertDialogchk.setCancelable(false);
    	mAlertDialogchk.setCanceledOnTouchOutside(false);
    	mAlertDialogchk.setTitle(title);
    	mAlertDialogchk.setMessage(des);
    	mAlertDialogchk.setOk(ok);
    	mAlertDialogchk.setOnClickListener(l);
    	mAlertDialogchk.show();
    	
    	
    }
    
    public void alertWait(final boolean show, final String msg)
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
        }else{
        	mWaitDialog.dismiss();
        }
        
    }
}
