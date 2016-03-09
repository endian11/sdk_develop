package com.travelrely.v2.model;

import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class SubOrder
{
    String orderId;
    String orderFee;
    String orderExpPrice;
    String orderExpBarcode;
    String orderDiscount;
    int orderCurrencyUnit;
    int orderStatus;
    
    
    String subOrderId;

    // 订单类型，用于区分是普通订单和香港友好用户订单
    int subOrderType;

    private double subOrderFee;

    // 行程列表
    List<Trip> trips;
    
    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderFee()
    {
        return orderFee;
    }

    public void setOrderFee(String orderFee)
    {
        this.orderFee = orderFee;
    }

    public String getOrderExpPrice()
    {
        return orderExpPrice;
    }

    public void setOrderExpPrice(String orderExpPrice)
    {
        this.orderExpPrice = orderExpPrice;
    }

    public String getOrderExpBarcode()
    {
        return orderExpBarcode;
    }

    public void setOrderExpBarcode(String orderExpBarcode)
    {
        this.orderExpBarcode = orderExpBarcode;
    }

    public String getOrderDiscount()
    {
        return orderDiscount;
    }

    public void setOrderDiscount(String orderDiscount)
    {
        this.orderDiscount = orderDiscount;
    }

    public int getOrderCurrencyUnit()
    {
        return orderCurrencyUnit;
    }

    public void setOrderCurrencyUnit(int orderCurrencyUnit)
    {
        this.orderCurrencyUnit = orderCurrencyUnit;
    }

    public int getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getSubOrderId()
    {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId)
    {
        this.subOrderId = subOrderId;
    }

    public int getOrderType()
    {
        return subOrderType;
    }

    public void setOrderType(int orderType)
    {
        this.subOrderType = orderType;
    }

    public double getSubOrderFee()
    {
        return subOrderFee;
    }

    public void setSubOrderFee(double fee)
    {
        this.subOrderFee = fee;
    }

    public List<Trip> getTrips()
    {
        return trips;
    }

    public void setTrips(List<Trip> trips)
    {
        this.trips = trips;
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_id", orderId);
        contentValues.put("order_fee", orderFee);
        contentValues.put("order_exp_price", orderExpPrice);
        contentValues.put("order_exp_barcode", orderExpBarcode);
        contentValues.put("order_discount", orderDiscount);
        contentValues.put("order_currency_unit", orderCurrencyUnit);
        contentValues.put("order_status", orderStatus);
        
        contentValues.put("sub_order_id", subOrderId);
        contentValues.put("sub_order_type", subOrderType);
        contentValues.put("sub_order_fee", subOrderFee);

        return contentValues;
    }
    
    public void setCursorValue(Cursor cursor)
    {
        orderId = cursor.getString(cursor.getColumnIndex("order_id"));
        orderFee = cursor.getString(cursor.getColumnIndex("order_fee"));
        orderExpPrice = cursor.getString(cursor.getColumnIndex("order_exp_price"));
        orderExpBarcode = cursor.getString(cursor.getColumnIndex("order_exp_barcode"));
        orderDiscount = cursor.getString(cursor.getColumnIndex("order_discount"));
        orderCurrencyUnit = cursor.getInt(cursor.getColumnIndex("order_currency_unit"));
        orderStatus = cursor.getInt(cursor.getColumnIndex("order_status"));
        
        subOrderId = cursor.getString(cursor.getColumnIndex("sub_order_id"));
        subOrderType = cursor.getInt(cursor.getColumnIndex("sub_order_type"));
        subOrderFee = cursor.getDouble(cursor.getColumnIndex("sub_order_fee"));
    }
    
    public void setJsonValue(JSONObject jsonP, JSONObject jsonS)
    {
        orderId = jsonP.optString("orderid");
        orderFee = jsonP.optString("totalfee");
        orderExpPrice = jsonP.optString("expressprice");
        orderExpBarcode = jsonP.optString("");
        orderDiscount = jsonP.optString("favorableprice");
        orderCurrencyUnit = jsonP.optInt("");
        orderStatus = jsonP.optInt("status");
        
        subOrderId = jsonS.optString("suborderid");
        subOrderType = jsonS.optInt("ordertype");
        subOrderFee = jsonS.optDouble("suborderfee");
    }
}
