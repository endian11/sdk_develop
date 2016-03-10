package com.travelrely.v2.model;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class Trip
{
    private String subOrderId;
    
    private String tripId;
    private String user;
    
    private String begin;
    private String end;
    
    private String mcc;
    private String mnc;
    private int simType;
    private int simSize;
    private int data;
    private int localVoice;
    private int iddVoice;
    private int remainData;
    private int remainLocal;
    private int remainIdd;
    
    private int crbtType;
    
    private int days;
    
    private int btBox;

    private int price;

    public String getSubOrderId()
    {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId)
    {
        this.subOrderId = subOrderId;
    }

    public String getTripId()
    {
        return tripId;
    }

    public void setTripId(String tripId)
    {
        this.tripId = tripId;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getBegin()
    {
        return begin;
    }

    public void setBegin(String begin)
    {
        this.begin = begin;
    }

    public String getEnd()
    {
        return end;
    }

    public void setEnd(String end)
    {
        this.end = end;
    }

    public int getSimType()
    {
        return simType;
    }

    public void setSimType(int simType)
    {
        this.simType = simType;
    }

    public int getSimSize()
    {
        return simSize;
    }

    public void setSimSize(int simSize)
    {
        this.simSize = simSize;
    }

    public int getCrbtType()
    {
        return crbtType;
    }

    public void setCrbtType(int crbtType)
    {
        this.crbtType = crbtType;
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

    public int getData()
    {
        return data;
    }

    public void setData(int data)
    {
        this.data = data;
    }

    public int getLocalVoice()
    {
        return localVoice;
    }

    public void setLocalVoice(int localVoice)
    {
        this.localVoice = localVoice;
    }

    public int getIddVoice()
    {
        return iddVoice;
    }

    public void setIddVoice(int iddVoice)
    {
        this.iddVoice = iddVoice;
    }
    
    public int getRemainData()
    {
        return remainData;
    }

    public void setRemainData(int remainData)
    {
        this.remainData = remainData;
    }

    public int getRemainLocal()
    {
        return remainLocal;
    }

    public void setRemainLocal(int remainLocal)
    {
        this.remainLocal = remainLocal;
    }

    public int getRemainIdd()
    {
        return remainIdd;
    }

    public void setRemainIdd(int remainIdd)
    {
        this.remainIdd = remainIdd;
    }

    public int getBtBox()
    {
        return btBox;
    }

    public void setBtBox(int btBox)
    {
        this.btBox = btBox;
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sub_order_id", subOrderId);
        contentValues.put("trip_id", tripId);
        contentValues.put("begin", begin);
        contentValues.put("end", end);
        contentValues.put("mcc", mcc);
        contentValues.put("mnc", mnc);
        contentValues.put("sim_type", simType);
        contentValues.put("sim_size", simSize);
        contentValues.put("crbt_type", crbtType);
        contentValues.put("days", days);
        contentValues.put("data", data);
        contentValues.put("local", localVoice);
        contentValues.put("idd", iddVoice);
        contentValues.put("remain_data", remainData);
        contentValues.put("remain_idd", remainIdd);
        contentValues.put("remain_local", remainLocal);
        contentValues.put("bt_box", btBox);
        contentValues.put("price", price);

        return contentValues;
    }
    
    public void setCursorValue(Cursor cursor)
    {
        subOrderId = cursor.getString(cursor.getColumnIndex("sub_order_id"));
        tripId = cursor.getString(cursor.getColumnIndex("trip_id"));
        user = cursor.getString(cursor.getColumnIndex("user"));
        begin = cursor.getString(cursor.getColumnIndex("begin"));
        end = cursor.getString(cursor.getColumnIndex("end"));
        mcc = cursor.getString(cursor.getColumnIndex("mcc"));
        mnc = cursor.getString(cursor.getColumnIndex("mnc"));
        simType = cursor.getInt(cursor.getColumnIndex("sim_type"));
        simSize = cursor.getInt(cursor.getColumnIndex("sim_size"));
        crbtType = cursor.getInt(cursor.getColumnIndex("crbt_type"));
        days = cursor.getInt(cursor.getColumnIndex("days"));
        data = cursor.getInt(cursor.getColumnIndex("data"));
        localVoice = cursor.getInt(cursor.getColumnIndex("local"));
        iddVoice = cursor.getInt(cursor.getColumnIndex("idd"));
        remainData = cursor.getInt(cursor.getColumnIndex("remain_data"));
        remainIdd = cursor.getInt(cursor.getColumnIndex("remain_idd"));
        remainLocal = cursor.getInt(cursor.getColumnIndex("remain_local"));
        btBox = cursor.getInt(cursor.getColumnIndex("bt_box"));
        price = cursor.getInt(cursor.getColumnIndex("price"));
    }
    
    public void setJsonValue(JSONObject jsonP, JSONObject jsonS)
    {
        subOrderId = jsonP.optString("suborderid");

        tripId = jsonS.optString("tripid");
        user = jsonS.optString("username");
        begin = jsonS.optString("begin_date");
        end = jsonS.optString("end_date");
        mcc = jsonS.optString("mcc");
        mnc = jsonS.optString("mnc");
        simType = jsonS.optInt("simcardtype");
        simSize = jsonS.optInt("simcardsize");
        crbtType = jsonS.optInt("crbttype");
        days = jsonS.optInt("crbtduration");
        data = jsonS.optInt("package_data");
        localVoice = jsonS.optInt("local_voice");
        iddVoice = jsonS.optInt("idd_voice");
        remainData = jsonS.optInt("remaindata");
        remainIdd = jsonS.optInt("remainvoice_idd");
        remainLocal = jsonS.optInt("remainvoice_local");
        btBox = jsonS.optInt("bt_buyflag");
        price = jsonS.optInt("packageprice");
    }
}
