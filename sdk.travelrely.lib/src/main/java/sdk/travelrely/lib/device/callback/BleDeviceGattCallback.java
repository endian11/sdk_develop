package sdk.travelrely.lib.device.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import java.util.List;
import java.util.UUID;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.device.manager.BLEManager;
import sdk.travelrely.lib.device.manager.BoxManager;
import sdk.travelrely.lib.device.message.ProcessMessage;
import sdk.travelrely.lib.util.ByteUtil;
import sdk.travelrely.lib.util.LogUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device
 * ＊ 14:24
 */
public class BleDeviceGattCallback extends BluetoothGattCallback {

    private static BleDeviceGattCallback callback = new BleDeviceGattCallback();
    private static final String TAG = BleDeviceGattCallback.class.getSimpleName();

    public static BleDeviceGattCallback getDefault() {
        return callback;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        LogUtil.d(TAG, "onConnectionStateChange : status:" + status + "   newState:" + newState);

        //TODO 连接状态发生改变
        if (status != BluetoothGatt.GATT_SUCCESS)
        {
            BLEManager.getDefault().setConnect(false);
            return;
        }else if (newState == BluetoothProfile.STATE_CONNECTED) {
            //TODO 连接成功
            BLEManager.getDefault().setConnect(true);
            LogUtil.d("BluetoothGatt is connect success");
            BLEManager.getDefault().discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            //TODO 连接断开
            BLEManager.getDefault().setConnect(false);
            LogUtil.d("BluetoothGatt is disconnect");
        } else {
            BLEManager.getDefault().reconnect();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        LogUtil.d("BluetoothGatt ->" + status);
        if (gatt == null) {
            LogUtil.d("gatt is null");
            return;
        }

        if (status == BluetoothGatt.GATT_SUCCESS) {
            LogUtil.d("BluetoothGatt.GATT_SUCCESS");

            BluetoothGattService service = gatt.getService(UUID.fromString(Constant.SERVICE_UUID));
            if (service == null) return;
            List<BluetoothGattCharacteristic> results = service.getCharacteristics();
            if (results != null && results.size() > 0) {
                for (BluetoothGattCharacteristic gattChar : results) {
                    // UUID_RECV是接收蓝牙盒子数据的Characteristic
                    if (gattChar == null || gattChar.getUuid() == null) continue;
                    if (gattChar.getUuid().toString().equals(Constant.UUID_RX)) {
                        /**
                         * 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发
                         * mOnDataAvailable.onCharacteristicWrite()
                         */
                        LogUtil.d("BluetoothGatt equals UUID_RX");
                        gatt.setCharacteristicNotification(gattChar, true);
                    }

                    if (gattChar.getUuid().toString().equals(Constant.UUID_TX)) {
                        LogUtil.d("BluetoothGatt equals UUID_TX");
                        gatt.setCharacteristicNotification(gattChar, true);
                        BLEManager.getDefault().setmCharacter(gattChar);

                        //TODO 执行其它
                        LogUtil.d("BluetoothGatt send order");
                        BoxManager.getDefaultBoxManager().AutoSet();
                    }
                }
            }
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        //TODO 未知
        LogUtil.d("onCharacteristicRead ->" + ByteUtil.toHexString(characteristic.getValue()));
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        //TODO 收蓝牙数据
        LogUtil.d("onCharacteristicChanged-> " + ByteUtil.toHexString(characteristic.getValue()));

        ProcessMessage.process(characteristic.getValue());
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            LogUtil.i(TAG, "BT TX succ：" + ByteUtil.toHexString(
                    characteristic.getValue()));
            //TODO 发送成功

            return;
        }
        LogUtil.e(TAG, "BT TX fail:" + ByteUtil.toHexString(
                characteristic.getValue()));
        //TODO 发送失败
    }
}
