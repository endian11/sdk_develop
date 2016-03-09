package com.travelrely.net.request;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.net.response.VerifyRsp;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;
import com.travelrely.v2.util.Utils;

public class VerifyReq {
	
	private static final String api = "api/account/verify";
	
    public static final int VERIFY_BY_PHONE = 0;
    public static final int VERIFY_BY_BOX = 1;
    public static final int VERIFY_BY_OTHER = 2;
    public static final int VERIFY_BY_REGIST = 3;
    
    public static final String formJsonData(String userName, String code,
            int type, DeviceInfo deviceInfo) {
        String result = "";
        try {
            JSONObject verify = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            verify.put("data", data);
            data.put("regtype", Integer.toString(type));
            data.put("username", userName);
            data.put("user_agent",
                    Utils.getVersion(Engine.getInstance().getContext()));
            data.put("platform_id", "1");
            data.put("device_type", deviceInfo.device_type);
            data.put("verification_code", code);
            //data.put("ipaddress", NetUtil.getPhoneIp());

            result = verify.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static VerifyRsp verify(final Context mContext,
            String userName, String code, int type) {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + api;

        DeviceInfo deviceInfo = DeviceInfo.getInstance(Engine.getInstance()
                .getContext().getApplicationContext());
        
        String postdata = formJsonData(userName, code, type, deviceInfo);
        
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals("")) {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        VerifyRsp rsp = new VerifyRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
