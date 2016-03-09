package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.LOGManager;
import com.travelrely.v2.util.UrlUtil;

public class TransferClientIdReq {
	 public static final String formJsonData(String username,String clientid )
	    {
	        String result = "";
	        try
	        {
	        	JSONObject root = Request.generateBaseJson();
				JSONObject data = new JSONObject();
				root.put("data", data);
				data.put("username", username);
				data.put("client_id", clientid);

	            result = root.toString();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    public static String transferclientidReq(final Context mContext, String clientid,
	            String usrName)
	    {
//	        String urls = UrlUtil.url("api/message/set_client_id");
	    	 String cc = Engine.getInstance().getCC();
	         String host = ReleaseConfig.getUrl(cc);
	    	String urls = host+"api/message/set_client_id";
	    	LOGManager.d("clientid url: " + urls);
	        String postdata = formJsonData(usrName,clientid);
	        LOGManager.d("clientid postdata: " + postdata);
	        HttpConnector httpConnector = new HttpConnector();
	        String httpRslt = httpConnector.requestByHttpPut(urls, postdata,
	                mContext, false);
	        if (httpRslt == null || httpRslt.equals(""))
	        {
	            LOGManager.d("未接收到服务器端的数据");
	            return null;
	        }


	        return httpRslt;
	    }
}
