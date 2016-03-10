package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.v2.model.RoamProfile;

public class ProfileIpDialDBHelper
{
    static String tableName = "roam_profile";
    
    public static String createTable()
    {
        // day 表示它当前所在的是哪一天的毫秒数，note_time是它写记录的时间

        String table = "CREATE TABLE if not exists " + tableName + " ("
                + "id integer primary key autoincrement,"
                + "mcc text,"
                + "mnc text,"
                + "ip_dial_number text,"
                + "is_homing text,"
                + "text_2 text," + "text_3 text," + "text_4 text,"
                + "text_5 text," + "text_6 text," + "int_1 integer,"
                + "int_2 integer," + "int_3 integer," + "int_4 integer,"
                + "int_5 integer," + "int_6 integer," + "long_1 long,"
                + "long_2 long," + "long_3 long," + "long_4 long,"
                + "varchar_1 varchar," + "varchar_2 varchar,"
                + "varchar_3 varchar," + "varchar_4 varchar,"
                + "varchar_5 varchar," + "varchar_6 varchar,"
                + "varchar_7 varchar" + "note_create_time long" + ")";
        return table;
    }

    private static ProfileIpDialDBHelper profileIpDialDBHelper;

    private ProfileIpDialDBHelper()
    {

    }

    public static ProfileIpDialDBHelper getInstance()
    {
        if (profileIpDialDBHelper == null)
        {
            profileIpDialDBHelper = new ProfileIpDialDBHelper();
        }
        return profileIpDialDBHelper;
    }
    
    public void insertAll(List<RoamProfile> pkgs)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        db.execSQL("drop table if EXISTS " + tableName);
        db.execSQL(createTable());

        for (RoamProfile pkg : pkgs)
        {
            db.insert(tableName, null, pkg.getContentValues());
        }
        
        UserDbManager.getInstance().closeDb();
    }

    public long insert(RoamProfile profileIpDial)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        long raw = db.insert(tableName, null,
                profileIpDial.getContentValues());
        UserDbManager.getInstance().closeDb();
        return raw;
    }

    public void insertOrUpdate(RoamProfile profileIpDial)
    {
        RoamProfile dataBaseProfile = find(profileIpDial.getMcc(),
                profileIpDial.getMnc(), profileIpDial.getIp_dial_number(),
                profileIpDial.getIs_homing());
        if (dataBaseProfile != null)
        {
            // do nothing

            // if (update(dataBaseProfile) > 0) {
            // LOGManager.d(ProfileIpDial.table_name + ":update");
            // }
        }
        else
        {
            long d = insert(profileIpDial);
            if (d > -1)
            {
                // LOGManager.d(ProfileIpDial.table_name + ":insert");
            }
        }
    }

    public int update(RoamProfile dataBaseProfile)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        int raw = db.update(tableName,
                dataBaseProfile.getContentValues(), "id=?",
                new String[] { String.valueOf(dataBaseProfile.getId()) });
        UserDbManager.getInstance().closeDb();

        return raw;
    }

    public RoamProfile find(String mcc, String mnc, String ip_dial_number,
            String is_homing)
    {
        String sql = "select * from " + tableName
                + " where mcc=? and mnc=? and ip_dial_number=? and is_homing=?";
        
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        Cursor c = db.rawQuery(sql, new String[] { mcc, mnc,
                ip_dial_number, is_homing });

        if (c != null)
        {
            if (c.moveToNext())
            {
                RoamProfile profileIpDial = new RoamProfile();
                profileIpDial.setCursorValue(c);

                c.close();
                UserDbManager.getInstance().closeDb();
                return profileIpDial;
            }
            c.close();

        }
        UserDbManager.getInstance().closeDb();

        return null;
    }

    public List<RoamProfile> query(String mcc, String mnc, String is_homing)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        Cursor cursor = db.query(tableName, null,
                "mcc=? and mnc=? and is_homing=?", new String[] { mcc, mnc,
                        is_homing }, null, null, null);

        List<RoamProfile> list = new ArrayList<RoamProfile>();
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {

                RoamProfile profileIpDial = new RoamProfile();
                profileIpDial.setCursorValue(cursor);

                list.add(profileIpDial);
            }
            cursor.close();
        }
        UserDbManager.getInstance().closeDb();

        return list;
    }
}
