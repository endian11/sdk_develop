package com.travelrely.core.nrs.nr.msg;

import java.io.UnsupportedEncodingException;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppSysInfoInd
{
    byte[] id = new byte[4];
    byte[] len = new byte[4];
    byte[] usrLen = new byte[4];
    byte[] usrName = new byte[28];
    byte[] msgLen = new byte[4];
    byte[] msg;
	
	public AgtAppSysInfoInd(byte[] byteInput)
	{		
		msgLen = ByteUtil.subArray(byteInput, 40, 4);
	    int iMsgLen = ByteUtil.getInt(msgLen);
	    msg = ByteUtil.subArray(byteInput, 44, iMsgLen);
	}
	
	public String getMsg()
	{
        String strMsg = "";
        try
        {
            strMsg = new String(msg, "UTF-16BE");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        return strMsg;
    }
	
	public byte[] getMsgByte()
	{
        return msg;
    }	
}
