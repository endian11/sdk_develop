package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.Utils;
import com.travelrely.v2.response.ShopHomeRsp;

public class SingleCardInfoReq {
	public static final String formJsonData(String productId
	           )
	    {
	        String result = "";
	        try
	        {
	            JSONObject regist = Request.generateBaseJson();
	            JSONObject data = new JSONObject();
	            regist.put("data", data);
	            data.put("produc_id", productId);
	            data.put("user_agent",
	                    Utils.getVersion(Engine.getInstance().getContext()));
	            data.put("platform_id", "1");


	            result = regist.toString();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    /**
	     * @param mContext
	     * @param productId 单个商品Id
	     * @return
	     */
	    public static String singleCardInfoReq(final Context mContext, String productId)
	    {
	    	 String cc = Engine.getInstance().getCC();
	         String host = ReleaseConfig.getUrl(cc);
	        String urls = host + "api/shop/get_productinfo";
	        String postdata = formJsonData(productId);
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
