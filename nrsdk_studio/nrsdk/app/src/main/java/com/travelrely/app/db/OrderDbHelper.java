package com.travelrely.app.db;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.v2.model.Package0;
import com.travelrely.v2.model.SubOrder;
import com.travelrely.v2.model.Trip;

public class OrderDbHelper
{
    static String tableOrders = "order_infos";
    static String tableTrips = "order_trips";
    
    public static String createOrderTable()
    {
        String table = "CREATE TABLE if not exists " + tableOrders + " ("
                + "id integer primary key autoincrement,"
                + "order_id text,"
                + "order_fee text,"
                + "order_exp_price text,"
                + "order_exp_barcode text,"
                + "order_discount text,"
                + "order_currency_unit integer,"
                + "order_status integer,"
                + "sub_order_id text,"
                + "sub_order_type integer,"
                + "sub_order_fee text" + ")";
        return table;
    }
    
    public static String createTripTable()
    {
        String table = "CREATE TABLE if not exists " + tableTrips + " ("
                + "id integer primary key autoincrement,"
                + "sub_order_id text,"
                + "trip_id text,"
                + "user text,"
                + "begin text,"
                + "end text,"
                + "mcc text,"
                + "mnc text,"
                + "sim_type integer,"
                + "sim_size integer,"
                + "crbt_type integer,"
                + "days integer,"
                + "data integer,"
                + "idd integer,"
                + "local integer,"
                + "remain_data integer,"
                + "remain_idd integer,"
                + "remain_local integer,"
                + "bt_box integer,"
                + "price text" + ")";
        return table;
    }
    
    private static OrderDbHelper instance;

    private OrderDbHelper()
    {
        
    }

    public static OrderDbHelper getInstance()
    {
        if (instance == null)
        {
            instance = new OrderDbHelper();
        }
        return instance;
    }
    
    public void insertAll(List<SubOrder> orders)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        db.beginTransaction();
        
        db.execSQL("drop table if EXISTS " + tableOrders);
        db.execSQL("drop table if EXISTS " + tableTrips);
        db.execSQL(createOrderTable());
        db.execSQL(createTripTable());

        for (SubOrder order : orders)
        {
            db.insert(tableOrders, null, order.getContentValues());
            for (Trip trip : order.getTrips())
            {
                db.insert(tableTrips, null, trip.getContentValues());
            }
        }
        
        db.setTransactionSuccessful();
        db.endTransaction();
        
        UserDbManager.getInstance().closeDb();
    }
    
    public Package0 query(String mcc, String mnc, String days)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        String sql = "select * from " + tableOrders
                + " where mcc=? and mnc=? and days=?";
        Cursor cursor = db.rawQuery(sql, new String[] {mcc, mnc, days});
        if (cursor == null)
        {
            UserDbManager.getInstance().closeDb();
            return null;
        }

        Package0 profile = new Package0();

        while (cursor.moveToNext())
        {
            profile.setCursorValue(cursor);
        }
        cursor.close();

        UserDbManager.getInstance().closeDb();

        return profile;
    }
}
