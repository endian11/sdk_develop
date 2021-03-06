package sdk.travelrely.lib.config;

import sdk.travelrely.lib.TRSdk;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/3.
 * ＊ sdk.travelrely.lib
 * ＊ 13:20
 */
public class SDKConfig {
    public static Boolean DEBUG = false;

    public static String getSharedName() {
        //TODO 获取本地缓存shard的标记名称
        if (DEBUG) {
            return TRSdk.getInstance().getContext().getPackageName() + "shared.debug";
        } else {
            return TRSdk.getInstance().getContext().getPackageName() + "shared";
        }
    }
}
