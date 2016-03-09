package com.travelrely.core.nrs.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;
import java.util.UUID;

import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.util.LogUtil;

public class BleManager
{
    private final static String TAG = BleManager.class.getSimpleName();

    private BluetoothManager mBtManager;
    private BluetoothAdapter mBtAdapter;
    //private String mBtDevAddr;
    private BluetoothGatt mBtGatt;
    
    private boolean isConnected = false;

    public interface OnConnectListener
    {
        public void onConnect(BluetoothGatt gatt);
        public void onConnectFail(BluetoothGatt gatt);
        public void onDisconnect(BluetoothGatt gatt);
    }

    public interface OnServiceDiscoverListener
    {
        public void onServiceDiscover(BluetoothGatt gatt);
    }

    public interface OnBtDataRecvListener
    {
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic, int status);

        public void onCharacteristicWrite(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic);
    }
    
    public interface OnBtDataSendListener
    {
        public void onWriteSucc();
        public void onWriteFail(byte[] msg);
        
        public void onSendSucc();
        public void onSendFail(byte[] msg);
    }

    private OnConnectListener mOnConnectListener;
    private OnServiceDiscoverListener mOnServiceDiscoverListener;
    private OnBtDataRecvListener mOnDataAvailableListener;
    private OnBtDataSendListener mOnDataSendListener;
    private Context mContext;

    public void setOnConnectListener(OnConnectListener l)
    {
        mOnConnectListener = l;
    }

    public void setOnServiceDiscoverListener(OnServiceDiscoverListener l)
    {
        mOnServiceDiscoverListener = l;
    }

    public void setOnDataAvailableListener(OnBtDataRecvListener l)
    {
        mOnDataAvailableListener = l;
    }
    
    public void setOnDataSendListener(OnBtDataSendListener l)
    {
        mOnDataSendListener = l;
    }

    public BleManager(Context c)
    {
        mContext = c;
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                int newState)
        {
            if (mOnConnectListener == null)
            {
                LogUtil.e(TAG, "mOnConnectListener == null");
                return;
            }
            
            if (gatt == null)
            {
                LogUtil.e(TAG, "gatt == null");
                mOnConnectListener.onConnectFail(gatt);
                return;
            }

            if (status != BluetoothGatt.GATT_SUCCESS)
            {
                LogUtil.e(TAG, "onConnectionStateChange status=" + status);
                mOnConnectListener.onConnectFail(gatt);
                return;
            }
            
            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
                isConnected = true;
                mBtGatt = gatt;
                LogUtil.i(TAG, "Connected to GATT server.");
                if (mOnConnectListener != null)
                {
                    mOnConnectListener.onConnect(gatt);
                }
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED)
            {
                isConnected = false;
                LogUtil.i(TAG, "Disconnected from GATT server.");
                if (mOnConnectListener != null)
                {
                    mOnConnectListener.onDisconnect(gatt);
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS
                    && mOnServiceDiscoverListener != null)
            {
                mOnServiceDiscoverListener.onServiceDiscover(gatt);
            }
            else
            {
                LogUtil.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic, int status)
        {
            if (mOnDataAvailableListener != null)
            {
                mOnDataAvailableListener.onCharacteristicRead(gatt,
                        characteristic, status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic)
        {
            if (mOnDataAvailableListener != null)
            {
                mOnDataAvailableListener.onCharacteristicWrite(gatt,
                        characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic, int status)
        {
            if (mOnDataSendListener == null)
            {
                LogUtil.e(TAG, "mOnDataSendListener == null");
            }
            
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                LogUtil.i(TAG, "BT TX succ：" + ByteUtil.toHexString(
                        characteristic.getValue()));
                mOnDataSendListener.onSendSucc();
                return;
            }

            LogUtil.e(TAG, "BT TX fail:" + ByteUtil.toHexString(
                    characteristic.getValue()));
            mOnDataSendListener.onSendFail(characteristic.getValue());
        }
    };

    public boolean initialize()
    {
        /* For API level 18 and above, get a reference to BluetoothAdapter
         through BluetoothManager.*/
        if (mBtManager == null)
        {
            mBtManager = (BluetoothManager) mContext
                    .getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBtManager == null)
            {
                LogUtil.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBtAdapter = mBtManager.getAdapter();
        if (mBtAdapter == null)
        {
            LogUtil.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    public boolean connect(final String address)
    {
        if (mBtAdapter == null || address == null)
        {
            LogUtil.w(TAG,
                    "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        final BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
        if (device == null)
        {
            LogUtil.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        LogUtil.i(1, "AtoB001");
        /*
         *  We want to directly connect to the device, so we are setting the 
         * autoConnect parameter to false.
         */
        if (android.os.Build.FINGERPRINT.contains(" Meizu/meizu_m2cnote")){
        	//魅蓝note2
        	mBtGatt = device.connectGatt(mContext, true, mGattCallback);
        }else if(android.os.Build.FINGERPRINT.contains(" Xiaomi/cancro")){
        	//MI 3C
        	mBtGatt = device.connectGatt(mContext, true, mGattCallback);
        }else{
        	
        	mBtGatt = device.connectGatt(mContext, ConstantValue.connectGattFlag, mGattCallback);
        }
        LogUtil.i(TAG, "Trying to create a new connection.");
        //mBtDevAddr = address;
        return true;
    }

    public void disconnect()
    {
        if (mBtAdapter == null || mBtGatt == null)
        {
            LogUtil.e(TAG, "disconnect fail");
            return;
        }
        LogUtil.i(1, "AtoB002");
        LogUtil.w(TAG, "mBluetoothGatt.disconnect");
        mBtGatt.disconnect();
    }
    
    public void discoverServices()
    {
        if (mBtAdapter == null || mBtGatt == null)
        {
            LogUtil.e(TAG, "discoverServices fail");
            return;
        }
        
        // Attempts to discover services after successful connection.
        boolean rslt = mBtGatt.discoverServices();
        LogUtil.i(TAG, "Attempting to start service discovery:" + rslt);
    }

    public void close()
    {
        if (mBtGatt == null)
        {
            //mBtDevAddr = "";
            return;
        }
        LogUtil.w(TAG, "mBluetoothGatt.close");
        mBtGatt.close();
        mBtGatt = null;
        //mBtDevAddr = "";
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        if (mBtAdapter == null || mBtGatt == null)
        {
            LogUtil.e(TAG, "readCharacteristic fail");
            return;
        }
        mBtGatt.readCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled)
    {
        if (mBtAdapter == null || mBtGatt == null)
        {
            LogUtil.e(TAG, "setCharacteristicNotification fail");
            return;
        }
        mBtGatt.setCharacteristicNotification(characteristic, enabled);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic,
            byte[] msg)
    {
        if (characteristic == null)
        {
            LogUtil.e(TAG, "characteristic was released");
            return;
        }
        
        // 设置数据内容
        characteristic.setValue(msg);
        boolean rslt = mBtGatt.writeCharacteristic(characteristic);
        if (rslt)
        {
            LogUtil.w(TAG, "BT write succ:" + ByteUtil.toHexString(msg));
        }
        else
        {
            LogUtil.e(TAG, "BT write fail:" + ByteUtil.toHexString(msg));
            mOnDataSendListener.onWriteFail(msg);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices()
    {
        if (mBtGatt == null)
        {
            return null;
        }

        return mBtGatt.getServices();
    }
    
    public BluetoothGattService getService(String uuid)
    {
        if (mBtGatt == null)
        {
            return null;
        }

        return mBtGatt.getService(UUID.fromString(uuid));
    }
    
    public BluetoothDevice getDevice()
    {
        if (mBtGatt == null)
        {
            return null;
        }
        
        return mBtGatt.getDevice();
    }

    // 判断某个MAC地址的蓝牙设备是否连接
    public boolean isConnected(String mac)
    {
        if (TextUtils.isEmpty(mac))
        {
            return false;
        }
        
        if (mBtGatt == null)
        {
            return false;
        }
        
        if (mac.equals(mBtGatt.getDevice().getAddress())
                && isConnected)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public String getDevAddr()
    {
        if (mBtGatt == null)
        {
            return "";
        }
        return mBtGatt.getDevice().getAddress();
    }
}
