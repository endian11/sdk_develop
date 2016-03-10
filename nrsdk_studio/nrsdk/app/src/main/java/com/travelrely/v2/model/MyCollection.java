package com.travelrely.v2.model;

import android.content.ContentValues;
import android.database.Cursor;

public class MyCollection {
    
    public static final int typeText = 0;//文本
    public static final int typeImg = 1;//图片
    public static final int typeTrip = 2;//行程
    public static final int typeVoic = 3;//语音
    public static final int typeOther = 4;//其它
    
    int id;

    int type;
    
    String from_name;
    
    String time;
    
    String file_name;
    
    String mark;
    
    String other;
    
    String typeId;
    
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
    
    public ContentValues generateValues() {
        ContentValues contentValues = new ContentValues();
        
        contentValues.put("type", type);
        contentValues.put("type_id", typeId);
        contentValues.put("from_name", from_name);
        contentValues.put("time", time);
        contentValues.put("file_name", file_name);
        contentValues.put("other", other);
        
        return contentValues;
    }

    public void setValue(Cursor cursor)
    {
        //super.setValue(cursor);
        id = cursor.getInt(cursor.getColumnIndex("id"));
        type = cursor.getInt(cursor.getColumnIndex("type"));
        typeId = cursor.getString(cursor.getColumnIndex("type_id"));
        from_name = cursor.getString(cursor.getColumnIndex("from_name"));
        time = cursor.getString(cursor.getColumnIndex("time"));
        file_name = cursor.getString(cursor.getColumnIndex("file_name"));
        other = cursor.getString(cursor.getColumnIndex("other"));
        mark = cursor.getString(cursor.getColumnIndex("mark"));
    }
}
