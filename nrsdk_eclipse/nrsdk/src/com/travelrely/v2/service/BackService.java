package com.travelrely.v2.service;

import android.app.IntentService;
import android.content.Intent;

import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.core.IntentMsg;
import com.travelrely.v2.net_interface.GetCommStatusRsp;
import com.travelrely.v2.net_interface.LoginRsp;
import com.travelrely.v2.util.LOGManager;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.SpUtil;
import com.travelrely.v2.util.UrlUtil;

public class BackService extends IntentService
{
    public static final String TAG = BackService.class.getSimpleName();
    
    public BackService(String name)
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        int id = intent.getIntExtra(IntentMsg.INTENT_ID, 0);
        IntentMsgEntry(id);
    }
    
    void IntentMsgEntry(int id)
    {
        switch (id)
        {
            case IntentMsg.UI_BACK_LOGIN_IND:
                loginGlms();
                break;

            default:
                LogUtil.w(TAG, IntentMsg.getMsgStr(id));
                break;
        }
    }
    
    void loginGlms()
    {
        // 寻找登录地址
        // 登录
        LoginRsp login = Engine.getInstance().loginRequest(
                UrlUtil.ip(),
                Engine.getInstance().userName,
                SpUtil.getLongPswd(BackService.this),null);
        if (login == null)
        {
            LOGManager.e("网络不给力");
            return;
        }
        
        if (!login.getBaseRsp().isSuccess())
        {
            Engine.getInstance().isLogIn = false;
            //SpUtil.setLastLogin(false);
            Engine.getInstance().setLongPswd("");
            LOGManager.d("LoginService登录失败");
            
            Engine.getInstance().showSysDialogAct(BackService.this, "提示",
                    "登录失败，请确认密码！", "", "", 0, "LoginActivity");
        }

        Engine.getInstance().isLogIn = true;
        //SpUtil.setLastLogin(true);
        
        Engine.getInstance().syncContactThread();
        
        // 获取用户信息
        Engine.getInstance().getUserInfoRequest(
                Engine.getInstance().getUserName(), null);
        
        // 获取公共状态
        GetCommStatusRsp rsp = Engine.getInstance().getCommStatusRequest(
                BackService.this, UrlUtil.ip());
        Engine.getInstance().syncProfilesThread(rsp);
        
        // 上传log
//        Engine.getInstance().syncLog();
        
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
            }
            else
            {
                String strUrl = "http://" + strRoamGlmsIp + "/";
                Engine.getInstance().loginRequest(strUrl,
                        Engine.getInstance().userName,
                        SpUtil.getLongPswd(BackService.this),null);
            }
        }

        // 启动接收消息服务
//        Intent in = new Intent(BackService.this, TravelService.class);
//        startService(in);
        
        Engine.getInstance().tryToStartNR(BackService.this);
    }
}
