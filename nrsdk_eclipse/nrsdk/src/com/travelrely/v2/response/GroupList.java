package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.nrs.Engine;

import android.annotation.SuppressLint;
import android.content.ContentValues;

/** 
 * 
 * @author zhangyao
 * @version 2014年6月17日上午10:49:36
 */

public class GroupList extends BaseData implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    String group_id;

    String group_name;
    
    String group_head_portrait;
    
    String group_creater;

    String expireddate;
    
    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_head_portrait() {
        return group_head_portrait;
    }

    public void setGroup_head_portrait(String group_head_portrait) {
        this.group_head_portrait = group_head_portrait;
    }

    public String getGroup_creater() {
        return group_creater;
    }

    public void setGroup_creater(String group_creater) {
        this.group_creater = group_creater;
    }
    
    @SuppressLint("NewApi")
    @Override
    public void setValue(JSONObject jsonObject) {

        this.group_id = jsonObject.optString("group_id");
        this.group_name = jsonObject.optString("group_name");
        this.group_head_portrait = jsonObject.optString("group_head_portrait");
        this.group_creater = jsonObject.optString("group_creater");
        this.expireddate = jsonObject.optString("expireddate");
    }
    
    public ContentValues getValues() {
        
        if(Engine.getInstance().getUserName().equals(group_creater)){
            ContentValues contentValues = new ContentValues();
            contentValues.put("group_name", group_name);
            contentValues.put("group_id", group_id);
            contentValues.put("number", Engine.getInstance().getUserName());
            contentValues.put("type", 1);
            contentValues.put("group_head_portrait", group_head_portrait);
            contentValues.put("group_head_img_path", "");
            contentValues.put("create_contact_id", Engine.getInstance().getUserName());
            contentValues.put("expireddate", expireddate);
            
            return contentValues;
        }
        return null;
    }
}
