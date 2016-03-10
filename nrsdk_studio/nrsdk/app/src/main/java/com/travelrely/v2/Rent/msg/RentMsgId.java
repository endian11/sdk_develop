package com.travelrely.v2.Rent.msg;

public class RentMsgId
{
    public static final int MSG_BEGIN = 0x00;
    
    public static final int APP_AGENT_REGISTER_REQ = 0x01;
    public static final int AGENT_APP_REGISTER_RSP = 0x02;

    public static final int AGENT_APP_AUTH_REQ = 0x03;
    public static final int APP_AGENT_AUTH_RSP = 0x04;

    public static final int APP_AGENT_CALLING_REQ = 0x05;
    public static final int AGENT_APP_CALLING_RSP = 0x06;

    public static final int APP_AGENT_CALLED_IND = 0x07;
    public static final int AGENT_APP_CALLED_REQ = MSG_BEGIN + 0x0C;
    public static final int APP_AGENT_CALLED_RSP = MSG_BEGIN + 0x0D;

    public static final int APP_AGENT_BYE_IND = MSG_BEGIN + 0x0E;
    public static final int AGENT_APP_BYE_IND = MSG_BEGIN + 0x0F;

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
    
    public static final int MSG_MAX = 0x2A;

    // App内部使用的消息
    public static final int APP_STATE_EXPIRE = MSG_MAX + 0x01;
    public static final int APP_LOCK_SCREEN = MSG_MAX + 0x02;
    public static final int APP_UNLOCK_SCREEN = MSG_MAX + 0x03;

    public static final int APP_REG_EXPIRE = MSG_MAX + 0x04;
    public static final int APP_REG_FAIL = MSG_MAX + 0x05;
    public static final int APP_REG_SUCC = MSG_MAX + 0x06;

    public static final int APP_SEND_SMS_EXPIRE = MSG_MAX + 0x07;
    public static final int APP_SEND_SMS_FAIL = MSG_MAX + 0x08;
    public static final int APP_SEND_SMS_SUCC = MSG_MAX + 0x09;

    public static final int APP_SHUT_DOWN = MSG_MAX + 0x0A;
    public static final int APP_BT_DISCONNECT = MSG_MAX + 0x0B;

    public static final int APP_CALLING_REQ = MSG_MAX + 0x15;
    public static final int APP_CALLING_CONNECTED = MSG_MAX + 0x0C;
    public static final int APP_CALLED_REQ = MSG_MAX + 0x16;
    public static final int APP_CALLED_CONNECTED = MSG_MAX + 0x0D;
    
    public static final int APP_PLAY_RINGTONE = MSG_MAX + 0x10;
    public static final int APP_STOP_RINGTONE = MSG_MAX + 0x11;
    
    public static final int APP_ANSWER = MSG_MAX + 0x12;
    public static final int APP_HANG_UP = MSG_MAX + 0x13;
    public static final int APP_BE_HUNG_UP = MSG_MAX + 0x14;
    
    public static final int APP_SHOW_ERR_MSG = MSG_MAX + 0x17;
    
    public static final int APP_LAU_FAIL = MSG_MAX + 0x18;
    public static final int APP_LAU_SUCC = MSG_MAX + 0x19;
    
    public static final int APP_STOP_RECORD = MSG_MAX + 0x1A;
    public static final int APP_START_RECORD = MSG_MAX + 0x1B;
}
