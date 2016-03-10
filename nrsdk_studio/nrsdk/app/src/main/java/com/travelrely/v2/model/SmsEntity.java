package com.travelrely.v2.model;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;


public class SmsEntity implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int TYPE_TEXT = 0;// 文本
    
    public static final int DIR_OUTGOING = 1;
    public static final int DIR_INCOMING = 2;
    
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_SUCC = 2;
    public static final int STATUS_FAILED = 3;
    
    public static final int STATUS_UNREAD = 4;
    public static final int STATUS_READ = 5;

    int id; // 短消息序号
    String thread_id; // 对话的序号（conversation） ，同一发信人的id相同
    
    String address; // 收件人号码
    String person; // 发件人
    String nick_name; // 收件人姓名昵称
    
    String date; // 日期
    
    int protocol; // 协议 分为： 0 SMS_RPOTO, 1 MMS_PROTO
    
    int read; // 是否阅读 =0 未读； =1 已读
    int status; // 状态 -1接收，0 complete, 64 pending, 128 failed

    String reply_path_present; //
    String subject; // 主题
    
    String body; // 短消息内容
    String service_center; // 短信服务中心号码编号
    int dir; // 消息类型 1=to发送者 2=from接受者
    int bodyType; // 消息内容类型 0=文本

    public int getBody_type()
    {
        return bodyType;
    }

    public void setBodyType(int body_type)
    {
        this.bodyType = body_type;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getDir()
    {
        return dir;
    }

    public void setDir(int dir)
    {
        this.dir = dir;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getThread_id()
    {
        return thread_id;
    }

    public void setThread_id(String thread_id)
    {
        this.thread_id = thread_id;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPerson()
    {
        return person;
    }

    public void setPerson(String person)
    {
        this.person = person;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public int getProtocol()
    {
        return protocol;
    }

    public void setProtocol(int protocol)
    {
        this.protocol = protocol;
    }

    public int isRead()
    {
        return read;
    }

    public void setRead(int read)
    {
        this.read = read;
    }

    public int getRead()
    {
        return read;
    }

    public String getReply_path_present()
    {
        return reply_path_present;
    }

    public void setReply_path_present(String reply_path_present)
    {
        this.reply_path_present = reply_path_present;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getService_center()
    {
        return service_center;
    }

    public void setService_center(String service_center)
    {
        this.service_center = service_center;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public String getNickName()
    {
        return nick_name;
    }

    public void setNickName(String nick_name)
    {
        this.nick_name = nick_name;
    }

    public ContentValues getContentValues()
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put("thread_id", thread_id);
        contentValues.put("address", address);
        contentValues.put("person", person);
        contentValues.put("nick_name", nick_name);
        contentValues.put("protocol", protocol);
        contentValues.put("read", read);
        contentValues.put("status", status);
        contentValues.put("reply_path_present", reply_path_present);
        contentValues.put("subject", subject);
        contentValues.put("body", body);
        contentValues.put("service_center", service_center);
        contentValues.put("msg_type", dir);
        contentValues.put("body_type", bodyType);
        contentValues.put("date", date);

        return contentValues;
    }

    public void setCursorValues(Cursor c)
    {
        id = c.getInt(c.getColumnIndex("id"));
        thread_id = c.getString(c.getColumnIndex("thread_id"));
        address = c.getString(c.getColumnIndex("address"));
        person = c.getString(c.getColumnIndex("person"));
        nick_name = c.getString(c.getColumnIndex("nick_name"));
        protocol = c.getInt(c.getColumnIndex("protocol"));
        read = c.getInt(c.getColumnIndex("read"));
        status = c.getInt(c.getColumnIndex("status"));
        reply_path_present = c
                .getString(c.getColumnIndex("reply_path_present"));
        subject = c.getString(c.getColumnIndex("subject"));
        body = c.getString(c.getColumnIndex("body"));
        service_center = c.getString(c.getColumnIndex("service_center"));
        dir = c.getInt(c.getColumnIndex("msg_type"));
        bodyType = c.getInt(c.getColumnIndex("body_type"));
        date = c.getString(c.getColumnIndex("date"));
    }

    public boolean isText()
    {
        if (bodyType == TYPE_TEXT)
        {
            return true;
        }
        return false;
    }

}
