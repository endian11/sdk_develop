package com.travelrely.v2.model;

import android.content.ContentValues;
import android.database.Cursor;

public class RoamProfile 
{    
    int id;

    String mcc;

    String mnc;

    String ip_dial_number;

    String is_homing;
    
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

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

    public String getIp_dial_number()
    {
        return ip_dial_number;
    }

    public void setIp_dial_number(String ip_dial_number)
    {
        this.ip_dial_number = ip_dial_number;
    }

    public String getIs_homing()
    {
        return is_homing;
    }

    public void setIs_homing(String is_homing)
    {
        this.is_homing = is_homing;
    }

    public void setCursorValue(Cursor cursor)
    {
        mcc = cursor.getString(cursor.getColumnIndex("mcc"));
        mnc = cursor.getString(cursor.getColumnIndex("mnc"));
        ip_dial_number = cursor.getString(cursor
                .getColumnIndex("ip_dial_number"));
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mcc", mcc);
        contentValues.put("mnc", mnc);
        contentValues.put("ip_dial_number", ip_dial_number);

        return contentValues;
    }
}
