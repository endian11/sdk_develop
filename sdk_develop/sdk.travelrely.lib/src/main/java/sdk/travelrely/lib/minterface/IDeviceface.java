package sdk.travelrely.lib.minterface;

import android.bluetooth.BluetoothDevice;

import sdk.travelrely.lib.device.exception.BLEException;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/3.
 * ＊ sdk.travelrely.lib.minterface
 * ＊ 09:59
 */
public interface IDeviceface {
    void startScan(ITRCallback callback) throws BLEException;
    void stopScan();
    Boolean pairByDevice(BluetoothDevice device);
    Boolean pairByMacAddress(String macaddress);
}
