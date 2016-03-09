package com.travelrely.core.nr.mt100;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Log;

public class VersionUpgrade {
	private static final String TAG = "VersionUpgrade";
	
	private static class HexRecordInfo {
		public int extractRecord(String record) {
			String str = record.substring(7, 9);
			type = (byte)Integer.parseInt(str,16);
			//Log.i(TAG,"record:"+record);
			switch(type) {
			case 0x00://Data record
				str = record.substring(1, 3);
				//Log.i(TAG,"record len:"+str);
				len = (byte)Integer.parseInt(str, 16);
				addr = 0;
				str = record.substring(3, 5);
				addr += (Integer.parseInt(str, 16) << 8);
				str = record.substring(5, 7);
				addr += Integer.parseInt(str, 16);
				addr += (segAddr + linearAddr);
				str = record.substring(9, 9+len*2);
				data = Common.HexStringToBytes(str);
				break;
			case 0x01://File end record
				break;
			case 0x02://Ext segment addr record
				str = record.substring(9, 9+2*2);
				segAddr = Integer.parseInt(str, 16) << 4;
				break;
			case 0x04://Ext Linear addr record
				str = record.substring(9, 9+2*2);
				linearAddr = Integer.parseInt(str, 16) << 16;
				break;
			default:
				break;
			}
			return 0;
		}
		public int addr;
		public int segAddr;
		public int linearAddr;
		public byte type;
		public byte len;
		public byte[] data;
	}
	private static HexRecordInfo hri = new HexRecordInfo();
	
	public static void initExtractRecord() {
		hri.segAddr = 0;
		hri.linearAddr = 0;
		hri.addr = 0;
		hri.type = 0;
		hri.len = 0;
		hri.data = null;
	}
	
	public static int extractRecord(String record) {
		return hri.extractRecord(record);
	}
	
	public static byte getRecordType() {
		return hri.type;
	}
	
	public static byte[] getRawData() {
		return hri.data;
	}

	public static int getRecordAddr() {
		return hri.addr;
	}
	
	private static MessageDigest messageDigest = null;
	
	public static int initMD5() {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int updateMD5(byte[] binData) {
		messageDigest.update(binData);
		return 0;
	}
	
	public static byte[] getMD5() {
		return messageDigest.digest();
	}
	
	public static byte[] getMD5(byte[] data) {
		return messageDigest.digest(data);
	}
	
}
