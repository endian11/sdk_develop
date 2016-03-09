
package com.travelrely.v2.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.Engine;
import com.travelrely.v2.response.GroupMsg;
import com.travelrely.v2.response.GetNewGroup.Data;

/**
 * 群成员信息表
 * 
 * @author Travelrely
 */
public class GroupContactDBHelper {

    public static final String tableNnam = "group_contact";

    public static String createTable() {

        String table = "CREATE TABLE if not exists " + tableNnam + " ("
                + "id integer primary key autoincrement," + "group_id text,"
                + "number text," + "nick_name text,"  + "type integer," 
                + "head_portrait text"+ ")";
        return table;
    }

    private static GroupContactDBHelper groupContactHelper;

    private GroupContactDBHelper() {

    }

    public static GroupContactDBHelper getInstance() {
        if (groupContactHelper == null) {
            groupContactHelper = new GroupContactDBHelper();
        }
        return groupContactHelper;
    }

    public long insert(GroupMsg groupMsg) {
        long lRowId;
        UserDBOpenHelper travelrelyOpenHelper;
        SQLiteDatabase sqLiteDatabase;

        travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance().getContext(), Engine
                .getInstance().getUserName());
        sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        lRowId = sqLiteDatabase.insert(tableNnam, null, groupMsg.setValues());
        sqLiteDatabase.close();

        return lRowId;
    }
    
    /**
     * @param group_id
     * @return
     */
    public ArrayList<GroupMsg> getGroupMsg(String group_id) {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("select * from " + tableNnam + " where group_id=?", new String[] {
                group_id
        });
        ArrayList<GroupMsg> list = new ArrayList<GroupMsg>();
        GroupMsg gMsg = null;
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    gMsg = new GroupMsg();
                    gMsg.getValue(c);
                    list.add(gMsg);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return list;
    }
    
    public synchronized int delGroupItem(String groupName, String num) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        int raw = sqLiteDatabase.delete(tableNnam, "group_id=? and number=?", new String[] {
                groupName, num
        });
        sqLiteDatabase.close();
        return raw;
    }

}
