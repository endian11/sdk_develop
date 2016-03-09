package com.travelrely.model;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

public class Adv extends CursorBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;

    private String big_adv_pic;

    private String small_adv_pic;

    private String web_site;

    private String big_size;

    private String small_size;

    public String getSmall_size()
    {
        return small_size;
    }

    public void setSmall_size(String small_size)
    {
        this.small_size = small_size;
    }

    public String getBig_size()
    {
        return big_size;
    }

    public void setBig_size(String big_size)
    {
        this.big_size = big_size;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBig_adv_pic()
    {
        return big_adv_pic;
    }

    public void setBig_adv_pic(String big_adv_pic)
    {
        this.big_adv_pic = big_adv_pic;
    }

    public String getSmall_adv_pic()
    {
        return small_adv_pic;
    }

    public void setSmall_adv_pic(String small_adv_pic)
    {
        this.small_adv_pic = small_adv_pic;
    }

    public String getWeb_site()
    {
        return web_site;
    }

    public void setWeb_site(String web_site)
    {
        this.web_site = web_site;
    }

    public void setValue(Cursor cursor)
    {

        super.setValue(cursor);
        name = cursor.getString(cursor.getColumnIndex("name"));
        big_adv_pic = cursor.getString(cursor.getColumnIndex("big_adv_pic"));
        small_adv_pic = cursor
                .getString(cursor.getColumnIndex("small_adv_pic"));
        web_site = cursor.getString(cursor.getColumnIndex("web_site"));
        big_size = cursor.getString(cursor.getColumnIndex("big_size"));
        small_size = cursor.getString(cursor.getColumnIndex("small_size"));

    }

    public ContentValues getValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("big_adv_pic", big_adv_pic);
        contentValues.put("small_adv_pic", small_adv_pic);
        contentValues.put("web_site", web_site);
        contentValues.put("big_size", big_size);
        contentValues.put("small_size", small_size);

        return contentValues;
    }

    public void setData(ContentValues contentValues)
    {
        setName(contentValues.getAsString("name"));
        setBig_adv_pic(contentValues.getAsString("big_adv_pic"));
        setSmall_adv_pic(contentValues.getAsString("small_adv_pic"));
        setWeb_site(contentValues.getAsString("web_site"));
        setBig_size(contentValues.getAsString("big_size"));
        setSmall_size(contentValues.getAsString("small_size"));
    }
}
