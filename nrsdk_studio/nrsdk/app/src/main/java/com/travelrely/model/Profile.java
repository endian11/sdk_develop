package com.travelrely.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Profile extends CursorBean
{
    public static final String table_name = "profile";

    String mcc;

    String mnc;

    String glms_loc;

    String smc_loc;

    String gpns_loc;

    String ccs_loc;

    String mgw_number1;

    String mgw_number2;

    String is_homing;

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

    public String getGlms_loc()
    {
        return glms_loc;
    }

    public void setGlms_loc(String glms_loc)
    {
        this.glms_loc = glms_loc;
    }

    public String getSmc_loc()
    {
        return smc_loc;
    }

    public void setSmc_loc(String smc_loc)
    {
        this.smc_loc = smc_loc;
    }

    public String getGpns_loc()
    {
        return gpns_loc;
    }

    public void setGpns_loc(String gpns_loc)
    {
        this.gpns_loc = gpns_loc;
    }

    public String getCcs_loc()
    {
        return ccs_loc;
    }

    public void setCcs_loc(String ccs_loc)
    {
        this.ccs_loc = ccs_loc;
    }

    public String getMgw_number1()
    {
        return mgw_number1;
    }

    public void setMgw_number1(String mgw_number1)
    {
        this.mgw_number1 = mgw_number1;
    }

    public String getMgw_number2()
    {
        return mgw_number2;
    }

    public void setMgw_number2(String mgw_number2)
    {
        this.mgw_number2 = mgw_number2;
    }

    public String getIs_homing()
    {
        return is_homing;
    }

    public void setIs_homing(String is_homing)
    {
        this.is_homing = is_homing;
    }

    public static String getTableName()
    {
        return table_name;
    }

    public void setValue(Cursor cursor)
    {
        super.setValue(cursor);
        mcc = cursor.getString(cursor.getColumnIndex("mcc"));
        mnc = cursor.getString(cursor.getColumnIndex("mnc"));
        glms_loc = cursor.getString(cursor.getColumnIndex("glms_loc"));
        smc_loc = cursor.getString(cursor.getColumnIndex("smc_loc"));
        gpns_loc = cursor.getString(cursor.getColumnIndex("gpns_loc"));
        ccs_loc = cursor.getString(cursor.getColumnIndex("ccs_loc"));
        mgw_number1 = cursor.getString(cursor.getColumnIndex("mgw_number1"));
        mgw_number2 = cursor.getString(cursor.getColumnIndex("mgw_number2"));
    }

    public ContentValues getValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mcc", mcc);
        contentValues.put("mnc", mnc);
        contentValues.put("glms_loc", glms_loc);
        contentValues.put("smc_loc", smc_loc);
        contentValues.put("gpns_loc", gpns_loc);
        contentValues.put("ccs_loc", ccs_loc);
        contentValues.put("mgw_number1", mgw_number1);
        contentValues.put("mgw_number2", mgw_number2);
        contentValues.put("is_homing", is_homing);

        return contentValues;
    }

    public void setData(ContentValues contentValues)
    {
        setMcc(contentValues.getAsString("mcc"));
        setMnc(contentValues.getAsString("mnc"));
        setGlms_loc(contentValues.getAsString("glms_loc"));
        setSmc_loc(contentValues.getAsString("smc_loc"));
        setGpns_loc(contentValues.getAsString("gpns_loc"));
        setCcs_loc(contentValues.getAsString("ccs_loc"));
        setMgw_number1(contentValues.getAsString("mgw_number1"));
        setMgw_number2(contentValues.getAsString("mgw_number2"));
        setIs_homing(contentValues.getAsString("is_homing"));
    }
}
