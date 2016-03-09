package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.LOGManager;

public class GetComAdvReq
{
    public static final String formJsonData()
    {
        String result = "";
        try
        {
            JSONObject root = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            root.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));

            result = root.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public static GetComAdvRsp getAdvInfo(final Context mContext)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/adv/get_com_adv";
        
        String postdata = formJsonData();
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        GetComAdvRsp rsp = new GetComAdvRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
