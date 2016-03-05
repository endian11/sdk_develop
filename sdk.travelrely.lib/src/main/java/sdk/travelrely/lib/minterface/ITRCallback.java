package sdk.travelrely.lib.minterface;

import android.bluetooth.BluetoothDevice;

/**
 * Created by weihaichao on 16/2/29.
 */
public interface ITRCallback {
    /**
     * 出现错误 发送错误事件
     * @param message
     */
    void faild(Object message);

    /**
     * 发现新设备
     * @param device
     */
    void findDevice(BluetoothDevice device);

    /**
     * 发现已经配对过的设备
     * @param device
     */
    void findPairedDevice(BluetoothDevice device);
}
