package com.travelrely.v2.model;

import android.content.ContentValues;
import android.database.Cursor;

public class ReceptionInfo {
    
    int id;
    
    String groupId;
    
    String fromName;
    
    String context;
    
    int type;//1代表签到   2代表未签到
    
    String longitude;
    
    String latitude;
    
    String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
    
    public ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("group_id", groupId);
        contentValues.put("from_name", fromName);
        contentValues.put("context", context);
        contentValues.put("type", type);
        contentValues.put("longitude", longitude);
        contentValues.put("latitude", latitude);
        contentValues.put("nick_name", nickName);
        
        return contentValues;
    }
    
    public void setValue(Cursor c) {
        groupId = c.getString(c.getColumnIndex("group_id"));
        fromName = c.getString(c.getColumnIndex("from_name"));
        context = c.getString(c.getColumnIndex("context"));
        type = c.getInt(c.getColumnIndex("type"));
        longitude = c.getString(c.getColumnIndex("longitude"));
        latitude = c.getString(c.getColumnIndex("latitude"));
        nickName = c.getString(c.getColumnIndex("nick_name"));
    }
}
