package com.travelrely.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.nrs.Engine;
import com.travelrely.app.db.UserDBOpenHelper;
import com.travelrely.v2.response.FetchToken.Data.FetchTokenContact;

public class ContactModel extends BaseResponse implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int NORMAL_USER = 2;
    public static final int TRAVELRELY_USER = 1;
    public static final int TRAVELRELY_GROUP = 3;
    public static final int TRAVELRELY_SERVICE_NUM = 4;
    public static final int PUBLIC_SERVICE_NUM = 5;
    
    private String sort_key;

    String first_name;

    private String last_name;

    private String email;

    private long id;

    private String nickName;

    private int server_id = -1;

    int MAX_SIZE = 8;

    private String travel_phone_number;
    private int only_in_group;
    private String head_portrait;

    private String local_head_img_path;
    private long rawContactId;
    
    public String getSortKey()
    {
        return sort_key;
    }

    public void setSortKey(String key)
    {
        this.sort_key = key;
    }

    public String getLocalHeadImgPath()
    {
        return local_head_img_path;
    }

    public void setLocalHeadImgPath(String local_head_img_path)
    {
        this.local_head_img_path = local_head_img_path;
    }

    public String getHeadPortrait()
    {
        return head_portrait;
    }

    public void setHeadPortrait(String head_portrait)
    {
        this.head_portrait = head_portrait;
    }

    public long getRawContactId()
    {
        return rawContactId;
    }

    public void setRawContactId(long contact_id)
    {
        this.rawContactId = contact_id;
    }

    public int getOnly_in_group()
    {
        return only_in_group;
    }

    public void setOnly_in_group(int only_in_group)
    {
        this.only_in_group = only_in_group;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public void setTravelPhoneNumber(String travel_phone_number)
    {
        this.travel_phone_number = travel_phone_number;
    }

    public int getServerId()
    {
        return server_id;
    }

    public void setServerId(int server_id)
    {
        this.server_id = server_id;
    }

    public ContactModel()
    {

    }

    public int getContactType()
    {
        return iContactType;
    }

    public void setContactType(int contact_type)
    {
        this.iContactType = contact_type;
    }

    public String getGroupId()
    {
        return group_id;
    }

    public void setGroupId(String group_id)
    {
        this.group_id = group_id;
    }

    public String getGroupName()
    {
        return group_name;
    }

    public void setGroup_name(String group_name)
    {
        this.group_name = group_name;
    }

    List<TagNumber> phone_number_list;

    public int is_travel_user;

    // 旅信号码
    public String travel_user_phone;

    public String getTravelUserPhone()
    {
        return travel_user_phone;
    }

    public void setTravelUserPhone(String travel_user_phone)
    {
        this.travel_user_phone = travel_user_phone;
    }

    /**
     * 1.为旅信用户，3为群组
     */
    private int iContactType = 2;

    String group_id;

    String group_name;

    /**
     * 为了群发多选
     */
    public boolean isSelected;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return first_name;
    }

    public void setFirstName(String first_name)
    {
        this.first_name = first_name;
    }

    public String getLastName()
    {
        return last_name;
    }

    public void setLastName(String last_name)
    {
        this.last_name = last_name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public List<TagNumber> getPhoneNumList()
    {
        return phone_number_list;
    }

    public void setPhoneNumList(List<TagNumber> phone_number_list)
    {
        if (phone_number_list != null)
        {
            for (int i = 0; i < phone_number_list.size(); i++)
            {
                TagNumber tagNumber = phone_number_list.get(i);
                tagNumber.setParentContact(this);
            }
        }

        this.phone_number_list = phone_number_list;
    }

    public String getName()
    {
        if (first_name == null)
        {
            first_name = "";
        }

        if (last_name == null)
        {
            last_name = "";
        }

        nickName = first_name + last_name;
        if (nickName.equals(""))
        {
            List<TagNumber> tagNumbers = getPhoneNumList();
            if (tagNumbers != null && tagNumbers.size() > 0)
            {
                nickName = tagNumbers.get(0).getValue();

            }
        }

        return nickName;
    }

    public void setCursorValue(Cursor cursor)
    {
        first_name = cursor.getString(cursor.getColumnIndex("first_name"));
        server_id = cursor.getInt(cursor.getColumnIndex("server_id"));
        last_name = cursor.getString(cursor.getColumnIndex("last_name"));
        email = cursor.getString(cursor.getColumnIndex("email"));
        id = cursor.getLong(cursor.getColumnIndex("id"));
        is_travel_user = cursor.getInt(cursor.getColumnIndex("is_travel_user"));
        travel_user_phone = cursor.getString(cursor
                .getColumnIndex("travel_user_phone"));

        group_id = cursor.getString(cursor.getColumnIndex("group_id"));
        group_name = cursor.getString(cursor.getColumnIndex("group_name"));
        iContactType = cursor.getInt(cursor.getColumnIndex("contact_type"));
        travel_phone_number = cursor.getString(cursor
                .getColumnIndex("travel_phone_number"));
        only_in_group = cursor.getInt(cursor.getColumnIndex("only_in_group"));
        head_portrait = cursor
                .getString(cursor.getColumnIndex("head_portrait"));
        rawContactId = cursor.getLong(cursor.getColumnIndex("raw_contact_id"));
        local_head_img_path = cursor.getString(cursor
                .getColumnIndex("local_head_img_path"));
        sort_key = cursor.getString(cursor
                .getColumnIndex("sort_key"));
    }

    public ContentValues generateValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", first_name);
        contentValues.put("last_name", last_name);
        contentValues.put("email", email);
        contentValues.put("id", id);
        contentValues.put("is_travel_user", is_travel_user);
        contentValues.put("travel_user_phone", travel_user_phone);
        contentValues.put("contact_type", iContactType);
        contentValues.put("group_name", group_name);
        contentValues.put("group_id", group_id);
        contentValues.put("server_id", server_id);
        contentValues.put("travel_phone_number", travel_phone_number);
        contentValues.put("only_in_group", only_in_group);
        contentValues.put("head_portrait", head_portrait);
        contentValues.put("raw_contact_id", rawContactId);
        contentValues.put("local_head_img_path", local_head_img_path);

        return contentValues;
    }

    public boolean isGroup()
    {
        return iContactType == TRAVELRELY_GROUP;
    }

    public boolean isTravelrelyService()
    {
        return iContactType == TRAVELRELY_SERVICE_NUM;
    }
    
    public boolean isPublicService()
    {
        return iContactType == PUBLIC_SERVICE_NUM;
    }

    public boolean isTravelrelyUser()
    {
        return iContactType == TRAVELRELY_USER;
    }

    public TagNumber getTravelrelyNumber()
    {
        if (iContactType == 1)
        {
            for (int i = 0; i < phone_number_list.size(); i++)
            {
                TagNumber tagNumber = phone_number_list.get(i);
                if (tagNumber.regist == 1)
                {
                    return tagNumber;
                }
            }
        }

        return null;
    }

    public List<TagNumber> setTravelrelyNumber(String number)
    {
        List<TagNumber> tagNumbers = new ArrayList<ContactModel.TagNumber>();
        TagNumber tagNumber = new TagNumber();
        tagNumber.setValue(number);
        tagNumbers.add(tagNumber);
        return tagNumbers;
    }

    public void setValue(Cursor cursor, SQLiteDatabase sb)
    {
        setCursorValue(cursor);

        String sql = "select * from tag_num where contact_id=?";
        Cursor c = sb.rawQuery(sql, new String[] { String.valueOf(id) });
        if (c != null)
        {
            List<TagNumber> tagNumbers = new ArrayList<ContactModel.TagNumber>();
            while (c.moveToNext())
            {
                TagNumber tagNumber = new TagNumber();
                tagNumber.setValue(c);
                tagNumbers.add(tagNumber);
            }

            this.setPhoneNumList(tagNumbers);
            c.close();
        }
    }

