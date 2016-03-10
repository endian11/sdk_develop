package sdk.travelrely.lib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import sdk.travelrely.lib.config.SDKConfig;
import sdk.travelrely.lib.device.exception.BLEException;
import sdk.travelrely.lib.device.manager.BLEManager;
import sdk.travelrely.lib.minterface.IDeviceface;
import sdk.travelrely.lib.minterface.ITRCallback;
import sdk.travelrely.lib.minterface.TRAlertCallback;
import sdk.travelrely.lib.minterface.TRSdkCallback;
import sdk.travelrely.lib.service.BleService;
import sdk.travelrely.lib.util.LogUtil;
import sdk.travelrely.lib.util.ToastUtil;

/**
 * Created by weihaichao on 16/2/29.
 *
 */
public class TRSdk implements IDeviceface,TRSdkCallback {
    private static TRSdk sdk;
    private Context mContext;
    private TRDevice mDevice;
    private BluetoothAdapter mAdapter;

    /**
     * 初始化SDK
     *
     * @param context
     */
    public static TRSdk init(Context context) throws RuntimeException {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 ||
                !context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            throw new RuntimeException("sdk version is not supported");
        }

        sdk = new TRSdk(context);
        return sdk;
    }

    /**
     * 设置是否启用调试功能
     *
     * @param value
     */
    public void setDebug(Boolean value) {
        SDKConfig.DEBUG = value;
    }

    /**
     * 获取单例sdk对象
     *
     * @return
     */
    public static TRSdk getInstance() {
        return sdk;
    }

    public BluetoothAdapter getBlueAdapter() {
        return mAdapter;
    }

    /**
     * 入口函数
     *
     * @param context
     */
    private TRSdk(Context context) {
        mContext = context;
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        initModules();
    }

    /**
     * 获取上下文实例
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 初始化各模块
     */
    private void initModules() {
        createDevice();
    }

    /**
     * 获取硬件操作模块
     *
     * @return
     */
    private synchronized void createDevice() {
        if (!IsError()) {
            if (mDevice == null) mDevice = new TRDevice(mContext);
        }
    }

    /**
     * 断开蓝牙连接
     */
    public void disconnectBlueTooth() {
        BLEManager.getDefault().disconnect();
    }

    /**
     * 检测错误 条件成立则返回 true
     *
     * @return
     */
    private Boolean IsError() {
        if (mContext == null) return true;

        return false;
    }

    @Override
    public void startScan(ITRCallback callback) {
        if (mDevice == null) createDevice();
        try {
            mDevice.startScan(callback);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopScan() {
        if (mDevice == null) createDevice();
        mDevice.stopScan();
    }

    @Override
    public Boolean pairByDevice(BluetoothDevice device) {
        if (mDevice == null) createDevice();
        return mDevice.pairByDevice(device);
    }

    @Override
    public Boolean pairByMacAddress(String macaddress) {
        if(mDevice == null) createDevice();
        return mDevice.pairByMacAddress(macaddress);
    }

    @Override
    public void faild(Object message) {
        LogUtil.d("error:"+message);
        disconnectBlueTooth();
        ToastUtil.showLong(mContext,message.toString());
    }

    @Override
    public void alert(Object message) {
        LogUtil.d("alert:"+message);
        disconnectBlueTooth();
        ToastUtil.showLong(mContext,message.toString());
    }
}
