package com.travelrely.core.glms.request;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.glms.response.GetVerifyCodeRsp;
import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.LOGManager;

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
