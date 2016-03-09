package com.travelrely.core.util;

import com.travelrely.core.nrs.ReleaseConfig;

import android.util.Log;

/**
 * We use this class to print the log, by modifying the LogType when the final
 * release, you can log Close(code:LogType=HIDE_LOG), released a beta version
 * you can open the log(code:LogType=SHOW_LOG).
 */
public class LOGManager
{
    public static final String TAG = "TravelRelyLog";

    public static void v(String msg)
    {
        if (ReleaseConfig.IS_DEBUG_VERSION)
        {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg)
    {
        if (ReleaseConfig.IS_DEBUG_VERSION)
        {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg)
    {
        if (ReleaseConfig.IS_DEBUG_VERSION)
        {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg)
    {
        if (ReleaseConfig.IS_DEBUG_VERSION)
        {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg)
    {
        if (ReleaseConfig.IS_DEBUG_VERSION)
        {
            Log.e(TAG, msg);
        }
    }
}
