package com.travelrely.v2.util;

import com.travelrely.core.Engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/** 
 * 
 * @author zhangyao
 * @version 2014年7月25日下午3:11:14
 * 
 * 用来保存简单数据的工具类
 */

public class PreferencesUtil {
    
    public static final String PUBLIC_PRERENCES = Engine.getInstance().getUserName() + "config";
    public static final String ALERT_CONFIG = "alert_config";
    
    
    public static void setSharedPreferences(Context mContext, String tabName, String key, String velau){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                tabName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString(key, velau);
        e.commit();
    } 
    
    public static void setSharedPreferences(Context mContext, String tabName, String key, int velau){
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                tabName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt(key, velau);
        e.commit();
    } 
    
    public static void setSharedPreferences(Context mContext, String tabName, String key, boolean velau){
        
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                tabName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putBoolean(key, velau);
        e.commit();
    } 
    
    public static int getSharedPreferencesInt(Context mContext, String tabName, String str_key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                tabName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(str_key, 0);
    }
    
    public static String getSharedPreferencesStr(Context mContext, String tabName, String str_key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                tabName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(str_key, "");
    }
    
    public static boolean getSharedPreferencesBoolean(Context mContext, String tabName, String str_key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                tabName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(str_key, false);
    }

}
