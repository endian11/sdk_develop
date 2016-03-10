package com.travelrely.app.activity;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.nrs.nr.msg.MsgId;
import com.travelrely.core.nrs.nr.util.ActivityCollector;
import com.travelrely.core.util.LOGManager;

public class BaseActivity extends Activity
{
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LOGManager.i(this.getClass().getSimpleName() + " onCreate()");

        Engine.getInstance().addActivity(this);
        ActivityCollector.addActivity(this);  
        // 隐藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        handler = new Handler();

        StatService.setDebugOn(ReleaseConfig.IS_DEBUG_VERSION);
    }

    @Override
    protected void onStart()
    {
        LOGManager.i(this.getClass().getSimpleName() + " onStart()");
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        LOGManager.i(this.getClass().getSimpleName() + " onResume()");
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause()
    {
        LOGManager.i(this.getClass().getSimpleName() + " onPause()");
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onStop()
    {
        LOGManager.i(this.getClass().getSimpleName() + " onStop()");
        super.onStop();

        Engine.getInstance().syncForegroundThread();
    }

    @Override
    protected void onRestart()
    {
        LOGManager.i(this.getClass().getSimpleName() + " onRestart()");
        super.onRestart();
        
        // background to foreground
        if (Engine.getInstance().isForeground == false)
        {
            Engine.getInstance().isForeground = true;
            Engine.getInstance().startLoginService(this);
            
            Engine.getInstance().startNRService(this, MsgId.BLE_NR_KEEPALIVE_IND);
        }
    }

    @Override
    protected void onDestroy()
    {
        LOGManager.i(this.getClass().getSimpleName() + " onDestroy()");
        super.onDestroy();
        ActivityCollector.removeActivity(this);  
    }

    public void showLongToast(final String pMsg)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(BaseActivity.this, pMsg, Toast.LENGTH_LONG)
                .show();
            }
        });
    }

    public void showLongToast(final int pMsgId)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(BaseActivity.this, pMsgId, Toast.LENGTH_LONG)
                .show();
            }
        });
    }

    public void showShortToast(final String pMsg)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(BaseActivity.this, pMsg, Toast.LENGTH_SHORT)
                .show();
            }
        });
    }

    public void showShortToast(final int pMsg)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(BaseActivity.this, pMsg, Toast.LENGTH_SHORT)
                .show();
            }
        });
    }

    protected void openActivity(Class<?> pClass)
    {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle)
    {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null)
        {
            intent.putExtras(pBundle);
        }

        startActivity(intent);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle, int flags)
    {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null)
        {
            intent.putExtras(pBundle);
        }
        intent.setFlags(flags);
        startActivity(intent);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle, String action)
    {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null)
        {
            intent.putExtras(pBundle);
        }
        intent.setAction(action);
        startActivity(intent);
    }

    protected void openActivity(Class<?> pClass, int flags)
    {
        Intent intent = new Intent(this, pClass);
        intent.setFlags(flags);
        startActivity(intent);
    }

    protected void openActivity(String pAction)
    {
        openActivity(pAction, null);
    }

    protected void openActivity(String pAction, Bundle pBundle)
    {
        Intent intent = new Intent(pAction);
        if (pBundle != null)
        {
            intent.putExtras(pBundle);
        }

        startActivity(intent);
    }
    
    protected void openBroadcast(String pAction, Bundle pBundle)
    {
        Intent intent = new Intent(pAction);
        if (pBundle != null)
        {
            intent.putExtras(pBundle);
        }
        intent.setAction(pAction);
        sendBroadcast(intent);
    }

    public void hideKeyBoard()
    {
        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null
                && this.getCurrentFocus().getWindowToken() != null)
        {
            im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void startActivityIfLogin(Intent intent)
    {
        if (Engine.getInstance().isLogIn)
        {
            startActivity(intent);
        } else {
            Intent intent2 = new Intent(this, LoginActivity.class);
            startActivity(intent2);
        }
    }

    @Override
    public void finish()
    {
        // TODO Auto-generated method stub
        super.finish();
        hideKeyBoard();
    }

    public static Dialog showDialog(Context ctx, int iconId, String title,
            String message, String okName, OnClickListener okListener,
            String noName, OnClickListener noListener)
    {
        Dialog dialog = null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                ctx);
        builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okName, okListener);
        builder.setNegativeButton(noName, noListener);
        dialog = builder.create();
        return dialog;
    }
    
    // 暂时没有使用
    public boolean isAppOnForeground()
    {  
        // Returns a list of application processes that are running on the  
        // device  
           
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);  
        String packageName = getApplicationContext().getPackageName();  

        List<RunningAppProcessInfo> appProcesses = activityManager  
                        .getRunningAppProcesses();
        if (appProcesses == null)
        {
            return false;
        }

        for (RunningAppProcessInfo appProcess : appProcesses)
        {  
            // The name of the process that this object is associated with.  
            if (appProcess.processName.equals(packageName)  
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            {  
                return true;
            }  
        }  

        return false;  
    }
}
