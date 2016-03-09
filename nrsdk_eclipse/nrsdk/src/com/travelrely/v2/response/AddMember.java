package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

public class AddMember extends BaseResponse implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    ResponseInfo responseInfo;
    
    AddMember.Data data;
    
    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public AddMember.Data getData() {
        return data;
    }

    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new AddMember.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable{
        
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        
        @Override
        public void setValue(JSONObject jsonObject) {
            // TODO Auto-generated method stub
            this.username = jsonObject.optString("username");
        }
    }

}