
package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

public class GetMessage extends BaseResponse implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetMessage.Data data;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public GetMessage.Data getData() {
        return data;
    }

    public void setData(GetMessage.Data data) {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetMessage.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        String userName;

        List<TraMessage> message_list;

        public List<TraMessage> getMessage_list() {
            return message_list;
        }

        public void setMessage_list(List<TraMessage> message_list) {
            this.message_list = message_list;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public void setValue(JSONObject jsonObject) {
            // TODO Auto-generated method stub
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");

            JSONArray jsonArray = jsonObject.optJSONArray("message_list");

            if (jsonArray != null) {
                message_list = new ArrayList<TraMessage>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                    TraMessage message = new TraMessage();
                    message.setValue(jsonObject2);
                    message_list.add(message);
                }
            }

        }
    }
}
