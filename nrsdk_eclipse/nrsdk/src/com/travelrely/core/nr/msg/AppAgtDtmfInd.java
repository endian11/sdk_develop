package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AppAgtDtmfInd
{
	public int			cmdCode		= MsgId.APP_AGENT_DTMF_IND;
	public int			contentLen	= 34;
	public OctArray28_s	username;
	public char 			key;

	public AppAgtDtmfInd(String username, char key)
	{
		this.username = new OctArray28_s(username);
		this.key = key;
	}
	
    public AppAgtDtmfInd(byte[] in)
    {
        this.username = new OctArray28_s(ByteUtil.subArray(in, 8, 32));
        this.key = ByteUtil.getChar(ByteUtil.subArray(in, 40, 2));
    }

	public byte[] toByte()
	{
		// 不指定编码，默认ascii
		byte[] rtByte = new byte[contentLen];
		System.arraycopy(username.toByte(), 0, rtByte, 0, 32);
		return rtByte;
	}

	public byte[] toMsg()
	{
		byte[] rtmsg = new byte[contentLen + 8];
		System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
		System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);
		System.arraycopy(toByte(), 0, rtmsg, 8, 32);
		System.arraycopy(ByteUtil.getBytes(key), 0, rtmsg, 40, 1);
		return rtmsg;
	}
}
