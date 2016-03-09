package com.travelrely.core.nrs.nr;

import java.nio.ByteBuffer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.core.nrs.App;
import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.ble.BleMsgId;
import com.travelrely.core.nrs.nr.TcpManager.TcpCallback;
import com.travelrely.core.nrs.nr.msg.AgtAppSimCardFeeInd;
import com.travelrely.core.nrs.nr.msg.AppAgtCallingReq;
import com.travelrely.core.nrs.nr.msg.AppAgtDtmfInd;
import com.travelrely.core.nrs.nr.msg.AppAgtLSmsSendReq;
import com.travelrely.core.nrs.nr.msg.AppAgtPowerOnInd;
import com.travelrely.core.nrs.nr.msg.AppAgtTcpReconn;
import com.travelrely.core.nrs.nr.msg.MsgId;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.nrs.nr.util.CallUtil;
import com.travelrely.core.nrs.nr.util.TextUtil;
import com.travelrely.core.util.LogUtil;
import com.travelrely.core.util.NetUtil;
import com.travelrely.core.util.PreferencesUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.sdk.SDKAction;
import com.travelrely.v2.AesLib;
import com.travelrely.sdk.R;
import com.travelrely.v2.Rent.RentCallAct;
import com.travelrely.v2.db.SmsEntityDBHelper;
import com.travelrely.v2.model.SmsEntity;
import com.travelrely.v2.service.PlayMusic;

public class NRService extends Service
{
    private static final String TAG = "NRService";

    App myApp = null;
    private boolean isPush = false;

    private NRHandler mNRHandler;

    private Bundle bundle;
    SmsEntity smsEntity = null;

    NotificationManager mNotificationManager;
    
