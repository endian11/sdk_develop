package sdk.travelrely.lib.util;

import sdk.travelrely.lib.TRSdk;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/3.
 * ＊ sdk.travelrely.lib.util
 * ＊ 17:36
 */
public class SharedUtil {

    public static void set(String action,Object value){
        SPUtils.put(TRSdk.getInstance().getContext(),action,value);
    }

    public static <T> T get(String key , T defaultValue){
        Object value = SPUtils.get(TRSdk.getInstance().getContext(),key,defaultValue);
        if(value == null){
            return defaultValue;
        }
        return (T) value;
    }
}
