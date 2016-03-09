package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

public class AppendFast extends BaseResponse implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ResponseInfo responseInfo;
    AppendFast.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public AppendFast.Data getData()
    {
        return data;
    }

    public void setData(AppendFast.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new AppendFast.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public String userName;
        public String reorderId;
        public String balance;

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public String getreorderId()
        {
            return reorderId;
        }

        public void setreorderId(String reorderId)
        {
            this.reorderId = reorderId;
        }

        public String getBalance()
        {
            return balance;
        }

        public void setBalance(String balance)
        {
            this.balance = balance;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
            this.reorderId = jsonObject.optString("tradeNo");
            this.balance = jsonObject.optString("AcountBalance");
        }
    }

}
