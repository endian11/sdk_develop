package com.travelrely.app.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;

import com.travelrely.app.view.FormsArrawsRightCentreBt;
import com.travelrely.app.view.FormsRightOnOff;
import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.ProgressOverlay;
import com.travelrely.core.glms.ProgressOverlay.OnProgressEvent;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.nrs.Res;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.PreferencesUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.sdk.R;
import com.travelrely.v2.net_interface.ChangeChallengeRsp;
import com.travelrely.v2.response.Response;

/**
 * @author zhangyao
 * @version 2014年7月25日上午10:34:34
 */

public class NRSetAct extends NavigationActivity implements
        OnCheckedChangeListener, OnClickListener
{
    FormsRightOnOff call_voice_onoff;
    FormsRightOnOff call_vabration_onoff;

    FormsRightOnOff layoutType;
    FormsRightOnOff layoutTime;
    FormsArrawsRightCentreBt startTime;
    FormsArrawsRightCentreBt endTime;
    View line1;
    View line2;
    Calendar calendar;
    int temp;

    FormsRightOnOff switchBtUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_noroaming_type_set);

        calendar = Calendar.getInstance();
        init();
    }

    @Override
    protected void initNavigationBar()
    {
        // TODO Auto-generated method stub
        setTitle(R.string.NRSet);
        getNavigationBar().hideLeftText();
        getNavigationBar().hideRightImg();
        setRightText(R.string.save);
    }

    private void init()
    {
    	call_voice_onoff = (FormsRightOnOff) findViewById(R.id.layout_call_voice_on_off);
    	call_vabration_onoff = (FormsRightOnOff) findViewById(R.id.layout_call_vabration_on_off);
    	layoutType = (FormsRightOnOff) findViewById(R.id.layout_line_on_off);
    	layoutTime = (FormsRightOnOff) findViewById(R.id.layout_time_on_off);
    	startTime = (FormsArrawsRightCentreBt) findViewById(R.id.layout_date_start);
    	endTime = (FormsArrawsRightCentreBt) findViewById(R.id.layout_date_end);
    	line1 = findViewById(R.id.line1);
    	line2 = findViewById(R.id.line2);
    	switchBtUpdate = (FormsRightOnOff) findViewById(R.id.btUpdateAlert);
    	
    	
        call_voice_onoff.setTextLeft(R.string.tv_voice);
        call_vabration_onoff.setTextLeft(R.string.tv_vibration);
        call_voice_onoff.tButton.setOnCheckedChangeListener(this);
        call_vabration_onoff.tButton.setOnCheckedChangeListener(this);

        layoutType.tButton.setOnCheckedChangeListener(this);
        layoutTime.tButton.setOnCheckedChangeListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        startTime.showRightText();
        endTime.showRightText();
        startTime.setRightTextColor(R.drawable.btn_black_color_selector);
        endTime.setRightTextColor(R.drawable.btn_black_color_selector);

        switchBtUpdate.setTextLeft("软件更新提醒");

        refresh();
    }

    private void refresh()
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                call_voice_onoff.tButton.setChecked(isCheckBt(ConstantValue.callVoice));
                call_vabration_onoff.tButton
                        .setChecked(isCheckBt(ConstantValue.callVabration));
                layoutType.setTextLeft(R.string.do_not_disturb);
                layoutTime.setTextLeft(R.string.do_not_disturb_time);
                startTime.setLeftText(R.string.tv_start_time);
                endTime.setLeftText(R.string.tv_end_time);

                String layoutTypeKey = PreferencesUtil.getSharedPreferencesStr(
                        NRSetAct.this, PreferencesUtil.PUBLIC_PRERENCES,
                        ConstantValue.layoutTypeKey);

                if ("1".equals(layoutTypeKey))
                {
                    layoutType.tButton.setChecked(true);
                }
                if ("2".equals(layoutTypeKey))
                {
                    layoutTime.tButton.setChecked(true);
                    startTime.setRightText(PreferencesUtil
                            .getSharedPreferencesStr(NRSetAct.this,
                                    PreferencesUtil.PUBLIC_PRERENCES,
                                    ConstantValue.startTimeKey));
                    endTime.setRightText(PreferencesUtil
                            .getSharedPreferencesStr(NRSetAct.this,
                                    PreferencesUtil.PUBLIC_PRERENCES,
                                    ConstantValue.endTimeKey));
                }
                else
                {
                    startTime.setRightText("00:00");
                    endTime.setRightText("06:00");
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView == layoutTime.tButton)
        {
            if (layoutType.tButton.isChecked())
            {
                showShortToast("若想开启此功能，请关闭勿扰模式");
                layoutTime.tButton.setChecked(false);
            }
            else
            {
                if (isChecked)
                {
                    showTime();
                }
                else
                {
                    hideTime();
                }
            }
        }

        if (buttonView == layoutType.tButton)
        {
            if (layoutTime.tButton.isChecked())
            {
                showShortToast("若想开启此功能，请关闭时差休息模式");
                layoutType.tButton.setChecked(false);
                return;
            }
        }

        if (buttonView == call_voice_onoff.tButton)
        {
            if (isChecked)
            {
            //    saveAlertType(callVoice, true);
            }
            else
            {
          //      saveAlertType(callVoice, false);
            }
        }
        else if (buttonView == call_vabration_onoff.tButton)
        {
            if (isChecked)
            {
      //          saveAlertType(callVabration, true);
            }
            else
            {
        //        saveAlertType(callVabration, false);
            }
        }

        if (buttonView == switchBtUpdate.tButton)
        {
            SpUtil.setBtUpdate(isChecked);
        }
    }

    private void showTime()
    {
        startTime.setVisibility(View.VISIBLE);
        endTime.setVisibility(View.VISIBLE);
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.VISIBLE);
    }

    private void hideTime()
    {
        startTime.setVisibility(View.GONE);
        endTime.setVisibility(View.GONE);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
    }

    @Override
    public void onRightClick()
    {
        if (layoutTime.tButton.isChecked() == false
                && layoutType.tButton.isChecked() == false)
        {
            if ("0".equals(PreferencesUtil.getSharedPreferencesStr(
                    NRSetAct.this, PreferencesUtil.PUBLIC_PRERENCES,
                    ConstantValue.layoutTypeKey)))
            {
                finish();
            }
            else
            {
                saveNRState("0");
            }
        }
        else
        {
            if (layoutType.tButton.isChecked())
            {
                saveNRState("1");
            }
            else if (layoutTime.tButton.isChecked())
            {
                saveNRState("2");
            }
        }
		
		
		     //加上是否铃音 震动保存
        if(call_voice_onoff.tButton.isChecked()){
        	saveAlertType(ConstantValue.callVoice, true);
        }else{
        	saveAlertType(ConstantValue.callVoice, false);
        }
        
        if(call_vabration_onoff.tButton.isChecked()){
        	saveAlertType(ConstantValue.callVabration, true);
        }else{
        	saveAlertType(ConstantValue.callVabration, false);
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
		if (id == R.id.layout_date_start) {
			temp = 1;
			showTimePickerDialog();
		} else if (id == R.id.layout_date_end) {
			temp = 2;
			showTimePickerDialog();
		} else {
		}
    }

    private void showTimePickerDialog()
    {
        new TimePickerDialog(this, t, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener()
    {
        // 同DatePickerDialog控件
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            refreshTime();
        }
    };

    @SuppressLint("SimpleDateFormat")
    private void refreshTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.hm);

        switch (temp)
        {
            case 1:
                startTime.setRightText(sdf.format(calendar.getTime()));
                break;
                
            case 2:
                endTime.setRightText(sdf.format(calendar.getTime()));
                break;
            default:
                break;
        }
    }

    private void saveNRState(final String type)
    {
        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                String startTm = startTime.getRightText();
                String endTm = endTime.getRightText();

                // String postData = Request.setNoamingState(startTm +
                // ":00",endTm + ":00",
                // TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT),
                // type);
                String postData = Request.setNoamingState(startTm + ":00",
                        endTm + ":00", "8", type);
                
                String cc = Engine.getInstance().getCC();
                String host = ReleaseConfig.getUrl(cc);
                String urls = host + "api/user/set_noroaming_status";
                String httpResult = null;
                HttpConnector httpConnector = new HttpConnector();
                httpResult = httpConnector.requestByHttpPut(urls, postData,
                        null, true);
                ChangeChallengeRsp challenge = Response
                        .getChangeChallenge(httpResult);
                if (challenge.getBaseRsp().isSuccess())
                {
                    saveNoroamingType(type, startTm, endTm);
                    showShortToast(getResources().getString(R.string.save_ok));

                    finish();
                }
                else
                {
                    String strMsg = challenge.getBaseRsp().getMsg();
                    LOGManager.d(strMsg);

                    int retCode = challenge.getBaseRsp().getRet();
                    int errCode = challenge.getBaseRsp().getErrCode();
                    if (retCode < Res.networkErrCode.length
                            && errCode < Res.networkErrCode[retCode].length)
                    {
                        strMsg = getString(Res.networkErrCode[retCode][errCode]);
                    }
                    else
                    {
                        strMsg = getString(R.string.unknownNetErr);
                    }
                    showShortToast(strMsg);
                }
            }
        });
    }

    private void saveNoroamingType(String type, String startTm, String endTm)
    {
        if (type.equals("1") || type.equals("0"))
        {
            PreferencesUtil.setSharedPreferences(NRSetAct.this,
                    PreferencesUtil.PUBLIC_PRERENCES, ConstantValue.layoutTypeKey, type);
        }

        if (type.equals("2"))
        {
            PreferencesUtil.setSharedPreferences(NRSetAct.this,
                    PreferencesUtil.PUBLIC_PRERENCES, ConstantValue.layoutTypeKey, type);
            PreferencesUtil.setSharedPreferences(NRSetAct.this,
                    PreferencesUtil.PUBLIC_PRERENCES, ConstantValue.startTimeKey, startTm);
            PreferencesUtil.setSharedPreferences(NRSetAct.this,
                    PreferencesUtil.PUBLIC_PRERENCES, ConstantValue.endTimeKey, endTm);
        }
    }

    private void saveAlertType(String key, boolean value)
    {
        PreferencesUtil.setSharedPreferences(this,
                PreferencesUtil.ALERT_CONFIG, key, value);
    }

    private boolean isCheckBt(String key)
    {
        return PreferencesUtil.getSharedPreferencesBoolean(this,
                PreferencesUtil.ALERT_CONFIG, key);
    }
}
