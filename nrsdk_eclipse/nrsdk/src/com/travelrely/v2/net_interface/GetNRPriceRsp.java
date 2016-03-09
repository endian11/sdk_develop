package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class GetNRPriceRsp extends BaseResponse implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	ResponseInfo				responseInfo;
	GetNRPriceRsp.Data		data;

	public ResponseInfo getBaseRspInfo()
	{
		return responseInfo;
	}

	public void setBaseRspInfo(ResponseInfo responseInfo)
	{
		this.responseInfo = responseInfo;
	}

	public GetNRPriceRsp.Data getData()
	{
		return data;
	}

	public void setData(GetNRPriceRsp.Data data)
	{
		this.data = data;
	}

	@Override
	public void setValue(JSONObject jsonObject)
	{
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);

		data = new GetNRPriceRsp.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}

	public static class Data extends BaseData implements Serializable
	{
		private static final long	serialVersionUID	= 1L;

		double						btBoxPrice;

		List<PackagePrice>			pkgPriceList;
		double usa_btbox_price; //3.0.0新增接口 2/15
		String noroaming_pricetxt1;//3.0.0 新增接口 2/13
		String noroaming_pricetxt2;//3.0.0 新增接口 2/13
		
		public String getNoroaming_pricetxt1() {
			return noroaming_pricetxt1;
		}

		public String getNoroaming_pricetxt2() {
			return noroaming_pricetxt2;
		}
		
		public void setNoroaming_pricetxt1(String noroaming_pricetxt1) {
			this.noroaming_pricetxt1 = noroaming_pricetxt1;
		}

		public void setNoroaming_pricetxt2(String noroaming_pricetxt2) {
			this.noroaming_pricetxt2 = noroaming_pricetxt2;
		}

		public double getBtBoxPrice()
		{
			return btBoxPrice;
		}

		public double getUsa_btbox_price() {
			return usa_btbox_price;
		}

		public void setUsa_btbox_price(double usa_btbox_price) {
			this.usa_btbox_price = usa_btbox_price;
		}

		public List<PackagePrice> getPkgPrices()
		{
			return pkgPriceList;
		}

		public void setDataPrices(List<PackagePrice> dataPrices)
		{
			this.pkgPriceList = dataPrices;
		}

		@Override
		public void setValue(JSONObject jsonObject)
		{
			super.setValue(jsonObject);

			btBoxPrice = jsonObject.optDouble("btbox_price");
			usa_btbox_price = jsonObject.optDouble("usa_btbox_price");
			noroaming_pricetxt1 = jsonObject.optString("noroaming_pricetxt1");
			noroaming_pricetxt2 = jsonObject.optString("noroaming_pricetxt2");
			JSONArray priceArray = jsonObject
					.optJSONArray("noroaming_price_list");
			if (priceArray == null)
			{
				return;
			}

			pkgPriceList = new ArrayList<PackagePrice>();
			for (int i = 0; i < priceArray.length(); i++)
			{
				JSONObject orderJsonObject = priceArray.optJSONObject(i);
				PackagePrice order = new PackagePrice();
				order.setValue(orderJsonObject);
				pkgPriceList.add(order);
			}
		}

		public static class PackagePrice extends BaseResponse implements
				Serializable
		{
			@Override
			public String toString() {
				return "PackagePrice [days=" + days + ", netcall_price="
						+ netcall_price + ", lxnum_price=" + lxnum_price + "]";
			}


			/**
			 * 数据价格包
			 */
			private static final long	serialVersionUID	= 1L;

			int days;
			
			double netcall_price; //网络电话价格
			
			double lxnum_price; //旅信小号价格


			public int getDays() {
                return days;
            }


            public void setDays(int days) {
                this.days = days;
            }


            public double getNetcall_price() {
                return netcall_price;
            }


            public void setNetcall_price(double netcall_price) {
                this.netcall_price = netcall_price;
            }


            public double getLxnum_price() {
                return lxnum_price;
            }


            public void setLxnum_price(double lxnum_price) {
                this.lxnum_price = lxnum_price;
            }


            @Override
			public void setValue(JSONObject jsonObject)
			{
				super.setValue(jsonObject);
				this.days = jsonObject.optInt("days");
				this.netcall_price = jsonObject.optDouble("netcall_price");
				this.lxnum_price = jsonObject.optInt("lxnum_price");
			}
		}
	}
}
