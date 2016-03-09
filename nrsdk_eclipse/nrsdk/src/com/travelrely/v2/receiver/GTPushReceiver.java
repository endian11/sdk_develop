package com.travelrely.v2.receiver;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.travelrely.core.App;
import com.travelrely.core.ConstantValue;
import com.travelrely.core.Engine;
import com.travelrely.core.nr.NRService;
import com.travelrely.core.nr.msg.MsgId;
import com.travelrely.v2.GetMsg.FetchMessage;
import com.travelrely.v2.util.LOGManager;
import com.travelrely.v2.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GTPushReceiver extends BroadcastReceiver {
	  /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    
    public FetchMessage mFetchMsg = new FetchMessage();

	@Override
	public void onReceive(Context context, Intent intent) {
		  Bundle bundle = intent.getExtras();
	        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

	        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
	            case PushConsts.GET_MSG_DATA:
	                // 获取透传数据
	                // String appid = bundle.getString("appid");
	                byte[] payload = bundle.getByteArray("payload");

	                String taskid = bundle.getString("taskid");
	                String messageid = bundle.getString("messageid");

	                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
	                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
	                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

	                if (payload != null) {
	                    String data = new String(payload);

	                    LogUtil.i("GetuiSdkDemo", "receiver payload : " + data);

	                    payloadData.append(data);
	                    payloadData.append("\n");
	                    LOGManager.e("启动nrservice");
	                    if (ConstantValue.FetchMsg.equals(data)){
	                    	//NR服务已启动，则是服务器告诉我要取消息
	                    	if (mFetchMsg != null){
	                    		LogUtil.i("mFetchMsg != null 收到取消息 ", data);
	                    		mFetchMsg.fetchMsg();
	                    	}else{
	                    		LogUtil.i("mFetchMsg == null 收到取消息 ", data);
	                    		mFetchMsg = new FetchMessage();
	                    		mFetchMsg.fetchMsg();
	                    	}
	                    }else if(ConstantValue.INComingCall.equals(data)){
	                    	//服务未启动，则启动NR服务
//	                    	Engine.getInstance().startNR(context);
	                    	Engine.getInstance().startNRService(context, MsgId.APP_GETUI_IND);
	                    	LogUtil.i("启动nr服务  收到取消息 ", data);
	                    	mFetchMsg.fetchMsg();
	                    }
	                    
//	                    if (GetuiSdkDemoActivity.tLogView != null) {
//	                        GetuiSdkDemoActivity.tLogView.append(data + "\n");
//	                    }
	                }
	                break;

	            case PushConsts.GET_CLIENTID:
	                // 获取ClientID(CID)
	                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
	                String cid = bundle.getString("clientid");
//	                if (GetuiSdkDemoActivity.tView != null) {
//	                    GetuiSdkDemoActivity.tView.setText(cid);
//	                }
	                break;

	            case PushConsts.THIRDPART_FEEDBACK:
	                /*
	                 * String appid = bundle.getString("appid"); String taskid =
	                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
	                 * String result = bundle.getString("result"); long timestamp =
	                 * bundle.getLong("timestamp");
	                 * 
	                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
	                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
	                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
	                 */
	                break;

	            default:
	                break;
	        }

	}

}