    int iTimeTick = 0;
    LocalBroadcastManager mloBroadcastManager;
    
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle b;
            switch (msg.what)
            {
                case MsgId.AGENT_APP_TCP_RSP:
                    Engine.getInstance().isTcpOK = true;

                    if (isPush)
                    {
                        isPush = false;
                        myApp.setLoadFlag(false);
                        AppAgtPowerOnInd ind = new AppAgtPowerOnInd(
                                Engine.getInstance().userName());
                        cmdSocket.sendCmdMsg(ind.toMsg());
                        LogUtil.i(4, "send power on ind");
                        return;
                    }
                    if (Engine.getInstance().isNRRegisted)
                    {
                        notifyNRRegisted();
                    }
                    else if (!TextUtils.isEmpty(SpUtil.getBtAddr()))
                    {
                        Engine.getInstance().startBleService(NRService.this,
                                BleMsgId.NR_BLE_READ_SIM_IND);
                    }
                    break;
                    
                // APP发REG超时
                case MsgId.APP_REG_EXPIRE:
                    Engine.getInstance().broadcast(IAction.NR_REG_EXPIRE);
                    break;

                // APP发REG失败
                case MsgId.APP_REG_FAIL:
                    Engine.getInstance().broadcast(IAction.NR_REG_FAIL);
                    break;

                // APP发REG成功
                case MsgId.APP_REG_SUCC:
                	
                	if (SpUtil.getfirstConnect() == 0){
                		SpUtil.setfirstConnect(1);
                		Engine.getInstance().broadcast(IAction.NR_REG_SUCC);
                	}
                	
                		
                    // 供张耀使用
                    PreferencesUtil.setSharedPreferences(NRService.this,
                            PreferencesUtil.PUBLIC_PRERENCES,
                            ConstantValue.layoutTypeKey, "0");
                    Engine.getInstance().isNRRegisted = true;
                    notifyNRRegisted();
                    break;
                    
                case MsgId.NR_UI_SIM_FEE_ALERT:
                    b = msg.getData();
                    byte[] msgContent = b.getByteArray("cmd");
                    AgtAppSimCardFeeInd si = new AgtAppSimCardFeeInd(msgContent);
                    String title = "";
                    if (si.getTitleLen() > 0)
                    {
                        title = si.getTitle();
                    }
                    Engine.getInstance().showAppAlertDialog(NRService.this, title,
                            si.getMsg(), "知道了", "不再提醒", new OnSysAlertClickListener()
                            {
                                @Override
                                public void onRightClick(SysAlertDialog dialog)
                                {
                                    if (mNRHandler == null)
                                    {
                                        return;
                                    }
                                    mNRHandler.procMsg(MsgId.APP_AGENT_SIM_CARD_FEE_RSP, null);
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
                    break;
                    
                case MsgId.APP_SHOW_REG_FAIL:
                    clearNotification(R.drawable.nr_registed);
                    Engine.getInstance().isNRRegisted = false;
                    break;

                case MsgId.APP_SHOW_REG_SUCC:
                    Engine.getInstance().isNRRegisted = true;
                    notifyNRRegisted();
                    break;

                // 主叫被接通,启动语音
                case MsgId.APP_CALLING_CONNECTED:
                    playMusic(false);
                    Engine.getInstance().broadcast(IAction.CALL_STATE_CHANGED,
                            MsgId.APP_CALLING_CONNECTED);
                    break;
                    
                 // APP播放本地铃音
                case MsgId.APP_PLAY_RINGTONE:
                    playMusic(true);
                    break;

                default:
                    break;
            }
        }
    };
    
    private final Runnable mChkNet = new Runnable()
    {
        @Override
        public void run()
        {
            if (NetUtil.isNetworkAvailable(getApplicationContext()))
            {
                cmdSocket.start();
            }
            else
            {
                LogUtil.w(TAG, "There is no network available");
                mHandler.postDelayed(this, 5000);
            }
        }
    };

    private int lastNet = 0;
    private TcpClient cmdSocket;
    private ByteBuffer mTcpBuffer = ByteBuffer.allocate(2048);
    private TcpCallback mTcpCallback = new TcpCallback()
    {
        @Override
        public void onTcpConnected()
        {
            lastNet = NetUtil.getNetType(getApplicationContext());
            LogUtil.w(4, "AtoN001,%d", lastNet);
            AppAgtTcpReconn reconn = new AppAgtTcpReconn(Engine
                    .getInstance().userName(), NetUtil.getNetType(NRService.this),
                    NetUtil.getPhoneIp());
            cmdSocket.sendCmdMsg(reconn.toMsg());
        }
        
        @Override
        public void onTcpDisconnected()
        {
            LogUtil.w(TAG, "TCP disconnected!");
            Engine.getInstance().isTcpOK = false;
            clearNotification(R.drawable.nr_registed);
            
            mHandler.post(mChkNet);
        }

        /* (non-Javadoc)收到NR服务器发过来的消息回调
         * @see com.travelrely.v2.NR.TcpManager.TcpCallback#onTcpReceived(byte[], int)
         */
        @Override
        public void onTcpReceived(byte[] buf, int len)
        {
            iTimeTick = 0;
            
            //mTcpBuffer.put(buf, 0, len);
            
            //mTcpBuffer.flip();
            //int iMsgId = mTcpBuffer.getInt();
            //int iContentLen = mTcpBuffer.getInt();
            //byte[] dst = new byte[iContentLen];
            //mTcpBuffer.get(dst, 0, dst.length);
            
            
            byte[] msg = new byte[len];
            System.arraycopy(buf, 0, msg, 0, len);

            int i = 0;
            while (i < len)
            {
                int iMsgId = ByteUtil.getInt(ByteUtil.subArray(msg, i, 4));
                int iContentLen = ByteUtil.getInt(ByteUtil.subArray(msg, i + 4, 4));
                byte[] MsgBuf = ByteUtil.subArray(msg, i, 8 + iContentLen);
                
                i = i + 8 + iContentLen;
                
                if (iContentLen == 0)
                {
                    mNRHandler.procMsg(iMsgId, MsgBuf);
                    continue;
                }
                
                if (!Engine.getInstance().isEncEnable)
                {
                    mNRHandler.procMsg(iMsgId, MsgBuf);
                    continue;
                }
                
                byte[] tmp = ByteUtil.subArray(MsgBuf, 8, MsgBuf.length-8);
                AesLib.decrypt(tmp, tmp);

                System.arraycopy(tmp, 0, MsgBuf, 8, tmp.length);
                
                //LogUtil.i(TAG, ByteUtil.toHexString(MsgBuf));
                
                mNRHandler.procMsg(iMsgId, MsgBuf);
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();
            LogUtil.i(TAG, "action = " + action);
            
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action))
            {
                if (NetUtil.getNetType(getApplicationContext()) == 0)//wifi ok
                {
                    if (lastNet != 0)
                    {
                        cmdSocket.reset();
                    }
                }
            }
            
            if (Intent.ACTION_TIME_TICK.equals(action))
            {
                iTimeTick++;
                if (iTimeTick < 5)
                {
                    return;
                }
                
                iTimeTick = 0;
                
                if (mNRHandler.iUeMainState == UeState.MAIN_REGISTED)
                {
                    Engine.getInstance().startBleService(NRService.this,
                            BleMsgId.NR_BLE_SCAN_BOX);
                }
                else if (mNRHandler.iUeMainState == UeState.MAIN_NULL)
                {
                    if (Engine.getInstance().isTcpOK)
                    {
                        mNRHandler.procMsg(MsgId.APP_AGENT_KEEPALIVE_REQ, null);
                    }
                    else
                    {
                        LogUtil.e(TAG, "TCP is not connected");
                        cmdSocket.reset();
                    }
                }
            }

            if (IAction.BOX_MISS_MATCH.equals(action))
            {
                clearNotification(R.drawable.nr_registed);
            }

            if (IAction.SMS_NOTIFY.equals(action))
            {
                long id = intent.getLongExtra("LONG_ARGS", -1L);
                if (id > 0)
                {
                    SmsEntity sms = SmsEntityDBHelper.getInstance().getSms(id);
                    // NotificationUtil.notifySms(context, sms);
                    Engine.getInstance().broadcastSmsEntity(SDKAction.NR_SDK_NOTIFIY_SMS, sms);
                    
                    Engine.getInstance().broadcast(IAction.SMS_RECV);
                }
            }
            
            if (IAction.SMS_CLEAR_NOTIFY.equals(action))
            {
                clearNotification(R.drawable.msg_notify);
            }
            
            if (IAction.NOTIFY_MISSED_CALL.equals(action))
            {
                String s = intent.getStringExtra("STR_ARGS");
                
                Engine.getInstance().broadcast(SDKAction.NR_SDK_NOTIFIY_MISSED_CALL, s);;
                // NotificationUtil.notifyMissedCall(context, s);
            }
            
            if (IAction.CLEAR_MISSED_CALL.equals(action))
            {
                clearNotification(R.drawable.nr_missing_call);
            }
        }
    };

