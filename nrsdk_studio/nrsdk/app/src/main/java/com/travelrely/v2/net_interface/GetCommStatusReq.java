package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.Utils;

public class GetCommStatusReq
{
    public static final String formJsonData(final Context c)
    {
        String result = "";
        try
        {
            JSONObject root = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            root.put("data", data);
            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("platform_id", "1");
            data.put("sim_mcc", DeviceInfo.getInstance(c).sim_mcc);
            data.put("user_agent", Utils.getVersion(c));
            data.put("home_config_version", Integer.toString(SpUtil
                    .getHomeProfileVer()));
            data.put("roam_config_version", Integer.toString(SpUtil
                    .getRoamProfileVer()));
            data.put("express_price_version", Integer.toString(SpUtil
                    .getExpPriceVer()));
            data.put("adv_version", Integer.toString(SpUtil.getAdvVer()));
            data.put("country_info_version", Integer.toString(SpUtil
                    .getCountryVer()));
            data.put("package_version", Integer.toString(SpUtil.getPkgVer()));
            //=====6/29==
            data.put("order_version", Integer.toString(SpUtil.getOrderVersion()));
            
            data.put("nrs_info_version", Integer.toString(SpUtil.getNRInfoVer()));
            data.put("user_roaminfo_version", Integer.toString(SpUtil.getUsrRoamProfileVer())); 
            
            data.put("server_info_version", Integer.toString(SpUtil
                    .getHomeProfileVer()));
            result = root.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public static GetCommStatusRsp getCommStatus(final Context mContext,
            String url)
    {
        url = url + "api/user/get_common_status";
        String postdata = formJsonData(mContext);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        GetCommStatusRsp rsp = new GetCommStatusRsp();
        rsp.setValue(httpRslt);

        return rsp;
    }
}
