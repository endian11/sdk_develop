
package com.travelrely.v2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.travelrely.core.util.LOGManager;
import com.travelrely.v2.service.TravelService;

public class BootReceiver extends BroadcastReceiver {
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
		LOGManager.d("Boot completed");
//		Toast.makeText(context, "TravelService has started!", Toast.LENGTH_LONG).show();
		if (intent.getAction().equals(ACTION)) {
			// service
//			Intent myintent = new Intent(context, TravelService.class);
//			myintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//			context.startService(myintent);
			LOGManager.d("Start BootReceiver");
		}
	}
}
