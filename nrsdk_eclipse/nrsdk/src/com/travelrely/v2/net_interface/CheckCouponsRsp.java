package com.travelrely.v2.net_interface;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

/**
 * 解析优惠券
 * 
 * @author Travelrely
 * 
 */
public class CheckCouponsRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    public ResponseInfo responseInfo;
    CheckCouponsRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public CheckCouponsRsp.Data getData()
    {
        return data;
    }

    public void setData(CheckCouponsRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new CheckCouponsRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        public String userName;
        public String couponCode;
        public String currencyUnit;
        public double couponPrice;
        public int couponType;

        public int getCouponType()
        {
            return couponType;
        }

        public void setCouponType(int couponType)
        {
            this.couponType = couponType;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public String getCouponCode()
        {
            return couponCode;
        }

        public void setCouponCode(String couponCode)
        {
            this.couponCode = couponCode;
        }

        public String getCurrencyUnit()
        {
            return currencyUnit;
        }

        public void setCurrencyUnit(String currencyUnit)
        {
            this.currencyUnit = currencyUnit;
        }

        public double getCouponPrice()
        {
            return couponPrice;
        }

        public void setCouponPrice(double couponPrice)
        {
            this.couponPrice = couponPrice;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
            this.couponCode = jsonObject.optString("coupon_code");
            this.currencyUnit = jsonObject.optString("currency_unit");
            this.couponPrice = jsonObject.optDouble("coupon_price");
            this.couponType = jsonObject.optInt("coupon_type");
        }
    }
}
