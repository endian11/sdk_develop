package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

public class SetAdvCat extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ResponseInfo responseInfo;
	SetAdvCat.Data data;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public SetAdvCat.Data getData() {
		return data;
	}

	public void setData(SetAdvCat.Data data) {
		this.data = data;
	}

	@Override
	public void setValue(JSONObject jsonObject) {
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);
		data = new SetAdvCat.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}

	public static class Data extends BaseData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String userName;

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
		}
	}
}
