package com.travelrely.v2.net_interface;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class NotifyKiSuccRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    ResponseInfo responseInfo;
    NotifyKiSuccRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public void setBaseRsp(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public NotifyKiSuccRsp.Data getData()
    {
        return data;
    }

    public void setData(NotifyKiSuccRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new NotifyKiSuccRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;
        String userName;
        String result;

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }
        
        public String getRslt()
        {
            return result;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
            this.result = jsonObject.optString("result");
        }
    }
}
