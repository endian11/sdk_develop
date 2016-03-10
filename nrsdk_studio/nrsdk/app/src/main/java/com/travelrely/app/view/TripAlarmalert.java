package com.travelrely.app.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.travelrely.sdk.R;
import com.travelrely.v2.response.TripInfoList.ActivityList;
import com.travelrely.v2.service.PlayMusic;

public class TripAlarmalert extends Activity{
    
    Handler handler;
    ActivityList activityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        handler = new Handler();
        
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            activityList = (ActivityList) bundle.getSerializable("ACTIVITY_LIST");
        }
        
        showAlarm();
    }
    
    private void showAlarm(){
        
        Intent intent1= new Intent(this,PlayMusic.class);
        startService(intent1);
        new AlertDialog.Builder(this).setTitle(R.string.trayelrely_alert).setMessage(activityList.getmAlarm().getAlarm_content())
        .setPositiveButton(R.string.close_alarm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent1= new Intent(TripAlarmalert.this,PlayMusic.class);
                stopService(intent1);
                TripAlarmalert.this.finish();
            }
        }).show();
    }

}
