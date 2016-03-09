package com.travelrely.model;

import java.io.Serializable;
import java.util.Calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.CallLog;

import com.travelrely.v2.db.ContactDBHelper;
import com.travelrely.v2.util.TimeUtil;

public class CallRecord extends BaseModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String vipNum;

    public String name;

    String time;

    String number;

    long currentTime;

    String day;

    int type; // 来电:1  拨出:2   未接:3

    long duration;//持续时间
    
    long contact_id = -1;//app通讯录的id

    long id;
    
    int numberLable;//拨号方式 1=VOIP拨号 ；2=旅信网络电话拨号
    
    public long getContact_id() {
        return contact_id;
    }

    public void setContact_id(long contact_id) {
        this.contact_id = contact_id;
    }

    public int getNumberLable() {
        return numberLable;
    }

    public void setNumberLable(int numberLable) {
        this.numberLable = numberLable;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public long getCurrentTime()
    {
        return currentTime;
    }

    public void setCurrentTime(long currentTime)
    {
        this.currentTime = currentTime;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
    
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public boolean isOutGoing()
    {
        return type == CallLog.Calls.OUTGOING_TYPE;
    }

    public boolean isCancel()
    {
        if (duration <= 0)
        {
            return true;
        }
        return false;
    }

    public long getDuration()
    {
        return duration;
    }
    
    public void setDuration(long durations)
    {
        this.duration = durations;
    }

    public ContentValues generateValue()
    {
        if(number != null){
            ContactModel cModel = ContactDBHelper.getInstance().getContactByNumberTry(number);
            if(cModel != null){
                contact_id = cModel.getId();
                name = cModel.getName();
            }
        }
        
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("duration", duration);
        contentValues.put("type", type);
        contentValues.put("current_time", Calendar.getInstance().getTimeInMillis());
        contentValues.put("contact_id", contact_id);
        contentValues.put("numberlabel", numberLable);

        return contentValues;
    }

    public void setLocalValues(Cursor c)
    {
        id = c.getInt(c.getColumnIndex("id"));
        name = c.getString(c.getColumnIndex("name"));
        number = c.getString(c.getColumnIndex("number"));
        type = c.getInt(c.getColumnIndex("type"));
        duration = c.getInt(c.getColumnIndex("duration"));
        currentTime = c.getLong(c.getColumnIndex("current_time"));
        time = TimeUtil.getDateString(currentTime, TimeUtil.dateFormat1);
        day = TimeUtil.getDateString(currentTime, TimeUtil.dateFormat4);
        numberLable = c.getInt(c.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL));
        contact_id = c.getInt(c.getColumnIndex("contact_id"));
    }
}
