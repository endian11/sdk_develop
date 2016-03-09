package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppGpnsInd {
	public int  	octcnt;
	public String 	usernameString;
	public int 		type;
	
	public AgtAppGpnsInd(byte[] byteInput) {
		octcnt=ByteUtil.getInt(ByteUtil.subArray(byteInput, 8, 4));
		usernameString=new String(ByteUtil.subArray(byteInput, 12, octcnt));
		type=ByteUtil.getInt(ByteUtil.subArray(byteInput, 40, 4));
	}
}
