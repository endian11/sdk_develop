package com.travelrely.core.glms.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.v2.model.FemtoInfo;
import com.travelrely.v2.response.BaseData;

public class VerifyRsp extends BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private ResponseInfo responseInfo;
    private VerifyRsp.Data data;

    public ResponseInfo getBaseRsp() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public VerifyRsp.Data getData() {
        return data;
    }

    public void setData(VerifyRsp.Data data) {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new VerifyRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable {
        private static final long serialVersionUID = 1L;

        String userName;

        String password;
        
        int userExist;
        
        int nrs_info_version;
        int nrs_status;
        int lxnum_status;
        String nrs_start_date;
        String nrs_end_date;
        
        List<FemtoInfo> femtoInfos;
        
        String timeZone;
        int no_disturb_mode;
        String no_disturb_start;
        String no_disturb_end;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public boolean isUserExist() {
            return userExist == 1;
        }

        public int getNrs_info_version() {
            return nrs_info_version;
        }

        public void setNrs_info_version(int nrs_info_version) {
            this.nrs_info_version = nrs_info_version;
        }

        public int getNrs_status() {
            return nrs_status;
        }

        public void setNrs_status(int nrs_status) {
            this.nrs_status = nrs_status;
        }

        public int getLxnum_status() {
            return lxnum_status;
        }

        public void setLxnum_status(int lxnum_status)
        {
            this.lxnum_status = lxnum_status;
        }

        public String getNrs_start_date()
        {
            return nrs_start_date;
        }

        public void setNrs_start_date(String nrs_start_date)
        {
            this.nrs_start_date = nrs_start_date;
        }

        public String getNrs_end_date()
        {
            return nrs_end_date;
        }

        public void setNrs_end_date(String nrs_end_date)
        {
            this.nrs_end_date = nrs_end_date;
        }

        public List<FemtoInfo> getFemtoInfos()
        {
            return femtoInfos;
        }

        public void setFemtoInfos(List<FemtoInfo> femtoInfos)
        {
            this.femtoInfos = femtoInfos;
        }

        public String getTimeZone()
        {
            return timeZone;
        }

        public void setTimeZone(String timeZone)
        {
            this.timeZone = timeZone;
        }

        public int getNo_disturb_mode()
        {
            return no_disturb_mode;
        }

        public void setNo_disturb_mode(int no_disturb_mode)
        {
            this.no_disturb_mode = no_disturb_mode;
        }

        public String getNo_disturb_start()
        {
            return no_disturb_start;
        }

        public void setNo_disturb_start(String no_disturb_start)
        {
            this.no_disturb_start = no_disturb_start;
        }

        public String getNo_disturb_end()
        {
            return no_disturb_end;
        }

        public void setNo_disturb_end(String no_disturb_end)
        {
            this.no_disturb_end = no_disturb_end;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
            this.password = jsonObject.optString("password");
            
            this.userExist = jsonObject.optInt("user_exist_flag");

            this.nrs_info_version = jsonObject.optInt("nrs_info_version");
            this.nrs_status = jsonObject.optInt("nrs_status");
            this.lxnum_status = jsonObject.optInt("lxnum_status");
            this.nrs_start_date = jsonObject.optString("nrs_start_date");
            this.nrs_end_date = jsonObject.optString("nrs_end_date");
            
            JSONArray jsonArray = jsonObject.optJSONArray("femto_info");
            if (jsonArray != null)
            {
                femtoInfos = new ArrayList<FemtoInfo>();
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    FemtoInfo info = new FemtoInfo();
                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                    info.setMcc(jsonObject2.optString("mcc"));
                    info.setIp(jsonObject2.optString("femto_ip"));
                    info.setPort(jsonObject2.optInt("femto_port"));
                    
                    femtoInfos.add(info);
                }
            }
            
            timeZone = jsonObject.optString("time_zone");
            no_disturb_mode = jsonObject.optInt("no_disturb_mode");

            JSONObject obj = jsonObject.optJSONObject("no_disturb_period");
            if (obj == null)
            {
                no_disturb_start = "";
                no_disturb_end = "";
                return;
            }

            no_disturb_start = obj.optString("start_time");
            no_disturb_end = obj.optString("end_time");
        }
    }
}
