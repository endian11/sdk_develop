
package com.travelrely.v2.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.nrs.Engine;
import com.travelrely.v2.model.LocationModel;
import com.travelrely.v2.model.ReceptionInfo;

public class ReceptionInfoDBHelper {

    /**
     * 签到DB
     */
    static String tableNnam = "reception_info";

    public static String createTable() {

        String table = "CREATE TABLE if not exists " + tableNnam + " ("
                + "id integer primary key autoincrement," + "group_id text," + "from_name text,"
                + "nick_name text," + "type integer," + "context text," + "longitude text," + "latitude text" + ")";
        return table;
    }

    private static ReceptionInfoDBHelper receptionInfoDBHelper;

    public static ReceptionInfoDBHelper getInstance() {

        if (receptionInfoDBHelper == null) {
            receptionInfoDBHelper = new ReceptionInfoDBHelper();
        }
        return receptionInfoDBHelper;
    }

    public long insert(ReceptionInfo rInfo) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        long raw = sqLiteDatabase.insert(tableNnam, null, rInfo.getValues());
        sqLiteDatabase.close();
        return raw;
    }

    public synchronized boolean getReception(String userName, String msgId) {

        boolean isReception = false;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam + " where from_name=? and context=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                userName, msgId
        });
        
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    ReceptionInfo rInfo = new ReceptionInfo();
                    rInfo.setValue(c);
                    if (rInfo.getType() == 1) {
                        isReception = true;
                        return isReception;
                    }
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return isReception;
    }
    
    public synchronized boolean getReceptionMsgId(String from, String msgId) {

        boolean isReception = false;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam + " where from_name=? and context=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                from, msgId
        });
        
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    isReception = true;
                    return isReception;
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return isReception;
    }
    
    public synchronized void updateContext(String type, String  msgId, String field,String newContext) {
        ContentValues values = new ContentValues();
        values.put(field, newContext);
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        sqLiteDatabase.update(tableNnam, values,  type + "=?", new String[] {
                msgId
        });
        sqLiteDatabase.close();
    }
    
    /**
     * 用来刷新雷达响应后的数据
     * @param type
     * @param msgId
     * @param field
     * @param newContext
     */
    public synchronized void updateContextResponse(String type, String  msg, String type2, String msg2, 
            String field1, String newContext1 , String field2,String newContext2, String field3, String newContext3,
            String field4, String newContext4) {
        ContentValues values = new ContentValues();
        values.put(field1, newContext1);
        values.put(field2, newContext2);
        values.put(field3, newContext3);
        values.put(field4, newContext4);
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        sqLiteDatabase.update(tableNnam, values,  type + "=? and " + type2 + "=?", new String[] {
                msg, msg2
        });
        sqLiteDatabase.close();
    }
    
    public synchronized LocationModel selectUserLocation(String userName, String context, String type){
        
        LocationModel lModels = null;
        
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam + " where from_name=? and context=? and type=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                userName, context, type
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    lModels = new LocationModel();
                    lModels.setValue(c);
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return lModels;
    }

}
