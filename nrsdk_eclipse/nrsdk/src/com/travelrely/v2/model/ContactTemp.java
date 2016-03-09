package com.travelrely.v2.model;

public class ContactTemp
{
    private String name;

    private String num;

    private long raw_contact_id;

    private String tag;

    private String sort_key;
    
    private String token;

    private String new_num;

    private String head_portrait;

    private String nick_name;

    private String valid_time;
    
    private String time_difference;

    private int presence;

    private int regist;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNum()
    {
        return num;
    }

    public void setNum(String num)
    {
        this.num = num;
    }

    public long getRawContactId()
    {
        return raw_contact_id;
    }

    public void setRawContactId(long raw_contact_id)
    {
        this.raw_contact_id = raw_contact_id;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public String getSortKey()
    {
        return sort_key;
    }

    public void setSortKey(String sort_key)
    {
        this.sort_key = sort_key;
    }

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

    public void setNewNum(String new_num)
    {
        this.new_num = new_num;
    }

    public String getHeadPortrait()
    {
        return head_portrait;
    }

    public void setHeadPortrait(String head_portrait)
    {
        this.head_portrait = head_portrait;
    }

    public String getNickName()
    {
        return nick_name;
    }

    public void setNickName(String nick_name)
    {
        this.nick_name = nick_name;
    }

    public String getValidTime()
    {
        return valid_time;
    }

    public void setValidTime(String valid_time)
    {
        this.valid_time = valid_time;
    }

    public String getTimeDifference()
    {
        return time_difference;
    }

    public void setTimeDifference(String time_difference)
    {
        this.time_difference = time_difference;
    }

    public int getPresence()
    {
        return presence;
    }

    public void setPresence(int presence)
    {
        this.presence = presence;
    }

    public int getRegist()
    {
        return regist;
    }

    public void setRegist(int regist)
    {
        this.regist = regist;
    }
}
