package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppUserVerifyRsp
{
    public byte[] userLen;
	public byte[] user;
	
    public byte[] recvPhoneLen;
    public byte[] recvPhone;
    
    public byte[] rslt;
	
	public AgtAppUserVerifyRsp(byte[] byteInput)
	{
	    userLen = ByteUtil.subArray(byteInput, 8, 4);
	    user = ByteUtil.subArray(byteInput, 12, 28);
	    recvPhoneLen = ByteUtil.subArray(byteInput, 40, 4);
	    recvPhone = ByteUtil.subArray(byteInput, 44, 28);
	    rslt = ByteUtil.subArray(byteInput, 72, 4);
	}

	public String getRecvPhone()
	{
	    int len = ByteUtil.getInt(recvPhoneLen);
        return new String(ByteUtil.subArray(recvPhone, 0, len));
    }
	
	public int getRslt()
	{        
	    return ByteUtil.getInt(rslt);
	}
}
