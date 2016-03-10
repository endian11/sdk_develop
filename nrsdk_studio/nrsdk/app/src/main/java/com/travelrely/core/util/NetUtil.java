package com.travelrely.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.telephony.TelephonyManager;

public class NetUtil
{
    public static int getNetType(Context mCtx)
    {
        ConnectivityManager connectMgr = (ConnectivityManager) mCtx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null)
        {
            return 255;
        }
        
        if (!info.isAvailable())
        {
            return 255;
        }

        if (info.getType() == ConnectivityManager.TYPE_WIFI)
        {
            return 0;
        }

        if (info.getType() == ConnectivityManager.TYPE_MOBILE)
        {
            switch (info.getSubtype())
            {
                // 2G
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return 1;

                // 3G
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:

                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return 2;

                case TelephonyManager.NETWORK_TYPE_LTE:
                    return 3;

                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return 4;

                default:
                    return 255;
            }
        }
        
        return 255;
    }

    /**
     * 判断手机网络是否可用
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info == null || info.length == 0)
        {
            return false;
        }
        for (int i = 0; i < info.length; i++)
        {
            if (info[i].getState() == NetworkInfo.State.CONNECTED)
            {
                return true;
            }
        }
        return false;
    }

    public static String getPhoneIp()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address)
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (Exception e)
        {
        }
        return "";
    }
    
    public static long getMobileRxBytes()
    {
        /** 获取手机通过 2G/3G 接收的字节流量总数 */
        return TrafficStats.getMobileRxBytes();
    }
    
    public static long getMobileRxPackets()
    {
        /** 获取手机通过 2G/3G 接收的数据包总数 */
        return TrafficStats.getMobileRxPackets();
    }
    
    public static long getMobileTxBytes()
    {
        /** 获取手机通过 2G/3G 发出的字节流量总数 */
        return TrafficStats.getMobileTxBytes();
    }
    
    public static long getMobileTxPackets()
    {
        /** 获取手机通过 2G/3G 发出的数据包总数 */
        return TrafficStats.getMobileTxPackets();
    }
    
    public static long getMobileTtlBytes()
    {
        /** 获取手机通过 2G/3G 接收的字节流量总数 */
        return TrafficStats.getMobileRxBytes()
                + TrafficStats.getMobileTxBytes();
    }
    
    public static long getTotalRxBytes()
    {
        /** 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
         return TrafficStats.getTotalRxBytes();
    }
    
    public static long getTotalRxPackets()
    {
        /** 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
          return TrafficStats.getTotalRxPackets();
    }
    
    public static long getTotalTxBytes()
    {
        /** 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
           return TrafficStats.getTotalTxBytes();
    }
    
    public static long getTotalTxPackets()
    {
        /** 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
        return TrafficStats.getTotalTxPackets();
    }
    
    public static long getUidRxBytes(int uid)
    {
        /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
        return TrafficStats.getUidRxBytes(uid);
    }
    
    public static long getUidTxBytes(int uid)
    {
        /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
        return TrafficStats.getUidTxBytes(uid);
    }
}
