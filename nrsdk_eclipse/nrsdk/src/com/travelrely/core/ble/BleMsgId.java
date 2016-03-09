package com.travelrely.core.ble;

import java.util.HashMap;

import android.text.TextUtils;

public class BleMsgId
{
    public final static int NR_BLE_MSG = 0;
    public final static int NR_BLE_FIND_BOX = 1;
    public final static int NR_BLE_MATCH_BOX = 2;
    public final static int NR_BLE_SCAN_BOX = 4;
    public final static int NR_BLE_DISCONNECT_IND = 5;
    public final static int NR_BLE_READ_SIM_IND = 6;
    public final static int NR_BLE_AUTH_SIM_IND = 7;
    public final static int NR_BLE_CIPHER_ON_IND = 8;
    public final static int NR_BLE_CIPHER_OFF_IND = 9;
    public final static int NR_BLE_RAND_IND = 10;
    public final static int NR_BLE_NO_RAND_IND = 11;
    
    public final static int BLE_BLE_MSG = 100;
    public final static int BLE_BLE_STATE_EXPIRE = BLE_BLE_MSG + 0x01;
    public final static int BLE_BLE_CONNECT_SUCC = BLE_BLE_MSG + 0x02;
    public final static int BLE_BLE_CONNECT_FAIL = BLE_BLE_MSG + 0x03;
    public final static int BLE_BLE_SERVICE_SUCC = BLE_BLE_MSG + 0x04;
    public final static int BLE_BLE_SERVICE_FAIL = BLE_BLE_MSG + 0x05;
    public final static int BLE_BLE_DISCONNECT = BLE_BLE_MSG + 0x06;
    
    public final static int BOX_BLE_MSG = 200;
    public final static int BOX_BLE_FIND_BOX_SUCC = BOX_BLE_MSG + 0x01;
    public final static int BOX_BLE_FIND_BOX_FAIL = BOX_BLE_MSG + 0x02;
    public final static int BOX_BLE_MAC_RECEIVED = BOX_BLE_MSG + 0x03;
    public final static int BOX_BLE_MATCH_SUCC = BOX_BLE_MSG + 0x04;
    public final static int BOX_BLE_MATCH_FAIL = BOX_BLE_MSG + 0x05;
    public final static int BOX_BLE_KEY_CHK_SUCC = BOX_BLE_MSG + 0x06;
    public final static int BOX_BLE_KEY_CHK_FAIL = BOX_BLE_MSG + 0x07;
    public final static int BOX_BLE_INIT_MT100_RSP = BOX_BLE_MSG + 0x08;
    public final static int BOX_BLE_READ_SIM_INFO_RSP = BOX_BLE_MSG + 0x0A;
    public final static int BOX_BLE_AUTH_SIM_RSP = BOX_BLE_MSG + 0x0B;
    public final static int BOX_BLE_COS_VER_RECEIVED = BOX_BLE_MSG + 0x0C;
    public final static int BOX_BLE_SAVE_CIPHER_KEY_RSP = BOX_BLE_MSG + 0x0D;
    
    public final static int BOX_BLE_READ_UL01_RSP = BOX_BLE_MSG + 0x0E;
    public final static int BOX_BLE_SAVE_DL01_RSP = BOX_BLE_MSG + 0x0F;
    public final static int BOX_BLE_CANCEL_VSIM_RSP = BOX_BLE_MSG + 0x10;
    
    public final static int BOX_BLE_BOX_SN_RSP = BOX_BLE_MSG + 0x11;
    public final static int BOX_BLE_AES_KEY_RSP = BOX_BLE_MSG + 0x12;
    
    public final static int BOX_BLE_QUERY_BATTERY_RSP = BOX_BLE_MSG + 0x13;
    
