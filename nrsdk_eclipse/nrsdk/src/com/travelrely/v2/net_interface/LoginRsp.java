package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.model.ServerIp;
import com.travelrely.v2.response.BaseData;

public class LoginRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    LoginRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public void setBaseRsp(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public LoginRsp.Data getData()
    {
        return data;
    }

    public void setData(LoginRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new LoginRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        String userName;
        
        String last_login_info;
        
        int serverip_list_flag;
        
        List<ServerIp> ipList;

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }
        
        public String getLastLoginInfo()
        {
            return last_login_info;
        }
        
        public int getServerIpFlag()
        {
            return serverip_list_flag;
        }
        
        public List<ServerIp> getServerIpList()
        {
            return ipList;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
            this.last_login_info = jsonObject.optString("last_login_info");
            this.serverip_list_flag = jsonObject.optInt("serverip_list_flag");
            if (serverip_list_flag == 1)
            {
                JSONArray arr = jsonObject.optJSONArray("serveriplist");
                if (arr == null)
                {
                    return;
                }
                
                ipList = new ArrayList<ServerIp>();
                for (int i = 0; i < arr.length(); i++)
                {
                    JSONObject obj = arr.optJSONObject(i);
                    ServerIp ip = new ServerIp();
                    ip.setMcc(obj.optString("countryid"));
                    ip.setIp(obj.optString("server_ip"));
                    ip.setPort(obj.optInt("server_port"));
                    ipList.add(ip);
                }
            }
        }
    }
}
