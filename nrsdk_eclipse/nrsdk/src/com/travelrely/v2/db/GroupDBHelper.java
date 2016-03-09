package com.travelrely.v2.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.nrs.Engine;
import com.travelrely.v2.response.GetNewGroup;
import com.travelrely.v2.response.GroupMsg;
import com.travelrely.v2.response.GetNewGroup.Data;
import com.travelrely.v2.response.GroupList;

/**
 * 群表
 * @author Travelrely
 *
 */
public class GroupDBHelper{
    
    String tableNnam = "t_group";
    
    /*
     * group_id  群ID
     * group_name 群名字
     * number 群主号码
     * type  1为群主  2为群成员
     * group_head_portrait 群头像
     * group_head_img_path 群头像路径
     * create_contact_id 创建联系人
     */
    
    public static String createTable() {
        
        
        String table = "CREATE TABLE if not exists t_group ("
                + "id integer primary key autoincrement," + "group_id text," + "group_name text," 
                +"number text," + "type integer," + "group_head_portrait text," 
                +"group_head_img_path text,"+ "create_contact_id integer," 
                + "expireddate datetime," + "version integer" + ")";
        return table;
    }
    
    private static GroupDBHelper groupDBHelper;

    private GroupDBHelper() {

    }

    public static GroupDBHelper getInstance() {
        if (groupDBHelper == null) {
            groupDBHelper = new GroupDBHelper();
        }
        return groupDBHelper;
    }
    
    public long insert(GetNewGroup getNewGroup) {
        long lRowId;
        UserDBOpenHelper travelrelyOpenHelper;
        SQLiteDatabase sqLiteDatabase;

        travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance().getContext(), Engine
                .getInstance().getUserName());
        sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        lRowId = sqLiteDatabase.insert(tableNnam, null, getNewGroup.getData().getValues());
        sqLiteDatabase.close();

        return lRowId;
    }
    
    public long insertGroupList(GroupList groupList) {
        
        long lRowId = 0;
        if(groupList.getValues() != null){
            UserDBOpenHelper travelrelyOpenHelper;
            SQLiteDatabase sqLiteDatabase;

            travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance().getContext(), Engine
                    .getInstance().getUserName());
            sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
            lRowId = sqLiteDatabase.insert(tableNnam, null, groupList.getValues());
            sqLiteDatabase.close();
        }

        return lRowId;
    }
    
    public boolean isGroup(String number) {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("select * from " + tableNnam + " where number=?", new String[] {
                number
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    return true;
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return false;
    }
    
    public Data getNewGroup(String group_id, int type) {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("select * from " + tableNnam + " where group_id=? and type=?", new String[] {
                group_id, String.valueOf(type)
        });
        Data data = null;
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    data = new Data();
                    data.setData(c);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return data;
    }
    
    public boolean isGroupLeader(String group_id, String type) {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("select * from " + tableNnam + " where group_id=? and type=?", new String[] {
                group_id, type
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    return true;
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        c.close();
        return false;
    }
    
    public long update(GetNewGroup getNewGroup) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        long row = sqLiteDatabase.update(tableNnam, getNewGroup.getData().getValues(), "group_id=?",
                new String[] {getNewGroup.getData().getGroupNname()});

        sqLiteDatabase.close();
        return row;
    }
    
    public synchronized boolean deleteMessage(GetNewGroup getNewGroup) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(),Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(tableNnam, "group_id=?", new String[] {
                getNewGroup.getData().getGroupNname()
        });
        sqLiteDatabase.close();
        if (row > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public synchronized boolean updateColumn(String whereStr, String type, String newStr) {
        ContentValues values = new ContentValues();
        values.put(type, newStr);
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        int row = sqLiteDatabase.update(tableNnam, values, "group_id=?", new String[] {
                whereStr
        });
        sqLiteDatabase.close();
        if (row > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public void delAllData(){
        
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        sqLiteDatabase.delete(tableNnam, null, new String[]{});
    }
}
