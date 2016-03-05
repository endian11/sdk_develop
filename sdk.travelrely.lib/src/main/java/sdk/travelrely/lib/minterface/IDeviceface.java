package sdk.travelrely.lib.minterface;

import android.bluetooth.BluetoothDevice;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/3.
 * ＊ sdk.travelrely.lib.minterface
 * ＊ 09:59
 */
public interface IDeviceface {
    void startScan();
    void startScan(ITRCallback callback);
    void stopScan();
    Boolean pairByDevice(BluetoothDevice device);
    void pairByUUID(String UUID);
}
