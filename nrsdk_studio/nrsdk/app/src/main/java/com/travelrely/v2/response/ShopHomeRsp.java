package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.v2.response.TripInfo.Data;

public class ShopHomeRsp extends BaseData{
	ResponseInfo responseInfo ;
	Data data ;
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
		 private String shop_info_version;
		 private String adv_update_flag;
//		 private List adv_list;
		 private String shop_update_flag;
	 
	 }
}
