package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class GetPackagesReq
{
    public static final String formJsonData(String userName, String mcc)
    {
        String result = "";
        try
        {
            JSONObject get_customized_packages = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            get_customized_packages.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", userName);
            data.put("mcc", mcc);
            
            result = get_customized_packages.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public static GetPackagesRsp getPackages(final Context mContext,
            String usrName, String mcc)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/operator/get_price_packages";
        
        String postdata = formJsonData(usrName, mcc);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }
        
        GetPackagesRsp rsp = new GetPackagesRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
