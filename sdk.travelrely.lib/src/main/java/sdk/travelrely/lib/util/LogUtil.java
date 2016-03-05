package sdk.travelrely.lib.util;

import android.util.Log;

import sdk.travelrely.lib.config.SDKConfig;

/**
 * Log统一管理类
 *
 * @author way
 */
public class LogUtil {
    private static LogCallback mCallback;

    private LogUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public interface LogCallback {
        void log(String info);
    }

    public static void setLogCallback(LogCallback callback) {
        mCallback = callback;
    }

    private static final String TAG = "trsdk";

    private static void callbackMessage(String message) {
        if (mCallback != null) {
            mCallback.log(message);
        }
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (SDKConfig.DEBUG) {
            Log.i(TAG, msg);
            callbackMessage(msg);
        }
    }

    public static void d(String msg) {
        if (SDKConfig.DEBUG) {
            Log.d(TAG, msg);
            callbackMessage(msg);
        }
    }

    public static void e(String msg) {
        if (SDKConfig.DEBUG) {
            Log.e(TAG, msg);
            callbackMessage(msg);
        }
    }

    public static void v(String msg) {
        if (SDKConfig.DEBUG) {
            Log.v(TAG, msg);
            callbackMessage(msg);
        }
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (SDKConfig.DEBUG) {
            Log.i(tag, msg);
            callbackMessage(msg);
        }
    }

    public static void d(String tag, String msg) {
        if (SDKConfig.DEBUG) {
            Log.i(tag, msg);
            callbackMessage(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (SDKConfig.DEBUG) {
            Log.i(tag, msg);
            callbackMessage(msg);
        }
    }

    public static void v(String tag, String msg) {
        if (SDKConfig.DEBUG) {
            Log.i(tag, msg);
            callbackMessage(msg);
        }
    }
}