package com.travelrely.core.nr.msg;

import java.util.HashMap;

import android.text.TextUtils;

public class MsgId
{
    public static final int APP_AGENT_REGISTER_REQ = 0x01;//app向agent发送请求，宿主卡需要在归属地网络注册
    public static final int AGENT_APP_REGISTER_RSP = 0x02;

    public static final int AGENT_APP_AUTH_REQ = 0x03;
    public static final int APP_AGENT_AUTH_RSP = 0x04;

    public static final int APP_AGENT_CALLING_REQ = 0x05;
    public static final int AGENT_APP_CALLING_RSP = 0x06;

    public static final int APP_AGENT_CALLED_IND = 0x07;
    public static final int AGENT_APP_CALLED_REQ = 0x08;
    public static final int APP_AGENT_CALLED_RSP = 0x09;

    public static final int APP_AGENT_BYE_IND = 0x0A;
    public static final int AGENT_APP_BYE_IND = 0x0B;

    public static final int APP_AGENT_SMS_SEND_REQ = 0x0C;
    public static final int AGENT_APP_SMS_SEND_RSP = 0x0D;

    public static final int APP_AGENT_SMS_RECV_IND = 0x0E;
    public static final int AGENT_APP_SMS_RECV_REQ = 0x0F;
    public static final int APP_AGENT_SMS_RECV_RSP = 0x10;

    public static final int APP_AGENT_LAU_REQ = 0x11;
    public static final int AGENT_APP_LAU_RSP = 0x12;

    public static final int AGENT_APP_PAGING_REQ = 0x13;

    public static final int AGENT_APP_UE_ALERT = 0x14;
    public static final int APP_AGENT_TCP_RECONN = 0x15;
    public static final int AGENT_APP_TCP_RSP = 0x16;
    public static final int AGENT_APP_DISCONN_IND = 0x17;
    public static final int AGENT_APP_LAU_IND = 0x18;
    public static final int APP_AGENT_VERIFY_IND = 0x19;
    public static final int AGENT_APP_MESSAGE_IND = 0x1A;
    public static final int APP_AGENT_DTMF_IND = 0x1B;

    public static final int AGENT_APP_VOIP_REQ = 0x1C;
    public static final int APP_AGENT_VOIP_ALERT = 0x1D;
    public static final int AGENT_APP_VOIP_ALERT = 0x1E;
    public static final int APP_AGENT_VOIP_RSP = 0x1F;
    public static final int APP_AGENT_VOIP_BYE_IND = 0x20;
    public static final int AGENT_APP_VOIP_BYE_IND = 0x21;
    public static final int AGENT_APP_VOIP_RSP = 0x22;
    
    public static final int APP_AGENT_KEEPALIVE_REQ = 0x23;
    public static final int AGENT_APP_KEEPALIVE_RSP = 0x24;
    
    public static final int AGENT_APP_ERR_IND = 0x25;
    
    public static final int AGENT_APP_TEST_REQ = 0x26;
    public static final int APP_AGENT_TEST_RSP = 0x27;
    
    public static final int AGENT_APP_NEED_REG_IND = 0x28;
    public static final int AGENT_APP_STATE_ERR = 0x29;

    public static final int APP_AGENT_TEST_CALLING_REQ = 0x30;
    public static final int AGENT_APP_TEST_CALLING_RSP = 0x31;
    
    public static final int APP_AGENT_ON_OFF_IND = 0x32;
    
    public static final int AGENT_APP_SYSINFO_IND = 0x33;
    
    public static final int APP_AGENT_USER_VERIFY_REQ = 0x34;
    public static final int AGENT_APP_USER_VERIFY_RSP = 0x35;
    
    public static final int APP_AGENT_STORE_KEY_RSLT_IND = 0x36;
    
    public static final int AGENT_APP_SIM_CARD_FEE_IND = 0x37;
    public static final int APP_AGENT_SIM_CARD_FEE_RSP = 0x38;
    
    public static final int APP_AGENT_LSMS_SEND_REQ = 0x39;
    public static final int AGENT_APP_LSMS_AUTH_REQ = 0x40;
    
    public static final int APP_AGENT_HW_INFO_REQ = 0x41;
    public static final int AGENT_APP_HW_INFO_RSP = 0x42;
    
    public static final int APP_AGENT_POWER_ON_IND = 0x43;

