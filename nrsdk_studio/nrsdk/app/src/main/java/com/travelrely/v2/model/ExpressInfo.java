package com.travelrely.v2.model;

import java.io.Serializable;

//The model of express info
public class ExpressInfo implements Serializable {
	
	private static final long serialVersionUID = 4318838659250781723L;
	
	// 递送方式，1-快递, 2-机场自取
	private int deliveryMode;
	
	// 快递地址编号
	private String id;
	
	// 收件人姓名
	private String consignee;
	
	// 国家
	private String country;
	
	// 州或省
	private String state;
	
	// 城市
	private String city;
	
	// 区/县
	private String county;
	
	// 地址
	private String address;
	
	// 邮编
	private String postalCode;
	
	// 电话号码
	private String phoneNumber;

	// 货币计算单位
	private String currencyUnit;
	
	// 快递价格
	private double price;

	public int getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(int deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getCurrencyUnit() {
		return currencyUnit;
	}

	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ExpressInfo [deliveryMode=" + deliveryMode + ", id=" + id
				+ ", consignee=" + consignee + ", country=" + country + ", state=" + state
				+ ", city=" + city + ", county=" + county + ", address=" + address 
				+ ", postalCode=" + postalCode + ", phoneNumber=" + phoneNumber
				+ ", currencyUnit=" + currencyUnit + ", price=" + price + "]";
	}

}
