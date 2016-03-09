package com.travelrely.v2.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SimInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cardUser;//使用人手机号
	private int simcardType;//1.一次性实体硬卡  3.已有vSim卡 2.无vSim卡、首次使用
	private int simcard_size;// 0 标准卡 1.Micro卡 2.Nano
	 // 行程开始日期
    private String beginDate;

    // 行程结束日期
    private String endDate;
    
    private int orderType;//orderType 订单类型
    
    private String product_id;
    private String imei;
    
    
    // 物流信息
    private ExpressInfo expressInfo;

    // 优惠券信息
    private CouponInfo couponInfo;
    
    private double userMoney;//package 价格
    
    
    public ExpressInfo getExpressInfo() {
		return expressInfo;
	}

	public void setExpressInfo(ExpressInfo expressInfo) {
		this.expressInfo = expressInfo;
	}

	public CouponInfo getCouponInfo() {
		return couponInfo;
	}

	public void setCouponInfo(CouponInfo couponInfo) {
		this.couponInfo = couponInfo;
	}

	public String getCardUser() {
		return cardUser;
	}

	public void setCardUser(String cardUser) {
		this.cardUser = cardUser;
	}

	public int getSimcardType() {
		return simcardType;
	}

	public void setSimcardType(int simcardType) {
		this.simcardType = simcardType;
	}

	public int getSimcard_size() {
		return simcard_size;
	}

	public void setSimcard_size(int simcard_size) {
		this.simcard_size = simcard_size;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
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
    public final static SimInfo deSerialization(String str) throws IOException,
            ClassNotFoundException
    {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        SimInfo cart = (SimInfo) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return cart;
    }

	public void setPrice_package(double userMoney) {
		// TODO Auto-generated method stub
		
	}
    
}
