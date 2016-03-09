package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travelrely.core.glms.response.ResponseInfo;

public class GetGroupMsg extends BaseData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ResponseInfo responseInfo;
	GetGroupMsg.Data data;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public GetGroupMsg.Data getData() {
		return data;
	}

	public void setData(GetGroupMsg.Data data) {
		this.data = data;
	}
	
    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);
		data = new GetGroupMsg.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		
		data.setValue(dataobj);

    }

	public static class Data extends BaseData implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		String username;
		
		String name;
		
		String group_head_portrait;
		
		int version;
		
		String expireddate;
		
		String delflag;
		
		private List<GroupMsg> groupMsgs;
		
		public String getDelflag() {
            return delflag;
        }

        public void setDelflag(String delflag) {
            this.delflag = delflag;
        }

        public String getExpireddate() {
            return expireddate;
        }

        public void setExpireddate(String expireddate) {
            this.expireddate = expireddate;
        }

		public List<GroupMsg> getGroupMsgs() {
			return groupMsgs;
		}

		public void setGroupMsgs(List<GroupMsg> groupMsgs) {
			this.groupMsgs = groupMsgs;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getGroup_head_portrait() {
			return group_head_portrait;
		}

		public void setGroup_head_portrait(String group_head_portrait) {
			this.group_head_portrait = group_head_portrait;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}
		
	    @Override
	    public void setValue(JSONObject jsonObject) {
	        super.setValue(jsonObject);

	        username = jsonObject.optString("username");
	        name = jsonObject.optString("name");
	        group_head_portrait = jsonObject.optString("group_head_portrait");
	        version = jsonObject.optInt("version");
	        expireddate = jsonObject.optString("expireddate");
	        delflag = jsonObject.optString("delflag");
	        
	        JSONArray jsonArray = jsonObject.optJSONArray("member_list");
	        this.groupMsgs = new ArrayList<GroupMsg>();
	        try {
	            if(jsonArray != null){
	                for (int i = 0; i < jsonArray.length(); i++) {
	                    JSONObject jsonObject2 = (JSONObject)jsonArray.getJSONObject(i);
	                    GroupMsg groupMsg = new GroupMsg();
	                    groupMsg.setValue(jsonObject2);
	                    groupMsgs.add(groupMsg);
	                }
	            }
	        } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	    }
	}

}
