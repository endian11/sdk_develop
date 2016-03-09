package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travelrely.core.glms.response.ResponseInfo;

/** 
 * 
 * @author zhangyao
 * @version 2014年6月17日上午10:35:41
 */

public class GetGroupList extends BaseData implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    ResponseInfo responseInfo;
    
    Data data;
    
    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public Data getDate() {
        return data;
    }

    public void setDate(Data data) {
        this.data = data;
    }
    
    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        
        data.setValue(dataobj);

    }



    public static class Data extends BaseData implements Serializable{

        private static final long serialVersionUID = 1L;
        
        String username;
        
        List<GroupList> grouplist;

        public List<GroupList> getGrouplist() {
            return grouplist;
        }

        public void setGrouplist(List<GroupList> grouplist) {
            this.grouplist = grouplist;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        
        @Override
        public void setValue(JSONObject jsonObject) {
            super.setValue(jsonObject);

            username = jsonObject.optString("username");
            
            JSONArray jsonArray = jsonObject.optJSONArray("grouplist");
            this.grouplist = new ArrayList<GroupList>();
            try {
                if(jsonArray != null){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = (JSONObject)jsonArray.getJSONObject(i);
                        GroupList groupList = new GroupList();
                        groupList.setValue(jsonObject2);
                        grouplist.add(groupList);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}