//    public void setValue(JSONObject jsonObject)
//    {
//        iContactType = jsonObject.optInt("contact_type");
//
//        if (isGroup())
//        {
//            group_id = jsonObject.optString("group_id");
//            group_name = jsonObject.optString("group_name");
//            return;
//        }
//        first_name = jsonObject.optString("first_name");
//        last_name = jsonObject.optString("last_name");
//        email = jsonObject.optString("email");
//        id = jsonObject.optInt("id");
//        server_id = jsonObject.optInt("server_id");
//
//        JSONArray jsonArray = jsonObject.optJSONArray("phone_number_list");
//        if (jsonArray != null)
//        {
//            phone_number_list = new ArrayList<ContactModel.TagNumber>();
//            for (int i = 0; i < jsonArray.length(); i++)
//            {
//                JSONObject tagJsonObject = jsonArray.optJSONObject(i);
//                TagNumber tagNumber = new TagNumber();
//                tagNumber.setValue(tagJsonObject);
//
//                phone_number_list.add(tagNumber);
//            }
//            this.setPhoneNumList(phone_number_list);
//        }
//
//        super.setValue(jsonObject);
//    }

    public JSONObject generateJson()
    {
        JSONObject jsonObject = new JSONObject();
        // String num;
        //
        // String first_name;
        //
        // String last_name;
        //
        // String email;
        //
        // List<Number> phone_number_list;
        if (isGroup())
        {
            try
            {
                jsonObject.put("group_id", group_id);
                jsonObject.put("group_name", group_name);
                jsonObject.put("contact_type", 3);
                return jsonObject;
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                jsonObject.put("contact_type", iContactType);
                jsonObject.put("first_name", first_name);
                jsonObject.put("id", server_id);
                jsonObject.put("last_name", last_name);
                jsonObject.put("email", email);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }

    public boolean addTagNumberInDB(TagNumber tagNumber)
    {
        if (tagNumber == null)
        {
            return false;
        }

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(
                Engine.getInstance().getContext(), Engine.getInstance()
                        .getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getWritableDatabase();
        long id = sqLiteDatabase.insert("tag_num", null, tagNumber.getValues());
        tagNumber.setContact_id(this.getId());
        tagNumber.id = id;
        sqLiteDatabase.close();
        if (id > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean updateOrInsertTagNumberInDB(TagNumber tagNumber)
    {
        if (tagNumber.id >= 0)
        {
            return updateTagNumberInDB(tagNumber);
        }
        else
        {
            return addTagNumberInDB(tagNumber);
        }
    }

    public boolean updateTagNumberInDB(TagNumber tagNumber)
    {
        if (tagNumber == null)
        {
            return false;
        }
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(
                Engine.getInstance().getContext(), Engine.getInstance()
                        .getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getWritableDatabase();
        long id = sqLiteDatabase.update("tag_num", tagNumber.getValues(),
                "id=?", new String[] { String.valueOf(tagNumber.getId()) });
        sqLiteDatabase.close();
        if (id > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean delTagNumberInDB(TagNumber tagNumber)
    {
        if (tagNumber == null)
        {
            return false;
        }
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(
                Engine.getInstance().getContext(), Engine.getInstance()
                        .getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper
                .getWritableDatabase();
        long row = sqLiteDatabase.delete("tag_num", "id=?",
                new String[] { String.valueOf(tagNumber.id) });
        sqLiteDatabase.close();
        if (row > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean insert(SQLiteDatabase sb)
    {
        // 开启事务
        sb.beginTransaction();
        
        // 先插入contact表
        long id = sb.insert("contact", null, getValues());
        this.id = id;

        // 再插入tag_num表
        if (phone_number_list != null)
        {
            for (int i = 0; i < this.phone_number_list.size(); i++)
            {
                TagNumber tagNumber = phone_number_list.get(i);
                tagNumber.contact_id = id;
                long tag_num_id = sb.insert("tag_num", null,
                        tagNumber.getValues());
            }
        }

        sb.setTransactionSuccessful();
        sb.endTransaction();

        return true;
    }

    public void coverInsertTagNumbers(List<TagNumber> list)
    {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(
                Engine.getInstance().getContext(), Engine.getInstance()
                        .getUserName());
        SQLiteDatabase sql = travelrelyOpenHelper.getWritableDatabase();
        
        sql.beginTransaction();
        sql.delete("tag_num", "contact_id=?",
                new String[] { String.valueOf(this.id) });

        for (int i = 0; i < list.size(); i++)
        {
            TagNumber tagNumber = list.get(i);
            tagNumber.contact_id = id;
            long tag_num_id = sql
                    .insert("tag_num", null, tagNumber.getValues());
            tagNumber.id = tag_num_id;
        }
        sql.setTransactionSuccessful();
        sql.endTransaction();
        
        setPhoneNumList(list);
    }

    public static class TagNumber implements Serializable
    {
        private static final long serialVersionUID = 1L;

        ContactModel parentContact;

        public ContactModel getParentContact()
        {
            return parentContact;
        }

        public void setParentContact(ContactModel parentContact)
        {
            this.parentContact = parentContact;
        }

        public static final String TAG_DEFAULT = "电话";

        String tag;

        String value;
        
        String new_num;
        
        String nick_name;
        
        String head_portrait;
        
        String local_head_path;

        long contact_id;

        long id;

        /**
         * 1注册
         */
        int regist;

        String token;

        public String getToken()
        {
            return token;
        }

        public void setToken(String token)
        {
            this.token = token;
        }

        public String getNewNum()
        {
            return new_num;
        }

        public void setNewNum(String num)
        {
            this.new_num = num;
        }
        
        public String getNickName()
        {
            return nick_name;
        }

        public void setNickName(String name)
        {
            this.nick_name = name;
        }
        
        public String getHeadPortrait()
        {
            return head_portrait;
        }

        public void setHeadPortrait(String head)
        {
            this.head_portrait = head;
        }
        
        public String getLocalHeadPath()
        {
            return local_head_path;
        }

        public void setLocalHeadPath(String path)
        {
            this.local_head_path = path;
        }
        
        public String getValid_time()
        {
            return valid_time;
        }

        public void setValid_time(String valid_time)
        {
            this.valid_time = valid_time;
        }

        public int getPresence()
        {
            return presence;
        }

        public void setPresence(int presence)
        {
            this.presence = presence;
        }

        public String getTime_difference()
        {
            return time_difference;
        }

        public void setTime_difference(String time_difference)
        {
            this.time_difference = time_difference;
        }

        String valid_time;

        int presence;

        String time_difference;

        public void setValue(FetchTokenContact fetchTokenContact)
        {
            this.regist = fetchTokenContact.getRegister();
            this.time_difference = fetchTokenContact.getTime_difference();
            this.valid_time = fetchTokenContact.getValid_time();
            this.token = fetchTokenContact.getToken();
            this.presence = fetchTokenContact.getPresence();
            this.new_num = fetchTokenContact.getNewNum();
            this.nick_name = fetchTokenContact.getNick_name();
            this.head_portrait = fetchTokenContact.getHeadportrait();
        }

        public boolean isRegisted()
        {
            return regist == 1;
        }
        
        public int getRegist()
        {
            return regist;
        }

        public void setRegist(int regist)
        {
            this.regist = regist;
        }

        public long getId()
        {
            return id;
        }

        public void setId(long id)
        {
            this.id = id;
        }

        public long getContact_id()
        {
            return contact_id;
        }

        public void setContact_id(long contact_id)
        {
            this.contact_id = contact_id;
        }

        public String getTag()
        {
            return tag;
        }

        public void setTag(String tag)
        {
            this.tag = tag;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

//        public void setValue(JSONObject jsonObject)
//        {
//
//            this.tag = jsonObject.optString("tag");
//            this.value = jsonObject.optString("value");
//        }

//        public JSONObject generateJson()
//        {
//            JSONObject jsonObject = new JSONObject();
//            try
//            {
//                jsonObject.put("value", value);
//                jsonObject.put("tag", tag);
//            }
//            catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//
//            return jsonObject;
//        }

        /**
         * String token; String valid_time; int presence; String
         * time_difference;
         * 
         * @return
         */
        public ContentValues getValues()
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("tag", tag);
            contentValues.put("value", value);
            //0319 新加入的联系人 new_tum 一般会null，所以要加一句判断
            if (TextUtils.isEmpty(new_num)){
            	new_num = "+86" + value;
            }
            contentValues.put("new_num", new_num);
            contentValues.put("contact_id", contact_id);
            contentValues.put("regist", regist);
            contentValues.put("token", token);
            contentValues.put("valid_time", valid_time);
            contentValues.put("presence", presence);
            contentValues.put("time_difference", time_difference);
            contentValues.put("nick_name", nick_name);
            contentValues.put("head_portrait", head_portrait);
            contentValues.put("local_head_path", local_head_path);

            return contentValues;
        }

        /**
         * * String token; String valid_time; int presence; String
         * time_difference;
         * 
         * @param cursor
         */
        public void setValue(Cursor cursor)
        {
            id = cursor.getLong(cursor.getColumnIndex("id"));
            tag = cursor.getString(cursor.getColumnIndex("tag"));
            contact_id = cursor.getLong(cursor.getColumnIndex("contact_id"));
            value = cursor.getString(cursor.getColumnIndex("value"));
            new_num = cursor.getString(cursor.getColumnIndex("new_num"));
            token = cursor.getString(cursor.getColumnIndex("token"));
            valid_time = cursor.getString(cursor.getColumnIndex("valid_time"));
            time_difference = cursor.getString(cursor
                    .getColumnIndex("time_difference"));
            regist = cursor.getInt(cursor.getColumnIndex("regist"));
            presence = cursor.getInt(cursor.getColumnIndex("presence"));
            head_portrait = cursor.getString(cursor.getColumnIndex("head_portrait"));
        }

        public String toCode()
        {
            String result = tag + value;
            return result;
        }
    }

    public ContentValues getValues()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", first_name);
        contentValues.put("last_name", last_name);
        contentValues.put("email", email);
        contentValues.put("travel_user_phone", travel_user_phone);
        contentValues.put("contact_type", iContactType);
        contentValues.put("group_name", group_name);
        contentValues.put("group_id", group_id);
        contentValues.put("server_id", server_id);
        contentValues.put("only_in_group", only_in_group);
        contentValues.put("head_portrait", head_portrait);
        contentValues.put("raw_contact_id", rawContactId);
        contentValues.put("local_head_img_path", local_head_img_path);
        contentValues.put("sort_key", sort_key);
        return contentValues;
    }

    public boolean isSame(ContactModel other)
    {
        if (other.getRawContactId() != this.getRawContactId())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getOneNum()
    {
        if (phone_number_list == null || phone_number_list.size() == 0)
        {
            return "";
        }
        else
        {
            TagNumber tagNumber = phone_number_list.get(0);
            if (tagNumber != null)
            {
                return tagNumber.getValue();
            }

            return "";
        }
    }
    

    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     * 
     * @param sortKeyString
     *            数据库中读取出的sort key
     * @return 英文字母或者#
     */
    public String getSortKeyVal()
    {
        String key = sort_key.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]"))
        {
            return key;
        }
        return "#";
    }
}