    public final static int UI_BLE_MSG = 300;
    public final static int UI_BLE_READ_UL01 = UI_BLE_MSG + 0x01;
    public final static int UI_BLE_SAVE_DL01 = UI_BLE_MSG + 0x02;
    public final static int UI_BLE_CANCLE_VSIM = UI_BLE_MSG + 0x03;

    
    private static HashMap<Integer, String> msg = new HashMap<Integer, String>();
    static
    {
        msg.put(NR_BLE_MSG, "NR_BLE_MSG");
        msg.put(NR_BLE_FIND_BOX, "NR_BLE_FIND_BOX");
        msg.put(NR_BLE_MATCH_BOX, "NR_BLE_MATCH_BOX");
        msg.put(NR_BLE_SCAN_BOX, "NR_BLE_SCAN_BOX");
        msg.put(NR_BLE_DISCONNECT_IND, "NR_BLE_DISCONNECT_IND");
        msg.put(NR_BLE_READ_SIM_IND, "NR_BLE_READ_SIM_IND");
        msg.put(NR_BLE_AUTH_SIM_IND, "NR_BLE_AUTH_SIM_IND");
        msg.put(NR_BLE_CIPHER_ON_IND, "NR_BLE_CIPHER_ON_IND");
        msg.put(NR_BLE_CIPHER_OFF_IND, "NR_BLE_CIPHER_OFF_IND");
        msg.put(NR_BLE_RAND_IND, "NR_BLE_RAND_IND");
        msg.put(NR_BLE_NO_RAND_IND, "NR_BLE_NO_RAND_IND");
        
        msg.put(BLE_BLE_MSG, "BLE_BLE_MSG");
        msg.put(BLE_BLE_STATE_EXPIRE, "BLE_BLE_STATE_EXPIRE");
        msg.put(BLE_BLE_CONNECT_SUCC, "BLE_BLE_CONNECT_SUCC");
        msg.put(BLE_BLE_CONNECT_FAIL, "BLE_BLE_CONNECT_FAIL");
        msg.put(BLE_BLE_SERVICE_SUCC, "BLE_BLE_SERVICE_SUCC");
        msg.put(BLE_BLE_SERVICE_FAIL, "BLE_BLE_SERVICE_FAIL");
        msg.put(BLE_BLE_DISCONNECT, "BLE_BLE_DISCONNECT");

        msg.put(BOX_BLE_MSG, "BOX_BLE_MSG");
        msg.put(BOX_BLE_FIND_BOX_SUCC, "BOX_BLE_FIND_BOX_SUCC");
        msg.put(BOX_BLE_FIND_BOX_FAIL, "BOX_BLE_FIND_BOX_FAIL");
        msg.put(BOX_BLE_MAC_RECEIVED, "BOX_BLE_MAC_RECEIVED");
        msg.put(BOX_BLE_MATCH_SUCC, "BOX_BLE_MATCH_SUCC");
        msg.put(BOX_BLE_MATCH_FAIL, "BOX_BLE_MATCH_FAIL");
        msg.put(BOX_BLE_KEY_CHK_SUCC, "BOX_BLE_KEY_CHK_SUCC");
        msg.put(BOX_BLE_KEY_CHK_FAIL, "BOX_BLE_KEY_CHK_FAIL");
        msg.put(BOX_BLE_INIT_MT100_RSP, "BOX_BLE_INIT_MT100_RSP");
        msg.put(BOX_BLE_READ_SIM_INFO_RSP, "BOX_BLE_READ_SIM_INFO_RSP");
        msg.put(BOX_BLE_AUTH_SIM_RSP, "BOX_BLE_AUTH_SIM_RSP");
        msg.put(BOX_BLE_COS_VER_RECEIVED, "BOX_BLE_COS_VER_RECEIVED");
        msg.put(BOX_BLE_SAVE_CIPHER_KEY_RSP, "BOX_BLE_SAVE_CIPHER_KEY_RSP");
        msg.put(BOX_BLE_READ_UL01_RSP, "BOX_BLE_READ_UL01_RSP");
        msg.put(BOX_BLE_SAVE_DL01_RSP, "BOX_BLE_SAVE_DL01_RSP");
        msg.put(BOX_BLE_CANCEL_VSIM_RSP, "BOX_BLE_CANCEL_VSIM_RSP");
        msg.put(BOX_BLE_BOX_SN_RSP, "BOX_BLE_BOX_SN_RSP");
        msg.put(BOX_BLE_AES_KEY_RSP, "BOX_BLE_AES_KEY_RSP");
        
        msg.put(BOX_BLE_QUERY_BATTERY_RSP, "BOX_BLE_QUERY_BATTERY_RSP");
        
        msg.put(UI_BLE_MSG, "UI_BLE_MSG");
        msg.put(UI_BLE_READ_UL01, "UI_BLE_READ_UL01");
        msg.put(UI_BLE_SAVE_DL01, "UI_BLE_SAVE_DL01");
        msg.put(UI_BLE_CANCLE_VSIM, "UI_BLE_CANCLE_VSIM");
    }
    
    public static String getMsgStr(int iMsgId)
    {
        String result = msg.get(iMsgId);
        if (TextUtils.isEmpty(result))
        {
            result = "UNKNOW";
        }
        return result;
    }
}
