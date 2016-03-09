package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;

public class NewOrder extends BaseResponse implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    NewOrder.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public NewOrder.Data getData()
    {
        return data;
    }

    public void setData(NewOrder.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new NewOrder.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        String userName;

        String orderId;

        String tradeNo;

        String currencyUnit;

        String packagePrice;

        String expressPrice;

        String favorablePrice;

        String totalFee;

        String accountBalance;

        List<String> trips;
        
        String productname;

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public String getOrderId()
        {
            return orderId;
        }

        public void setOrderId(String orderId)
        {
            this.orderId = orderId;
        }

        public String getTradeNo()
        {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo)
        {
            this.tradeNo = tradeNo;
        }

        public String getCurrencyUnit()
        {
            return currencyUnit;
        }

        public void setCurrencyUnit(String currencyUnit)
        {
            this.currencyUnit = currencyUnit;
        }

        public String getPackagePrice()
        {
            return packagePrice;
        }

        public void setPackagePrice(String packagePrice)
        {
            this.packagePrice = packagePrice;
        }

        public String getExpressPrice()
        {
            return expressPrice;
        }

        public void setExpressPrice(String expressPrice)
        {
            this.expressPrice = expressPrice;
        }

        public String getFavorablePrice()
        {
            return favorablePrice;
        }

        public void setFavorablePrice(String favorablePrice)
        {
            this.favorablePrice = favorablePrice;
        }

        public String getTotalFee()
        {
            return totalFee;
        }

        public void setTotalFee(String totalFee)
        {
            this.totalFee = totalFee;
        }

        public List<String> getTrips()
        {
            return trips;
        }

        public void setTrips(List<String> trips)
        {
            this.trips = trips;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public String getAccountBalance()
        {
            return accountBalance;
        }

        public void setAccountBalance(String accountBalance)
        {
            this.accountBalance = accountBalance;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
            this.orderId = jsonObject.optString("order_id");
            this.tradeNo = jsonObject.optString("tradeNo");
            this.currencyUnit = jsonObject.optString("currency_unit");
            this.packagePrice = jsonObject.optString("package_price");
            this.expressPrice = jsonObject.optString("express_price");
            this.favorablePrice = jsonObject.optString("favorable_price");
            this.totalFee = jsonObject.optString("total_fee");
            this.accountBalance = jsonObject.optString("AcountBalance");
            this.productname = jsonObject.optString("productname");
            this.trips = new ArrayList<String>();
            JSONArray jsonArray = jsonObject.optJSONArray("trip");
            if (jsonArray != null && jsonArray.length() > 0)
            {

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);

                    trips.add(jsonObject2.optString("trip_id"));
                }
            }
        }
    }
}
