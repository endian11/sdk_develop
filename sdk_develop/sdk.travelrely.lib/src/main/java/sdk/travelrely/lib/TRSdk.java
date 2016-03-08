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

import sdk.travelrely.lib.config.SDKConfig;
import sdk.travelrely.lib.device.manager.BLEManager;
import sdk.travelrely.lib.minterface.IDeviceface;
import sdk.travelrely.lib.minterface.ITRCallback;
import sdk.travelrely.lib.service.BleService;
import sdk.travelrely.lib.util.LogUtil;

/**
 * Created by weihaichao on 16/2/29.
 *
 */
public class TRSdk {
    private static TRSdk sdk;
    private Context mContext;
    private TRDevice device;
    private IBleServiceAidl bleService;
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

    public BluetoothAdapter getBlueAdapter(){
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

//        Intent mIntent = new Intent();
//        mIntent.setPackage(mContext.getPackageName());
//        mIntent.setAction(BleService.ACTION);
//        mContext.startService(mIntent);
//        mContext.bindService(mIntent,connection,Context.BIND_AUTO_CREATE);

        initModules();
    }

    private IBleCallback.Stub mBleCallback = new IBleCallback.Stub() {
        @Override
        public void found() throws RemoteException {
            LogUtil.d("bleservice found device...");
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleService = IBleServiceAidl.Stub.asInterface(service);
            try {
                bleService.registBleCallback(mBleCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bleService = null;
        }
    };

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
        device = getDevice();
    }

    /**
     * 获取硬件操作模块
     *
     * @return
     */
    public synchronized TRDevice getDevice() {
        if (!IsError()) {
            if (device == null) device = new TRDevice(mContext);
            return device;
        }

        return null;
    }

    public void disconnectBlueTooth(){
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

}
