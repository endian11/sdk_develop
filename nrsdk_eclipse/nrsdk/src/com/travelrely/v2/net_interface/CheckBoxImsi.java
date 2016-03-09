package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.ConstantValue;
import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.LOGManager;

public class CheckBoxImsi {
	 public static final String formJsonData(String username,String imsi)
	 {
	        String result = "";
	        try
	        {
	            JSONObject regist = Request.generateBaseJson();
	            JSONObject data = new JSONObject();
	            regist.put("data", data);
	            data.put("username", username);
	            data.put("partner_id", ConstantValue.PartnerID);
	            data.put("platform_id", "1");
	            data.put("imsi", imsi);//盒子内sim卡的imsi
	            
	            result = regist.toString();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        return result;
	    }
	 
	 
	 public static CheckImsiRsp checkImsi(final Context mContext,String imsi,
	            String usrName){
		 	String cc = Engine.getInstance().getCC();
	        String host = ReleaseConfig.getUrl(cc);
	        String url = host + "api/user/check_box_imsi";
	        
	        String postdata = formJsonData(usrName, imsi);
	        HttpConnector httpConnector = new HttpConnector();
	        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
	                mContext, false);
	        if (httpRslt == null || httpRslt.equals(""))
	        {
	            LOGManager.d("未接收到服务器端的数据");
	            return null;
	        }

	        CheckImsiRsp rsp = new CheckImsiRsp();
	        rsp.setValue(httpRslt);

	        return rsp;
	        
	 }
	 
}
