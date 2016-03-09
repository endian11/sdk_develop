package com.travelrely.v2.net_interface;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class SetRingtoneRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    ResponseInfo rspInfo;
    SetRingtoneRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return rspInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.rspInfo = responseInfo;
    }

    public SetRingtoneRsp.Data getData()
    {
        return data;
    }

    public void setData(SetRingtoneRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        rspInfo = new ResponseInfo();
        rspInfo.setValue(jsonObject);
        data = new SetRingtoneRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
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
            // TODO Auto-generated method stub
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
        }
    }
}
