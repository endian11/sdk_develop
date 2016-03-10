package com.travelrely.core.nrs;

import java.util.HashMap;

import android.text.TextUtils;

public final class IntentMsg
{
    public static final String INTENT_ID = "INT_ARGS";
    public static final String INTENT_LONG = "LONG_ARGS";
    public static final String INTENT_STR_MSG = "STR_ARGS";
    
	public static final int UI_BACK = 0;
	public static final int UI_BACK_LOGIN_IND = 1;
	
    private static HashMap<Integer, String> msg = new HashMap<Integer, String>();
    static
    {
        msg.put(UI_BACK, "UI_BACK");
        msg.put(UI_BACK_LOGIN_IND, "UI_BACK_LOGIN_IND");
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

    // 写号过程中的广播通知
    public static final int WR_SIM = 100;
    public static final int WR_SIM_READ_UL01_FAIL = WR_SIM + 0x01;
    public static final int WR_SIM_READ_UL01_EXPIRE = WR_SIM + 0x02;
    public static final int WR_SIM_READ_UL01_SUCC = WR_SIM + 0x03;
    
    // VSIM写号过程
    public static final int WR_SIM_SAVE_DL01_FAIL = WR_SIM + 0x04;
    public static final int WR_SIM_SAVE_DL01_EXPIRE = WR_SIM + 0x05;
    public static final int WR_SIM_SAVE_DL01_SUCC = WR_SIM + 0x06;
    
    // 注销VSIM过程
    public static final int WR_SIM_SAVE_DL04_FAIL = WR_SIM + 0x07;
    public static final int WR_SIM_SAVE_DL04_EXPIRE = WR_SIM + 0x08;
    public static final int WR_SIM_SAVE_DL04_SUCC = WR_SIM + 0x09;
}
