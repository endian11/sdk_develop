package com.travelrely.v2.Rent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;

import com.travelrely.app.activity.NRSetAct;
import com.travelrely.core.ConstantValue;
import com.travelrely.core.IAction;
import com.travelrely.core.nr.LockService;
import com.travelrely.v2.Rent.msg.AgtRentCalledReq;
import com.travelrely.v2.Rent.msg.RentMsgId;
import com.travelrely.v2.service.PlayMusic;
import com.travelrely.v2.util.PreferencesUtil;

public class RentService extends Service
{
    private RentWorker worker;

    private Bundle bundle;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle b = msg.getData();
            if (b == null)
            {
                return;
            }

            byte[] msgContent = b.getByteArray("cmd");
            int iMsgId = msgContent[0];
            MsgEntry(iMsgId, msgContent);
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        // 创建消息处理线程
        worker = new RentWorker(this, mHandler);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        if (intent == null)
        {
            return;
        }
        bundle = intent.getExtras();
        if (bundle == null)
        {
            return;
        }

        int iMsgId = bundle.getInt("MSG_ID");
        byte[] msg = bundle.getByteArray("MSG");
        // worker.recvMsg(iMsgId, msg);
        MsgEntry(iMsgId, msg);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void MsgEntry(int iMsgId, byte[] msg)
    {
        switch (iMsgId)
        {
            case RentMsgId.APP_CALLED_REQ:
                unlockScreen(true);// 点亮亮屏

                intentAct(RentMsgId.APP_CALLED_REQ,
                        new AgtRentCalledReq(msg).getCaller());
                worker.recvMsg(RentMsgId.APP_CALLED_REQ, msg);
                setAlert(true);// 响铃
                break;

            case RentMsgId.APP_ANSWER:
                setAlert(false);// 关闭铃音
                worker.recvMsg(RentMsgId.APP_ANSWER, null);
                break;

            case RentMsgId.APP_HANG_UP:
                setAlert(false);// 关闭铃音
                worker.recvMsg(RentMsgId.APP_HANG_UP, null);
                break;

            case RentMsgId.APP_BE_HUNG_UP:
                setAlert(false);// 关闭铃音
                sendCallActBroadcast(RentMsgId.APP_BE_HUNG_UP);
                break;

            case RentMsgId.APP_LOCK_SCREEN:
                unlockScreen(false);// APP锁屏
                break;

            default:
                break;
        }
    }

    private void intentAct(int code, String num)
    {
        // 跳转到接听界面
        Intent intent2 = new Intent(this, RentCallAct.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", code);
        bundle.putString("NUMBER", num);
        intent2.putExtras(bundle);

        startActivity(intent2);
    }

    private void sendCallActBroadcast(int code)
    {
        Intent intent = new Intent();
        intent.setAction(IAction.CALL_STATE_CHANGED);
        Bundle bundle = new Bundle();
        bundle.putInt("CODE", code);
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

    private void unlockScreen(boolean on)
    {
        Intent intent = new Intent(this, LockService.class);

        if (on)
        {
            startService(intent);
        }
        else
        {
            stopService(intent);
        }
    }
}
