package com.travelrely.v2.model;

import android.content.ContentValues;
import android.database.Cursor;

public class ExpressPrice
{
    private int id;
    private String state_name;
    private int currency_unit;
    private int express_price;
    
    public int getId()
    {
        return id;
    }

    public String getStateName()
    {
        return state_name;
    }

    public void setStateName(String state_name)
    {
        this.state_name = state_name;
    }

    public int getCurrencyUnit()
    {
        return currency_unit;
    }

    public void setCurrencyUnit(int currency_unit)
    {
        this.currency_unit = currency_unit;
    }

    public int getExpressPrice()
    {
        return express_price;
    }

    public void setExpressPrice(int express_price)
    {
        this.express_price = express_price;
    }
    
    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("state_name", state_name);
        contentValues.put("currency_unit", currency_unit);
        contentValues.put("express_price", express_price);

        return contentValues;
    }
    
    public void setCursorValue(Cursor cursor)
    {
        id = cursor.getInt(cursor.getColumnIndex("id"));
        state_name = cursor.getString(cursor.getColumnIndex("state_name"));
        express_price = cursor.getInt(cursor.getColumnIndex("express_price"));
        currency_unit = cursor.getInt(cursor.getColumnIndex("currency_unit"));
    }
}
