package sdk.travelrely.lib.device.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;

import sdk.travelrely.lib.util.AndroidUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device.bluetooth
 * ＊ 18:24
 */
public class BlueToothCallbackCreator {

    /** 获取与系统版本所对应的回调 **/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Object getDefault(final BTResult BTresult) {
        if (AndroidUtil.IS_LOLLIPOP) {
            return new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (result.getScanRecord() != null)
                        BTresult.result(result.getDevice(),result.getScanRecord().getBytes());
                }
            };
        } else {
            return new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    BTresult.result(device,scanRecord);
                }
            };
        }
    }


    public interface BTResult {
        void result(BluetoothDevice device,byte[] scanRecord);
    }
}
