package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.LOGManager;

public class VerifySuccReq
{
    public static final String formJsonData(String userName, int flag)
    {
        String result = "";
        try
        {
            JSONObject verify = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            verify.put("data", data);
            data.put("username", userName);
            //data.put("password", strPswd);
            data.put("verifyflag", Integer.toString(flag));

            result = verify.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static VerifySuccRsp verifySucc(final Context mContext,
            String userName, int flag)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/account/verify_success";
        
        String postdata = formJsonData(userName, flag);
        
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        VerifySuccRsp rsp = new VerifySuccRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
