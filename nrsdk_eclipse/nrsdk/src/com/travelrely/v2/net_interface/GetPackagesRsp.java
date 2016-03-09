package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;
import com.travelrely.v2.response.Package;

public class GetPackagesRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;
    GetPackagesRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public void setBaseRsp(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetPackagesRsp.Data getData()
    {
        return data;
    }

    public void setData(GetPackagesRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetPackagesRsp.Data();
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

        String mcc;

        String countryname;

        public String getCountryname()
        {
            return countryname;
        }

        public void setCountryname(String countryname)
        {
            this.countryname = countryname;
        }

        Package crbtPkg;
        List<Package> packages;
        List<Package> packagesVoice;

        public Package getCrbtPkg()
        {
            return crbtPkg;
        }

        public void setCrbtPkg(Package pkgs)
        {
            this.crbtPkg = pkgs;
        }

        public List<Package> getPackagesVoice()
        {
            return packagesVoice;
        }

        public void setPackagesVoice(List<Package> packagesVoice)
        {
            this.packagesVoice = packagesVoice;
        }

        public List<Package> getPackages()
        {
            return packages;
        }

        public void setPackages(List<Package> packages)
        {
            this.packages = packages;
        }

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
            this.mcc = jsonObject.optString("mcc");
            this.countryname = jsonObject.optString("countryname");

            packages = new ArrayList<Package>();
            packagesVoice = new ArrayList<Package>();

            JSONObject crbtPkgJsonObject = jsonObject
                    .optJSONObject("crbt_price");
            if (crbtPkgJsonObject != null)
            {
                crbtPkg = new Package();
                crbtPkg.setCarriername(crbtPkgJsonObject
                        .optString("carriername"));
                crbtPkg.setMnc(crbtPkgJsonObject.optString("mnc"));
                crbtPkg.setPackageprice(crbtPkgJsonObject
                        .optDouble("packageprice"));
                crbtPkg.setPackageid(crbtPkgJsonObject.optString("packageid"));
                crbtPkg.setPackagecurrency(crbtPkgJsonObject
                        .optInt("packagecurrency"));
            }

            JSONArray packageJsonObjects = jsonObject
                    .optJSONArray("data_price");
            if (packageJsonObjects != null)
            {
                for (int i = 0; i < packageJsonObjects.length(); i++)
                {
                    JSONObject jsonObject2 = packageJsonObjects
                            .optJSONObject(i);

                    Package package1 = new Package();
                    package1.setValue(jsonObject2);

                    packages.add(package1);

                }
            }

            JSONArray packageVoicJsonObjects = jsonObject
                    .optJSONArray("voice_price");
            if (packageVoicJsonObjects != null)
            {
                for (int i = 0; i < packageVoicJsonObjects.length(); i++)
                {
                    JSONObject jObject = packageVoicJsonObjects
                            .optJSONObject(i);

                    Package pVoic = new Package();
                    pVoic.setValueVoic(jObject);
                    packagesVoice.add(pVoic);
                }
            }
        }
    }
}
