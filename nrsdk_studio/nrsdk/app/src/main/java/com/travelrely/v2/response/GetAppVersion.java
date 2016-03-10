package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;

public class GetAppVersion extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ResponseInfo responseInfo;

	GetAppVersion.Data data;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public GetAppVersion.Data getData() {
		return data;
	}

	public void setData(GetAppVersion.Data data) {
		this.data = data;
	}

	@Override
	public void setValue(JSONObject jsonObject) {
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);
		data = new GetAppVersion.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}

	public static class Data extends BaseData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		String version;
		
		String size;
		
		String function_desc;
		
		String website;

        public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getFunction_desc() {
			return function_desc;
		}

		public void setFunction_desc(String function_desc) {
			this.function_desc = function_desc;
		}
		
		public String getWebsite() {
			return website;
		}

		public void setWebsite(String website) {
			this.website = website;
		}

		@Override
		public String toString() {
			return "Data [version=" + version + "]";
		}

		@Override
		public void setValue(JSONObject jsonObject) {
			// TODO Auto-generated method stub
			super.setValue(jsonObject);
			version = jsonObject.optString("version");
			size = jsonObject.optString("size");
			function_desc = jsonObject.optString("functiondesc");
			website = jsonObject.optString("website");
		}
	}
}
