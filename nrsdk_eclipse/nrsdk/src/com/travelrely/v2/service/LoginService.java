package com.travelrely.v2.service;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.UrlUtil;
import com.travelrely.v2.db.ServerIpDbHelper;
import com.travelrely.v2.model.ServerIp;
import com.travelrely.v2.net_interface.GetCommStatusRsp;
import com.travelrely.v2.net_interface.LoginRsp;

public class LoginService extends Service
{
    Context mContext = this;
    String strPostData;
    String strUrl;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        if (intent == null)
        {
            LOGManager.e(getClass().getName());
            return;
        }
        
        new TravelThread().start();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public class TravelThread extends Thread
    {
        @Override
        public void run()
        {
            LOGManager.d(getClass().getName()+":启动LoginService服务");
            String url = "";
            
            List<ServerIp> ipList = ServerIpDbHelper.getInstance().query();
            if (ipList == null || ipList.size() == 0)
            {
                LOGManager.v(getClass().getName()+":启用默认地址列表登录");
                url = ReleaseConfig.getUrl(Engine.getInstance().getCC());
            }
            else
            {
                LOGManager.v(getClass().getName()+":启用更新地址列表登录");
                String ip = ipList.get(0).getIp();
                if (TextUtils.isEmpty(ip))
                {
                    ip = ipList.get(1).getIp();
                    if (TextUtils.isEmpty(ip))
                    {
                        ip = ipList.get(2).getIp();
                        if (TextUtils.isEmpty(ip))
                        {
                            LOGManager.v(getClass().getName()+":启用默认地址列表登录");
                            url = ReleaseConfig.getUrl(Engine.getInstance().getCC());
                        }
                    }
                }
                url = UrlUtil.makeUrl(ip);
            }

            // SERVICE方式正常登录
            LoginRsp login = Engine.getInstance().loginRequest(
                    url,
                    Engine.getInstance().userName,
                    SpUtil.getLongPswd(LoginService.this),null);
            if (login == null)
            {
                LOGManager.e("网络不给力");

                // 停止登录服务
                Intent i = new Intent(mContext, LoginService.class);
                stopService(i);
                return;
            }

            if (login.getBaseRsp().isSuccess())
            {
                Engine.getInstance().isLogIn = true;
                //SpUtil.setLastLogin(true);
                
                Engine.getInstance().syncContactThread();
                
                // 获取用户信息
                Engine.getInstance().getUserInfoRequest(
                        Engine.getInstance().getUserName(), null);
                
                // 获取公共状态
                GetCommStatusRsp rsp = Engine.getInstance().getCommStatusRequest(
                        LoginService.this, url);
                Engine.getInstance().syncProfilesThread(rsp);
                
                // 上传log
//                Engine.getInstance().syncLog();
                
                LOGManager.d("LoginService登录成功");

                Engine.getInstance().judgeHomeOrRoam();                

                Intent intent = new Intent();
                intent.setAction(IAction.MY_CHANGED);
                sendBroadcast(intent);

                // 根据homeLogin判断是否获取归属地配置信息还是漫游地配置信息
                if (Engine.getInstance().homeLogin == true)
                {
                    // get home profile
                    Engine.getInstance().getHomeProfileFromDB(
                            Engine.getInstance().getSimMcc(),
                            Engine.getInstance().getSimMnc());
                }
                else
                {
                    // get roam profile
                    Engine.getInstance().getRoamProfileFromDB(
                            Engine.getInstance().getSimMcc(),
                            Engine.getInstance().getSimMnc());

                    String strRoamGlmsIp = Engine.getInstance()
                            .getRoamGlmsLoc();
                    if (null == strRoamGlmsIp || strRoamGlmsIp.equals(""))
                    {
                        LOGManager.e("没有获取到漫游地配置信息");

                        // 停止登录服务
                        Intent i = new Intent(mContext, LoginService.class);
                        stopService(i);
                    }
                    else
                    {
                        String strUrl = "http://" + strRoamGlmsIp + "/";
                        Engine.getInstance().loginRequest(strUrl,
                                Engine.getInstance().userName,
                                SpUtil.getLongPswd(LoginService.this),null);
                    }
                }

                // 启动接收消息服务
//                Intent in = new Intent(mContext, TravelService.class);
//                startService(in);
                
                Engine.getInstance().tryToStartNR(LoginService.this);

                // 停止登录服务
                Intent i = new Intent(mContext, LoginService.class);
                stopService(i);
            }
            else
            {
                Engine.getInstance().stopNR(mContext);
                Engine.getInstance().isLogIn = false;
                //SpUtil.setLastLogin(false);
                Engine.getInstance().setLongPswd("");
                LOGManager.d("LoginService登录失败");

                // 登录失败,清长密码,转向界面登录                
                Engine.getInstance().showSysDialogAct(mContext, "提示",
                        login.getBaseRsp().getMsg(), "", "", 0, "LoginActivity");

                // 停止登录服务
                Intent i = new Intent(mContext, LoginService.class);
                stopService(i);
                
                Intent intent = new Intent(mContext, TravelService.class);
                stopService(intent);
            }
        }
    }
}
