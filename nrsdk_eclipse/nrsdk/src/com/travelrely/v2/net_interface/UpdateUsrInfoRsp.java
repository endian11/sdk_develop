package com.travelrely.v2.net_interface;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class UpdateUsrInfoRsp extends BaseResponse implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    ResponseInfo responseInfo;
    UpdateUsrInfoRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public UpdateUsrInfoRsp.Data getData()
    {
        return data;
    }

    public void setData(UpdateUsrInfoRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new UpdateUsrInfoRsp.Data();
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
            // TODO Auto-generated method stub
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
        }
    }
}
