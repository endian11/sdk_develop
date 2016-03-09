package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.ConstantValue;
import com.travelrely.core.Engine;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;
import com.travelrely.v2.util.Utils;

public class LoginReq
{
    public static final String formJsonData(String username,
            String pswd, DeviceInfo deviceInfo)
    {
        String result = "";
        try
        {
            JSONObject regist = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            regist.put("data", data);
            data.put("username", username);
            data.put("password", pswd);
            data.put("user_agent",
                    Utils.getVersion(Engine.getInstance().getContext()));
            data.put("platform_id", "1");

            data.put("device_type", deviceInfo.device_type);
            data.put("device_model", deviceInfo.device_model);
            
            data.put("partner_id", ConstantValue.PartnerID);
            data.put("sim_mcc", deviceInfo.sim_mcc);
            
            result = regist.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static LoginRsp login(final Context mContext, String url,
            String usrName, String pswd)
    {
        String urls = url + "api/account/login";
//        System.out.println("我登陆的服务器是：======================= " + url + " api/account/login");
        DeviceInfo deviceInfo = DeviceInfo.getInstance(Engine.getInstance()
                .getContext().getApplicationContext());
        String postdata = formJsonData(usrName, pswd, deviceInfo);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(urls, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        LoginRsp rsp = new LoginRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
