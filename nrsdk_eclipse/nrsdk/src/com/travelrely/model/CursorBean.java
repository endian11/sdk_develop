
package com.travelrely.model;

import android.database.Cursor;

public class CursorBean {

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(Cursor cursor) {
        id = cursor.getInt(0);
    }
}
