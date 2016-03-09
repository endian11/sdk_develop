package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class OctArray28_s {
	public int octcnt;
	public byte[] data=new byte[28];
	
	public byte[] toByte(){
		byte[] rtbyte=new byte[32];
		System.arraycopy(ByteUtil.getBytes(octcnt), 0, rtbyte, 0, 4);
		System.arraycopy(data, 0, rtbyte, 4, 28);
		return rtbyte;
	}
	
	public OctArray28_s(int octcntInput, byte[] dataInput){//数据初始化
		octcnt=octcntInput;
		System.arraycopy(dataInput, 0, data, 0, octcntInput);
	}
	
	public OctArray28_s(String stringInput){//字符串初始化
		octcnt=stringInput.length();
		byte[] byteTmp=stringInput.getBytes();
		System.arraycopy(byteTmp, 0, data, 0, byteTmp.length);
	}
	
	public OctArray28_s(byte[] dataInput){
		octcnt=ByteUtil.getInt(ByteUtil.subArray(dataInput, 0, 4));
		System.arraycopy(dataInput, 0, data, 0, 16);
	}
	
	public int stringLeng(){
		return octcnt;
	}
}
