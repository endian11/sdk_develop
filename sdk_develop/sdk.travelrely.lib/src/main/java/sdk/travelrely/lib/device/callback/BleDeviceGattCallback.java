package sdk.travelrely.lib.device.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import java.util.List;
import java.util.UUID;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.device.manager.BLEManager;
import sdk.travelrely.lib.device.manager.BoxManager;
import sdk.travelrely.lib.device.message.ProcessMessage;
import sdk.travelrely.lib.device.util.BLEUtil;
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

        //TODO GATT执行失败
        if (status != BluetoothGatt.GATT_SUCCESS) {
            if (status == 133 || status == 129) {
                BLEManager.getDefault().setConnect(false);
                BLEManager.getDefault().ConnectFaild();
            }
            return;
        }

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            //TODO 连接成功
            BLEManager.getDefault().setConnect(true);
            BLEManager.getDefault().setBluetoothGatt(gatt);
            LogUtil.d("BluetoothGatt is connect success");
            BLEManager.getDefault().discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            //TODO 连接断开
            LogUtil.d("BluetoothGatt is disconnect");
            BLEManager.getDefault().setConnect(false);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        //TODO 当发现蓝牙设备service 做相应处理
        super.onServicesDiscovered(gatt, status);
        LogUtil.d("BluetoothGatt -> status " + status);
        if (gatt == null) {
            LogUtil.d("gatt is null");
            return;
        }

        if (status == BluetoothGatt.GATT_SUCCESS) {
            LogUtil.d("BluetoothGatt.GATT_SUCCESS");

            BluetoothGattService service = gatt.getService(UUID.fromString(Constant.SERVICE_UUID));
            if (service == null) return;

            // -----Service的字段信息-----//
            int type = service.getType();
            LogUtil.e(TAG, "-->service type:" + BLEUtil.getServiceType(type));
            LogUtil.e(TAG, "-->includedServices size:"
                    + service.getIncludedServices().size());
            LogUtil.e(TAG, "-->service uuid:" + service.getUuid());

            List<BluetoothGattCharacteristic> results = service.getCharacteristics();
            if (results != null && results.size() > 0) {
                for (BluetoothGattCharacteristic gattChar : results) {
                    // UUID_RECV是接收蓝牙盒子数据的Characteristic
                    if (gattChar == null || gattChar.getUuid() == null) continue;
                    LogUtil.e(TAG, "---->char uuid:" + gattChar.getUuid());
                    int permission = gattChar.getPermissions();
                    LogUtil.e(TAG,
                            "---->char permission:"
                                    + BLEUtil.getCharPermission(permission));
                    int property = gattChar.getProperties();
                    LogUtil.e(TAG, "---->char property:" + BLEUtil.getCharPropertie(property));
                    byte[] data = gattChar.getValue();
                    if (data != null && data.length > 0) {
                        LogUtil.e(TAG, "---->char value:" + new String(data));
                    }

                    if (gattChar.getUuid().toString().equals(Constant.UUID_RX)) {
                        /**
                         * 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发
                         * mOnDataAvailable.onCharacteristicWrite()
                         */
                        LogUtil.d("BluetoothGatt equals UUID_RX");
                        gatt.setCharacteristicNotification(gattChar, true);

                        // -----Descriptors的字段信息-----//
                        List<BluetoothGattDescriptor> gattDescriptors = gattChar
                                .getDescriptors();
                        for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
                            LogUtil.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                            int descPermission = gattDescriptor.getPermissions();
                            LogUtil.e(TAG,
                                    "-------->desc permission:"
                                            + BLEUtil.getDescPermission(descPermission));
                            byte[] desData = gattDescriptor.getValue();
                            if (desData != null && desData.length > 0) {
                                LogUtil.e(TAG, "-------->desc value:" + new String(desData));
                            }
                        }
                    }

                    if (gattChar.getUuid().toString().equals(Constant.UUID_TX)) {
                        LogUtil.d("BluetoothGatt equals UUID_TX");
                        gatt.setCharacteristicNotification(gattChar, true);
                        BLEManager.getDefault().setmCharacter(gattChar);
                        //TODO 执行其它
                        LogUtil.d("BluetoothGatt send order");
                        BoxManager.getDefault().CheckTask();
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
        //TODO 当向蓝牙设备发送数据结果
        switch (status) {
            case BluetoothGatt.GATT_SUCCESS:
                LogUtil.d(TAG, "Characteristic write succ：" + ByteUtil.toHexString(
                        characteristic.getValue()));
                break;// 写入成功
            case BluetoothGatt.GATT_FAILURE:
                LogUtil.d(TAG, "Characteristic write faild：");
                break;// 写入失败
            case BluetoothGatt.GATT_WRITE_NOT_PERMITTED:
                LogUtil.d(TAG, "Characteristic no write permission");
                break;// 没有写入的权限
        }
    }
}
