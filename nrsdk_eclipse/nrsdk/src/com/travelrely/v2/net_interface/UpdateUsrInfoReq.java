package com.travelrely.v2.net_interface;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.net.HttpConnector;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.net_interface.GetUsrInfoRsp.ShipmentInfo;
import com.travelrely.v2.util.DeviceInfo;
import com.travelrely.v2.util.LOGManager;

public class UpdateUsrInfoReq
{
    /**
     * 更新用户信息
     */
    private static final String formUpdatePersonInfo(String newNickName,
            String head_portrait, String mobile_phone)
    {
        String result = "";
        try
        {
            JSONObject baseJson = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            baseJson.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());

            JSONObject userinfo = new JSONObject();
            data.put("user_info", userinfo);

            JSONObject personInfo = new JSONObject();
            userinfo.put("personal_info", personInfo);

            personInfo.put("nick_name", newNickName);
            personInfo.put("head_portrait", head_portrait);
            personInfo.put("mobile_phone", mobile_phone);

            result = baseJson.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }
    
    public static UpdateUsrInfoRsp updatePersonInfo(final Context mContext,
            String nickname, String head, String phone)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/user/update_info";
        
        String postdata = formUpdatePersonInfo(nickname, head, phone);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        UpdateUsrInfoRsp rsp = new UpdateUsrInfoRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }

    private static final String formUpdateShipInfo(List<ShipmentInfo> list)
    {
        String result = "";
        try
        {
            JSONObject httpReqJson = Request.generateBaseJson();

            JSONObject data = new JSONObject();
            httpReqJson.put("data", data);

            data.put("link_source", DeviceInfo.linkSource);
            data.put("username", Engine.getInstance().getUserName());

            JSONObject userinfo = new JSONObject();
            data.put("user_info", userinfo);

            JSONArray shipmentInfo = new JSONArray();
            userinfo.put("shipment_info", shipmentInfo);

            for (ShipmentInfo s : list)
            {
                JSONObject si = new JSONObject();
                si.put("id", s.getId());
                si.put("consignee", s.getConsignee());
                si.put("country", s.getCountry());
                si.put("state", s.getState());
                si.put("city", s.getCity());
                si.put("county", s.getCounty());
                si.put("address", s.getAddress());
                si.put("postal_code", s.getPostalCode());
                si.put("phone_number", s.getPhoneNum());
                shipmentInfo.put(si);
            }

            result = httpReqJson.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public static UpdateUsrInfoRsp updateShipmentInfo(final Context mContext,
            List<ShipmentInfo> list)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/user/update_info";
        
        String postdata = formUpdateShipInfo(list);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        UpdateUsrInfoRsp rsp = new UpdateUsrInfoRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
    
    private static final String formDelShipInfo(List<ShipmentInfo> list)
    {
        String result = "";
        try
        {
            JSONObject httpReqJson = Request.generateBaseJson();

            JSONObject data = new JSONObject();
            httpReqJson.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());

            JSONObject userinfo = new JSONObject();
            data.put("user_info", userinfo);

            JSONArray shipmentInfo = new JSONArray();
            userinfo.put("shipment_info", shipmentInfo);

            for (ShipmentInfo s : list)
            {
                JSONObject si = new JSONObject();
                si.put("del_id", s.getId());

                shipmentInfo.put(si);
            }

            result = httpReqJson.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }
    
    public static UpdateUsrInfoRsp delShipmentInfo(final Context mContext,
            List<ShipmentInfo> list)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/user/update_info";

        String postdata = formDelShipInfo(list);

        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        UpdateUsrInfoRsp rsp = new UpdateUsrInfoRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
