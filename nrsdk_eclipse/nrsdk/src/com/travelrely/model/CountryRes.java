package com.travelrely.model;

public class CountryRes
{
    private int nameId;

    private int flagId;

    private int unitId;

    private String country_name;

    private String country_short_name;

    private String carrier_name;

    private String national_flag;

    private String website_url;
    
    private int packagetype; //0-预付费 1-后付费
    private String packageDesc; //0-预付费 1-后付费

    public int getNameId()
    {
        return nameId;
    }

    public void setNameId(int nameId)
    {
        this.nameId = nameId;
    }

    public int getFlagId()
    {
        return flagId;
    }

    public void setFlagId(int flagId)
    {
        this.flagId = flagId;
    }

    public int getMonetaryUnitId()
    {
        return unitId;
    }

    public void setMonetaryUnitId(int unitId)
    {
        this.unitId = unitId;
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
}
