package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppLauInd
{
	public byte[]	usrNameLen	= new byte[4];
	public byte[]	usrName		= new byte[28];

	public AgtAppLauInd(byte[] byteInput)
	{
		System.arraycopy(byteInput, 0, usrNameLen, 0, 4);
		System.arraycopy(byteInput, 4, usrName, 0, 28);
	}

	public int getUsrNameLen()
	{
		return ByteUtil.getInt(usrNameLen);
	}

	public String getUsrName()
	{
		return new String(usrName).trim();
	}
}
