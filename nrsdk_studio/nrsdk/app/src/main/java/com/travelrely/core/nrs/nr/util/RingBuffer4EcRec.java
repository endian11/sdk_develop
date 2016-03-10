package com.travelrely.core.nrs.nr.util;

import android.util.Log;

public class RingBuffer4EcRec {//环形缓冲
	private static final RingBuffer4EcRec Instance=new RingBuffer4EcRec(10000);
	volatile private memUnit[] memArray=null;
	volatile private int rPtr;
	volatile private int wPtr;
	volatile public long lstartTimeStamp;
	volatile public int nSize;
	public RingBuffer4EcRec(int memunitCount) {
		// TODO Auto-generated constructor stub
		init(memunitCount);
		Log.e("","ring buffer init!!");
	}
	public static RingBuffer4EcRec getInstance(){
		return Instance;
	}
	
	public boolean init(int memunitCount){//只能初始化一次
		rPtr=0;
		wPtr=0;
		if (memArray==null) {
			memArray=new memUnit[memunitCount];
			lstartTimeStamp = 0;
			nSize=0;
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
			if (lstartTimeStamp==0) {
				lstartTimeStamp = memUnitInput.timeStamp;
			}
			memArray[wPtr]=memUnitInput;
			nSize+=1;
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
			nSize-=1;
			rPtr=nNextRPtr;
			return rtReadMemunit;
		}
	}
	
	public long getStartTimeStamp(){
		return lstartTimeStamp;
	}
	
	public int getSize(){
		return nSize;
	}
	public void clean(){
		//init(memunitCount);
		//Log.e("","ring buffer init!!");
		rPtr=0;
		wPtr=0;	
		lstartTimeStamp = 0;
		nSize=0;
	}
}
