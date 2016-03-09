package com.travelrely.v2.model;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class UserRoamProfile
{
    int user_roam_version;

    String mcc;

    String start_date;

    String end_date;

    String package_data;

    String package_voice_idd;

    String package_voice_local;

    String remain_voice;

    String remain_voice_idd;

    String remain_voice_local;

    public String getMcc()
    {
        return mcc;
    }

    public void setMcc(String mcc)
    {
        this.mcc = mcc;
    }

    public String getStart()
    {
        return start_date;
    }

    public void setStart(String start_date)
    {
        this.start_date = start_date;
    }

    public String getEnd()
    {
        return end_date;
    }

    public void setEnd(String end_date)
    {
        this.end_date = end_date;
    }

    public String getPackage_data()
    {
        return package_data;
    }

    public void setPackage_data(String package_data)
    {
        this.package_data = package_data;
    }

    public String getPackage_voice_idd()
    {
        return package_voice_idd;
    }

    public void setPackage_voice_idd(String package_voice_idd)
    {
        this.package_voice_idd = package_voice_idd;
    }

    public String getPackage_voice_local()
    {
        return package_voice_local;
    }

    public void setPackage_voice_local(String package_voice_local)
    {
        this.package_voice_local = package_voice_local;
    }

    public String getRemain_voice()
    {
        return remain_voice;
    }

    public void setRemain_voice(String remain_voice)
    {
        this.remain_voice = remain_voice;
    }

    public String getRemain_voice_idd()
    {
        return remain_voice_idd;
    }

    public void setRemain_voice_idd(String remain_voice_idd)
    {
        this.remain_voice_idd = remain_voice_idd;
    }

    public String getRemain_voice_local()
    {
        return remain_voice_local;
    }

    public void setRemain_voice_local(String remain_voice_local)
    {
        this.remain_voice_local = remain_voice_local;
    }

    public int getUser_roam_version()
    {
        return user_roam_version;
    }

    public void setUser_roam_version(int user_roam_version)
    {
        this.user_roam_version = user_roam_version;
    }

    public void setJsonValue(JSONObject jsonObject)
    {
        mcc = jsonObject.optString("mcc");
        start_date = jsonObject.optString("start_date");
        end_date = jsonObject.optString("end_date");
        package_data = jsonObject.optString("package_data");
        package_voice_idd = jsonObject.optString("package_voice_idd");
        package_voice_local = jsonObject.optString("package_voice_local");
        remain_voice = jsonObject.optString("remain_voice");
        remain_voice_idd = jsonObject.optString("remain_voice_idd");
        remain_voice_local = jsonObject.optString("remain_voice_local");
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_roam_version", user_roam_version);
        contentValues.put("mcc", mcc);
        contentValues.put("start_date", start_date);
        contentValues.put("end_date", end_date);
        contentValues.put("package_data", package_data);
        contentValues.put("package_voice_idd", package_voice_idd);
        contentValues.put("package_voice_local", package_voice_local);
        contentValues.put("remain_voice", remain_voice);
        contentValues.put("remain_voice_idd", remain_voice_idd);
        contentValues.put("remain_voice_local", remain_voice_local);

        return contentValues;
    }

    public void setCursorValue(Cursor c)
    {
        user_roam_version = c.getInt(c.getColumnIndex("user_roam_version"));
        mcc = c.getString(c.getColumnIndex("mcc"));
        start_date = c.getString(c.getColumnIndex("start_date"));
        end_date = c.getString(c.getColumnIndex("end_date"));
        package_data = c.getString(c.getColumnIndex("package_data"));
        package_voice_idd = c.getString(c.getColumnIndex("package_voice_idd"));
        package_voice_local = c.getString(c
                .getColumnIndex("package_voice_local"));
        remain_voice = c.getString(c.getColumnIndex("remain_voice"));
        remain_voice_idd = c.getString(c.getColumnIndex("remain_voice_idd"));
        remain_voice_local = c
                .getString(c.getColumnIndex("remain_voice_local"));
    }
}
