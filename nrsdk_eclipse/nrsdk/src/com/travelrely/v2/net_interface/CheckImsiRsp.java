package com.travelrely.v2.net_interface;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

/**
 * @author john
 *检查盒子IMSI（check_box_imsi）
   
	功能说明：通过IMSI判断是联通还是移动客户，下发漫游资费说明让客户确认。
 */
public class CheckImsiRsp extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ResponseInfo responseInfo;
	CheckImsiRsp.Data data;
	
	  public CheckImsiRsp.Data getData() {
		return data;
	}

	public void setData(CheckImsiRsp.Data data) {
		this.data = data;
	}

	public ResponseInfo getBaseRsp() {
	        return responseInfo;
	  }

	 public void setResponseInfo(ResponseInfo responseInfo){
	        this.responseInfo = responseInfo;
	 }

	 
	 @Override
	public void setValue(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		super.setValue(jsonObject);
		  responseInfo = new ResponseInfo();
	        responseInfo.setValue(jsonObject);
	        data = new CheckImsiRsp.Data();
	        JSONObject dataobj = jsonObject.optJSONObject("data");
	        data.setValue(dataobj);
	}
	 
	 public static class Data extends BaseData implements Serializable{
		 private static final long serialVersionUID = 1L;
		 String userName;//用户名
		 int check_flag;// 是否校验 0-未校验 1-已校验
		 String notify_content; //提示内容
		 String comp_content;//与用户输入的比较字符串
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public int getCheck_flag() {
			return check_flag;
		}
		public void setCheck_flag(int check_flag) {
			this.check_flag = check_flag;
		}
		public String getNotify_content() {
			return notify_content;
		}
		public void setNotify_content(String notify_content) {
			this.notify_content = notify_content;
		}
		public String getComp_content() {
			return comp_content;
		}
		public void setComp_content(String comp_content) {
			this.comp_content = comp_content;
		}
		 
		 @Override
		public void setValue(JSONObject jsonObject) {
			// TODO Auto-generated method stub
			super.setValue(jsonObject);
			this.userName = jsonObject.optString("username");
            this.notify_content = jsonObject.optString("notify_content");
            this.comp_content = jsonObject.optString("comp_content");
            
            this.check_flag = jsonObject.optInt("check_flag");
		}
	 }

	@Override
	public String toString() {
		return "CheckImsiRsp [responseInfo=" + responseInfo + ", data=" + data
				+ ", getData()=" + getData() + ", getBaseRsp()=" + getBaseRsp()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	 
	 
	 
}
