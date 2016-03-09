package com.travelrely.v2.util;

public class SysUtil
{
    public static String getSysName()
    {
        return android.os.Build.VERSION.RELEASE;
    }
    
    public static String getSysSdk()
    {
        return android.os.Build.VERSION.SDK;
    }
    
    public static int getSysSdkCode()
    {
        return android.os.Build.VERSION.SDK_INT;
    }
}
