package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class GetNRPriceReq
{
    public static final String formJsonData()
    {
        String result = "";
        try
        {
            JSONObject baseJson = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            baseJson.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("version", Integer.toString(0));
            result = baseJson.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static GetNRPriceRsp getNoRoamPrice(final Context mContext)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/operator/get_noroaming_price";
        
        String postdata = formJsonData();
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }
        
        GetNRPriceRsp rsp = new GetNRPriceRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
