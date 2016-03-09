package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.Utils;

/**
 * @author john
 *获取更多商品信息接口
 */
public class PutongCardReq {
	 public static final String formJsonData(String product_type_id)
	    {
	        String result = "";
	        try
	        {
	            JSONObject json = Request.generateBaseJson();
	            JSONObject data = new JSONObject();
	            json.put("data", data);
	            data.put("product_type_id", product_type_id);
	            data.put("user_agent",
	                    Utils.getVersion(Engine.getInstance().getContext()));
	            data.put("platform_id", "1");

	            result = json.toString();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    /**获取更多商品信息
	     * @param mContext
	     * @param id productTypeId
	     * @return
	     */
	    public static String getMoreProInfo(final Context mContext, String id)
	    {
	        String cc = Engine.getInstance().getCC();
	        String host = ReleaseConfig.getUrl(cc);
	        String url = host + "api/shop/get_more_prodcutinfo";
	        
	        String postdata = formJsonData(id);
	        HttpConnector httpConnector = new HttpConnector();
	        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
	                mContext, false);
	        if (httpRslt == null || httpRslt.equals(""))
	        {
	            LOGManager.d("未接收到服务器端的数据");
	            return null;
	        }


	        return httpRslt;
	    }
}
