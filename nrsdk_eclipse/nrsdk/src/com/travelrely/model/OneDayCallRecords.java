package com.travelrely.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.travelrely.v2.util.TimeUtil;

/**
 * 一天的拨号记录
 * 
 * @author developer
 */
public class OneDayCallRecords implements Comparable<OneDayCallRecords>,
        Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private long time;

    private String keyNumber;

    private String name;

    NumberCity numberCity;

    public NumberCity getNumberCity()
    {
        return numberCity;
    }

    public void setNumberCity(NumberCity numberCity)
    {
        this.numberCity = numberCity;
    }

    public String getName()
    {
        return name;
    }

    public String getDisplayName()
    {
        if (name == null || name.equals(""))
        {
            return keyNumber;
        }
        else
        {
            return name;
        }
    }

    public String getTimesStr()
    {
        if (getRecordSize() > 1)
        {
            return "(" + getRecordSize() + ")";
        }
        else
        {
            return "";
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private List<CallRecord> callRecords = new ArrayList<CallRecord>();

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = Math.max(time, this.time);
    }

    public String getKeyNumber()
    {
        return keyNumber;
    }

    public void setKeyNumber(String keyNumber)
    {
        this.keyNumber = keyNumber;
        numberCity = new NumberCity(keyNumber);
    }

    public List<CallRecord> getCallRecords()
    {
        return callRecords;
    }

    public int getRecordSize()
    {
        return callRecords.size();
    }

    public void setCallRecords(List<CallRecord> callRecords)
    {
        this.callRecords = callRecords;
    }

    @Override
    public int compareTo(OneDayCallRecords another)
    {
        if (another.time < this.time)
        {
            return -1;
        }
        else if (another.time > this.time)
        {

            return 1;
        }

        return 0;
    }

    long ONE_DAY = 1000 * 60 * 60 * 24;

    public String getDisplayTime()
    {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        if (day == currentDay)
        {
            return TimeUtil.getDateString(time, TimeUtil.dateFormat9);
        }

        if (currentDay - day == 1)
        {
            return "昨天";
        }

        if (currentDay - day > 1 && currentDay - day <= 7)
        {
            return TimeUtil.getDay(time);
        }
        else
        {
            return TimeUtil.getDateString(time, TimeUtil.dateFormat4);

        }
    }
}
