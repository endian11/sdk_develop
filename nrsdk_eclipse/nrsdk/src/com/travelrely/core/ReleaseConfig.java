package com.travelrely.core;

import java.util.HashMap;

import android.text.TextUtils;

public class ReleaseConfig
{
    public static boolean initValue = true;
    public static final boolean IS_DEBUG_VERSION = true;
    
    // 是否连接测试服务器
    public static final boolean isTest = false;//false表示商用服务器
    
    private static HashMap<String, String> msg = new HashMap<String, String>();
    static
    {
        msg.put("+1", "198.20.249.88");
        msg.put("+86", "glms.460.travelrely.com");
        msg.put("+852", "server01.travelrely.hk");
        msg.put("+853", "server01.travelrely.hk");
        msg.put("+886", "server01.travelrely.hk");
    }
    
    public static String getHost(String cc)
    {
        if (isTest)
        {
            return "115.28.167.215";
        }
        
        String result = msg.get(cc);
        if (TextUtils.isEmpty(result))
        {
            result = "glms.460.travelrely.com";
        }
        return result;
    }
    
    public static String getUrl(String cc)
    {
        return "http://" + getHost(cc) + "/";
    }
    
    
    /*********打包渠道 目前有5个渠道********/
    /**
     * 1.360 Market
     * 2.91 Market
     * 3.google Market
     * 4.anzhi Market
     * 5.travelrely Market
     * 6.myapp Market                         //应用宝
     */
}
