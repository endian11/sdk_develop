package com.travelrely.v2.model;

import java.io.Serializable;

import android.database.Cursor;

public class LocationModel implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String longitude;
    
    public String latitude;
    
    public String userName;
    
    public String context;
    
    public String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
    
    public void setValue(Cursor c) {
        userName = c.getString(c.getColumnIndex("from_name"));
        longitude = c.getString(c.getColumnIndex("longitude"));
        latitude = c.getString(c.getColumnIndex("latitude"));
        nickName = c.getString(c.getColumnIndex("nick_name"));
    }

}
