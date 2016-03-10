package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppVoipByeInd
{
	public byte[]	usrNameLen	= new byte[4];
	public byte[]	usrName		= new byte[28];
	
	public byte[]   peerNameLen  = new byte[4];
	public byte[]   peerName     = new byte[28];

	public AgtAppVoipByeInd(byte[] byteInput)
	{
		System.arraycopy(byteInput, 0, usrNameLen, 0, 4);
		System.arraycopy(byteInput, 4, usrName, 0, 28);
		
		System.arraycopy(byteInput, 32, peerNameLen, 0, 4);
        System.arraycopy(byteInput, 36, peerName, 0, 28);
	}

	public int getUsrNameLen()
	{
		return ByteUtil.getInt(usrNameLen);
	}

	public String getUsrName()
	{
		return new String(usrName).trim();
	}
	
	public int getPeerNameLen()
    {
        return ByteUtil.getInt(peerNameLen);
    }

    public String getPeerName()
    {
        return new String(peerName).trim();
    }
}
