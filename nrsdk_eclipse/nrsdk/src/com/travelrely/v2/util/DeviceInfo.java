package com.travelrely.v2.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceInfo
{
    public int platform_type = 1;

    public String device_type = "Android 2.3";

    public String device_model = "gsm/wcdma";

    public String imsi;

    public String imei;

    public String sim_mcc;
    public String sim_mnc;

    public String sim_provider_name;

    // 返回移动终端的类型
    public int phoneType;

    // 返回SIM卡提供商的国家代码
    public String simCountryIso;

    // 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
    public String simOperator;
    public String simOperatorName;

    // 返回SIM卡的序列号(IMEI)
    public String simSerialNumber;

    // 返回网络提供商的国家代码
    public String netCountryIso;

    // 返回MCC+MNC代码 (网络运营商国家代码和运营商网络代码)(IMSI)
    public String netOperator;
    public String netOperatorName;

    public String net_mcc;
    public String net_mnc;

    public static int linkSource = 0;

    // 是否装入SIM卡
    public boolean hasSim = false;

    // 是否有蜂窝网络
    public boolean hasNet = false;

    Context context;

    public static DeviceInfo getInstance(Context context)
    {
        DeviceInfo deviceInfo = new DeviceInfo(context);
        deviceInfo.init();
        return deviceInfo;
    }

    private DeviceInfo(Context context)
    {
        this.context = context;
    }

    private void init()
    {
        getMccAndMnc();
    }

    private void getMccAndMnc()
    {
        // TelephonyManager mTelephonyMgr =
        // (TelephonyManager)application.getBaseContext().
        // .getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyManager mTelephonyMgr = (TelephonyManager) this.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        imsi = mTelephonyMgr.getSubscriberId();
        imei = mTelephonyMgr.getDeviceId();
        /**
         * 返回移动终端的类型
         * 
         * PHONE_TYPE_CDMA 手机制式为CDMA，电信 PHONE_TYPE_GSM 手机制式为GSM，移动和联通
         * PHONE_TYPE_NONE 手机制式未知
         */
        phoneType = mTelephonyMgr.getPhoneType();
        
        // 返回SIM卡提供商的国家代码
        simCountryIso = mTelephonyMgr.getSimCountryIso();
        
        // 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
        simOperator = mTelephonyMgr.getSimOperator();
        simOperatorName = mTelephonyMgr.getSimOperatorName();
        
        // 返回SIM卡的序列号(IMEI)
        simSerialNumber = mTelephonyMgr.getSimSerialNumber();

        if (simOperator == null || simOperator.equals(""))
        {
            hasSim = true;
            LOGManager.d("未装入SIM卡");
        }
        // imsi = "460021184135849";
        // imsi = "310381184135849";
        // imsi = "454001184135849";
        LOGManager.d("simOperator=" + simOperator);
        if (simOperator != null && simOperator.length() >= 5)
        {
            sim_mcc = simOperator.substring(0, 3);
            sim_mnc = simOperator.substring(3, simOperator.length());
        }
        else
        {
            sim_mcc = "";
            sim_mnc = "";
        }

        // 返回SIM卡提供商的国家代码
        netCountryIso = mTelephonyMgr.getNetworkCountryIso();
        
        // 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
        netOperator = mTelephonyMgr.getNetworkOperator();
        netOperatorName = mTelephonyMgr.getNetworkOperatorName();

        if (netOperator == null || netOperator.equals(""))
        {
            hasNet = true;
            LOGManager.d("未检测到蜂窝网络");
        }
        LOGManager.d("netOperator=" + netOperator);
        if (netOperator != null && netOperator.length() >= 5)
        {
            net_mcc = netOperator.substring(0, 3);
            net_mnc = netOperator.substring(3, netOperator.length());
        }
        else
        {
            net_mcc = "";
            net_mnc = "";
        }
    }
}
