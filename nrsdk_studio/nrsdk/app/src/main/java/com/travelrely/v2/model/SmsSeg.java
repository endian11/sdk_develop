package com.travelrely.v2.model;

import android.content.ContentValues;
import android.database.Cursor;

public class SmsSeg
{
    private String peer;
    private int xx;
    private int num;
    private int sn;
    
    private int len;
    private String content;
    
    public String getPeer()
    {
        return peer;
    }

    public void setPeer(String peer)
    {
        this.peer = peer;
    }

    public int getXx()
    {
        return xx;
    }

    public void setXx(int xx)
    {
        this.xx = xx;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getSn()
    {
        return sn;
    }

    public void setSn(int sn)
    {
        this.sn = sn;
    }

    public int getLen()
    {
        return len;
    }

    public void setLen(int len)
    {
        this.len = len;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("peer", peer);
        contentValues.put("xx", xx);
        contentValues.put("num", num);
        contentValues.put("sn", sn);
        contentValues.put("len", len);
        contentValues.put("content", content);

        return contentValues;
    }
    
    public void setCursorValue(Cursor cursor)
    {
        peer = cursor.getString(cursor.getColumnIndex("peer"));
        xx = cursor.getInt(cursor.getColumnIndex("xx"));
        num = cursor.getInt(cursor.getColumnIndex("num"));
        sn = cursor.getInt(cursor.getColumnIndex("sn"));
        len = cursor.getInt(cursor.getColumnIndex("len"));
        content = cursor.getString(cursor.getColumnIndex("content"));
    }
}
