package com.travelrely.sdk;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.core.Res;
import com.travelrely.core.ble.BleMsgId;
import com.travelrely.core.nr.HMediaManager;
import com.travelrely.core.nr.msg.MsgId;
import com.travelrely.core.nr.util.CallUtil;
import com.travelrely.v2.db.GroupDBHelper;
import com.travelrely.v2.db.SmsEntityDBHelper;
import com.travelrely.v2.db.TravelrelyMessageDBHelper;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.model.SmsEntity;
import com.travelrely.v2.net_interface.GetCommStatusRsp;
import com.travelrely.v2.net_interface.LoginRsp;
import com.travelrely.v2.net_interface.TransferClientIdReq;
import com.travelrely.v2.net_interface.VerifySuccReq;
import com.travelrely.v2.net_interface.VerifySuccRsp;
import com.travelrely.v2.response.GetGroupList;
import com.travelrely.v2.response.GroupList;
import com.travelrely.v2.response.TraMessage;
import com.travelrely.v2.util.AESUtils;
import com.travelrely.v2.util.FileUtil;
import com.travelrely.v2.util.LOGManager;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.MyRecorder;
import com.travelrely.v2.util.PictureUtil;
import com.travelrely.v2.util.SpUtil;
import com.travelrely.v2.util.TimeUtil;
import com.travelrely.v2.util.UrlUtil;
import com.travelrely.v2.util.Utils;

public class SdkService2 extends Service{
	public static SdkService2 instance=null;
	public Context context=null;
	
