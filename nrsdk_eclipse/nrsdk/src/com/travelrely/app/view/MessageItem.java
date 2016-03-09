
package com.travelrely.app.view;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.travelrely.app.activity.MessageActivity;
import com.travelrely.app.adapter.MessageListViewAdapter;
import com.travelrely.app.view.SysListAlert.OnListAlertClickListener;
import com.travelrely.core.glms.ProgressOverlay;
import com.travelrely.core.glms.ProgressOverlay.OnProgressEvent;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.Res;
import com.travelrely.core.util.AESUtils;
import com.travelrely.core.util.FetchTokenOneTask;
import com.travelrely.core.util.FileUtil;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.TimeUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.model.ContactModel;
import com.travelrely.model.ContactModel.TagNumber;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.ContactDBHelper;
import com.travelrely.v2.db.TravelrelyMessageDBHelper;
import com.travelrely.v2.db.TripInfoDBHelper;
import com.travelrely.v2.model.CheckTextNumModel;
import com.travelrely.v2.model.LocationModel;
import com.travelrely.v2.response.GetGroupMsg;
import com.travelrely.v2.response.TraMessage;
import com.travelrely.v2.response.TripInfo;
import com.travelrely.v2.response.TripInfoList;
import com.travelrely.v2.response.TripInfoList.ActivityList;
import com.travelrely.v2.response.TripInfoList.Daylist;
import com.travelrely.v2.service.CallAlarm;

