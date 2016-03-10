package com.travelrely.v2.db;

import com.travelrely.core.util.LOGManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ComDBOpenHelper extends SQLiteOpenHelper
{
    public static final String dataBaseName = "travelrely_app_com";

    public ComDBOpenHelper(Context context)
    {
        super(context, dataBaseName, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        long time = System.currentTimeMillis();

        sqLiteDatabase.beginTransaction();
        
        String sql = ChinaAdmin.hat_areaTable();
        // LOGManager.d("hat_areaTable" + sql);
        sqLiteDatabase.execSQL(sql);

        sql = ChinaAdmin.hat_province_table();
        // LOGManager.d("hat_province_table" + sql);
        sqLiteDatabase.execSQL(sql);

        sql = ChinaAdmin.hat_city_table();
        // LOGManager.d("hat_city_table" + sql);
        sqLiteDatabase.execSQL(sql);

        ChinaAdmin.insert_province_table(sqLiteDatabase);
        ChinaAdmin.insertArea(sqLiteDatabase);
        ChinaAdmin.insertCity(sqLiteDatabase);

        sqLiteDatabase.execSQL(AdvDBHelper.createTable());
        sqLiteDatabase.execSQL(CountryInfoDBHelper.createTable());
        sqLiteDatabase.execSQL(PackageDbHelper.createTable());
        sqLiteDatabase.execSQL(ExpPriceDbHelper.createTable());

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

        time = System.currentTimeMillis() - time;
        LOGManager.d("init ComDBOpenHelper time" + time);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
            int newVersion)
    {
        LOGManager.d("oldVersion：" + oldVersion + ", newVersion:" + newVersion);
    }
}
