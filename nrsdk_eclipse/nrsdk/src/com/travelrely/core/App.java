package com.travelrely.core;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.travelrely.v2.db.ComDBOpenHelper;
import com.travelrely.v2.db.ComDbManager;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.MyExceptionHandler;

public class App extends Application
{
    private final static String TAG = App.class.getSimpleName();
    
    private SparseArray<Object> sa = new SparseArray<Object>();
	/** 主线程ID */
	private static int mMainThreadId = -1;
    
    /** 主线程 */
	private static Thread mMainThread;
	/** 主线程Looper */
	private static Looper mMainLooper;

    
    /** 主线程Handler */
	private static Handler mMainThreadHandler;
	
	private boolean isNewLoad;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        LogUtil.v(TAG, "App onCreate");
        mMainThreadId = android.os.Process.myPid();
        Engine.getInstance(this);
        mMainThreadHandler = new Handler();
        ComDbManager.init(new ComDBOpenHelper(getApplicationContext()));
//        Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler.getInstance(getApplicationContext()));
        MyExceptionHandler myExp = MyExceptionHandler.getInstance();
        myExp.init(getApplicationContext());
        
        isNewLoad = true;
    
    }
    
    public boolean isNewLoad()
    {
        return isNewLoad;
    }
    
    public void setLoadFlag(boolean flag)
    {
        isNewLoad = flag;
    }
    
    public Object get(int key)
    {
        return sa.get(key);
    }
    
    public void put(int key, Object value)
    {
        sa.put(key, value);
    }
	/** 获取主线程 */
	public static Thread getMainThread() {
		return mMainThread;
	}

	/** 获取主线程的handler */
	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	/** 获取主线程的looper */
	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}

	public static long getMainThreadId() {
		// TODO Auto-generated method stub
		return mMainThreadId;
	}
}