    private static IntentFilter makeFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        intentFilter.addAction(IAction.BOX_MISS_MATCH);

        intentFilter.addAction(IAction.SMS_NOTIFY);
        intentFilter.addAction(IAction.SMS_CLEAR_NOTIFY);
        
        intentFilter.addAction(IAction.NOTIFY_MISSED_CALL);
        intentFilter.addAction(IAction.CLEAR_MISSED_CALL);
       

        return intentFilter;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        LogUtil.i(TAG, "NRService onCreate");
        myApp = (App)getApplication();
        
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        registerReceiver(mReceiver, makeFilter());


        // 创建消息处理线程
        //cmdSocket = new TcpManager("42.96.191.167", 30005);
        //cmdSocket.setTcpCallback(mTcpCallback);
        
        //worker = new NRWorker(this, cmdSocket);
        //cmdThread = new Thread(worker);
        //cmdThread.start();
        
        //cmdSocket.start();
        
        cmdSocket = new TcpClient(SpUtil.getNRFemtoIp(), 30005);
        cmdSocket.setTcpCallback(mTcpCallback);

        HandlerThread mNRThread = new HandlerThread("NRThread");
        mNRThread.start();
        mNRHandler = new NRHandler(mNRThread.getLooper(), cmdSocket, this, mHandler);

