
package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.model.ContactModel;

public class DownloadContact extends BaseResponse implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    DownloadContact.Data data;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public DownloadContact.Data getData() {
        return data;
    }

    public void setData(DownloadContact.Data data) {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new DownloadContact.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        String userName;

        List<ContactModel> contactList;

        public List<ContactModel> getContactList() {
            return contactList;
        }

        public void setContactList(List<ContactModel> contactList) {
            this.contactList = contactList;
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
            JSONArray jsonArray = jsonObject.optJSONArray("contact_list");
            if (jsonArray != null) {
                contactList = new ArrayList<ContactModel>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject contactJsonObject = jsonArray.optJSONObject(i);
                    ContactModel contactModel = new ContactModel();
                    contactModel.setValue(contactJsonObject);
                    contactList.add(contactModel);
                }
            }

        }
    }
}
