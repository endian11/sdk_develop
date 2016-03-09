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

public class GetHomeProfileRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetHomeProfileRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public void setBaseRsp(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetHomeProfileRsp.Data getData()
    {
        return data;
    }

    public void setData(GetHomeProfileRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetHomeProfileRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        int config_version;

        String home_ios_femtoip;

        String home_android_femtoip;

        List<Profile> carrier_list;

        List<RoamProfile> ip_dial_method_list;

        public List<Profile> getCarrierList()
        {
            return carrier_list;
        }

        public void setCarrierList(List<Profile> carrier_list)
        {
            this.carrier_list = carrier_list;
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

        public String getIosIp()
        {
            return home_ios_femtoip;
        }

        public void setIosIp(String home_ios_femtoip)
        {
            this.home_ios_femtoip = home_ios_femtoip;
        }

        public String getAndIp()
        {
            return home_android_femtoip;
        }

        public void setAndIp(String home_and_femtoip)
        {
            this.home_android_femtoip = home_and_femtoip;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            config_version = jsonObject.optInt("config_version");

            home_ios_femtoip = jsonObject.optString("home_ios_femtoip");
            home_android_femtoip = jsonObject.optString("home_android_femtoip");

            JSONArray jsonArray = jsonObject.optJSONArray("carrier_list");
            if (jsonArray != null)
            {
                carrier_list = new ArrayList<Profile>();
                ip_dial_method_list = new ArrayList<RoamProfile>();
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                    Profile profile = new Profile();
                    profile.setMcc(jsonObject2.optString("mcc"));
                    profile.setMnc(jsonObject2.optString("mnc"));
                    profile.setSmc_loc(jsonObject2.optString("home_smc_loc"));
                    profile.setCcs_loc(jsonObject2.optString("home_ccs_loc"));
                    profile.setGpns_loc(jsonObject2.optString("home_gpns_loc"));
                    profile.setMgw_number1(jsonObject2
                            .optString("home_mgw_number1"));
                    profile.setMgw_number2(jsonObject2
                            .optString("home_mgw_number2"));
                    carrier_list.add(profile);

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
