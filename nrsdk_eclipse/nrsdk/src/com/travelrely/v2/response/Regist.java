package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

public class Regist extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ResponseInfo responseInfo;
	Regist.Data data;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public Regist.Data getData() {
		return data;
	}

	public void setData(Regist.Data data) {
		this.data = data;
	}

	@Override
	public void setValue(JSONObject jsonObject) {
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);
		data = new Regist.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}

	public static class Data extends BaseData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String userName;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getInherent_mcc() {
			return inherent_mcc;
		}

		public void setInherent_mcc(String inherent_mcc) {
			this.inherent_mcc = inherent_mcc;
		}

		public String getInherent_mnc() {
			return inherent_mnc;
		}

		public void setInherent_mnc(String inherent_mnc) {
			this.inherent_mnc = inherent_mnc;
		}

		String password;
		String inherent_mcc;
		String inherent_mnc;

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
			this.password = jsonObject.optString("password");
			this.inherent_mcc = jsonObject.optString("inherent_mcc");
			this.inherent_mnc = jsonObject.optString("inherent_mnc");
		}
	}
}
