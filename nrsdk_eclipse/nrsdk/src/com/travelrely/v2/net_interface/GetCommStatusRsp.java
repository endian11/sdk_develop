package com.travelrely.v2.net_interface;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;
import com.travelrely.v2.util.SpUtil;

public class GetCommStatusRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetCommStatusRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public void setBaseRsp(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetCommStatusRsp.Data getData()
    {
        return data;
    }

    public void setData(GetCommStatusRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetCommStatusRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        private String username;
        private int home_config_flag;
        private int roam_config_flag;
        private int expressprice_change_flag;
        private int adv_update_flag;
        private int countryinfo_update_flag;
        private int appversion_update_flag;
        private int package_update_flag;
        private int order_update_flag;
        private int nrsinfo_update_flag;
        private int userroaminfo_update_flag;
        private int serverinfo_update_flag;
        private int is_visiting;
        private int is_roaming;
        
        
        public int getHPChangeFlag()
        {
            return home_config_flag;
        }

        public void setHPChangeFlag(int home_config_flag)
        {
            this.home_config_flag = home_config_flag;
        }

        public int getEpChangeFlag()
        {
            return expressprice_change_flag;
        }

        public void setEpChangeFlag(int expressprice_change_flag)
        {
            this.expressprice_change_flag = expressprice_change_flag;
        }

        public int getOrder_update_flag() {
            return order_update_flag;
        }

        public void setOrder_update_flag(int order_update_flag) {
            this.order_update_flag = order_update_flag;
        }

        public int getNRInfoChangeFlag()
        {
            return nrsinfo_update_flag;
        }

        public void setNRInfoChangeFlag(int nrsinfo_update_flag)
        {
            this.nrsinfo_update_flag = nrsinfo_update_flag;
        }

        public int getUserroaminfo_update_flag() {
            return userroaminfo_update_flag;
        }

        public void setUserroaminfo_update_flag(int userroaminfo_update_flag) {
            this.userroaminfo_update_flag = userroaminfo_update_flag;
        }

        public int getServerinfo_update_flag() {
            return serverinfo_update_flag;
        }

        public void setServerinfo_update_flag(int serverinfo_update_flag) {
            this.serverinfo_update_flag = serverinfo_update_flag;
        }

        public String getUsrName()
        {
            return username;
        }

        public void setUsrName(String name)
        {
            this.username = name;
        }

        public void setAdvChangeFlag(int flag)
        {
            this.adv_update_flag = flag;
        }
        
        public int getAdvChangeFlag()
        {
            return adv_update_flag;
        }

        public void setAppChangeFlag(int flag)
        {
            this.appversion_update_flag = flag;
        }
        
        public int getAppChangeFlag()
        {
            return appversion_update_flag;
        }
        
        public void setCountryChangeFlag(int flag)
        {
            this.countryinfo_update_flag = flag;
        }
        
        public int getCountryChangeFlag()
        {
            return countryinfo_update_flag;
        }

        public void setRPChangeFlag(int flag)
        {
            this.roam_config_flag = flag;
        }
        
        public int getRPChangeFlag()
        {
            return roam_config_flag;
        }
        
        public void setVisitingFlag(int flag)
        {
            this.is_visiting = flag;
        }
        
        public int getVisitingFlag()
        {
            return is_visiting;
        }
        
        public void setRoamingFlag(int flag)
        {
            this.is_roaming = flag;
        }
        
        public int getRoamingFlag()
        {
            return is_roaming;
        }
        
        public void setPkgChangeFlag(int flag)
        {
            this.package_update_flag = flag;
        }
        
        public int getPkgChangeFlag()
        {
            return package_update_flag;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            username = jsonObject.optString("username");
            home_config_flag = jsonObject.optInt("home_config_flag");
            roam_config_flag = jsonObject.optInt("roam_config_flag");
            expressprice_change_flag = jsonObject.optInt("expressprice_change_flag");
            adv_update_flag = jsonObject.optInt("adv_update_flag");
            countryinfo_update_flag = jsonObject.optInt("countryinfo_update_flag");
            appversion_update_flag = jsonObject.optInt("appversion_update_flag");
            package_update_flag = jsonObject.optInt("package_update_flag");
            order_update_flag = jsonObject.optInt("order_update_flag");
            //=======================06/29
            SpUtil.setOrderUpdate(order_update_flag);
            //===============================
 
            nrsinfo_update_flag = jsonObject.optInt("nrsinfo_update_flag");
            userroaminfo_update_flag = jsonObject.optInt("userroaminfo_update_flag");
            serverinfo_update_flag = jsonObject.optInt("serverinfo_update_flag");
            is_visiting = jsonObject.optInt("is_visiting");
            is_roaming = jsonObject.optInt("is_roaming");
            
        }
    }
}
