package com.travelrely.v2.Rent;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.travelrely.app.view.DrawRoundView;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.nr.msg.MsgId;
import com.travelrely.core.util.BitmapCacheManager;
import com.travelrely.core.util.LogUtil;
import com.travelrely.model.CallRecord;
import com.travelrely.model.ContactModel;
import com.travelrely.sdk.R;
import com.travelrely.sdk.SDKAction;
import com.travelrely.app.db.CallRecordsDBHelper;
import com.travelrely.app.db.ContactDBHelper;

public class RentCallAct extends Activity implements OnClickListener,
        OnCheckedChangeListener, OnChronometerTickListener
{
    private static final String TAG = "RentCallAct";
    
    private static int w;

    private CallActReceiver mReceiver;
    private NotificationManager nMgr;
    private AudioManager mAudioManger;
    private SensorManager sm;

    private LinearLayout layoutCall, layoutWave, layoutHead;

    private ImageView ivHead;
    private TextView tvCurNum, tvName, tvCalling;
    private Chronometer timer;

    private LinearLayout opBtns;
    private ToggleButton btnMute, btnSpeaker;
    private ImageView ivPad;
    
    private LinearLayout layoutPad;

    private LinearLayout lHangUp, lAnswer;
    private TextView tvHangUp, tvAnswer, tvBack;

    private long lTimeCount = 0;

    private int iMsgId;
    
    private ContactModel contact;
    private String number = "";
    private String nickName;
    private String handUrl;
    private Bitmap head = null;

    private Handler handler = new Handler();

    private String strCurNum = "";
    
    CallRecord callRecord;
    
    private int count = 0;
    private final Runnable taskWave = new Runnable()
    {
        @Override
        public void run()
        {
            if (iMsgId == MsgId.APP_CALLING_REQ
                    || iMsgId == MsgId.APP_XH_CALLED_REQ)
            {
                drawView(count % 5);
                handler.postDelayed(this, 250);
                count++;
                if (count > 4)
                {
                    count = 0;
                }
            }
        }
    };

    private final Runnable taskRefresh = new Runnable()
    {
        @Override
        public void run()
        {
            if (TextUtils.isEmpty(number))
            {
                number = "未知号码";
                tvName.setText(number);
                return;
            }
            
            contact = ContactDBHelper.getInstance().getContactByNumberTry(number);
            if (contact != null)
            {
                nickName = contact.getName();
                handUrl = contact.getLocalHeadImgPath();
            }

            if (TextUtils.isEmpty(nickName))
            {
                tvName.setText(number);
            }
            else
            {
                tvName.setText(nickName);
            }

            if (!TextUtils.isEmpty(handUrl))
            {
                head = BitmapCacheManager.getInstance().getBitmapFromCache(
                        handUrl);
                ivHead.setImageBitmap(head);
            }
            else
            {
                ivHead.setImageResource(R.drawable.default_icon);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        w = this.getWindowManager().getDefaultDisplay().getWidth();

        /*
         *  让activity出现在锁屏画面前,mainifest中需要设置主题样式
         *  Theme.Wallpaper.NoTitleBar
         */
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.nr_call_act);

        mAudioManger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            iMsgId = bundle.getInt("TYPE");
            number = bundle.getString("NUMBER");

            if (!TextUtils.isEmpty(number))
            {
                callRecord = new CallRecord();
                callRecord.setNumber(number);
                callRecord.setNumberLable(2);
            }
        }
        init();
    }

    private void init()
    {
        layoutCall = (LinearLayout) findViewById(R.id.layoutCall);
        layoutWave = (LinearLayout) findViewById(R.id.wave);
        layoutHead = (LinearLayout) findViewById(R.id.layout_heand);
        layoutHead.setLayoutParams(new FrameLayout.LayoutParams(w, w * 25 / 32));

        ivHead = (ImageView) findViewById(R.id.ivHead);
        ivHead.setLayoutParams(new LinearLayout.LayoutParams(w * 6 / 32, w * 6 / 32));

        tvCurNum = (TextView) findViewById(R.id.tvCurNum);
        tvName = (TextView) findViewById(R.id.tvName);
        tvCalling = (TextView) findViewById(R.id.tv_calling);

        timer = (Chronometer) findViewById(R.id.chronometer);
        timer.setFormat("00:%s");
        timer.setOnChronometerTickListener(this);

        opBtns = (LinearLayout) findViewById(R.id.optBtn);
        btnMute = (ToggleButton) findViewById(R.id.btnMute);
        btnMute.setOnCheckedChangeListener(this);
        btnSpeaker = (ToggleButton) findViewById(R.id.btnSpeaker);
        btnSpeaker.setOnCheckedChangeListener(this);
        ivPad = (ImageView) findViewById(R.id.ivPad);
        ivPad.setOnClickListener(this);
        
        layoutPad = (LinearLayout) findViewById(R.id.layoutPad);

        lHangUp = (LinearLayout) findViewById(R.id.lHangUp);
        lAnswer = (LinearLayout) findViewById(R.id.lAnswer);
        tvHangUp = (TextView) findViewById(R.id.tvHangUp);
        tvHangUp.setOnClickListener(this);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);
        tvAnswer.setOnClickListener(this);
        
        tvBack = (TextView) findViewById(R.id.tvBack);
        tvBack.setOnClickListener(this);

        callState();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mReceiver == null)
        {
            mReceiver = new CallActReceiver();
            registerReceiver(mReceiver, makeFilter());
        }

        refresh();

        // 获取距离传感器
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
		if (id == R.id.tvHangUp) {
			iMsgId = MsgId.APP_XH_HANG_UP;
			showCallOver();
			clearNotification();
			Engine.getInstance().startNRService(this, iMsgId);
		} else if (id == R.id.tvAnswer) {
			iMsgId = MsgId.APP_XH_ANSWER;
			Engine.getInstance().startNRService(this, iMsgId);
			showConnect();
		} else if (id == R.id.ivPad) {
			showKeyboard();
		} else if (id == R.id.tvBack) {
			hideKeyboard();
		} else {
		}
    }

    private void refresh()
    {
        handler.post(taskRefresh);
    }

    /**
     * 处理进入界面的状态
     */
    private void callState()
    {
        handler.post(taskWave);
        switch (iMsgId)
        {
            case MsgId.APP_CALLING_REQ:
                showCalling();
                break;

            case MsgId.APP_XH_CALLED_REQ:
                showCalled();
                break;

            default:
                break;
        }
    }

    public class CallActReceiver extends BroadcastReceiver
    {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey"; // home key
        static final String SYSTEM_RECENT_APPS = "recentapps"; // long home key

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
            {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason == null)
                {
                    return;
                }

                if (reason.equals(SYSTEM_HOME_KEY))
                {
                    // home key处理点
                    showNotification();
                }
                else if (reason.equals(SYSTEM_RECENT_APPS))
                {
                    // long home key处理点
                    showNotification();
                }

                return;
            }
            
            if (action.equals(IAction.CALL_STATE_CHANGED))
            {
                Bundle bundle = intent.getExtras();
                int code = bundle.getInt("CODE");

                switch (code)
                {
                    case MsgId.APP_XH_BE_HUNG_UP:
                        if (iMsgId == MsgId.APP_XH_CALLED_REQ)
                        {
                            callRecord.setType(CallLog.Calls.MISSED_TYPE);
                            Engine.getInstance().broadcast(SDKAction.NR_SDK_NOTIFIY_MISSED_CALL, number);;
                            // notifyMissedCall();
                        }
                        iMsgId = MsgId.APP_XH_BE_HUNG_UP;
                        showCallOver();
                        clearNotification();
                        break;

                    case MsgId.APP_CALLING_CONNECTED:
                        iMsgId = MsgId.APP_CALLING_CONNECTED;
                        showConnect();
                        break;

                    default:
                        finish();
                        break;
                }
            }

            if (action.equals(IAction.CALL_SEND_DTMF))
            {
                Bundle bundle = intent.getExtras();
                int i = bundle.getInt("CODE");
                char c = (char)i;
                strCurNum = strCurNum + String.valueOf(c);
                if (!tvCurNum.isShown())
                {
                    tvName.setVisibility(View.GONE);                    
                    tvCurNum.setVisibility(View.VISIBLE);
                }
                tvCurNum.setText(strCurNum);
            }
        }
    }
    
    private static IntentFilter makeFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intentFilter.addAction(IAction.CALL_STATE_CHANGED);
        intentFilter.addAction(IAction.CALL_SEND_DTMF);
        return intentFilter;
    }

    private void manageNotification()
    {
        String str = "";
        switch (iMsgId)
        {
            case MsgId.APP_XH_CALLED_REQ:
                str = "等待接听";
                break;

            case MsgId.APP_CALLING_CONNECTED:
                str = "正在通话中";
                break;

            default:
                break;
        }

        Intent intent = new Intent(this, RentCallAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                R.layout.call_notification_layout, intent,
                Notification.FLAG_ONLY_ALERT_ONCE);

        RemoteViews rv = new RemoteViews(getPackageName(),
                R.layout.call_notification_layout);
        if (head != null)
        {
            rv.setImageViewBitmap(R.id.image, head);
        }
        else
        {
            rv.setImageViewResource(R.id.image, R.drawable.default_icon);
        }

        if (TextUtils.isEmpty(nickName))
        {
            rv.setTextViewText(R.id.tvName, number);
        }
        else
        {
            rv.setTextViewText(R.id.tvName, nickName);
        }
        rv.setTextViewText(R.id.tvCall, str);
        rv.setChronometer(R.id.chronometer_bar, SystemClock.elapsedRealtime(),
                "%s", true);

        Notification notification = new Notification.Builder(this)
        .setSmallIcon(R.drawable.dial_icon)
        .setContent(rv)
        .setAutoCancel(true)
        .setContentIntent(contentIntent)
        .setTicker(str)
        .build();

        nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nMgr.notify(R.layout.call_notification_layout, notification);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            showNotification();
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        timer.setBase(SystemClock.elapsedRealtime());

        if (mReceiver != null)
        {
            unregisterReceiver(mReceiver);
        }

        sm.unregisterListener(listener);// 注消所有传感器监听
    }

    private void showNotification()
    {
        manageNotification();
        moveTaskToBack(true);
    }
    
    private void clearNotification()
    {
        if (nMgr != null)
        {
            nMgr.cancel(R.layout.call_notification_layout);
        }
    }

    private void showCalling()
    {
        callRecord.setType(CallLog.Calls.OUTGOING_TYPE);

        tvCalling.setVisibility(View.VISIBLE);
        timer.setVisibility(View.GONE);
        lAnswer.setVisibility(View.GONE);
        lHangUp.setVisibility(View.VISIBLE);

        tvCalling.setText("正在呼叫...");

        refresh();
    }

    private void showCalled()
    {
        callRecord.setType(CallLog.Calls.INCOMING_TYPE);
        
        tvCalling.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.GONE);
        
        opBtns.setVisibility(View.INVISIBLE);

        lAnswer.setVisibility(View.VISIBLE);
        lHangUp.setVisibility(View.VISIBLE);

        //tvCalling.setText("来电");

        refresh();
    }

    private void showCallOver()
    {
        if (timer.isShown())
        {
            timer.stop();
            timer.setVisibility(View.GONE);
        }

        tvCalling.setVisibility(View.VISIBLE);
        tvCalling.setText("通话结束");
        
        if (callRecord != null)
        {
            callRecord.setDuration(lTimeCount);
            CallRecordsDBHelper.getInstance().insert(callRecord);
        }

        //drawView(new int[] { Color.RED });

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mAudioManger.setSpeakerphoneOn(false);
                Engine.getInstance().startNRService(RentCallAct.this,
                        MsgId.APP_LOCK_SCREEN);
                finish();
            }
        }, 2000);
    }

    private void showConnect()
    {
        lAnswer.setVisibility(View.GONE);
        tvCalling.setVisibility(View.GONE);
        timer.setVisibility(View.VISIBLE);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        
        opBtns.setVisibility(View.VISIBLE);

        drawView(new int[] { Color.WHITE });
    }

    private void drawView(int[] colors)
    {
        DrawRoundView dView = new DrawRoundView(this);
        dView.setMinimumWidth(100);
        dView.setMinimumHeight(100);
        dView.setCircleColor(colors);
        dView.invalidate();
        layoutWave.removeAllViews();
        layoutWave.addView(dView);
    }

    private void drawView(int circles)
    {
        if (circles == 0)
        {
            layoutWave.removeAllViews();
            return;
        }
        DrawRoundView dView = new DrawRoundView(this);
        dView.setMinimumWidth(100);
        dView.setMinimumHeight(100);
        dView.setCircleNum(circles);
        dView.invalidate();
        layoutWave.removeAllViews();
        layoutWave.addView(dView);
    }

    private void showKeyboard()
    {
        /*
        View view = getLayoutInflater().inflate(R.layout.keyboard, null);
        pw = new PopupWindow(view, w - 50, (w - 50) * 66 / 100);

        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true); // 设置是否允许在外点击使其消失,到底有用没

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        pw.showAtLocation(v, Gravity.NO_GRAVITY, 25,
                location[1] - pw.getHeight() - 45);*/
        
        if (!TextUtils.isEmpty(strCurNum))
        {
            tvName.setVisibility(View.GONE);
            tvCurNum.setVisibility(View.VISIBLE);
        }
        layoutCall.setVisibility(View.GONE);
        layoutPad.setVisibility(View.VISIBLE);
        tvBack.setVisibility(View.VISIBLE);
    }
    
    private void hideKeyboard()
    {
        tvCurNum.setVisibility(View.GONE);
        tvName.setVisibility(View.VISIBLE);
        
        layoutPad.setVisibility(View.GONE);
        layoutCall.setVisibility(View.VISIBLE);
        tvBack.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1)
    {
        int id = arg0.getId();
		if (id == R.id.btnMute) {
			if (arg1)
			{
			    Engine.getInstance().startNRService(this,
			            MsgId.APP_STOP_RECORD);
			}
			else
			{
			    Engine.getInstance().startNRService(this,
			            MsgId.APP_START_RECORD);
			}
		} else if (id == R.id.btnSpeaker) {
			mAudioManger.setSpeakerphoneOn(arg1);
		} else {
		}
    }

    private SensorEventListener listener = new SensorEventListener()
    {
        @Override
        public void onSensorChanged(SensorEvent event)
        {
            switch (event.sensor.getType())
            {
                case Sensor.TYPE_PROXIMITY:
                    float distance = event.values[0];
                    LogUtil.i(TAG, "Sensor.TYPE_PROXIMITY" + distance);
                    if (distance == 0.0)
                    {
                        // LOGManager.i("distance == 0.0");
                        // Window win = getWindow();
                        // WindowManager.LayoutParams lp = win.getAttributes();
                        // lp.screenBrightness = 0.001f;
                        // win.setAttributes(lp);

                        RentCallAct.this.findViewById(R.id.layoutRoot0)
                                .setVisibility(View.VISIBLE);
                        RentCallAct.this.findViewById(R.id.layoutRoot1)
                                .setVisibility(View.GONE);
                    }
                    else
                    {
                        // LOGManager.i("distance == "+distance);
                        // final Window win = getWindow();
                        // WindowManager.LayoutParams lp = win.getAttributes();
                        // lp.screenBrightness = 0.5f;
                        // win.setAttributes(lp);
                        RentCallAct.this.findViewById(R.id.layoutRoot0)
                                .setVisibility(View.GONE);
                        RentCallAct.this.findViewById(R.id.layoutRoot1)
                                .setVisibility(View.VISIBLE);
                    }

                    break;

                default:
                    break;
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {
            // TODO Auto-generated method stub
        }
    };

    @Override
    public void onChronometerTick(Chronometer chronometer)
    {
        lTimeCount++;
    }
}