public class MessageItem extends BaseLinearLayout implements TraMessage.OnImgDownloadListener,
        OnClickListener, OnLongClickListener {

    // public static MessageListsActivity.MessageItemReceiver mItemReceiver;

    TextView tvNicknameFrom;

    View to;
    View from;
    TextView from_message;
    TextView to_message;
    ImageView from_message_img;
    ImageView to_message_img;
    ImageView from_head, to_head;
    TextView time;
    View layout_time;

    View to_alarm;
    View from_alarm;
    TextView to_alarm_nume, to_alarm_data, to_alarm_time;
    TextView from_alarm_nume, from_alarm_date, from_alarm_time;
    Button from_alarm_accept;
    ImageView alarmSendState;
    View from_alarm_centre;

    View to_location;
    View from_location;
    TextView to_location_tv, from_location_tv;
    ImageView locationSendState;

    View from_trip;
    View to_trip;
    TextView from_trip_title, tv_from_name_trip_date, tv_from_name_trip_time,
            tv_from_name_trip_alarm, tv_from_name_trip_address;
    TextView to_trip_title, tv_to_name_trip_date, tv_to_name_trip_time, tv_to_name_trip_alarm,
            tv_to_name_trip_address;
    Button from_msg_type_below_bt;
    ImageView tripSendState;

    View from_location_set;
    View to_location_set;
    TextView from_location_set_title, from_location_set_context, from_location_set_address,
            from_location_set_time;
    TextView to_location_set_title, to_location_set_context, to_location_set_check,
            to_location_set_time, to_location_set_address;
    Button from_location_set_check;
    ImageView locationSetSendState;

    View from_reception;
    View to_reception;
    TextView from_reception_date, from_reception_time;
    TextView to_reception_date, to_reception_time;
    Button to_reception_check;
    ImageView receptionSetSendState;
    Button fromCheckBt;

    View from_radar;
    View to_radar;
    Button from_radar_send_location_bt;

    View from_convene, to_convene;
    TextView tv_from_convene_name, tv_from_convene_time, tv_to_convene_name, tv_to_convene_time;
    ImageView conveneSetSendState;

    public Handler otherThreadHandler;

    Handler uiHandler;

    public Context mContext;

    public TraMessage messages;

    String[] from_alarm_msg;

    String[] to_alarm_msg;

    Date nowDate = Calendar.getInstance().getTime();

    int position;
    MessageListViewAdapter messageListViewAdapter;
    

    public MessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        uiHandler = new Handler();

        // if(mItemReceiver == null){
        // mItemReceiver = new MessageListsActivity.MessageItemReceiver();
        // IntentFilter intentFilter = new IntentFilter();
        // intentFilter.addAction(MessageListsActivity.MessageItemReceiver.action);
        // mContext.registerReceiver(mItemReceiver, intentFilter);
        // }
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
    }

    @SuppressLint("NewApi")
    public void setUI(final TraMessage message, final int positions,
            MessageListViewAdapter messageListViewAdapter) {

        this.messageListViewAdapter = messageListViewAdapter;
        messages = message;
        position = positions;

        String data_time = messages.getTime();
//        System.out.println("data_time" + data_time +"msg.gettime()");
        try {
            if (data_time != null) {
                // data_time = Utils.timeFormat(message.getTime(),Utils.hm);
                data_time = TimeUtil.getChatTime(messages.getTime(), TimeUtil.dateFormat2);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        to = findViewById(R.id.to);
        from = findViewById(R.id.from);

        tvNicknameFrom = (TextView) findViewById(R.id.tv_msg_nickname_from);

        from_message = (TextView) findViewById(R.id.from_message);
        from_message.setOnLongClickListener(this);
        to_message = (TextView) findViewById(R.id.to_message);
        to_message.setOnLongClickListener(this);
        from_message_img = (ImageView) findViewById(R.id.from_message_img);
        from_message_img.setOnLongClickListener(this);
        to_message_img = (ImageView) findViewById(R.id.to_message_img);
        to_message_img.setOnLongClickListener(this);
        from_head = (ImageView) findViewById(R.id.from_head);
        to_head = (ImageView) findViewById(R.id.to_head);

        time = (TextView) findViewById(R.id.tv_time);
        layout_time = findViewById(R.id.layout_msg_time);
        View fromVoiceItem = findViewById(R.id.send_voice_item);
        View toVoiceItem = findViewById(R.id.from_voice_item);

        if (data_time != null) {
            layout_time.setVisibility(View.VISIBLE);
        } else {
            layout_time.setVisibility(View.INVISIBLE);
        }

        from_message.setOnClickListener(this);
        to_message.setOnClickListener(this);
        from_message_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showBigImage(message, 2);
            }
        });
        to_message_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showBigImage(message, 1);
            }
        });
        from_head.setOnClickListener(this);
        to_head.setOnClickListener(this);

        to_alarm = findViewById(R.id.layout_alarm_right);
        from_alarm = findViewById(R.id.layout_alarm_left);
        to_alarm.setOnLongClickListener(this);
        from_alarm.setOnLongClickListener(this);

        to_alarm_nume = (TextView) findViewById(R.id.tv_to_alarm_name);
        to_alarm_data = (TextView) findViewById(R.id.tv_to_date);
        to_alarm_time = (TextView) findViewById(R.id.tv_to_alarm_time);
        from_alarm_centre = findViewById(R.id.layout_alarm_from_centre);
        alarmSendState = (ImageView) findViewById(R.id.alarm_send_state);

        alarmSendState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialogRepeat(mContext, message);
            }
        });

        from_alarm_nume = (TextView) findViewById(R.id.tv_from_name);
        from_alarm_date = (TextView) findViewById(R.id.tv_from_date);
        from_alarm_time = (TextView) findViewById(R.id.tv_from_time);
        from_alarm_accept = (Button) findViewById(R.id.bt_from_accept);
        from_alarm_accept.setOnClickListener(this);

        from_location_tv = (TextView) findViewById(R.id.location_address_from);
        from_location = findViewById(R.id.location_layout_from);
        from_location.setOnLongClickListener(this);

        to_location = findViewById(R.id.location_layout_to);
        to_location.setOnLongClickListener(this);
        to_location_tv = (TextView) findViewById(R.id.location_address_to);

        locationSendState = (ImageView) findViewById(R.id.location_send_state);

        locationSendState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRepeat(mContext, message);
            }
        });

        to_location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showLocation(positions);
            }
        });
        from_location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showLocation(positions);
            }
        });

        from_trip = findViewById(R.id.layout_trip_from);
        to_trip = findViewById(R.id.layout_trip_to);
        to_trip_title = (TextView) findViewById(R.id.tv_to_trip_title);
        tv_to_name_trip_date = (TextView) findViewById(R.id.tv_to_trip_time);
        tv_to_name_trip_time = (TextView) findViewById(R.id.tv_to_name_trip_clock);
        tv_to_name_trip_alarm = (TextView) findViewById(R.id.tv_to_name_trip_alarm);
        tv_to_name_trip_address = (TextView) findViewById(R.id.tv_to_name_trip_address);

        from_trip_title = (TextView) findViewById(R.id.tv_from_trip_title);
        tv_from_name_trip_date = (TextView) findViewById(R.id.tv_from_trip_time);
        tv_from_name_trip_time = (TextView) findViewById(R.id.tv_from_name_trip_clock);
        tv_from_name_trip_alarm = (TextView) findViewById(R.id.tv_from_name_trip_alarm);
        tv_from_name_trip_address = (TextView) findViewById(R.id.tv_from_name_trip_address);

        from_msg_type_below_bt = (Button) findViewById(R.id.bt_from_trip_accept);
        tripSendState = (ImageView) findViewById(R.id.trip_send_state);
        from_msg_type_below_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    saveTripCalendar(message);
                    tripButtonDown();
                } catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(mContext,
                            mContext.getResources().getString(R.string.tv_add_trip_calendar_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        from_location_set = findViewById(R.id.layout_location_set_from);
        to_location_set = findViewById(R.id.layout_location_set_to);

        from_location_set_title = (TextView) findViewById(R.id.tv_from_name_location_title);
        from_location_set_context = (TextView) findViewById(R.id.tv_from_name_location_context);
        from_location_set_check = (Button) findViewById(R.id.tv_from_name_location_check);
        from_location_set_check.setOnClickListener(this);
        from_location_set_address = (TextView) findViewById(R.id.tv_from_location_address);
        from_location_set_time = (TextView) findViewById(R.id.tv_from_name_location_time);
        to_location_set_title = (TextView) findViewById(R.id.tv_to_name_location_title);
        to_location_set_context = (TextView) findViewById(R.id.tv_to_name_location_context);
        to_location_set_check = (TextView) findViewById(R.id.tv_to_name_location_check);
        to_location_set_check.setOnClickListener(this);
        to_location_set_address = (TextView) findViewById(R.id.tv_to_location_address);
        to_location_set_time = (TextView) findViewById(R.id.tv_to_name_location_time);
        locationSetSendState = (ImageView) findViewById(R.id.location_set_send_state);
        locationSetSendState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialogRepeat(mContext, message);
            }
        });

        from_reception = findViewById(R.id.layout_reception_from);
        to_reception = findViewById(R.id.layout_reception_to);

        from_reception_date = (TextView) findViewById(R.id.tv_reception_date);
        from_reception_time = (TextView) findViewById(R.id.tv_reception_time);
        fromCheckBt = (Button) findViewById(R.id.layout_reception_bt);

        to_reception_date = (TextView) findViewById(R.id.tv_reception_to_date);
        to_reception_time = (TextView) findViewById(R.id.tv_reception_to_time);
        to_reception_check = (Button) findViewById(R.id.tv_to_reception_check);
        receptionSetSendState = (ImageView) findViewById(R.id.reception_set_send_state);
        receptionSetSendState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialogRepeat(mContext, message);
            }
        });
        to_reception_check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getGroupReception(message.getFrom(), message);
            }
        });

        to_radar = findViewById(R.id.layout_radar_to);
        from_radar = findViewById(R.id.layout_radar_from);
        from_radar_send_location_bt = (Button) findViewById(R.id.bt_send_location_msg);
        from_radar_send_location_bt.setOnClickListener(this);

        to_convene = findViewById(R.id.layout_convene_right);
        from_convene = findViewById(R.id.layout_convene_left);
        tv_to_convene_name = (TextView) findViewById(R.id.tv_to_name);
        tv_to_convene_time = (TextView) findViewById(R.id.tv_to_time);
        tv_from_convene_name = (TextView) findViewById(R.id.tv_from_name_convene);
        tv_from_convene_time = (TextView) findViewById(R.id.tv_from_time_convene);

        conveneSetSendState = (ImageView) findViewById(R.id.convene_send_state);
        conveneSetSendState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialogRepeat(mContext, message);
            }
        });

        setTag(message);

        if (message.getFrom_type() == 0 || message.getFrom_type() == 1) {
            from_head.setImageBitmap(Utils.drawableToBitmap(mContext.getResources().getDrawable(
                    R.drawable.tra_msg_icon)));
        } 
        else if(message.isGroup()){
            ContactModel cModel = ContactDBHelper.getInstance().getContactByNumberTry(message.getTo());
            if(cModel != null){
                Bitmap bitmap = FileUtil.getBitmpHead(cModel.getHeadPortrait() + "_s");
                from_head.setImageBitmap(bitmap);
            }else{
                from_head.setImageBitmap(Utils.drawableToBitmap(mContext.getResources().getDrawable(
                        R.drawable.default_icon)));
            }
        }
        else {
            from_head.setImageBitmap(message.getHeadBitmap(getContext()));
        }
        if (Engine.getInstance().isHeadImg) {
            Bitmap bitmap = FileUtil.getBitmpHead(Engine.getInstance().getUserInfo().getData()
                    .getPersonal_info().getHeadPortrait()
                    + "_s");
            to_head.setImageBitmap(bitmap);
        } else {
            to_head.setImageBitmap(message.getHeadBitmap(getContext()));
        }

        if (message.getNick_name() != null) {
            tvNicknameFrom.setText(message.getNick_name());
        } else {
            tvNicknameFrom.setText(message.getFrom());
        }

        if (message.getIs_nickname() == 0) {
            tvNicknameFrom.setVisibility(View.INVISIBLE);
        } else {
            tvNicknameFrom.setVisibility(View.VISIBLE);
        }

        if (message.getMsg_type() == 1) {
            time.setText(data_time);
            from.setVisibility(View.GONE);
            to.setVisibility(View.VISIBLE);
            if (message.getContent().length() > 0) {
                to_head.setVisibility(View.VISIBLE);
            } else {
                to_head.setVisibility(View.GONE);
            }
            if (message.isText()) {
                String content = message.getDecryptContent();
                if (content == null) {
                    to_message.setVisibility(View.GONE);
                    time.setVisibility(View.GONE);
                } else {
                    time.setVisibility(View.VISIBLE);
                    fromVoiceItem.setVisibility(View.GONE);
                    toVoiceItem.setVisibility(View.GONE);
                    to_message.setVisibility(View.VISIBLE);
                    to_message.setText(content);
                    to_message_img.setVisibility(View.GONE);
                    to_alarm.setVisibility(View.GONE);
                    to_location.setVisibility(View.GONE);
                    to_trip.setVisibility(View.GONE);
                    to_location_set.setVisibility(View.GONE);
                    to_reception.setVisibility(View.GONE);
                    to_radar.setVisibility(View.GONE);
                    to_convene.setVisibility(View.GONE);
                    if (Utils.isSmilies(to_message.getText().toString())) {
                        setSmile(to_message.getText().toString(), to_message);
                    }
                    // sendMessageType(mContext, messages,
                    // receptionSetSendState);
                }
            } else if (message.isJPG()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                to_message_img
                        .setImageBitmap(message.getSmallBitmap(getContext(), message.getUrl()));
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.VISIBLE);
                to_alarm.setVisibility(View.GONE);
                to_location.setVisibility(View.GONE);
                to_trip.setVisibility(View.GONE);
                to_location_set.setVisibility(View.GONE);
                to_reception.setVisibility(View.GONE);
                to_radar.setVisibility(View.GONE);
                to_convene.setVisibility(View.GONE);
                // sendMessageType(mContext, messages);
            } else if (message.isLocation()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.GONE);
                to_alarm.setVisibility(View.GONE);
                to_location.setVisibility(View.VISIBLE);
                to_location_tv.setText(message.getDecryptContentLocation());
                to_trip.setVisibility(View.GONE);
                to_location_set.setVisibility(View.GONE);
                to_reception.setVisibility(View.GONE);
                to_radar.setVisibility(View.GONE);
                to_convene.setVisibility(View.GONE);
                sendMessageType(mContext, messages, locationSendState);
            } else if (message.isAlarm()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                to_alarm_msg = message.getDecryptContent().split("\t");
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.GONE);
                to_location.setVisibility(View.GONE);
                to_alarm.setVisibility(View.VISIBLE);
                to_trip.setVisibility(View.GONE);
                to_location_set.setVisibility(View.GONE);
                to_reception.setVisibility(View.GONE);
                to_radar.setVisibility(View.GONE);
                to_alarm_data.setText(to_alarm_msg[0]);
                to_alarm_time.setText(to_alarm_msg[1]);
                to_alarm_nume.setText(to_alarm_msg[2]);
                sendMessageType(mContext, messages, alarmSendState);
                to_convene.setVisibility(View.GONE);

                if (message.getAlarm_type() == 0 && message.getAlarm_on_off() == 0) {
                    try {
                        setAlarm(to_alarm_time.getText().toString(), to_alarm_data.getText()
                                .toString(), to_alarm_msg);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            } else if (message.isMp3()) {
                fromVoiceItem.setVisibility(View.VISIBLE);
                toVoiceItem.setVisibility(View.GONE);
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.GONE);
                to_alarm.setVisibility(View.GONE);
                to_location.setVisibility(View.GONE);
                View v = fromVoiceItem.findViewById(R.id.voice_layout);
                TextView time = (TextView) fromVoiceItem.findViewById(R.id.voice_time);
                setTimeText(time, message);
                v.setTag(message);
                v.setOnClickListener(this);
                v.setOnLongClickListener(this);
                View length = fromVoiceItem.findViewById(R.id.length);
                setVoiceLength(length, message, true);
                ImageView imageView = (ImageView) fromVoiceItem.findViewById(R.id.voiceAni);
                if (messageListViewAdapter.isPlaying(message)) {
                    imageView.setImageDrawable(toVoiceAniDrawable);
                    toVoiceAniDrawable.stop();
                    toVoiceAniDrawable.start();
                    messageListViewAdapter.setPlayingView(imageView);
                } else {
                    imageView.setImageDrawable(toVoice);
                }
                imageView.setTag(message);
                to_message.setTag(message);
                to_trip.setVisibility(View.GONE);
                to_location_set.setVisibility(View.GONE);
                to_reception.setVisibility(View.GONE);
                to_radar.setVisibility(View.GONE);
                to_convene.setVisibility(View.GONE);
                // sendMessageType(mContext, messages);
            } else if (message.isTrip()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.GONE);
                to_alarm.setVisibility(View.GONE);
                to_location.setVisibility(View.GONE);
                to_trip.setVisibility(View.VISIBLE);
                to_trip_title.setText("111\n22\t3");
                to_location_set.setVisibility(View.GONE);
                to_reception.setVisibility(View.GONE);
                to_radar.setVisibility(View.GONE);
                setTripContextTo(message, to_trip_title, tv_to_name_trip_date,
                        tv_to_name_trip_time, tv_to_name_trip_alarm, tv_to_name_trip_address);
                sendMessageType(mContext, messages, tripSendState);
                to_convene.setVisibility(View.GONE);
            } else if (message.isColltion()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.GONE);
                to_alarm.setVisibility(View.GONE);
                to_location.setVisibility(View.GONE);
                to_trip.setVisibility(View.GONE);
                to_location_set.setVisibility(View.VISIBLE);
                to_reception.setVisibility(View.GONE);
                to_radar.setVisibility(View.GONE);
                to_convene.setVisibility(View.GONE);
                setToLocationSetCotext(message);
                sendMessageType(mContext, messages, locationSetSendState);
            } else if (message.isRegistration()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.GONE);
                to_alarm.setVisibility(View.GONE);
                to_location.setVisibility(View.GONE);
                to_trip.setVisibility(View.GONE);
                to_location_set.setVisibility(View.GONE);
                to_reception.setVisibility(View.VISIBLE);
                to_radar.setVisibility(View.GONE);
                to_convene.setVisibility(View.GONE);
                setToReception(message);
                sendMessageType(mContext, messages, receptionSetSendState);
            } else if (message.isRadar()) {
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.GONE);
                to_alarm.setVisibility(View.GONE);
                to_location.setVisibility(View.GONE);
                to_trip.setVisibility(View.GONE);
                to_location_set.setVisibility(View.GONE);
                to_reception.setVisibility(View.GONE);
                to_radar.setVisibility(View.VISIBLE);
                to_convene.setVisibility(View.GONE);
                // sendMessageType(mContext, messages);
            } else if (message.isConvene()) {
                String[] msg = message.getDecryptContentConvene();
                to_message.setVisibility(View.GONE);
                to_message_img.setVisibility(View.GONE);
                to_alarm.setVisibility(View.GONE);
                to_location.setVisibility(View.GONE);
                to_trip.setVisibility(View.GONE);
                to_location_set.setVisibility(View.GONE);
                to_reception.setVisibility(View.GONE);
                to_radar.setVisibility(View.GONE);
                to_convene.setVisibility(View.VISIBLE);
                tv_to_convene_name.setText(msg[1]);
                tv_to_convene_time.setText(msg[0]);
                sendMessageType(mContext, messages, conveneSetSendState);
            }

        } else {
//        	System.out.println("lzw"+data_time+" msg_type==2");
        	time.setVisibility(View.VISIBLE);
            time.setText(data_time);
            from.setVisibility(View.VISIBLE);
            to.setVisibility(View.GONE);
            if (message.isText()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                from_message.setText(message.getDecryptContent());
                from_message_img.setVisibility(View.GONE);
                from_message.setVisibility(View.VISIBLE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(View.GONE);
                from_trip.setVisibility(View.GONE);
                from_location_set.setVisibility(View.GONE);
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_convene.setVisibility(View.GONE);
                if (Utils.isSmilies(from_message.getText().toString())) {
                    setSmile(from_message.getText().toString(), from_message);
                }
                if (message.getFrom_type() == 0) {
                    checkContacts(mContext, message, from_message);
                }
            } else if (message.isJPG()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                from_message_img.setImageBitmap(message.getSmallBitmap(getContext(),
                        message.getUrl()));
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.VISIBLE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(View.GONE);
                from_trip.setVisibility(View.GONE);
                from_location_set.setVisibility(View.GONE);
                from_reception.setVisibility(View.GONE);
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
            } else if (message.isLocation()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.GONE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(VISIBLE);
                from_trip.setVisibility(GONE);
                from_location_tv.setText(message.getDecryptContentLocation());
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
            } else if (message.isAlarm()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                from_alarm_msg = message.getDecryptContent().split("\t");
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.GONE);
                from_location.setVisibility(View.GONE);
                from_alarm.setVisibility(View.VISIBLE);
                from_trip.setVisibility(View.GONE);
                from_location_set.setVisibility(View.GONE);
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
                from_alarm_date.setText(from_alarm_msg[0]);
                from_alarm_time.setText(from_alarm_msg[1]);
                from_alarm_nume.setText(from_alarm_msg[2]);
                if (message.getAlarm_type() == 0) {
                    from_alarm_accept.setVisibility(View.VISIBLE);
                    from_alarm_centre.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.send_msg_type_centre));
                }
                if (message.getAlarm_type() == 1) {
                    from_alarm_accept.setVisibility(View.GONE);
                    from_alarm_centre.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.send_msg_type_below));
                }
                if (message.getAlarm_type() == 2) {
                    from_alarm_accept.setVisibility(View.GONE);
                    from_alarm_centre.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.send_msg_type_below));
                }
            } else if (message.isMp3()) {
                from_message.setText(message.getDecryptContent());
                // 收到消息
                fromVoiceItem.setVisibility(View.GONE);
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.GONE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(GONE);
                from_message.setTag(message);
                from_trip.setVisibility(View.GONE);
                from_location_set.setVisibility(View.GONE);
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.VISIBLE);
                View v = toVoiceItem.findViewById(R.id.voice_layout);
                ImageView imageView = (ImageView) toVoiceItem.findViewById(R.id.voiceAni);
                View length = toVoiceItem.findViewById(R.id.length);

                if (messageListViewAdapter.isPlaying(message)) {
                    imageView.setImageDrawable(fromVoiceAniDrawable);
                    fromVoiceAniDrawable.stop();
                    fromVoiceAniDrawable.start();
                    messageListViewAdapter.setPlayingView(imageView);
                } else {
                    imageView.setImageDrawable(fromVoice);
                }
                imageView.setTag(message);
                TextView time = (TextView) toVoiceItem
                        .findViewById(R.id.voice_time);
                setTimeText(time, message);
                setVoiceLength(length, message, false);
                v.setTag(message);
                v.setOnClickListener(this);
                v.setOnLongClickListener(this);
            } else if (message.isTrip()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.GONE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(View.GONE);
                from_trip.setVisibility(View.VISIBLE);
                from_location_set.setVisibility(View.GONE);
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
                try {
                    setTripContextTo(message, from_trip_title, tv_from_name_trip_date,
                            tv_from_name_trip_alarm, tv_from_name_trip_time,
                            tv_from_name_trip_address);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else if (message.isColltion()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.GONE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(View.GONE);
                from_trip.setVisibility(View.GONE);
                from_location_set.setVisibility(View.VISIBLE);
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
                setFromLocationSetCotext(message);
            } else if (message.isRegistration()) {
                fromVoiceItem.setVisibility(View.GONE);
                toVoiceItem.setVisibility(View.GONE);
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.GONE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(View.GONE);
                from_trip.setVisibility(View.GONE);
                from_location_set.setVisibility(View.GONE);
                from_reception.setVisibility(View.VISIBLE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
                try {
                    setFromReception(message);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (message.isRadar()) {
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.GONE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(View.GONE);
                from_trip.setVisibility(View.GONE);
                from_location_set.setVisibility(View.GONE);
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.VISIBLE);
                from_radar_send_location_bt.setVisibility(View.VISIBLE);
            } else if (message.isConvene()) {
                String[] msg = message.getDecryptContentConvene();
                from_message.setVisibility(View.GONE);
                from_message_img.setVisibility(View.GONE);
                from_alarm.setVisibility(View.GONE);
                from_location.setVisibility(View.GONE);
                from_trip.setVisibility(View.GONE);
                from_location_set.setVisibility(View.GONE);
                from_reception.setVisibility(View.GONE);
                from_radar.setVisibility(View.GONE);
                from_radar_send_location_bt.setVisibility(View.GONE);
                from_convene.setVisibility(View.VISIBLE);
                tv_from_convene_name.setText(msg[1]);
                tv_from_convene_time.setText(msg[0]);
            }
        }
    }

    @Override
    public void onDownLoading() {

    }

    @Override
    public void onDownLoaded(final TraMessage message, final Bitmap bitmap) {

        uiHandler.post(new Runnable() {

            @Override
            public void run() {
                if (message == getTag()) {
                    if (message.getMsg_type() == 1) {
                        to_message_img.setImageBitmap(bitmap);
                    } else {
                        from_message_img.setImageBitmap(bitmap);
                    }
                }
            }
        });

    }

    public void replaceLocationgBitmap(final TraMessage message, final Bitmap bitmap) {

        uiHandler.post(new Runnable() {

            @Override
            public void run() {

                if (message.locationBitmap != null) {
                    message.locationBitmap.recycle();
                    message.locationBitmap = bitmap;
                }
            }
        });

    }

    AnimationDrawable fromVoiceAniDrawable, toVoiceAniDrawable;
    Drawable fromVoice, toVoice;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        fromVoiceAniDrawable = (AnimationDrawable) getContext().getResources()
                .getDrawable(R.drawable.from_voice_playing);
        toVoiceAniDrawable = (AnimationDrawable) getContext().getResources()
                .getDrawable(R.drawable.send_voice_playing);
        fromVoice = getContext().getResources().getDrawable(
                R.drawable.voice_from_play);
        toVoice = getContext().getResources().getDrawable(
                R.drawable.voice_send_play);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
		if (id == R.id.from_head) {
			int i = messages.getFrom_type();
			if (messages.getFrom_type() == 2 || messages.getFrom_type() == 3) {
			    if (messages.isGroup()) {
			        goDetails(messages.getTo(), messages);
			    } else {
			        goDetails(messages.getFrom(), messages);
			    }
			}
		} else if (id == R.id.to_head) {
			goDetails(messages.getTo(), null);
		} else if (id == R.id.bt_from_accept) {
			try {
			    setAlarm(from_alarm_time.getText().toString(), from_alarm_date.getText()
			            .toString(), from_alarm_msg);
			} catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		} else if (id == R.id.voice_layout) {
			TraMessage message = (TraMessage) v.getTag();
			if (message != null && message.isMp3()) {
			    ImageView img = (ImageView) v.findViewById(R.id.voiceAni);
			    if (message.getMsg_type() == 1) {
			        playMp3(message.getUrl(), message, img, toVoiceAniDrawable);
			    } else {
			        playMp3(message.getUrl(), message, img, fromVoiceAniDrawable);
			    }
			}
		} else if (id == R.id.tv_from_name_location_check) {
			checkMapLoctionMap(messages);
		} else if (id == R.id.tv_to_name_location_check) {
			checkMapLoctionMap(messages);
		} else if (id == R.id.bt_send_location_msg) {
			try {
				Toast.makeText(mContext,
			            getResources().getString(R.string.tv_googlemap_no),
			            Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
			    // TODO: handle exception
			    e.printStackTrace();
			}
		} else {
		}
    }

    MediaPlayer mediaPlayer;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    private void playMp3(String url, TraMessage message, ImageView v, AnimationDrawable anDrawable) {
        messageListViewAdapter.playMp3(url, message, v, anDrawable);
    }

    public void goDetails(String number, TraMessage message) {

        ContactDBHelper dHelper = ContactDBHelper.getInstance();
        // String userName =
        // MessageListViewAdapter.list.get(position).getUser_name();
        ContactModel contactModel = dHelper.getContactByNumber(number, "new_num");

        LOGManager.d("*****查询号码 = " + number);
        LOGManager.d("*****查询数据库 = " + contactModel);
        
        if (contactModel != null) {

//            goContactDetailActivity(contactModel);
            LOGManager.d("*****直接跳转 = " + number);
        } else {
            if (message != null) {
                LOGManager.d("******添加联系人 = " + number);
                addContact(number, message);
            }
        }
    }

    public void showBigImage(TraMessage message, int msgType) {

//        List<String> list = selectMsgImg(message, msgType);
//        Intent intent = new Intent(mContext, PhotoViewAct.class);
//        Bundle b = new Bundle();
//        b.putStringArrayList("MSG_IMG", (ArrayList<String>) list);
//        intent.putExtras(b);
//        mContext.startActivity(intent);
    	// TODO cwj
    };

    /**
     * 用来查询user消息所有图片资源
     * 
     * @param posion
     */
    private List<String> selectMsgImg(TraMessage message, int msgType) {

        TravelrelyMessageDBHelper tDbHelper = TravelrelyMessageDBHelper.getInstance();
        List<String> list = tDbHelper.selectMsgImg(message.getId(), message.getFrom(),
                message.getType(), msgType);
        return list;
    }

    public void showLocation(int posion) {
//        Intent intent = new Intent(mContext, ShowLocationActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("positions", posion);
//        intent.putExtras(bundle);
//        mContext.startActivity(intent);
    	// TODO cwj
    }

    private void setAlarm(String alarm_time, String alarm_data, String[] alarm_msg)
            throws Exception {

        String[] time = alarm_time.split(":");
        String[] date = alarm_data.split("-");

        long alarmTimes = Utils.ConverToDate(
                alarm_data + " " + alarm_time).getTime();

        Date dates = new Date(alarmTimes);
        Calendar c = Calendar.getInstance();
        c.setTime(dates);
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(time[1]));
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date alarmTime = c.getTime();
        long timeLong = alarmTime.getTime() - nowDate.getTime();

        if (timeLong > 0) {
            // 定闹钟设定时间到时要执行CallAlarm.class
            refreshAlarmMsg(messages, 1, 1);
            Intent intent = new Intent(mContext, CallAlarm.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("alarm_msg", alarm_msg);
            // bundle.putSerializable("message", messages);
            bundle.putInt("position", position);
            intent.putExtras(bundle);
            PendingIntent sender = PendingIntent.getBroadcast(mContext, messages.getId(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
            // set()设定的PendingIntent只会执行一次
            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
        } else {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.alarmTimeout),
                    Toast.LENGTH_LONG).show();
            refreshAlarmMsg(messages, 2, 1);
        }
    }

    private void saveMessageAndUpdate(final TraMessage message) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction(IAction.MSM_ACTION);
                mContext.sendBroadcast(intent);
            }
        });
    }

    private void refreshAlarmMsg(TraMessage msg, int type, int on_off) {

        from_alarm_accept.setVisibility(View.GONE);
        TravelrelyMessageDBHelper.getInstance().updateColumn(msg, type, on_off);
        saveMessageAndUpdate(msg);
    }

    private void setSmile(String s, TextView v) {

        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m = pattern.matcher(s);
        SpannableString spannableString = new SpannableString(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String str = m.group();
            int count = 0;
            if (str != null) {
                count = Integer.valueOf(str.substring(str.length() - 3, str.length() - 1));
            }
            ImageSpan span = new ImageSpan(mContext, Res.expressionImgs[count - 10]);
            // spannableString.setSpan(new ForegroundColorSpan(Color.RED),
            // start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//高亮字
            spannableString.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        v.setText(spannableString);
    }

    @Override
    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        String[] items = {
                getResources().getString(R.string.delete), getResources().getString(R.string.copy)
        };
        String[] items2 = {
                getResources().getString(R.string.delete)
        };
        int id = v.getId();
		if (id == R.id.to_message) {
			if (!Utils.isSmilies(to_message.getText().toString())) {
			    showDialog(items);
			} else {
			    showDialog(items2);
			}
		} else if (id == R.id.from_message) {
			if (!Utils.isSmilies(to_message.getText().toString())) {
			    showDialog(items);
			} else {
			    showDialog(items2);
			}
		} else {
			showDialog(items2);
		}
        return false;
    }

    /**
     * 显示选择对话框
     */
    private void showDialog(String[] item) {

        new AlertDialog.Builder(mContext)
                .setTitle("消息选项")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                boolean flag = TravelrelyMessageDBHelper.getInstance()
                                        .deleteMessage(messages.getId());
                                if (messages.getUrl() != null) {
                                    FileUtil fileUtil = new FileUtil(mContext);
                                    fileUtil.deleteFile(messages.getUrl());
                                }
                                if (flag) {
                                    Intent intent = new Intent();
                                    intent.setAction(IAction.MSM_ACTION);
                                    mContext.sendBroadcast(intent);
                                    Toast.makeText(mContext,
                                            mContext.getResources().getString(R.string.delSucc),
                                            Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                String string = messages.getDecryptContent();
                                copy(string, mContext);
                                Toast.makeText(mContext,
                                        mContext.getResources().getString(R.string.textCopied),
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 接收签到有异常，年后需要做的
     * 
     * @param message
     * @throws ParseException
     */

    private void setFromReception(final TraMessage message) throws ParseException {

        if (message.getContent() != null) {
            final String[] content = message.getDecryptContentReception();
            final String id = content[1];
            String[] startTime = content[1].split(" ");
            String[] endTime = content[2].split(" ");
            if (message.getReception_on_off() == 1) {
                setReceptionBt(1);
            }
            if (!Utils.nowTimeCompare(content[2], Utils.y_m_d_h_m)) {
                setReceptionBt(2);
            }
            if (message.getReception_on_off() == 0) {
                setReceptionBtNormal();
            }

            from_reception_date.setText(getResources().getString(R.string.tv_validity_to)
                    + endTime[0]);
            from_reception_time.setText(endTime[1]);
            fromCheckBt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // startLocationGoogle(position);
                    sendReceptionRequest(mContext, message);
                    setReceptionBt(1);
                }
            });
        }
    }

    private void setToReception(TraMessage message) {

        if (message.getContent() != null) {

            String[] content = message.getDecryptContentReception();
            String[] startTime = content[1].split(" ");
            String[] endTime = content[2].split(" ");

            to_reception_date
                    .setText(getResources().getString(R.string.tv_validity_to) + endTime[0]);
            to_reception_time.setText(endTime[1]);

        }
    }

    /**
     * @param type=1 已签到
     * @param type=2 过期
     */
    private void setReceptionBt(int type) {

        if (type == 1) {
            fromCheckBt.setText(R.string.tv_already_reception);
        } else if (type == 2) {
            fromCheckBt.setText(R.string.tv_back_reception);
        }
        fromCheckBt.setEnabled(false);
    }

    private void setReceptionBtNormal() {
        fromCheckBt.setText(R.string.tv_i_will_reception);
        fromCheckBt.setEnabled(true);
    }

    /**
     * 获取签到群信息
     */
    private void getGroupReception(final String from, final TraMessage msg) {

        ProgressOverlay progressOverlay = new ProgressOverlay(mContext);
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                // TODO Auto-generated method stub
                GetGroupMsg getGroupMsg = Engine.getInstance().getGroupMsg(mContext, from, 0);
                if (getGroupMsg.getResponseInfo().isRet()) {

                    String[] content = msg.getDecryptContentReception();
                    String msgId = content[1];

//                    Intent intent = new Intent(mContext, ReceptionListActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("GROUP_MSG", getGroupMsg);
//                    bundle.putString("FROM", from);
//                    bundle.putString("MSG_CONTEXT", msg.getContent());
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
                    // TODO cwj
                } else {
                    Toast.makeText(mContext, R.string.tv_reception_error, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    MediaPlayer mediaPlayer2 = new MediaPlayer();

    private void setTimeText(TextView textView, TraMessage message) {
        if (message.voiceTimeLength != null) {
            textView.setText(message.voiceTimeLength);
            return;
        }
        try {
            mediaPlayer2.reset();
            mediaPlayer2.setDataSource(message.getUrl());
            mediaPlayer2.prepare();
            long time = mediaPlayer2.getDuration();
            float second = time / 1000f;
            message.voiceLength = second;
            // String result = String.format("%.1f",second);
            String result = String.valueOf((int) second);
            message.voiceTimeLength = result + "''";
            textView.setText(message.voiceTimeLength);
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText("");
        }

    }

    private void setVoiceLength(View length, TraMessage message, boolean left) {
        int time = (int) (message.voiceLength);
        LinearLayout.LayoutParams params = (LayoutParams) length
                .getLayoutParams();
        DisplayMetrics displayMetrics = length.getContext().getResources().getDisplayMetrics();
        int maxLength = (int) (130 * displayMetrics.density);
        int minLength = (int) (1 * displayMetrics.density);
        float maxTime = 30f;
        if (time > maxTime) {
            params.width = maxLength;
        } else {
            float lengthtime = time * (maxLength / maxTime);
            params.width = Math.max(minLength, (int) lengthtime);
        }
    }

    private void startLocationService(int pos, Class<?> pClass) {

        Intent intent = new Intent(mContext, pClass);
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", pos);
        intent.putExtras(bundle);
        mContext.startService(intent);
    }

    private void setFromLocationSetCotext(TraMessage message) {

        String[] content = message.getDecryptColltion();

        if (content != null) {
            try {
                String[] strDate = content[2].split("\n");
                String[] date = strDate[0].toString().split(" ");
                from_location_set_context.setText(date[0]);
                from_location_set_time.setText(date[1]);
                from_location_set_address.setText(strDate[1]);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private void setToLocationSetCotext(TraMessage message) {

        String[] content = message.getDecryptColltion();
        if (content != null) {
            String[] strDate = content[2].split("\n");
            String[] date = strDate[0].toString().split(" ");
            to_location_set_context.setText(date[0]);
            to_location_set_time.setText(date[1]);
            to_location_set_address.setText(strDate[1]);
        }
    }

    private void goGoogleMap(TraMessage msg, Class<?> pClass) {

        String[] context = msg.getDecryptColltion();
        if (context != null) {
            LocationModel lModels = new LocationModel();
            lModels.setLatitude(context[0]);
            lModels.setLongitude(context[1]);
            lModels.setContext(context[2]);
            Bundle bundle = new Bundle();
            bundle.putSerializable("LOCATION_MODEL", (Serializable) lModels);
            Intent intent = new Intent(mContext, pClass);
            intent.putExtras(bundle);
            mContext.startActivity(intent);

        }
    }

    private void setTripContextTo(TraMessage msg, TextView tvTitle, TextView date, TextView time,
            TextView alarm, TextView adds) {

        String context = AESUtils.getDecryptString(msg.getContent());
        Gson gson = new Gson();
        TripInfo trInfo = gson.fromJson(context, TripInfo.class);
        List<TripInfoList> lInfoLists = trInfo.getData().getlInfoLists();
        String title = "";
        String startDate = "";
        String endDate = "";
        String dayContent = "";
        String address = "";
        String tripId = "";
        String alarmType = "";
        String dateTime = null;
        List<Daylist> daylists;
        List<ActivityList> activityLists;
        for (TripInfoList tInfoList : lInfoLists) {
            title = tInfoList.getTripdesc();
            dayContent = String
                    .valueOf(tInfoList.getDaylists().get(0).getActivityLists().size() + 1);
            startDate = tInfoList.getDaylists().get(0).getDate().replace("-", ".");
            endDate = tInfoList.getDaylists().get(tInfoList.getDaylists().size() - 1).getDate()
                    .replace("-", ".");
            daylists = tInfoList.getDaylists();
            LOGManager.v("***********大小 = " + daylists.size());
            for (int i = 0; i < daylists.size(); i++) {
                activityLists = daylists.get(i).getActivityLists();
                LOGManager.v("***********大小2 = " + activityLists.size());
                for (int t = 0; t < activityLists.size(); t++) {
                    if (activityLists.get(t).getmAlarm().getTime() != null) {
                        if (activityLists.get(t).getmAlarm().getTime().length() > 0) {
                            alarmType = "有";
                        } else {
                            alarmType = "无";
                        }
                    } else {
                        alarmType = "无";
                    }
                }
            }

            address = tInfoList.getDaylists().get(0).getActivityLists().get(0).getPosition();
            dateTime = tInfoList.getDaylists().get(0).getActivityLists().get(0).getBegintime();
        }

        if (trInfo != null) {
            String[] tempDate = dateTime.split(":");
            String dtaeTime = startDate + "-" + endDate;
            tvTitle.setText(title);
            date.setText(dtaeTime);
            alarm.setText(getResources().getString(R.string.tv_alarm) + ": " + alarmType);
            adds.setText(getResources().getString(R.string.tv_trip_location) + ": " + address);
            time.setText(getResources().getString(R.string.trip_time) + ": " + tempDate[0] + ":"
                    + tempDate[1]);
        }

        if (msg.getMsg_type() == 2) {
            if (isHaveTrip(tripId)) {
                tripButtonDown();
            } else {
                tripButtonNormal();
            }

        }
    }

    private void tripButtonNormal() {
        from_msg_type_below_bt.setEnabled(true);
        from_msg_type_below_bt.setText(R.string.tv_add_trip_calendar);
    }

    private void tripButtonDown() {
        from_msg_type_below_bt.setEnabled(false);
        from_msg_type_below_bt.setText(R.string.tv_add_trip_calendar_ok);
    }

    private void saveTripCalendar(TraMessage msg) {

        String context = AESUtils.getDecryptString(msg.getContent());
        TripInfo gTripInfo = new TripInfo();
        gTripInfo.setValue(context);

        if (!isHaveTrip(gTripInfo.getData().getlInfoLists().get(0).getTripid())) {
            Engine.getInstance().saveTripInfo(gTripInfo);
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.tv_add_trip_calendar_ok),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext,
                    mContext.getResources().getString(R.string.tv_add_trip_calendar_have),
                    Toast.LENGTH_LONG).show();
        }

    }

    private boolean isHaveTrip(String tripId) {

        TripInfoDBHelper tHelper = TripInfoDBHelper.getInstance();
        return tHelper.isTripInf(tripId);
    }

    /**
     * 发送签到回应
     */
    private void sendReceptionRequest(Context mContext, TraMessage message) {

        String[] msgContext = AESUtils.getDecryptString(message.getContent()).split("\t");
        String sendMsg = AESUtils.getEntryString(msgContext[0]);

        TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(),
                sendMsg,
                messages.getFrom(), 8, "", 3, 1, 1, messages.getNick_name(),
                messages.getGroup_name(),
                Engine.getInstance().getUserInfo().getData().getPersonal_info().getHeadPortrait(),
                messages.getFrom_head_portrait());

        Engine.getInstance().sendMessageInBackground(mContext, message2);

        refreshReceptionMsg(message);
    }

    private void refreshReceptionMsg(TraMessage msg) {

        TravelrelyMessageDBHelper.getInstance().updateContext(msg.getFrom(),
                String.valueOf(msg.getId()), "id",
                "reception_on_off", String.valueOf(1));
    }

    /**
     * 在系统消息中，用来检测推送内容中的电话号码在手机通讯录中是否存在，若有则把姓名也显示出来
     */
    @SuppressLint("NewApi")
    private void checkContacts(final Context mContext, TraMessage message, TextView v) {

        List<CheckTextNumModel> listNum = new ArrayList<CheckTextNumModel>();
        String numName = null;
        String context = AESUtils.getDecryptString(message.getContent());
        if(context != null){
            Pattern pattern = Pattern.compile("[+(0-9)]*");
            Matcher m = pattern.matcher(context);
            StringBuffer sb = new StringBuffer();
            int end = 0;
            while (m.find()) {
                if (!m.group().isEmpty()) {
                    numName = selectContacts(mContext, m.group());

                    if (!numName.equals(m.group())) {
                        m.appendReplacement(sb, numName + "(" + m.group() + ")");
                        m.appendTail(sb);
                        end = numName.length() + 2;
                        context = sb.toString();
                        listNum.add(setCheckTextNumModel(m.group() , m.start(), m.end() + end));
                    }
                    else if (Utils.isMobileNO(numName.replace("+86", ""))) {
                        if(end == 0){
                            listNum.add(setCheckTextNumModel(m.group(), m.start(), m.end()));
                        }else{
                            listNum.add(setCheckTextNumModel(m.group(), m.start() + end, m.end() + end));
                        }
                    }
                    else if (Utils.isTel(m.group())) {
                        if(end == 0){
                            listNum.add(setCheckTextNumModel(m.group(), m.start(), m.end()));
                        }else{
                            listNum.add(setCheckTextNumModel(m.group(), m.start() + end, m.end() + end));
                        }
                    }
                }
            }
            SpannableStringBuilder style = new SpannableStringBuilder(context);
            if (listNum.size() > 0) {
                for (final CheckTextNumModel model : listNum) {
                    style.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            // TODO Auto-generated method stub
                            showCallDialog(mContext, widget, model.getNum());
                        }
                    },
                            model.getStartPosttion(), model.getEndPosttion(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    style.setSpan(
                            new ForegroundColorSpan(getResources().getColor(R.color.wathet_blue)),
                            model.getStartPosttion(), model.getEndPosttion(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    style.setSpan(new UnderlineSpan(),
                            model.getStartPosttion(), model.getEndPosttion(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            v.setText(style);
            v.setMovementMethod(LinkMovementMethod.getInstance());
            v.setFocusable(false);
            v.setClickable(false);
        }
    }

    /**
     * 遍历手机通讯录数据库
     */
    private String selectContacts(Context mContext, String number) {

        String phonename = number;

        if (number != null) {
            if (number.length() > 3) {
                String str = number.substring(0, 3);
                if (str.equals("+86")) {
                    number.replace("+86", "");
                }
            }
            Cursor c = mContext.getContentResolver().query(Uri.withAppendedPath(
                    PhoneLookup.CONTENT_FILTER_URI, number), new String[] {
                    PhoneLookup._ID,
                    PhoneLookup.NUMBER,
                    PhoneLookup.DISPLAY_NAME,
                    PhoneLookup.TYPE, PhoneLookup.LABEL
            }, null, null, null);

            if (c.getCount() == 0)
            {
                return phonename;
            } else if (c.getCount() > 0) {
                c.moveToFirst();
                phonename = c.getString(2); // 获取姓名
            }
        }
        return phonename;
    }

    /**
     * 消息发送状态，成功与非成功
     */
    private void sendMessageType(Context mContext, TraMessage message, ImageView toSendTypeImg) {

        toSendTypeImg.setImageResource(R.drawable.msg_state_sending);
        toSendTypeImg.setVisibility(View.VISIBLE);
        if (message.getCode() != null && message.getCode().equals(TraMessage.SEND_MSG_CODE_OK)) {
            toSendTypeImg.setVisibility(View.GONE);
        } else if (message.getCode() != null
                && message.getCode().equals(TraMessage.SEND_MSG_CODE_NO)) {
            toSendTypeImg.setVisibility(View.VISIBLE);
            toSendTypeImg.setImageResource(R.drawable.msg_state_fail_resend);
        } else if (message.getCode() != null
                && message.getCode().equals(TraMessage.SEND_MSG_CODE_ERROR)) {
            toSendTypeImg.setVisibility(View.VISIBLE);
            toSendTypeImg.setImageResource(R.drawable.msg_state_fail_resend);
        }
    }

    /**
     * 重新发送对话框
     */
    private void showDialogRepeat(final Context context, final TraMessage message) {

        new AlertDialog.Builder(context)
                .setCancelable(false)
                // 设置不能通过“后退”按钮关闭对话框
                .setMessage(getResources().getString(R.string.tv_isRepeat))
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                                if (message.isVoic()) {
                                    Intent intent = new Intent();
                                    intent.setAction(IAction.TRA_MSG_ITEM);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("VOIC_PATH", message.getUrl());
                                    bundle.putInt("MSG_ID", message.getId());
                                    intent.putExtras(bundle);
                                    context.sendBroadcast(intent);
                                } else {
                                    message.setTo(message.getFrom());
                                    message.setFrom(Engine.getInstance().getUserName());
                                    message.setMsg_type(1);
                                    Engine.getInstance().sendMessageInBackground(mContext, message);
                                }
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .show();// 显示对话框

    }

    private CheckTextNumModel setCheckTextNumModel(String number, int start, int end) {

        CheckTextNumModel checkTextNumModel = new CheckTextNumModel();
        checkTextNumModel.setNum(number);
        checkTextNumModel.setStartPosttion(start);
        checkTextNumModel.setEndPosttion(end);

        return checkTextNumModel;

        // final SpannableString sp = new SpannableString(text);
        // sp.setSpan(new ClickableSpan() {
        //
        // @Override
        // public void onClick(View widget) {
        // // TODO Auto-generated method stub
        // showCallDialog(mContext, widget, number);
        // }
        // }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // tv.setText(sp);
        // tv.setLinkTextColor(getResources().getColor(R.color.wathet_blue));
        // tv.setMovementMethod(LinkMovementMethod.getInstance());
        // tv.setFocusable(false);
        // tv.setClickable(false);
    }

    private String callNum;

    private void showCallDialog(final Context mContext, View v, final String num) {

        if (Engine.getInstance().isLogIn) {

            callNum = num;
            Engine.getInstance().showCallDialog(mContext, onListAlertClick, 1);
        }
    }

    private void checkCallItem(Context mContext, String number, String str) {

        if (str.equals(mContext.getResources().getString(R.string.call_ip)))
        {
            Utils.callVoip(mContext, Engine.getInstance().setCallRecord(number, 1, 0, 
                    CallLog.Calls.INCOMING_TYPE));
        }
    }

    /**
     * 添加联系人
     */
    private void addContact(String number, TraMessage message) {

        ArrayList<TagNumber> tagNumbers = new ArrayList<TagNumber>();
        final ContactModel contact = new ContactModel();
        TagNumber tagNumber = new TagNumber();
        tagNumber.setValue(number);
        tagNumber.setRegist(1);
        tagNumber.setNewNum(number);
        tagNumbers.add(tagNumber);
        contact.setPhoneNumList(tagNumbers);
        contact.setTravelrelyNumber(tagNumber.getNewNum());
        contact.setTravelPhoneNumber(tagNumber.getNewNum());
        contact.setNickName(message.getNick_name());
        contact.setLastName(message.getNick_name());
        contact.setFirstName("");
        contact.setContactType(1);
        contact.setTravelUserPhone(message.getTo());
        contact.setHeadPortrait(message.getHead_portrait());
        contact.addTagNumberInDB(tagNumber);
        
//        ContactDBHelper.getInstance().insert(contact);
//        
//        
//        doFinish(contact);
//
//        goContactDetailActivity(contact);
    }

    private void doFinish(ContactModel contactModel) {
        FetchTokenOneTask fetchTokenOneTask = new FetchTokenOneTask();
        fetchTokenOneTask.setFetchTokenListener(Engine.getInstance());
        fetchTokenOneTask.execute(contactModel);
    }

    OnListAlertClickListener onListAlertClick = new OnListAlertClickListener() {

        @Override
        public void onListAlertClick(int id, int which) {
            // TODO Auto-generated method stub
            Engine.getInstance().getOnListAlertClick(mContext, id, which,
                    Engine.getInstance().setCallRecord(callNum, 1, 0, 
                            CallLog.Calls.INCOMING_TYPE));
        }
    };
    
    private void checkMapLoctionMap(TraMessage traMessage){
        
        try {
        	Toast.makeText(mContext,
                    getResources().getString(R.string.tv_googlemap_no),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
