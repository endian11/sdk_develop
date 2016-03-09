package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.model.ServiceNum;
import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class GetServiceNumRsp extends BaseResponse implements
        Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo baseRspInfo;

    GetServiceNumRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return baseRspInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.baseRspInfo = responseInfo;
    }

    public GetServiceNumRsp.Data getData()
    {
        return data;
    }

    public void setData(GetServiceNumRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        baseRspInfo = new ResponseInfo();
        baseRspInfo.setValue(jsonObject);
        data = new GetServiceNumRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        int version;

        List<ServiceNum> publicNumList;


        public int getVersion()
        {
            return version;
        }

        public void setVersion(int version)
        {
            this.version = version;
        }
        
        public List<ServiceNum> getServNumList()
        {
            return publicNumList;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            version = jsonObject.optInt("version");

            JSONArray cListArray = jsonObject.optJSONArray("country_list");
            if (cListArray == null)
            {
                return;
            }

            publicNumList = new ArrayList<ServiceNum>();

            for (int i = 0; i < cListArray.length(); i++)
            {
                JSONObject jsonObject2 = cListArray.optJSONObject(i);
                JSONArray numListArray = jsonObject2.optJSONArray("num_list");
                if (numListArray == null)
                {
                    continue;
                }
                
                for (int j = 0; j < numListArray.length(); j++)
                {
                    JSONObject numJsonObj = numListArray.optJSONObject(j);

                    ServiceNum profile = new ServiceNum();
                    profile.setCountryName(jsonObject2.optString("countryname"));
                    profile.setNumName(numJsonObj.optString("name"));
                    profile.setNum(numJsonObj.optString("num"));
                    profile.setFlag(numJsonObj.optString("flag"));
                    
                    publicNumList.add(profile);
                }                 
            }
        }
    }
}
