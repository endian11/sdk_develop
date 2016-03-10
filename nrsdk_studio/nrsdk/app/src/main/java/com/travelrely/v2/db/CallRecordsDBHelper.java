package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.util.LOGManager;
import com.travelrely.model.CallRecord;

public class CallRecordsDBHelper
{
    public final static String TABLE_NAME = "call_record";

    public static String createTable()
    {
        String table = "CREATE TABLE if not exists "
                + TABLE_NAME + " (" + "id integer primary key autoincrement,"
                + "name varchar," + "number varchar,"
                + "duration long," + "type integer," + "current_time long,"
                + "contact_id integer," + "numberlabel integer" + ")";
        
        return table;
    }
    
    
    private static CallRecordsDBHelper instance;

    private CallRecordsDBHelper()
    {

    }

    public static CallRecordsDBHelper getInstance()
    {
        if (instance == null)
        {
            instance = new CallRecordsDBHelper();
        }
        return instance;
    }

    public long insert(CallRecord callRecord)
    {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(
                Engine.getInstance().getContext(), Engine.getInstance()
                        .getUserName());
        SQLiteDatabase sqlite = travelrelyOpenHelper.getWritableDatabase();
        long id = sqlite.insert(TABLE_NAME, null,
                callRecord.generateValue());
        LOGManager.d("插入ID = " + callRecord.getId());
        sqlite.close();
        return id;
    }

    public int update(CallRecord callRecord)
    {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(
                Engine.getInstance().getContext(), Engine.getInstance()
                        .getUserName());
        SQLiteDatabase sqlite = travelrelyOpenHelper.getWritableDatabase();
        int row = sqlite.update(TABLE_NAME,
                callRecord.generateValue(), "id=?",
                new String[] { String.valueOf(callRecord.getId()) });
        sqlite.close();
        
        LOGManager.d("更新ID = " + callRecord.getId());
        
        return row;
    }
    
    public synchronized boolean delete(String key, String callRecord) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(TABLE_NAME, key + "=?", new String[] {
                callRecord
        });
        sqLiteDatabase.close();
        if (row > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<CallRecord> query(String coum)
    {
        List<CallRecord> callRecords = new ArrayList<CallRecord>();
        
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + TABLE_NAME + coum + " order by id desc";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {});
        
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    CallRecord callRecord = new CallRecord();
                    callRecord.setLocalValues(c);
                    callRecords.add(callRecord);
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        
        return callRecords;
    }

//    public List<CallRecord> genreateCallrecords()
//    {
//        List<CallRecord> list = new ArrayList<CallRecord>();
//        ContentResolver resolver = Engine.getInstance().getContext()
//                .getContentResolver();
//        Cursor c = resolver.query(CallLog.Calls.CONTENT_URI, null, null,
//                new String[] {}, null);
//        while (c.moveToNext())
//        {
//            CallRecord callRecord = new CallRecord();
//            callRecord.setValues(c);
//
//            list.add(callRecord);
//        }
//        LOGManager.d(c.getCount() + "");
//        c.close();
//        return list;
//    }
    
    public List<CallRecord> getNumCallRecords(String num, String type)
    {
        
        if(num != null){
            List<CallRecord> list = new ArrayList<CallRecord>();
            
            UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(
                    Engine.getInstance().getContext(), Engine.getInstance()
                            .getUserName());
            SQLiteDatabase sqlite = travelrelyOpenHelper.getReadableDatabase();

            String sql = "select * from " + TABLE_NAME + " where number=? " + type;
            
            Cursor c = sqlite.rawQuery(sql, new String[] {
                    num
                    });
            
            while (c.moveToNext())
            {
                CallRecord callRecord = new CallRecord();
                callRecord.setLocalValues(c);
                list.add(callRecord);
            }
            LOGManager.d(c.getCount() + "");
            c.close();
            return list;
        }
        return null;
        
    }
    
    public CallRecord getNumCalLog(String num)
    {
        
        if(num != null){
            CallRecord callRecord = null;
            UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(
                    Engine.getInstance().getContext(), Engine.getInstance()
                            .getUserName());
            SQLiteDatabase sqlite = travelrelyOpenHelper.getReadableDatabase();

            String sql = "select * from " + TABLE_NAME + " where number=? order by current_time desc";
            
            Cursor c = sqlite.rawQuery(sql, new String[] {
                    num
                    });
            
            while (c.moveToNext())
            {
                callRecord = new CallRecord();
                callRecord.setLocalValues(c);
            }
            LOGManager.d(c.getCount() + "");
            c.close();
            return callRecord;
        }
        return null;
        
    }
}
