package com.travelrely.net.request;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.ConstantValue;
import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.net.response.GetVerifyCodeRsp;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class GetVerifyCodeReq {
	
	private static final String api = "api/account/get_verif_code";
	
    public static final String formJsonData(String usrName) {
        String result = "";
        try {
            JSONObject verifyCode = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            verifyCode.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", usrName);
            data.put("partner_id", ConstantValue.PartnerID);

            result = verifyCode.toString();
        } catch (Exception e) {
        }

        return result;
    }

    public static GetVerifyCodeRsp getVerifyCode(final Context mContext,
            String usrName) {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + api;
        
        String postdata = formJsonData(usrName);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals("")) {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }
        
        GetVerifyCodeRsp rsp = new GetVerifyCodeRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
