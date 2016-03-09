package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.util.LOGManager;

public class GetKiReq
{
    public static final String formJsonData(String ul01otadata)
    {
        String result = "";
        try
        {
            JSONObject json = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("username", Engine.getInstance().getUserName());
            data.put("ul01otadata", ul01otadata);
            /*===========服务器增加一个tripid字段==============*/
            int tripidPos = Engine.getInstance().getTripIdPos();
            String tripid = Engine.getInstance().getTripIds().get(tripidPos);
            if (!TextUtils.isEmpty(tripid)){
            	data.put("tripid", tripid);//tripId
            }else{
            	LOGManager.e("tripId字段为空"+tripid);
            }
            /*==================END=========================*/
            result = json.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static GetKiRsp getKi(final Context mContext, String ul01otadata)
    {
        String url = ConstantValue.ShaoHaoUrl+ ConstantValue.GetKiPath;
        
        String postdata = formJsonData(ul01otadata);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        GetKiRsp rsp = new GetKiRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
