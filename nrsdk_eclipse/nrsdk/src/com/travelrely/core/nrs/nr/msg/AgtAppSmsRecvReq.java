package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppSmsRecvReq
{
	public OctArray28_s usernameArray28_s;
	public OctArray28	senderArray28_s;
	public CharString_s	messageCharString_s;
	
	public AgtAppSmsRecvReq(byte[] byteInput)
	{
		usernameArray28_s = new OctArray28_s(ByteUtil.subArray(byteInput, 8, 32));
		senderArray28_s = new OctArray28(ByteUtil.subArray(byteInput, 40, 32));
		messageCharString_s = new CharString_s(ByteUtil.subArray(byteInput, 72, byteInput.length-72));
	}
	
	public String getSms()
	{
		return new String(messageCharString_s.data);
	}
	
	public byte[] getSmsByte()
	{
        return messageCharString_s.data;
    }
	
	public String getSenderArray28_s()
	{
		return new String(senderArray28_s.data);
	}
	
}
