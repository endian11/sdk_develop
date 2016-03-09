package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.model.Profile;
import com.travelrely.v2.model.RoamProfile;
import com.travelrely.v2.response.BaseData;

public class GetRoamProfileRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetRoamProfileRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public void setBaseRsp(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetRoamProfileRsp.Data getData()
    {
        return data;
    }

    public void setData(GetRoamProfileRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetRoamProfileRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        int config_version;

        List<Profile> country_list;

        List<RoamProfile> ip_dial_method_list;

        public List<Profile> getCountryList()
        {
            return country_list;
        }

        public void setCountryList(List<Profile> country_list)
        {
            this.country_list = country_list;
        }

        public List<RoamProfile> getIpDialMethodList()
        {
            return ip_dial_method_list;
        }

        public void setIpDialMethodList(
                List<RoamProfile> ip_dial_method_list)
        {
            this.ip_dial_method_list = ip_dial_method_list;
        }

        public int getConfigVersion()
        {
            return config_version;
        }

        public void setConfigVersion(int config_version)
        {
            this.config_version = config_version;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            config_version = jsonObject.optInt("config_version");

            JSONArray countryListArray = jsonObject
                    .optJSONArray("country_list");
            if (countryListArray != null)
            {
                country_list = new ArrayList<Profile>();
                ip_dial_method_list = new ArrayList<RoamProfile>();
                for (int i = 0; i < countryListArray.length(); i++)
                {
                    JSONObject jsonObject2 = countryListArray.optJSONObject(i);
                    Profile profile = new Profile();
                    profile.setMcc(jsonObject2.optString("mcc"));
                    profile.setMnc(jsonObject2.optString("mnc"));
                    profile.setGlms_loc(jsonObject2.optString("roam_glms_loc"));
                    profile.setSmc_loc(jsonObject2.optString("roam_smc_loc"));
                    profile.setCcs_loc(jsonObject2.optString("roam_ccs_loc"));
                    profile.setGpns_loc(jsonObject2.optString("roam_gpns_loc"));
                    profile.setMgw_number1(jsonObject2
                            .optString("roam_mgw_number1"));
                    profile.setMgw_number2(jsonObject2
                            .optString("roam_mgw_number2"));
                    country_list.add(profile);

                    // ip dial method list
                    JSONArray ipDialNumberArray = jsonObject2
                            .optJSONArray("ip_dial_method_list");
                    if (ipDialNumberArray != null)
                    {
                        for (int j = 0; j < ipDialNumberArray.length(); j++)
                        {
                            String ip_dial_number = ipDialNumberArray
                                    .optString(j);
                            RoamProfile profileIpDial = new RoamProfile();
                            profileIpDial.setMcc(jsonObject2.optString("mcc"));
                            profileIpDial.setMnc(jsonObject2.optString("mnc"));
                            profileIpDial.setIp_dial_number(ip_dial_number);
                            ip_dial_method_list.add(profileIpDial);
                        }
                    }
                }
            }
        }
    }
}
