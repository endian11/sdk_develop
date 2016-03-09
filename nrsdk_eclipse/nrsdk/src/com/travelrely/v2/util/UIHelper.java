
package com.travelrely.v2.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class UIHelper {

    private static Handler uiHandler = null;

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidthDip(Context context) {
        int widthPx = UIHelper.getScreenWidth(context);
        int widthDip = UIHelper.px2dip(context, widthPx);
        return widthDip;
    }

    public static int getScreenHeightDip(Context context) {
        int heightPx = UIHelper.getScreenHeight(context);
        int heightDip = UIHelper.px2dip(context, heightPx);
        return heightDip;
    }

    public static float getDensity(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().density;
        } else {
            return 1.0f;
        }
    }

    public static boolean isPad(Context context) {
        float density = getDensity(context);
        int screenwidth = getScreenWidth(context);
        int screenheight = getScreenHeight(context);
        if ((screenwidth / density) > 700
                || (screenheight / density) > 700)
        {
            return true;
        }
        else {
            return false;
        }
    }

    public static int getDensityDpi(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().densityDpi;
        } else {
            return 120;
        }
    }

    public static int getScreenWidth(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        } else {
            return 0;
        }
    }

    public static int getScreenHeight(Context context) {
        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        } else {
            return 0;
        }
    }

    public static boolean isUiThread() {

        return Looper.getMainLooper().getThread() == Thread.currentThread();

    }

    public static Handler getUIHandler() {

        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
        return uiHandler;
    }

    public static void runOnUiThread(Runnable run) {

        if (isUiThread()) {
            run.run();
        } else {
            getUIHandler().post(run);
        }
    }

    public static void runOnUIThreadDelay(Runnable action, long delayMillis) {
        getUIHandler().postDelayed(action, delayMillis);
    }

}
