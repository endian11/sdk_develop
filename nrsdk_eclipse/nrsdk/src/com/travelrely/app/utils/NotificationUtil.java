package com.travelrely.app.utils;

import java.util.List;
import java.util.Random;

import com.travelrely.app.activity.ContactActivity;
import com.travelrely.app.activity.HomePageActivity;
import com.travelrely.app.activity.SmsChatListAct;
import com.travelrely.app.activity.WelcomeAct;
import com.travelrely.core.Engine;
import com.travelrely.core.nr.util.CallUtil;
import com.travelrely.core.nr.util.MessageUtil;
import com.travelrely.model.ContactModel;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.ContactDBHelper;
import com.travelrely.v2.model.SmsEntity;
import com.travelrely.v2.response.TraMessage;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public class NotificationUtil {

	public static void sendNotifiyIfNeed(TraMessage traMessage, SmsEntity smsEntity) {
        PlaySound();
        Context context = Engine.getInstance().getContext();
        // 如果未登录，则从启动页进入，目的是为了进行后台登录
        // 如果已登录，则直接进入我的旅信首页
        Intent intent;
        if (isTopActivity(context) == true) {
            intent = new Intent(context, HomePageActivity.class);
        } else {
            intent = new Intent(context, WelcomeAct.class);
        }
        
        String withUser = "";
        String content = ""; 
        
        if(traMessage != null){
            Bundle extras = MessageUtil.generateBunlde(traMessage);
            intent.putExtras(extras);
            withUser = MessageUtil.getFromType(traMessage);
            content = MessageUtil.generateContent(traMessage);
        }else if(smsEntity != null){
            withUser = smsEntity.getAddress();
            content = smsEntity.getBody();
        }
        
        content = withUser + ":" + content ;
        Notification notification = new Notification(R.drawable.ic_launcher, content,
                System.currentTimeMillis());

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent,
                Notification.FLAG_ONLY_ALERT_ONCE);
        notification.setLatestEventInfo(context, context.getResources().getString(R.string.app_name),
                content, contentIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(R.drawable.ic_launcher, notification);

        // 如果登录，则清除通知
        if (isTopActivity(context) == true) {
            nm.cancel(R.drawable.ic_launcher);
        } else {
            // do nothing
        }

    }
	
	public static void notifyMissedCall(Context context, String num) {
        String str = num;
        ContactModel c = ContactDBHelper.getInstance()
                .getContactByNumberTry(num);
        if (c != null)
        {
            str = c.getName();
        }
        
        if (TextUtils.isEmpty(str))
        {
            str = num;
        }

        Intent intent = new Intent(context, ContactActivity.class);
        intent.putExtra("FRAGMENT_IDX", 1);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        
        Notification notification = new Notification.Builder(context)
        .setSmallIcon(R.drawable.nr_missing_call)
        .setTicker("来自" + str + "的未接电话")
        .setContentTitle("旅信未接电话").setContentText(str)
        .setAutoCancel(true)
        .setContentIntent(pi)
        .build();

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(R.drawable.nr_missing_call, notification);
    }

	public static void notifySms(Context context, SmsEntity sms)
    {
        if (sms == null)
        {
            return;
        }
        CallUtil.unlockScreen(context, true);
        
        String title = sms.getNickName();
        String str = sms.getBody();

        Intent intent = new Intent(context, SmsChatListAct.class);
        intent.putExtra("MESSAGE_SMS", sms);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.msg_notify)
                .setTicker(str)
                .setContentTitle(title)
                .setContentText(str)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setLights(0x00cd00, 500, 300)
                .setSound(
                        Uri.parse("android.resource://" + "com.travelrely.v2"
                                + "/" + R.raw.lilac)).build();

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        nm.notify(R.drawable.msg_notify, notification);
        
        CallUtil.unlockScreen(context, false);
    }


	public static int PlaySound() {
        Context context = Engine.getInstance().getContext();

        NotificationManager mgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification nt = new Notification();
        nt.sound=Uri.parse("android.resource://" + "com.travelrely.v2" + "/" +R.raw.lilac);
        nt.vibrate = new long[]{0,80};

        int soundId = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
        mgr.notify(soundId, nt);
        return soundId;
    }
	
	 public static boolean isTopActivity(Context context) {
	     String packageName = "com.travelrely.v2";
	     ActivityManager activityManager = (ActivityManager) context
	                .getSystemService(Context.ACTIVITY_SERVICE);
	     List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
	     if (tasksInfo.size() > 0) {
	         System.out.println("packagename" + tasksInfo.get(0).topActivity.getPackageName());
	         // 应用程序位于堆栈的顶层
	         if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
	             return true;
	         }
	    }
	    return false;
	}
}
