
package com.travelrely.v2.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.util.LOGManager;
import com.travelrely.model.ContactModel;
import com.travelrely.v2.response.TraMessage;

public class TravelrelyMessageDBHelper {

    // static String userNnam = "message" +
    // Engine.getInstance().getUserName().replace("+86", "");

    static String tableNnam = "message";

    public static String createTable() {

        // day 表示它当前所在的是哪一天的毫秒数，note_time是它写记录的时间
        // insert_time插入表时的时间
        // is_from，是否为收到的消息
        //

        String table = "CREATE TABLE if not exists " + tableNnam + " ("
                + "id integer primary key autoincrement," + "from_ text," + "from_type integer,"
                + "to_ text," + "to_type integer," + "type integer," + "msg_id integer,"
                + "ext_type text," + "time datetime," + "priority integer," + "content text,"
                + "url text," + "is_from integer," + "insert_time long," + "isRead integer,"
                + "store_type integer," + "nick_name text," + "head_portrait text,"
                + "head_portrait_url text,"
                + "group_name text," + "group_head_portrait text," + "group_id text,"
                + "msg_type integer," + "msg_count integer," + "alarm_type integer,"
                + "alarm_count integer," + "alarm_on_off integer," + "from_head_portrait text,"
                + "unReadCount integer," + "reception_on_off integer," + "code text," 
                + "is_nickname integer," + "contact_id long" + "append_int_1 integer," 
                + "append_int_2 integer,"+ "append_int_3 integer,"
                + "append_int_4 integer," + "append_int_5 integer,"
                + "append_long_1 long," + "append_long_2 long," + "append_long_3 long,"
                + "append_long_4 long," + "append_long_5 long,"
                + "append_text_1 text," + "append_text_2 text," + "append_text_3 text,"
                + "append_text_4 text," + "append_text_5 text,"
                + "append_text_6 text," + "append_text_7 text," + "append_text_8 text,"
                + "append_text_9 text," + "append_text_10 text,"
                + "width_user text," + "user_name text,"
                + "note_create_time long" + ")";
        return table;

    }

    private static TravelrelyMessageDBHelper travelrelyMessageDBHelper;

    private TravelrelyMessageDBHelper() {

    }

    public static TravelrelyMessageDBHelper getInstance() {

        if (travelrelyMessageDBHelper == null) {
            travelrelyMessageDBHelper = new TravelrelyMessageDBHelper();
        }
        return travelrelyMessageDBHelper;
    }

    public synchronized long addMessage(TraMessage message) {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        long row = sqLiteDatabase.insert(tableNnam, null, message.generateValues());
        sqLiteDatabase.close();
        return row;

    }

