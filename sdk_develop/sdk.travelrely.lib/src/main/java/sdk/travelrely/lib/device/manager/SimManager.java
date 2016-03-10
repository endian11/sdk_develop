package sdk.travelrely.lib.device.manager;

import android.os.Message;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.device.exception.BLEException;

/**
 * Created by john on 2016/3/9.
 */
public class SimManager extends BaseManager {

    private static SimManager manager = new SimManager();

    private SimManager() {
        super();
    }


    public static final int ACTION_READ_SIM = 0;
    public static  int CURRENT_ACTION = -1;

    public static SimManager getDefault() {
        return manager;
    }

    @Override
    public void receiveMessage(Message message) {
        super.receiveMessage(message);

        switch (message.what) {

            case ACTION_READ_SIM:
                //收到SIM卡信息解析完之后的数据
                               break;
        }
    }

    /**
     * 读取sim卡信息
     */
    public void readSimInfo() {
        CURRENT_ACTION = ACTION_READ_SIM;
        try {
            BLEManager.getDefault().send(Constant.simInfoReq);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }

}
