package com.travelrely.core.nr.voice;

public class amrnb {
	public static final int MR475=0;
	public static final int MR515=1;
	public static final int MR59=2;
	public static final int MR67=3;
	public static final int MR74=4;
	public static final int MR795=5;
	public static final int MR102=6;
	public static final int MR122=7;
	public static final int MRDTX=8;
	
	public static int getCodeLen(int amrMode){
		int codeLen=0;
		switch (amrMode) {
		case MR475:
			codeLen=13;
			break;
		case MR515:
			codeLen=14;
			break;
		case MR59:
			codeLen=16;
			break;
		case MR67:
			codeLen=18;
			break;
		case MR74:
			codeLen=20;
			break;
		case MR795:
			codeLen=21;
			break;
		case MR102:
			codeLen=27;
			break;
		case MR122:
			codeLen=32;
			break;
		default:
			break;
		}
		return codeLen;
	}	
}
