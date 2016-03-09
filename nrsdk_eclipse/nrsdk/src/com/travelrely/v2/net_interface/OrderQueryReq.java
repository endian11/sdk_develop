package com.travelrely.v2.net_interface;

import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.LOGManager;

/**订单查询请求
 * @author john
 *
 */
public class OrderQueryReq
{
    public static final String formJsonData(String state)
    {
        String result = "";
        try
        {
            JSONObject vertifCode = Request.generateBaseJson();
            JSONObject data = new JSONObject();
            vertifCode.put("data", data);

            data.put("link_source", Integer.toString(DeviceInfo.linkSource));
            data.put("username", Engine.getInstance().getUserName());
            data.put("state", state);

            result = vertifCode.toString();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    /**从服务器上查询所有订单
     * @param mContext
     * @param state
     * @return OrderQuery
     */
    public static OrderQuery queryOrder(final Context mContext, String state)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String url = host + "api/order/query";
        
        String postdata = formJsonData(state);
        HttpConnector httpConnector = new HttpConnector();
        String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                mContext, false);
        if (httpRslt == null || httpRslt.equals(""))
        {
            LOGManager.d("未接收到服务器端的数据");
            return null;
        }

        OrderQuery rsp = new OrderQuery();//查询到的订单放的数据结构
        rsp.setValue(httpRslt);

        return rsp;
    }
}
