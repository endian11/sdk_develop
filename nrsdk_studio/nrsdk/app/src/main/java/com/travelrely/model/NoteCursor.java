
package com.travelrely.model;

import android.content.ContentValues;
import android.database.Cursor;

public class NoteCursor extends CursorBean {

    long dayTime;

    long createTime;

    long updateTime;

    String note;

    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "NoteCursor [dayTime=" + dayTime + ", createTime=" + createTime + ", updateTime="
                + updateTime + ", note=" + note + ", dayStr=" + date + "]";
    }

    public void setValue(Cursor cursor) {

        super.setValue(cursor);
        dayTime = cursor.getLong(cursor.getColumnIndex("day"));
        createTime = cursor.getLong(cursor.getColumnIndex("note_create_time"));
        updateTime = cursor.getLong(cursor.getColumnIndex("note_update_time"));
        note = cursor.getString(cursor.getColumnIndex("note"));
        date = cursor.getString(cursor.getColumnIndex("date"));

    }

    public ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", getDayTime());
        contentValues.put("note_create_time", getCreateTime());
        contentValues.put("note_update_time", getUpdateTime());
        contentValues.put("note", getNote());
        contentValues.put("date", getDate());

        return contentValues;

    }
}
