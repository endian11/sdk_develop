package com.travelrely.v2.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.travelrely.sdk.R;

public class PlayMusic extends Service
{
    private MediaPlayer myMediaPlayer;

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        myMediaPlayer = MediaPlayer.create(this, R.raw.tx);
        myMediaPlayer.setLooping(true);
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        myMediaPlayer.stop();
        myMediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        // TODO Auto-generated method stub
        myMediaPlayer.start();
        super.onStart(intent, startId);
    }
}
