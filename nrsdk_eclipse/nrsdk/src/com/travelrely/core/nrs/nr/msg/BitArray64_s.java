package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class BitArray64_s {
	private int 	bitcnt;
	private byte[] 	data=new byte[8];
	
	public byte[] toByte(){
		byte[] rtbyte=new byte[12];
		System.arraycopy(ByteUtil.getBytes(bitcnt), 0, rtbyte, 0, 4);
		System.arraycopy(data, 0, rtbyte, 4, 8);
		return rtbyte;
	}
	
	public BitArray64_s(int bitcntInput, byte[] dataInput){
		bitcnt=bitcntInput;
		System.arraycopy(dataInput, 0, data, 0, 8);
	}
	
	public BitArray64_s(byte[] dataInput){
		bitcnt=ByteUtil.getInt(ByteUtil.subArray(dataInput, 0, 4));
		System.arraycopy(ByteUtil.subArray(dataInput,4,8), 0, data, 0, 8);
	}
}
