package com.travelrely.core.nrs.ble;

import java.util.List;

import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.IntentMsg;
import com.travelrely.core.nrs.ble.BleManager.OnBtDataRecvListener;
import com.travelrely.core.nrs.ble.BleManager.OnBtDataSendListener;
import com.travelrely.core.nrs.ble.BleManager.OnConnectListener;
import com.travelrely.core.nrs.ble.BleManager.OnServiceDiscoverListener;
import com.travelrely.core.nrs.nr.BoxManager;
import com.travelrely.core.nrs.nr.BoxManager.BoxCallback;
import com.travelrely.core.nrs.nr.msg.MsgId;
import com.travelrely.core.nrs.nr.mt100.Common;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.nrs.nr.util.TextUtil;
import com.travelrely.core.util.LogUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.v2.AesLib;

public class BleService extends Service
{
	private final static String TAG = BleService.class.getSimpleName();
	
    public int iBtMainState = BtState.MAIN_NULL;
    public int iBtSubState = BtState.SUB_NULL;

	private final static String UUID_SERVICE = "0000fee9-0000-1000-8000-00805f9b34fb";
    private final static String UUID_TX = "d44bc439-abfd-45a2-b575-925416129600";
    private final static String UUID_RX = "d44bc439-abfd-45a2-b575-925416129601";

    private BleManager mBleM;
    //private BluetoothGattCharacteristic rxGattChar = null;
    private BluetoothGattCharacteristic txGattChar = null;
    private BleDataPool pool = new BleDataPool();
    private boolean isWriteEnable = true;
    
    public static byte[] gAesKey=new byte[16];
    public static String gStrAesKeyString="";
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle bundle = msg.getData();
            if (bundle == null)
            {
                return;
            }

