package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;

public class QuitGroup  extends BaseResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    ResponseInfo responseInfo;
    
    QuitGroup.Data data;
    
    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public QuitGroup.Data getData() {
        return data;
    }

    public void setData(QuitGroup.Data data) {
        this.data = data;
    }
    
    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new QuitGroup.Data();
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
            super.setValue(jsonObject);
            this.username = jsonObject.optString("username");
        }

    }
}
