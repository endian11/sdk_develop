package sdk.travelrely.lib.device.bluetooth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Set;

import sdk.travelrely.lib.TRSdk;
import sdk.travelrely.lib.device.util.BLEUtil;
import sdk.travelrely.lib.minterface.ITRCallback;
import sdk.travelrely.lib.util.AndroidUtil;
import sdk.travelrely.lib.util.LogUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device.bluetooth
 * ＊ 18:13
 */
public class BlueToothScan extends Thread implements BlueToothCallbackCreator.BTResult {
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private ITRCallback mCallback;
    private Object scanCallback;
    private Boolean serching = false;

    /**
     * 消息为设备类型消息
     **/
    public static final int TYPE_DEVICE = 1;

    /**
     * 消息为通知类型消息
     **/
    public static final int TYPE_ACTION = 0;

    /**
     * 设置蓝牙设备搜索时长 默认20秒钟
     **/
    private static final long SCAN_PERIOD = 20000;

    /**
     * 初始化蓝牙搜索器
     **/
    public BlueToothScan(ITRCallback callback) {
        mCallback = callback;
        scanCallback = BlueToothCallbackCreator.getDefault(this);
    }

    public Boolean isSearching() {
        return serching;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        super.run();

        if (!checkBlueTooth()) {
            stopScan();
        } else {
            if (bluetoothDevices == null) {
                bluetoothDevices = new ArrayList<>();
            }

            Set<BluetoothDevice> devices = TRSdk.getInstance().getBlueAdapter().getBondedDevices();
            for (BluetoothDevice device : devices) {
                bluetoothDevices.add(device);
                if (mCallback != null) {
                    mCallback.findPairedDevice(device);
                }
            }

            serching = true;

            if (AndroidUtil.IS_LOLLIPOP) {
                BluetoothLeScanner scanner = TRSdk.getInstance().getBlueAdapter().getBluetoothLeScanner();
                if (scanner != null) {
                    scanner.startScan((ScanCallback) scanCallback);
                }
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    TRSdk.getInstance().getBlueAdapter().startLeScan((BluetoothAdapter.LeScanCallback) scanCallback);
                }
            }

            mHandler.postDelayed(OverTime, SCAN_PERIOD);
        }
    }

    /**
     * 超出搜索时间，停止蓝牙设备搜索
     */
    private Runnable OverTime = new Runnable() {
        @Override
        public void run() {
            stopScan();
        }
    };

    /**
     * 检查BlueTooth
     *
     * @return 返回BlueTooth实例
     */
    private synchronized Boolean checkBlueTooth() {
        if (!TRSdk.getInstance().getBlueAdapter().isEnabled()) {
            if (TRSdk.getInstance().getBlueAdapter().enable()) {
                while (!TRSdk.getInstance().getBlueAdapter().isEnabled()) {
                    LogUtil.d("prapare bluetooth...");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 发送事件回调
     * 因为会发生，非UI线程 无法更新界面错误 故使用Handler 发送事件刷新
     *
     * @param action 事件类型
     * @param device 蓝牙设备
     */
    private void dispatchCallback(int action, BluetoothDevice device) {
        if (mHandler == null) return;
        Message message = mHandler.obtainMessage();
        message.arg1 = action;
        Bundle bundle = new Bundle();
        bundle.putParcelable("device", device);
        message.setData(bundle);
        message.sendToTarget();
    }

    /**
     * @param action
     * @param value
     */
    private void dispatchCallback(int action, Object value) {
        if (mHandler == null) return;
        Message message = mHandler.obtainMessage();
        message.arg1 = action;
        message.obj = value;
        message.sendToTarget();
    }

    /**
     * 发送数据给UI界面，更新绑定数据
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.arg1) {
                case TYPE_DEVICE:
                    if (mCallback != null) {
                        Bundle bundle = msg.getData();
                        BluetoothDevice device = bundle.getParcelable("device");
                        mCallback.findDevice(device);
                    }
                    break;

                case TYPE_ACTION:
                    if (mCallback != null) {
                        mCallback.faild(msg.obj);
                    }
                    break;
            }

        }
    };

    /**
     * 停止蓝牙搜索
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void stopScan() {
        LogUtil.d("stop scan search");
        if (mHandler != null) {
            mHandler.removeCallbacks(OverTime);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (TRSdk.getInstance().getBlueAdapter() != null) {
            if (AndroidUtil.IS_LOLLIPOP) {
                BluetoothLeScanner scanner = TRSdk.getInstance().getBlueAdapter().getBluetoothLeScanner();
                if (scanner != null) {
                    scanner.stopScan((ScanCallback) scanCallback);
                }
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    TRSdk.getInstance().getBlueAdapter().stopLeScan((BluetoothAdapter.LeScanCallback) scanCallback);
                }
            }
            interrupt();
        }

        serching = false;
    }

    /**
     * 派发扫描结果
     *
     * @param device
     * @param scanRecord
     */
    @Override
    public void result(BluetoothDevice device, byte[] scanRecord) {
        if (BLEUtil.isBox(scanRecord)) {
            if (!bluetoothDevices.contains(device)) {
                bluetoothDevices.add(device);
                LogUtil.d("bluetoothDevices size:" + bluetoothDevices.size());
                dispatchCallback(TYPE_DEVICE, device);
            }
        }
    }
}