	private BluetoothManager btManager;
	private BluetoothAdapter mBtAdapter;
	private List<BluetoothDevice> bleList;
	private final static String TAG="sdkService";
	private AudioManager mAudioManager;
	private MyRecorder recorder;
	public static String path = "/voice/";
	public volatile String recordFilePath;
	public static ResultListener resultListener;
	public static SdkService2 getInstance()
	{
		return instance;
	}
	
	
	public void startRecord(ResultListener listener) {

		try {
			JSONObject rtJsonObject = new JSONObject();
			rtJsonObject.put("opt", "startRecord");
			if (!Engine.getInstance().isLogIn) {
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "请先登录");
				listener.fail(rtJsonObject);
				return;
			}
			rtJsonObject.put("rtcode", "success");
			rtJsonObject.put("username", Engine.getInstance().userName());
			String filePath = getExternalCacheDir() + path
					+ System.currentTimeMillis() + ".amr";
			recordFilePath = filePath;
			if (recorder==null) {
				recorder = new MyRecorder();
			}			
			recorder.start(filePath);
			listener.success(rtJsonObject);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void stopRecord(ResultListener listener) {

		try {
			JSONObject rtJsonObject = new JSONObject();
			rtJsonObject.put("opt", "stopRecord");
			if (!Engine.getInstance().isLogIn) {
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "请先登录");
				listener.fail(rtJsonObject);
				return;
			}
			rtJsonObject.put("rtcode", "success");
			rtJsonObject.put("username", Engine.getInstance().userName());
			if (recorder!=null) {
				recorder.stop();
				MediaPlayer player = new MediaPlayer();
				File file = new File(recordFilePath);
				player.setDataSource(file.getAbsolutePath());
				player.start();
			}
			listener.success(rtJsonObject);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	 private String uploadVoice(String path) {
        String smc_loc = null;
        File file = new File(path);

        if (!file.exists()) {
            return null;
        }
        if (Engine.getInstance().homeLogin == true) {
            smc_loc = Engine.getInstance().getHomeSmcLoc();
        } else {
            smc_loc = Engine.getInstance().getRoamSmcLoc();
        }
        String urlstr = "http://" + smc_loc + "/" + "api/message/upload";
        String end = "\r\n";
        String Hyphens = "--";
        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            URL url = new URL(urlstr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(Hyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; name=\"msgfile\"; filename=\"bi0503.jpg\";Context-Type:image/jpeg"
                    + end);
            ds.writeBytes(end);
            // bitmap.compress(CompressFormat.JPEG, 100, ds);
            byte[] buffer = new byte[256];
            while (fileInputStream.read(buffer) > 0) {
                ds.write(buffer);
            }
            ds.writeBytes(end);
            ds.writeBytes(Hyphens + boundary + Hyphens + end);
            ds.flush();
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            System.out.println("b=" + b);
            ds.close();
            return b.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
	 
//	 String content = uploadVoice(path);
//     // 如果上传失败，就把这张图片用时间点存下来
//     if (content == null) {
//
//         content = String.valueOf(System.currentTimeMillis());
//     }
//     sendVoiceMessage(path, content);
	
	 public void sendVoiceMessage(final String path, final String from, final String to, final int fromType, final int toType, final ResultListener listener) 
	 {
		 
		 final JSONObject rtJsonObject=new JSONObject();
			try {
				rtJsonObject.put("opt", "sendVoiceMessage");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (!Engine.getInstance().isLogIn)
			{
				try {
					
					rtJsonObject.put("rtcode", "fail");
					rtJsonObject.put("msg", "请先登录");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listener.fail(rtJsonObject);
				return;	
			}
		 
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String content = uploadVoice(path);
					if (content == null) {
						content = String.valueOf(System.currentTimeMillis());
					}
					FileUtil fileUtil = new FileUtil(context);
					TraMessage message = Request.generateSendMessage(from, content, to, 4,
							"amr", fromType, toType, 4, "", "", "", "");
					boolean suc = fileUtil.saveVoice(message, path);
					if (suc) {
						TravelrelyMessageDBHelper.getInstance().addMessage(message);
						Engine.getInstance().sendMessageInBackground(context, message);
						 try {
								rtJsonObject.put("rtcode", "success");
								rtJsonObject.put("username", Engine.getInstance().userName());
								listener.success(rtJsonObject);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				}
			}).start();
	 }
	 
	 
	 
	private void sendImgMessageLocal(Bitmap bitmap, String content, String from, String to, int fromtype, int totype) {
        // String to = this.message.getWidth_user();

        TraMessage message2 = Request.generateSendMessage(from, content,
                to, 2, TraMessage.EXT_TYPE_JPG, fromtype, totype, 2, "", "",
                "",
                "");
        FileUtil file = new FileUtil(this);
        boolean isSave = file.saveImg(message2, bitmap);
        bitmap.recycle();
        if (isSave) {
            TravelrelyMessageDBHelper.getInstance().addMessage(message2);
            Engine.getInstance().sendMessageInBackground(context,message2);
        }
    }
	public void sendImgMessage(String path, final String from, final String to, final int fromType, final int toType, final ResultListener listener) {

		final JSONObject rtJsonObject=new JSONObject();
		try {
			rtJsonObject.put("opt", "sendImgMessage");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (!Engine.getInstance().isLogIn)
		{
			try {
				
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "请先登录");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listener.fail(rtJsonObject);
			return;	
		}
		Bitmap bitmap = PictureUtil.loadImageFromUrl(path);
        if (bitmap != null) {
            final Bitmap scaled = Utils.generateBigBitmap(bitmap, path);
            if (scaled != bitmap) {
                bitmap.recycle();
            }

            new Thread(
            		new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String cc = Engine.getInstance().getCC();
		                    String host = ReleaseConfig.getUrl(cc);
		                    String content = Engine.getInstance().uploadImg(host + "api/message/upload", scaled, null);
		                    // 如果上传失败，就把这张图片用时间点存下来
		                    if (content == null) {

		                        content = String.valueOf(System.currentTimeMillis());
		                    }
		                    sendImgMessageLocal(scaled, content, from , to, fromType, toType);
		                    try {
								rtJsonObject.put("rtcode", "success");
								rtJsonObject.put("username", Engine.getInstance().userName());
								listener.success(rtJsonObject);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
            };
        }

	
	
	
	public void sendTextMessage(String message, String from, String to, int fromType, int toType, ResultListener listener) {
        try {
			JSONObject rtJsonObject=new JSONObject();
			rtJsonObject.put("opt", "sendTextMessage");
			if (!Engine.getInstance().isLogIn)
			{
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "请先登录");
				listener.fail(rtJsonObject);
				return;	
			}
			rtJsonObject.put("rtcode", "success");
			rtJsonObject.put("username", Engine.getInstance().userName());
			String messageEnc = AESUtils.getEntryString(message);
	        final TraMessage message2 = Request.generateSendMessage(from, messageEnc, to, 0, "", fromType,
	                toType, 0, "", "", "", "");
	        TravelrelyMessageDBHelper.getInstance().addMessage(message2);
	        Engine.getInstance().sendMessageInBackground(context,message2);
	        
	        listener.success(rtJsonObject);
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
	
	
	
	public void getBalance(ResultListener listener) //0:归属地 1:漫游地
	{
		try {
			JSONObject rtJsonObject=new JSONObject();
			rtJsonObject.put("opt", "getBalance");
			if (!Engine.getInstance().isLogIn)
			{
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "请先登录");
				listener.fail(rtJsonObject);
				return;	
			}
			double balance = SpUtil.getBalance();
			rtJsonObject.put("rtcode", "success");
			rtJsonObject.put("balance", balance);
			rtJsonObject.put("username", Engine.getInstance().userName());
			listener.success(rtJsonObject);
			
//			String date = TimeUtil.getDateString(System.currentTimeMillis(),
//	                TimeUtil.dateFormat2);
//			SmsEntity sendMessageSms = new SmsEntity();
//	        sendMessageSms.setAddress(phoneNumber);
//	        sendMessageSms.setPerson(Engine.getInstance().getUserName());
//	        sendMessageSms.setBody(sms);
//	        sendMessageSms.setDir(SmsEntity.DIR_OUTGOING);
//	        sendMessageSms.setBodyType(SmsEntity.TYPE_TEXT);
//	        sendMessageSms.setStatus(SmsEntity.STATUS_PENDING);
//	        sendMessageSms.setDate(date);
//	        SmsEntityDBHelper mHelper = SmsEntityDBHelper.getInstance();
//	        long id = mHelper.addMessageSms(sendMessageSms);
//	        Engine.getInstance().sendSMS(this, id);
//			rtJsonObject.put("rtcode", "success");
//			listener.success(rtJsonObject);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void sendSms(String phoneNumber, String sms, ResultListener listener)
	{
		try {
			JSONObject rtJsonObject=new JSONObject();
			rtJsonObject.put("opt", "sendSms");
			if (!Engine.getInstance().isNRRegisted)
			{
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "请先注册");
				listener.fail(rtJsonObject);
				return;	
			}
			
			String date = TimeUtil.getDateString(System.currentTimeMillis(),
	                TimeUtil.dateFormat2);
			SmsEntity sendMessageSms = new SmsEntity();
	        sendMessageSms.setAddress(phoneNumber);
	        sendMessageSms.setPerson(Engine.getInstance().getUserName());
	        sendMessageSms.setBody(sms);
	        sendMessageSms.setDir(SmsEntity.DIR_OUTGOING);
	        sendMessageSms.setBodyType(SmsEntity.TYPE_TEXT);
	        sendMessageSms.setStatus(SmsEntity.STATUS_PENDING);
	        sendMessageSms.setDate(date);
	        SmsEntityDBHelper mHelper = SmsEntityDBHelper.getInstance();
	        long id = mHelper.addMessageSms(sendMessageSms);
	        Engine.getInstance().sendSMS(this, id);
			rtJsonObject.put("rtcode", "success");
			listener.success(rtJsonObject);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void smsRecvCallback(SmsEntity sms)
	{
		Engine.getInstance().showSysAlert(context, "来自于"+sms.getPerson()+"的短信",
				sms.getBody(), "知道了");
	}
	
	
	public void calledCallback(String caller) //给nrservcie调用的callback函数, 当有呼叫进入时
	{
		Engine.getInstance().showAppAlertDialog(context, "来电提醒",
        		caller, "接听", "不接听", new OnSysAlertClickListener()
                {
                    @Override
                    public void onRightClick(SysAlertDialog dialog)
                    {
//                        if (mNRHandler == null)
//                        {
//                            return;
//                        }
//                        mNRHandler.procMsg(MsgId.APP_AGENT_SIM_CARD_FEE_RSP, null);
                    	Engine.getInstance().hangUp(context);
                    }
                    
                    @Override
                    public void onOkClick(SysAlertDialog dialog)
                    {
                        // TODO Auto-generated method stub
                        return;
                    }
                    
                    @Override
                    public void onLeftClick(SysAlertDialog dialog)
                    {
                        // TODO Auto-generated method stub
                    	Engine.getInstance().answerPhone(context);
                    	return;
                    }
                });
	}
	
	public void TraMsgCallback(TraMessage msg) 
	{
		String content = msg.getContent();
		String infoString="";
		if (msg.getType()==0)//文本消息
		{
			content =  AESUtils.getDecryptString(msg.getContent());
			infoString="收到来自于"+msg.getFrom()+"的旅信文本消息:"+content+"\n";
		}
		else if (msg.getType()==2) //图片
		{
			infoString="收到来自于"+msg.getFrom()+"的旅信图片消息,图片文件名称:"+msg.getContent()+"\n";
		}
		else if (msg.getType()==4) {
			infoString="收到来自于"+msg.getFrom()+"的旅信语音消息,语音文件名称:"+msg.getContent()+"\n";
		}
		
		Looper.prepare();
		Toast.makeText(context, infoString, Toast.LENGTH_LONG).show();
		Looper.loop();
	}
	public void hangup(ResultListener listener)
	{
		try {
			JSONObject rtJsonObject=new JSONObject();
			rtJsonObject.put("opt", "hangup");
            Engine.getInstance().hangUp(context);
            rtJsonObject.put("rtcode", "success");
            listener.success(rtJsonObject);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void call(String number,ResultListener listener)
	{
		try {
			JSONObject rtJsonObject=new JSONObject();
			rtJsonObject.put("opt", "call");
			if (!Engine.getInstance().isNRRegisted)
			{
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "请先注册");
				listener.fail(rtJsonObject);
				return;	
			}
			Engine.getInstance().startNRService(context, MsgId.APP_CALLING_REQ, number);
			rtJsonObject.put("rtcode", "success");
			listener.success(rtJsonObject);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void speaker(ResultListener listener)
	{
		try {
			JSONObject rtJsonObject=new JSONObject();
			rtJsonObject.put("opt", "speaker");
			if (!Engine.getInstance().isNRRegisted)
			{
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "请先注册");
				listener.fail(rtJsonObject);
				return;	
			}
			if(HMediaManager.mCall !=0){
				if (HMediaManager.loudspeaker_st ==0) {
					HMediaManager.loudspeaker_st = 1;
					mAudioManager.setSpeakerphoneOn(true);
					CallUtil.enableSpeaker(mAudioManager,true);
					HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "on");
					rtJsonObject.put("rtcode", "success");
					rtJsonObject.put("msg", "speaker on");
					listener.success(rtJsonObject);
				} else {
					HMediaManager.loudspeaker_st = 0;
					mAudioManager.setSpeakerphoneOn(false);
					CallUtil.enableSpeaker(mAudioManager,false);
					HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "off");
					rtJsonObject.put("rtcode", "success");
					rtJsonObject.put("msg", "speaker off");
					listener.success(rtJsonObject);
				}
        	}
			else
			{
				rtJsonObject.put("rtcode", "fail");
				rtJsonObject.put("msg", "无通话");
				listener.fail(rtJsonObject);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override 
	public void onCreate() {
		Log.e(TAG, "onCreate");
		registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				onBroadcastReceived(context,intent);
			}
		}, makeFilter());
		
		if (instance==null) {
			instance=this;
			context=getApplicationContext();
			this.btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
			mBtAdapter=btManager.getAdapter();
			bleList=new ArrayList<BluetoothDevice>();
			Engine.getInstance().judgeHomeOrRoam();
			PushManager.getInstance().initialize(this.getApplicationContext());
			Engine.getInstance().tryToStartNR(context);
			Engine.getInstance().getCountryInfoRequest(context);
            Engine.getInstance().getPkgRequest(context);
            // CallActivity.genmMedia(context);
            mAudioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            //FetchMessage.sdkinstance=this;
		}
	};
	
	
	@Override 
	public void onStart(Intent intent, int startId) 
	{
				
	};
	
	public void bleMatch(ResultListener listener)
	{
		JSONObject object = new JSONObject();
		try {
			object.put("opt", "bleMatch");
			if (bleList.size()<=0) {
				object.put("rtcode", "fail");
				object.put("msg", "blelist is empty");
				listener.fail(object);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		final BluetoothDevice device=bleList.get(0);
		
		Engine.getInstance().startNRService(context);
		Engine.getInstance().stopBleService(context);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Engine.getInstance().startBleService(context,
		                 BleMsgId.NR_BLE_MATCH_BOX,
		                 device.getAddress().getBytes());
			}
		}).start();	
	}
	
	private static IntentFilter makeFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(IAction.BLE_CONN_EXPIRE);
        
        intentFilter.addAction(IAction.BOX_FOUND);
        intentFilter.addAction(IAction.BOX_NOT_FOUND);
        intentFilter.addAction(IAction.BOX_MATCH_SUCC);
        intentFilter.addAction(IAction.BOX_MATCH_FAIL);
        
        intentFilter.addAction(IAction.NR_CLOSE_SUCC);
        intentFilter.addAction(IAction.NR_CLOSED);
        intentFilter.addAction(IAction.NR_START_REG);
        intentFilter.addAction(IAction.NR_REG_SUCC);
        intentFilter.addAction(IAction.NR_REG_FAIL);
        intentFilter.addAction(IAction.NR_REG_EXPIRE);

        intentFilter.addAction(IAction.BOX_MISS_MATCH);
        
        intentFilter.addAction(IAction.BOX_NO_SIM);
        intentFilter.addAction(IAction.BOX_SIM_INIT_FAIL);
        intentFilter.addAction(IAction.BOX_SIM_AUTH_FAIL);
        intentFilter.addAction(IAction.BOX_SIM_SYNC_FAIL);
        intentFilter.addAction(IAction.BOX_SIM_CHANGED);
        
        intentFilter.addAction(IAction.BOX_CIPHER_KEY_SAVED);
        intentFilter.addAction(IAction.USER_VERIFY_SUCC);
        intentFilter.addAction(IAction.USER_VERIFY_FAIL);
        
        intentFilter.addAction(IAction.OP_VSIM);
        
        return intentFilter;
    }
	
	
    protected void onBroadcastReceived(Context context, Intent intent)
    {        
        String action = intent.getAction();
        
        LogUtil.i(TAG, "action = " + action);
        
        if (IAction.BOX_FOUND.equals(action))
        {
//            BluetoothDevice device = intent.getParcelableExtra(
//                    BluetoothDevice.EXTRA_DEVICE);
//            showDevice(device);
//
//            scanLeDevice(true);
        	Log.e(TAG, "BOX_FOUND");
        }
        
        if (IAction.BOX_NOT_FOUND.equals(action))
        {
            //scanLeDevice(true);
        	Log.e(TAG, "BOX_NOT_FOUND");
        }
        
        if (IAction.BOX_MATCH_SUCC.equals(action))
        {
//            int pos = lvAdapter.getSelectItem();
//            BluetoothDevice device = devList.get(pos);
//            lvAdapter.setSelectItem(-1);
//            devList.clear();
//            devList.add(device);
//            lvAdapter.notifyDataSetChanged();
            Log.e(TAG, "BOX_MATCH_SUCC");
            Engine.getInstance().startNRService(getApplicationContext(),
                    MsgId.APP_INNER_RESET);
        }
        
        if (IAction.BOX_MATCH_FAIL.equals(action))
        {
//            lvAdapter.setSelectItem(-1);
//            alertWait(false, "");
        	Log.e(TAG, "BOX_MATCH_FAIL");
        }
        
        if (IAction.NR_START_REG.equals(action))
        {
//            verifyState.setVisibility(View.VISIBLE);
//            verifyDesc.setText("正在验证...");
//            timer.start();
        	Log.e(TAG, "NR_START_REG");
        }
        
        if (IAction.NR_REG_FAIL.equals(action))
        {
//        	alertWait(false, "");
//            showAppAlert("提示",
//                    "验证失败（注册失败）", "关闭");
//            verifyDesc.setText("验证失败");
//            timer.cancel();
        	Log.e(TAG, "NR_REG_FAIL");
        }
        
        if (IAction.NR_REG_EXPIRE.equals(action))
        {
//        	 alertWait(false, "");
//            showAppAlert("提示",
//                    "验证失败（注册超时）", "关闭");
//            verifyDesc.setText("验证失败");
//            timer.cancel();
        	Log.e(TAG, "NR_REG_EXPIRE");
        }
        
        if (IAction.BOX_MISS_MATCH.equals(action))
        {
//        	 alertWait(false, "");
//            showAppAlert("提示",
//                    "验证失败（盒子未配对）", "关闭");
//            verifyDesc.setText("验证失败");
//            timer.cancel();
        	Log.e(TAG, "BOX_MISS_MATCH");
        }
        
        if (IAction.BOX_NO_SIM.equals(action))
        {
//        	alertWait(false, "");
//            showAppAlert("提示",
//                    "蓝牙设备未插入sim卡，请插入手机sim卡后使用", "关闭");
//            verifyDesc.setText("验证失败");
//            timer.cancel();
        	Log.e(TAG, "BOX_NO_SIM");
        }
        
        if (IAction.BOX_SIM_INIT_FAIL.equals(action))
        {
//        	alertWait(false, "");
//            showAppAlert("提示",
//                    "请确认蓝牙设备内sim卡是否正确安装或电池是否可用", "关闭");
//            verifyDesc.setText("验证失败");
//            timer.cancel();
        	Log.e(TAG, "BOX_SIM_INIT_FAIL");
        }
        
        if (IAction.BOX_SIM_CHANGED.equals(action))
        {
//        	alertWait(false, "");
//            showAppAlert("提示",
//                    "验证失败（sim卡已更换）", "关闭");
//            verifyDesc.setText("验证失败");
//            timer.cancel();
        	Log.e(TAG, "BOX_SIM_CHANGED");
        }
        
        if (IAction.BOX_CIPHER_KEY_SAVED.equals(action))
        {
            Engine.getInstance().startNRService(context,
                    MsgId.APP_AGENT_USER_VERIFY_REQ);
            Log.e(TAG, "BOX_CIPHER_KEY_SAVED");
        }
        
        if (IAction.USER_VERIFY_SUCC.equals(action))
        {
            Engine.getInstance().stopNR(context);
            SpUtil.setfirstConnect(0);
            //gotoVerifySucc(intent.getIntExtra(IntentMsg.INTENT_ID, 0));
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    VerifySuccRsp rsp = VerifySuccReq.verifySucc(SdkService2.this.context,
                            Engine.getInstance().getUserName(), 0);
                    if (!rsp.getBaseRsp().isSuccess())
                    {
                        //Res.toastErrCode(VerifyByBoxAct.this, rsp.getBaseRsp());
                        return;
                    	
                    }
                    
                    Engine.getInstance().isOnVerify = false;
                    Engine.getInstance().setUserName(rsp.getData().getUserName());
                    Engine.getInstance().setLongPswd(rsp.getData().getPassword());

                    SpUtil.setLongPswd(rsp.getData().getPassword());
                    SpUtil.setUserName(rsp.getData().getUserName());
                    JSONObject object = loginProcess(Engine.getInstance().getUserName());
                    if (object.optString("rtcode")=="success")
                    {
                        Engine.getInstance().tryToStartNR(SdkService2.this.context);
                    }else{
                    	Log.e(TAG, "LOG IN FAIL");
                    }
                }
            }).start();
        }
        
        if (IAction.USER_VERIFY_FAIL.equals(action))
        {
        	Log.e(TAG, "USER_VERIFY_FAIL");
        }
    }
	public void bleScan(ResultListener listener)
	{
		JSONObject rtObject=new JSONObject();
		if (mBtAdapter==null)
	    {
			try {
				rtObject.put("opt", "bleScan");
				rtObject.put("rtcode", "fail");
				rtObject.put("msg", "该设备不支持蓝牙");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listener.fail(rtObject);
			return;
	    }
		if (!mBtAdapter.isEnabled()) {
			try {
				rtObject.put("opt", "bleScan");
				rtObject.put("rtcode", "fail");
				rtObject.put("msg", "蓝牙没有打开");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listener.fail(rtObject);
			return;
		}
		bleList.clear();	
		mBtAdapter.startLeScan(mLeScanCallback);
		
		try {
			rtObject.put("opt", "bleScan");
			rtObject.put("rtcode", "success");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listener.success(rtObject);
	}
	public void bleStopScan(ResultListener listener)
	{
		JSONObject rtObject=new JSONObject();
		if (mBtAdapter==null)
	    {
			try {
				rtObject.put("opt", "bleScan");
				rtObject.put("rtcode", "fail");
				rtObject.put("msg", "该设备不支持蓝牙");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listener.fail(rtObject);
			return;
	    }
		mBtAdapter.stopLeScan(mLeScanCallback);
		String bleListString="";
		for (int i = 0; i < bleList.size(); i++) {
			bleListString+="\t"+bleList.get(i).getName();
		}
		try {
			rtObject.put("opt", "bleStopScan");
			rtObject.put("rtcode", "success");
			rtObject.put("msg", bleListString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listener.success(rtObject);
	}
	
	private List<UUID> parseUuids(byte[] advertisedData)
    {
        List<UUID> uuids = new ArrayList<UUID>();

        ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2)
        {
            byte length = buffer.get();
            if (length == 0)
            {
                break;
            }

            byte type = buffer.get();
            switch (type)
            {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (length >= 2)
                    {
                        uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
                        length -= 2;
                    }
                    break;
                    
                case 0x04: // Partial list of 32-bit UUIDs
                case 0x05: // Complete list of 32-bit UUIDs
                    while (length >= 4)
                    {
                        uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getInt())));
                        length -= 4;
                    }
                    break;

                case 0x06: // Partial list of 128-bit UUIDs
                case 0x07: // Complete list of 128-bit UUIDs
                    while (length >= 16)
                    {
                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
                        uuids.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                    break;

                default:
                    buffer.position(buffer.position() + length - 1);
                    break;
            }
        }

        return uuids;
    }
	private boolean isBox(byte[] code)
    {
        List<UUID> uuids = parseUuids(code);
        for (UUID uuid : uuids)
        {
            //LogUtil.i(TAG, uuid.toString());
            if (uuid.toString().equals("0000fee9-0000-1000-8000-00805f9b34fb"))
            {
                return true;
            }
        }
        
        return false;
    }
	private LeScanCallback mLeScanCallback = new LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                byte[] scanRecord)
        {            
            if (isBox(scanRecord))
            {      
            	
            	Log.e("", "搜索到盒子");
                //String nameString = device.getName();
                if (!bleList.contains(device)&&device.getName().contains("1B9F")) {
                	bleList.add(device);
				}
            }
        }
    };
	
	
	private JSONObject loginProcess(String strUsrName) //strUsrName 包含cc, 需要预先在Engine.getInstance()中设定cc， 在sp中设定longpwd
    {
		JSONObject rtObject=new JSONObject();
		try {
			rtObject.put("opt", "login");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String cc=Engine.getInstance().getCC();
		final String url = ReleaseConfig.getUrl(cc);
	    //String strFullUserName=cc+strUsrName;
		
		//Engine.getInstance().setUserName(strUsrName);
        //Engine.getInstance().setCC(cc);
                
        //String FullUserName=cc+strUsrName;
        String pswd = SpUtil.getLongPswd(context);
        final LoginRsp login = Engine.getInstance().loginRequest(
                url, strUsrName, pswd, context);
        if (login == null)
        {
            //JSONObject object=new JSONObject();
            try {
            	rtObject.put("rtcode", "网络不给力");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//listener.fail(object);
        	return rtObject;
        }
        	
        if (!login.getBaseRsp().isSuccess())
        {
        	Engine.getInstance().isLogIn = false;
            try {
            	rtObject.put("rtcode", "fail");
            	rtObject.put("msg", "请先验证");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//listener.fail(object);
        	return rtObject;
        }
        else
        {
        	Engine.getInstance().isLogIn = true;
            
        	
        	SpUtil.setCC(Engine.getInstance().getCC());
        	SpUtil.setUserName(strUsrName);
        	SpUtil.setLongPswd(pswd);
        	
        	Engine.getInstance().setUserName(strUsrName);
        	Engine.getInstance().setLongPswd(pswd);
            Engine.getInstance().setCC(cc);
            // 直接登录成功的后续处理
        	
        	TransferClientIdReq.transferclientidReq(null,
        			PushManager.getInstance().getClientid(context)+"",strUsrName);
            
            // 启动同步通讯录
            Engine.getInstance().syncContactThread();
            
            // 获取用户信息
            Engine.getInstance().getUserInfoRequest(
                    Engine.getInstance().getUserName(), context);
            
            //获取群列表
            getGroupList();
            
            // 获取公共状态
//            GetCommStatusRsp rsp = Engine.getInstance().getCommStatusRequest(context, url);
//            Engine.getInstance().syncProfilesThread(rsp);

            LOGManager.d("login成功");
    // 启动消息线程
//            Intent intent = new Intent(LoginActivity.this, TravelService.class);
//            startService(intent);
            
           Engine.getInstance().judgeHomeOrRoam();
           
           GetCommStatusRsp rsp = Engine.getInstance().getCommStatusRequest(context, url);
           Engine.getInstance().syncProfilesThread(rsp);
           
            // 根据homeLogin判断是否获取归属地配置信息还是漫游地配置信息
            if (Engine.getInstance().homeLogin == true)
            {
                // get home profile
                Engine.getInstance().getHomeProfileFromDB(
                        Engine.getInstance().getSimMcc(),
                        Engine.getInstance().getSimMnc());
            }
            else
            {
                // get roam profile
                Engine.getInstance().getRoamProfileFromDB(
                        Engine.getInstance().getSimMcc(),
                        Engine.getInstance().getSimMnc());

                String strRoamGlmsIp = Engine.getInstance().getRoamGlmsLoc();
                if (null == strRoamGlmsIp || strRoamGlmsIp.equals(""))
                {

                    try {
                    	rtObject.put("rtcode", "没有获取到漫游地配置信息");
        			} catch (JSONException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
                	return (rtObject);
                }

                Engine.getInstance().loginRequest(UrlUtil.makeUrl(strRoamGlmsIp),
                        Engine.getInstance().userName, pswd, null);
            }
            
            try {
				rtObject.put("rtcode", "success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return rtObject;
        }
    }
	
	
	private void getGroupList()
    {
        GroupDBHelper gHelper = GroupDBHelper.getInstance();
        if(!gHelper.isGroup(Engine.getInstance().getUserName()))
        {
            GetGroupList getGroupList = Engine.getInstance().getGroupList(context);
            if (getGroupList == null)
            {
                return;
            }
            for(GroupList gList : getGroupList.getDate().getGrouplist()){
                gHelper.insertGroupList(gList);
            }
        }
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
