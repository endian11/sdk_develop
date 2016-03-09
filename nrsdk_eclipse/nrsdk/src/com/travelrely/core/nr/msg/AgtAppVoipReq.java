package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppVoipReq
{
	public byte[]	usrNameLen	= new byte[4];
	public byte[]	usrName		= new byte[28];
	
	public byte[]	callerLen	= new byte[4];
	public byte[]	caller		= new byte[28];
	
	public byte[]	ip			= new byte[32];
	public byte[]	port		= new byte[4];

	public AgtAppVoipReq(byte[] byteInput)
	{
		System.arraycopy(byteInput, 0, usrNameLen, 0, 4);
		System.arraycopy(byteInput, 4, usrName, 0, 28);
		
		System.arraycopy(byteInput, 32, ip, 0, 32);
		System.arraycopy(byteInput, 64, port, 0, 4);
		
	    System.arraycopy(byteInput, 68, callerLen, 0, 4);
	    System.arraycopy(byteInput, 72, caller, 0, 28);
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
