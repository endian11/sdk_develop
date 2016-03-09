package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

public class Balance extends BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ResponseInfo responseInfo;
	
	Balance.Data data;
	
	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public Balance.Data getData() {
		return data;
	}

	public void setData(Balance.Data data) {
		this.data = data;
	}
	
	@Override
	public void setValue(JSONObject jsonObject) {
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);
		data = new Balance.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}
	
	public static class Data extends BaseData implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public String userName;
		public String currencyUnit;
		public double balance;
		
		
		public String getUserName() {
			return userName;
		}


		public void setUserName(String userName) {
			this.userName = userName;
		}


		public String getCurrencyUnit() {
			return currencyUnit;
		}


		public void setCurrencyUnit(String currencyUnit) {
			this.currencyUnit = currencyUnit;
		}


		public double getBalance() {
			return balance;
		}


		public void setBalance(double balance) {
			this.balance = balance;
		}


		@Override
		public void setValue(JSONObject jsonObject) {
			// TODO Auto-generated method stub
			super.setValue(jsonObject);
			this.userName = jsonObject.optString("username");
			this.currencyUnit = jsonObject.optString("currency_unit");
			this.balance = jsonObject.optDouble("balance");
		}
		
	}

}
