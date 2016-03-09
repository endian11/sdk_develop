package com.travelrely.net.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.v2.response.BaseData;

public class GetVerifyCodeRsp extends BaseResponse implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;
    GetVerifyCodeRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetVerifyCodeRsp.Data getData()
    {
        return data;
    }

    public void setData(GetVerifyCodeRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetVerifyCodeRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        String userName;

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
        }
    }
}
