package com.travelrely.core.nr.msg;

import java.io.UnsupportedEncodingException;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppErrInd
{
	public OctArray28_s usernameArray28_s;
	public CharString_s	messageCharString_s;
	
	public AgtAppErrInd(byte[] byteInput)
	{
		usernameArray28_s = new OctArray28_s(ByteUtil.subArray(byteInput, 8, 32));
		messageCharString_s = new CharString_s(ByteUtil.subArray(byteInput, 40, byteInput.length-40));
	}
	
	public String getErrMsg()
	{
        String strMsg = "";
        try
        {
            strMsg = new String(messageCharString_s.data, "UTF-16BE");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        return strMsg;
    }
	
	public byte[] getErrMsgByte()
	{
        return messageCharString_s.data;
    }	
}
