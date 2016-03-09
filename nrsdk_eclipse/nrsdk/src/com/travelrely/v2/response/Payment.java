package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.ResponseInfo;


public class Payment extends BaseData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    ResponseInfo responseInfo;

    Payment.Data data;
	
	

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public Payment.Data getData() {
		return data;
	}

	public void setData(Payment.Data data) {
		this.data = data;
	}
	
    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new Payment.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

	public static class Data extends BaseData implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public String userName;
		Order order;

		public Order getOrder() {
			return order;
		}


		public void setOrder(Order order) {
			this.order = order;
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
			JSONObject jObject = jsonObject.optJSONObject("order");
			order = new Order();
            order.setValue(jObject);
		}
		
	}
	
	public static class Order extends BaseData implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		String id;
		String balance;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}
		
		@Override
		public void setValue(JSONObject jsonObject) {
			// TODO Auto-generated method stub
			super.setValue(jsonObject);
			this.id = jsonObject.optString("id");
			this.balance = jsonObject.optString("balance");
			
		}
	} 

}
