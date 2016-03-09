package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class GetUsrInfoReq
{
    public static final String formJsonData(String usrName)
    {
        String result = "";
        try
        {
            JSONObject json = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", usrName);

            result = json.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static GetUsrInfoRsp getUsrInfo(final Context mContext,
            String usrName)
    {
        String cc = Engine.getInstance().getCC();
        String url = ReleaseConfig.getUrl(cc) + "api/user/get_info";
        String postdata = formJsonData(usrName);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }
        
        GetUsrInfoRsp rsp = new GetUsrInfoRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
