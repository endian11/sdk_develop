package com.travelrely.v2.service;

import com.travelrely.app.activity.Alarmalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CallAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//	    alarm_name
	    
	    String[] alarm_msg = null;
        int position = 0;
	    
	    Bundle bundle = intent.getExtras();
        if(bundle != null){
            alarm_msg = bundle.getStringArray("alarm_msg");
            position = bundle.getInt("position");
        }
	    
		Intent i= new Intent(context,Alarmalert.class);
		Bundle b = new Bundle();
		b.putStringArray("alarm_msg", alarm_msg);
		b.putInt("position", position);
		i.putExtras(bundle);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		}

}
