package sdk.travelrely.lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import sdk.travelrely.lib.device.bluetooth.BlueToothScan;
import sdk.travelrely.lib.device.exception.BLEException;
import sdk.travelrely.lib.device.manager.BLEManager;
import sdk.travelrely.lib.minterface.IDeviceface;
import sdk.travelrely.lib.minterface.ITRCallback;
import sdk.travelrely.lib.util.LogUtil;

/**
 * Created by weihaichao on 16/2/29.
 */

public class TRDevice implements IDeviceface {

    private static final String TAG = TRDevice.class.getSimpleName();

    private ITRCallback mCallback;
    private Context mContext;
    BlueToothScan scan;

    public TRDevice(Context context) {
        mContext = context;
    }

    /**
     * 清除回调函数
     */
    private void clearDeviceCallback() {
        mCallback = null;
    }

    /**
     * 释放占用的资源
     */
    private void release() {
        clearDeviceCallback();
    }

    /**
     * 开始搜索蓝牙设备
     *
     * @param callback
     */
    @Override
    public void startScan(ITRCallback callback) throws BLEException {
        if (callback == null) {
            if (mCallback != null) {
                throw new BLEException("ITRCallback can not be null");
            }
        }

        mCallback = callback;

        if (scan != null) {
            if (scan.isSearching()) {
                if (mCallback != null)
                    mCallback.faild("bluetooth is in searching....please stop it first");
                return;
            } else {
                scan = null;
            }
        }

        scan = new BlueToothScan(mCallback);
        scan.start();
    }

    /**
     * connect the bluetooth box
     */
    @Override
    public Boolean pairByDevice(BluetoothDevice device) {
        //TODO
        stopScan();
        if (device == null) {
            if (mCallback != null)
                mCallback.faild("BluetoothDevice is null");
            return false;
        }
        return BLEManager.getDefault().connect(device.getAddress());
    }

    @Override
    public Boolean pairByMacAddress(String macaddress) {
        stopScan();
        if (TextUtils.isEmpty(macaddress)) {
            if (mCallback != null)
                mCallback.faild("macAddress can not be empty");
        }
        return BLEManager.getDefault().connect(macaddress);
    }

    /**
     * stop search the bluetooth box
     */
    @Override
    public void stopScan() {
        //TODO
        if (scan != null) {
            LogUtil.d("stop search..");
            scan.stopScan();
            scan = null;
        }

        release();
    }

}
