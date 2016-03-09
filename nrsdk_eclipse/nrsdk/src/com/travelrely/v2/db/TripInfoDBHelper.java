
package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.Engine;
import com.travelrely.v2.response.TripInfoList;
import com.travelrely.v2.response.TripInfoList.ActivityList;
import com.travelrely.v2.response.TripInfoList.Daylist;

public class TripInfoDBHelper {

    static String tableNnam_trip_info = "trip_info";
    static String tableNnam_daylist = "trip_daylist";
    static String tableNnam_activitylist = "trip_activitylist";

    public static String createTable_tripInfo() {

        String table = "CREATE TABLE if not exists " + tableNnam_trip_info + " ("
                + "id integer primary key autoincrement," + "tripid text," + "tripdesc text,"
                + "trippos text" + ")";
        return table;
    }

    public static String createTable_daylist() {

        String table = "CREATE TABLE if not exists " + tableNnam_daylist + " ("
                + "id integer primary key autoincrement," + "tripid text," + "day text,"
                + "date text," + "tripdesc text" + ")";
        return table;
    }

    public static String createTable_activitylist() {

        String table = "CREATE TABLE if not exists " + tableNnam_activitylist + " ("
                + "id integer primary key autoincrement," + "date text," + "alarm_content text,"
                + "alarm_time text," + "alarm_on_off integer," + "begintime text," + "endtime text," + "position text," + "remark text,"
                + "content text," + "location_type text" + ")";
        return table;
    }

    private static TripInfoDBHelper tripInfoDBHelper;

    public static TripInfoDBHelper getInstance() {

        if (tripInfoDBHelper == null) {

            tripInfoDBHelper = new TripInfoDBHelper();
        }

        return tripInfoDBHelper;
    }

    public synchronized void addTripInfo(TripInfoList tripInfoList) {

        List<TripInfoList.Daylist> daylists = new ArrayList<TripInfoList.Daylist>();

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        sqLiteDatabase.insert(tableNnam_trip_info, null, tripInfoList.generateValues());
        for (int i = 0; i < tripInfoList.getDaylists().size(); i++) {
            sqLiteDatabase.insert(tableNnam_daylist, null, tripInfoList.getDaylists().get(i)
                    .generateValues(tripInfoList.getTripid(), tripInfoList.getTripdesc()));
            Daylist daylist = new Daylist();
            daylist = tripInfoList.getDaylists().get(i);
            for (int j = 0; j < daylist.getActivityLists().size(); j++) {
                sqLiteDatabase.insert(tableNnam_activitylist, null,
                        daylist.getActivityLists().get(j).generateValues(daylist.getDate()));
            }
        }

        sqLiteDatabase.close();
    }

    public synchronized List<TripInfoList> getTripInfoList() {

        List<TripInfoList> result = new ArrayList<TripInfoList>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam_trip_info;
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                });

        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TripInfoList tripInfoList = new TripInfoList();
                    tripInfoList.getData(c);
                    tripInfoList.setDaylists(getDayList(tripInfoList.getTripid()));
                    result.add(tripInfoList);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }

        return result;
    }
    
    public synchronized List<TripInfoList> getTripInfoList(String tripId) {

        List<TripInfoList> result = new ArrayList<TripInfoList>();
        TripInfoList tInfoList;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        String sql = "select * from " + tableNnam_trip_info + " where tripid = ? ";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                tripId
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    tInfoList = new TripInfoList();
                    tInfoList.getData(c);
                    result.add(tInfoList);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }
    
    public synchronized boolean isTripInf(String tripId) {
        
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        String sql = "select * from " + tableNnam_trip_info + " where tripid = ? ";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                tripId
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

    public synchronized List<TripInfoList.Daylist> getDayList(String tripId) {

        Engine.getInstance().daylists.clear();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam_daylist + " where tripid = ? ";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                tripId
        });

        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TripInfoList.Daylist daylist = new Daylist();
                    daylist.getData(c);
                    Engine.getInstance().daylists.add(daylist);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return Engine.getInstance().daylists;
    }

    public synchronized List<TripInfoList.Daylist> getDay(String tripId) {

        List<TripInfoList.Daylist> result = new ArrayList<TripInfoList.Daylist>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam_daylist + " where tripid = ? ";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                tripId
        });

        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TripInfoList.Daylist daylist = new Daylist();
                    daylist.getData(c);
                    result.add(daylist);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }

    public synchronized void getActivityList(String date) {

        ActivityList aList;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        String sql = "select * from " + tableNnam_activitylist + " where date = ? ";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                date
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    aList = new ActivityList();
                    aList.getData(c);
                    Engine.getInstance().activityLists.add(aList);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
    }
    
    public synchronized List<ActivityList> getActivityListInfo(String dates, String ids) {

        String tag = "id";
        String connect = "";
        if(dates != null){
            tag = "date";
            connect = dates;
        }else if(ids != null){
            tag = "id";
            connect = ids;
        }
        
        List<ActivityList> lActivityLists = new ArrayList<TripInfoList.ActivityList>();
        ActivityList aList = null;
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        String sql = "select * from " + tableNnam_activitylist + " where " + tag + " = ? ";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                connect
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    aList = new ActivityList();
                    aList.getData(c);
                    lActivityLists.add(aList);
                }
            } catch (Exception e) {
            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return lActivityLists;
    }

    public long update(ActivityList activityList, String date) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        long row = sqLiteDatabase.update(tableNnam_activitylist, activityList.generateValues(date),
                "id=?",
                new String[] {
                    activityList.getId()
                });

        sqLiteDatabase.close();
        return row;
    }
    
    public synchronized int deleteTripInfo(String tripId) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(),Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        int raw = sqLiteDatabase.delete(tableNnam_trip_info, "tripid=?", new String[] {
                tripId
        });
        sqLiteDatabase.close();
        
        deleteDayList(tripId);
        
        return raw;
    }
    
    public synchronized int deleteDayList(String tripId) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(),Engine.getInstance().getUserName());
        
        for(int i = 0; i < getDay(tripId).size(); i ++){
            deleteActivityList(getDay(tripId).get(i).getDate());
        }

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        int raw = sqLiteDatabase.delete(tableNnam_daylist, "tripid=?", new String[] {
                tripId
        });
        sqLiteDatabase.close();
        return raw;
    }
    
    public synchronized int deleteActivityList(String date) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(),Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        int raw = sqLiteDatabase.delete(tableNnam_activitylist, "date=?", new String[] {
                date
        });
        sqLiteDatabase.close();
        return raw;
    }
    

}
