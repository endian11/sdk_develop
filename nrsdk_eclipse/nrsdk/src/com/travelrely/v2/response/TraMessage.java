
package com.travelrely.v2.response;

import java.io.Serializable;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.travelrely.model.ContactModel;
import com.travelrely.net.response.BaseResponse;
import com.travelrely.v2.util.AESUtils;
import com.travelrely.v2.util.AlarmUtil;
import com.travelrely.v2.util.FileUtil;
import com.travelrely.v2.util.TimeUtil;
import com.travelrely.v2.util.Utils;

public class TraMessage extends BaseResponse implements Serializable, Comparable<TraMessage> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int TYPE_TEXT = 0;// 文本

    public static final int TYPE_LOCATION = 1;// 位置

    public static final int TYPE_FILE = 2;// 文件

    public static final int TYPE_REMIND = 3;// 提醒

    public static final int TYPE_VOICE = 4;// 语音

    public static final int TYPE_TRIP = 5;// 行程

    public static final int TYPE_COLLTION = 6;// 定点集合

    public static final int TYPE_REGISTRATION = 7;// 签到

    public static final int TYPE_REGISTRATION_RESPONSE = 8;// 签到应答

    public static final int TYPE_RADAR = 9;// 位置雷达

    public static final int TYPE_RADAR_RESPONSE = 10;// 位置雷达响应

    public static final int TYPE_CONVENE = 11;// 召集

    public static final int TYPE_VOIP_MSG = 99;// voip消息

    public static final String EXT_TYPE_JPG = "jpeg";

    public static final String EXT_TYPE_MP3 = "mp3";
    public static final String EXT_TYPE_AMR = "amr";

    public static final int GROUP_LEADER = 1;// 群主
    public static final int GROUP_MWMBER = 2;// 群成员

    public static final String SEND_MSG_CODE_OK = "0";// 发送消息成功
    public static final String SEND_MSG_CODE_NO = "1";// 发送消息失败
    public static final String SEND_MSG_CODE_ERROR = "2";// 发送消息失败，网络异常

    // 当有消息时对它进行绑定
    public ContactModel contactModel;

    int id;

    String from;

    int from_type;

    String to;

    int to_type;

    String time;

    int priority;

    int type;

    String content;

    String decryptContent;

    String[] decryptReception;

    String nick_name;

    String head_portrait;// 个人头像文件名

    String from_head_portrait;// 对方头像文件名

    String head_portrait_url;

    String group_name;

    String group_head_portrait;// 群组成员头像名

    String group_id;

    int alarm_type;// 闹钟状态 0：未接收 1：已接收 2：过时作废 3:删除

    int alarm_count;// 多个闹钟状态

    int alarm_on_off;// 闹钟开关 1：开 0：关

    int reception_on_off;// 签到状态 0：未签到 1：签到 2：已过期

    String code;// 发送消息返回状态码

    public String voiceTimeLength = null;

    public float voiceLength = -1;

    public boolean isPlaying = false;
    
    long contact_id;

    public long getContact_id() {
        return contact_id;
    }

    public void setContact_id(long contact_id) {
        this.contact_id = contact_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getReception_on_off() {
        return reception_on_off;
    }

    public void setReception_on_off(int reception_on_off) {
        this.reception_on_off = reception_on_off;
    }

    public String getFrom_head_portrait() {
        return from_head_portrait;
    }

    public void setFrom_head_portrait(String from_head_portrait) {
        this.from_head_portrait = from_head_portrait;
    }

    public int getAlarm_on_off() {
        return alarm_on_off;
    }

    public void setAlarm_on_off(int alarm_on_off) {
        this.alarm_on_off = alarm_on_off;
    }

    public int getAlarm_count() {
        return alarm_count;
    }

    public void setAlarm_count(int alarm_count) {
        this.alarm_count = alarm_count;
    }

    public int getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(int alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getHead_portrait_url() {
        return head_portrait_url;
    }

    public void setHead_portrait_url(String head_portrait_url) {
        this.head_portrait_url = head_portrait_url;
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

    // 以下是为数据库
    int isRead;

    int status;

    /**
     * 正在发送附件
     */
    public static final int status_sending_ext = 0;

    /**
     * 正在发送文本
     */
    public static final int status_sending_text = 1;

    /**
     * 发送时候网络错误
     */
    public static final int status_send_ext_error = 2;

    public static final int status_send_text_error = 3;

    public static final int status_send_ext_success = 4;

    public static final int status_send_text_success = 5;

    public static final int status_downloading_ext = 6;

    public static final int status_receiving_text = 7;

    public static final int status_downloading_ext_error = 8;

    public static final int status_received_text_error = 9;

    public static final int status_received_text_success = 10;

    public static final int status_downloading_ext_success = 11;

    String ext_type;

    public ContactModel getContactModel() {
        return contactModel;
    }

    public void setContactModel(ContactModel contactModel) {
        this.contactModel = contactModel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStore_type() {
        return store_type;
    }

    public void setStore_type(int store_type) {
        this.store_type = store_type;
    }

    String url = "";

    /**
     * 为了listview刷新速度快
     */
    public Bitmap smallBitmap;

    public Bitmap locationBitmap;

    public Bitmap headBitmap;

    /**
     * 0,sd卡 1,系统内存
     */
    int store_type = 0;

    public Bitmap getSmallBitmap(Context context, String path) {
        if (smallBitmap == null || smallBitmap.isRecycled()) {

            FileUtil fileUtil = new FileUtil(context);
            Bitmap bitmap = fileUtil.readImg(this);
            Bitmap small = Utils.generateSmallBitmap(bitmap, path);

            if (small == bitmap) {
            } else {
                bitmap.recycle();
            }

            smallBitmap = small;
        }
        return smallBitmap;
    }

    public Bitmap getHeadBitmap(Context context) {
        if (headBitmap == null || headBitmap.isRecycled()) {

            FileUtil fileUtil = new FileUtil(context);
            Bitmap bitmap = fileUtil.readHeadImg(this);
            Bitmap small = Utils.headBitmap(bitmap);

            if (small == bitmap) {
            } else {
                bitmap.recycle();
            }

            headBitmap = small;
        }
        return headBitmap;
    }

    public String getExt_type() {
        return ext_type;
    }

    public void setExt_type(String ext_type) {
        this.ext_type = ext_type;
    }

    /**
     * 登录人和对方聊天的电话号
     */
    String width_user;

    /**
     * 登录人的电话号
     */
    String user_name;

    int msg_type;

    int unReadCount;// 消息未读条数

    int msg_id;

    int is_nickname; // 0:不显示昵称 1：显示昵称

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getFrom_type() {
        return from_type;
    }

    public void setFrom_type(int from_type) {
        this.from_type = from_type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTo_type() {
        return to_type;
    }

    public void setTo_type(int to_type) {
        this.to_type = to_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIs_nickname() {
        return is_nickname;
    }

    public void setIs_nickname(int is_nickname) {
        this.is_nickname = is_nickname;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    /**
     * @return
     */
    public String getWidth_user() {
        return width_user;
    }

    public void setWidth_user(String width_user) {
        this.width_user = width_user;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);

        String types;

        from = jsonObject.optString("from");

        JSONArray toArray = jsonObject.optJSONArray("to");
        to = toArray.optString(0);
        time = jsonObject.optString("time");
        content = jsonObject.optString("content");

        from_type = jsonObject.optInt("from_type");
        to_type = jsonObject.optInt("to_type");
        priority = jsonObject.optInt("priority");
        type = jsonObject.optInt("type");
        msg_id = jsonObject.optInt("id");
        ext_type = jsonObject.optString("ext_type");
        nick_name = jsonObject.optString("nick_name");
        head_portrait = jsonObject.optString("head_portrait");
        group_name = jsonObject.optString("group_name");
        group_head_portrait = jsonObject.optString("group_head_portrait");
        alarm_count = jsonObject.optInt("alarm_count");
        alarm_on_off = jsonObject.optInt("alarm_on_off");

        if (type == 3) {
            String[] str = AlarmUtil.analysisAlarmMsg(content);
            String s = str[0] + " " + str[1];
            try {
                if (Utils.nowTimeCompare(s, Utils.y_m_d_h_m)) {
                    alarm_type = 0;
                } else {
                    alarm_type = 2;
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        user_name = to;
        width_user = from;
    }

    public void setValue(Cursor c) {
        from = c.getString(c.getColumnIndex("from_"));
        to = c.getString(c.getColumnIndex("to_"));
        time = c.getString(c.getColumnIndex("time"));
        content = c.getString(c.getColumnIndex("content"));
        width_user = c.getString(c.getColumnIndex("width_user"));
        user_name = c.getString(c.getColumnIndex("user_name"));

        from_type = c.getInt(c.getColumnIndex("from_type"));
        to_type = c.getInt(c.getColumnIndex("to_type"));
        priority = c.getInt(c.getColumnIndex("priority"));
        type = c.getInt(c.getColumnIndex("type"));
        id = c.getInt(c.getColumnIndex("id"));
        msg_id = c.getInt(c.getColumnIndex("id"));
        isRead = c.getInt(c.getColumnIndex("isRead"));
        ext_type = c.getString(c.getColumnIndex("ext_type"));
        store_type = c.getInt(c.getColumnIndex("store_type"));
        url = c.getString(c.getColumnIndex("url"));
        nick_name = c.getString(c.getColumnIndex("nick_name"));
        head_portrait = c.getString(c.getColumnIndex("head_portrait"));
        head_portrait_url = c.getString(c.getColumnIndex("head_portrait_url"));
        group_name = c.getString(c.getColumnIndex("group_name"));
        group_head_portrait = c.getString(c.getColumnIndex("group_head_portrait"));
        msg_type = c.getInt(c.getColumnIndex("msg_type"));
        group_id = c.getString(c.getColumnIndex("group_id"));
        alarm_type = c.getInt(c.getColumnIndex("alarm_type"));
        alarm_count = c.getInt(c.getColumnIndex("id"));
        alarm_on_off = c.getInt(c.getColumnIndex("alarm_on_off"));
        from_head_portrait = c.getString(c.getColumnIndex("from_head_portrait"));
        unReadCount = c.getInt(c.getColumnIndex("unReadCount"));
        reception_on_off = c.getInt(c.getColumnIndex("reception_on_off"));
        code = c.getString(c.getColumnIndex("code"));
        is_nickname = c.getInt(c.getColumnIndex("is_nickname"));
        contact_id = c.getInt(c.getColumnIndex("contact_id"));
    }

    public ContentValues generateValues() {
        ContentValues contentValues = new ContentValues();
        // 以下是为数据库
        if (id <= 0) {

        } else {
            contentValues.put("id", id);
        }

        contentValues.put("from_type", from_type);
        contentValues.put("to_type", to_type);
        contentValues.put("time", time);
        contentValues.put("priority", priority);
        contentValues.put("type", type);
        contentValues.put("content", content);
        contentValues.put("isRead", isRead);
        contentValues.put("width_user", width_user);
        contentValues.put("user_name", user_name);
        contentValues.put("msg_id", msg_id);
        contentValues.put("ext_type", ext_type);
        contentValues.put("store_type", store_type);
        contentValues.put("url", url);
        contentValues.put("nick_name", nick_name);
        contentValues.put("head_portrait_url", head_portrait_url);
        contentValues.put("group_id", group_id);
        contentValues.put("group_name", group_name);
        contentValues.put("group_head_portrait", group_head_portrait);
        contentValues.put("msg_type", msg_type);
        contentValues.put("alarm_type", alarm_type);
        contentValues.put("alarm_count", alarm_count);
        contentValues.put("alarm_on_off", alarm_on_off);
        contentValues.put("from_head_portrait", from_head_portrait);
        contentValues.put("unReadCount", unReadCount);
        contentValues.put("reception_on_off", reception_on_off);
        contentValues.put("code", code);
        contentValues.put("is_nickname", is_nickname);
        contentValues.put("contact_id", contact_id);
        

        if (isGroup()) {
            contentValues.put("to_", from);
            contentValues.put("from_", to);
            contentValues.put("group_id", to);
            contentValues.put("head_portrait", group_head_portrait);
        } else {
            contentValues.put("head_portrait", head_portrait);
            if (msg_type == 1) {
                contentValues.put("to_", from);
                contentValues.put("from_", to);
            } else if (msg_type == 2) {
                contentValues.put("to_", to);
                contentValues.put("from_", from);
            }
        }

        return contentValues;
    }

    public JSONObject generateJson() {
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("from", from);
            messageJson.put("from_type", Integer.toString(from_type));
            JSONArray toArray = new JSONArray();
            toArray.put(to);
            messageJson.put("to", toArray);
            messageJson.put("to_type", Integer.toString(to_type));
            messageJson.put("time", time);
            messageJson.put("priority", Integer.toString(priority));
            messageJson.put("type", Integer.toString(type));
            messageJson.put("content", content);
//            nick_name由服务器来加
//            if(TextUtils.isEmpty(nick_name)){
//                messageJson.put("nick_name", to); 
//            }else{
//                messageJson.put("nick_name", nick_name); 
//            }
            
            messageJson.put("group_name", group_name);

            if (type == 2) {
                messageJson.put("ext_type", ext_type);
            } else {
                messageJson.put("ext_type", "");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return messageJson;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", from=" + from + ", from_type=" + from_type + ", to=" + to
                + ", to_type=" + to_type + ", time=" + time + ", priority=" + priority + ", type="
                + type + ", content=" + content + ", nick_name=" + nick_name + ", group_name="
                + group_name + ", head_portrait=" + head_portrait + "]";
    }

    public String getDecryptContent() {

        if (type == 0 || type == 3) {

            if (decryptContent == null) {
                decryptContent = AESUtils.getDecryptString(content);
            }
            return decryptContent;
        } else {
            return content;
        }

    }

    public String getDecryptContentTest() {

        if (type == TYPE_REGISTRATION) {

            if (decryptContent == null) {
                decryptContent = AESUtils.getDecryptString(content);
            }
            return decryptContent;
        } else {
            return content;
        }

    }

    public String getDecryptContentLocation() {

        if (type == 1) {
            if (decryptContent == null) {

                String[] con = AESUtils.getDecryptString(content).split("\t");
                decryptContent = con[2].trim();
            }
            return decryptContent;
        } else {
            return content;
        }
    }

    public String[] getDecryptColltion() {

        if (type == TYPE_COLLTION) {
            if (decryptReception == null) {

                decryptReception = AESUtils.getDecryptString(content).split("\t");
            }
            return decryptReception;
        } else {
            return decryptReception;
        }
    }

    public String[] getDecryptContentReception() {

        if (type == TYPE_REGISTRATION || type == TYPE_REGISTRATION_RESPONSE) {
            if (decryptReception == null) {

                decryptReception = AESUtils.getDecryptString(content).split("\t");
            }
            return decryptReception;
        } else {
            return decryptReception;
        }
    }

    public String[] getDecryptContentConvene() {

        String[] decryptConvene = null;

        if (type == TYPE_CONVENE) {
            decryptConvene = AESUtils.getDecryptString(content).split("\t");
            return decryptConvene;
        } else {
            return decryptConvene;
        }
    }

    @Override
    public int compareTo(TraMessage another) {
        try {
            long thisTime = TimeUtil.getTimeByString(this.getTime(), TimeUtil.dateFormat2);

            long anotherTime = TimeUtil.getTimeByString(another.getTime(), TimeUtil.dateFormat2);

            if (thisTime < anotherTime) {
                return 1;
            } else if (thisTime > anotherTime) {
                return -1;

            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isJPG() {

        if (type != TYPE_FILE) {
            return false;
        }
        if (ext_type == null) {
            return false;
        }
        if (type == TYPE_FILE && ext_type.equals(EXT_TYPE_JPG)) {
            return true;
        }
        return false;
    }

    public boolean isMp3() {
        if (EXT_TYPE_AMR.equals(ext_type) || TYPE_VOICE == type) {
            return true;
        } else if (type == TYPE_FILE && EXT_TYPE_MP3.equals(ext_type)) {
            return true;
        }
        return false;

    }

    public boolean isText() {
        if (type == TYPE_TEXT) {
            return true;
        }
        return false;
    }

    public boolean isLocation() {

        if (type == TYPE_LOCATION) {
            return true;
        }
        return false;
    }

    public boolean isGroup() {
        if (to_type == 1) {
            return true;
        }
        return false;
    }

    public boolean isRemind() {

        if (type == TYPE_REMIND) {
            return true;
        }
        return false;
    }

    public boolean isTrip() {

        if (type == TYPE_TRIP) {
            return true;
        }
        return false;
    }

    public boolean isAlarm() {

        if (type == TYPE_REMIND) {
            return true;
        }
        return false;
    }

    public boolean isVoic() {

        if (type == TYPE_VOICE) {
            return true;
        }
        return false;
    }

    public boolean isRegistration() {

        if (type == TYPE_REGISTRATION) {
            return true;
        }
        return false;
    }

    public boolean isRegistrationResponse() {

        if (type == TYPE_REGISTRATION_RESPONSE) {
            return true;
        }
        return false;
    }

    public boolean isRadarResponse() {

        if (type == TYPE_RADAR_RESPONSE) {
            return true;
        }
        return false;
    }

    public boolean isRadar() {

        if (type == TYPE_RADAR) {
            return true;
        }
        return false;
    }

    public boolean isColltion() {

        if (type == TYPE_COLLTION) {
            return true;
        }
        return false;
    }

    public boolean isConvene() {

        if (type == TYPE_CONVENE) {
            return true;
        }
        return false;
    }

    public boolean isVoipMsg() {

        if (type == TYPE_VOIP_MSG) {
            return true;
        }
        return false;
    }

    double latiude;

    double longtitude;

    public double getLatiude() {
        return latiude;
    }

    public void setLatiude(double latiude) {
        this.latiude = latiude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void destory() {
        recyleIfNeed(locationBitmap);
        recyleIfNeed(smallBitmap);
        recyleIfNeed(headBitmap);
    }

    private void recyleIfNeed(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

    }

    public static interface OnImgDownloadListener {

        public void onDownLoading();

        public void onDownLoaded(TraMessage message, Bitmap bitmap);

        public void replaceLocationgBitmap(TraMessage message, Bitmap bitmap);
    }

}
