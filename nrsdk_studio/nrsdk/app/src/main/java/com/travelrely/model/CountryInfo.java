package com.travelrely.model;

import android.content.ContentValues;
import android.database.Cursor;

public class CountryInfo extends CursorBean
{
    public static final String table_name = "country_info";
    
    private CountryRes res; //本地需要管理的资源

    // 以下是接口中需要管理的信息
    private String mcc;

    private String mnc;

    private String cc;

    private String country_name;

    private String country_short_name;

    private String carrier_name;

    private String national_flag;

    private String website_url;
    
    private int packagetype; //0-预付费 1-后付费
    private String packageDesc; //0-预付费 1-后付费

    public String getMcc()
    {
        return mcc;
    }

    public void setMcc(String mcc)
    {
        this.mcc = mcc;
    }

    public String getMnc()
    {
        return mnc;
    }

    public void setMnc(String mnc)
    {
        this.mnc = mnc;
    }

    public String getCC()
    {
        return cc;
    }

    public void setCC(String cc)
    {
        this.cc = cc;
    }

    public String getCountryName()
    {
        return country_name;
    }

    public void setCountryName(String country_name)
    {
        this.country_name = country_name;
    }

    public String getCountryShortName()
    {
        return country_short_name;
    }

    public void setCountryShortName(String country_short_name)
    {
        this.country_short_name = country_short_name;
    }

    public String getCarrierName()
    {
        return carrier_name;
    }

    public void setCarrierName(String carrier_name)
    {
        this.carrier_name = carrier_name;
    }

    public String getNationalFlag()
    {
        return national_flag;
    }

    public void setNationalFlag(String national_flag)
    {
        this.national_flag = national_flag;
    }

    public String getWebsiteUrl()
    {
        return website_url;
    }

    public void setWebsiteUrl(String website_url)
    {
        this.website_url = website_url;
    }
    
    public int getPkgType()
    {
        return packagetype;
    }

    public void setPkgType(int packagetype)
    {
        this.packagetype = packagetype;
    }
    
    public String getPkgDesc()
    {
        return packageDesc;
    }

    public void setPkgDesc(String packagetype)
    {
        this.packageDesc = packagetype;
    }
    
    public CountryRes getRes()
    {
        return res;
    }

    public void setRes(CountryRes res)
    {
        this.res = res;
    }

    public static String getTableName()
    {
        return table_name;
    }

    public void setValue(Cursor cursor)
    {
        super.setValue(cursor);
        mcc = cursor.getString(cursor.getColumnIndex("mcc"));
        mnc = cursor.getString(cursor.getColumnIndex("mnc"));
        cc = cursor.getString(cursor.getColumnIndex("cc"));
        country_name = cursor.getString(cursor.getColumnIndex("country_name"));
        country_short_name = cursor.getString(cursor
                .getColumnIndex("country_short_name"));
        carrier_name = cursor.getString(cursor.getColumnIndex("carrier_name"));
        national_flag = cursor
                .getString(cursor.getColumnIndex("national_flag"));
        website_url = cursor.getString(cursor.getColumnIndex("website_url"));
        packagetype = cursor.getInt(cursor.getColumnIndex("packagetype"));
        packageDesc = cursor.getString(cursor.getColumnIndex("packageDesc"));
    }

    public ContentValues getValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mcc", mcc);
        contentValues.put("mnc", mnc);
        contentValues.put("cc", cc);
        contentValues.put("country_name", country_name);
        contentValues.put("country_short_name", country_short_name);
        contentValues.put("carrier_name", carrier_name);
        contentValues.put("national_flag", national_flag);
        contentValues.put("website_url", website_url);
        contentValues.put("packagetype", packagetype);
        contentValues.put("packageDesc", packageDesc);

        return contentValues;
    }

    public void setData(ContentValues contentValues)
    {
        setMcc(contentValues.getAsString("mcc"));
        setMnc(contentValues.getAsString("mnc"));
        setCC(contentValues.getAsString("cc"));
        setCountryName(contentValues.getAsString("country_name"));
        setCountryShortName(contentValues.getAsString("country_short_name"));
        setCarrierName(contentValues.getAsString("carrier_name"));
        setNationalFlag(contentValues.getAsString("national_flag"));
        setWebsiteUrl(contentValues.getAsString("website_url"));
        setPkgType(contentValues.getAsInteger("packagetype"));
        setPkgDesc(contentValues.getAsString("packageDesc"));
    }
}
