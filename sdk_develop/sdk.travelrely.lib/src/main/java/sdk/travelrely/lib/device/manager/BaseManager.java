package sdk.travelrely.lib.device.manager;

import android.os.Message;

import sdk.travelrely.lib.minterface.IDeviceManager;
import sdk.travelrely.lib.obersver.DeviceManagerObersver;
import sdk.travelrely.lib.util.LogUtil;

/**
 * ＊ sdk_develop
 * Created by weihaichao on 16/3/9.
 * ＊ sdk.travelrely.lib.device.manager
 * ＊ 15:39
 */
public class BaseManager implements IDeviceManager {

    public BaseManager() {
        initObserver();
    }

    private void initObserver() {
        LogUtil.d("add obersver to deviceobserver");
        DeviceManagerObersver.get().add(this);
    }

    @Override
    public void receiveMessage(Message message) {

    }
}
