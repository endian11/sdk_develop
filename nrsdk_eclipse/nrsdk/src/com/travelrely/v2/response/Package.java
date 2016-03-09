package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;

public class Package extends BaseResponse implements Serializable
{
    /**
     * 
     */
    public static final long serialVersionUID = 1L;
    public String mnc;
    public String countryname;
    public String carriername;

    public String packageid;
    public String packageidVoice;

    public String getPackageid()
    {
        return packageid;
    }

    public void setPackageid(String packageid)
    {
        this.packageid = packageid;
    }

    public String getPackageidVoice()
    {
        return packageidVoice;
    }

    public void setPackageidVoice(String packageidVoice)
    {
        this.packageidVoice = packageidVoice;
    }

    public double getPackageprice()
    {
        return packageprice;
    }

    public void setPackageprice(double packageprice)
    {
        this.packageprice = packageprice;
    }

    public double getPackagepriceVoice()
    {
        return packagepriceVoice;
    }

    public void setPackagepriceVoice(double packagepriceVoice)
    {
        this.packagepriceVoice = packagepriceVoice;
    }

    public int getPackagecurrency()
    {
        return packagecurrency;
    }

    public void setPackagecurrency(int packagecurrency)
    {
        this.packagecurrency = packagecurrency;
    }

    public int getPackagecurrencyVoice()
    {
        return packagecurrencyVoice;
    }

    public void setPackagecurrencyVoice(int packagecurrencyVoice)
    {
        this.packagecurrencyVoice = packagecurrencyVoice;
    }

    public int getPackagedata()
    {
        return packagedata;
    }

    public void setPackagedata(int packagedata)
    {
        this.packagedata = packagedata;
    }

    public int getPackagevoice()
    {
        return packagevoice;
    }

    public void setPackagevoice(int packagevoice)
    {
        this.packagevoice = packagevoice;
    }

    public double packageprice;
    public double packagepriceVoice;

    public int packagecurrency;
    public int packagecurrencyVoice;

    public int packagedata;
    public int packagevoice;

    public String getMnc()
    {
        return mnc;
    }

    public void setMnc(String mnc)
    {
        this.mnc = mnc;
    }

    public String getCountryname()
    {
        return countryname;
    }

    public void setCountryname(String countryname)
    {
        this.countryname = countryname;
    }

    public String getCarriername()
    {
        return carriername;
    }

    public void setCarriername(String carriername)
    {
        this.carriername = carriername;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        mnc = jsonObject.optString("mnc");
        countryname = jsonObject.optString("countryname");
        carriername = jsonObject.optString("carriername");
        packageid = jsonObject.optString("packageid");
        packageprice = jsonObject.optDouble("packageprice");
        packagecurrency = jsonObject.optInt("packagecurrency");
        packagedata = jsonObject.optInt("packagedata");
    }

    public void setValueVoic(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        mnc = jsonObject.optString("mnc");
        countryname = jsonObject.optString("countryname");
        carriername = jsonObject.optString("carriername");
        packageidVoice = jsonObject.optString("packageid");
        packagepriceVoice = jsonObject.optDouble("packageprice");
        packagecurrencyVoice = jsonObject.optInt("packagecurrency");
        packagevoice = jsonObject.optInt("packagevoice");
    }

}
