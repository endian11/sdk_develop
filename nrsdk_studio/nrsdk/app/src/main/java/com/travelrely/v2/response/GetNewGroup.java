package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.core.nrs.Engine;

import android.content.ContentValues;
import android.database.Cursor;

public class GetNewGroup extends BaseResponse implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    ResponseInfo responseInfo;
    
    Data data;
    
    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    
    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetNewGroup.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    
    public static class Data extends BaseData implements Serializable{

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        String username;
        
        String id;
        
        String group_head_portrait;//群头像
        
        String group_name;//群名字
        
        String group_head_img_path;
        
        int create_contact_id;
        
        String number;
        
        int type;
        
        String expireddate;//群过期时间
        
        int version;
        
        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getExpireddate() {
            return expireddate;
        }

        public void setExpireddate(String expireddate) {
            this.expireddate = expireddate;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getGroup_head_img_path() {
            return group_head_img_path;
        }

        public void setGroup_head_img_path(String group_head_img_path) {
            this.group_head_img_path = group_head_img_path;
        }

        public int getCreate_contact_id() {
            return create_contact_id;
        }

        public void setCreate_contact_id(int create_contact_id) {
            this.create_contact_id = create_contact_id;
        }

        public String getGroup_head_portrait() {
            return group_head_portrait;
        }

        public void setGroup_head_portrait(String group_head_portrait) {
            this.group_head_portrait = group_head_portrait;
        }

        public String getGroupNname() {
            return group_name;
        }

        public void setGroupName(String group_name) {
            this.group_name = group_name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        
        @Override
        public void setValue(JSONObject jsonObject) {
            // TODO Auto-generated method stub
            super.setValue(jsonObject);
            this.username = jsonObject.optString("username");
            this.id = jsonObject.optString("id");
        }
        
        public ContentValues getValues() {
            
            ContentValues contentValues = new ContentValues();
            contentValues.put("group_name", group_name);
            contentValues.put("group_id", id);
            contentValues.put("number", Engine.getInstance().getUserName());
            contentValues.put("type", type);
            contentValues.put("group_head_portrait", group_head_portrait);
            contentValues.put("group_head_img_path", group_head_img_path);
            contentValues.put("create_contact_id", create_contact_id);
            contentValues.put("expireddate", expireddate);
            contentValues.put("version", version);
            
            return contentValues;

        }
        
        public void setData(Cursor c) {
            
            group_name = c.getString(c.getColumnIndex("group_name"));
            id = c.getString(c.getColumnIndex("group_id"));
            number = c.getString(c.getColumnIndex("number"));
            type = c.getInt(c.getColumnIndex("type"));
            group_head_portrait = c.getString(c.getColumnIndex("group_head_portrait"));
            group_head_img_path = c.getString(c.getColumnIndex("group_head_img_path"));
            create_contact_id = c.getInt(c.getColumnIndex("create_contact_id"));
            expireddate = c.getString(c.getColumnIndex("expireddate"));
            version = c.getInt(c.getColumnIndex("version"));

        }

    }

}
