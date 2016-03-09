package sdk.travelrely.lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;

import sdk.travelrely.lib.device.bluetooth.BlueToothScan;
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
    public void clearDeviceCallback() {
        mCallback = null;
    }

    /**
     * 释放占用的资源
     */
    public void release() {
        clearDeviceCallback();
    }

    @Override
    public void startScan(@NonNull ITRCallback callback) {
        if (scan != null) {
            if (scan.isSearching()) {
                if(mCallback!=null)
                    mCallback.faild("bluetooth is in searching....please stop it first");
                return;
            } else {
                scan = null;
            }
        }

        mCallback = callback;
        scan = new BlueToothScan(mCallback);
        scan.start();
    }

    /**
     * connect the bluetooth box
     */
    @Override
    public Boolean pairByDevice(@NonNull BluetoothDevice device) {
        //TODO
        stopScan();

        BLEManager.getDefault().connect(device.getAddress());
        return true;
    }


    @Override
    public void pairByUUID(String UUID) {

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
    }

}
