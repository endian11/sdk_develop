package com.travelrely.v2.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FinishReceiver extends BroadcastReceiver
{
    
    
    private Activity mContext;

    public FinishReceiver(Activity c)
    {
        mContext = c;
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        mContext.finish();
    }
}