        cmdSocket.start();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {        
        if (intent == null)
        {
            LogUtil.e(TAG, "intent == null");
            return;
        }
        bundle = intent.getExtras();
        if (bundle == null)
        {
            LogUtil.e(TAG, "bundle == null");
            return;
        }
//        Engine.getInstance().broadcast(IAction.NOTIFY_POP_DLG, "你好金佛寺");
        LogUtil.i(TAG, "NRService onStart");

        int iMsgId = bundle.getInt("MSG_ID");
        byte[] msg = bundle.getByteArray("MSG");
        opMsg(iMsgId, msg);
    }

    @Override
    public void onDestroy()
    {
        shutdown();

        unregisterReceiver(mReceiver);

        LogUtil.e(TAG, "NRService onDestroy");

        super.onDestroy();

        //stopForeground(true);
		//Intent localIntent = new Intent();
		//localIntent.setClass(this, NRService.class); // 销毁时重新启动Service
		//this.startService(localIntent);
    }

    private void opMsg(int iMsgId, byte[] msg)
    {
        switch (iMsgId)
        {
            case MsgId.BLE_NR_REG_IND:
                if (Engine.getInstance().isTcpOK)
                {
                    mNRHandler.procMsg(MsgId.APP_AGENT_REGISTER_REQ, null);
                }
                else
                {
                    Engine.getInstance().broadcast(IAction.NR_REG_FAIL);
                    LogUtil.e(TAG, "TCP is not connected");
                }
                break;
                
            case MsgId.APP_AGENT_HW_INFO_REQ:
                mNRHandler.procMsg(MsgId.APP_AGENT_HW_INFO_REQ, null);
                break;

            case MsgId.APP_AGENT_AUTH_RSP:
                mNRHandler.procMsg(MsgId.APP_AGENT_AUTH_RSP, null);
                break;
                
            case MsgId.APP_AGENT_STORE_KEY_RSLT_IND:
                mNRHandler.procMsg(MsgId.APP_AGENT_STORE_KEY_RSLT_IND, null);
                break;

            case MsgId.BLE_NR_KEEPALIVE_IND:
                if (Engine.getInstance().isTcpOK)
                {
                    mNRHandler.procMsg(MsgId.APP_AGENT_KEEPALIVE_REQ, null);
                }
                else
                {
                    LogUtil.e(TAG, "TCP is not connected");
                    cmdSocket.reset();
                }
                break;

            // 主叫
            case MsgId.APP_CALLING_REQ:
                if (mNRHandler.iUeMainState != UeState.MAIN_REGISTED)
                {
                    Engine.getInstance().showSysAlert(this, "提示",
                            "系统正忙，请稍后再拨", "确定");
                    break;
                }
                String num = bundle.getString("MSG_STR", "");
                if (num.length() > 28)
                {
                    Engine.getInstance().showSysAlert(this, "提示",
                            "该号码是无效号码，请确认后再拨", "确定");
                    break;
                }
                AppAgtCallingReq calling = new AppAgtCallingReq(Engine
                        .getInstance().userName(), num);
                mNRHandler.procMsg(MsgId.APP_AGENT_CALLING_REQ, calling.toMsg());
                // gotoCallAct(CallActivity.class, MsgId.APP_CALLING_REQ, num);
                sendCallAction(SDKAction.NR_SDK_CALLING_ACTION, MsgId.APP_CALLING_REQ, num);
                break;

            // 被叫请求,显示被叫界面
            case MsgId.APP_CALLED_REQ:
                CallUtil.unlockScreen(this, true);// 点亮亮屏

                LogUtil.i(TAG, "显示被叫界面");

                String caller = bundle.getString("MSG_STR");
                // gotoCallAct(CallActivity.class, MsgId.APP_CALLED_REQ, caller);
                sendCallAction(SDKAction.NR_SDK_CALLED_ACTION, MsgId.APP_CALLED_REQ, caller);
                setAlert(true);
                break;

            // 被叫接听操作
            case MsgId.APP_ANSWER:
                setAlert(false);// 关闭铃音
                mNRHandler.procMsg(MsgId.APP_ANSWER, null);
                break;

            // APP挂断
            case MsgId.APP_HANG_UP:
                setAlert(false);
                mNRHandler.procMsg(MsgId.APP_HANG_UP, null);
                break;

            // 对方挂断
            case MsgId.APP_BE_HUNG_UP:
                setAlert(false);
                Engine.getInstance().broadcast(IAction.CALL_STATE_CHANGED,
                        MsgId.APP_BE_HUNG_UP);
                break;

            // APP发SMS                
            case MsgId.UI_NR_SEND_SMS_REQ:
                long l = bundle.getLong("MSG_LONG");
                smsEntity = SmsEntityDBHelper.getInstance().getSms(l);
                String user = Engine.getInstance().userName();
                String recv = Utils.FullNumber(smsEntity.getAddress()).replace("+", "");
                byte[] sms = TextUtil.EncodeUCS2BE(smsEntity.getBody());
                AppAgtLSmsSendReq send = new AppAgtLSmsSendReq(user,
                        recv, sms);
                mNRHandler.procMsg(MsgId.APP_AGENT_LSMS_SEND_REQ, send.toMsg());
                break;

            // APP发SMS超时
            case MsgId.APP_SEND_SMS_EXPIRE:

                // APP发SMS失败
            case MsgId.APP_SEND_SMS_FAIL:
                smsEntity.setStatus(SmsEntity.STATUS_FAILED);
                SmsEntityDBHelper.getInstance().update(smsEntity);
                Engine.getInstance().broadcast(IAction.REFRESH_UI);
                break;

            // APP发SMS成功
            case MsgId.APP_SEND_SMS_SUCC:
                smsEntity.setStatus(SmsEntity.STATUS_SUCC);
                SmsEntityDBHelper.getInstance().update(smsEntity);
                Engine.getInstance().broadcast(IAction.REFRESH_UI);
                break;

            // APP发键盘值
            case MsgId.APP_AGENT_DTMF_IND:
                mNRHandler.procMsg(MsgId.APP_AGENT_DTMF_IND, msg);
                Engine.getInstance().broadcast(IAction.CALL_SEND_DTMF,
                        new AppAgtDtmfInd(msg).key);
                break;

            case MsgId.APP_SHOW_ERR_MSG://服务器弹出错误提示框
                String strMsg = bundle.getString("MSG_STR");
                if (!TextUtils.isEmpty(strMsg))
                {
                    Engine.getInstance().broadcast(IAction.NOTIFY_POP_DLG, strMsg);
                }
                break;

            case MsgId.APP_START_RECORD:
                mNRHandler.procMsg(MsgId.APP_START_RECORD, null);
                break;

            case MsgId.APP_STOP_RECORD:
                mNRHandler.procMsg(MsgId.APP_STOP_RECORD, null);
                break;

            // APP锁屏
            case MsgId.APP_LOCK_SCREEN:
                CallUtil.unlockScreen(this, false);
                break;

            // APP解锁屏
            case MsgId.APP_UNLOCK_SCREEN:
                CallUtil.unlockScreen(this, true);
                break;

            

            // APP停止播放本地铃音
            case MsgId.APP_STOP_RINGTONE:
                playMusic(false);
                break;

            // 被叫请求,显示被叫界面
            case MsgId.APP_XH_CALLED_REQ:
                CallUtil.unlockScreen(this, true);// 点亮亮屏

                LogUtil.i(TAG, "显示小号被叫界面");

                String peer = bundle.getString("MSG_STR", "");
                gotoCallAct(RentCallAct.class, MsgId.APP_XH_CALLED_REQ, peer);
                setAlert(true);
                break;

            // 被叫接听操作
            case MsgId.APP_XH_ANSWER:
                LogUtil.e(TAG, "接电话");
                setAlert(false);// 关闭铃音
                mNRHandler.procMsg(MsgId.APP_XH_ANSWER, null);
                break;

            // APP挂断
            case MsgId.APP_XH_HANG_UP:
                setAlert(false);
                mNRHandler.procMsg(MsgId.APP_XH_HANG_UP, null);
                break;

            // 对方挂断
            case MsgId.APP_XH_BE_HUNG_UP:
                setAlert(false);
                Engine.getInstance().broadcast(IAction.CALL_STATE_CHANGED,
                        MsgId.APP_XH_BE_HUNG_UP);
                break;

            case MsgId.APP_CLOSE_NR:
                mNRHandler.procMsg(MsgId.APP_CLOSE_NR, null);
                Engine.getInstance().isNRRegisted = false;
                clearNotification(R.drawable.nr_registed);
                break;

            case MsgId.APP_OPEN_NR:
                mNRHandler.procMsg(MsgId.APP_OPEN_NR, null);
                break;

            case MsgId.APP_INNER_RESET:
                mNRHandler.procMsg(MsgId.APP_INNER_RESET, null);
                Engine.getInstance().isNRRegisted = false;
                clearNotification(R.drawable.nr_registed);
                break;

            case MsgId.APP_AGENT_USER_VERIFY_REQ:
                mNRHandler.procMsg(MsgId.APP_AGENT_USER_VERIFY_REQ, msg);
                break;

            case MsgId.APP_GETUI_IND:
                isPush = true;
                if (myApp.isNewLoad())
                {
                    // 当APP被kill后个推起来能够处理被叫，但是主叫需要该标志
                    Engine.getInstance().isNRRegisted = true;
                    notifyNRRegisted();

                    myApp.setLoadFlag(false);
                }
                else
                {
                    cmdSocket.reset();//重建TCP连接
                }
                break;

            default:
                LogUtil.w(TAG, MsgId.getMsgStr(iMsgId));
                break;
        }
    }

