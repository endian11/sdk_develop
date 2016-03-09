package com.travelrely.app.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
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
import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.core.nr.HMediaManager;
import com.travelrely.core.nr.msg.MsgId;
import com.travelrely.core.nr.util.CallUtil;
import com.travelrely.model.CallRecord;
import com.travelrely.model.ContactModel;
import com.travelrely.sdk.Api;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.ContactDBHelper;
import com.travelrely.v2.util.BitmapCacheManager;
import com.travelrely.v2.util.LogUtil;

public class CallActivity extends Activity implements OnClickListener,
        OnCheckedChangeListener, OnChronometerTickListener
{
    private static final String TAG = "CallActivity";
    
    private static int w;

    private CallActReceiver mReceiver;
    private NotificationManager nMgr;
    private AudioManager mAudioManager;
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
    private String nickName = "";
    private String handUrl;
    private Bitmap head = null;
    private Handler handler = new Handler();

    private String strCurNum = "";
    private CallRecord callRecord;
    private int count = 0;
    
    private final Runnable taskWave = new Runnable() {
        @Override
        public void run() {
            if (iMsgId == MsgId.APP_CALLING_REQ
                    || iMsgId == MsgId.APP_CALLED_REQ) {
                drawView(count % 5);
                handler.postDelayed(this, 250);
                count++;
                if (count > 4) {
                    count = 0;
                }
            }
        }
    };

    private final Runnable taskRefresh = new Runnable() {
        @Override
        public void run() {
            if (TextUtils.isEmpty(number)) {
                number = "未知号码";
                tvName.setText(number);
                return;
            }
            
            contact = ContactDBHelper.getInstance().getContactByNumberTry(number);
            
            Cursor c = getContentResolver().query(Uri.withAppendedPath(
                    PhoneLookup.CONTENT_FILTER_URI, number), new String[] {
                    PhoneLookup._ID,
                    PhoneLookup.NUMBER,
                    PhoneLookup.DISPLAY_NAME,
                    PhoneLookup.TYPE, PhoneLookup.LABEL }, null, null,   null );

            if(c.getCount() == 0) {
               //没找到电话号码
            } else if (c.getCount() > 0) {
                c.moveToFirst();
                String phonename = c.getString(2); //获
                if (!TextUtils.isEmpty(phonename)){
                	callRecord.name = phonename;
//                	contact.setNickName(phonename);
                }
            }
            
            System.out.println("call activity : " + number );
            if (contact != null) {
                nickName = contact.getName();
                handUrl = contact.getLocalHeadImgPath();
            }

            if (TextUtils.isEmpty(nickName)) {
                tvName.setText(number);
            } else {
                tvName.setText(nickName);
            }

            if (!TextUtils.isEmpty(handUrl)) {
                head = BitmapCacheManager.getInstance().getBitmapFromCache(
                        handUrl);
                ivHead.setImageBitmap(head);
            } else {
                ivHead.setImageResource(R.drawable.default_icon);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            iMsgId = bundle.getInt("TYPE");
            number = bundle.getString("NUMBER");
            number = number.replaceAll(" ", "");
            LogUtil.i(TAG, "PERR NUM:" + number);

            if (!TextUtils.isEmpty(number)) {
                callRecord = new CallRecord();
                System.out.println("call record : ================= " + number);
                callRecord.setNumber(number);
                callRecord.setNumberLable(2);
            }
        }
        init();
        
        HMediaManager.init(getApplicationContext());
    }
    
    private void init() {
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
    protected void onResume() {
        super.onResume();
        if (mReceiver == null) {
            mReceiver = new CallActReceiver();
            registerReceiver(mReceiver, makeFilter());
        }

        refresh();

        // 获取距离传感器
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
		if (id == R.id.tvHangUp) {
			iMsgId = MsgId.APP_HANG_UP;
			showCallOver();
			clearNotification();
			Api.getInstance().hungUp(this);
		} else if (id == R.id.tvAnswer) {
			iMsgId = MsgId.APP_ANSWER;
			Engine.getInstance().answerPhone(this);
			showConnect();
		} else if (id == R.id.ivPad) {
			showKeyboard();
		} else if (id == R.id.tvBack) {
			hideKeyboard();
		} else {
		}
    }

    private void refresh() {
        handler.post(taskRefresh);
    }

    /**
     * 处理进入界面的状态
     */
    private void callState() {
        handler.post(taskWave);
        switch (iMsgId) {
            case MsgId.APP_CALLING_REQ:
                showCalling();
                break;

            case MsgId.APP_CALLED_REQ:
                showCalled();
                break;

            default:
                break;
        }
    }

    public class CallActReceiver extends BroadcastReceiver {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey"; // home key
        static final String SYSTEM_RECENT_APPS = "recentapps"; // long home key

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason == null) {
                    return;
                }

                if (reason.equals(SYSTEM_HOME_KEY)) {
                    // home key处理点
                    if (iMsgId == MsgId.APP_HANG_UP
                            || iMsgId == MsgId.APP_BE_HUNG_UP) {
                        return;
                    }
                    showNotification();
                } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                    // long home key处理点
                    if (iMsgId == MsgId.APP_HANG_UP
                            || iMsgId == MsgId.APP_BE_HUNG_UP) {
                        return;
                    }
                    showNotification();
                }

                return;
            }
            
            if (action.equals(IAction.CALL_STATE_CHANGED)) {
                Bundle bundle = intent.getExtras();
                int code = bundle.getInt("INT_ARGS");

                switch (code) {
                    case MsgId.APP_BE_HUNG_UP:
                        if (iMsgId == MsgId.APP_CALLED_REQ) {
                            if (callRecord != null) {
                                callRecord.setType(CallLog.Calls.MISSED_TYPE);
                                //notifyMissedCall();
                                Engine.getInstance().broadcast(IAction.NOTIFY_MISSED_CALL, number);
                            }
                        }
                        iMsgId = MsgId.APP_BE_HUNG_UP;
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

            if (action.equals(IAction.CALL_SEND_DTMF)) {
                Bundle bundle = intent.getExtras();
                int i = bundle.getInt("INT_ARGS");
                char c = (char)i;
                strCurNum = strCurNum + String.valueOf(c);
                if (!tvCurNum.isShown()) {
                    tvName.setVisibility(View.GONE);                    
                    tvCurNum.setVisibility(View.VISIBLE);
                }
                tvCurNum.setText(strCurNum);
            }
        }
    }
    
    private static IntentFilter makeFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intentFilter.addAction(IAction.CALL_STATE_CHANGED);
        intentFilter.addAction(IAction.CALL_SEND_DTMF);
        return intentFilter;
    }

    private void manageNotification() {
        String str = "";
        switch (iMsgId) {
            case MsgId.APP_CALLING_REQ:
                str = "正在呼叫...";
                break;

            case MsgId.APP_CALLED_REQ:
                str = "等待接听";
                break;

            case MsgId.APP_CALLING_CONNECTED:
            case MsgId.APP_ANSWER:
                str = "正在通话中";
                break;

            default:
                break;
        }

        Intent intent = new Intent(this, CallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                R.layout.call_notification_layout, intent,
                Notification.FLAG_ONLY_ALERT_ONCE);

        RemoteViews rv = new RemoteViews(getPackageName(),
                R.layout.call_notification_layout);
        if (head != null) {
            rv.setImageViewBitmap(R.id.image, head);
        } else {
            rv.setImageViewResource(R.id.image, R.drawable.default_icon);
        }

        if (TextUtils.isEmpty(nickName)) {
            rv.setTextViewText(R.id.tvName, number);
        } else {
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

    int getInCallStream() {
        /* Archos 5IT */
        if ((android.os.Build.BRAND.equalsIgnoreCase("archos")
		 && android.os.Build.DEVICE.equalsIgnoreCase("g7a")) 
		 || (android.os.Build.BRAND.equalsIgnoreCase("Huawei")
		 && android.os.Build.DEVICE.equalsIgnoreCase("hwC8813Q"))
		 || (android.os.Build.BRAND.equalsIgnoreCase("Huawei")
		 && android.os.Build.DEVICE.equalsIgnoreCase("hwc8813"))		 
		 || (android.os.Build.BRAND.equalsIgnoreCase("Xiaomi")
		 && android.os.Build.MODEL.contains("MI 3"))
		 || (android.os.Build.BRAND.equalsIgnoreCase("Xiaomi")
		 && android.os.Build.MODEL.contains("MI 2"))
		 || (android.os.Build.BRAND.equalsIgnoreCase("samsung")
		 && android.os.Build.DEVICE.equalsIgnoreCase("klte"))
		 || (android.os.Build.BRAND.equalsIgnoreCase("samsung")
		 && android.os.Build.DEVICE.equalsIgnoreCase("klteduosctc"))
		 || (android.os.Build.BRAND.equalsIgnoreCase("samsung")
		 && android.os.Build.DEVICE.equalsIgnoreCase("hllte"))
		 || (android.os.Build.BRAND.equalsIgnoreCase("samsung")
		 && android.os.Build.DEVICE.equalsIgnoreCase("trltechn"))	
		 || (android.os.Build.BRAND.equalsIgnoreCase("Huawei")
		 && android.os.Build.DEVICE.equalsIgnoreCase("hwB199"))
        || (android.os.Build.BRAND.equalsIgnoreCase("TCL")
		 && android.os.Build.DEVICE.equalsIgnoreCase("Diablo_LTE"))		 
		 ||(android.os.Build.BRAND.equalsIgnoreCase("Xiaomi")
		 && android.os.Build.MODEL.contains("MI 4"))) {
               // Since device has no voice call capabilities, voice call stream is
               // not implemented
               // So we have to choose the good stream tag, which is by default
               // falled back to music
               return AudioManager.STREAM_MUSIC;
        }
        // return AudioManager.STREAM_MUSIC;
       return AudioManager.STREAM_VOICE_CALL;
   }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int mode;
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mode = getInCallStream();
        int currentVolume = mAudioManager.getStreamVolume(mode);
        if (keyCode ==  KeyEvent.KEYCODE_VOLUME_UP) // 音量增大
        	mAudioManager.setStreamVolume(mode, currentVolume+1, 1);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) // 音量减小
        	mAudioManager.setStreamVolume(mode, currentVolume-1, 1);

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            showNotification();
            return true;
        }	
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.setBase(SystemClock.elapsedRealtime());

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        sm.unregisterListener(listener);// 注消所有传感器监听
    }

    private void showNotification() {
        manageNotification();
        moveTaskToBack(true);
    }
    
    private void clearNotification() {
        if (nMgr != null) {
            nMgr.cancel(R.layout.call_notification_layout);
        }
    }

    private void showCalling() {
        if (callRecord != null) {
            callRecord.setType(CallLog.Calls.OUTGOING_TYPE);
        }

        tvCalling.setVisibility(View.VISIBLE);
        timer.setVisibility(View.GONE);
        lAnswer.setVisibility(View.GONE);
        lHangUp.setVisibility(View.VISIBLE);

        tvCalling.setText("正在呼叫...");

        refresh();
    }

    private void showCalled() {
        if (callRecord != null) {
            callRecord.setType(CallLog.Calls.INCOMING_TYPE);
        }
        
        tvCalling.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.GONE);
        
        opBtns.setVisibility(View.INVISIBLE);

        lAnswer.setVisibility(View.VISIBLE);
        lHangUp.setVisibility(View.VISIBLE);

        //tvCalling.setText("来电");

        refresh();
    }

    private void showCallOver() {
        if (timer.isShown()) {
            timer.stop();
            timer.setVisibility(View.GONE);
        }

        tvCalling.setVisibility(View.VISIBLE);
        tvCalling.setText("通话结束");
        
        if (callRecord != null) {
            callRecord.setDuration(lTimeCount);
            System.out.println("callRecourd 字段name :-------- " +callRecord.name);
            Engine.getInstance().insertCallLog(CallActivity.this, callRecord, 2);
        }

        //drawView(new int[] { Color.RED });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mAudioManger.setSpeakerphoneOn(false);
                CallUtil.enableSpeaker(mAudioManager,false);
                Engine.getInstance().startNRService(CallActivity.this,
                        MsgId.APP_LOCK_SCREEN);
                finish();
            }
        }, 2000);
    }

    private void showConnect() {
        lAnswer.setVisibility(View.GONE);
        tvCalling.setVisibility(View.GONE);
        timer.setVisibility(View.VISIBLE);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        
        opBtns.setVisibility(View.VISIBLE);

        drawView(new int[] { Color.WHITE });
    }

    private void drawView(int[] colors) {
        DrawRoundView dView = new DrawRoundView(this);
        dView.setMinimumWidth(100);
        dView.setMinimumHeight(100);
        dView.setCircleColor(colors);
        dView.invalidate();
        layoutWave.removeAllViews();
        layoutWave.addView(dView);
    }

    private void drawView(int circles) {
        if (circles == 0) {
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

    private void showKeyboard() {   
        if (!TextUtils.isEmpty(strCurNum)) {
            tvName.setVisibility(View.GONE);
            tvCurNum.setVisibility(View.VISIBLE);
        }
        layoutCall.setVisibility(View.GONE);
        layoutPad.setVisibility(View.VISIBLE);
        tvBack.setVisibility(View.VISIBLE);
    }
    
    private void hideKeyboard() {
        tvCurNum.setVisibility(View.GONE);
        tvName.setVisibility(View.VISIBLE);
        
        layoutPad.setVisibility(View.GONE);
        layoutCall.setVisibility(View.VISIBLE);
        tvBack.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        int id = arg0.getId();
		if (id == R.id.btnMute) {
			if (arg1) {
			    Engine.getInstance().startNRService(this,
			            MsgId.APP_STOP_RECORD);
			} else {
			    Engine.getInstance().startNRService(this,
			            MsgId.APP_START_RECORD);
			}
		} else if (id == R.id.btnSpeaker) {
			if (HMediaManager.loudspeaker_st==0) {
				HMediaManager.loudspeaker_st = 1;
//    					if(mCall !=0){
//	    					mAudioManager.setSpeakerphoneOn(true);
//	    					CallUtil.enableSpeaker(mAudioManager,true);
//	    					mMedia.OptWebrtcCall(mCall, 1 /* HMC_APFlags*/, "on");
//    					}
				if(HMediaManager.mCall !=0){
					mAudioManager.setSpeakerphoneOn(true);
					CallUtil.enableSpeaker(mAudioManager,true);
					HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "on");
				}
			} else {
				HMediaManager.loudspeaker_st = 0;
				if(HMediaManager.mCall !=0){
					mAudioManager.setSpeakerphoneOn(false);
					CallUtil.enableSpeaker(mAudioManager,false);
					HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "off");
				}
			}
		} else {
		}
    }
    
    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_PROXIMITY:
                    float distance = event.values[0];
                    LogUtil.i(TAG, "Sensor.TYPE_PROXIMITY" + distance);
                    if (distance == 0.0) {
                        CallActivity.this.findViewById(R.id.layoutRoot0)
                                .setVisibility(View.VISIBLE);
                        CallActivity.this.findViewById(R.id.layoutRoot1)
                                .setVisibility(View.GONE);
                    } else {
                        CallActivity.this.findViewById(R.id.layoutRoot0)
                                .setVisibility(View.GONE);
                        CallActivity.this.findViewById(R.id.layoutRoot1)
                                .setVisibility(View.VISIBLE);
                    }
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    };

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        lTimeCount++;
    }
}
