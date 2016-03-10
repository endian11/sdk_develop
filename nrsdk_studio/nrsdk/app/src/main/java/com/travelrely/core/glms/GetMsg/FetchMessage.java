package com.travelrely.core.glms.GetMsg;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Constant;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.util.AESUtils;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.FileUtil;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.LogUtil;
import com.travelrely.core.util.UrlUtil;
import com.travelrely.model.ContactModel;
import com.travelrely.sdk.R;
import com.travelrely.sdk.SDKAction;
import com.travelrely.app.db.ContactDBHelper;
import com.travelrely.app.db.ReceptionInfoDBHelper;
import com.travelrely.app.db.TravelrelyMessageDBHelper;
import com.travelrely.v2.model.ReceptionInfo;
import com.travelrely.v2.response.GetMessage;
import com.travelrely.v2.response.Response;
import com.travelrely.v2.response.TraMessage;

public class FetchMessage {
	int max_id =0;
	public static int msg_count;
	public static int unReadMsg;
	private static Handler handler;
	public static int tempMaxId = -1;
	public static List<Map<String, ContactModel>> contactIdList = new ArrayList<Map<String,ContactModel>>(); 
	 
	public FetchMessage(){
		handler = new Handler();
	}
	
	
	public  void fetchMsg(){
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 GetMessage getMessage = getMessage(max_id);
				 if (getMessage != null){
					 if (getMessage.getResponseInfo().isSuccess()){
						 //获取消息成功

                         List<TraMessage> list = getMessage.getData().getMessage_list();
                         LogUtil.i("总共取到 ", list.size()+" 条消息");

                         for (int i = 0; i < list.size(); i++)
                         {
                             // 小号通话业务入口
                             if (list.get(i).getType() == 16
                                     || list.get(i).getType() == 17)
                             {
                                 Engine.getInstance().RentEntry(
                                         list.get(i).getType(),
                                         list.get(i).getFrom(),
                                         list.get(i).getContent());
                             }
                             
                             max_id = Math.max(list.get(i).getMsg_id(), max_id);
                             list.get(i).setMsg_type(2);
                             Engine.getInstance().setFromType(list.get(i).getTo());
                         }
                         List<TraMessage> messages = getMessage.getData().getMessage_list();
                         if (messages.size() <= 0) {
                             LOGManager.d("message:没有消息，我要请求notifyme");
                             max_id = 0;
                             new Thread(new Runnable() {

                                 @Override
                                 public void run() {

                                     SystemClock.sleep(1000);
                                     getMessageInOrtherThread(max_id);
                                 }
                             }).start();
                             // 请求notify的时候去请求一下getMessage();

                         } else {

                             for (int i = 0; i < list.size(); i++) {
                                 final TraMessage message = messages.get(i);
                                 downloadIfNeed(message);
                                 LOGManager.d("message:有消息，我要再请求一次");
                             }
                             LogUtil.i("max_id in getmessage ", max_id+"");
                             getMessageInOrtherThread(max_id);
                         }
                     
						 }
					 }
				 }
		}).start();
	
	}
