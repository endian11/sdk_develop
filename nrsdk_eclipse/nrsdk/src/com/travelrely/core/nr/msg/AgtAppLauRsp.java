package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppLauRsp {
	public String 	usernameString;
	public int 		result;
	
	public AgtAppLauRsp(byte[] byteInput) {
		usernameString=new String(ByteUtil.subArray(byteInput, 12, 28));
		result=ByteUtil.getInt(ByteUtil.subArray(byteInput, 40, 4));
	}
}
