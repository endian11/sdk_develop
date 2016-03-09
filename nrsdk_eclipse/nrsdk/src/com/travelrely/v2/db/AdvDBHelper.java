package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.model.Adv;

public class AdvDBHelper
{
    public static final String tableName = "adv";

    public static String createTable()
    {
        String table = "CREATE TABLE if not exists " + tableName + " ("
                + "id integer primary key autoincrement," + "name text,"
                + "big_adv_pic text," + "small_adv_pic text,"
                + "web_site text," + "note_create_time long,"
                + "big_size text," + "small_size text," + "text_3 text,"
                + "text_4 text," + "text_5 text," + "text_6 text,"
                + "int_1 integer," + "int_2 integer," + "int_3 integer,"
                + "int_4 integer," + "int_5 integer," + "int_6 integer,"
                + "long_1 long," + "long_2 long," + "long_3 long,"
                + "long_4 long," + "varchar_1 varchar," + "varchar_2 varchar,"
                + "varchar_3 varchar," + "varchar_4 varchar,"
                + "varchar_5 varchar," + "varchar_6 varchar,"
                + "varchar_7 varchar" + ")";
        return table;
    }

    private static AdvDBHelper advDBHelper;

    private AdvDBHelper()
    {

    }

    public static AdvDBHelper getInstance()
    {
        if (advDBHelper == null)
        {
            advDBHelper = new AdvDBHelper();
        }
        return advDBHelper;
    }
    
    public void insertAll(List<Adv> advs)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        
        db.beginTransaction();
        db.execSQL("drop table if EXISTS " + tableName);
        db.execSQL(createTable());

        for (Adv adv : advs)
        {
            db.insert(tableName, null, adv.getValues());
        }
        
        db.setTransactionSuccessful();
        db.endTransaction();
        
        ComDbManager.getInstance().closeDb();
    }

    public long insert(Adv adv)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        long raw = db.insert(tableName, null, adv.getValues());
        ComDbManager.getInstance().closeDb();
        return raw;
    }

    public void insertOrUpdate(Adv adv)
    {
        Adv dataBaseAdv = findBySmallAdvPic(adv.getSmall_adv_pic());
        if (dataBaseAdv != null)
        {
            if (update(dataBaseAdv) > 0)
            {
                // LOGManager.d(Adv.table_name + ":update");
            }
        }
        else
        {
            long d = insert(adv);
            if (d > -1)
            {
                // LOGManager.d(Adv.table_name + ":insert");
            }
        }
    }

    public int update(Adv dataBaseAdv)
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        int raw = db.update(tableName,
                dataBaseAdv.getValues(), "id=?",
                new String[] { String.valueOf(dataBaseAdv.getId()) });
        ComDbManager.getInstance().closeDb();

        return raw;
    }

    public Adv findBySmallAdvPic(String small_adv_pic)
    {
        String sql = "select * from " + tableName
                + " where small_adv_pic=?";
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        Cursor c = db.rawQuery(sql, new String[] { small_adv_pic });

        if (c != null)
        {
            if (c.moveToNext())
            {
                Adv adv = new Adv();
                adv.setValue(c);

                c.close();
                ComDbManager.getInstance().closeDb();
                return adv;
            }
            c.close();
        }
        ComDbManager.getInstance().closeDb();

        return null;
    }

    public List<Adv> query()
    {
        SQLiteDatabase db = ComDbManager.getInstance().openDb();
        Cursor cursor = db.query(tableName, null, null, null,
                null, null, null);

        List<Adv> list = new ArrayList<Adv>();
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                Adv adv = new Adv();
                adv.setValue(cursor);

                list.add(adv);
            }
            cursor.close();
        }
        ComDbManager.getInstance().closeDb();
        return list;
    }
}
