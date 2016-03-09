package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppCallingRsp
{
	public String			usernameString;
	public int				result;

	public AgtAppCallingRsp(byte[] byteInput)
	{
		usernameString = new String(ByteUtil.subArray(byteInput, 12, 28));
		result = ByteUtil.getInt(ByteUtil.subArray(byteInput, 40, 4));
	}
}