package com.travelrely.v2.Rent.msg;

import com.travelrely.core.nr.util.ByteUtil;

import android.text.TextUtils;

public class AgtRentCalledReq
{
    public byte[]   msgId  = new byte[4];
    public byte[]   msgLen  = new byte[4];
    
	public byte[]	usrNameLen	= new byte[4];
	public byte[]	usrName		= new byte[28];
	
	public byte[]	callerLen	= new byte[4];
	public byte[]	caller		= new byte[28];
	
	public byte[]	ip			= new byte[32];
	public byte[]	port		= new byte[4];

	public AgtRentCalledReq(byte[] byteInput)
	{
	    System.arraycopy(byteInput, 0, msgId, 0, 4);
	    System.arraycopy(byteInput, 4, msgLen, 0, 4);
	    
		System.arraycopy(byteInput, 8, usrNameLen, 0, 4);
		System.arraycopy(byteInput, 12, usrName, 0, 28);
		
		System.arraycopy(byteInput, 40, callerLen, 0, 4);
		System.arraycopy(byteInput, 44, caller, 0, 28);
		
		System.arraycopy(byteInput, 72, ip, 0, 32);
		System.arraycopy(byteInput, 104, port, 0, 4);
	}
	
	public AgtRentCalledReq(int iMsgId, String from, String to, String ipStr, int iPort)
	{
        System.arraycopy(ByteUtil.getBytes(iMsgId), 0, msgId, 0, 4);
        System.arraycopy(ByteUtil.getBytes(100), 0, msgLen, 0, 4);
        
        if (!TextUtils.isEmpty(to))
        {
            System.arraycopy(ByteUtil.getBytes(to.length()), 0, usrNameLen, 0, 4);
            System.arraycopy(to.getBytes(), 0, usrName, 0, to.length());
        }
        
        System.arraycopy(ByteUtil.getBytes(from.length()), 0, callerLen, 0, 4);
        System.arraycopy(from.getBytes(), 0, caller, 0, from.length());
        
        System.arraycopy(ipStr.getBytes(), 0, ip, 0, ipStr.length());
        System.arraycopy(ByteUtil.getBytes(iPort), 0, port, 0, 4);
	}
	
    public byte[] toByte()
    {
        byte[] rtByte = new byte[ByteUtil.getInt(msgLen) + 8];
        System.arraycopy(msgId, 0, rtByte, 0, 4);
        System.arraycopy(msgLen, 0, rtByte, 4, 4);
        System.arraycopy(usrNameLen, 0, rtByte, 8, 4);
        System.arraycopy(usrName, 0, rtByte, 12, 28);
        System.arraycopy(callerLen, 0, rtByte, 40, 4);
        System.arraycopy(caller, 0, rtByte, 44, 28);
        System.arraycopy(ip, 0, rtByte, 72, 32);
        System.arraycopy(port, 0, rtByte, 104, 4);

        return rtByte;
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
