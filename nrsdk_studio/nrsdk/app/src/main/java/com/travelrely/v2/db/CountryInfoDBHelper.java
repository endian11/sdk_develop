package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.model.CountryInfo;

public class CountryInfoDBHelper
{
    public static String createTable()
    {
        String table = "CREATE TABLE if not exists country_info ("
                + "id integer primary key autoincrement," + "cc text,"
                + "country_name text," + "country_short_name text,"
                + "mcc text," + "mnc text," + "carrier_name text,"
                + "national_flag text," + "website_url text,"
                + "packagetype integer," + "packageDesc text,"
                + "text_1 text," + "text_2 text,"
                + "text_3 text," + "text_4 text,"
                + "text_5 text," + "text_6 text," + "int_1 integer,"
                + "int_2 integer," + "int_3 integer," + "int_4 integer,"
                + "int_5 integer," + "int_6 integer," + "long_1 long,"
                + "long_2 long," + "long_3 long," + "long_4 long,"
                + "varchar_1 varchar," + "varchar_2 varchar,"
                + "varchar_3 varchar," + "varchar_4 varchar,"
                + "varchar_5 varchar," + "varchar_6 varchar,"
                + "varchar_7 varchar" + ")";
        return table;
    }

    private static CountryInfoDBHelper countryInfoDBHelper;

    private CountryInfoDBHelper()
    {

    }

    public static CountryInfoDBHelper getInstance()
    {
        if (countryInfoDBHelper == null)
        {
            countryInfoDBHelper = new CountryInfoDBHelper();
        }
        return countryInfoDBHelper;
    }
    
    public void insertAll(List<CountryInfo> cs)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        
        db.execSQL("drop table if EXISTS " + CountryInfo.table_name);
        db.execSQL(createTable());

        for (CountryInfo c : cs)
        {
            db.insert(CountryInfo.table_name, null, c.getValues());
        }
        
        ComDbManager.getInstance().closeDb();
    }
    
    public long insert(CountryInfo profile)
    {
        long lRowId;
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        lRowId = db.insert(CountryInfo.table_name, null,
                profile.getValues());
        ComDbManager.getInstance().closeDb();

        return lRowId;
    }

    public int update(CountryInfo countryInfo)
    {
        int iRowNum;
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        iRowNum = db.update(CountryInfo.table_name,
                countryInfo.getValues(), "id=?",
                new String[] { String.valueOf(countryInfo.getId()) });
        ComDbManager.getInstance().closeDb();

        return iRowNum;
    }

    public CountryInfo findByMccAndMnc(String mcc, String mnc)
    {
        String sql;
        Cursor cursor;

        sql = "select * from " + CountryInfo.table_name
                + " where mcc=? and mnc=?";
        SQLiteDatabase db = ComDbManager.getInstance().openDb();

        cursor = db.rawQuery(sql, new String[] { mcc, mnc });
        if (cursor != null)
        {
            if (cursor.moveToNext())
            {
                CountryInfo profile = new CountryInfo();
                profile.setValue(cursor);

                cursor.close();
                ComDbManager.getInstance().closeDb();
                return profile;
            }

            cursor.close();
        }

        ComDbManager.getInstance().closeDb();

        return null;
    }

    public void insertOrUpdate(CountryInfo countryInfo)
    {
        CountryInfo dataBaseProfile;

        dataBaseProfile = findByMccAndMnc(countryInfo.getMcc(),
                countryInfo.getMnc());
        if (dataBaseProfile != null)
        {
            if (update(dataBaseProfile) > 0)
            {
                // LOGManager.d(Profile.table_name + ":update");
            }
        }
        else
        {
            long d = insert(countryInfo);
            if (d > -1)
            {
                // LOGManager.d(Profile.table_name + ":insert");
            }
        }
    }

    public List<CountryInfo> query()
    {
        Cursor cursor;

        CountryInfo countryInfo;
        List<CountryInfo> list;

        list = new ArrayList<CountryInfo>();

        SQLiteDatabase db = ComDbManager.getInstance().openDb();

        cursor = db.query(CountryInfo.table_name, null, null, null,
                null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                countryInfo = new CountryInfo();
                countryInfo.setValue(cursor);

                list.add(countryInfo);
            }

            cursor.close();
        }

        ComDbManager.getInstance().closeDb();

        return list;
    }
}
