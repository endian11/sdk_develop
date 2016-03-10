package sdk.travelrely.lib.device.manager;

import android.os.Message;
import android.text.TextUtils;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.TRAction;
import sdk.travelrely.lib.device.BLE;
import sdk.travelrely.lib.device.exception.BLEException;
import sdk.travelrely.lib.device.util.BLEUtil;
import sdk.travelrely.lib.obersver.DeviceManagerObersver;
import sdk.travelrely.lib.util.LogUtil;
import sdk.travelrely.lib.util.SharedUtil;

/**
 * Created by john on 2016/3/9.
 */
public class SimManager extends BaseManager {

    private static SimManager manager = new SimManager();

    private SimManager() {
        super();
    }

    public static SimManager getDefault() {
        return manager;
    }

    @Override
    public void receiveMessage(Message message) {
        super.receiveMessage(message);

        switch (message.what) {
            case BLE.ACTION_READ_SIMINFO:
                LogUtil.d("receive BoxManager.ACTION_READ_SIMINFO :" + message.obj);
                break;

        }
    }
    /**
     * 读取SIM卡信息
     */
    public void ReadSim() {
        BLE.CURRENT_ACTION = BLE.ACTION_READ_SIMINFO;
        LogUtil.d("send ReadSim cmd");
        try {
            BLEManager.getDefault().send(Constant.simInfoReq);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param needCheck 是否检查key存在
     */
    public void readSimInfo(Boolean needCheck) {

    }
}
