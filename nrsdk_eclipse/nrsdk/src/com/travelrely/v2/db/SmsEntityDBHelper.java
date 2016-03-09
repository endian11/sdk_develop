package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.Engine;
import com.travelrely.v2.model.SmsEntity;

/**
 * @author zhangyao
 * @version 2014年7月14日下午5:58:13
 */

public class SmsEntityDBHelper
{
    static String tableName = "message_sms";

    public static String createTable()
    {
        String table = "CREATE TABLE if not exists " + tableName + " ("
                + "id integer primary key autoincrement," + "thread_id text,"
                + "address text," + "person text," + "nick_name text,"
                + "date datetime," + "protocol integer," + "read integer,"
                + "status integer," + "type integer,"
                + "reply_path_present text," + "subject text," + "body text,"
                + "body_type integer," + "service_center text,"
                + "msg_type integer," + "append_int_1 integer,"
                + "append_int_2 integer," + "append_int_3 integer,"
                + "append_int_4 integer," + "append_int_5 integer,"
                + "append_long_1 long," + "append_long_2 long,"
                + "append_long_3 long," + "append_long_4 long,"
                + "append_long_5 long," + "append_text_1 text,"
                + "append_text_2 text," + "append_text_3 text,"
                + "append_text_4 text," + "append_text_5 text,"
                + "append_text_6 text," + "append_text_7 text,"
                + "append_text_8 text," + "append_text_9 text,"
                + "append_text_10 text," + "width_user text,"
                + "user_name text," + "note_create_time long" + ")";
        return table;
    }

    private static SmsEntityDBHelper smsMessageDBHelper;

    public static SmsEntityDBHelper getInstance()
    {
        if (smsMessageDBHelper == null)
        {
            smsMessageDBHelper = new SmsEntityDBHelper();
        }
        return smsMessageDBHelper;
    }

    public synchronized long addMessageSms(SmsEntity tSms)
    {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getWritableDatabase();
        long row = sqLiteDatabase
                .insert(tableName, null, tSms.getContentValues());
        sqLiteDatabase.close();
        return row;

    }

    public synchronized List<SmsEntity> getMessagesSms(String from,
            int count)
    {

        List<SmsEntity> result = new ArrayList<SmsEntity>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getReadableDatabase();
        String sql = "select * from " + tableName
                + " where address=? order by id desc limit " + count;
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] { from });
        if (c != null)
        {
            try
            {
                while (c.moveToNext())
                {
                    SmsEntity message = new SmsEntity();
                    message.setCursorValues(c);
                    result.add(message);
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

    public synchronized List<SmsEntity> getMessagesLastSms()
    {

        List<SmsEntity> result = new ArrayList<SmsEntity>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getReadableDatabase();

        String sql = "select * from " + tableName
                + " group by address order by date desc";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {});
        if (c != null)
        {
            try
            {
                while (c.moveToNext())
                {
                    SmsEntity message = new SmsEntity();
                    message.setCursorValues(c);
                    result.add(message);
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

    public synchronized boolean deleteMessageSms(String key, String value)
    {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getWritableDatabase();

        int row = sqLiteDatabase.delete(tableName, key + "=?",
                new String[] { value });
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

    public synchronized SmsEntity getMessagesSms(String key, String value)
    {

        SmsEntity result = null;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getReadableDatabase();
        String sql = "select * from " + tableName + " where " + key + "=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] { value });
        if (c != null)
        {
            try
            {
                while (c.moveToNext())
                {
                    result = new SmsEntity();
                    result.setCursorValues(c);
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
    
    public synchronized SmsEntity getSms(long id)
    {
        SmsEntity result = null;

        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        String sql = "select * from " + tableName + " where id=?";
        Cursor c = db.rawQuery(sql, new String[] { String.valueOf(id) });
        if (c == null)
        {
            UserDbManager.getInstance().closeDb();
            return null;
        }
        
        while (c.moveToNext())
        {
            result = new SmsEntity();
            result.setCursorValues(c);
        }

        c.close();
        UserDbManager.getInstance().closeDb();

        return result;
    }
    
    public synchronized int getUnReadCount(String addr)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        String sql = "select * from " + tableName + " where address=? and read=?";
        Cursor c = db.rawQuery(sql, new String[] { addr, String.valueOf(SmsEntity.STATUS_UNREAD) });
        if (c == null)
        {
            UserDbManager.getInstance().closeDb();
            return 0;
        }
        
        int count = c.getCount();

        c.close();
        UserDbManager.getInstance().closeDb();

        return count;
    }
    
    public synchronized int getUnReadCount()
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        String sql = "select * from " + tableName + " where read=?";
        Cursor c = db.rawQuery(sql, new String[] {String.valueOf(SmsEntity.STATUS_UNREAD) });
        if (c == null)
        {
            UserDbManager.getInstance().closeDb();
            return 0;
        }
        
        int count = c.getCount();

        c.close();
        UserDbManager.getInstance().closeDb();

        return count;
    }

    public synchronized boolean update(SmsEntity message)
    {
        ContentValues content = message.getContentValues();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine
                .getInstance().getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getWritableDatabase();
        int row = sqLiteDatabase.update(tableName, content, "id=?",
                new String[] { String.valueOf(message.getId()) });
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

}
