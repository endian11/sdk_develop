package sdk.travelrely.lib.util;

import android.app.Activity;
import android.content.Intent;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.util
 * ＊ 10:25
 */
public class AndroidUtil {

    public static Boolean IS_LOLLIPOP = false;

    static {
        IS_LOLLIPOP = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
    }

    //位置和安全设置
    public static final String ACTION_SECURITY_SETTINGS
            = android.provider.Settings.ACTION_SECURITY_SETTINGS;

    //选择of2G/3G显示设置
    public static final String ACTION_DATA_ROAMING_SETTINGS
            = android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS;

    //显示设置，以允许应用程序开发相关的设置配置
    public static final String ACTION_APPLICATION_DEVELOPMENT_SETTINGS
            = android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS;

    //设置->可访问性
    public static final String ACTION_ACCESSIBILITY_SETTINGS
            = android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS;

    //设置->账户和同步设置->添加账户
    public static final String ACTION_ADD_ACCOUNT
            = android.provider.Settings.ACTION_ADD_ACCOUNT;

    //设置->无线和网络设置->飞行模式
    public static final String ACTION_AIRPLANE_MODE_SETTINGS
            = android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS;

    //无线和网络设置->移动网络设置->接入点名称->APN
    public static final String ACTION_APN_SETTINGS
            = android.provider.Settings.ACTION_APN_SETTINGS;

    //设置->应用程序
    public static final String ACTION_APPLICATION_SETTINGS
            = android.provider.Settings.ACTION_APPLICATION_SETTINGS;

    //设置->无线和网络设置->蓝牙设置
    public static final String ACTION_BLUETOOTH_SETTINGS
            = android.provider.Settings.ACTION_BLUETOOTH_SETTINGS;

    //设置->日期和时间设置
    public static final String ACTION_DATE_SETTINGS
            = android.provider.Settings.ACTION_DATE_SETTINGS;

    //设置->关于手机
    public static final String ACTION_DEVICE_INFO_SETTINGS
            = android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS;

    //设置->显示
    public static final String ACTION_DISPLAY_SETTINGS
            = android.provider.Settings.ACTION_DISPLAY_SETTINGS;

    //设置->语言和键盘设置
    public static final String ACTION_INPUT_METHOD_SETTINGS
            = android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS;

    //设置->SD卡和手机内存->存储设置->手机内存
    public static final String ACTION_INTERNAL_STORAGE_SETTINGS
            = android.provider.Settings.ACTION_INTERNAL_STORAGE_SETTINGS;

    //设置->语言和键盘设置->选择区域->语言
    public static final String ACTION_LOCALE_SETTINS
            = android.provider.Settings.ACTION_LOCALE_SETTINGS;

    //设置->位置和安全设置
    public static final String ACTION_LOCATION_SOURCE_SETTINGS
            = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;

    // 设置->应用程序->管理应用程序
    public static final String ACTION_MANAGE_APPLICATIONS_SETTINGS
            = android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS;

    //设置->SD卡和手机内存->存储设置->SD卡
    public static final String ACTION_MEMORY_CARD_SETTINGS
            = android.provider.Settings.ACTION_MEMORY_CARD_SETTINGS;

    //设置->隐私设置
    public static final String ACTION_PRIVACY_SETTINGS
            = android.provider.Settings.ACTION_PRIVACY_SETTINGS;

    // 设置->搜索设置
    public static final String ACTION_SEARCH_SETTINGS
            = android.provider.Settings.ACTION_SEARCH_SETTINGS;

    //设置
    public static final String ACTION_SETTINGS
            = android.provider.Settings.ACTION_SETTINGS;

    //设置->声音设置
    public static final String ACTION_SOUND_SETTINGS
            = android.provider.Settings.ACTION_SOUND_SETTINGS;

    //设置->账户与同步设置
    public static final String ACTION_SYNC_SETTINGS
            = android.provider.Settings.ACTION_SYNC_SETTINGS;

    //设置->用户字典设置
    public static final String ACTION_USER_DICTIONARY_SETTINGS
            = android.provider.Settings.ACTION_USER_DICTIONARY_SETTINGS;

    //设置->无线和网络设置->Wlan设置
    public static final String ACTION_WIFI_SETTINGS
            = android.provider.Settings.ACTION_WIFI_SETTINGS;

    //设置->无线和网络设置->无线设置
    public static final String ACTION_WIRELESS_SETTINGS
            = android.provider.Settings.ACTION_WIRELESS_SETTINGS;

    //设置->无线和网络设置->Wlan设置->(菜单键)高级
    public static final String ACTION_WIFI_IP_SETTINGS
            = android.provider.Settings.ACTION_WIFI_IP_SETTINGS;

    //显示设置，以允许快速启动快捷键的配置
    public static final String ACTION_QUICK_LAUNCH_SETTINGS
            = android.provider.Settings.ACTION_QUICK_LAUNCH_SETTINGS;

    //选择网络运营商的显示设置
    public static final String ACTION_NETWORK_OPERATOR_SETTINGS
            = android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS;

    /**
     * 打开设置界面
     * @param action   打开的界面类型
     * @param activity
     */
    public static void openSystemSetting(String action, Activity activity) {
        Intent intent = new Intent(action);
        activity.startActivity(intent);
    }

}
