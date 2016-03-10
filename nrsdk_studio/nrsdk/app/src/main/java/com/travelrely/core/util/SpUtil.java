package com.travelrely.core.util;

import com.travelrely.core.nrs.Engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


public class SpUtil
{
    public static final String PUBLIC = "public";
    
    //保存订单更新状态   6/29新加
    public static final String ORDERUPDATE = "orderUpdate";
    
    public static int getScreenWidth()
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("screen_width", 0);
    }

    public static void setScreenWidth(int screenWidth)
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("screen_width", screenWidth);
        e.commit();
    }

    public static int getScreenHeight()
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("screen_height", 0);
    }

    public static void setScreenHeight(int screenHeight)
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("screen_height", screenHeight);
        e.commit();
    }
    
    public static void setUserName(String user)
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("username", user);
        e.commit();
    }
    
    /**
     * @param user
     */
    /**保存用户选择的套餐
     * @param selectPkg
     */
    public static void setSelectedPkg(String selectPkg)
    {
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences sharedPreferences = mContext.getSharedPreferences(
    			PUBLIC, Context.MODE_PRIVATE);
    	Editor e = sharedPreferences.edit();
    	e.putString("selectPkg", selectPkg);
    	e.commit();
    }
    
    /**返回用户保存的套餐
     * @param mContext
     * @return
     */
    public static String getSelectedPkg(Context mContext)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        String selectPkg = sharedPreferences.getString("selectPkg", "");
        return selectPkg;
    }
    public static String getUserName(Context mContext)
    {
    	SharedPreferences sharedPreferences = mContext.getSharedPreferences(
    			PUBLIC, Context.MODE_PRIVATE);
    	String userName = sharedPreferences.getString("username", "");
    	return userName;
    }
    
    public static boolean setLongPswd(String longPassword)
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("long_password", longPassword);

        boolean success = e.commit();
        return success;
    }

    public static String getLongPswd(Context mContext)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        String long_password = sharedPreferences.getString("long_password", "");

        return long_password;
    }
    
