package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

public class GroupMsg extends BaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String number;
	
	int type;
	
	String nickname;
	
	String head_portrait;
	
	String group_id;
	
	int contact_id;
	
	String tag;

	public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHead_portrait() {
		return head_portrait;
	}

	public void setHead_portrait(String head_portrait) {
		this.head_portrait = head_portrait;
	}
	
    @SuppressLint("NewApi")
    @Override
    public void setValue(JSONObject jsonObject) {

        this.number = jsonObject.optString("number");
        this.type = jsonObject.optInt("type");
        this.nickname = jsonObject.optString("nick_name");
        this.head_portrait = jsonObject.optString("head_portrait");
        if(this.nickname.isEmpty()){
            this.nickname = this.number;
        }
    }
    
    public ContentValues setValues() {
        
        ContentValues contentValues = new ContentValues();
        contentValues.put("group_id", group_id);
        contentValues.put("number", number);
        contentValues.put("type", type);
        contentValues.put("nick_name", nickname);
        contentValues.put("head_portrait", head_portrait);

        return contentValues;

    }
    
    public void getValue(Cursor cursor) {

        group_id = cursor.getString(cursor.getColumnIndex("group_id"));
        number = cursor.getString(cursor.getColumnIndex("number"));
        type = cursor.getInt(cursor.getColumnIndex("type"));
        nickname = cursor.getString(cursor.getColumnIndex("nick_name"));
        head_portrait = cursor.getString(cursor.getColumnIndex("head_portrait"));
    }

}
