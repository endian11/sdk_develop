package com.travelrely.app.activity;

import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.igexin.sdk.PushManager;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.SpUtil;
import com.travelrely.sdk.R;

public class WelcomeAct extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Resources res;
        Configuration conf;
        DisplayMetrics dm;

        super.onCreate(savedInstanceState);

        int iLangType = SpUtil.getLangType();
        PushManager.getInstance().initialize(this.getApplicationContext());//该方法必须在Activity或Service类内调用
        
        //强制设置为简体中文
//        switch (iLangType)
        switch (1)
        {
            case 1:
                res = getResources();
                conf = res.getConfiguration();
                conf.locale = Locale.SIMPLIFIED_CHINESE;
                dm = res.getDisplayMetrics();
                res.updateConfiguration(conf, dm);
                break;

            case 2:
                res = getResources();
                conf = res.getConfiguration();
                conf.locale = Locale.TAIWAN;
                dm = res.getDisplayMetrics();
                res.updateConfiguration(conf, dm);
                break;

            case 3:
                res = getResources();
                conf = res.getConfiguration();
                conf.locale = Locale.US;
                dm = res.getDisplayMetrics();
                res.updateConfiguration(conf, dm);
                break;

            default:
                break;
        }

        setContentView(R.layout.welcome_activity);

        // 获取屏幕大小
        Engine.getInstance().getDisplayScreenResolution(WelcomeAct.this);

        // 判断归属地还是漫游地,并设置homeLogin全局变量
        Engine.getInstance().judgeHomeOrRoam();
        
        //Engine.getInstance().syncCountryInfoThread();
        //Engine.getInstance().syncPkgThread();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Engine.getInstance().getCountryInfoRequest(WelcomeAct.this);
                Engine.getInstance().getPkgRequest(WelcomeAct.this);
            }
        }).start();

        // 启动地图定位服务
        // Intent intent = new Intent(WelcomeActivity.this,
        // LocationGaoDe.class);
        // startService(intent);

        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 1000);
    }

    class splashhandler implements Runnable
    {
        public void run()
        {
            dispatchIsLogin();
        }
    }

    private void dispatchIsLogin()
    {
    	LOGManager.d("启动主页");
        openActivity(HomePageActivity.class);
        finish();
    }
}