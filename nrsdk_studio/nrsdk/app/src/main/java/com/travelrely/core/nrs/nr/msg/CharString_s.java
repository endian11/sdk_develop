package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class CharString_s {
	public  int 	octcnt;
	public  byte[]	data;
	
	public byte[] toByte(){
		byte[] rtbyte=new byte[octcnt+4];
		System.arraycopy(ByteUtil.getBytes(octcnt), 0, rtbyte, 0, 4);
		System.arraycopy(data, 0, rtbyte, 4, octcnt);
		return rtbyte;
	}
	
	public CharString_s(int octcntInput, byte[] dataInput){//数据初始化
		octcnt=octcntInput;
		data=new byte[octcntInput];
		System.arraycopy(dataInput, 0, data, 0, octcntInput);
	}
	
	public CharString_s(String stringInput){//字符串初始化
		octcnt=stringInput.length();
		byte[] byteTmp=stringInput.getBytes();
		data=new byte[byteTmp.length];
		System.arraycopy(byteTmp, 0, data, 0, byteTmp.length);
	}
	
	public CharString_s(byte[] dataInput){
		octcnt=ByteUtil.getInt(ByteUtil.subArray(dataInput, 0, 4));
		data=new byte[octcnt];
		System.arraycopy(dataInput, 4, data, 0, octcnt);
	}
	
	public int getLen(){
		return octcnt + 4;
	}
}
