package com.travelrely.app.view;

import com.travelrely.core.nrs.App;

import android.os.Handler;


public class UIUtils {
	/** 获取主线程的handler */
	public static Handler getHandler() {
		return App.getMainThreadHandler();
	}
	
	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}
	
	//判断当前的线程是不是在主线程 
		public static boolean isRunInMainThread() {
			return android.os.Process.myTid() == getMainThreadId();
		}
		
		public static long getMainThreadId() {
			return App.getMainThreadId();
		}
		/** 在主线程执行runnable */
		public static boolean post(Runnable runnable) {
			return getHandler().post(runnable);
		}
}
