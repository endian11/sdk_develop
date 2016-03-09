package com.travelrely.core.nr.util;

public class memUnit {
	public byte[] 	data=null;
	public long 	timeStamp=0;
	public memUnit(byte[] byteInput) {
		data=byteInput;
	}
	public memUnit(byte[] byteInput,long timeStampInput) {
		data=byteInput;
		timeStamp=timeStampInput;
	}
	public memUnit(){
		
	}
}
