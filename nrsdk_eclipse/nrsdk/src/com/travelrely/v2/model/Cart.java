package com.travelrely.v2.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.travelrely.model.PersonalInfo;

//The model of Cart
public class Cart implements Serializable
{
    private static final long serialVersionUID = 4318838659250781720L;

    // number of commodity
    private int iTtlNum;

    // 已选择的商品数量
    private int iSelectedNum;

    // price of order
    private double orderPrice;
    
    // price of order
    private double selectedOrderPrice;

    // 总价
    private double totalMoney;

    // 货币计算单位
    private String currencyUnit;

    // mode of payment
    private int paymentMode; // 1-账户余额, 2-支付宝客户端, 3-支付宝网页

    // 物流信息
    private ExpressInfo expressInfo;

    // 优惠券信息
    private CouponInfo couponInfo;

    // 商品列表
    private List<Commodity> commodityItems;
    
    //个人信息字典
    private PersonalInfo personalInfo;
    

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public int getTtlNumber()
    {
        return iTtlNum;
    }

    public void setTtlNumber(int number)
    {
        this.iTtlNum = number;
    }
    
    public int getSelectedNum()
    {
        return iSelectedNum;
    }

    public void setSelectedNum(int number)
    {
        this.iSelectedNum = number;
    }

    public List<Commodity> getCommodityItems()
    {
        return commodityItems;
    }

    public void setCommodityItems(List<Commodity> commodityItems)
    {
        this.commodityItems = commodityItems;
    }

    public double getTtlOrderPrice()
    {
        return orderPrice;
    }

    public void setTtlOrderPrice(double orderPrice)
    {
        this.orderPrice = orderPrice;
    }
    
    public double getSelectedOrderPrice()
    {
        return selectedOrderPrice;
    }

    public void setSelectedOrderPrice(double orderPrice)
    {
        this.selectedOrderPrice = orderPrice;
    }

    public double getTotalMoney()
    {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney)
    {
        this.totalMoney = totalMoney;
    }

    public String getCurrencyUnit()
    {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit)
    {
        this.currencyUnit = currencyUnit;
    }

    public int getPaymentMode()
    {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode)
    {
        this.paymentMode = paymentMode;
    }

    public ExpressInfo getExpressInfo()
    {
        return expressInfo;
    }

    public void setExpressInfo(ExpressInfo expressInfo)
    {
        this.expressInfo = expressInfo;
    }

    public CouponInfo getCouponInfo()
    {
        return couponInfo;
    }

    public void setCouponInfo(CouponInfo couponInfo)
    {
        this.couponInfo = couponInfo;
    }

    @Override
    public String toString()
    {
        return "Commodity [number=" + iTtlNum + ", orderPrice=" + orderPrice
                + ", totalMoney=" + totalMoney + ", currencyUnit="
                + currencyUnit + ", paymentMode=" + paymentMode
                + ", expressInfo=" + expressInfo + ", couponInfo=" + couponInfo
                + ", commodityItems=" + commodityItems + "]";
    }

    /**
     * 序列化对象
     * 
     * @param person
     * @return
     * @throws IOException
     */
    public final static String serialize(Cart cart) throws IOException
    {
    	System.out.println("serialize cart " + cart);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(cart);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    /**
     * 反序列化对象
     * 
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public final static Cart deSerialization(String str) throws IOException,
            ClassNotFoundException
    {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Cart cart = (Cart) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return cart;
    }
}