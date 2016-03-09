package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

/**
 * 个性化套餐提交
 * 
 * @author developer
 * 
 */
public class GetCustomized extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ResponseInfo responseInfo;
	GetCustomized.Data data;

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public GetCustomized.Data getData() {
		return data;
	}

	public void setData(GetCustomized.Data data) {
		this.data = data;
	}

	@Override
	public void setValue(JSONObject jsonObject) {
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);
		data = new GetCustomized.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}

	public static class Data extends BaseData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String userName;

		String mcc;

		Package personal_package;

		public Package getPersonal_package() {
			return personal_package;
		}

		public void setPersonal_package(Package personal_package) {
			this.personal_package = personal_package;
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

			JSONObject personal_packageObject = jsonObject
					.optJSONObject("personal_package");

			if (personal_packageObject != null) {
				personal_package = new Package();
				personal_package.setValue(personal_packageObject);
			}
		}

		public static class PersonalPackage extends BaseData implements
				Serializable {

			String mnc;
			String country_name;
			String carrier_name;
			String packageid;
			int packagetarget;
			double packageprice;
			int packagecurrency;
			int packagevoice;
			int packagedata;
			int packageduration;
			int packagemode;

			@Override
			public void setValue(JSONObject jsonObject) {
				// TODO Auto-generated method stub
				super.setValue(jsonObject);
				mnc = jsonObject.optString("mnc");
				country_name = jsonObject.optString("country_name");
				carrier_name = jsonObject.optString("carrier_name");
				packageid = jsonObject.optString("packageid");
				packagetarget = jsonObject.optInt("packagetarget");
				packageprice = jsonObject.optDouble("packageprice");
				packagecurrency = jsonObject.optInt("packagecurrency");
				packagevoice = jsonObject.optInt("packagevoice");
				packagedata = jsonObject.optInt("packagedata");
				packageduration = jsonObject.optInt("packageduration");
				packagemode = jsonObject.optInt("packagemode");
			}

			public String getMnc() {
				return mnc;
			}

			public void setMnc(String mnc) {
				this.mnc = mnc;
			}

			public String getCountry_name() {
				return country_name;
			}

			public void setCountry_name(String country_name) {
				this.country_name = country_name;
			}

			public String getCarrier_name() {
				return carrier_name;
			}

			public void setCarrier_name(String carrier_name) {
				this.carrier_name = carrier_name;
			}

			public String getPackageid() {
				return packageid;
			}

			public void setPackageid(String packageid) {
				this.packageid = packageid;
			}

			public int getPackagetarget() {
				return packagetarget;
			}

			public void setPackagetarget(int packagetarget) {
				this.packagetarget = packagetarget;
			}

			public double getPackageprice() {
				return packageprice;
			}

			public void setPackageprice(double packageprice) {
				this.packageprice = packageprice;
			}

			public int getPackagecurrency() {
				return packagecurrency;
			}

			public void setPackagecurrency(int packagecurrency) {
				this.packagecurrency = packagecurrency;
			}

			public int getPackagevoice() {
				return packagevoice;
			}

			public void setPackagevoice(int packagevoice) {
				this.packagevoice = packagevoice;
			}

			public int getPackagedata() {
				return packagedata;
			}

			public void setPackagedata(int packagedata) {
				this.packagedata = packagedata;
			}

			public int getPackageduration() {
				return packageduration;
			}

			public void setPackageduration(int packageduration) {
				this.packageduration = packageduration;
			}

			public int getPackagemode() {
				return packagemode;
			}

			public void setPackagemode(int packagemode) {
				this.packagemode = packagemode;
			}

		}
	}
}
