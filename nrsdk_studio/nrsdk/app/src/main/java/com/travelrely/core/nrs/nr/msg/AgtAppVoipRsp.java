package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppVoipRsp
{
	public byte[]	usrNameLen	= new byte[4];
	public byte[]	usrName		= new byte[28];
	
	public byte[]	rslt		= new byte[4];

	public AgtAppVoipRsp(byte[] byteInput)
	{
		System.arraycopy(byteInput, 8, usrNameLen, 0, 4);
		System.arraycopy(byteInput, 12, usrName, 0, 28);
		
		System.arraycopy(byteInput, 40, rslt, 0, 4);
	}

	public int getUsrNameLen()
	{
		return ByteUtil.getInt(usrNameLen);
	}

	public String getUsrName()
	{
		return new String(usrName).trim();
	}
	
	public int getRslt()
	{
		return ByteUtil.getInt(rslt);
	}
}