    // App内部使用的消息
    public static final int APP_MSG_BEGIN = 0x100;
    public static final int APP_STATE_EXPIRE = APP_MSG_BEGIN + 0x01;
    public static final int APP_LOCK_SCREEN = APP_MSG_BEGIN + 0x02;
    public static final int APP_UNLOCK_SCREEN = APP_MSG_BEGIN + 0x03;

    public static final int APP_REG_EXPIRE = APP_MSG_BEGIN + 0x04;
    public static final int APP_REG_FAIL = APP_MSG_BEGIN + 0x05;
    public static final int APP_REG_SUCC = APP_MSG_BEGIN + 0x06;

    public static final int APP_SEND_SMS_EXPIRE = APP_MSG_BEGIN + 0x07;
    public static final int APP_SEND_SMS_FAIL = APP_MSG_BEGIN + 0x08;
    public static final int APP_SEND_SMS_SUCC = APP_MSG_BEGIN + 0x09;

    public static final int APP_SHUT_DOWN = APP_MSG_BEGIN + 0x0A;
    public static final int APP_BT_DISCONNECT = APP_MSG_BEGIN + 0x0B;
    
    public static final int APP_CALLING_CONNECTED = APP_MSG_BEGIN + 0x0C;
    public static final int APP_CALLED_CONNECTED = APP_MSG_BEGIN + 0x0D;
    
    public static final int APP_SHOW_CALL_ACT = APP_MSG_BEGIN + 0x0E;
    public static final int APP_CLOSE_CALL_ACT = APP_MSG_BEGIN + 0x0F;
    
    public static final int APP_PLAY_RINGTONE = APP_MSG_BEGIN + 0x10;
    public static final int APP_STOP_RINGTONE = APP_MSG_BEGIN + 0x11;
    
    public static final int APP_ANSWER = APP_MSG_BEGIN + 0x12;
    public static final int APP_HANG_UP = APP_MSG_BEGIN + 0x13;
    public static final int APP_BE_HUNG_UP = APP_MSG_BEGIN + 0x14;
    
    public static final int APP_CALLING_REQ = APP_MSG_BEGIN + 0x15;
    public static final int APP_CALLED_REQ = APP_MSG_BEGIN + 0x16;
    
    public static final int APP_SHOW_ERR_MSG = APP_MSG_BEGIN + 0x17;
    
    public static final int APP_SHOW_REG_FAIL = APP_MSG_BEGIN + 0x18;
    public static final int APP_SHOW_REG_SUCC = APP_MSG_BEGIN + 0x19;
    
    public static final int APP_STOP_RECORD = APP_MSG_BEGIN + 0x1A;
    public static final int APP_START_RECORD = APP_MSG_BEGIN + 0x1B;
    
    public static final int APP_CLOSE_NR = APP_MSG_BEGIN + 0x1C;
    public static final int APP_OPEN_NR = APP_MSG_BEGIN + 0x1D;
    public static final int APP_CLOSE_XH = APP_MSG_BEGIN + 0x1E;
    public static final int APP_OPEN_XH = APP_MSG_BEGIN + 0x1F;
    
    public static final int APP_TCP_DISCONNECTED = APP_MSG_BEGIN + 0x20;
    public static final int APP_TCP_CONNECTED = APP_MSG_BEGIN + 0x21;
    
    public static final int APP_INNER_RESET = APP_MSG_BEGIN + 0x22;
    public static final int NR_UI_SIM_FEE_ALERT = APP_MSG_BEGIN + 0x23;
    
    public static final int APP_GETUI_IND = APP_MSG_BEGIN + 0x24;
    
    public static final int BLE_NR_MSG = 0x200;
    public static final int BLE_NR_KEEPALIVE_IND = BLE_NR_MSG + 0x01;
    public static final int BLE_NR_REG_IND = BLE_NR_MSG + 0x02;
    
    public static final int UI_NR_MSG = 0x300;
    public static final int UI_NR_SEND_SMS_REQ = UI_NR_MSG + 0x01;

