package com.travelrely.model;

import java.io.Serializable;

import com.travelrely.v2.net_interface.OrderQueryRsp.Data.Trip;

public class TripInterface implements Serializable
{
    private static final long serialVersionUID = 1L;

    // 记录所在订单的ID号(订单编号)
    String strOrderId;
    
    // 订单类型，用于区分是普通订单和香港友好用户订单
    int strOrderType;
    
    Trip trip;
    
    public void setOrderId(String orderId)
    {
        strOrderId = orderId;
    }
    
    public String getOrderId()
    {
        return strOrderId;
    }
    
    public int getOrderType()
    {
        return strOrderType;
    }

    public void setOrderType(int strOrderType)
    {
        this.strOrderType = strOrderType;
    }
    
    public Trip getTrip()
    {
        return trip;
    }

    public void setTrip(Trip trip)
    {
        this.trip = trip;
    }
}
