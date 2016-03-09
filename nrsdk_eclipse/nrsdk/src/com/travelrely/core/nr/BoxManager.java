package com.travelrely.core.nr;

import java.io.UnsupportedEncodingException;

import android.text.TextUtils;
import android.util.Log;

import com.travelrely.core.ble.BleMsgId;
import com.travelrely.core.ble.BtState;
import com.travelrely.core.nr.mt100.AppMessage;
import com.travelrely.core.nr.mt100.BTBoxIf;
import com.travelrely.core.nr.mt100.BTMessage;
import com.travelrely.core.nr.mt100.Common;
import com.travelrely.core.nr.mt100.Mt100Callback;
import com.travelrely.core.nr.util.ByteUtil;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.SpUtil;


public class BoxManager
{
    public static final int NEW_FW_VER = 0x01020000;
    
    private static final String TAG = BoxManager.class.getSimpleName();
    
    private static final byte[] SUCC = new byte[]{0x00, (byte) 0x90, 0x00};
    private static final byte[] FAIL = new byte[]{0x00, (byte) 0x90, 0x01};
    
    private static final byte[] keyStateReq = new byte[]{0x0F, 0x0F, 0x57, 0x00, 0x00};
    private static final byte[] macAddrReq = new byte[]{0x0F, 0x0F, 0x57, 0x01, 0x00};
    private static final byte[] keySaveReq = new byte[]{0x0F, 0x0F, 0x57, 0x02, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static final byte[] keyChkReq = new byte[]{0x0F, 0x0F, 0x57, 0x03, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static final byte[] BatteryQueryReq = new byte[]{0x0F, 0x0F, 0x57, 0x19, 0x00};

    private static final byte[] boxSnReq = new byte[]
    {
        (byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, 0x51, 0x00, 0x01, 0x0A
    };
    
    private static final byte[] CosVerReq = new byte[]
    {
        (byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, 0x51, 0x00, 0x02, 0x04
    };
    
    private static final byte[] simInfoReq = new byte[]
    {
        (byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, 0x55, 0x00, 0x00, 0x03,
        0x01, 0x09, 0x0A
    };
    
    private static final byte[] simAuthKey = new byte[]
    {
        (byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, 0x52, 0x00, 0x00, 0x11,
        0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    
    private static final byte[] aesKeyReq = new byte[]
    {
        (byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, 0x53, 0x00, 0x00, 0x10,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    
    private static BoxManager instance;
    
    private BoxCallback mBoxCallback;
    
    public boolean isMt100Ready = false;

    Mt100Callback mRsltCallback = new Mt100Callback()
    {
        @Override
        public void callRslt(byte[] rslt)
        {
            if (rslt == null || rslt.length == 0)
            {
                LogUtil.e(TAG, "rslt == null || rslt.length == 0");
                return;
            }
            
            LogUtil.i(TAG, "callRslt = " + ByteUtil.toHexString(rslt));
            
            if (mBoxCallback == null)
            {
                LogUtil.e(TAG, "mBoxCallback == null");
                return;
            }

            switch (rslt[1]&0xFF)
            {
                case BTBoxIf.MSG_INIT_CARD_COMPLETE:
                    if ((rslt[2]&0xFF) == 0x00)
                    {
                        mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_INIT_MT100_RSP, SUCC);
                    }

                    if ((rslt[2]&0xFF) == 0x01)
                    {
                        // 初始化卡失败
                        mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_INIT_MT100_RSP,
                                new byte[]{0x00, (byte) 0xCE, 0x01});
                    }
                    
                    if ((rslt[2]&0xFF) == 0x80)
                    {
                        // 检测卡失败(或无卡)
                        mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_INIT_MT100_RSP,
                                new byte[]{0x00, (byte) 0xCE, 0x00});
                    }
                    break;

                case BTBoxIf.MSG_READ_ESSEN_INFO:
                    parseEssenInfo(rslt);
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_READ_SIM_INFO_RSP,
                            new byte[]{0x00, (byte) 0x90, 0x00});
                    break;
                    
                case BTBoxIf.MSG_READ_AUTH_DATA:
                    if ((rslt[3]&0xFF) == 0x00)
                    {
                        iAuthErr = 0;
                        parseAuthRslt(rslt);
                        mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_AUTH_SIM_RSP,
                                new byte[]{0x00, (byte) 0x90, 0x00});
                    }

                    if ((rslt[3]&0xFF) == 0x01)// 鉴权失败
                    {
                        iAuthErr = 1;
                        LogUtil.e(TAG, "鉴权失败");
                        mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_AUTH_SIM_RSP,
                                new byte[]{0x00, (byte) 0x90, 0x01});
                    }
                    
                    if ((rslt[3]&0xFF) == 0x02)// 网络同步失败
                    {
                        iAuthErr = 2;
                        int len = rslt[0] - 3;
                        mAuthRslt = new byte[len];
                        System.arraycopy(rslt, 4, mAuthRslt, 0, len);
                        
                        LogUtil.e(TAG, "网络同步失败");
                        mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_AUTH_SIM_RSP,
                                new byte[]{0x00, (byte) 0x90, 0x00});
                    }
                    
                    if ((rslt[3]&0xFF) == 0x40)// 卡被更换
                    {
                        iAuthErr = 0x40;
                        LogUtil.e(TAG, "卡被更换");
                        mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_AUTH_SIM_RSP,
                                new byte[]{0x00, (byte) 0x90, 0x40});
                    }
                    
                    if ((rslt[3]&0xFF) == 0x80)// 无卡
                    {
                        iAuthErr = 0x80;
                        // 检测卡失败(或无卡)
                        mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_AUTH_SIM_RSP,
                                new byte[]{0x00, (byte) 0xCE, 0x00});
                    }
                    break;

                default:
                    break;
            }
        }

        @Override
        public void sendBtMsg(byte[] msg)
        {
            if (msg == null || msg.length == 0)
            {
                Log.e(TAG, "bt msg == null || msg.length == 0");
                return;
            }
            
            if (mBoxCallback != null)
            {
                mBoxCallback.onBtMsgReady(msg);
            }
            else
            {
                Log.e(TAG, "mBoxCallback == null");
            }
        }
    };
    
