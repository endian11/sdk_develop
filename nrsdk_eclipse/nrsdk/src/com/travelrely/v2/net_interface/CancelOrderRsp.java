package com.travelrely.v2.net_interface;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class CancelOrderRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;
    CancelOrderRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public CancelOrderRsp.Data getData()
    {
        return data;
    }

    public void setData(CancelOrderRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new CancelOrderRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        String id;

        String balance;

        public String getBalance()
        {
            return balance;
        }

        public void setBalance(String balance)
        {
            this.balance = balance;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);

            JSONObject jo = jsonObject.optJSONObject("order");
            id = jo.optString("id");
            balance = jo.optString("balance");
        }
    }
}
