package com.travelrely.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.travelrely.v2.service.PlayMusic;

public class Alarmalert extends Activity {

	String[] alarm_msg;
	Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		handler = new Handler();
		
		Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            alarm_msg = bundle.getStringArray("alarm_msg");
        }

        saveMessageAndUpdate();
        
		Intent intent1= new Intent(Alarmalert.this,PlayMusic.class);
		startService(intent1);
		new AlertDialog.Builder(Alarmalert.this).setTitle("旅信提醒!!").setMessage(alarm_msg[0] + "\t\t" + alarm_msg[1] + "\n" + alarm_msg[2])
		.setPositiveButton("关闭闹钟", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent1= new Intent(Alarmalert.this,PlayMusic.class);
				stopService(intent1);
				Alarmalert.this.finish();
			}
		}).show();
	}
	
    private void saveMessageAndUpdate() {

        handler.post(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent();
                intent.setAction("com.travelrely.receiver.setAlarm");
                sendBroadcast(intent);
            }
        });
    }

}
