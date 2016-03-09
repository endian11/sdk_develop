package com.travelrely.v2.model;

import java.io.Serializable;

public class UsrSimInfo implements Serializable
{
    @Override
	public String toString() {
		return "UsrSimInfo [usrPhone=" + usrPhone + ", iSimType=" + iSimType
				+ ", iSimSize=" + iSimSize + ", netcall_buyflag="
				+ netcall_buyflag + ", bt_buyflag=" + bt_buyflag
				+ ", lxnum_flag=" + lxnum_flag + ", lxnum_price=" + lxnum_price
				+ ", price_package=" + price_package + "]";
	}

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String usrPhone;//使用人手机号

    private int iSimType;//暂时无用

    private int iSimSize;//卡大小
    
    private int netcall_buyflag;//暂时无用
    
    private int bt_buyflag;
  //暂时无用
    private int lxnum_flag;//暂时无用
    
    private double lxnum_price;//暂时无用
    
    private double price_package;//单个订单金额
    
    public double getPrice_package() {
        return price_package;
    }

    public void setPrice_package(double price_package) {
        this.price_package = price_package;
    }

    public double getLxnum_price() {
        return lxnum_price;
    }

    public void setLxnum_price(double lxnum_price) {
        this.lxnum_price = lxnum_price;
    }

    public int getNetcall_buyflag() {
        return netcall_buyflag;
    }

    public void setNetcall_buyflag(int netcall_buyflag) {
        this.netcall_buyflag = netcall_buyflag;
    }

    public int getBt_buyflag() {
        return bt_buyflag;
    }

    public void setBt_buyflag(int bt_buyflag) {
        this.bt_buyflag = bt_buyflag;
    }

    public int getLxnum_flag() {
        return lxnum_flag;
    }

    public void setLxnum_flag(int lxnum_flag) {
        this.lxnum_flag = lxnum_flag;
    }

    public String getPhone()
    {
        return usrPhone;
    }

    public void setPhone(String phone)
    {
        usrPhone = phone;
    }

    public int getSimType()
    {
        return iSimType;
    }

    public void setSimType(int simType)
    {
        iSimType = simType;
    }

    public int getSimSize()
    {
        return iSimSize;
    }

    public void setSimSize(int size)
    {
        this.iSimSize = size;
    }
}
