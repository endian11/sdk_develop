package com.travelrely.v2.model;

import java.io.Serializable;

//The model of coupon info
public class CouponInfo implements Serializable {
	
	private static final long serialVersionUID = 4318838659250781724L;
	
	// 优惠券金额
    private double couponMoney;
    
    // 优惠码
    private String couponCode;
    
    // 优惠劵类型, 0-金额, 1-折扣
    private int couponType;
    
	public double getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(double couponMoney) {
		this.couponMoney = couponMoney;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponCode() {
		return couponCode;
	}

	@Override
	public String toString() {
		return "ExpressInfo [couponMoney=" + couponMoney + ", couponType=" + couponType
				+ ", couponType=" + couponType + "]";
	}

}