private void getMessageInOrtherThread(int max_id) {

    HttpConnector httpConnector = new HttpConnector();
    
    String postData = Request.getMessage(Engine.getInstance().getUserName(),
            DeviceInfo.linkSource, max_id);

    String httpResult = httpConnector.requestByHttpPutMessageOrtherThread(UrlUtil.url("api/message/get"), postData,
            Engine.getInstance().getContext(), true);
    GetMessage getMessage = null;
    if (httpResult != null) {
        getMessage = Response.GetMessage(httpResult);
        LOGManager.d("**************get1 doninotherthread" + getMessage);
        if (getMessage != null) {
            if (getMessage.getResponseInfo().isSuccess()) {
                List<TraMessage> list = getMessage.getData().getMessage_list();

                List<TraMessage> messages = getMessage.getData().getMessage_list();
                if (messages.size() <= 0) {
                    LOGManager.d("message:异步请求，没有收到消息，该怎么办 doinotherthread!");
                } else {

                    for (int i = 0; i < list.size(); i++)
                    {
                        // 小号通话业务入口
                        if (list.get(i).getType() == 16
                                || list.get(i).getType() == 17)
                        {
                            Engine.getInstance().RentEntry(
                                    list.get(i).getType(),
                                    list.get(i).getFrom(),
                                    list.get(i).getContent());
                        }
                        
                        max_id = Math.max(list.get(i).getMsg_id(), max_id);
                        list.get(i).setMsg_type(2);
                    }
                    final TraMessage message = messages.get(0);
                    downloadIfNeed(message);
                    LOGManager.d("message:异步请求，收到消息，我还要异步请求  doinotherthread");
                    getMessageInOrtherThread(message.getMsg_id());
                }
            }
        }
    }

}

	
	public GetMessage getMessage(int max_id) {
	        
	        HttpConnector httpConnector = new HttpConnector();
	        
	        String postData = Request.getMessage(Engine.getInstance().getUserName(),
	                DeviceInfo.linkSource, max_id);

	        String httpResult = httpConnector.requestByHttpPutMessage(UrlUtil.url("api/message/get"), postData,
	                Engine.getInstance().getContext(), true);
	        LogUtil.i("取消息url ", UrlUtil.url("api/message/get"));
	        GetMessage getMessage = null;
	        if (httpResult != null) {
	        	getMessage = Response.GetMessage(httpResult);
	            if (getMessage.getData().getMessage_list() == null){
	            	
	            }else{
	            	
	            	getMessage = Response.GetMessage(httpResult);
	            }
	        } else {
	            // 如果为null说明服务器拒绝返回
	        }
//	         LogUtil.d("getMessage response" , httpResult);
	        return getMessage;
	 }
	
	
    /**
     * 检测通讯录id是否保存在内存中，目的是为了减少对数据库的操作
     */
    public static ContactModel isContactId(String num){
        
        for(Map<String, ContactModel> map1 : contactIdList){
            if(map1.get(num) != null){
                return map1.get(num);
            }
        }
        return null;
    }
    
	
	 private void downloadIfNeed(TraMessage message) {

	        if (tempMaxId >= message.getMsg_id()) {
	            return;
	        } else {
	            tempMaxId = message.getMsg_id();
	            // String context = AESUtils.getDecryptString(message.getContent());
	            // LOGManager.e("收到=" + context);

	            TravelrelyMessageDBHelper travelrelyMessageDBHelper = TravelrelyMessageDBHelper
	                    .getInstance();

	            if (message.isJPG() || message.isMp3()) {

	                downloadExtra(message);
	            }

	            if (message.getHead_portrait() != null) {

	                String oldHead = null;
	                String newHead = message.getHead_portrait();
	                List<TraMessage> list = travelrelyMessageDBHelper.getHeadMessages();
	                if (list.size() > 0) {
	                    oldHead = list.get(0).getHead_portrait();
	                    if (!newHead.equals(oldHead)) {
	                        Log.d("", "" + "有新头像:");
	                        Engine.getInstance().downloadHeadImg(Engine.getInstance().getContext(), message, "_s");
//	                        TravelrelyMessageDBHelper tHelper = TravelrelyMessageDBHelper.getInstance();
//	                        List<TraMessage> listMList = tHelper.getMessages(message.getFrom(), 0);
//	                        for(TraMessage traMessage : listMList){
//	                            tHelper.updateContext(traMessage.getFrom(), String.valueOf(traMessage.getId()), "id", "head_portrait", message.getHead_portrait());
//	                        }
	                    } else {
	                        message.setHead_portrait_url(FileUtil.getImagePath("head_img") + "/"
	                                + message.getHead_portrait() + "_s" + ".jpg");
	                        Log.d("", "" + "没有新头像");
	                    }
	                } else {
	                    Engine.getInstance().downloadHeadImg(Engine.getInstance().getContext(), message, "_s");
	                    Log.d("", "" + "第一次设置头像");
	                }
	            }

	            Log.d("", "收到的类型=" + message.getType());

	            if (message.getFrom_type() == 3) {
	                unReadMsg = travelrelyMessageDBHelper.getContext(message.getTo(), "id")
	                        .getUnReadCount();
	            } else {
	                unReadMsg = travelrelyMessageDBHelper.getContext(message.getFrom(), "id")
	                        .getUnReadCount();
	            }

	            if (message.isRegistrationResponse()) {
	                setReception(message);
	                return;
	            }
	            if (message.isRadarResponse()) {
	                setRadar(message);
	                return;
	            }
	            else {
	                List<TraMessage> lMessages = travelrelyMessageDBHelper.selectMessagesState(
	                        message.getTo(), "msg_type", "2");
	                if (lMessages.size() >= 1) {
	                    if (lMessages.get(lMessages.size() - 1).getIs_nickname() == 1) {
	                        message.setIs_nickname(1);
	                    }
	                }
	                
	                unReadMsg += 1;
	                message.setUnReadCount(unReadMsg);
	                message.setContact_id(setContactId(message));
	                travelrelyMessageDBHelper.addMessage(message);
	                saveMessageAndUpdate(message);
	            }
	        }
	    }
	    public static Long setContactId(TraMessage msg){
	        
	        ContactModel cModel;
	        String number = msg.getFrom();
	        if(!number.contains("+")){
	            number =  "+" + number;
	        }
	        if(isContactId(msg.getFrom()) != null){
	            cModel = isContactId(number);
	            return cModel.getRawContactId();
	        }else{
	            cModel = ContactDBHelper.getInstance().getContactByNumber(number, "value");
	            if(cModel != null){
	                Map<String, ContactModel> map = new HashMap<String, ContactModel>();
	                map.put(number, cModel);
	                contactIdList.add(map);
	                return cModel.getRawContactId();
	            }
	        }
	        return (long) 0;
	    }
	    private void downloadExtra(TraMessage message) {

	        String end = "\r\n";
	        String Hyphens = "--";
	        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";

	        try {
	            URL url = new URL(UrlUtil.url("api/message/download"));
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            con.setDoInput(true);
	            con.setDoOutput(true);
	            con.setUseCaches(false);
	            con.setRequestMethod("POST");
	            /* setRequestProperty */
	            con.setRequestProperty("Connection", "Keep-Alive");
	            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
	            ds.writeBytes(message.getContent());
	            ds.flush();
	            InputStream is = con.getInputStream();

	            if (message.isMp3()) {
	                FileUtil fileUtil = new FileUtil(Engine.getInstance().getContext());
	                fileUtil.saveVoice(message, is);
	            } else if (message.isJPG()) {
	                Bitmap bitmap = BitmapFactory.decodeStream(is);

	                FileUtil fileUtil = new FileUtil(Engine.getInstance().getContext());
	                fileUtil.saveImg(message, bitmap);

	                bitmap.recycle();
	            }
	            ds.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    }
	    
	    public static void saveMessageAndUpdate(final TraMessage message) {

	        handler.post(new Runnable() {

	            @Override
	            public void run() {

	                msg_count++;
	                Intent intent = new Intent();
	                intent.setAction(IAction.MSM_ACTION);
	                Bundle bundle = new Bundle();
	                bundle.putString("message_from_head", message.getHead_portrait());
	                bundle.putString("from", message.getFrom());
	                bundle.putInt("messageId", message.getMsg_id());
	                bundle.putInt("type", 1);
	                intent.putExtras(bundle);
	                Engine.getInstance().getContext().sendBroadcast(intent);
	                sendMainTabMsgCount(1);
	                // NotificationUtil.sendNotifiyIfNeed(message, null);
	                Engine.getInstance().broadcastNotifiy(SDKAction.NR_SDK_NOTIFIY_REC_SMS, message);
	            }
	        });
	    }
	    
	    /**
	     * 发送消息未读广播
	     */
	    public static void sendMainTabMsgCount(int type)
	    {
	        if(type == 1){
	        	Constant.isMsgCount = true;
	        }else{
	        	Constant.isSmsCount = true;
	        }
	        Intent intent = new Intent();
	        intent.setAction(IAction.MSG_COUNT_ACTION);
	        Engine.getInstance().getContext().sendBroadcast(intent);
	    }

	    
	    /**
	     * 处理签到回应
	     */
	    private void setReception(TraMessage msg) {

	        try {
	            saveReceptionInfo(msg);
	        } catch (Exception e) {
	            // TODO: handle exception
	            Log.d("", "接收签到及雷达信息异常：" + e.getLocalizedMessage());
	        }

	        Intent intent = new Intent();
	        intent.setAction(IAction.ReceptionListActivity);
	        Engine.getInstance().getContext().sendBroadcast(intent);

	        
	        // NotificationUtil.sendNotifiyIfNeed(msg, null);
	        Engine.getInstance().broadcastNotifiy(SDKAction.NR_SDK_NOTIFIY_REC_SMS, msg);
	    }
	    
	    private void saveReceptionInfo(TraMessage msg) {

	        if (msg.getContent() != null) {

	            Log.d("", "收到的签到回应=：" + AESUtils.getDecryptString(msg.getContent()));
	            String strContent = AESUtils.getDecryptString(msg.getContent());

	            if (msg.isRegistrationResponse()) {
	                if (selectReception(msg.getFrom(), strContent)) {
	                    refreshReceptionMsg("context", strContent, "type", String.valueOf(1));
	                } else {
	                    ReceptionInfo rInfo = setReceptionInfo(msg, strContent, 1, "", "", msg.getNick_name());
	                    ReceptionInfoDBHelper rInfoDBHelper = ReceptionInfoDBHelper.getInstance();
	                    rInfoDBHelper.insert(rInfo);
	                }
	            }
	        }
	    }
	    
	    private void refreshReceptionMsg(String type, String msgId, String field, String newContext) {

	        ReceptionInfoDBHelper rInfoDBHelper = ReceptionInfoDBHelper.getInstance();
	        rInfoDBHelper.updateContext(type, msgId, field, newContext);
	    }
	    
	    private ReceptionInfo setReceptionInfo(TraMessage msg, String content, int type, String latitude,
	            String longitude, String nickName) {

	        ReceptionInfo rInfo = new ReceptionInfo();

	        rInfo.setGroupId(msg.getTo());
	        rInfo.setFromName(msg.getFrom());
	        rInfo.setType(type);// 1代表签到
	        rInfo.setContext(content);
	        rInfo.setLatitude(latitude);
	        rInfo.setLongitude(longitude);
	        rInfo.setNickName(nickName);
	        return rInfo;
	    }

	    

	    private boolean selectReception(String from, String msgId) {

	        ReceptionInfoDBHelper rInfoDBHelper = ReceptionInfoDBHelper.getInstance();
	        return rInfoDBHelper.getReceptionMsgId(from, msgId);
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
	    
	    /**
	     * 处理收到雷达回应
	     */
	    private void setRadar(TraMessage message) {

	        String[] strContent = AESUtils.getDecryptString(message.getContent()).split("\t");

	        if (selectReception(message.getFrom(), strContent[0])) {
	            refreshRadarMsg("from_name", message.getFrom(), "group_id", strContent[0],
	                    "longitude", strContent[1], "latitude", strContent[2], "type",
	                    String.valueOf(TraMessage.TYPE_RADAR_RESPONSE), "nick_name", message.getNick_name());
	        } else {
	            ReceptionInfo rInfo = setReceptionInfo(message, strContent[0],
	                    TraMessage.TYPE_RADAR_RESPONSE, strContent[1], strContent[2], message.getNick_name());
	            ReceptionInfoDBHelper rInfoDBHelper = ReceptionInfoDBHelper.getInstance();
	            rInfoDBHelper.insert(rInfo);
	        }

	        // NotificationUtil.sendNotifiyIfNeed(message, null);
	        Engine.getInstance().broadcastNotifiy(SDKAction.NR_SDK_NOTIFIY_REC_SMS, message);
	    }
	    
	    

	    private void refreshRadarMsg(String from, String msgId, String from2, String msgId2,
	            String field, String newContext,
	            String field2, String newContext2, String field3, String newContext3, String field4, String newContext4) {

	        ReceptionInfoDBHelper rInfoDBHelper = ReceptionInfoDBHelper.getInstance();
	        rInfoDBHelper.updateContextResponse(from, msgId, from2, msgId2, field, newContext, field2,
	                newContext2, field3, newContext3, field4, newContext4);
	    }

}
