package com.travelrely.core.nrs.nr.util;

import android.util.Log;

public class RingBuffer4Play {//环形缓冲
	private static final RingBuffer4Play Instance=new RingBuffer4Play(10000);
	volatile private memUnit[] memArray=null;
	volatile private int rPtr;
	volatile private int wPtr;
	
	public RingBuffer4Play(int memunitCount) {
		// TODO Auto-generated constructor stub
		init(memunitCount);
		Log.e("","ring buffer init!!");
	}
	public static RingBuffer4Play getInstance(){
		return Instance;
	}
	
	public boolean init(int memunitCount){//只能初始化一次
		rPtr=0;
		wPtr=0;
		if (memArray==null) {
			memArray=new memUnit[memunitCount];
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean writeMemunit(memUnit memUnitInput) {
		int nNextWPtr=(wPtr+1)%memArray.length;
		if (rPtr==nNextWPtr) {
			return false;
		}else {
			memArray[wPtr]=memUnitInput;
			wPtr=nNextWPtr;		
			return true;
		}
	}	
	
	public readMemunit readMemunit(){
		int nNextRPtr=(rPtr+1)%memArray.length;
		if (rPtr==wPtr) {
			return new readMemunit(null, false);
		}else {
			readMemunit rtReadMemunit=new readMemunit(memArray[rPtr],true);
			rPtr=nNextRPtr;
			return rtReadMemunit;
		}
	}
	
	public void clean(){
		//init(memunitCount);
		//Log.e("","ring buffer init!!");
		rPtr=0;
		wPtr=0;		
	}
}