//    public static void setLastLogin(boolean lastLoginFlag)
//    {
//        Context mContext = Engine.getInstance().getContext();
//        String user = Engine.getInstance().getUserName();
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
//                user, Context.MODE_PRIVATE);
//        Editor e = sharedPreferences.edit();
//        e.putBoolean("last_login_flag", lastLoginFlag);
//        e.commit();
//    }
//
//    public static Boolean getLastLogin()
//    {
//        Context mContext = Engine.getInstance().getContext();
//        String user = Engine.getInstance().getUserName();
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
//                user, Context.MODE_PRIVATE);
//        return sharedPreferences.getBoolean("last_login_flag", false);
//    }
    
    public static void setCC(String cc)
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("cc", cc);
        e.commit();
    }

    public static String getCC(Context mContext)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        return sharedPreferences.getString("cc", "");
    }
    
    public static void setLangType(int iLangType)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("lang_type", iLangType);
        e.commit();
    }

    public static int getLangType()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("lang_type", 255);
    }
    
    public static int getVisiting()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        int isVisiting = sharedPreferences.getInt("is_visiting", 0);
        return isVisiting;
    }

    public static void setVisiting(int isVisiting)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("is_visiting", isVisiting);
        e.commit();
    }
    
    public static int getHomeProfileVer()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("home_profile_version", 0);
    }

    public static void setHomeProfileVer(int homeProfileVersion)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("home_profile_version", homeProfileVersion);
        e.commit();
    }
    
    public static int getRoamProfileVer()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("roam_profile_version", 0);
    }

    public static void setRoamProfileVer(int roamProfileVersion)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("roam_profile_version", roamProfileVersion);
        e.commit();
    }
    
    public static int getExpPriceVer()
    {
        Context mContext = Engine.getInstance().getContext();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("express_price_version", 0);
    }

    public static void setExpPriceVer(int expressPriceVersion)
    {
        Context mContext = Engine.getInstance().getContext();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("express_price_version", expressPriceVersion);
        e.commit();
    }
    
    public static int getAdvVer()
    {
        Context mContext = Engine.getInstance().getContext();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("adv_version", 0);
    }

    public static void setAdvVer(int advVersion)
    {
        Context mContext = Engine.getInstance().getContext();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("adv_version", advVersion);
        e.commit();
    }
    
    public static int getCountryVer()
    {
        Context mContext = Engine.getInstance().getContext();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("country_info_version", 0);
    }

    public static void setCountryVer(int countryInfoVersion)
    {
        Context mContext = Engine.getInstance().getContext();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("country_info_version", countryInfoVersion);
        e.commit();
    }
    
    public static int getPkgVer()
    {
        Context mContext = Engine.getInstance().getContext();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("package_version", 0);
    }

    public static void setPkgVer(int advVersion)
    {
        Context mContext = Engine.getInstance().getContext();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("package_version", advVersion);
        e.commit();
    }
    
    public static int getUsrRoamProfileVer()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        int isVisiting = sharedPreferences.getInt("usr_roam_profile_ver", 0);
        return isVisiting;
    }

    public static void setUsrRoamProfileVer(int version)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("usr_roam_profile_ver", version);
        e.commit();
    }
    
    public static int getNRInfoVer()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        int isVisiting = sharedPreferences.getInt("NR_info_version", 0);
        return isVisiting;
    }

    public static void setNRInfoVer(int version)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("NR_info_version", version);
        e.commit();
    }
  //默认用户能用旅信nr服务
    public static int getNRService()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        int isVisiting = sharedPreferences.getInt("NoRoaming", 1);
        return isVisiting;
    }

    public static void setNRService(int isNoRoaming)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("NoRoaming", isNoRoaming);
        e.commit();
    }
    
    public static String getNRStart()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getString("nr_start_date", "");
    }

    public static void setNRStart(String date)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("nr_start_date", date);
        e.commit();
    }
    
    public static String getNREnd()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getString("nr_end_date", "");
    }

    public static void setNREnd(String date)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("nr_end_date", date);
        e.commit();
    }
    
    public static String getNRFemtoIp()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getString("femto_ip", "");
    }

    public static void setNRFemtoIp(String ip)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("femto_ip", ip);
        e.commit();
    }
    
    public static String getNRTimeZone()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getString("NR_time_zone", "");
    }

    public static void setNRTimeZone(String timeZone)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("NR_time_zone", timeZone);
        e.commit();
    }
    
    public static int getNRMode()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        int isVisiting = sharedPreferences.getInt("NR_mode", 0);
        return isVisiting;
    }

    public static void setNRMode(int mode)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("NR_mode", mode);
        e.commit();
    }
    
    public static String getNRNoDisturbBegin()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getString("no_disturb_start", "");
    }

    public static void setNRNoDisturbBegin(String begin)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("no_disturb_start", begin);
        e.commit();
    }
    
    public static String getNRNoDisturbEnd()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getString("no_disturb_end", "");
    }

    public static void setNRNoDisturbEnd(String end)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("no_disturb_end", end);
        e.commit();
    }
    
    public static long getNRTxBytes()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("NR_TX_BYTES", 0L);
    }

    public static void setNRTxBytes(long l)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putLong("NR_TX_BYTES", l);
        e.commit();
    }
    
    public static long getNRRxBytes()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("NR_RX_BYTES", 0L);
    }

    public static void setNRRxBytes(long bytes)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putLong("NR_RX_BYTES", bytes);
        e.commit();
    }
    
    public static int getXHService()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        int isVisiting = sharedPreferences.getInt("XHService", 0);
        return isVisiting;
    }

    public static void setXHService(int XHService)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("XHService", XHService);
        e.commit();
    }
    
    public static void setBtName(String btName)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("btName", btName);
        e.commit();
    }

    public static String getBtName()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        String btAddr = sharedPreferences.getString("btName", "");
        return btAddr;
    }
    
    public static void setBtAddr(String btAddr)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("btAddr", btAddr);
        e.commit();
    }

    public static String getBtAddr()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        String btAddr = sharedPreferences.getString("btAddr", "");
        return btAddr;
    }
    public static void setfirstConnect(int btAddr)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("firstconnect", btAddr);
        e.commit();
    }
    public static int getfirstConnect()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        int btAddr = sharedPreferences.getInt("firstconnect", 0);
        return btAddr;
    }
    public static void setBtKey(String btAddr)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("btKey", btAddr);
        e.commit();
    }

    public static String getBtKey()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        String btAddr = sharedPreferences.getString("btKey", "");
        return btAddr;
    }
    
    public static void setBtUpdate(boolean btAddr)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putBoolean("btUpdate", btAddr);
        e.commit();
    }

    public static boolean getBtUpdate()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("btUpdate", false);
    }
    
    public static void setCosVer(int fwVer)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("box_fw_ver", fwVer);
        e.commit();
    }

    public static int getCosVer()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("box_fw_ver", 0);
    }
    
    public static void setBoxSn(String sn)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("box_sn", sn);
        e.commit();
    }

    public static String getBoxSn()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getString("box_sn", "");
    }
    
    public static void setBoxAesKey(String key)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("box_aes_key", key);
        e.commit();
    }

    public static String getBoxAesKey()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("box_aes_key", null);
        return (TextUtils.isEmpty(key) ? null : key);
    }
    
    public static void setBoxBattery(int battery)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("box_battery", battery);
        e.commit();
    }

    public static int getBoxBattery()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("box_battery", -1);
    }
    
    public static void setLastFreground(String time)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("last_time", time);
        e.commit();
    }

    public static String getLastFreground()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        String btAddr = sharedPreferences.getString("last_time", "");
        return btAddr;
    }
    
    public static int getContactStatus()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        int isVisiting = sharedPreferences.getInt("ContactStatus", 0);
        return isVisiting;
    }

    public static void setContactStatus(int status)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("ContactStatus", status);
        e.commit();
    }
    
    public static double getBalance()
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        float val = sharedPreferences.getFloat("balance", (float) 0.0);

        return ArithUtil.float2double(val);
    }

    public static void setBalance(double balance)
    {
        Context mContext = Engine.getInstance().getContext();
        String user = Engine.getInstance().getUserName();
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                user, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putFloat("balance", (float) balance);
        e.commit();
    }
    
    public static int getGuide()
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        int hasGuide = sharedPreferences.getInt("has_guide", 0);
        return hasGuide;
    }

    public static void setGuide(int hasGuide)
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt("has_guide", hasGuide);
        e.commit();
    }
    public static int getfirstInstallToLogin()
    {
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences sharedPreferences = mContext.getSharedPreferences(
    			PUBLIC, Context.MODE_PRIVATE);
    	int hasGuide = sharedPreferences.getInt("firstInstallToLogin", 0);
    	return hasGuide;
    }
    
    public static void setfirstInstallToLogin(int hasGuide)
    {
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences sharedPreferences = mContext.getSharedPreferences(
    			PUBLIC, Context.MODE_PRIVATE);
    	Editor e = sharedPreferences.edit();
    	e.putInt("firstInstallToLogin", hasGuide);
    	e.commit();
    }
    public static String getDefaultMCC()
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        String default_mcc = sharedPreferences.getString("default_mcc", "");
        return default_mcc;
    }

    public static void setDefaultMCC(String defaultMCC)
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("default_mcc", defaultMCC);
        e.commit();
    }
    
    public static String getDefaultMNC()
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        String default_mnc = sharedPreferences.getString("default_mnc", "");
        return default_mnc;
    }

    public static void setDefaultMNC(String defaultMNC)
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PUBLIC, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("default_mnc", defaultMNC);
        e.commit();
    }
    
    /***============================保存订单是否需要更新状态========================***/
    public static void setOrderUpdate(int flag){
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences mshSharedPreferences = mContext.getSharedPreferences(ORDERUPDATE, 
    			Context.MODE_PRIVATE);
    	Editor meEditor = mshSharedPreferences.edit();
    	meEditor.putInt("updateFlag", flag);
    	meEditor.commit();
    }
    
    public static int	getOrderUpdate()
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
        		ORDERUPDATE, Context.MODE_PRIVATE);
        int updateFlage = sharedPreferences.getInt("updateFlag", 0);
        return updateFlage;
    }
    /****========================END=========================================***/
    
    
    
    /***============================保存订单版本号========================***/
    public static void setOrderVersion(int flag){
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences mshSharedPreferences = mContext.getSharedPreferences("orderV", 
    			Context.MODE_PRIVATE);
    	Editor meEditor = mshSharedPreferences.edit();
    	meEditor.putInt("updateFlag", flag);
    	meEditor.commit();
    }
    
    public static int	getOrderVersion()
    {
        Context mContext = Engine.getInstance().getContext();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
        		"orderV", Context.MODE_PRIVATE);
        int updateFlage = sharedPreferences.getInt("orderVersion", 0);
        return updateFlage;
    }
    /****========================END=========================================***/
    
    /***============================保存商店版本号========================***/
    public static void setshop_info_version(String flag){
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences mshSharedPreferences = mContext.getSharedPreferences("shopinfoversion", 
    			Context.MODE_PRIVATE);
    	Editor meEditor = mshSharedPreferences.edit();
    	meEditor.putString("shop_info_version", flag);
    	meEditor.commit();
    }
    
    public static String	getshop_info_version()
    {
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences sharedPreferences = mContext.getSharedPreferences(
    			"shopinfoversion", Context.MODE_PRIVATE);
    	String updateFlage = sharedPreferences.getString("shop_info_version", "0");
    	return updateFlage;
    }
    /****========================END=========================================***/
    
    /***============================保存盒子里sim卡imsi========================***/
    public static void setImsiInBox(String flag){
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences mshSharedPreferences = mContext.getSharedPreferences("imsi", 
    			Context.MODE_PRIVATE);
    	Editor meEditor = mshSharedPreferences.edit();
    	meEditor.putString("ImsiInBox", flag);
    	meEditor.commit();
    }
    
    public static String	getImsiInBox()
    {
    	Context mContext = Engine.getInstance().getContext();
    	SharedPreferences sharedPreferences = mContext.getSharedPreferences(
    			"imsi", Context.MODE_PRIVATE);
    	String updateFlage = sharedPreferences.getString("ImsiInBox", "");
    	return updateFlage;
    }
    /****========================END=========================================***/
    
    
    
    
}
