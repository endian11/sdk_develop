package com.travelrely.model;

import com.travelrely.v2.net_interface.OrderQueryRsp.Data.OrderInfo;

public class OrderInfoInterface extends OrderInfo
{
    private static final long serialVersionUID = 1L;

    // 记录所在订单的ID号(订单编号)
    String strOrderId = "";
    
    String subOrderId = "";
    
    // 订单类型，用于区分是普通订单和香港友好用户订单
    String strOrderType;
    
    public void setOrderId(String orderId)
    {
        strOrderId = orderId;
    }
    
    public String getOrderId()
    {
        return strOrderId;
    }
    
    public String getSubOrderId()
    {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId)
    {
        this.subOrderId = subOrderId;
    }
}
