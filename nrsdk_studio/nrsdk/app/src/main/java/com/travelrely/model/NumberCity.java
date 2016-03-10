package com.travelrely.model;

import java.io.Serializable;
import java.util.TimeZone;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.util.TimeZoneIDS;
import com.travelrely.sdk.R;

public class NumberCity implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String cityName;

    private String number;

    private String timeZoneId;

    long timeInCity;

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getTimeZoneId()
    {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId)
    {
        this.timeZoneId = timeZoneId;
    }

    public NumberCity(String number)
    {
        this.number = number;
        generateTimeZoneIdAndCityName();
    }

    public void generateTimeZoneIdAndCityName()
    {
        if (number == null || number.equals("") || number.length() < 3)
        {
            timeZoneId = TimeZoneIDS.Asia_Shang_hai;
            cityName = Engine.getInstance().getString(R.string.unknown);
            return;
        }

        // 如果包含+
        if (number.charAt(0) == '+')
        {
            if (number.charAt(1) == '1')
            {
                // 是美国
                timeZoneId = TimeZoneIDS.America_New_York;
                cityName = Engine.getInstance().getString(R.string.usa);
                return;
            }
            else if (number.charAt(1) == '8' && number.charAt(2) == '6')
            {
                timeZoneId = TimeZoneIDS.Asia_Shang_hai;
                cityName = Engine.getInstance().getString(R.string.china_1);
                return;
            }
            else if (number.charAt(1) == '8' && number.charAt(2) == '5' && number.charAt(3) == '2')
            {
                timeZoneId = TimeZoneIDS.Asia_Hongkong;
                cityName = Engine.getInstance().getString(R.string.Hongkong);
                return;
            }
            else if (number.charAt(1) == '6' && number.charAt(2) == '2')
            {
                timeZoneId = TimeZoneIDS.Asia_Indonesia_Jakarta;
                cityName = Engine.getInstance().getString(R.string.Indonesia);
                return;
            }
            else if (number.charAt(1) == '6' && number.charAt(2) == '6')
            {
                timeZoneId = TimeZoneIDS.Asia_Thailand;
                cityName = Engine.getInstance().getString(R.string.Thailand);
                return;
            }
        }

        if (number.charAt(0) == '0')
        {
            if (number.charAt(1) == '0')
            {
                // 说明他是国家码
                if (number.charAt(2) == '1')
                {
                    timeZoneId = TimeZoneIDS.America_New_York;
                    cityName = Engine.getInstance().getString(R.string.usa);
                    return;
                }
            }
        }

        timeZoneId = TimeZoneIDS.Asia_Shang_hai;
        cityName = Engine.getInstance().getString(R.string.china_1);
    }

    public long getTimeInCity()
    {
        long localTime = System.currentTimeMillis();
        String TimeZoneId = timeZoneId;
        
//        String[] TimeZones = TimeZone.getAvailableIDs();
//        for (String s : TimeZones)
//        {
//            System.out.println(s);
//        }

        long targetOffset = TimeZone.getTimeZone(TimeZoneId).getOffset(
                localTime);
        long defaultOffset = TimeZone.getDefault().getOffset(localTime);
        long added = targetOffset - defaultOffset + localTime;

        timeInCity = added;
        return timeInCity;
    }
}
