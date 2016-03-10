package com.travelrely.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.travelrely.core.nrs.nr.util.ActivityCollector;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * @author zhangyao
 * @version 2014年11月12日下午5:25:34
 */

public class MyExceptionHandler implements UncaughtExceptionHandler
{
    private static final String TAG = MyExceptionHandler.class.getSimpleName();
  //用来存储设备信息和异常信息
  	private Map<String, String> infos = new HashMap<String, String>();
  	
  	
  //系统默认的UncaughtException处理类 
  	private Thread.UncaughtExceptionHandler mDefaultHandler;
  	
    private static MyExceptionHandler mHandler;
    static Context mContext;

    private MyExceptionHandler()
    {

    }

    
	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
    
    
    public synchronized static MyExceptionHandler getInstance()
    {
        if (mHandler == null)
        {
            mHandler = new MyExceptionHandler();
        }
        return mHandler;
    }

    @Override
    public void uncaughtException(Thread arg0, Throwable ex)
    {
    	
    	
    	if (!handleException(ex) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(arg0, ex);
		} else {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			//退出程序
			ActivityCollector.finishAll();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
    	
    	
    	

    }

    
	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//使用Toast来显示异常信息
//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
//				Looper.loop();
//			}
//		}.start();
		//收集设备参数信息 
		collectDeviceInfo(mContext,ex);
		
	
		return true;
	}
    
    
    
    
    /**
	 * 收集设备参数信息
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx,Throwable ex) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
			//保存日志文件
			StringBuffer sb = new StringBuffer();
			for (Map.Entry<String, String> entry : infos.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append(key + "=" + value + "\n");
			}
			
			 Writer writer = new StringWriter();
			 PrintWriter printWriter = new PrintWriter(writer);
			 ex.printStackTrace(printWriter);
			 Throwable cause = ex.getCause();
			 while (cause != null) {
			    cause.printStackTrace(printWriter);
			    cause = cause.getCause();
			   }
			   printWriter.close();
			   String result = writer.toString();
			  sb.append(result);
			
			
			LogUtil.saveLog("[ERROR]", TAG, sb.toString());
		}
	}

    
}
