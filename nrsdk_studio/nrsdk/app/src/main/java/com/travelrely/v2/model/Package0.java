package com.travelrely.v2.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Package0
{
    private String mcc;
    private String mnc;
    private int days;
    private int price;

    private String data;
    private String localVoice;
    private String iddVoice;

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

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int packageprice)
    {
        this.price = packageprice;
    }

    public int getDays()
    {
        return days;
    }

    public void setDays(int days)
    {
        this.days = days;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getLocalVoice()
    {
        return localVoice;
    }

    public void setLocalVoice(String localVoice)
    {
        this.localVoice = localVoice;
    }

    public String getIddVoice()
    {
        return iddVoice;
    }

    public void setIddVoice(String iddVoice)
    {
        this.iddVoice = iddVoice;
    }
    
    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mcc", mcc);
        contentValues.put("mnc", mnc);
        contentValues.put("days", days);
        contentValues.put("price", price);
        contentValues.put("data", data);
        contentValues.put("localvoice", localVoice);
        contentValues.put("iddvoice", iddVoice);

        return contentValues;
    }
    
    public void setCursorValue(Cursor cursor)
    {
        mcc = cursor.getString(cursor.getColumnIndex("mcc"));
        mnc = cursor.getString(cursor.getColumnIndex("mnc"));
        days = cursor.getInt(cursor.getColumnIndex("days"));
        price = cursor.getInt(cursor.getColumnIndex("price"));
        data = cursor.getString(cursor.getColumnIndex("data"));
        localVoice = cursor.getString(cursor.getColumnIndex("localvoice"));
        iddVoice = cursor.getString(cursor.getColumnIndex("iddvoice"));
    }
}
