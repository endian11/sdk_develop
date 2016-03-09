package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.SpUtil;

public class GetCountryInfoReq
{
    public static final String formJsonData()
    {
        String result = "";
        try
        {
            JSONObject reqJson = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            reqJson.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", "");
            data.put("version", Integer.toString(SpUtil.getCountryVer()));

            result = reqJson.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public static GetCountryInfoRsp getCountryInfo(final Context mContext)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/config/get_country_info";
        
        String postdata = formJsonData();
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }
        
        GetCountryInfoRsp rsp = new GetCountryInfoRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
