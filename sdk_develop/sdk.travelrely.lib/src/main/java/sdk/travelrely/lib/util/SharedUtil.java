package sdk.travelrely.lib.util;

import sdk.travelrely.lib.TRSdk;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/3.
 * ＊ sdk.travelrely.lib.util
 * ＊ 17:36
 */
public class SharedUtil {

    /**
     *
     * @param action sdk.travelrely.lib.TRAction.SHARED_*
     * @param value  要设置的值
     */
    public static void set(String action,Object value){
        SPUtils.put(TRSdk.getInstance().getContext(),action,value);
    }

    /**
     *
     * @param key   本地存储数据的键值
     * @param defaultValue  默认值
     * @param <T>   返回数据类型
     * @return
     */

    public static <T> T get(String key , T defaultValue){
        Object value = SPUtils.get(TRSdk.getInstance().getContext(),key,defaultValue);
        if(value == null){
            return defaultValue;
        }
        //noinspection unchecked
        return (T) value;
    }
}
