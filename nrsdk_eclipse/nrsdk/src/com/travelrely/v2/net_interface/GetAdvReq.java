package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.LOGManager;

public class GetAdvReq
{
    public static final String formJsonData(int lateral_resolution,
            int axial_resolution)
    {
        String result = "";
        try
        {
            JSONObject root = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            root.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().userName);
            data.put("lateral_resolution", Integer.toString(lateral_resolution));
            data.put("axial_resolution", Integer.toString(axial_resolution));

            result = root.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public static GetAdvRsp getAdvInfo(final Context mContext,
            int lateral_resolution,
            int axial_resolution)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/adv/get_adv";
        
        String postdata = formJsonData(lateral_resolution, axial_resolution);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        GetAdvRsp rsp = new GetAdvRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