            int iMsgId = bundle.getInt("BLE_MSG_ID");
            byte[] msgContent = bundle.getByteArray("BLE_MSG");
            AppBleMsgEntry(iMsgId, msgContent);
        }
    };
    
    public synchronized void procMsg(int iMsgId, byte[] msgContent)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("BLE_MSG_ID", iMsgId);
        
        if (msgContent == null)
        {
            bundle.putByteArray("BLE_MSG", null);
        }
        else
        {
            bundle.putByteArray("BLE_MSG",
                    ByteUtil.subArray(msgContent, 0, msgContent.length));
        }
        

        Message msg = mHandler.obtainMessage();
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {        
        if (mBleM == null)
        {
            setupBleManager();
        }
        
        BoxManager.getDefault().setCallback(mBoxCallback);
        
        LogUtil.i(TAG, "BleService onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        if (intent == null)
        {
            LogUtil.e(TAG, "intent == null");
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null)
        {
            LogUtil.e(TAG, "bundle == null");
            return;
        }
        
        int iMsgId = bundle.getInt("BLE_MSG_ID");
        byte[] msg = bundle.getByteArray("BLE_MSG");
        procMsg(iMsgId, msg);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mHandler.removeMessages(BleMsgId.BLE_BLE_STATE_EXPIRE);
        BoxManager.getDefault().close();
        
        mBleM.close();
        //mBleM = null;
        
        pool.clear();
        pool = null;
        
        LogUtil.e(TAG, "BleService onDestroy");
    }
    
    private void setupBleManager()
    {
        // 创建并初始化BLE管理器
        mBleM = new BleManager(this);
        if (!mBleM.initialize())
        {
            LogUtil.e(TAG, "Unable to initialize Bluetooth");
            stopSelf();
            return;
        }

        // BLE终端连接或断开时回调
        mBleM.setOnConnectListener(mOnConnect);

        // 发现BLE终端的Service时回调
        mBleM.setOnServiceDiscoverListener(mOnServiceDiscover);

        // 收到BLE终端数据交互的事件
        mBleM.setOnDataAvailableListener(mOnDataRecv);
        
        // 向BLE终端发送数据结束事件
        mBleM.setOnDataSendListener(mOnDataSend);
    }
    
    /**
     * BLE连接状态变化
     */
    private OnConnectListener mOnConnect = new OnConnectListener()
    {
        @Override
        public void onConnect(BluetoothGatt gatt)
        {
            LogUtil.i(1, "BtoA001,0");
            procMsg(BleMsgId.BLE_BLE_CONNECT_SUCC, null);
        }
        
        @Override
        public void onConnectFail(BluetoothGatt gatt)
        {
            LogUtil.e(1, "BtoA001,1");
            mBleM.disconnect();

            procMsg(BleMsgId.BLE_BLE_CONNECT_FAIL, null);
        }

        @Override
        public void onDisconnect(BluetoothGatt gatt)
        {
            Engine.getInstance().broadcast(IAction.BLE_DISCONNECT);
            txGattChar = null;
            isWriteEnable = true;

            LogUtil.i(1, "BtoA002,0");
            mBleM.close();
            setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
        }
    };

    /**
     * 搜索到BLE终端服务的事件
     */
    private OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener()
    {
        @Override
        public void onServiceDiscover(BluetoothGatt gatt)
        {
            BluetoothGattService gattService = mBleM.getService(UUID_SERVICE);
            if (gattService == null)
            {
                LogUtil.e(TAG, "没有发现对应的服务");
                mBleM.disconnect();

                procMsg(BleMsgId.BLE_BLE_SERVICE_FAIL, null);
                
                return;
            }
            
            displayGattService(gattService);

            procMsg(BleMsgId.BLE_BLE_SERVICE_SUCC, null);
        }
    };

    /**
     * 收到BLE终端数据交互的事件
     */
    private OnBtDataRecvListener mOnDataRecv = new OnBtDataRecvListener()
    {
        /**
         * BLE终端数据被读的事件
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                LogUtil.e(TAG, "onCharRead "
                        + gatt.getDevice().getName()
                        + " read "
                        + characteristic.getUuid().toString()
                        + " -> "
                        + ByteUtil.toHexString(characteristic.getValue()));
            }
        }

        /**
         * 收到BLE终端写入数据回调
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic)
        {
            /*
            AppLog.e(TAG, "onCharWrite "
                    + gatt.getDevice().getName()
                    + " write "
                    + characteristic.getUuid().toString()
                    + " -> "
                    + ByteUtil.toHexString(characteristic.getValue()));
            */
            BoxManager.getDefault().msgEntry(iBtMainState, iBtSubState,
                    characteristic.getValue());
            Log.e("bleservice 收到蓝牙配件发过来的数据---", characteristic.getValue() + "");
        }
    };
    
    private OnBtDataSendListener mOnDataSend = new OnBtDataSendListener()
    {
        @Override
        public void onWriteSucc()
        {

        }

        @Override
        public void onWriteFail(byte[] msg)
        {
            //sendBtDateFrame(msg);
        }

        @Override
        public void onSendSucc()
        {
            isWriteEnable = true;
            if (pool.hasData())
            {
                sendBtDateFrame(pool.getData());
            }
        }

        @Override
        public void onSendFail(byte[] msg)
        {
            isWriteEnable = true;
            sendBtDateFrame(msg);
        }
    };
    
    @SuppressWarnings("unused")
    private void displayGattServices(List<BluetoothGattService> gattServices)
    {
        if (gattServices == null)
        {
            return;
        }

        for (BluetoothGattService gattService : gattServices)
        {
            displayGattService(gattService);
        }
    }
    
    private void displayGattService(BluetoothGattService gattService)
    {
        if (gattService == null)
        {
            return;
        }
        
        // -----Service的字段信息-----//
        /*
        int type = gattService.getType();
        AppLog.e(TAG, "-->service type:" + Utils.getServiceType(type));
        AppLog.e(TAG, "-->includedServices size:"
                + gattService.getIncludedServices().size());
        AppLog.e(TAG, "-->service uuid:" + gattService.getUuid());*/

        // -----Characteristics的字段信息-----//
        List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                .getCharacteristics();
        for (final BluetoothGattCharacteristic gattChar : gattCharacteristics)
        {
            /*
            AppLog.e(TAG, "---->char uuid:" + gattChar.getUuid());

            int permission = gattChar.getPermissions();
            AppLog.e(TAG, "---->char permission:"
                            + Utils.getCharPermission(permission));

            int property = gattChar.getProperties();
            AppLog.e(TAG, "---->char property:"
                            + Utils.getCharPropertie(property));

            byte[] data = gattChar.getValue();
            if (data != null && data.length > 0)
            {
                AppLog.e(TAG, "---->char value:" + new String(data));
            }*/

            // UUID_RECV是接收蓝牙盒子数据的Characteristic
            if (gattChar.getUuid().toString().equals(UUID_RX))
            {
                /**
                 * 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发
                 * mOnDataAvailable.onCharacteristicWrite()
                 */
                mBleM.setCharacteristicNotification(gattChar, true);
            }

            if (gattChar.getUuid().toString().equals(UUID_TX))
            {
                mBleM.setCharacteristicNotification(gattChar, true);
                txGattChar = gattChar;
            }

            // -----Descriptors的字段信息-----//
            /*
            List<BluetoothGattDescriptor> gattDescriptors = gattChar
                    .getDescriptors();
            for (BluetoothGattDescriptor gattDescriptor : gattDescriptors)
            {
                AppLog.e(TAG, "-------->desc uuid:" + gattDescriptor.getUuid());
                int descPermission = gattDescriptor.getPermissions();
                AppLog.e(TAG, "-------->desc permission:"
                                + Utils.getDescPermission(descPermission));

                byte[] desData = gattDescriptor.getValue();
                if (desData != null && desData.length > 0)
                {
                    AppLog.e(TAG, "-------->desc value:" + new String(desData));
                }
            }*/
        }
    }

   
    /**
     * @param msg
     */
    private void sendBtDateFrame(final byte[] msg)
    { // 供MT100通信模块回调
        if (txGattChar == null)
        {
            LogUtil.e(TAG, "txGattChar == null");
            pool.moveToPreData();
            return;
        }
        
        // 往蓝牙模块写入数据
        mBleM.writeCharacteristic(txGattChar, msg);
        isWriteEnable = false;
    }
    
    private BoxCallback mBoxCallback = new BoxCallback()
    {
        @Override
        public void onBtMsgReady(byte[] msg)
        {
            if (msg == null)
            {
                return;
            }

            pool.setDataPool(msg);
            
            if (!isWriteEnable)
            {
                LogUtil.e(TAG, "还没有等到蓝牙发送数据回调");
                return;
            }
            
            if (pool.hasData())
            {
                sendBtDateFrame(pool.getData());
            }
        }
        
        @Override
        public void onBoxMsgArrived(int iMsgId, byte[] msg)
        {
            procMsg(iMsgId, msg);//收到盒子过来的消息
        }
    };
    
    private void AppBleMsgEntry(int iMsgId, byte[] msg)
    {
        switch (iMsgId)
        {
            case BleMsgId.BLE_BLE_STATE_EXPIRE:
                StateExpireMsgEntry(msg, msg.length);
                return;

            default:
                break;
        }
        
        switch (iBtMainState)
        {
            case BtState.MAIN_NULL:
                MsgEntryOnMainNull(iMsgId, msg);
                break;
                
            case BtState.MAIN_FIND_BOX:
                MsgEntryOnFinding(iMsgId, msg);
                break;
                
            case BtState.MAIN_MATCH_BOX:
                MsgEntryOnMatching(iMsgId, msg);
                break;
                
            case BtState.MAIN_READ_SIM:
                MsgEntryOnReading(iMsgId, msg);
                break;
                
            case BtState.MAIN_AUTH_SIM:
                MsgEntryOnAuth(iMsgId, msg);
                break;
                
            case BtState.MAIN_CHK_BOX:
                MsgEntryOnChkBox(iMsgId, msg);
                break;
                
            case BtState.MAIN_WRITE_SIM:
                MsgEntryOnWriteSim(iMsgId, msg);
                break;
                
            case BtState.MAIN_CANCEL_VSIM:
                MsgEntryOnCancelVsim(iMsgId, msg);
                break;

            default:
                break;
        }
    }
    
    private void MsgEntryOnMainNull(int iMsgId, byte[] msg)
    {
        String addr = null;
        
        switch (iMsgId)
        {
            case BleMsgId.NR_BLE_FIND_BOX:
                addr = new String(msg);
                if (TextUtils.isEmpty(addr))
                {
                    LogUtil.e(TAG, "蓝牙地址为空(寻找蓝牙盒子)");
                    return;
                }
                mBleM.connect(addr);
                setState(BtState.MAIN_FIND_BOX, BtState.SUB_W_CONNECT_RSP, 10);
                break;
                
            case BleMsgId.NR_BLE_MATCH_BOX:
                addr = new String(msg);
                if (TextUtils.isEmpty(addr))
                {
                    LogUtil.e(TAG, "蓝牙地址为空(配对蓝牙盒子)");
                    return;
                }
                mBleM.connect(addr);
                setState(BtState.MAIN_MATCH_BOX, BtState.SUB_W_CONNECT_RSP, 10);
                break;
                
            case BleMsgId.NR_BLE_READ_SIM_IND:
                if (TextUtils.isEmpty(SpUtil.getBtAddr()))
                {
                    LogUtil.e(TAG, "蓝牙地址为空(读基本信息)");
                    return;
                }
                if (mBleM.isConnected(SpUtil.getBtAddr()))
                {
                    LogUtil.w(TAG, "蓝牙已经连接(读基本信息)");
                    
                    if (!BoxManager.getDefault().isMt100Ready)
                    {
                        BoxManager.getDefault().initMt100();
                        setState(BtState.MAIN_READ_SIM, BtState.SUB_W_MT100_READY, 5);
                    }
                    else
                    {
                        BoxManager.getDefault().readEssenInfo();
                        setState(BtState.MAIN_READ_SIM, BtState.SUB_W_SIM_INFO, 10);
                    }
                    
                    return;
                }
                mBleM.connect(SpUtil.getBtAddr());
                setState(BtState.MAIN_READ_SIM, BtState.SUB_W_CONNECT_RSP, 10);
                
                Engine.getInstance().broadcast(IAction.NR_START_REG);
                break;
                
            case BleMsgId.NR_BLE_AUTH_SIM_IND:
                if (TextUtils.isEmpty(SpUtil.getBtAddr()))
                {
                    LogUtil.e(TAG, "蓝牙地址为空(鉴权)");
                    return;
                }
                if (mBleM.isConnected(SpUtil.getBtAddr()))
                {
                    LogUtil.w(TAG, "蓝牙已经连接(鉴权)");

                    BoxManager.getDefault().readSimAuthData();
                    setState(BtState.MAIN_AUTH_SIM, BtState.SUB_W_SIM_AUTH_RSLT, 10);
                
                    return;
                }
                mBleM.connect(SpUtil.getBtAddr());
                setState(BtState.MAIN_AUTH_SIM, BtState.SUB_W_CONNECT_RSP, 5);
                break;

            case BleMsgId.NR_BLE_SCAN_BOX:
                //scanLeDevice(true);
                if (TextUtils.isEmpty(SpUtil.getBtAddr()))
                {
                    LogUtil.e(TAG, "蓝牙地址为空(扫盒子)");
                    return;
                }
                if (mBleM.isConnected(SpUtil.getBtAddr()))
                {
                    LogUtil.w(TAG, "蓝牙已经连接(扫盒子)");
                    return;
                }
                mBleM.connect(SpUtil.getBtAddr());
                setState(BtState.MAIN_CHK_BOX, BtState.SUB_W_CONNECT_RSP, 10);
                break;
                
            case BleMsgId.UI_BLE_READ_UL01:
                if (TextUtils.isEmpty(SpUtil.getBtAddr()))
                {
                    LogUtil.e(TAG, "蓝牙地址为空(写号)");
                    return;
                }
                if (mBleM.isConnected(SpUtil.getBtAddr()))
                {
                    LogUtil.w(TAG, "蓝牙已经连接(写号)");
                    return;
                }
                mBleM.connect(SpUtil.getBtAddr());
                setState(BtState.MAIN_WRITE_SIM, BtState.SUB_W_CONNECT_RSP, 10);
                break;
                
            case BleMsgId.UI_BLE_CANCLE_VSIM:
                if (TextUtils.isEmpty(SpUtil.getBtAddr()))
                {
                    LogUtil.e(TAG, "蓝牙地址为空(注销)");
                    return;
                }
                if (mBleM.isConnected(SpUtil.getBtAddr()))
                {
                    LogUtil.w(TAG, "蓝牙已经连接(注销)");
                    return;
                }
                mBleM.connect(SpUtil.getBtAddr());
                setState(BtState.MAIN_CANCEL_VSIM, BtState.SUB_W_CONNECT_RSP, 10);
                break;
                
            case BleMsgId.NR_BLE_DISCONNECT_IND:
                mBleM.disconnect();
                break;

            default:
                break;
        }
    }
    
    private void MsgEntryOnFinding(int iMsgId, byte[] msg)
    {
        switch (iBtSubState)
        {
            case BtState.SUB_W_CONNECT_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_SUCC)
                {
                    mBleM.discoverServices();
                    setState(BtState.MAIN_FIND_BOX, BtState.SUB_W_SERVICE_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.BOX_NOT_FOUND);
                }
                break;
                
            case BtState.SUB_W_SERVICE_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_SUCC)
                {
                    BoxManager.getDefault().sendKeyStateReq();
                    setState(BtState.MAIN_FIND_BOX, BtState.SUB_W_BOX_KEY_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.BOX_NOT_FOUND);
                }
                break;
                
            case BtState.SUB_W_BOX_KEY_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_FIND_BOX_SUCC)
                {
                    String s = mBleM.getDevAddr();
                    LogUtil.i(TAG, s);
                    if (s.equals(SpUtil.getBtAddr()))
                    {
                        SpUtil.setBtAddr("");
                        SpUtil.setfirstConnect(0);
                        SpUtil.setBtName("");
                        SpUtil.setBtKey("");
                        
                        Engine.getInstance().isNRRegisted = false;
//                        Engine.getInstance().broadcast(IAction.BOX_MISS_MATCH);
                    }
                    Engine.getInstance().broadcast(IAction.BOX_FOUND,
                            mBleM.getDevice());
                }
                
                if (iMsgId == BleMsgId.BOX_BLE_FIND_BOX_FAIL)
                {
                    Engine.getInstance().broadcast(IAction.BOX_NOT_FOUND);
                }
                mBleM.disconnect();
                break;

            default:
                break;
        }
    }

    private void MsgEntryOnMatching(int iMsgId, byte[] msg)
    {
        switch (iBtSubState)
        {
            case BtState.SUB_W_CONNECT_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_SUCC)
                {
                    mBleM.discoverServices();
                    setState(BtState.MAIN_MATCH_BOX, BtState.SUB_W_SERVICE_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.BOX_MATCH_FAIL);
                }
                break;
                
            case BtState.SUB_W_SERVICE_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_SUCC)
                {
                    BoxManager.getDefault().sendAddrReq();
                    setState(BtState.MAIN_MATCH_BOX, BtState.SUB_W_BOX_MAC_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.BOX_MATCH_FAIL);
                }
                break;
                
            case BtState.SUB_W_BOX_MAC_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_MAC_RECEIVED)
                {
                    LogUtil.i(1, "BtoA004,"+mBleM.getDevAddr());
                    // 生成并保key
                    String key = TextUtil.getRandomString(6);
                    SpUtil.setBtKey(key);
                  
                    BoxManager.getDefault().sendKeySaveReq(key);
                  
                    setState(BtState.MAIN_MATCH_BOX, BtState.SUB_W_BOX_SET_KEY_RSP, 5);
                }
                break;
                
            case BtState.SUB_W_BOX_SET_KEY_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_MATCH_SUCC)
                {
                    LogUtil.i(1, "BtoA005,0");
                    BoxManager.getDefault().sendCosVerReq();
                    setState(BtState.MAIN_MATCH_BOX, BtState.SUB_W_COS_VER_RSP, 5);
                }

                if (iMsgId == BleMsgId.BOX_BLE_MATCH_FAIL)
                {
                    LogUtil.i(1, "BtoA005,1");
                    SpUtil.setBtKey("");
                    
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.BOX_MATCH_FAIL);
                }
                break;
                
            case BtState.SUB_W_COS_VER_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_COS_VER_RECEIVED)
                {
                    LogUtil.i(2, "MtoA002,%s", Common.bytesToHexString(msg));
                    SpUtil.setCosVer(ByteUtil.getIntByBigEnd(msg));

                    BoxManager.getDefault().sendBoxSnReq();
                    setState(BtState.MAIN_MATCH_BOX, BtState.SUB_W_BOX_SN_RSP, 5);
                }
                break;
                
            case BtState.SUB_W_BOX_SN_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_BOX_SN_RSP)
                {
                    LogUtil.i(2, "MtoA001,%s", Common.bytesToHexString(msg));
                    // 保存蓝牙盒子SN
                    SpUtil.setBoxSn(Common.bytesToHexString(msg));

                    // 保存配对信息
                    SpUtil.setBtAddr(mBleM.getDevice().getAddress());
                    SpUtil.setBtName(mBleM.getDevice().getName());
                    //Engine.getInstance().broadcast(Actions.BOX_MATCH_SUCC);

                    // 查询电量
                    BoxManager.getDefault().queryBattery();
                    setState(BtState.MAIN_MATCH_BOX, BtState.SUB_W_BATTERY, 5);
                }
                break;

            case BtState.SUB_W_BATTERY:
                if (iMsgId != BleMsgId.BOX_BLE_QUERY_BATTERY_RSP)
                {
                    return;
                }
                
                Engine.getInstance().broadcast(IAction.BOX_MATCH_SUCC);
                
                // 打开NR开关并发起读卡注册
                Engine.getInstance().isNRSwitchOn = true;
                BoxManager.getDefault().sendKeyChkReq(SpUtil.getBtKey());
                setState(BtState.MAIN_READ_SIM, BtState.SUB_W_BOX_CHK_KEY_RSP, 5);
                SpUtil.setfirstConnect(0);
                Engine.getInstance().broadcast(IAction.NR_START_REG);
                break;

            default:
                break;
        }
    }
    
    private void MsgEntryOnReading(int iMsgId, byte[] msg)
    {
        switch (iBtSubState)
        {
            case BtState.SUB_W_CONNECT_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_SUCC)
                {
                    mBleM.discoverServices();
                    setState(BtState.MAIN_READ_SIM, BtState.SUB_W_SERVICE_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.NR_REG_FAIL);
                }
                break;
                
            case BtState.SUB_W_SERVICE_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_SUCC)
                {
                    BoxManager.getDefault().sendKeyChkReq(SpUtil.getBtKey());
                    setState(BtState.MAIN_READ_SIM, BtState.SUB_W_BOX_CHK_KEY_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.NR_REG_FAIL);
                }
                break;
                
            case BtState.SUB_W_BOX_CHK_KEY_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_KEY_CHK_SUCC)
                {                    
                    Engine.getInstance().startNRService(BleService.this,
                            MsgId.APP_AGENT_HW_INFO_REQ);
                    setState(BtState.MAIN_READ_SIM, BtState.SUB_W_NR_RAND_IND, 5);
                }

                if (iMsgId == BleMsgId.BOX_BLE_KEY_CHK_FAIL)
                {
                    mBleM.disconnect();
                    
                    SpUtil.setBtAddr("");
                    SpUtil.setfirstConnect(0);
                  
                    Engine.getInstance().isNRRegisted = false;
//                    Engine.getInstance().broadcast(IAction.BOX_MISS_MATCH);
                }
                break;

            case BtState.SUB_W_NR_RAND_IND:
                if (iMsgId == BleMsgId.NR_BLE_RAND_IND)
                {
                    // 发送rand到盒子换取aes加密key
                    BoxManager.getDefault().sendAesKeyReq(msg);
                    setState(BtState.MAIN_READ_SIM, BtState.SUB_W_AES_KEY_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.NR_BLE_NO_RAND_IND)
                {
                    gStrAesKeyString=null;
                    SpUtil.setBoxAesKey("");
                    if (!BoxManager.getDefault().isMt100Ready)
                    {
                        BoxManager.getDefault().initMt100();
                        setState(BtState.MAIN_READ_SIM, BtState.SUB_W_MT100_READY, 5);
                    }
                    else
                    {
                        BoxManager.getDefault().readEssenInfo();
                        setState(BtState.MAIN_READ_SIM, BtState.SUB_W_SIM_INFO, 10);
                    }
                }
                break;

            case BtState.SUB_W_AES_KEY_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_AES_KEY_RSP)
                {
                    BoxBleRsp keyRsp = new BoxBleRsp(msg);
                    if (keyRsp.getRslt() > 0)
                    {
                        mBleM.disconnect();
                        setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                        Engine.getInstance().broadcast(IAction.BOX_SIM_INIT_FAIL);
                        return;
                    }
                    
                    byte[] key = ByteUtil.subArray(keyRsp.getContent(), 4, 16);
                    
                    // 设置加解密库的key值并读取卡信息发起注册流程
                    Log.e("bleservice", "set aes key");
                    AesLib.set_key(key);
                    SpUtil.setBoxAesKey(Common.bytesToHexString(key));
                    System.arraycopy(key, 0, gAesKey, 0, 16);
                    
                    for (int i = 0; i < gAesKey.length; i++) {
						Log.e("bleservice","gAesKey["+i+"] is "+String.format("%02x", gAesKey[i])+", key["+i+"] is "+String.format("%02x", key[i]));
						//Log.e("bleservice","key["+i+"] is "+String.format("%02x", key[i]));
					}
                    
                    gStrAesKeyString="";
            		for (int i = 0; i < gAesKey.length; i++) {
            			gStrAesKeyString+=String.format("%02x", gAesKey[i]);
            		}
            		
                    if (!BoxManager.getDefault().isMt100Ready)
                    {
                        BoxManager.getDefault().initMt100();
                        setState(BtState.MAIN_READ_SIM, BtState.SUB_W_MT100_READY, 5);
                    }
                    else
                    {
                        BoxManager.getDefault().readEssenInfo();
                        setState(BtState.MAIN_READ_SIM, BtState.SUB_W_SIM_INFO, 10);
                    }
                }
                break;
                
            case BtState.SUB_W_MT100_READY:
                if (iMsgId != BleMsgId.BOX_BLE_INIT_MT100_RSP)
                {
                    return;
                }
                
                BoxBleRsp mt100InitRsp = new BoxBleRsp(msg);
                if (mt100InitRsp.getRslt() > 0)
                {
                    LogUtil.i(2, "MtoA003,1,%s", Common.bytesToHexString(msg));
                    mBleM.disconnect();
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.BOX_SIM_INIT_FAIL);
                    return;
                }
                
                LogUtil.i(2, "MtoA003,0");
                BoxManager.getDefault().isMt100Ready = true;
                BoxManager.getDefault().readEssenInfo();
                setState(BtState.MAIN_READ_SIM, BtState.SUB_W_SIM_INFO, 10);
                break;
                
            case BtState.SUB_W_SIM_INFO:
                if (iMsgId != BleMsgId.BOX_BLE_READ_SIM_INFO_RSP)
                {
                    return;
                }

                print(LogUtil.INF, iMsgId);
                LogUtil.i(3, "StoA001,%s,%s,%s", BoxManager.getDefault().getImsiStr(),
                        BoxManager.getDefault().getMsisdn(),
                        BoxManager.getDefault().getSmsp());
                
                //保存imsi
                if (TextUtils.isEmpty(BoxManager.getDefault().getImsiStr())){
                	
                }else{
                	
                	SpUtil.setImsiInBox(BoxManager.getDefault().getImsiStr());
                }
                Engine.getInstance().startNRService(BleService.this,
                        MsgId.BLE_NR_REG_IND);
                setState(BtState.MAIN_READ_SIM, BtState.SUB_W_NR_AUTH_IND, 10);
                break;
                
            case BtState.SUB_W_NR_AUTH_IND:
                if (iMsgId == BleMsgId.NR_BLE_AUTH_SIM_IND)
                {
                    BoxManager.getDefault().readSimAuthData();
                    setState(BtState.MAIN_READ_SIM, BtState.SUB_W_SIM_AUTH_RSLT, 10);
                }
                break;

            case BtState.SUB_W_SIM_AUTH_RSLT:
                if (iMsgId != BleMsgId.BOX_BLE_AUTH_SIM_RSP)
                {
                    return;
                }
                
                BoxBleRsp authRsp = new BoxBleRsp(msg);
                if (authRsp.getRslt() > 0)
                {
                    LogUtil.i(3, "StoA002,1,%d", BoxManager.getDefault().getAuthErr());
                    mBleM.disconnect();
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.BOX_SIM_INIT_FAIL);
                    return;
                }

                LogUtil.i(3, "StoA002,0,%s", Common.bytesToHexString(
                        BoxManager.getDefault().getAuthRslt()));
                Engine.getInstance().startNRService(BleService.this,
                        MsgId.APP_AGENT_AUTH_RSP);
                setState(BtState.MAIN_READ_SIM, BtState.SUB_W_NR_CIPHER_IND, 0);
                break;
                
            case BtState.SUB_W_NR_CIPHER_IND:
                if (iMsgId == BleMsgId.NR_BLE_CIPHER_ON_IND)
                {
                    BoxManager.getDefault().sendAuthCipherKey(true);
                    setState(BtState.MAIN_READ_SIM, BtState.SUB_W_BOX_CIPHER_RSP, 10);
                }
                
                if (iMsgId == BleMsgId.NR_BLE_CIPHER_OFF_IND)
                {
                    mBleM.disconnect();
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.BOX_CIPHER_KEY_SAVED);
                }
                break;
                
            case BtState.SUB_W_BOX_CIPHER_RSP:
                if (iMsgId != BleMsgId.BOX_BLE_SAVE_CIPHER_KEY_RSP)
                {
                    return;
                }
                BoxBleRsp cipherKeySaveRsp = new BoxBleRsp(msg);
                if (cipherKeySaveRsp.getRslt() > 0)
                {
                    Engine.getInstance().broadcast(IAction.BOX_CIPHER_KEY_SAVED);
                }
                else
                {
                    Engine.getInstance().broadcast(IAction.BOX_CIPHER_KEY_SAVED);
                }

                mBleM.disconnect();
                setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                break;

            default:
                break;
        }
    }
    
    /**鉴权
     * @param iMsgId
     * @param msg
     */
    private void MsgEntryOnAuth(int iMsgId, byte[] msg)
    {
        switch (iBtSubState)
        {
            case BtState.SUB_W_CONNECT_RSP://开始连接蓝牙盒子
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_SUCC)
                {
                    mBleM.discoverServices();
                    setState(BtState.MAIN_AUTH_SIM, BtState.SUB_W_SERVICE_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                }
                break;
                
            case BtState.SUB_W_SERVICE_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_SUCC)//发现服务（设置收发通道为enable） 连接成功
                {
                    BoxManager.getDefault().sendKeyChkReq(SpUtil.getBtKey());//发送key校验命令
                    setState(BtState.MAIN_AUTH_SIM, BtState.SUB_W_BOX_CHK_KEY_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                }
                break;
                
            case BtState.SUB_W_BOX_CHK_KEY_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_KEY_CHK_SUCC)//key校验成功
                {
                    BoxManager.getDefault().readSimAuthData();//读取鉴权数据
                    setState(BtState.MAIN_AUTH_SIM, BtState.SUB_W_SIM_AUTH_RSLT, 10);
                }
                
                if (iMsgId == BleMsgId.BOX_BLE_KEY_CHK_FAIL)
                {
                    mBleM.disconnect();
                    
                    SpUtil.setBtAddr("");
                    SpUtil.setfirstConnect(0);
                  
                    Engine.getInstance().isNRRegisted = false;
//                    Engine.getInstance().broadcast(IAction.BOX_MISS_MATCH);
                }
                break;

            case BtState.SUB_W_SIM_AUTH_RSLT:
                if (iMsgId != BleMsgId.BOX_BLE_AUTH_SIM_RSP)
                {
                    return;
                }
                
                BoxBleRsp authRsp = new BoxBleRsp(msg);
                if (authRsp.getRslt() > 0)
                {
                    mBleM.disconnect();
                    return;
                }
                Engine.getInstance().startNRService(BleService.this,
                        MsgId.APP_AGENT_AUTH_RSP);
                
                BoxManager.getDefault().queryBattery();//查询电量
                setState(BtState.MAIN_AUTH_SIM, BtState.SUB_W_BATTERY, 5);
                break;
                
            case BtState.SUB_W_BATTERY:
                if (iMsgId != BleMsgId.BOX_BLE_QUERY_BATTERY_RSP)
                {
                    return;
                }
                
                BoxBleRsp qRsp = new BoxBleRsp(msg);
                if (qRsp.getRslt() > 0)
                {
                    mBleM.disconnect();
                    return;
                }
                mBleM.disconnect();//断开蓝牙
                setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                break;

            default:
                break;
        }
    }
    
    private void MsgEntryOnChkBox(int iMsgId, byte[] msg)
    {
        switch (iBtSubState)
        {                
            case BtState.SUB_W_CONNECT_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_SUCC)
                {
                    BoxManager.getDefault().isConnected = true;
                    Engine.getInstance().startNRService(BleService.this,
                            MsgId.BLE_NR_KEEPALIVE_IND);
                    mBleM.disconnect();
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    BoxManager.getDefault().isConnected = false;
                    Engine.getInstance().startNRService(BleService.this,
                            MsgId.BLE_NR_KEEPALIVE_IND);
                }
                break;

            default:
                break;
        }
    }
    
    private void MsgEntryOnWriteSim(int iMsgId, byte[] msg)
    {
        switch (iBtSubState)
        {
            case BtState.SUB_W_CONNECT_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_SUCC)
                {
                    mBleM.discoverServices();
                    setState(BtState.MAIN_WRITE_SIM, BtState.SUB_W_SERVICE_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                }
                break;
                
            case BtState.SUB_W_SERVICE_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_SUCC)
                {
                    BoxManager.getDefault().sendKeyChkReq(SpUtil.getBtKey());
                    setState(BtState.MAIN_WRITE_SIM, BtState.SUB_W_BOX_CHK_KEY_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                }
                break;
                
            case BtState.SUB_W_BOX_CHK_KEY_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_KEY_CHK_SUCC)
                {
                    BoxManager.getDefault().sendUl01Req();
                    setState(BtState.MAIN_WRITE_SIM, BtState.SUB_W_SIM_UL01OTA, 10);
                }
                
                if (iMsgId == BleMsgId.BOX_BLE_KEY_CHK_FAIL)
                {
                    mBleM.disconnect();
                    
                    SpUtil.setBtAddr("");
                    SpUtil.setfirstConnect(0);
                  
                    Engine.getInstance().isNRRegisted = false;
//                    Engine.getInstance().broadcast(IAction.BOX_MISS_MATCH);
                }
                break;
                
            case BtState.SUB_W_SIM_UL01OTA:
                if (iMsgId != BleMsgId.BOX_BLE_READ_UL01_RSP)
                {
                    return;
                }
                
                BoxBleRsp readUl01Rsp = new BoxBleRsp(msg);
                if (readUl01Rsp.getRslt() > 0)
                {
                    Engine.getInstance().broadcast(IAction.OP_VSIM,
                            IntentMsg.WR_SIM_READ_UL01_FAIL, 
                            readUl01Rsp.getErrCode());
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    mBleM.disconnect();
                    return;
                }
                BoxManager.getDefault().setUl01Ota(readUl01Rsp.getContent());
                Engine.getInstance().broadcast(IAction.OP_VSIM,
                        IntentMsg.WR_SIM_READ_UL01_SUCC);
                setState(BtState.MAIN_WRITE_SIM, BtState.SUB_W_SVR_DL01OTA, 30);
                break;
                
            case BtState.SUB_W_SVR_DL01OTA:
                if (iMsgId != BleMsgId.UI_BLE_SAVE_DL01)
                {
                    return;
                }

                BoxManager.getDefault().sendDl01Req();
                setState(BtState.MAIN_WRITE_SIM, BtState.SUB_W_SIM_DL01OTA_SAVE_RSP, 10);
                break;
                
            case BtState.SUB_W_SIM_DL01OTA_SAVE_RSP:
                if (iMsgId != BleMsgId.BOX_BLE_SAVE_DL01_RSP)
                {
                    return;
                }
                
                BoxBleRsp saveDl01Rsp = new BoxBleRsp(msg);
                if (saveDl01Rsp.getRslt() > 0)
                {
                    Engine.getInstance().broadcast(IAction.OP_VSIM,
                            IntentMsg.WR_SIM_SAVE_DL01_FAIL,
                            saveDl01Rsp.getErrCode());
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    mBleM.disconnect();
                    return;
                }
                BoxManager.getDefault().setUl02Ota(saveDl01Rsp.getContent());
                Engine.getInstance().broadcast(IAction.OP_VSIM,
                        IntentMsg.WR_SIM_SAVE_DL01_SUCC);
                setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                mBleM.disconnect();
                break;

            default:
                break;
        }
    }
    
    private void MsgEntryOnCancelVsim(int iMsgId, byte[] msg)
    {
        switch (iBtSubState)
        {
            case BtState.SUB_W_CONNECT_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_SUCC)
                {
                    mBleM.discoverServices();
                    setState(BtState.MAIN_CANCEL_VSIM, BtState.SUB_W_SERVICE_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_CONNECT_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                }
                break;
                
            case BtState.SUB_W_SERVICE_RSP:
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_SUCC)
                {
                    BoxManager.getDefault().sendKeyChkReq(SpUtil.getBtKey());
                    setState(BtState.MAIN_CANCEL_VSIM, BtState.SUB_W_BOX_CHK_KEY_RSP, 5);
                }
                
                if (iMsgId == BleMsgId.BLE_BLE_SERVICE_FAIL)
                {
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                }
                break;
                
            case BtState.SUB_W_BOX_CHK_KEY_RSP:
                if (iMsgId == BleMsgId.BOX_BLE_KEY_CHK_SUCC)
                {
                    BoxManager.getDefault().sendCancelReq();
                    setState(BtState.MAIN_CANCEL_VSIM, BtState.SUB_W_VSIM_CANCEL_RSP, 10);
                }
                
                if (iMsgId == BleMsgId.BOX_BLE_KEY_CHK_FAIL)
                {
                    mBleM.disconnect();
                    
                    SpUtil.setBtAddr("");
                    SpUtil.setfirstConnect(0);
                  
                    Engine.getInstance().isNRRegisted = false;
//                    Engine.getInstance().broadcast(IAction.BOX_MISS_MATCH);
                }
                break;
                
            case BtState.SUB_W_VSIM_CANCEL_RSP:
                if (iMsgId != BleMsgId.BOX_BLE_CANCEL_VSIM_RSP)
                {
                    return;
                }
                
                BoxBleRsp cancelRsp = new BoxBleRsp(msg);
                if (cancelRsp.getRslt() > 0)
                {
                    Engine.getInstance().broadcast(IAction.OP_VSIM,
                            IntentMsg.WR_SIM_SAVE_DL04_FAIL,
                            cancelRsp.getErrCode());
                    setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                    mBleM.disconnect();
                    return;
                }
                BoxManager.getDefault().setUl04Ota(cancelRsp.getContent());
                Engine.getInstance().broadcast(IAction.OP_VSIM,
                        IntentMsg.WR_SIM_SAVE_DL04_SUCC);
                setState(BtState.MAIN_NULL, BtState.SUB_NULL, 0);
                mBleM.disconnect();
                break;

            default:
                break;
        }
    }
    
    private void StateExpireMsgEntry(byte[] msg, int iMsgLen)
    {
        if (iMsgLen < 12)
        {
            LogUtil.e(TAG, "StateExpireMsg Error Len[" + iMsgLen + "]");
            return;
        }

        int tempMain = ByteUtil.getInt(ByteUtil.subArray(msg, 4, 4));
        int tempSub = ByteUtil.getInt(ByteUtil.subArray(msg, 8, 4));
        if (tempMain != iBtMainState || tempSub != iBtSubState)
        {
            LogUtil.e(TAG, "过期的状态超时消息[" + BtState.getStateStr(tempMain)
                    + ", " + BtState.getStateStr(tempSub) + "]");
            return;
        }

        LogUtil.e(TAG, "超时状态[" + BtState.getStateStr(tempMain) + ", "
                + BtState.getStateStr(tempSub) + "]");

        switch (iBtMainState)
        {
            case BtState.MAIN_FIND_BOX:
                mBleM.disconnect();
                iBtMainState = BtState.MAIN_NULL;
                iBtSubState = BtState.SUB_NULL;
                Engine.getInstance().broadcast(IAction.BOX_NOT_FOUND);
                break;
                
            case BtState.MAIN_MATCH_BOX:
                switch (iBtSubState)
                {
                    case BtState.SUB_W_BATTERY:
                        Engine.getInstance().broadcast(IAction.BOX_MATCH_SUCC);
                        
                        // 打开NR开关并发起读卡注册
                        Engine.getInstance().isNRSwitchOn = true;
                        BoxManager.getDefault().sendKeyChkReq(SpUtil.getBtKey());
                        setState(BtState.MAIN_READ_SIM, BtState.SUB_W_BOX_CHK_KEY_RSP, 5);
                    
                        Engine.getInstance().broadcast(IAction.NR_START_REG);
                        break;
                        
                    case BtState.SUB_W_BOX_INFO:
                        mBleM.disconnect();
                        iBtMainState = BtState.MAIN_NULL;
                        iBtSubState = BtState.SUB_NULL;
                        break;
                        
                    default:
                        mBleM.disconnect();
                        Engine.getInstance().broadcast(IAction.BOX_MATCH_FAIL);
                        iBtMainState = BtState.MAIN_NULL;
                        iBtSubState = BtState.SUB_NULL;
                        break;
                }                
                break;
                
            case BtState.MAIN_READ_SIM:
                mBleM.disconnect();
                switch (iBtSubState)
                {
                    case BtState.SUB_W_CONNECT_RSP:
                        Engine.getInstance().broadcast(IAction.BLE_CONN_EXPIRE);
                        break;
                        
                    case BtState.SUB_W_MT100_READY:
                        Engine.getInstance().broadcast(IAction.BOX_SIM_INIT_FAIL);
                        break;
                        
                    case BtState.SUB_W_BATTERY:
                        break;

                    default:
                        Engine.getInstance().broadcast(IAction.NR_REG_EXPIRE);
                        break;
                }
                
                iBtMainState = BtState.MAIN_NULL;
                iBtSubState = BtState.SUB_NULL;
                break;
                
            case BtState.MAIN_AUTH_SIM:
                mBleM.disconnect();
                iBtMainState = BtState.MAIN_NULL;
                iBtSubState = BtState.SUB_NULL;
                break;
                
            case BtState.MAIN_CHK_BOX:
                mBleM.disconnect();
                iBtMainState = BtState.MAIN_NULL;
                iBtSubState = BtState.SUB_NULL;
                
                BoxManager.getDefault().isConnected = false;
                Engine.getInstance().startNRService(BleService.this,
                        MsgId.BLE_NR_KEEPALIVE_IND);
                break;
                
            case BtState.MAIN_WRITE_SIM:
            case BtState.MAIN_CANCEL_VSIM:
                mBleM.disconnect();
                iBtMainState = BtState.MAIN_NULL;
                iBtSubState = BtState.SUB_NULL;
                
                Engine.getInstance().broadcast(IAction.OP_VSIM);
                break;
        }

        LogUtil.e(TAG, "To State[" + BtState.getStateStr(iBtMainState) + ", "
                + BtState.getStateStr(iBtSubState) + "]");
    }

    private void setState(final int iMainState, final int iSubState, int delay)
    {
        // 如果上一个状态没有超时,则先关掉定时器
        mHandler.removeMessages(BleMsgId.BLE_BLE_STATE_EXPIRE);

        this.iBtMainState = iMainState;
        this.iBtSubState = iSubState;
        LogUtil.i(TAG, "To State[" + BtState.getStateStr(iBtMainState) + ", "
                + BtState.getStateStr(iBtSubState) + "]");

        if (delay == 0)
        {
            return;
        }

        byte[] msgContent = new byte[12];
        byte[] len = ByteUtil.getBytes(8);
        byte[] main = ByteUtil.getBytes(iMainState);
        byte[] sub = ByteUtil.getBytes(iSubState);
        System.arraycopy(len, 0, msgContent, 0, 4);
        System.arraycopy(main, 0, msgContent, 4, 4);
        System.arraycopy(sub, 0, msgContent, 8, 4);
        
        Bundle bundle = new Bundle();
        bundle.putInt("BLE_MSG_ID", BleMsgId.BLE_BLE_STATE_EXPIRE);
        bundle.putByteArray("BLE_MSG", msgContent);

        Message msg = mHandler.obtainMessage();
        msg.what = BleMsgId.BLE_BLE_STATE_EXPIRE;
        msg.setData(bundle);
        mHandler.sendMessageDelayed(msg, delay * 1000);
    }
    
    public void print(int iLevel, int iMsgId)
    {
        String strMain, strSub, msg;

        strMain = BtState.getStateStr(iBtMainState);
        strSub = BtState.getStateStr(iBtSubState);

        msg = "Recv " + BleMsgId.getMsgStr(iMsgId) + "["
                + String.format("0x%02X", iMsgId)
                + "] in state[" + strMain + ", " + strSub + "]";

        switch (iLevel)
        {
            case LogUtil.DBG:
                LogUtil.d(TAG, msg);
                break;
                
            case LogUtil.INF:
                LogUtil.i(TAG, msg);
                break;
                
            case LogUtil.WAR:
                LogUtil.w(TAG, msg);
                break;
                
            case LogUtil.ERR:
                LogUtil.e(TAG, msg);
                break;
                
            default:
                break;
        }
    }

    public void print(int iLevel, String msg)
    {
        String strMain, strSub;

        strMain = BtState.getStateStr(iBtMainState);
        strSub = BtState.getStateStr(iBtSubState);

        switch (iLevel)
        {
            case LogUtil.DBG:
                LogUtil.d(TAG, msg + " in state[" + strMain + ", " + strSub + "]");
                break;
                
            case LogUtil.INF:
                LogUtil.i(TAG, msg + " in state[" + strMain + ", " + strSub + "]");
                break;
                
            case LogUtil.WAR:
                LogUtil.w(TAG, msg + " in state[" + strMain + ", " + strSub + "]");
                break;
                
            case LogUtil.ERR:
                LogUtil.e(TAG, msg + " in state[" + strMain + ", " + strSub + "]");
                break;
                
            default:
                break;
        }
    }
    

}
