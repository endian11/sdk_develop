package com.travelrely.core;

public final class IAction
{
    public static final String base = "com.travelrely.v2";

    public static final String REFRESH_UI = base + ".REFRESH_UI";
    
    //public static final String LOGIN_SUCC = base + ".LOGIN_SUCC";
    public static final String MY_CHANGED = base + ".onMyChanged";
    
    public static final String CONTACT_CHANGED = base + ".onContactChanged";

    public static final String BLE_CONNECTED = base + ".BLE_CONNECTED";
    public static final String BLE_DISCONNECT = base + ".BLE_DISCONNECT";
    public static final String BLE_CONN_FAIL = base + ".BLE_CONN_FAIL";
    public static final String BLE_CONN_EXPIRE = base + ".BLE_CONN_EXPIRE";
    
    public static final String BOX_FOUND = base + ".BOX_FOUND";
    public static final String BOX_NOT_FOUND = base + ".BOX_NOT_FOUND";
    
    public static final String BOX_MATCH_SUCC = base + ".BOX_MATCH_SUCC";
    public static final String BOX_MATCH_FAIL = base + ".BOX_MATCH_FAIL";
    public static final String BOX_MISS_MATCH = base + ".BOX_MISS_MATCH";

    public static final String BOX_CHK_PASS = base + ".BOX_CHK_PASS";
    public static final String BOX_CHK_FAIL = base + ".BOX_CHK_FAIL";
    
    public static final String BOX_NO_SIM = base + ".BOX_NO_SIM";
    public static final String BOX_SIM_CHANGED = base + ".BOX_SIM_CHANGED";
    public static final String BOX_SIM_SYNC_FAIL = base + ".BOX_SIM_SYNC_FAIL";
    public static final String BOX_SIM_AUTH_FAIL = base + ".BOX_SIM_AUTH_FAIL";
    public static final String BOX_SIM_INIT_FAIL = base + ".BOX_SIM_INIT_FAIL";
    
    public static final String BOX_CIPHER_KEY_SAVED = base + ".BOX_CIPHER_KEY_SAVED";

    public static final String NR_OPEN_ALERT = base + ".NR_OPEN_ALERT";
    
    public static final String NR_START_REG = base + ".NR_START_REG";
    
    public static final String NR_REG_SUCC = base + ".NR_REG_SUCC";
    public static final String NR_REG_FAIL = base + ".NR_REG_FAIL";
    public static final String NR_REG_EXPIRE = base + ".NR_REG_EXPIRE";
    public static final String NR_W_AUTH_REQ_EXPIRE = base + ".NR_W_AUTH_REQ_EXPIRE";
    
    public static final String NR_CLOSED = base + ".NR_CLOSED";
    public static final String NR_CLOSE_SUCC = base + ".NR_CLOSE_SUCC";

    public static final String CALL_STATE_CHANGED = base + ".CALL_STATE_CHANGED";
    public static final String CALL_SEND_DTMF = base + ".CALL_SEND_DTMF";
    
    public static final String SMS_NOTIFY = base + ".SMS_NOTIFY";
    public static final String SMS_CLEAR_NOTIFY = base + ".SMS_CLEAR_NOTIFY";
    public static final String SMS_RECV = base + ".SMS_RECV";
    
    public static final String NOTIFY_MISSED_CALL = base + ".NOTIFY_MISSED_CALL";
    public static final String CLEAR_MISSED_CALL = base + ".CLEAR_MISSED_CALL";
    public static final String NOTIFY_POP_DLG = base + ".NOTIFY_POP_DLG";
    
    public static final String USER_VERIFY_SUCC = base + ".USER_VERIFY_SUCC";
    public static final String USER_VERIFY_FAIL = base + ".USER_VERIFY_FAIL";
    
    public static final String OP_VSIM = base + ".OP_VSIM";
    
    
    
    public static final String APP_UPDATE = base + ".app_update";
    public static final String TRA_MSG_ITEM = base + ".view.MessageItem";
    public static final String ContactDetailReceiver = base + ".activity.ContactDetailReceiver";
    public static final String MSM_ACTION = base + ".receiver.getMessage";
    public static final String MSG_COUNT_ACTION = base + ".receiver.getMessageCount";
    public static final String MessageGroupDetailsActReceiver = base + ".MessageGroupDetailsActReceiver";
    public static final String MyOrderReceiver = base + ".activity.MyOrderReceiver";
    public static final String ReceptionListActivity = base + ".ReceptionListActivity";
    public static final String setAlarm = base + ".receiver.setAlarm";
    public static final String onShipmentInfoChanged = base + ".onShipmentInfoChanged";
    public static final String TripListActivity = base + ".TripListActivity";
    public static final String BOOLEAN_EXTRA_DOWNLOAD_HEAD = base + "download_head";
    public static final String finish = base + ".activity.finish";
    
    
}
