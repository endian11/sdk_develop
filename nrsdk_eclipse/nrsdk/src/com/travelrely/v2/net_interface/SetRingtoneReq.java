package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class SetRingtoneReq
{
    public static final String formJsonRingtone(String usrName, String myRingtone)
    {
        String result = "";
        try
        {
            JSONObject json = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", usrName);
            data.put("crbt_content", myRingtone);

            result = json.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static SetRingtoneRsp setRingtone(final Context mContext,
            String usrName, String myRingtone)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/user/set_crbt_content";

        String postdata = formJsonRingtone(usrName, myRingtone);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }
        
        SetRingtoneRsp setRingtoneRsp = new SetRingtoneRsp();
        setRingtoneRsp.setValue(httpRslt);

        return setRingtoneRsp;
    }
}