    private void gotoCallAct(Class<?> pClass, int code, String num)
    {
        // 跳转到接听界面
        Intent intent2 = new Intent(this, pClass);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", code);
        bundle.putString("NUMBER", num);

        intent2.putExtras(bundle);

        startActivity(intent2);
    }
    
    private void sendCallAction(String action, int code, String num) {
    	Intent intent = new Intent();
    	intent.setAction(action);
    	
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", code);
        bundle.putString("NUMBER", num);
        intent.putExtras(bundle);
        
    	sendBroadcast(intent);
    	
    }

    // 控制声音和振动
    private void setAlert(boolean on)
    {
        if (PreferencesUtil.getSharedPreferencesBoolean(this,
                PreferencesUtil.ALERT_CONFIG, ConstantValue.callVoice))
        {
            playMusic(on);
        }

        if (PreferencesUtil.getSharedPreferencesBoolean(this,
                PreferencesUtil.ALERT_CONFIG, ConstantValue.callVabration))
        {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (on)
            {
                long[] pattern = { 100, 800, 900, 800 };
                vibrator.vibrate(pattern, 2);
            }
            else
            {
                vibrator.cancel();
            }
        }
    }

    private void playMusic(boolean on)
    {
        Intent intent1 = new Intent(this, PlayMusic.class);
        if (on)
        {
            startService(intent1);
        }
        else
        {
            stopService(intent1);
        }
    }

    
    private void notifyNRRegisted()
    {
        String str = "旅信网络电话注册成功";

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.nr_registed).setTicker(str)
                .setContentTitle("旅信网络电话").setContentText(str).setOngoing(true)
                .build();

        mNotificationManager.notify(R.drawable.nr_registed, notification);

        Engine.getInstance().broadcast(IAction.MY_CHANGED);
    }
    
    // 删除通知
    private void clearNotification(int id)
    {
        // 启动后删除之前我们定义的通知
        mNotificationManager.cancel(id);

        Engine.getInstance().broadcast(IAction.MY_CHANGED);
    }

    private void shutdown()
    {
        cmdSocket.stop();
        mNRHandler.getLooper().quit();
        clearNotification(R.drawable.nr_registed);
        Engine.getInstance().isNRRegisted = false;
    }
}
