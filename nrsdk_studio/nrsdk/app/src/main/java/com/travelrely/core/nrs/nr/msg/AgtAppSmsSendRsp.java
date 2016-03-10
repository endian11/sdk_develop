package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppSmsSendRsp
{
	public OctArray28_s	usernameArray28_s;
	public int			result;

	public AgtAppSmsSendRsp(byte[] byteInput)
	{
		usernameArray28_s = new OctArray28_s(
				ByteUtil.subArray(byteInput, 8, 32));
		result = ByteUtil.getInt(ByteUtil.subArray(byteInput, 40, 4));
	}
}
