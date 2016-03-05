package sdk.travelrely.lib.device.util;

import android.text.TextUtils;
import android.util.Log;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.util.LogUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/4.
 * ＊ sdk.travelrely.lib.device.util
 * ＊ 11:15
 */
public class BLEUtil {

    private static String TAG = BLEUtil.class.getSimpleName();


    /**
     *
     * @param key
     * @return
     */
    public static byte[] getKeySaveReq(String key) {
        if (TextUtils.isEmpty(key)) {
            LogUtil.e(TAG, "send key to box, but key is empty");
            return null;
        }
        byte[] sendReq = new byte[Constant.keySaveReq.length];
        System.arraycopy(Constant.keySaveReq,0,sendReq,0,Constant.keySaveReq.length);
        System.arraycopy(key.getBytes(), 0, sendReq, 5, 6);
        return sendReq;
    }
}