    public static BoxManager getDefault()
    {
        if (instance == null)
        {
            instance = new BoxManager();
        }

        return instance;
    }
    
    private BoxManager()
    {

    }
    
    public void setCallback(BoxCallback l)
    {
        this.mBoxCallback = l;
    }
    
    public void sendKeyStateReq()
    {
        if (mBoxCallback != null)
        {
            LogUtil.i(1, "AtoB003");
            mBoxCallback.onBtMsgReady(keyStateReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    public void sendAddrReq()
    {
        if (mBoxCallback != null)
        {
            LogUtil.i(1, "AtoB004");
            mBoxCallback.onBtMsgReady(macAddrReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    public void sendKeySaveReq(String key)
    {
        if (TextUtils.isEmpty(key))
        {
            LogUtil.e(TAG, "send key to box, but key is empty");
            return;
        }
        if (mBoxCallback != null)
        {
            LogUtil.i(1, "AtoB005,"+key);
            System.arraycopy(key.getBytes(), 0, keySaveReq, 5, 6);
            mBoxCallback.onBtMsgReady(keySaveReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    public void sendKeyChkReq(String key)
    {
        if (TextUtils.isEmpty(key))
        {
            LogUtil.e(TAG, "key is not valid");
            return;
        }
        if (mBoxCallback != null)
        {
            LogUtil.i(1, "AtoB006,%s", Common.bytesToHexString(key.getBytes()));
            System.arraycopy(key.getBytes(), 0, keyChkReq, 5, 6);
            mBoxCallback.onBtMsgReady(keyChkReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    public void queryBattery()
    {
        if (mBoxCallback != null)
        {
            LogUtil.i(1, "AtoB007");
            mBoxCallback.onBtMsgReady(BatteryQueryReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    public void sendBoxSnReq()
    {
        if (mBoxCallback != null)
        {
            LogUtil.i(2, "AtoM001");
            mBoxCallback.onBtMsgReady(boxSnReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    public void sendCosVerReq()
    {
        if (mBoxCallback != null)
        {
            LogUtil.i(2, "AtoM002");
            mBoxCallback.onBtMsgReady(CosVerReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    public void sendAesKeyReq(byte[] key)
    {
        if (key == null || key.length == 0)
        {
            LogUtil.e(TAG, "aes key is not valid");
            return;
        }
        if (mBoxCallback != null)
        {
            LogUtil.i(2, "AtoM004,%s", Common.bytesToHexString(key));
            System.arraycopy(key, 0, aesKeyReq, 9, 16);
            mBoxCallback.onBtMsgReady(aesKeyReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    private void sendSimInfoReq()
    {
        if (mBoxCallback != null)
        {
            LogUtil.i(3, "AtoS001");
            mBoxCallback.onBtMsgReady(simInfoReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    private void sendSimAuthReq(byte[] authReq)
    {
        if (mBoxCallback != null)
        {
            LogUtil.i(3, "AtoS002,%s", Common.bytesToHexString(authReq));
            mBoxCallback.onBtMsgReady(authReq);
        }
        else
        {
            Log.e(TAG, "mBoxCallback == null");
        }
    }
    
    public void setAuthCipherKey(byte[] key)
    {
        System.arraycopy(key, 0, simAuthKey, 10, key.length);
    }
    
    public void sendAuthCipherKey(boolean on)
    {
        if (mBoxCallback == null)
        {
            Log.e(TAG, "mBoxCallback == null");
        }

        if (on)
        {
            LogUtil.i(2, "AtoM003,1");
            mBoxCallback.onBtMsgReady(simAuthKey);
        }
        else
        {
            LogUtil.i(2, "AtoM003,0");
            byte[] key = new byte[]{(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, 0x52, 0x00, 0x00, 0x11, 0x00, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
            mBoxCallback.onBtMsgReady(key);
        }
    }
    
    private byte[] ul01ota = null;
    private byte[] dl01ota = null;
    private byte[] ul02ota = null;
    private byte[] dl04ota = null;
    private byte[] ul04ota = null;
    
    public void sendUl01Req()
    {
        if (mBoxCallback == null)
        {
            Log.e(TAG, "mBoxCallback == null");
            return;
        }

        mBoxCallback.onBtMsgReady(new byte[]{(byte) 0xF0, (byte) 0xF0, 0x75,
                0x02, 0x00, 0x59, 0x00, 0x00, 0x01, 0x00});
    }
    
    public void sendDl01Req()
    {
        if (mBoxCallback == null)
        {
            Log.e(TAG, "mBoxCallback == null");
            return;
        }

        byte[] head = new byte[]{(byte) 0xF0, (byte) 0xF0, 0x75,
                0x02, 0x00, 0x59, 0x00, 0x01, 0x00};
        head[head.length-1] = (byte) dl01ota.length;
        
        byte[] packet = new byte[head.length+dl01ota.length];
        System.arraycopy(head, 0, packet, 0, head.length);
        System.arraycopy(dl01ota, 0, packet, head.length, dl01ota.length);
        mBoxCallback.onBtMsgReady(packet);
    }
    
    public void setUl01Ota(byte[] ota)
    {
        ul01ota = ota;
    }
    
    public byte[] getUl01Ota()
    {
        return ul01ota;
    }

    public void setDl01Ota(byte[] ota)
    {
        dl01ota = ota;
    }
    
    public byte[] getDl01Ota()
    {
        return dl01ota;
    }
    
    public void setUl02Ota(byte[] ota)
    {
        ul02ota = ota;
    }
    
    public byte[] getUl02Ota()
    {
        return ul02ota;
    }
    
    public void setDl04Ota(byte[] ota)
    {
        dl04ota = ota;
    }
    
    public byte[] getDl04Ota()
    {
        return dl04ota;
    }
    
    public void setUl04Ota(byte[] ota)
    {
        ul04ota = ota;
    }
    
    public byte[] getUl04Ota()
    {
        return ul04ota;
    }
    
    public void sendCancelReq()
    {
        if (mBoxCallback == null)
        {
            Log.e(TAG, "mBoxCallback == null");
            return;
        }

        byte[] head = new byte[]{(byte) 0xF0, (byte) 0xF0, 0x75,
                0x02, 0x00, 0x59, 0x00, 0x01, 0x00};
        head[head.length-1] = (byte) dl04ota.length;
        
        byte[] packet = new byte[head.length+dl04ota.length];
        System.arraycopy(head, 0, packet, 0, head.length);
        System.arraycopy(dl04ota, 0, packet, head.length, dl04ota.length);
        mBoxCallback.onBtMsgReady(packet);
    }
    
    /**
     * 初始化MT100模块
     */
    public boolean initMt100()
    {
        if (SpUtil.getCosVer() >= NEW_FW_VER)
        {
            sendAuthCipherKey(false);
        }
        else
        {
            BTBoxIf.initBTBoxIntf(1, mRsltCallback);
        }
        return true;
    }
    
    public void close()
    {
        BTBoxIf.uninitBTBoxIntf();
        isMt100Ready = false;
        clearBuffer();
        Log.d(TAG, "close");
    }

    public void readEssenInfo()
    {
        if (SpUtil.getCosVer() >= NEW_FW_VER)
        {
            sendSimInfoReq();
        }
        else
        {
            BTBoxIf.readEssenInfo(null);
        }
    }

    public void readSimAuthData()
    {
        if (SpUtil.getCosVer() >= NEW_FW_VER)
        {
            if (mNewAuthParam == null)
            {
                LogUtil.e(TAG, "Auth Param is null");
                return;
            }
            sendSimAuthReq(mNewAuthParam);
        }
        else
        {
            if (mOldAuthParam == null)
            {
                LogUtil.e(TAG, "Auth Param is null");
                return;
            }
            BTBoxIf.readAuthData(mOldAuthParam);
        }
    }
    
    public void msgEntry(int main, int sub, byte[] msg)
    {
        LogUtil.i(TAG, ByteUtil.toHexString(msg));

        if (sub == BtState.SUB_W_COS_VER_RSP)
        {
            if ((msg[0]&0xFF) == 0x01
                    && (msg[msg.length-2]&0xFF) == 0x90
                    && (msg[msg.length-1]&0xFF) == 0x00)
            {
                mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_COS_VER_RECEIVED,
                        ByteUtil.subArray(msg, 2, msg[1]&0xff));
                return;
            }
            
            if ((msg[0]&0xFF) == 0x80
                    && (msg[1]&0xFF) == 0x00
                    && (msg[2]&0xFF) == 0x00)
            {
                sendCosVerReq();
            }
        }
        
        if (sub == BtState.SUB_W_BOX_SN_RSP)
        {
            if ((msg[0]&0xFF) == 0x01
                    && (msg[msg.length-2]&0xFF) == 0x90
                    && (msg[msg.length-1]&0xFF) == 0x00)
            {
                mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_BOX_SN_RSP,
                        ByteUtil.subArray(msg, 2, msg[1]&0xff));
                return;
            }
            
            if ((msg[0]&0xFF) == 0x80
                    && (msg[1]&0xFF) == 0x00
                    && (msg[2]&0xFF) == 0x00)
            {
                sendBoxSnReq();
            }
        }

        int id = 0x00;
        if (msg != null && msg.length > 4)
        {
            id = ByteUtil.getInt(msg);
        }

        switch (id)
        {
            case 0x80570F0F:
                if (msg[5]==0x00)
                {
                    LogUtil.i(1, "BtoA003,0");
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_FIND_BOX_SUCC, SUCC);
                }
                else
                {
                    LogUtil.i(1, "BtoA003,1");
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_FIND_BOX_FAIL, FAIL);
                }
                break;
                
            case 0x81570F0F:
                mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_MAC_RECEIVED,
                        ByteUtil.subArray(msg, 5, msg[4]&0xff));
                break;
                
            case 0x82570F0F:
                if (msg[5]==0x00)
                {
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_MATCH_SUCC, SUCC);
                }
                else
                {
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_MATCH_FAIL, FAIL);
                }
                break;
                
            case 0x83570F0F:
                if (msg[5]==0x00)
                {
                    LogUtil.i(1, "BtoA006,0");
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_KEY_CHK_SUCC, SUCC);
                }
                else
                {
                    LogUtil.i(1, "BtoA006,1");
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_KEY_CHK_FAIL, FAIL);
                }
                break;
                
            case 0x99570F0F:
                SpUtil.setBoxBattery(msg[5]);
                LogUtil.i(1, "BtoA007,%d", msg[5]);
                mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_QUERY_BATTERY_RSP, SUCC);
                break;

            default:
                if (SpUtil.getCosVer() >= NEW_FW_VER)
                {
                    newMsgEntry(main, sub, msg);
                }
                else
                {
                    sendMsgToMt100(msg);
                }
                break;
        }
    }
    
    private void newMsgEntry(int main, int sub, byte[] msg)
    {
        switch (sub)
        {
            case BtState.SUB_W_AES_KEY_RSP:
                if (buffer(msg))
                {
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_AES_KEY_RSP,
                            msgBuff);
                    clearBuffer();
                }
                break;
                
            case BtState.SUB_W_MT100_READY:
                if (buffer(msg))
                {
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_INIT_MT100_RSP,
                            msgBuff);
                    clearBuffer();
                }
                break;
                
            case BtState.SUB_W_SIM_INFO:
                if (buffer(msg))
                {
                    parseNewEssenInfo(msgBuff);
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_READ_SIM_INFO_RSP, SUCC);
                    clearBuffer();
                }
                break;

            case BtState.SUB_W_SIM_AUTH_RSLT:
                if (buffer(msg))
                {
                    parseNewAuthRslt(msgBuff);
                    clearBuffer();
                }
                break;
                
            case BtState.SUB_W_BOX_CIPHER_RSP:
                if (buffer(msg))
                {
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_SAVE_CIPHER_KEY_RSP,
                            msgBuff);
                    clearBuffer();
                }
                break;
                
            case BtState.SUB_W_SIM_UL01OTA:
                if (buffer(msg))
                {
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_READ_UL01_RSP,
                            msgBuff);
                    clearBuffer();
                }
                
                break;
                
            case BtState.SUB_W_SIM_DL01OTA_SAVE_RSP:
                if (buffer(msg))
                {
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_SAVE_DL01_RSP,
                            msgBuff);
                    clearBuffer();
                }
                break;
                
            case BtState.SUB_W_VSIM_CANCEL_RSP:
                if (buffer(msg))
                {
                    mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_CANCEL_VSIM_RSP,
                            msgBuff);
                    clearBuffer();
                }
                break;

            default:
                break;
        }
    }
    
    /**
     * 当BLE通信模块接收到数据时调用此接口把数据递交给MT100模块
     */
    private void sendMsgToMt100(byte[] msg)
    {
        BTMessage.notifyData(msg, msg.length);
    }
    
    private byte[] byteToString(byte[] data)
    {
        StringBuffer sBuffer = new StringBuffer();
        String testStr = null;
        String onStr = null;
        if (data != null)
        {
            String hex = null;
            for (int i = 0; i < data.length; i++)
            {
                hex = Integer.toHexString(data[i] & 0xFF);
                if (hex.length() == 1)
                {
                    hex = "0" + hex;
                }
                String testChar1;
                String testChar2;
                testChar1 = hex.substring(0, 1);
                testChar2 = hex.substring(1, 2);
                testStr = testChar2 + testChar1;
                sBuffer.append(testStr);
            }
            onStr = sBuffer.toString().substring(
                    sBuffer.toString().length() - 15);
            imsiStr = onStr;
            LogUtil.i(TAG, "IMSI = " + onStr);
        }

        byte[] midbytes = null;
        try
        {
            midbytes = onStr.getBytes("UTF8");
            for (int k = 0; k < midbytes.length; k++)
            {
                midbytes[k] = (byte) (midbytes[k] - 48);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return midbytes;
    }

    private byte[] imsi;
    private String imsiStr;
    private String smsp;
    private String msisdn;
    private void parseEssenInfo(byte[] msg)
    {
        int imsiLen;
        int smspLen;
        int msisdnLen;
        
        if (msg == null || msg.length == 0)
        {
            return;
        }
        
        int msgLen = msg[0];
        int pos = 2;
        while (pos < msgLen+1)
        {
            if (msg[pos] == 0x01)
            {
                imsiLen = msg[pos+1];
                byte[] imsi = new byte[imsiLen];
                System.arraycopy(msg, pos+2, imsi, 0, imsiLen);
                this.imsi = byteToString(imsi);
                
                pos = pos + 1 + imsiLen + 1;
            }
            
            if (msg[pos] == 0x09)
            {
                smspLen = msg[pos+1];
                byte[] smsp = new byte[smspLen];
                System.arraycopy(msg, pos+2, smsp, 0, smspLen);
                this.smsp = new String(smsp);
                
                pos = pos + 1 + smspLen + 1;
            }
            
            if (msg[pos] == 0x0a)
            {
                msisdnLen = msg[pos+1];
                byte[] msisdn = new byte[msisdnLen];
                System.arraycopy(msg, pos+2, msisdn, 0, msisdnLen);
                this.msisdn = new String(msisdn);
                
                pos = pos + 1 + msisdnLen + 1;
            }
        }
    }
    
    private byte[] msgBuff = null;
    private int pos = 0;
    
    private boolean buffer(byte[] msg)
    {
        if (pos > 0)
        {
            System.arraycopy(msg, 0, msgBuff, pos, msg.length);
            pos = pos + msg.length;
            
            return pos == msgBuff.length ? true : false;
        }
        
        msgBuff = new byte[(msg[0]&0xFF) + 3];
        System.arraycopy(msg, 0, msgBuff, 0, msg.length);
        pos = pos + msg.length;
        
        return pos == msgBuff.length ? true : false;
    }
    
    private void clearBuffer()
    {
        msgBuff = null;
        pos = 0;
    }
    
    private void parseNewEssenInfo(byte[] msg)
    {
        int imsiLen;
        int smspLen;
        int msisdnLen;
        
        if (msg == null || msg.length == 0)
        {
            return;
        }
        
        if ((msg[msg.length-2]&0xFF) != 0x90
                || (msg[msg.length-1]&0xFF) != 0x00)
        {
            return;
        }
        
        int msgLen = msg[0];
        int pos = 1;
        while (pos < msgLen)
        {
            if (msg[pos] == 0x01)
            {
                imsiLen = msg[pos+1];
                byte[] imsi = new byte[imsiLen];
                System.arraycopy(msg, pos+2, imsi, 0, imsiLen);
                this.imsi = byteToString(imsi);
                
                pos = pos + 1 + imsiLen + 1;
            }
            
            if(msg[pos] == 0x09)
            {
                smspLen = msg[pos+1];
                byte[] smsp = new byte[16];
                byte[] tmp = ByteUtil.subArray(msg, pos+2, smspLen);
                if(tmp[0] == (byte)0xFF)
                {
                    //没有smsp
                    this.smsp = "";
                }
                else
                {
                    int len = AppMessage.convertBcd(
                            ByteUtil.subArray(tmp,2,smspLen-2),
                            (byte)(tmp[0]-1), smsp);
                    this.smsp = new String(ByteUtil.subArray(smsp, 0, len));
                }
                pos = pos + 1 + smspLen + 1;
            }
            
            if (msg[pos] == 0x0a)
            {
                msisdnLen = msg[pos+1];
                byte[] msisdn = new byte[16];
                byte[] tmp = ByteUtil.subArray(msg, pos+2, msisdnLen);
                if(tmp[0] == (byte)0xFF)
                {
                    //没有msisdn
                    this.msisdn = "";
                }
                else
                {
                    int len = AppMessage.convertBcd(
                            ByteUtil.subArray(tmp, 2, msisdnLen-2),
                            (byte)(tmp[0]-1), msisdn);
                    this.msisdn = new String(ByteUtil.subArray(msisdn, 0, len));
                }
                
                pos = pos + 1 + msisdnLen + 1;
            }
        }
    }
    
    public byte[] getImsi()
    {
        return imsi;
    }
    
    public String getImsiStr()
    {
        return imsiStr;
    }
    
    public String getSmsp()
    {
        return smsp;
    }
    
    public String getMsisdn()
    {
        return msisdn;
    }

    private byte[] mOldAuthParam = null;
    private byte[] mNewAuthParam = null;
    private byte[] mAuthRslt = null;
    private int iAuthErr = 0;
    
    public void setAuthParam(byte[] rand, byte[] autn)
    {
        //LogUtil.i(TAG, "rand = " + ByteUtil.toHexString(rand));
        //LogUtil.i(TAG, "autn = " + ByteUtil.toHexString(autn));
        
        if (SpUtil.getCosVer() >= NEW_FW_VER)
        {
            setNewAuthParam(rand, autn);
            //return;
        }

        byte type = 0x00;
        int apduLen = 0;

        if (autn != null)
        {
            apduLen = 36;
            type = 0x01;
        }
        else
        {
            apduLen = 19;
        }

        mOldAuthParam = new byte[apduLen];
        mOldAuthParam[0] = (byte) (apduLen -1);
        mOldAuthParam[1] = type;
        mOldAuthParam[2] = (byte) rand.length;

        System.arraycopy(rand, 0, mOldAuthParam, 3, rand.length);

        if (autn != null)
        {
            mOldAuthParam[3 + rand.length] = (byte)autn.length;
            System.arraycopy(autn, 0, mOldAuthParam, rand.length + 4, autn.length);
        }

        LogUtil.i(TAG, "old_auth_in = " + ByteUtil.toHexString(mOldAuthParam));
    }
    
    private void parseAuthRslt(byte[] data)
    {
        int authLen = data[0] - 3;
        mAuthRslt = new byte[authLen];
        System.arraycopy(data, 4, mAuthRslt, 0, authLen);

        LogUtil.i(TAG, "SRES = " + ByteUtil.toHexString(mAuthRslt));
    }
    
    private void setNewAuthParam(byte[] rand, byte[] autn)
    {
        byte[] head = new byte[]{(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, 0x58, 0x00, 0x00};

        int apduLen = 0;

        if (autn != null)
        {
            apduLen = head.length + 33;
        }
        else
        {
            apduLen = head.length + 17;
        }

        mNewAuthParam = new byte[apduLen];
        System.arraycopy(head, 0, mNewAuthParam, 0, head.length);

        mNewAuthParam[head.length] = 0x10;
        System.arraycopy(rand, 0, mNewAuthParam, head.length+1, rand.length);

        if (autn != null)
        {
            mNewAuthParam[head.length] = 0x20;
            System.arraycopy(autn, 0, mNewAuthParam,
                    head.length + 1 + rand.length, autn.length);
        }

        LogUtil.i(TAG, "new_auth_in = " + ByteUtil.toHexString(mNewAuthParam));
    }
    
    private void parseNewAuthRslt(byte[] msg)
    {
        /*
        int authLen = 0;
        
        if ((msg[msg.length-2]&0xFF) == 0x90
                && (msg[msg.length-1]&0xFF) == 0x00)
        {
            authLen = msg[0]&0xff;
            mAuthRslt = new byte[authLen];
            System.arraycopy(msg, 1, mAuthRslt, 0, authLen);
            
            LogUtil.i(TAG, "SRES = " + ByteUtil.toHexString(mAuthRslt));
            
            mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_AUTH_SIM_RSP,
                    new byte[]{0x00, (byte) 0x90, 0x00});
        }
        else
        {
            mBoxCallback.onBoxMsgArrived(BleMsgId.BOX_BLE_AUTH_SIM_RSP, msg);
        }*/
        byte[] tmp = new byte[128];
        AppMessage.handleAppMessage2((byte)0x58, mOldAuthParam, msg, tmp);
        mRsltCallback.callRslt(tmp);
    }
    
    public int getAuthErr()
    {
        return iAuthErr;
    }
    
    public byte[] getAuthRslt()
    {
        return mAuthRslt;
    }
    
    public void clearAuth()
    {
        mOldAuthParam = null;
        mNewAuthParam = null;
        mAuthRslt = null;
        iAuthErr = 0;
    }
    
    public boolean isConnected = true;
    
    public interface BoxCallback
    {
        /**
         * 当MT100模块处理好需要发送的数据时调用此接口发送到蓝牙盒子
         * 
         * @param data
         */
        public void onBtMsgReady(byte[] msg);

        /**
         * 当处理好最终的数据结果时调用此接口通知APP
         * 
         * @param rslt
         */        
        public void onBoxMsgArrived(int iMsgId, byte[] msg);
    }
}
