package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.nrs.Engine;
import com.travelrely.model.Profile;

public class ProfileDBHelper
{
    public static String createTable()
    {
        String table = "CREATE TABLE if not exists profile ("
                + "id integer primary key autoincrement," + "mcc text,"
                + "mnc text," + "glms_loc text," + "smc_loc text,"
                + "gpns_loc text," + "ccs_loc text," + "mgw_number1 text,"
                + "mgw_number2 text," + "note_create_time long,"
                + "is_homing text," + "text_2 text," + "text_3 text,"
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

    private static ProfileDBHelper profileDBHelper;

    private ProfileDBHelper()
    {

    }

    public static ProfileDBHelper getInstance()
    {
        if (profileDBHelper == null)
        {
            profileDBHelper = new ProfileDBHelper();
        }
        return profileDBHelper;
    }

    public long insert(Profile profile)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        long raw = db.insert(Profile.table_name, null,
                profile.getValues());
        UserDbManager.getInstance().closeDb();
        return raw;
    }

    public void insertOrUpdate(Profile profile)
    {
        Profile dataBaseProfile = findByMccAndMnc(profile.getMcc(),
                profile.getMnc(), profile.getIs_homing());
        if (dataBaseProfile != null)
        {
            // do nothing
            // if (update(dataBaseProfile) > 0) {
            // LOGManager.d(Profile.table_name + ":update");
            // }
        }
        else
        {
            long d = insert(profile);
            if (d > -1)
            {
                // LOGManager.d(Profile.table_name + ":insert");
            }
        }
    }

    public int update(Profile dataBaseProfile)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        int raw = db.update(Profile.table_name,
                dataBaseProfile.getValues(), "id=?",
                new String[] { String.valueOf(dataBaseProfile.getId()) });
        UserDbManager.getInstance().closeDb();

        return raw;
    }

    public Profile findByMccAndMnc(String mcc, String mnc, String is_homing)
    {
        String sql = "select * from " + Profile.table_name
                + " where mcc=? and mnc=? and is_homing=?";
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        Cursor c = db.rawQuery(sql, new String[] { mcc, mnc,
                is_homing });

        if (c != null)
        {
            if (c.moveToNext())
            {
                Profile profile = new Profile();
                profile.setValue(c);

                c.close();
                UserDbManager.getInstance().closeDb();
                return profile;
            }
            c.close();
        }
        UserDbManager.getInstance().closeDb();

        return null;
    }

    public List<Profile> query(String is_homing)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        Cursor cursor = db.query(Profile.table_name, null,
                "is_homing=?", new String[] { is_homing }, null, null, null);

        List<Profile> list = new ArrayList<Profile>();
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                Profile profile = new Profile();
                profile.setValue(cursor);

                list.add(profile);
            }
            cursor.close();
        }
        UserDbManager.getInstance().closeDb();

        return list;
    }
    
    public synchronized Profile selsctMcc(String type, String value) {

        Profile result = null;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        String sql = "select * from " + Profile.table_name + " where " + value + "=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                type
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    result = new Profile();
                    result.setValue(c);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }
}
