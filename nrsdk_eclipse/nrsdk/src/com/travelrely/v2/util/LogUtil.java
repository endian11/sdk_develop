package com.travelrely.v2.util;

import com.travelrely.core.Engine;

import android.util.Log;

public class LogUtil
{
    public static final int OFF = 0;
    public static final int ERR = 1;
    public static final int WAR = 2;
    public static final int INF = 3;
    public static final int DBG = 4;
    public static final int VER = 5;

    private static int logLevel = 4;

    public static void setLogLevel(int log)
    {
        logLevel = log;
    }

    public static void d(String TAG, String logMessage)
    {
        if (logLevel >= DBG)
        {
            Log.d(TAG, logMessage);
        }
    }

    public static void i(String TAG, String logMessage)
    {
        if (logLevel >= INF)
        {
            Log.i(TAG, logMessage);

            saveLog("INFO", TAG, logMessage);
        }
    }

    public static void w(String TAG, String logMessage)
    {
        if (logLevel >= WAR)
        {
            Log.w(TAG, logMessage);
            
            saveLog("WARN", TAG, logMessage);
        }
    }

    public static void e(String TAG, String logMessage)
    {
        if (logLevel >= ERR)
        {
            Log.e(TAG, logMessage);

            saveLog("ERROR", TAG, logMessage);
        }
    }
    
    public static void d(int TAG, String logMessage)
    {
        if (logLevel >= DBG)
        {
            Log.d(Integer.toString(TAG), logMessage);
        }
    }

    public static void i(int TAG, String logMessage)
    {
        if (logLevel >= INF)
        {
            Log.i(Integer.toString(TAG), logMessage);

            saveLog(TAG, logMessage);
        }
    }

    public static void w(int TAG, String logMessage)
    {
        if (logLevel >= WAR)
        {
            Log.w(Integer.toString(TAG), logMessage);
            
            saveLog(TAG, logMessage);
        }
    }

    public static void e(int TAG, String logMessage)
    {
        if (logLevel >= ERR)
        {
            Log.e(Integer.toString(TAG), logMessage);

            saveLog(TAG, logMessage);
        }
    }
    
    public static void v(String TAG, String format, Object... args)
    {
        if (logLevel >= VER)
        {
            String msg = String.format(format, args);
            Log.v(TAG, msg);
        }
    }
    
    public static void d(String TAG, String format, Object... args)
    {
        if (logLevel >= DBG)
        {
            String msg = String.format(format, args);
            Log.d(TAG, msg);
        }
    }
    
    public static void i(String TAG, String format, Object... args)
    {
        if (logLevel >= INF)
        {
            String msg = String.format(format, args);
            Log.i(TAG, msg);
            saveLog("INFO", TAG, msg);
        }
    }
    
    public static void w(String TAG, String format, Object... args)
    {
        if (logLevel >= WAR)
        {
            String msg = String.format(format, args);
            Log.w(TAG, msg);
            saveLog("WARN", TAG, msg);
        }
    }
    
    public static void e(String TAG, String format, Object... args)
    {
        if (logLevel >= ERR)
        {
            String msg = String.format(format, args);
            Log.e(TAG, msg);
            saveLog("ERROR", TAG, msg);
        }
    }
    
    public static void v(int TAG, String format, Object... args)
    {
        if (logLevel >= VER)
        {
            String msg = String.format(format, args);
            Log.v(Integer.toString(TAG), msg);
        }
    }
    
    public static void d(int TAG, String format, Object... args)
    {
        if (logLevel >= DBG)
        {
            String msg = String.format(format, args);
            Log.d(Integer.toString(TAG), msg);
        }
    }
    
    public static void i(int TAG, String format, Object... args)
    {
        if (logLevel >= INF)
        {
            String msg = String.format(format, args);
            Log.i(Integer.toString(TAG), msg);
            saveLog(TAG, msg);
        }
    }
    
    public static void w(int TAG, String format, Object... args)
    {
        if (logLevel >= WAR)
        {
            String msg = String.format(format, args);
            Log.w(Integer.toString(TAG), msg);
            saveLog(TAG, msg);
        }
    }
    
    public static void e(int TAG, String format, Object... args)
    {
        if (logLevel >= ERR)
        {
            String msg = String.format(format, args);
            Log.e(Integer.toString(TAG), msg);
            saveLog(TAG, msg);
        }
    }
    

    public synchronized static void saveLog(String lev, String tag, String log)
    {
        String t = Utils.GetDate(0, "yyyy-MM-dd HH:mm:ss.SSS");
        long id = Thread.currentThread().getId();
        //String msg = t + "|" + id + "|" + tag + "|" + log + "\n";
        String msg = String.format("%s %05X %s %s:%s\n", t, id, lev, tag, log);
        
        Engine.getInstance().syncLog(msg);
    }
    
    public synchronized static void saveLog(int tag, String log)
    {
        String t = Utils.GetDate(0, "yyyy-MM-dd HH:mm:ss:SSS");
        long id = Thread.currentThread().getId();
        //String msg = t + "|" + id + "|" + tag + "|" + log + "\n";
        String msg = String.format("%s|%05X|%d|%s\n", t, id, tag, log);
        
        Engine.getInstance().syncLog(msg);
    }
}
