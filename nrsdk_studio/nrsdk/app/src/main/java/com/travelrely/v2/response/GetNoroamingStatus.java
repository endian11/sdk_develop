package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;

/** 
 * 
 * @author zhangyao
 * @version 2014年7月29日下午4:08:59
 */

public class GetNoroamingStatus extends BaseResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public ResponseInfo responseInfo;
    GetNoroamingStatus.Data data;
    
    
    
    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public GetNoroamingStatus.Data getData() {
        return data;
    }

    public void setData(GetNoroamingStatus.Data data) {
        this.data = data;
    }
    
    @Override
    public void setValue(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetNoroamingStatus.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }



    public static class Data extends BaseData implements Serializable{

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        String username;
        
        String type;
        
        String starttime;
        
        String endtime;
        
        String timezone;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public static long getSerialversionuid() {
            return serialVersionUID;
        }
        
        @Override
        public void setValue(JSONObject jsonObject) {
            // TODO Auto-generated method stub
            super.setValue(jsonObject);
            this.username = jsonObject.optString("username");
            this.type = jsonObject.optString("type");
            this.starttime = jsonObject.optString("starttime");
            this.endtime = jsonObject.optString("endtime");
            this.timezone = jsonObject.optString("timezone");
        }
        
        
    }

}
