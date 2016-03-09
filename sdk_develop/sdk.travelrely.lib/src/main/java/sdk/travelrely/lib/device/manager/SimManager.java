package sdk.travelrely.lib.device.manager;

import android.text.TextUtils;

import sdk.travelrely.lib.TRAction;
import sdk.travelrely.lib.util.LogUtil;
import sdk.travelrely.lib.util.SharedUtil;

/**
 * Created by john on 2016/3/9.
 */
public class SimManager {
    public void readSimInfo(){
        if (BLEManager.getDefault().isConnect()){
            //蓝牙已经连接 校验key
            BoxManager.getDefault().sendChkKey();

            //校验key，失败 。整个任务失败
        }else{
            //蓝牙未连接，则连接蓝牙
            String mac = SharedUtil.get(TRAction.SHARED_BT_ADDR, "");
            if (!TextUtils.isEmpty(mac)){
                BLEManager.getDefault().connect(mac);

            }else{
                LogUtil.d("读取sim卡信息失败");
            }

        }
    }
}