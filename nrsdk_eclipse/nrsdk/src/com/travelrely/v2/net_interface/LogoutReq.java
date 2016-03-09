package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class LogoutReq
{
    public static final String formJsonData()
    {
        String result = "";
        try
        {
            JSONObject json = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            result = json.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static LogoutRsp logout(final Context mContext)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/account/logout";
        
        String postdata = formJsonData();
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        LogoutRsp rsp = new LogoutRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
