package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.travelrely.core.Engine;
import com.travelrely.model.ContactModel;
import com.travelrely.model.ContactModel.TagNumber;
import com.travelrely.v2.response.GetAppVersion;
import com.travelrely.v2.util.HanziToPinyin;
import com.travelrely.v2.util.HanziToPinyin.Token;
import com.travelrely.v2.util.LOGManager;
import com.travelrely.v2.util.Utils;

/**
 * 1.负责将手机系统的通讯录及sim卡里的通讯录读取 <br>
 * 2.负责旅信通讯录的增删改查<br>
 * 
 * @author seekting
 */
public class ContactDBHelper
{
    public static ContactDBHelper instance;

    /** 获取库Phon表字段 **/
    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME,
            Phone.NUMBER,
            Phone.CONTACT_ID,
            Phone.TYPE, // Phone.HAS_PHONE_NUMBER
            "sort_key"
    };

    /** 联系人显示名称 **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /** 电话号码 **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /** 联系人的ID **/
    private static final int PHONES_CONTACT_ID_INDEX = 2;

    private ContactDBHelper()
    {
    	
    }

    public static ContactDBHelper getInstance()
    {
        if (instance == null)
        {
            instance = new ContactDBHelper();
        }
        return instance;
    }

    public TreeMap<Long, ContactModel> getPhoneContactHash()
    {
        TreeMap<Long, ContactModel> treeMap = new TreeMap<Long, ContactModel>();

        ContentResolver resolver = Engine.getInstance().getContext()
                .getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null)
        {
            while (phoneCursor.moveToNext())
            {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                {
                    LOGManager.w("没有号码" + phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX));
                    
                    continue;
                }

                // 去除号码中的分隔符
                phoneNumber = Utils.removeNonnumericChar(phoneNumber);

                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);
                
                String sortKey = phoneCursor.getString(4);

                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                int tag = phoneCursor
                        .getInt(phoneCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                ContactModel contactModel = treeMap.get(contactid);
                List<TagNumber> numbers = null;
                if (contactModel != null)
                {
                    numbers = contactModel.getPhoneNumList();
                }
                else
                {
                    contactModel = new ContactModel();
                    contactModel.setFirstName(Utils.getFirstName(contactName));
                    contactModel.setLastName(Utils.getLastName(contactName));
                    contactModel.setSortKey(sortKey);
                    contactModel.setRawContactId(contactid);
                    numbers = new ArrayList<ContactModel.TagNumber>();
                    contactModel.setPhoneNumList(numbers);
                    treeMap.put(contactid, contactModel);
                }
                TagNumber tagNumber = new TagNumber();
                tagNumber.setTag(Utils.getAndroidTag(tag));
                tagNumber.setValue(phoneNumber);
                numbers.add(tagNumber);
            }

            phoneCursor.close();
        }

        // 做一次重复排查：
        return treeMap;

    }

    /** 得到手机通讯录联系人信息 **/
    public List<ContactModel> getPhoneContacts()
    {
        List<ContactModel> list = new ArrayList<ContactModel>();
        TreeMap<Long, ContactModel> map = getPhoneContactHash();
        Iterator<Entry<Long, ContactModel>> iterator = map.entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<Long, ContactModel> entry = (Entry<Long, ContactModel>) iterator.next();
            ContactModel model = (ContactModel) entry.getValue();
            list.add(model);
        }
        
        LOGManager.w("联系人数量：" + map.size() + ":" + list.size());

        return list;
    }

    public long delete(long id)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        db.delete("tag_num", "contact_id=?",
                new String[] { String.valueOf(id) });
        long raw = db.delete("contact", "id=?",
                new String[] { String.valueOf(id) });
        
        UserDbManager.getInstance().closeDb();

        return raw;
    }

    List<ContactModel> list;

    public static String createTable()
    {
        String sql = "CREATE TABLE if not exists contact ("
                + "id integer primary key autoincrement,"
                + "num varchar,"
                + "first_name varchar,"
                + "email varchar,"
                + "travel_phone_number text,"
                + "sort_key text,"
                + "append_text_3 text,"
                + "append_text_4 text,"
                + "append_text_5 text,"
                + "append_text_6 text,"
                + "append_int_1 integer,"
                + "append_int_2 integer,"
                + "append_int_3 integer,"
                + "append_int_4 integer,"
                + "append_int_5 integer,"
                + "append_int_6 integer,"
                + "append_long_1 long,"
                + "append_long_2 long,"
                + "append_long_3 long,"
                + "append_long_4 long,"
                + "is_travel_user integer,"
                + "contact_type integer,"
                + "group_id varchar,"
                + "group_name varchar,"
                + "only_in_group integer,"
                + "head_portrait text,"
                + "local_head_img_path text,"
                + "raw_contact_id long,"
                + "server_id integer,"
                + "travel_user_phone varchar,"
                + "last_name varchar" + ")";
        return sql;
    }

    /**
     * 应对接口 int regist; String token; String valid_time; int presence; String
     * time_difference;
     * 
     * @return
     */
    public static String createTagNumTable()
    {
        String sql = "CREATE TABLE if not exists tag_num ("
                + "id integer primary key autoincrement,"
                + "contact_id integer,"
                + "token varchar,"
                + "valid_time varchar,"
                + "presence integer,"
                + "time_difference varchar,"
                + "regist integer,"
                + "value varchar,"
                + "tag varchar,"
                + "new_num char,"
                + "nick_name char,"
                + "head_portrait char,"
                + "local_head_path char,"
                + "text_5 text,"
                + "text_6 text,"
                + "int_1 integer,"
                + "int_2 integer,"
                + "int_3 integer,"
                + "int_4 integer,"
                + "int_5 integer,"
                + "int_6 integer,"
                + "long_1 long,"
                + "long_2 long,"
                + "long_3 long,"
                + "long_4 long,"
                + "varchar_1 varchar,"
                + "varchar_2 varchar,"
                + "varchar_3 varchar,"
                + "varchar_4 varchar,"
                + "varchar_5 varchar,"
                + "varchar_6 varchar,"
                + "varchar_7 varchar" + ")";
        return sql;
    }

    public boolean insertAll(List<ContactModel> list)
    {
        if (list == null)
        {
            return false;
        }

        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        db.beginTransaction();

        // String sql = "DELETE FROM " + "contact" + ";";

        String sql = "drop table if EXISTS contact";
        db.execSQL(sql);

        sql = "drop table if EXISTS tag_num";
        db.execSQL(sql);

        db.execSQL(createTable());
        db.execSQL(createTagNumTable());
        boolean flag = true;
        for (int i = 0; i < list.size(); i++)
        {
            ContactModel contactModel = list.get(i);

            flag = flag && contactModel.insert(db);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        UserDbManager.getInstance().closeDb();
        return flag;
    }

    public boolean insertAll(TreeMap<Long, ContactModel> treeMap)
    {
        if (treeMap == null)
        {
            return false;
        }

        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        db.beginTransaction();

        // String sql = "DELETE FROM " + "contact" + ";";

        String sql = "drop table if EXISTS contact";
        db.execSQL(sql);

        sql = "drop table if EXISTS tag_num";
        db.execSQL(sql);

        db.execSQL(createTable());
        db.execSQL(createTagNumTable());

        boolean flag = true;
        Iterator<Entry<Long, ContactModel>> titer = treeMap.entrySet().iterator();
        while (titer.hasNext())
        {
            Entry<Long, ContactModel> ent = (Entry<Long, ContactModel>) titer.next();
            //long key = (Long) ent.getKey();
            ContactModel contactModel = (ContactModel) ent.getValue();
            flag = flag && contactModel.insert(db);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        UserDbManager.getInstance().closeDb();
        return flag;
    }
    
    public boolean insertAll(HashMap<Long, ContactModel> treeMap)
    {
        if (treeMap == null)
        {
            return false;
        }

        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        db.beginTransaction();

        String sql = "drop table if EXISTS contact";
        db.execSQL(sql);

        sql = "drop table if EXISTS tag_num";
        db.execSQL(sql);

        db.execSQL(createTable());
        db.execSQL(createTagNumTable());

        Iterator<Entry<Long, ContactModel>> titer = treeMap.entrySet().iterator();
        while (titer.hasNext())
        {
            Entry<Long, ContactModel> ent = (Entry<Long, ContactModel>) titer.next();
            ContactModel contactModel = (ContactModel) ent.getValue();
            //flag = flag && contactModel.insert(sqLiteDatabase);
            
            // 先插入contact表
            long id = db.insert("contact", null, contactModel.getValues());

            // 再插入tag_num表
            if (contactModel.getPhoneNumList() != null)
            {
                for (int i = 0; i < contactModel.getPhoneNumList().size(); i++)
                {
                    TagNumber tagNumber = contactModel.getPhoneNumList().get(i);
                    tagNumber.setContact_id(id);
                    db.insert("tag_num", null, tagNumber.getValues());
                }
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        UserDbManager.getInstance().closeDb();
        return true;
    }

    public boolean insert(ContactModel contactModel)
    {
        
        LOGManager.d("添加联系人到数据库 = " + contactModel.getTravelUserPhone());
        
        SQLiteDatabase db = UserDbManager.getInstance().openDb();

        boolean ret = contactModel.insert(db);

        UserDbManager.getInstance().closeDb();
        return ret;
    }
    
    public void getContacts(List<ContactModel> all, List<ContactModel> tra)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();

        Cursor c = db.query("contact", null, null, null, null, null, "sort_key");
        if (c != null)
        {
            while (c.moveToNext())
            {
                ContactModel contact = new ContactModel();
                contact.setCursorValue(c);

                all.add(contact);
                if (contact.isTravelrelyUser())
                {
                    tra.add(contact);
                }
            }

            c.close();
        }

        UserDbManager.getInstance().closeDb();
    }

    /**
     * 获取本地数据库的通讯录
     * 
     * @return
     */
    public List<ContactModel> getAllContacts()
    {
        List<ContactModel> list = new ArrayList<ContactModel>();
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        // Cursor c = sqLiteDatabase.rawQuery("select * from contact", null);
        Cursor c = db.query("contact", null, null, null, null,
                null, "sort_key");
        if (c != null)
        {
            while (c.moveToNext())
            {
                ContactModel contactModel = new ContactModel();
                contactModel.setValue(c, db);
                // LogManager.d(contactModel.toString());
                list.add(contactModel);
            }

            c.close();
        }

        UserDbManager.getInstance().closeDb();
        return list;
    }
    
    public Cursor getAllCursor(List<ContactModel> list)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();

        Cursor c = db.query("contact", null, null, null, null,
                null, "sort_key");
        if (c != null)
        {
            while (c.moveToNext())
            {
                ContactModel contactModel = new ContactModel();
                contactModel.setValue(c, db);
                // LogManager.d(contactModel.toString());
                list.add(contactModel);
            }

            //c.close();
        }
        return c;
    }
    
    public Cursor getTraCursor(List<ContactModel> list)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();

        Cursor c = db.query("contact", null, "contact_type=?",
                new String[] { String.valueOf(ContactModel.TRAVELRELY_USER) },
                null, null, "sort_key");
        if (c != null)
        {
            while (c.moveToNext())
            {
                ContactModel contactModel = new ContactModel();
                contactModel.setValue(c, db);
                // LogManager.d(contactModel.toString());
                list.add(contactModel);
            }

            //c.close();
        }

        return c;
    }

    /**
     * 获取本地数据库的通讯录
     * 
     * @return
     */
    public TreeMap<Long, ContactModel> getAllContactsHash()
    {
        TreeMap<Long, ContactModel> hashMap = new TreeMap<Long, ContactModel>();
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        // Cursor c = sqLiteDatabase.rawQuery("select * from contact", null);
        Cursor c = db.query("contact", null, null, null, null,
                null, null);
        if (c != null)
        {
            while (c.moveToNext())
            {
                ContactModel contactModel = new ContactModel();
                contactModel.setValue(c, db);
                // LogManager.d(contactModel.toString());
                hashMap.put(contactModel.getRawContactId(), contactModel);
            }

            c.close();
        }

        UserDbManager.getInstance().closeDb();
        return hashMap;

    }

    /**
     * 获取旅信用户的通讯录
     * 
     * @return
     */
    public List<ContactModel> getTravelrelyContacts()
    {
        return getContacts(ContactModel.TRAVELRELY_USER);
    }

    /**
     * 获取非旅信用户
     * 
     * @return
     */
    public List<ContactModel> getNormalContacts()
    {
        return getContacts(ContactModel.NORMAL_USER);
    }

    public List<ContactModel> getGroupContacts()
    {
        return getContacts(ContactModel.TRAVELRELY_GROUP);
    }

    public List<ContactModel> getContacts(int type)
    {
        List<ContactModel> list = new ArrayList<ContactModel>();
        SQLiteDatabase db = UserDbManager.getInstance().openDb();

        // Cursor c = sqLiteDatabase.rawQuery("select * from contact", null);
        Cursor c = db.query("contact", null, "contact_type=?",
                new String[] { String.valueOf(type) }, null, null, "sort_key");

        if (c != null)
        {
            while (c.moveToNext())
            {
                ContactModel contactModel = new ContactModel();
                contactModel.setValue(c, db);
                // LogManager.d(contactModel.toString());
                list.add(contactModel);
            }

            c.close();
        }

        UserDbManager.getInstance().closeDb();
        return list;
    }

    /**
     * 获取本地数据库的通讯录
     * 
     * @return
     */
    public ContactModel getContactById(long id)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();

        Cursor c = db.rawQuery("select * from contact where id=?",
                new String[] { String.valueOf(id) });
        ContactModel contactModel = null;
        if (c != null)
        {
            while (c.moveToNext())
            {
                contactModel = new ContactModel();
                contactModel.setValue(c, db);
            }

            c.close();
        }

        UserDbManager.getInstance().closeDb();
        return contactModel;
    }

    /**
     * @param num
     * @return
     */
    public ContactModel getContactByNumberTry(String num)
    {
        ContactModel contactModel = getContactByNumber(num, "value");
        if (contactModel == null)
        {
            // 如果含有+86
            if (num.indexOf("+86") > -1)
            {
                num = Utils.generateNum(num);
                contactModel = getContactByNumber(num, "value");
            }
        }
        return contactModel;
    }

    /**
     * 获取本地数据库的通讯录
     * num 号码
     * value 字段名称
     * @return
     */
    public ContactModel getContactByNumber(String num, String value)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        Cursor c = db.rawQuery(
                "select * from tag_num where " + value + "=?",
                new String[] { num });
        TagNumber tagNum = null;
        if (c != null)
        {
            while (c.moveToNext())
            {
                tagNum = new TagNumber();
                tagNum.setValue(c);
            }

            c.close();
        }

        UserDbManager.getInstance().closeDb();

        ContactModel contactModel = null;
        if (tagNum != null)
        {
            contactModel = getContactById(tagNum.getContact_id());
        }

        return contactModel;
    }

    /**
     * 获取本地数据库的通讯录
     * 
     * @return
     */
    public List<ContactModel> getContactsByNumber(String num)
    {
        List<Long> ids = new ArrayList<Long>();
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        Cursor c = db.rawQuery(
                "select * from tag_num where value=?",
                new String[] { String.valueOf(num) });
        TagNumber tagNum = null;

        if (c != null)
        {
            while (c.moveToNext())
            {
                tagNum = new TagNumber();
                tagNum.setValue(c);
                if (ids.contains(tagNum.getContact_id()))
                {

                }
                else
                {
                    ids.add(tagNum.getContact_id());
                }
            }

            c.close();
        }

        UserDbManager.getInstance().closeDb();

        List<ContactModel> contacts = new ArrayList<ContactModel>();
        ContactModel contactModel = null;
        for (int i = 0; i < ids.size(); i++)
        {
            contactModel = getContactById(ids.get(i));
            contacts.add(contactModel);
        }

        return contacts;
    }
    
    public String getImgPathByNumber(String num)
    {
        String path = "";
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        Cursor c = db.rawQuery("select * from tag_num where value=?",
                new String[] { String.valueOf(num) });
        if (c != null)
        {
            while (c.moveToNext())
            {
                TagNumber tagNum = new TagNumber();
                tagNum.setValue(c);
                path = tagNum.getLocalHeadPath();
            }

            c.close();
        }

        UserDbManager.getInstance().closeDb();
        
        return path;
    }

    public long update(ContactModel contactModel)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        long row = db.update("contact",
                contactModel.generateValues(), "id=?",
                new String[] { String.valueOf(contactModel.getId()) });

        UserDbManager.getInstance().closeDb();
        return row;
    }

    public void updateSetServerId(List<ContactModel> list)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        db.beginTransaction();

        for (int i = 0; i < list.size(); i++)
        {
            ContactModel contactModel = list.get(i);
            int serverId = contactModel.getServerId();
            db.execSQL("UPDATE contact SET server_id = " + serverId
                    + " WHERE ID = " + contactModel.getId());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        UserDbManager.getInstance().closeDb();
    }
    
    public List<TagNumber> getTravelrelyTagNum()
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        List<TagNumber> tagNumbers = new ArrayList<ContactModel.TagNumber>();
        
        String sql = "select * from tag_num where regist=?";
        Cursor c = db.rawQuery(sql, new String[] { String.valueOf(1) });
        if (c != null)
        {
            while (c.moveToNext())
            {
                TagNumber tagNumber = new TagNumber();
                tagNumber.setValue(c);
                tagNumbers.add(tagNumber);
            }
            c.close();
        }
        
        UserDbManager.getInstance().closeDb();
        
        return tagNumbers;
    }
    
    public long updateHeadImg(TagNumber tag)
    {
        SQLiteDatabase db = UserDbManager.getInstance().openDb();
        
        long row = db.update("tag_num",
                tag.getValues(), "id=?",
                new String[] { String.valueOf(tag.getId()) });
        
        String sql = "UPDATE contact SET head_portrait='" + tag.getHeadPortrait()
                + "', local_head_img_path='" + tag.getLocalHeadPath()
                + "' WHERE id='" + tag.getContact_id() + "';";
        db.execSQL(sql);

        UserDbManager.getInstance().closeDb();
        return row;
    }
    
    public String getSortKey(String displayName)
    {
        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(displayName);
        if (tokens != null && tokens.size() > 0)
        {
            StringBuilder sb = new StringBuilder();
            for (Token token : tokens)
            {
                // Put Chinese character's pinyin, then proceed with the
                // character itself.
                if (Token.PINYIN == token.type)
                {
                    if (sb.length() > 0)
                    {
                        sb.append(' ');
                    }
                    sb.append(token.target);
                    sb.append(' ');
                    sb.append(token.source);
                }
                else
                {
                    if (sb.length() > 0)
                    {
                        sb.append(' ');
                    }
                    sb.append(token.source);
                }
            }
            return sb.toString();
        }

        return displayName;
    }
}
