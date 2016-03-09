package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.model.Adv;
import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class GetAdvRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetAdvRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetAdvRsp.Data getData()
    {
        return data;
    }

    public void setData(GetAdvRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetAdvRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        int adv_version;

        List<Adv> adv_list;

        public List<Adv> getAdv_list()
        {
            return adv_list;
        }

        public void setAdv_list(List<Adv> adv_list)
        {
            this.adv_list = adv_list;
        }

        public int getAdv_version()
        {
            return adv_version;
        }

        public void setAdv_version(int adv_version)
        {
            this.adv_version = adv_version;
        }

        @Override
        public String toString()
        {
            return "Data [adv_version=" + adv_version + "]";
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            adv_version = jsonObject.optInt("adv_version");

            JSONArray advListArray = jsonObject.optJSONArray("adv_list");
            if (advListArray != null)
            {
                adv_list = new ArrayList<Adv>();
                for (int i = 0; i < advListArray.length(); i++)
                {
                    JSONObject jsonObject2 = advListArray.optJSONObject(i);
                    Adv adv = new Adv();
                    adv.setName(jsonObject2.optString("name"));
                    adv.setBig_adv_pic(jsonObject2.optString("big_adv_pic"));
                    adv.setSmall_adv_pic(jsonObject2.optString("small_adv_pic"));
                    adv.setWeb_site(jsonObject2.optString("web_site"));
                    adv.setBig_size(jsonObject2.optString("big_size"));
                    adv.setSmall_size(jsonObject2.optString("small_size"));
                    adv_list.add(adv);
                }
            }
        }
    }
}
