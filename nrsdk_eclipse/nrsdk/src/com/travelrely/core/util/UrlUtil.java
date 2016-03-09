package com.travelrely.core.util;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;

import android.text.TextUtils;

/**
 * @author zhangyao
 * @version 2014年12月9日下午5:53:52
 */

public class UrlUtil
{
    public static String url(String value)
    {
        String smc_loc;
        if (Engine.getInstance().homeLogin == true)
        {
            smc_loc = Engine.getInstance().getHomeSmcLoc();
        }
        else
        {
            smc_loc = Engine.getInstance().getRoamSmcLoc();
        }

        if (TextUtils.isEmpty(smc_loc))
        {
            String cc = Engine.getInstance().getCC();
            String host = ReleaseConfig.getUrl(cc);
            return host + value;
        }

        String url = makeUrl(smc_loc, value);

        return url;
    }

    public static String ip()
    {
        String smc_loc;
        if (Engine.getInstance().homeLogin == true)
        {
            smc_loc = Engine.getInstance().getHomeSmcLoc();
        }
        else
        {
            smc_loc = Engine.getInstance().getRoamSmcLoc();
        }

        return makeUrl(smc_loc);
    }

    public static String makeUrl(String ip, String value)
    {
        if (ip == null)
        {
            return null;
        }

        return "http://" + ip + "/" + value;
    }

    public static String makeUrl(String ip)
    {
        if (ip == null)
        {
            return null;
        }

        return "http://" + ip + "/";
    }
}
