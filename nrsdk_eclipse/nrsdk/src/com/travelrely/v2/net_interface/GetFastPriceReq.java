package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class GetFastPriceReq
{
    public static final String formJsonData(String userName, String mcc,
            String mnc)
    {
        String result = "";
        try
        {
            JSONObject verifychallenge = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            verifychallenge.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", userName);
            data.put("mcc", mcc);
            data.put("mnc", mnc);

            result = verifychallenge.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static GetFastPriceRsp getFastPrice(final Context mContext,
            String usrName, String mcc, String mnc)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/order/getpricefast";
        
        String postdata = formJsonData(usrName, mcc, mnc);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }
        
        GetFastPriceRsp rsp = new GetFastPriceRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
