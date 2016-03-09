package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;

import android.content.ContentValues;
import android.database.Cursor;

public class TripInfoList extends BaseResponse implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public String tripid;
    
    public String tripdesc;
    
    public String trippos;

    public List<Daylist> daylist;
    
    public String getTrippos() {
        return trippos;
    }

    public void setTrippos(String trippos) {
        this.trippos = trippos;
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public String getTripdesc() {
        return tripdesc;
    }

    public void setTripdesc(String tripdesc) {
        this.tripdesc = tripdesc;
    }

    public List<Daylist> getDaylists() {
        return daylist;
    }

    public void setDaylists(List<Daylist> daylists) {
        this.daylist = daylists;
    }
    
    public void setValue(JSONObject jsonObject){
        
        this.daylist = new ArrayList<Daylist>();
        
//        this.username = jsonObject.optString("username");
        this.tripid = jsonObject.optString("tripid");
        this.tripdesc = jsonObject.optString("tripdesc");
        this.trippos = jsonObject.optString("trippos");
        
        JSONArray jsonArray = jsonObject.optJSONArray("daylist");
        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length(); i ++){
                jsonObject = jsonArray.optJSONObject(i);
                
                if(jsonObject != null){
                    Daylist daylist = new Daylist();
                    daylist.setValue(jsonObject);
                    this.daylist.add(daylist);
                }
            }
        }
    }
    
    public ContentValues generateValues(){
        
        ContentValues contentValues = new ContentValues();
        
        contentValues.put("tripid", tripid);
        contentValues.put("tripdesc", tripdesc);
        contentValues.put("trippos", trippos);
//        contentValues.put("username", username);
        
        return contentValues;
    }
    
    public void getData(Cursor c){
        
        tripid = c.getString(c.getColumnIndex("tripid"));
        tripdesc = c.getString(c.getColumnIndex("tripdesc"));
        trippos = c.getString(c.getColumnIndex("trippos"));
//        username = c.getString(c.getColumnIndex("username"));
    }

    public static class Daylist extends BaseResponse implements Serializable{

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        public String day;
        
        public String date;
        
        public String tripid;
        
        public String tripdesc;

        public String getTripid() {
            return tripid;
        }

        public void setTripid(String tripid) {
            this.tripid = tripid;
        }

        public String getTripdesc() {
            return tripdesc;
        }

        public void setTripdesc(String tripdesc) {
            this.tripdesc = tripdesc;
        }

        public List<ActivityList> activitylist = new ArrayList<TripInfoList.ActivityList>();

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<ActivityList> getActivityLists() {
            return activitylist;
        }

        public void setActivityLists(List<ActivityList> activityLists) {
            this.activitylist = activityLists;
        }
        
        public void setValue(JSONObject jsonObject){
            
            this.activitylist = new ArrayList<ActivityList>();
            
            this.day = jsonObject.optString("day");
            this.date = jsonObject.optString("date");
            
            JSONArray activitylistArray = jsonObject.optJSONArray("activitylist");
            if(activitylistArray != null){
                for(int i = 0; i < activitylistArray.length(); i ++){
                    jsonObject = activitylistArray.optJSONObject(i);
                    
                    if(jsonObject != null){
                        ActivityList activityList = new ActivityList();
                        activityList.setValue(jsonObject);
                        this.activitylist.add(activityList);
                    }
                }
            }
        }
        
        public ContentValues generateValues(String tripid,String tripdesc){
            
            ContentValues contentValues = new ContentValues();
            
            contentValues.put("tripid", tripid);
            contentValues.put("day", day);
            contentValues.put("date", date);
            contentValues.put("tripdesc", tripdesc);
            
            return contentValues;
        }
        
        public void getData(Cursor c){
            
            day = c.getString(c.getColumnIndex("day"));
            date = c.getString(c.getColumnIndex("date"));
            tripid = c.getString(c.getColumnIndex("tripid"));
            tripdesc = c.getString(c.getColumnIndex("tripdesc"));
        }
    }
    
    public static class ActivityList extends BaseResponse implements Serializable{

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public String begintime;
        
        public String endtime;
        
        public String position;
        
        public String content;
        
        public String remark;
        
        public String location_type;
        
        public Alarm alarm;

        public String date;
        
        public String id;
        
        public int alarm_on_off = 1;//闹钟开关 1：关（默认） 2：开

        public int getAlarm_on_off() {
            return alarm_on_off;
        }

        public void setAlarm_on_off(int alarm_on_off) {
            this.alarm_on_off = alarm_on_off;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBegintime() {
            return begintime;
        }

        public void setBegintime(String begintime) {
            this.begintime = begintime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
        
        public String getLocation_type() {
            return location_type;
        }

        public void setLocation_type(String location_type) {
            this.location_type = location_type;
        }
        
        public Alarm getmAlarm() {
            return alarm;
        }

        public void setmAlarm(Alarm mAlarm) {
            this.alarm = mAlarm;
        }
        
        public void setValue(JSONObject jsonObject) {

            JSONObject jObject = jsonObject.optJSONObject("alarm");
            alarm = new Alarm();
            if(jObject != null){
                alarm.setValue(jObject);
            }
            this.begintime = jsonObject.optString("begintime");
            this.endtime = jsonObject.optString("endtime");
            this.position = jsonObject.optString("position");
            this.content = jsonObject.optString("content");
            this.remark = jsonObject.optString("remark");
        }
        
        public ContentValues generateValues(String date){
            
            ContentValues contentValues = new ContentValues();
            
            contentValues.put("date", date);
            contentValues.put("alarm_content", alarm.getAlarm_content());
            contentValues.put("alarm_time", alarm.getTime());
            contentValues.put("begintime", begintime);
            contentValues.put("endtime", endtime);
            contentValues.put("position", position);
            contentValues.put("remark", remark);
            contentValues.put("content", content);
            contentValues.put("location_type", location_type);
            contentValues.put("alarm_on_off", alarm_on_off);//闹钟开关 1：关（默认） 2：开
            
            return contentValues;
        }
        
        public void getData(Cursor c){
            
            if(alarm == null){
                alarm = new Alarm();
            }
            date = c.getString(c.getColumnIndex("date"));
            alarm.content = c.getString(c.getColumnIndex("alarm_content"));
            alarm.time = c.getString(c.getColumnIndex("alarm_time"));
            begintime = c.getString(c.getColumnIndex("begintime"));
            endtime = c.getString(c.getColumnIndex("endtime"));
            position = c.getString(c.getColumnIndex("position"));
            remark = c.getString(c.getColumnIndex("remark"));
            content = c.getString(c.getColumnIndex("content"));
            location_type = c.getString(c.getColumnIndex("location_type"));
            id = c.getString(c.getColumnIndex("id"));
            alarm_on_off = c.getInt(c.getColumnIndex("alarm_on_off"));
        }
    }
    
    public static class Alarm extends BaseResponse implements Serializable{

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        public String time;
        
        public String content;
        
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAlarm_content() {
            return content;
        }

        public void setAlarm_content(String alarm_content) {
            this.content = alarm_content;
        }
        
        public void setValue(JSONObject jsonObject){
            
            this.time = jsonObject.optString("time");
            this.content = jsonObject.optString("content");
        }
    }

}
