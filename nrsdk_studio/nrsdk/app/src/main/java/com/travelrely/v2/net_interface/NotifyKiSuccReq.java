package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.util.LOGManager;

public class NotifyKiSuccReq
{
    public static final String formJsonData(String ul02otadata)
    {
        String result = "";
        try
        {
            JSONObject json = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            json.put("data", data);
            data.put("username", Engine.getInstance().getUserName());
            data.put("ul02otadata", ul02otadata);
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

    public static NotifyKiSuccRsp notifySucc(final Context mContext,
            String ul02otadata)
    {
        String url = ConstantValue.ShaoHaoUrl + ConstantValue.NotifyKiSuccess;
        
        String postdata = formJsonData(ul02otadata);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        NotifyKiSuccRsp rsp = new NotifyKiSuccRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
