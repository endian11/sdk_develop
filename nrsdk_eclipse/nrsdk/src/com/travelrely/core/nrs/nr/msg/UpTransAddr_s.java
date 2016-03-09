package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class UpTransAddr_s {
	
	public byte[] 	ip=new byte[32];
	public int 	port;
	
	public byte[] toByte(){
		byte[] rtbyte=new byte[36];
		System.arraycopy(ip, 0, rtbyte, 0, 32);
		System.arraycopy(ByteUtil.getBytes(port), 0, rtbyte, 32, 4);
		return rtbyte;
	}
	
	public UpTransAddr_s(int portInput, byte[] ipInput){//数据初始化
		port=portInput;
		System.arraycopy(ipInput, 0, ip, 0, 32);
	}
	
	public UpTransAddr_s(byte[] dataInput){
		System.arraycopy(dataInput, 0 , ip, 0, 32);
		port = ByteUtil.getInt(ByteUtil.subArray(dataInput, 32, 4));
	}
}
