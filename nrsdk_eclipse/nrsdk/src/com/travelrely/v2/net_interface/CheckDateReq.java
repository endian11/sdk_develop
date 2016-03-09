package com.travelrely.v2.net_interface;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.model.SimInfo;
import com.travelrely.v2.model.UsrSimInfo;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class CheckDateReq
{
    public static final String formJsonData(String begin, String end, int type,
            List<UsrSimInfo> nums)
    {
        String result = "";
        try
        {
            JSONObject jsonObj = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            jsonObj.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("begin_date", begin);
            data.put("end_date", end);
            data.put("check_type", Integer.toString(type));
            
            JSONArray numArray = new JSONArray();
            for (UsrSimInfo num : nums)
            {
                JSONObject usr = new JSONObject();
                usr.put("simcard_type", Integer.toString(num.getSimType()));
                usr.put("simcard_size", Integer.toString(num.getSimSize()));
                usr.put("card_user", num.getPhone());
                
                numArray.put(usr);
            }
            
            data.put("num_list", numArray);

            result = jsonObj.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
    public static final String formJsonData1(String begin, String end, int type,
    		SimInfo nums)
    {
    	String result = "";
    	try
    	{
    		JSONObject jsonObj = Request.generateBaseJson();
    		JSONObject data = new JSONObject();
    		jsonObj.put("data", data);
    		data.put("link_source", Integer.toString(DeviceInfo.linkSource));
    		data.put("username", Engine.getInstance().getUserName());
    		data.put("begin_date", begin);
    		data.put("end_date", end);
    		data.put("check_type", Integer.toString(type));
    		
    		JSONArray numArray = new JSONArray();
    			JSONObject usr = new JSONObject();
//    			usr.put("simcard_type", Integer.toString(num.getSimType()));
    			usr.put("simcard_type", type);
    			usr.put("simcard_size", Integer.toString(nums.getSimcard_size()));
    			usr.put("card_user", nums.getCardUser());
    			
    			numArray.put(usr);
    		
    		data.put("num_list", numArray);
    		
    		result = jsonObj.toString();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	return result;
    }

    public static CheckDateRsp checkDate(final Context mContext,
            String begin, String end, int type, List<UsrSimInfo> nums)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/order/checkdate";
        
        String postdata = formJsonData(begin, end, type, nums);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        CheckDateRsp rsp = new CheckDateRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
//    public static CheckDateRsp checkDate(final Context mContext,
//    	 int type, SimInfo nums)
//    {
//    	String cc = Engine.getInstance().getCC();
//    	String host = ReleaseConfig.getUrl(cc);
//    	String url = host + "api/order/checkdate";
//    	
//    	String postdata = formJsonData1(nums.getBeginDate(), nums.getEndDate(), type, nums);
//    	HttpConnector httpConnector = new HttpConnector();
//    	String httpRslt = httpConnector.requestByHttpPut(url, postdata,
//    			mContext, false);
//    	if (httpRslt == null || httpRslt.equals(""))
//    	{
//    		LOGManager.d("未接收到服务器端的数据");
//    		return null;
//    	}
//    	
//    	CheckDateRsp rsp = new CheckDateRsp();
//    	rsp.setValue(httpRslt);
//    	
//    	return rsp;
//    }
}
