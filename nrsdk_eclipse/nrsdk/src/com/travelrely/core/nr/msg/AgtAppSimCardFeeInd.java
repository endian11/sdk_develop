package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppSimCardFeeInd
{
	public byte[]	titleLen	= new byte[4];
	public byte[]	title		= new byte[36];
	public byte[]	msgLen		= new byte[4];
	public byte[]	msg;

	public AgtAppSimCardFeeInd(byte[] byteInput)
	{
		System.arraycopy(byteInput, 8, titleLen, 0, 4);
		System.arraycopy(byteInput, 12, title, 0, 36);
		System.arraycopy(byteInput, 48, msgLen, 0, 4);
		
		int iMsgLen = ByteUtil.getInt(msgLen);
		msg = new byte[iMsgLen];
		System.arraycopy(byteInput, 52, msg, 0, iMsgLen);
	}
	
	public int getTitleLen()
	{
		return ByteUtil.getInt(titleLen);
	}

	public String getTitle()
	{
	    byte[] t = ByteUtil.subArray(title, 0, getTitleLen());
		return new String(t).trim();
	}
	
	public String getMsg()
	{		
		return new String(msg);
	}
}
