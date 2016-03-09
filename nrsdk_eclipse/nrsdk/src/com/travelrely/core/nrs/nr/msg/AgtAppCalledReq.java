package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppCalledReq
{
	public byte[]	usrNameLen	= new byte[4];
	public byte[]	usrName		= new byte[28];
	
	public byte[]	callerLen	= new byte[4];
	public byte[]	caller		= new byte[28];
	
	public byte[]	ip			= new byte[32];
	public byte[]	port		= new byte[4];

	public AgtAppCalledReq(byte[] byteInput)
	{
		System.arraycopy(byteInput, 8, usrNameLen, 0, 4);
		System.arraycopy(byteInput, 12, usrName, 0, 28);
		
		System.arraycopy(byteInput, 40, callerLen, 0, 4);
		System.arraycopy(byteInput, 44, caller, 0, 28);
		
		System.arraycopy(byteInput, 72, ip, 0, 32);
		System.arraycopy(byteInput, 104, port, 0, 4);
	}

	public int getUsrNameLen()
	{
		return ByteUtil.getInt(usrNameLen);
	}

	public String getUsrName()
	{
		return new String(usrName).trim();
	}
	
	public int getCallerLen()
	{
		return ByteUtil.getInt(callerLen);
	}

	public String getCaller()
	{
		return new String(caller).trim();
	}

	public String getIp()
	{
		return new String(ip).trim();
	}

	public int getPort()
	{
		return ByteUtil.getInt(port);
	}
}