    public static final int APP_AGT_XH_MSG = 0x500;
    public static final int APP_AGT_XH_CALLING_REQ = APP_AGT_XH_MSG + 0x01;
    public static final int AGT_APP_XH_CALLING_RSP = APP_AGT_XH_MSG + 0x02;
    public static final int AGT_APP_XH_CALLED_REQ = APP_AGT_XH_MSG + 0x03;
    public static final int APP_AGT_XH_CALLED_RSP = APP_AGT_XH_MSG + 0x04;
    public static final int APP_AGT_XH_BYE_IND = APP_AGT_XH_MSG + 0x05;
    public static final int AGT_APP_XH_BYE_IND = APP_AGT_XH_MSG + 0x06;
    
    public static final int APP_AGT_XH_SMS_SEND_REQ = APP_AGT_XH_MSG + 0x07;
    public static final int AGT_APP_XH_SMS_SEND_RSP = APP_AGT_XH_MSG + 0x08;
    public static final int AGT_APP_XH_SMS_RECV_REQ = APP_AGT_XH_MSG + 0x09;
    public static final int APP_AGT_XH_SMS_RECV_RSP = APP_AGT_XH_MSG + 0x0A;
    public static final int AGT_APP_XH_UE_ALERT = APP_AGT_XH_MSG + 0x0B;
    public static final int APP_AGT_XH_DTMF_IND = APP_AGT_XH_MSG + 0x0C;

    public static final int APP_XH_MSG = 0x550;
    public static final int APP_XH_CALLED_REQ = APP_XH_MSG + 0x01;
    public static final int APP_XH_HANG_UP = APP_XH_MSG + 0x02;
    public static final int APP_XH_BE_HUNG_UP = APP_XH_MSG + 0x03;
    public static final int APP_XH_ANSWER = APP_XH_MSG + 0x04;
    
