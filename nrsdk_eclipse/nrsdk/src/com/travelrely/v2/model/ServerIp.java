package com.travelrely.v2.model;

import android.content.ContentValues;
import android.database.Cursor;

public class ServerIp
{
    private String mcc;
    private int port;
    private String ip;

    public String getMcc()
    {
        return mcc;
    }

    public void setMcc(String mcc)
    {
        this.mcc = mcc;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mcc", mcc);
        contentValues.put("port", port);
        contentValues.put("ip", ip);

        return contentValues;
    }
    
    public void setCursorValue(Cursor cursor)
    {
        mcc = cursor.getString(cursor.getColumnIndex("mcc"));
        port = cursor.getInt(cursor.getColumnIndex("port"));
        ip = cursor.getString(cursor.getColumnIndex("ip"));
    }
}
