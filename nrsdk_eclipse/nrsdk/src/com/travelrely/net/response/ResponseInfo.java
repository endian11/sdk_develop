package com.travelrely.net.response;

import java.io.Serializable;
//import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

public class ResponseInfo extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Override
    public String toString()
    {
        return "BaseResponse [version=" + version + ", charset=" + charset
                + ", ret=" + ret + ", errorcode=" + errorcode + ", msg=" + msg
                + ", toString()=" + super.toString() + "]";
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCharset()
    {
        return charset;
    }

    public void setCharset(String charset)
    {
        this.charset = charset;
    }

    public int getRet()
    {
        return ret;
    }

    public void setRet(int ret)
    {
        this.ret = ret;
    }

    public int getErrCode()
    {
        return errorcode;
    }

    public void setErrCode(int errorcode)
    {
        this.errorcode = errorcode;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getUpdateflag()
    {
        return updateflag;
    }

    public void setUpdateflag(String updateflag)
    {
        this.updateflag = updateflag;
    }

    private String version;

    private String charset;

    private int ret;

    private int errorcode;

    private String msg;

    private String updateflag;

    @Override
    public void setValue(JSONObject jsonObject)
    {
        this.version = jsonObject.optString("version");
        this.charset = jsonObject.optString("charset");
        this.ret = jsonObject.optInt("ret");
        this.errorcode = jsonObject.optInt("errcode");
        this.msg = jsonObject.optString("msg");
        this.updateflag = jsonObject.optString("updateflag");
    }

    public boolean isSuccess()
    {
        if (ret == 0 && errorcode == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isRet()
    {
        return ret == 0;
    }
}