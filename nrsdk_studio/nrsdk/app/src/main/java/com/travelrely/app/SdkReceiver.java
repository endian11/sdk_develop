package com.travelrely.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.travelrely.app.activity.CallActivity;
import com.travelrely.app.activity.SysDialogActivity;
import com.travelrely.app.utils.NotificationUtil;
import com.travelrely.core.nrs.Constant;
import com.travelrely.core.nrs.IntentMsg;
import com.travelrely.sdk.SDKAction;
import com.travelrely.v2.model.SmsEntity;
import com.travelrely.v2.response.TraMessage;

public class SdkReceiver extends BroadcastReceiver {
	private String TAG = SdkReceiver.class.getName();
	
	private Context mContext;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	mContext = context;
        Log.i(TAG, "onReceive");
        
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (SDKAction.NR_SDK_CALLING_ACTION.equals(action) ||
        		SDKAction.NR_SDK_CALLED_ACTION.equals(action)) {
            if (bundle != null) {
                int iMsgId = bundle.getInt("TYPE");
                String number = bundle.getString("NUMBER");
                
                gotoCallAct(CallActivity.class, iMsgId, number);
            }
        } else if (SDKAction.NR_SDK_NOTIFIY_MISSED_CALL.equals(action)) {
        	if (bundle != null) {
        		String number = bundle.getString(IntentMsg.INTENT_STR_MSG);
        		NotificationUtil.notifyMissedCall(context, number);
        	}
        	
        } else if (SDKAction.NR_SDK_NOTIFIY_REC_SMS.equals(action)) {
        	if (bundle != null) {
        		TraMessage message = (TraMessage) bundle.getSerializable(Constant.TRA_ENTITY_INTENT_KEY);
            	NotificationUtil.sendNotifiyIfNeed(message, null);
        	}
        } else if (SDKAction.NR_SDK_NOTIFIY_SMS.equals(action)) {
        	if (bundle != null) {
        		SmsEntity smsMessage = (SmsEntity) bundle.getSerializable(Constant.SMS_ENTITY_INTENT_KEY);
            	NotificationUtil.notifySms(context, smsMessage);
        	}
        } else if (SDKAction.NR_SDK_SYSTEM_DIALOG_INTENT.equals(action)) {
        	if (bundle != null) {
        		Intent sysDialogIntent = new Intent(context, SysDialogActivity.class);
        		sysDialogIntent.putExtras(bundle);
        		sysDialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		context.startActivity(sysDialogIntent);
        	}
        }
    }
    
    private void gotoCallAct(Class<?> pClass, int code, String num) {
        Intent intent = new Intent(mContext, pClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", code);
        bundle.putString("NUMBER", num);

        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }

}