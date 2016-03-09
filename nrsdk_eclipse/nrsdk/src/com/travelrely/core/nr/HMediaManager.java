package com.travelrely.core.nr;

import android.content.Context;
import jni.media.HMedia;
import jni.media.RtcMediaJava;

public class HMediaManager {
	
	private static HMedia mMedia = null;
	public static int mCall = 0;
	public static int loudspeaker_st = 0;
	
	public static void init(Context context) {
		if (mMedia == null) {
        	mMedia = new RtcMediaJava(context);
		}
	}
	
	public static HMedia getHMedia() {
		return mMedia;
	}
	
}