    public synchronized List<TraMessage> getMessages(String username, String withUser, int count) {

        List<TraMessage> result = new ArrayList<TraMessage>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam + " where from_=? order by id desc limit "
                + count;
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                username
        });

        if (c != null) {
            try {

                while (c.moveToNext()) {

                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    result.add(message);

                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }

        }
        return result;
    }

    public synchronized List<TraMessage> getAlarmMessages(String type) {

        List<TraMessage> result = new ArrayList<TraMessage>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

//        String sql = "select * from " + tableNnam
//                + " where type=? and alarm_type=? order by time desc";
        String sql = "select * from " + tableNnam
                + " where type=? and alarm_type=1 or alarm_type=2 order by time desc";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                type
        });
        if (c != null) {
            try {

                while (c.moveToNext()) {
                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    result.add(message);
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }

    public synchronized List<TraMessage> getMessages(String username, int type) {

        List<TraMessage> result = new ArrayList<TraMessage>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql;

        if (type == 3) {
            sql = "select * from " + tableNnam + " where type=3 and alarm_type=1 or alarm_type=2";
        } else {
            sql = "select * from " + tableNnam + " group by from_";
        }
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                // username
                });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    result.add(message);
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }
    
    public synchronized List<TraMessage> selectMessages(String username) {

        List<TraMessage> result = new ArrayList<TraMessage>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        String sql = "select * from " + tableNnam + " where from_=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                 username
                });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    result.add(message);
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }
    
    public synchronized List<TraMessage> selectMessagesState(String username, String file, String fileMsg) {

        List<TraMessage> result = new ArrayList<TraMessage>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());
        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        String sql = "select * from " + tableNnam + " where from_=? and " + file + "=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                 username, fileMsg
                });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    result.add(message);
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }

    public synchronized TraMessage getContext(String username, String field) {

        TraMessage result = new TraMessage();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql;

        sql = "select * from " + tableNnam + " where from_=? and msg_type=2 order by " + field
                + " desc LIMIT 1";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                username
        });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    result = message;
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }

    public synchronized boolean deleteMessage(int id) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(tableNnam, "id=?", new String[] {
                String.valueOf(id)
        });
        sqLiteDatabase.close();
        if (row > 0) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean deleteMessageList(String from) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(tableNnam, "from_=?", new String[] {
                from
        });
        sqLiteDatabase.close();
        if (row > 0) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized int deleteMessages(String from) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        int raw = sqLiteDatabase.delete(tableNnam, "user_name=? and width_user=?", new String[] {
                Engine.getInstance().getUserName(), from
        });
        sqLiteDatabase.close();
        return raw;
    }
    
    public synchronized int deleteMessagesAll(String from) {
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();
        int raw = sqLiteDatabase.delete(tableNnam, "from_=?", new String[] {
                from
        });
        sqLiteDatabase.close();
        return raw;
    }

    public synchronized boolean update(TraMessage message) {
        ContentValues content = message.generateValues();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        int row = sqLiteDatabase.update(tableNnam, content, "id=?", new String[] {
                String.valueOf(message.getId())
        });
        sqLiteDatabase.close();
        if (row > 0) {
            return true;
        } else {
            return false;
        }

    }

    public synchronized boolean updateColumn(TraMessage message, int type, int on_off) {
        ContentValues values = new ContentValues();
        values.put("alarm_type", type);
        values.put("alarm_on_off", on_off);
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        int row = sqLiteDatabase.update(tableNnam, values, "id=?", new String[] {
                String.valueOf(message.getId())
        });
        sqLiteDatabase.close();
        if (row > 0) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void updateContext(String from, String id, String strId, String field,
            String newContext) {
        ContentValues values = new ContentValues();
        values.put(field, newContext);
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getWritableDatabase();
        sqLiteDatabase.update(tableNnam, values, "from_=? and " + strId + "=?", new String[] {
                from, id
        });
        sqLiteDatabase.close();
    }

    public synchronized TraMessage getMessageById(int id, String velue) {

        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam + " where " + velue + "=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                String.valueOf(id)
        });

        if (c != null) {
            try {

                if (c.moveToNext()) {
                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    LOGManager.e("有message");
                    return message;
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }

        }
        return null;
    }

    public List<ContactModel> getGroup() {

        List<ContactModel> list = new ArrayList<ContactModel>();
        List<TraMessage> messages = getMessages(Engine.getInstance().getUserName(), 3);
        List<TraMessage> toMessage = getToGroupMessages();
        toMessage.addAll(messages);
        HashMap<String, TraMessage> maps = new HashMap<String, TraMessage>();
        for (int i = 0; i < toMessage.size(); i++) {
            TraMessage message = toMessage.get(i);
            maps.put(message.getGroup_id(), message);
            LOGManager.d("group id" + message.getGroup_id());
        }
        Iterator iter = maps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            TraMessage val = (TraMessage) entry.getValue();
            ContactModel group = new ContactModel();
            group.setContactType(3);
            group.setGroupId(val.getGroup_id());
            group.setGroup_name(val.getGroup_name());
            list.add(group);

        }
        return list;
    }

    public List<TraMessage> getToGroupMessages() {
        List<TraMessage> result = new ArrayList<TraMessage>();
        String sql = "select * from " + tableNnam + " where to_type=?";
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                "1"
        });
        if (c != null) {
            try {

                while (c.moveToNext()) {

                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    result.add(message);

                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }

        }
        return result;

    }

    public synchronized List<TraMessage> getHeadMessages() {

        List<TraMessage> result = new ArrayList<TraMessage>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam + " where head_portrait order by time desc";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    result.add(message);
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }
    
    public synchronized List<String> selectMsgImg(int msgId, String from, int type, int msg_type) {

        List<String> result = new ArrayList<String>();
        UserDBOpenHelper travelrelyOpenHelper = new UserDBOpenHelper(Engine.getInstance()
                .getContext(), Engine.getInstance().getUserName());

        SQLiteDatabase sqLiteDatabase = travelrelyOpenHelper.getReadableDatabase();

        String sql = "select * from " + tableNnam + " where from_=? and type=? and msg_type=?";
        Cursor c = sqLiteDatabase.rawQuery(sql, new String[] {
                from, String.valueOf(type), String.valueOf(msg_type)
                });
        if (c != null) {
            try {
                while (c.moveToNext()) {
                    TraMessage message = new TraMessage();
                    message.setValue(c);
                    if(message.getUrl() != null){
                        if(message.getId() == msgId){
                            result.add(0, message.getUrl());
                        }else{
                            result.add(message.getUrl());
                        }
                    }
                }
            } catch (Exception e) {

            } finally {
                c.close();
                sqLiteDatabase.close();
            }
        }
        return result;
    }

}
