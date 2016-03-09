package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;

public class GetRechargeNum extends BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ResponseInfo responseInfo;
	GetRechargeNum.Data data;
	
	
	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public GetRechargeNum.Data getData() {
		return data;
	}

	public void setData(GetRechargeNum.Data data) {
		this.data = data;
	}
	
	@Override
	public void setValue(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);
		data = new GetRechargeNum.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}

	
	public static class Data extends BaseData implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public String userName;
		public String tradeNo;
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getTradeNo() {
			return tradeNo;
		}
		public void setTradeNo(String tradeNo) {
			this.tradeNo = tradeNo;
		}
		
		@Override
		public void setValue(JSONObject jsonObject) {
			// TODO Auto-generated method stub
			super.setValue(jsonObject);
			this.userName = jsonObject.optString("username");
			this.tradeNo = jsonObject.optString("trade_no");
		}
	}

}
