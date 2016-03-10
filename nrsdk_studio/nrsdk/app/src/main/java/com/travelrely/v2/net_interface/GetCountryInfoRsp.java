package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.model.CountryInfo;
import com.travelrely.v2.response.BaseData;

public class GetCountryInfoRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetCountryInfoRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetCountryInfoRsp.Data getData()
    {
        return data;
    }

    public void setData(GetCountryInfoRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetCountryInfoRsp.Data();
        JSONObject dataJson = jsonObject.optJSONObject("data");
        data.setValue(dataJson);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        int iCountryInfoVer;

        List<CountryInfo> countryInfoList;

        public List<CountryInfo> getCountryInfoList()
        {
            return countryInfoList;
        }

        public void setCountryInfoList(List<CountryInfo> countryInfoList)
        {
            this.countryInfoList = countryInfoList;
        }

        public int getCountryInfoVersion()
        {
            return iCountryInfoVer;
        }

        public void setCountryInfoVersion(int iCountryInfoVer)
        {
            this.iCountryInfoVer = iCountryInfoVer;
        }

        @Override
        public String toString()
        {
            return "Data [country_info_version = " + iCountryInfoVer + "]";
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            iCountryInfoVer = jsonObject.optInt("country_info_version");

            JSONArray countryListArray = jsonObject
                    .optJSONArray("country_info_list");
            if (countryListArray == null)
            {
                return;
            }

            countryInfoList = new ArrayList<CountryInfo>();
            for (int i = 0; i < countryListArray.length(); i++)
            {
                JSONObject jsonObject2 = countryListArray.optJSONObject(i);
                CountryInfo countryInfo = new CountryInfo();

                countryInfo.setCC(jsonObject2.optString("cc"));
                countryInfo.setCountryName(jsonObject2
                        .optString("country_name"));
                countryInfo.setCountryShortName(jsonObject2
                        .optString("country_short_name"));
                countryInfo.setMcc(jsonObject2.optString("mcc"));
                countryInfo.setMnc(jsonObject2.optString("mnc"));
                countryInfo.setCarrierName(jsonObject2
                        .optString("carrier_name"));
                countryInfo.setNationalFlag(jsonObject2
                        .optString("national_flag"));
                countryInfo.setWebsiteUrl(jsonObject2.optString("website_url"));
                countryInfo.setPkgType(jsonObject2.optInt("packagetype"));
                countryInfo.setPkgDesc(jsonObject2.optString("prepackagedesc"));

                countryInfoList.add(countryInfo);
            }
        }
    }
}
