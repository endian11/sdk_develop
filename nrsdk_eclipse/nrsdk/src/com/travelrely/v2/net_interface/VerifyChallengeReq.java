package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.LOGManager;

public class VerifyChallengeReq
{
    public static final String formJsonData(String userName, String strPswd)
    {
        String result = "";
        try
        {
            JSONObject verifychallenge = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            verifychallenge.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", userName);
            data.put("challenge", strPswd);

            result = verifychallenge.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static VerifyChallengeRsp verifyChallenge(final Context mContext,
            String userName, String strPswd)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/account/verify_challenge";
        
        String postdata = formJsonData(userName, strPswd);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        VerifyChallengeRsp rsp = new VerifyChallengeRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