    private static HashMap<Integer, String> msg = new HashMap<Integer, String>();
    static
    {
        msg.put(0, "UNKNOW");
        msg.put(APP_AGENT_REGISTER_REQ, "APP_AGENT_REGISTER_REQ");
        msg.put(AGENT_APP_REGISTER_RSP, "AGENT_APP_REGISTER_RSP");
        msg.put(AGENT_APP_AUTH_REQ, "AGENT_APP_AUTH_REQ");
        msg.put(APP_AGENT_AUTH_RSP, "APP_AGENT_AUTH_RSP");
        msg.put(APP_AGENT_CALLING_REQ, "APP_AGENT_CALLING_REQ");
        msg.put(AGENT_APP_CALLING_RSP, "AGENT_APP_CALLING_RSP");
        msg.put(APP_AGENT_CALLED_IND, "APP_AGENT_CALLED_IND");
        msg.put(AGENT_APP_CALLED_REQ, "AGENT_APP_CALLED_REQ");
        msg.put(APP_AGENT_CALLED_RSP, "APP_AGENT_CALLED_RSP");

        msg.put(APP_AGENT_BYE_IND, "APP_AGENT_BYE_IND");
        msg.put(AGENT_APP_BYE_IND, "AGENT_APP_BYE_IND");
        msg.put(APP_AGENT_SMS_SEND_REQ, "APP_AGENT_SMS_SEND_REQ");
        msg.put(AGENT_APP_SMS_SEND_RSP, "AGENT_APP_SMS_SEND_RSP");
        msg.put(APP_AGENT_SMS_RECV_IND, "APP_AGENT_SMS_RECV_IND");
        msg.put(AGENT_APP_SMS_RECV_REQ, "AGENT_APP_SMS_RECV_REQ");
        msg.put(APP_AGENT_SMS_RECV_RSP, "APP_AGENT_SMS_RECV_RSP");
        
        msg.put(APP_AGENT_LAU_REQ, "APP_AGENT_LAU_REQ");
        msg.put(AGENT_APP_LAU_RSP, "AGENT_APP_LAU_RSP");
        msg.put(AGENT_APP_PAGING_REQ, "AGENT_APP_PAGING_REQ");
        msg.put(AGENT_APP_UE_ALERT, "AGENT_APP_UE_ALERT");
        msg.put(APP_AGENT_TCP_RECONN, "APP_AGENT_TCP_RECONN");
        msg.put(AGENT_APP_TCP_RSP, "AGENT_APP_TCP_RSP");
        msg.put(AGENT_APP_DISCONN_IND, "AGENT_APP_DISCONN_IND");
        msg.put(AGENT_APP_LAU_IND, "AGENT_APP_LAU_IND");
        msg.put(APP_AGENT_VERIFY_IND, "APP_AGENT_VERIFY_IND");

        msg.put(AGENT_APP_MESSAGE_IND, "AGENT_APP_MESSAGE_IND");
        msg.put(APP_AGENT_DTMF_IND, "APP_AGENT_DTMF_IND");
        msg.put(AGENT_APP_VOIP_REQ, "AGENT_APP_VOIP_REQ");
        msg.put(APP_AGENT_VOIP_ALERT, "APP_AGENT_VOIP_ALERT");
        msg.put(AGENT_APP_VOIP_ALERT, "AGENT_APP_VOIP_ALERT");
        msg.put(APP_AGENT_VOIP_RSP, "APP_AGENT_VOIP_RSP");
        msg.put(APP_AGENT_VOIP_BYE_IND, "APP_AGENT_VOIP_BYE_IND");
        msg.put(AGENT_APP_VOIP_BYE_IND, "AGENT_APP_VOIP_BYE_IND");
        msg.put(AGENT_APP_VOIP_RSP, "AGENT_APP_VOIP_RSP");
        msg.put(APP_AGENT_KEEPALIVE_REQ, "APP_AGENT_KEEPALIVE_REQ");
        msg.put(AGENT_APP_KEEPALIVE_RSP, "AGENT_APP_KEEPALIVE_RSP");
        msg.put(AGENT_APP_ERR_IND, "AGENT_APP_ERR_IND");
        msg.put(AGENT_APP_TEST_REQ, "AGENT_APP_TEST_REQ");
        msg.put(APP_AGENT_TEST_RSP, "APP_AGENT_TEST_RSP");
        msg.put(AGENT_APP_NEED_REG_IND, "AGENT_APP_NEED_REG_IND");
        msg.put(AGENT_APP_STATE_ERR, "AGENT_APP_STATE_ERR");
        
        msg.put(APP_AGENT_TEST_CALLING_REQ, "APP_AGENT_TEST_CALLING_REQ");
        msg.put(AGENT_APP_TEST_CALLING_RSP, "AGENT_APP_TEST_CALLING_RSP");
        
        msg.put(APP_AGENT_ON_OFF_IND, "APP_AGENT_ON_OFF_IND");
        
        msg.put(AGENT_APP_SYSINFO_IND, "AGENT_APP_SYSINFO_IND");
        
        msg.put(APP_AGENT_USER_VERIFY_REQ, "APP_AGENT_USER_VERIFY_REQ");
        msg.put(AGENT_APP_USER_VERIFY_RSP, "AGENT_APP_USER_VERIFY_RSP");
        
        msg.put(APP_AGENT_STORE_KEY_RSLT_IND, "APP_AGENT_STORE_KEY_RSLT_IND");
        
        msg.put(AGENT_APP_SIM_CARD_FEE_IND, "AGENT_APP_SIM_CARD_FEE_IND");
        msg.put(APP_AGENT_SIM_CARD_FEE_RSP, "APP_AGENT_SIM_CARD_FEE_RSP");
        
        msg.put(APP_AGENT_LSMS_SEND_REQ, "APP_AGENT_LSMS_SEND_REQ");
        msg.put(AGENT_APP_LSMS_AUTH_REQ, "AGENT_APP_LSMS_AUTH_REQ");
        
        msg.put(APP_AGENT_HW_INFO_REQ, "APP_AGENT_HW_INFO_REQ");
        msg.put(AGENT_APP_HW_INFO_RSP, "AGENT_APP_HW_INFO_RSP");
        msg.put(APP_AGENT_POWER_ON_IND, "APP_AGENT_POWER_ON_IND");
        
        msg.put(APP_MSG_BEGIN, "APP_MSG_BEGIN");
        msg.put(APP_STATE_EXPIRE, "APP_STATE_EXPIRE");
        msg.put(APP_LOCK_SCREEN, "APP_LOCK_SCREEN");
        msg.put(APP_UNLOCK_SCREEN, "APP_UNLOCK_SCREEN");
        msg.put(APP_REG_EXPIRE, "APP_REG_EXPIRE");
        msg.put(APP_REG_FAIL, "APP_REG_FAIL");
        msg.put(APP_REG_SUCC, "APP_REG_SUCC");
        msg.put(APP_SEND_SMS_EXPIRE, "APP_SEND_SMS_EXPIRE");
        msg.put(APP_SEND_SMS_FAIL, "APP_SEND_SMS_FAIL");
        msg.put(APP_SEND_SMS_SUCC, "APP_SEND_SMS_SUCC");
        msg.put(APP_SHUT_DOWN, "APP_SHUT_DOWN");
        msg.put(APP_BT_DISCONNECT, "APP_BT_DISCONNECT");
        msg.put(APP_CALLING_CONNECTED, "APP_CALLING_CONNECTED");
        msg.put(APP_CALLED_CONNECTED, "APP_CALLED_CONNECTED");
        msg.put(APP_SHOW_CALL_ACT, "APP_SHOW_CALL_ACT");
        msg.put(APP_CLOSE_CALL_ACT, "APP_CLOSE_CALL_ACT");
        msg.put(APP_PLAY_RINGTONE, "APP_PLAY_RINGTONE");
        msg.put(APP_STOP_RINGTONE, "APP_STOP_RINGTONE");
        msg.put(APP_ANSWER, "APP_ANSWER");
        msg.put(APP_HANG_UP, "APP_HANG_UP");
        msg.put(APP_BE_HUNG_UP, "APP_BE_HUNG_UP");
        msg.put(APP_CALLING_REQ, "APP_CALLING_REQ");
        msg.put(APP_CALLED_REQ, "APP_CALLED_REQ");
        msg.put(APP_SHOW_ERR_MSG, "APP_SHOW_ERR_MSG");
        msg.put(APP_SHOW_REG_FAIL, "APP_SHOW_REG_FAIL");
        msg.put(APP_SHOW_REG_SUCC, "APP_SHOW_REG_SUCC");
        msg.put(APP_STOP_RECORD, "APP_STOP_RECORD");
        msg.put(APP_START_RECORD, "APP_START_RECORD");
        msg.put(NR_UI_SIM_FEE_ALERT, "NR_UI_SIM_FEE_ALERT");
        msg.put(APP_GETUI_IND, "APP_GETUI_IND");

        msg.put(BLE_NR_MSG, "BLE_NR_MSG");
        msg.put(BLE_NR_KEEPALIVE_IND, "BLE_NR_KEEPALIVE_IND");
        msg.put(BLE_NR_REG_IND, "BLE_NR_REG_IND");
        
        msg.put(UI_NR_MSG, "UI_NR_MSG");
        msg.put(UI_NR_SEND_SMS_REQ, "UI_NR_SEND_SMS_REQ");
        
        msg.put(APP_AGT_XH_MSG, "APP_AGT_XH_MSG");
        msg.put(APP_AGT_XH_CALLING_REQ, "APP_AGT_XH_CALLING_REQ");
        msg.put(AGT_APP_XH_CALLING_RSP, "AGT_APP_XH_CALLING_RSP");
        msg.put(AGT_APP_XH_CALLED_REQ, "AGT_APP_XH_CALLED_REQ");
        msg.put(APP_AGT_XH_CALLED_RSP, "APP_AGT_XH_CALLED_RSP");
        msg.put(APP_AGT_XH_BYE_IND, "APP_AGT_XH_BYE_IND");
        msg.put(AGT_APP_XH_BYE_IND, "AGT_APP_XH_BYE_IND");
        msg.put(APP_AGT_XH_SMS_SEND_REQ, "APP_AGT_XH_SMS_SEND_REQ");
        msg.put(AGT_APP_XH_SMS_SEND_RSP, "AGT_APP_XH_SMS_SEND_RSP");
        msg.put(AGT_APP_XH_SMS_RECV_REQ, "AGT_APP_XH_SMS_RECV_REQ");
        msg.put(APP_AGT_XH_SMS_RECV_RSP, "APP_AGT_XH_SMS_RECV_RSP");
        msg.put(AGT_APP_XH_UE_ALERT, "AGT_APP_XH_UE_ALERT");
        msg.put(APP_AGT_XH_DTMF_IND, "APP_AGT_XH_DTMF_IND");
        
        msg.put(APP_XH_MSG, "APP_XH_MSG");
        msg.put(APP_XH_CALLED_REQ, "APP_XH_CALLED_REQ");
        msg.put(APP_XH_HANG_UP, "APP_XH_HANG_UP");
        msg.put(APP_XH_BE_HUNG_UP, "APP_XH_BE_HUNG_UP");
        msg.put(APP_XH_ANSWER, "APP_XH_ANSWER");
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
