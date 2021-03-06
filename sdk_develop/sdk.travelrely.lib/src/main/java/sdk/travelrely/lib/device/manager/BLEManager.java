package sdk.travelrely.lib.device.manager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import sdk.travelrely.lib.TRSdk;
import sdk.travelrely.lib.device.callback.BleDeviceGattCallback;
import sdk.travelrely.lib.device.exception.BLEException;
import sdk.travelrely.lib.util.ByteUtil;
import sdk.travelrely.lib.util.LogUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.manager
 * ＊ 14:31
 */
public class BLEManager {

    private static final String TAG = BLEManager.class.getSimpleName();
    //单例实例
    private static BLEManager mInstance = new BLEManager();
    //是否已经连接成功
    private Boolean isConnect = false;
    private String currentMac;
    private BluetoothGatt mGatt;
    private BluetoothGattCharacteristic mCharacter;
    private Handler mHandler;

    private int retry = 0;

    private BLEManager() {
        mHandler = new Handler(TRSdk.getInstance().getContext().getMainLooper());
    }

    public static BLEManager getDefault() {
        return mInstance;
    }

    /**
     * 获取蓝牙设备是否已经连接成功
     *
     * @return
     */
    public Boolean isConnect() {
        return isConnect;
    }


    public void setConnect(Boolean connnect) {
        isConnect = connnect;
        if (!connnect) {
            release();
        }
    }

    public void release() {
        LogUtil.d("BLEManager do release");
        if(mGatt != null){
            mGatt.close();
        }

        if (mHandler != null) {
            LogUtil.d("BluetoothGatt handler is clear");
            mHandler.removeCallbacksAndMessages(null);
        }

        mGatt = null;
        mCharacter = null;
    }

    public BluetoothGatt getBluetoothGatt() {
        return mGatt;
    }

    public void setBluetoothGatt(BluetoothGatt value) {
        mGatt = value;
    }


    public BluetoothGattCharacteristic getmCharacter() {
        return mCharacter;
    }

    public void setmCharacter(BluetoothGattCharacteristic mCharacter) {
        this.mCharacter = mCharacter;
    }

    /**
     * 连接蓝牙设备
     *
     * @param address 蓝牙设备mac地址
     * @return 是否成功
     */
    public Boolean connect(final String address) {
        if (currentMac != null && address.equals(currentMac) && isConnect() && mGatt != null) {
            LogUtil.d(address + " is connected ! refused it");
            BoxManager.getDefault().CheckTask();
            return false;
        }

        if (isConnect()) {
            disconnect();
        }

        currentMac = address;

        final BluetoothDevice device = TRSdk.getInstance().getBlueAdapter().getRemoteDevice(address);

        if (device == null) {
            LogUtil.d(TAG, "bluetooth device is null , cancel connect");
            return false;
        }


        LogUtil.d("开始执行连接蓝牙盒子");

        if (android.os.Build.FINGERPRINT.contains(" Meizu/meizu_m2cnote")) {
            //魅蓝note2
           device.connectGatt(TRSdk.getInstance().getContext(), true, BleDeviceGattCallback.getDefault());
        } else if (android.os.Build.FINGERPRINT.contains(" Xiaomi/cancro")) {
            //MI 3C
            device.connectGatt(TRSdk.getInstance().getContext(), true, BleDeviceGattCallback.getDefault());
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    device.connectGatt(TRSdk.getInstance().getContext(), false, BleDeviceGattCallback.getDefault());
                }
            });
        }

        LogUtil.i(TAG, "Trying to create a new connection : " + address);

        return true;
    }

    /**
     * 连接失败
     */
    public void ConnectFaild() {
        setConnect(false);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (retry < 2) {
                    retry += 1;
                    if (currentMac != null && !currentMac.equals("")) {
                        LogUtil.d("reconnect " + retry + " ! current mac address is " + currentMac);
                        connect(currentMac);
                    } else {
                        LogUtil.d("reconnect faild ! current mac address is null or empty");
                        TRSdk.getInstance().faild("蓝牙连接失败");
                    }
                } else {
                    retry = 0;
                    LogUtil.d("reconnect for 3 times complete , faild");
                    TRSdk.getInstance().faild("蓝牙连接失败");
                }
            }
        }, 200);

    }

    /**
     * 断开连接
     */
    public void disconnect() {
        LogUtil.d("BLEManager connection is disconnect");
        setConnect(false);
    }

    /**
     * 扫描服务
     */
    public void discoverServices() {
        LogUtil.i(TAG, "mGatt->:" + mGatt);
        if (mGatt == null) {
            return;
        }
        ;
        // Attempts to discover services after successful connection.
        boolean rslt = mGatt.discoverServices();
        LogUtil.i(TAG, "Attempting to start service discovery:" + rslt);
    }

    /**
     * 发送数据
     *
     * @param message
     */
    public Boolean send(String message) {
        LogUtil.i(TAG, "send message:" + message);
        if (!TextUtils.isEmpty(message)) {
            try {
                return send(ByteUtil.getBytes(message));
            } catch (BLEException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 发送字节数组至蓝牙设备
     *
     * @param message 字节数组
     */
    public synchronized Boolean send(byte[] message) throws BLEException {
        if (mGatt == null) {
            throw new BLEException("send(byte[] message)  mGatt is null");
        }
        if (mCharacter == null) {
            throw new BLEException("send(byte[] message)  mCharacter is null");
        }
        if (message == null) {
            throw new BLEException("send(byte[] message)  byte[] is null");
        }
        if (mCharacter.setValue(message)) {
            if (!mGatt.writeCharacteristic(mCharacter)) {
                LogUtil.d(TAG, "send message " + ByteUtil.toHexString(message) + " faild...");
                return false;
            }
        }else{
            LogUtil.d(TAG, "mCharacter setValue faild...");
            return false;
        }

        return true;
    }
}
