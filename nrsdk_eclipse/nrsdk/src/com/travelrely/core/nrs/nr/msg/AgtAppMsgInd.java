package com.travelrely.core.nrs.nr.msg;

import java.io.UnsupportedEncodingException;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppMsgInd
{
	public byte[]	usrNameLen	= new byte[4];
	public byte[]	usrName		= new byte[28];
	public byte[]	senderLen	= new byte[4];
	public byte[]	sender		= new byte[28];
	public byte[]	msgLen		= new byte[4];
	public byte[]	msg;

	public AgtAppMsgInd(byte[] byteInput)
	{
		System.arraycopy(byteInput, 8, usrNameLen, 0, 4);
		System.arraycopy(byteInput, 12, usrName, 0, 28);
		System.arraycopy(byteInput, 40, senderLen, 0, 4);
		System.arraycopy(byteInput, 44, sender, 0, 28);
		System.arraycopy(byteInput, 72, msgLen, 0, 4);
		
		int iMsgLen = ByteUtil.getInt(msgLen);
		msg = new byte[iMsgLen];
		System.arraycopy(byteInput, 76, msg, 0, iMsgLen);
	}

	public int getUsrNameLen()
	{
		return ByteUtil.getInt(usrNameLen);
	}

	public String getUsrName()
	{
		return new String(usrName).trim();
	}
	
	public int getSenderLen()
	{
		return ByteUtil.getInt(senderLen);
	}

	public String getSender()
	{
		return new String(sender).trim();
	}
	
	public String getMsg()
	{
	    String s = "";
		try
        {
            s =  new String(msg, "UTF-16BE").trim();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
		
		return s;
	}
}
