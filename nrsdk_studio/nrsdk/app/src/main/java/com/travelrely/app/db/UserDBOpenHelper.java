package com.travelrely.app.db;

import com.travelrely.core.util.LOGManager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBOpenHelper extends SQLiteOpenHelper
{
    public static final String dataBaseName = "travelrely_v2";

    public UserDBOpenHelper(Context context, String userName)
    {
        super(context, dataBaseName + userName, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        long time = System.currentTimeMillis();
        String sql = ChinaAdmin.hat_areaTable();
        // LOGManager.d("hat_areaTable" + sql);
        try {
			sqLiteDatabase.beginTransaction();
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
			sqLiteDatabase.execSQL(ContactDBHelper.createTable());
			sqLiteDatabase.execSQL(ContactDBHelper.createTagNumTable());
			sqLiteDatabase.execSQL(TravelrelyMessageDBHelper.createTable());
			sqLiteDatabase.execSQL(ProfileDBHelper.createTable());
			sqLiteDatabase.execSQL(ProfileIpDialDBHelper.createTable());
			sqLiteDatabase.execSQL(AdvDBHelper.createTable());
			sqLiteDatabase.execSQL(CountryInfoDBHelper.createTable());
			sqLiteDatabase.execSQL(GroupContactDBHelper.createTable());
			sqLiteDatabase.execSQL(GroupDBHelper.createTable());
			sqLiteDatabase.execSQL(TripInfoDBHelper.createTable_tripInfo());
			sqLiteDatabase.execSQL(TripInfoDBHelper.createTable_daylist());
			sqLiteDatabase.execSQL(TripInfoDBHelper.createTable_activitylist());
			sqLiteDatabase.execSQL(MyCollectionDBHelper.createTable());
			sqLiteDatabase.execSQL(ReceptionInfoDBHelper.createTable());
			sqLiteDatabase.execSQL(SmsEntityDBHelper.createTable());
			sqLiteDatabase.execSQL(PackageDbHelper.createTable());
			sqLiteDatabase.execSQL(ExpPriceDbHelper.createTable());
			sqLiteDatabase.execSQL(CallRecordsDBHelper.createTable());
			sqLiteDatabase.execSQL(UserRoamProfileDBHelper.createTable());
			sqLiteDatabase.execSQL(OrderDbHelper.createOrderTable());
			sqLiteDatabase.execSQL(OrderDbHelper.createTripTable());
			sqLiteDatabase.execSQL(SmsSegDbHelper.createTable());
			sqLiteDatabase.execSQL(ServerIpDbHelper.createTable());

			sqLiteDatabase.setTransactionSuccessful();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGManager.e("userDB事务操作异常");
		}finally{
			sqLiteDatabase.endTransaction();
		}

        time = System.currentTimeMillis() - time;
        LOGManager.d("init area user time" + time);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
            int newVersion)
    {
        LOGManager.d("############oldVersion：" + oldVersion + "\n" 
    + "############newVersion:" + newVersion);
        
        if (oldVersion == 1)
        {
            sqLiteDatabase.beginTransaction();
            
            sqLiteDatabase.execSQL(SmsEntityDBHelper.createTable());
            sqLiteDatabase.execSQL(PackageDbHelper.createTable());
            sqLiteDatabase.execSQL(ExpPriceDbHelper.createTable());
            sqLiteDatabase.execSQL(AdvDBHelper.createTable());
            sqLiteDatabase.execSQL("drop table if EXISTS contact");
            sqLiteDatabase.execSQL("drop table if EXISTS tag_num");
            sqLiteDatabase.execSQL(ContactDBHelper.createTable());
            sqLiteDatabase.execSQL(ContactDBHelper.createTagNumTable());
            sqLiteDatabase.execSQL(UserRoamProfileDBHelper.createTable());
            sqLiteDatabase.execSQL(OrderDbHelper.createOrderTable());
            sqLiteDatabase.execSQL(OrderDbHelper.createTripTable());
            sqLiteDatabase.execSQL(SmsSegDbHelper.createTable());
            sqLiteDatabase.execSQL(ServerIpDbHelper.createTable());
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
        
        if (oldVersion == 2)
        {
            sqLiteDatabase.beginTransaction();
            
            sqLiteDatabase.execSQL(OrderDbHelper.createOrderTable());
            sqLiteDatabase.execSQL(OrderDbHelper.createTripTable());
            sqLiteDatabase.execSQL(UserRoamProfileDBHelper.createTable());
            sqLiteDatabase.execSQL(SmsSegDbHelper.createTable());
            sqLiteDatabase.execSQL(ServerIpDbHelper.createTable());
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
        
        if(newVersion == 4)
        {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(CallRecordsDBHelper.createTable());
            sqLiteDatabase.execSQL(TravelrelyMessageDBHelper.createTable());
            sqLiteDatabase.execSQL(SmsSegDbHelper.createTable());
            sqLiteDatabase.execSQL(ServerIpDbHelper.createTable());
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
    }
}
