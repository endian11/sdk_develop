
package com.travelrely.app.adapter;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.travelrely.app.view.MessageItem;
import com.travelrely.core.util.LOGManager;
import com.travelrely.sdk.R;
import com.travelrely.v2.response.TraMessage;

public class MessageListViewAdapter extends BaseAdapter implements
        OnCompletionListener, SensorEventListener {

    public static List<TraMessage> list;

    Activity context;

    Handler otherThreadHandler;

    private LayoutInflater lInflater;

    TraMessage message;

    MediaPlayer mediaPlayer;
    public VoiceViewMessage voiceViewMessage;
    Handler handler;

    private Sensor mSensor;

    SensorManager mSensorManager;

    public MessageListViewAdapter(Activity context, List<TraMessage> list) {

        this.context = context;
        this.list = list;
      
        HandlerThread handlerThread = new HandlerThread("downloadImg");
        handlerThread.start();
        otherThreadHandler = new Handler(handlerThread.getLooper());
        handler = new Handler();

        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        voiceViewMessage = new VoiceViewMessage();

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressWarnings("unused")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.online_msg_item, null);
        }
        View to = convertView.findViewById(R.id.to);
        View from = convertView.findViewById(R.id.from);

        TextView from_message = (TextView) convertView
                .findViewById(R.id.from_message);
        TextView to_message = (TextView) convertView
                .findViewById(R.id.to_message);
        ImageView from_message_img = (ImageView) convertView
                .findViewById(R.id.from_message_img);
        ImageView to_message_img = (ImageView) convertView
                .findViewById(R.id.to_message_img);

        final TraMessage message = list.get(position);
//        System.out.println(" MessageListViewAdapter list: " + list);
//        System.out.println("MessageListVIewAdatper : " + message);
        MessageItem messageItem = (MessageItem) convertView;
        messageItem.setMediaPlayer(mediaPlayer);
        messageItem.otherThreadHandler = otherThreadHandler;
        messageItem.mContext = context;
        messageItem.setUI(message, position, this);
        return convertView;
    }

    public void destory() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mSensorManager.unregisterListener(this);
    }

    int msgId = 0;

    public void playMp3(String url, TraMessage message, ImageView v, AnimationDrawable aDrawable) {

        voiceViewMessage.resumeImageView();
        voiceViewMessage.message = message;
        voiceViewMessage.imageView = v;
        try {
            if (msgId == message.getId()) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    aDrawable.stop();
                    return;
                }
            }
            voiceViewMessage.imageView.setImageDrawable(aDrawable);
            aDrawable.start();

            mediaPlayer.reset();
            mediaPlayer.setDataSource(message.getUrl());
            // mediaPlayer.setVolume(AudioManager.STREAM_ALARM,
            // AudioManager.FLAG_PLAY_SOUND);
            mediaPlayer.prepare();
            mediaPlayer.start();
            msgId = message.getId();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    onCompletion(mediaPlayer);
                }
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (voiceViewMessage.imageView != null) {
            if (voiceViewMessage.message.getMsg_type() == 1) {
                voiceViewMessage.imageView
                        .setImageResource(R.drawable.voice_send_play);
            } else {
                voiceViewMessage.imageView
                        .setImageResource(R.drawable.voice_from_play);
            }
        }
        voiceViewMessage.message = null;
        voiceViewMessage.imageView = null;
    }

    public boolean isPlaying(TraMessage message) {
        return message == voiceViewMessage.message;

    }

    public void setPlayingView(ImageView imageView) {
        voiceViewMessage.imageView = imageView;
    }

    public static class VoiceViewMessage {
        public TraMessage message;
        public ImageView imageView;

        public void resumeImageView() {
            if (message == null || imageView == null) {
                return;
            }
            if (message.getMsg_type() == 1) {
                imageView.setImageResource(R.drawable.voice_send_play);
            } else {
                imageView.setImageResource(R.drawable.voice_from_play);
            }
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        float range = event.values[0];
        if (mediaPlayer != null) {
            if (range == mSensor.getMaximumRange()) {
                LOGManager.d("正常开始");
//                operPlayer(AudioManager.STREAM_ALARM);
                LOGManager.d("正常结束");
            } else {
                LOGManager.d("听筒开始");
//                operPlayer(AudioManager.STREAM_VOICE_CALL);
                LOGManager.d("听筒结束");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }
}
