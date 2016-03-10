package com.travelrely.app.db;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.nrs.Engine;
import com.travelrely.v2.model.UserRoamProfile;

public class UserRoamProfileDBHelper
{
    static String tableName = "userroam_profile";

    public static String createTable()
    {

        String table = "CREATE TABLE if not exists " + tableName + " ("
                + "id integer primary key autoincrement,"
                + "user_roam_version integer," + "mcc text,"
                + "start_date text," + "end_date text," + "package_data text,"
                + "package_voice_idd text," + "package_voice_local text,"
                + "remain_voice text," + "remain_voice_idd text,"
                + "remain_voice_local text" + ")";

        return table;
    }

    private static UserRoamProfileDBHelper userroamProfileDB;

    public static UserRoamProfileDBHelper getInstance()
    {
        if (userroamProfileDB == null)
        {
            userroamProfileDB = new UserRoamProfileDBHelper();
        }
        return userroamProfileDB;
    }

    public void insertAll(List<UserRoamProfile> pkgs)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();

        db.beginTransaction();
        db.execSQL("drop table if EXISTS " + tableName);
        db.execSQL(createTable());

        for (UserRoamProfile pkg : pkgs)
        {
            db.insert(tableName, null, pkg.getContentValues());
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        UserDbManager.getInstance().closeDb();
    }

    public synchronized long insert(UserRoamProfile tripList)
    {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getWritableDatabase();
        long row = sqLiteDatabase.insert(tableName, null,
                tripList.getContentValues());
        sqLiteDatabase.close();
        return row;
    }

    /**
     * 因为TripList功能需求不完善，只用到了version，所以暂时先return tripList 后续需优化成return
     * ArrayList<TripList>
     * 
     * @return
     */
    public synchronized UserRoamProfile getTripList()
    {

        UserRoamProfile result = null;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getReadableDatabase();
        String sql = "select * from " + tableName;
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {});
        if (c != null)
        {
            try
            {
                while (c.moveToNext())
                {
                    result = new UserRoamProfile();
                    result.setCursorValue(c);
                }
            }
            catch (Exception e)
            {
            }
            finally
            {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }

    public synchronized boolean delete()
    {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getWritableDatabase();

        int row = sqLiteDatabase.delete(tableName, null, new String[] {});
        sqLiteDatabase.close();
        if (row > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public synchronized UserRoamProfile selsctData(String type, String value)
    {

        UserRoamProfile result = null;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getReadableDatabase();
        String sql = "select * from " + tableName + " where " + value + "=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] { type });
        if (c != null)
        {
            try
            {
                while (c.moveToNext())
                {
                    result = new UserRoamProfile();
                    result.setCursorValue(c);
                }
            }
            catch (Exception e)
            {
            }
            finally
            {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }

}
