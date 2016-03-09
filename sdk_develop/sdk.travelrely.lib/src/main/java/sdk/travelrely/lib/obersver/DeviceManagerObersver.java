package sdk.travelrely.lib.obersver;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import sdk.travelrely.lib.minterface.IDeviceManager;

/**
 * ＊ sdk_develop
 * Created by weihaichao on 16/3/9.
 * ＊ sdk.travelrely.lib.obersver
 * ＊ 15:56
 */
public class DeviceManagerObersver {

    private List<IDeviceManager> managers = new ArrayList<>();

    private static DeviceManagerObersver obersver = new DeviceManagerObersver();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            receiveMessage(msg);
        }
    };


    public Handler getHandler() {
        return mHandler;
    }

    /**
     * 获取观察器实例
     * @return
     */
    public static DeviceManagerObersver get() {
        return obersver;
    }

    /**
     * 收到数据解析结果
     * @param message
     */
    private void receiveMessage(Message message) {
        if (managers.size() <= 0) return;
        for (IDeviceManager manager : managers) {
            manager.receiveMessage(message);
        }
    }

    /**
     * 添加观察对象
     * @param manager
     */
    public void add(IDeviceManager manager) {
        managers.add(manager);
    }

    /**
     * 移除观察对象
     * @param manager
     */
    public void remove(IDeviceManager manager) {
        if (managers.contains(manager)) {
            managers.remove(manager);
        }
    }


}
