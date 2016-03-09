package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

public class GetCallforwardNumber extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ResponseInfo responseInfo;
	GetCallforwardNumber.Data data;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public GetCallforwardNumber.Data getData() {
		return data;
	}

	public void setData(GetCallforwardNumber.Data data) {
		this.data = data;
	}

	@Override
	public void setValue(JSONObject jsonObject) {
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);
		data = new GetCallforwardNumber.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}

	public static class Data extends BaseData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String userName;

		String enable_number;

		public String getEnable_number() {
			return enable_number;
		}

		public void setEnable_number(String enable_number) {
			this.enable_number = enable_number;
		}

		public String getDisable_number() {
			return disable_number;
		}

		public void setDisable_number(String disable_number) {
			this.disable_number = disable_number;
		}

		String disable_number;

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
			this.enable_number = jsonObject.optString("enable_number");
			this.disable_number = jsonObject.optString("disable_number");
		}
	}
}
