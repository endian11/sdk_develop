package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.LOGManager;

public class CancelVsimReq
{
    public static final String formJsonData(String cardsn)
    {
        String result = "";
        try
        {
            JSONObject json = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("username", Engine.getInstance().getUserName());
            data.put("cardsn", cardsn);
            result = json.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static CancelVsimRsp getCancelOta(final Context mContext,
            String cardsn)
    {
        String url = "http://210.51.190.111/tps/getcancelota";
        
        String postdata = formJsonData(cardsn);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        CancelVsimRsp rsp = new CancelVsimRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
