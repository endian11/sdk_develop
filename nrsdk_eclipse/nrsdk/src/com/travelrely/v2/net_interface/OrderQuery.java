package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.net_interface.OrderQueryRsp.Data.OrderInfo;
import com.travelrely.v2.response.BaseData;
import com.travelrely.v2.util.SpUtil;

/** 
 * 
 * @author zhangyao
 * @version 2015年1月19日下午12:55:10
 */

public class OrderQuery extends BaseData{
    
    Data data;
    
    ResponseInfo responseInfo;

	public String package_data;
    
    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    
    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable{
        
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        String username;//用户（一个手机号）
        
        List<OrderList> orderLists;//订单集合

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * @return 订单集合
         */
        public List<OrderList> getOrderLists() {
            return orderLists;
        }

        public void setOrderLists(List<OrderList> orderLists) {
            this.orderLists = orderLists;
        }
        private int orderVersion;
        
        public int getOrderVersion() {
			return orderVersion;
		}

		public void setOrderVersion(int orderVersion) {
			this.orderVersion = orderVersion;
		}

		@Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            
            username = jsonObject.optString("username");
            
            //=======06/29
            orderVersion = jsonObject.optInt("order_version");
            SpUtil.setOrderVersion(orderVersion);
            //=====================
            
            JSONArray oderlistArray = jsonObject.optJSONArray("order_list");
            if (oderlistArray == null)
            {
                return;
            }

            orderLists = new ArrayList<OrderList>();
            for (int i = 0; i < oderlistArray.length(); i++)
            {
                JSONObject jsonObj = oderlistArray.optJSONObject(i);
                if (jsonObj == null)
                {
                    continue;
                }
                OrderList orderList = new OrderList();
                orderList.setValue(jsonObj);
                orderLists.add(orderList);
            }
        }
    } 
    
    public static class OrderList extends BaseData implements Serializable{
        
    	
    	private List<OrderInfo> orderInfos;
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        String username;
        
        String orderid;
        
        int order_status;
        
        double orderfee;
        
        int card_num;
        
        int express_code;
        
        double express_price;//快递价格
        
        double favorable_price;//优惠价格
        
        String order_time;//订购时间
        
        List<SuborderList> suborderList;

        public List<SuborderList> getSuborderList() {
            return suborderList;
        }

        public void setSuborderList(List<SuborderList> suborderList) {
            this.suborderList = suborderList;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public int getOrder_status() {
            return order_status;
        }

        public void setOrder_status(int order_status) {
            this.order_status = order_status;
        }

        public double getOrderfee() {
            return orderfee;
        }

        public void setOrderfee(double orderfee) {
            this.orderfee = orderfee;
        }

        public int getCard_num() {
            return card_num;
        }

        public void setCard_num(int card_num) {
            this.card_num = card_num;
        }

        public int getExpress_code() {
            return express_code;
        }

        public void setExpress_code(int express_code) {
            this.express_code = express_code;
        }

        public double getExpress_price() {
            return express_price;
        }

        public void setExpress_price(double express_price) {
            this.express_price = express_price;
        }

        public double getFavorable_price() {
            return favorable_price;
        }

        public void setFavorable_price(double favorable_price) {
            this.favorable_price = favorable_price;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        
        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            
            username = jsonObject.optString("username");
            orderid = jsonObject.optString("orderid");
            order_status = jsonObject.optInt("order_status");
            orderfee = jsonObject.optDouble("orderfee");
            card_num = jsonObject.optInt("card_num");
            express_code = jsonObject.optInt("express_code");
            express_price = jsonObject.optDouble("express_price");
            favorable_price = jsonObject.optDouble("favorable_price");
            order_time = jsonObject.optString("order_time");
            
            

            JSONArray tripsJsonArray = jsonObject.optJSONArray("suborder_list");
            if (tripsJsonArray == null)
            {
                return;
            }

            suborderList = new ArrayList<SuborderList>();
            for (int i = 0; i < tripsJsonArray.length(); i++)
            {
                JSONObject tripJsonObj = tripsJsonArray.optJSONObject(i);
                if (tripJsonObj == null)
                {
                    continue;
                }
                SuborderList sList = new SuborderList();
                sList.setValue(tripJsonObj);
                suborderList.add(sList);
            }
            
        }
/********************************************************/
		public List<OrderInfo> getOrderInfos() {
			// TODO Auto-generated method stub
			return orderInfos;
		}
		//=======================
    }
    
    public static class SuborderList extends BaseData implements Serializable{
        
        String suborderid;
        
        String tripid;
        
        String suborderfee;
        
        int ordertype;
        
        String username;
        
        int simcardtype;
        
        int simcardsize;
        
        String mcc;
        
        String mnc;
        
        String begin_date;
        
        String end_date;
        
        String remaindata;
        
        String remainvoice_idd;
        
        String idd_voice;
        
        String local_voice;
        
        int bt_buyflag;
        
        int netcall_buyflag;
        
        int lxnum_flag;
        
        String lxnum_price;
        String package_data;
        public int getLxnum_flag() {
            return lxnum_flag;
        }

        public void setLxnum_flag(int lxnum_flag) {
            this.lxnum_flag = lxnum_flag;
        }

        public String getPackage_data() {
			return package_data;
		}

		public void setPackage_data(String package_data) {
			this.package_data = package_data;
		}

		public String getLxnum_price() {
            return lxnum_price;
        }

        public void setLxnum_price(String lxnum_price) {
            this.lxnum_price = lxnum_price;
        }

        public int getNetcall_buyflag() {
            return netcall_buyflag;
        }

        public void setNetcall_buyflag(int netcall_buyflag) {
            this.netcall_buyflag = netcall_buyflag;
        }

        public int getBt_buyflag() {
            return bt_buyflag;
        }

        public void setBt_buyflag(int bt_buyflag) {
            this.bt_buyflag = bt_buyflag;
        }

        public String getSuborderid() {
            return suborderid;
        }

        public void setSuborderid(String suborderid) {
            this.suborderid = suborderid;
        }

        public String getTripid() {
            return tripid;
        }

        public void setTripid(String tripid) {
            this.tripid = tripid;
        }

        public String getSuborderfee() {
            return suborderfee;
        }

        public void setSuborderfee(String suborderfee) {
            this.suborderfee = suborderfee;
        }

        public int getOrdertype() {
            return ordertype;
        }

        public void setOrdertype(int ordertype) {
            this.ordertype = ordertype;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getSimcardtype() {
            return simcardtype;
        }

        public void setSimcardtype(int simcardtype) {
            this.simcardtype = simcardtype;
        }

        public int getSimcardsize() {
            return simcardsize;
        }

        public void setSimcardsize(int simcardsize) {
            this.simcardsize = simcardsize;
        }

        public String getMcc() {
            return mcc;
        }

        public void setMcc(String mcc) {
            this.mcc = mcc;
        }

        public String getMnc() {
            return mnc;
        }

        public void setMnc(String mnc) {
            this.mnc = mnc;
        }

        public String getBegin_date() {
            return begin_date;
        }

        public void setBegin_date(String begin_date) {
            this.begin_date = begin_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getRemaindata() {
            return remaindata;
        }

        public void setRemaindata(String remaindata) {
            this.remaindata = remaindata;
        }

        public String getRemainvoice_idd() {
            return remainvoice_idd;
        }

        public void setRemainvoice_idd(String remainvoice_idd) {
            this.remainvoice_idd = remainvoice_idd;
        }

        public String getIdd_voice() {
            return idd_voice;
        }

        public void setIdd_voice(String idd_voice) {
            this.idd_voice = idd_voice;
        }

        public String getLocal_voice() {
            return local_voice;
        }

        public void setLocal_voice(String local_voice) {
            this.local_voice = local_voice;
        }
        
        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            
            suborderid = jsonObject.optString("suborderid");
            tripid = jsonObject.optString("tripid");
            suborderfee = jsonObject.optString("suborderfee");
            ordertype = jsonObject.optInt("ordertype");
            username = jsonObject.optString("username");
            simcardtype = jsonObject.optInt("simcardtype");
            simcardsize = jsonObject.optInt("simcardsize");
            mcc = jsonObject.optString("mcc");
            mnc = jsonObject.optString("mnc");
            begin_date = jsonObject.optString("begin_date");
            end_date = jsonObject.optString("end_date");
            remaindata = jsonObject.optString("remaindata");
            package_data = jsonObject.optString("package_data");
            remainvoice_idd = jsonObject.optString("remainvoice_idd");
            idd_voice = jsonObject.optString("idd_voice");
            local_voice = jsonObject.optString("local_voice");
            bt_buyflag = jsonObject.optInt("bt_buyflag");
            netcall_buyflag = jsonObject.optInt("netcall_buyflag");
            lxnum_flag = jsonObject.optInt("lxnum_flag");
            lxnum_price = jsonObject.optString("lxnum_price");
            
        }
    }

}
