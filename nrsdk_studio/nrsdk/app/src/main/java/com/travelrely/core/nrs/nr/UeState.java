package com.travelrely.core.nrs.nr;

import java.util.HashMap;

import android.text.TextUtils;

public class UeState
{
	// UE状态
	public static final int	MAIN_NULL							= 0;	// 未注册
	public static final int	MAIN_REGISTED						= 1;	// 注册成功
	public static final int	MAIN_CALLING_CONNECTED				= 2;	// 通话中
	public static final int	MAIN_CALLED_CONNECTED				= 3;	// 通话中

	public static final int	MAIN_REGISTING						= 5;
	public static final int	MAIN_CALLING						= 6;
	public static final int	MAIN_CALLED							= 7;
	public static final int	MAIN_SEND_SMS						= 8;
	public static final int	MAIN_RECV_SMS						= 9;
	public static final int	MAIN_LAU							= 10;
	public static final int MAIN_KEEPALIVE                      = 11;
	
	public static final int MAIN_VOIP_CALLING                   = 50;
	public static final int MAIN_VOIP_CALLED                    = 51;
	public static final int MAIN_VOIP_CALLING_CONNECTED         = 52;
	public static final int MAIN_VOIP_CALLED_CONNECTED          = 53;
	
	public static final int MAIN_TEST                           = 54;
	
	public static final int MAIN_VERIFY                           = 55;

	/* wait msg */
	public static final int	SUB_NULL							= 0;

	/* UE_MAIN_CONNECTED sub state */
	public static final int	SUB_CONNECT_W_APP_RECV_SMS_RSP		= 21;

	/* UE_MAIN_REGISTING sub state */
	public static final int	SUB_REGISTING_W_FMT_AUTH_REQ		= 51;
	public static final int	SUB_REGISTING_W_SIM_AUTH_RSP		= 52;
	public static final int	SUB_REGISTING_W_FMT_REG_RSP			= 53;

	/* UE_MAIN_CALLING sub state */
	public static final int	SUB_CALLING_W_FMT_AUTH_REQ			= 61;
	public static final int	SUB_CALLING_W_SIM_AUTH_RSP			= 62;
	public static final int	SUB_CALLING_W_FMT_ALERT				= 63;
	public static final int	SUB_CALLING_W_FMT_CALL_RSP			= 64;

	/* UE_MAIN_CALLED sub state */
	public static final int	SUB_CALLED_W_FMT_AUTH_REQ			= 73;
	public static final int	SUB_CALLED_W_SIM_AUTH_RSP			= 74;
	public static final int	SUB_CALLED_W_FMT_CALL_REQ			= 75;
	public static final int	SUB_CALLED_W_UI_CALL_RSP			= 76;

	/* UE_MAIN_SEND_SMS sub state */
	public static final int	SUB_SEND_SMS_W_FMT_AUTH_REQ			= 81;
	public static final int	SUB_SEND_SMS_W_SIM_AUTH_RSP			= 82;
	public static final int	SUB_SEND_SMS_W_FMT_SEND_RSP			= 83;

	/* UE_MAIN_RECV_SMS sub state */
	public static final int	SUB_RECV_SMS_W_FMT_AUTH_REQ			= 92;
	public static final int	SUB_RECV_SMS_W_SIM_AUTH_RSP			= 93;
	public static final int	SUB_RECV_SMS_W_FMT_RECV_REQ			= 94;

	/* UE_MAIN_LAU sub state */
	public static final int	SUB_LAU_W_FMT_AUTH_REQ				= 101;
	public static final int	SUB_LAU_W_SIM_AUTH_RSP				= 102;
	public static final int	SUB_LAU_W_FMT_RSP					= 103;
	
	public static final int SUB_VOIP_CALLING_W_FMT_CALL_RSP     = 501;
	
	public static final int SUB_VERIFY_W_RSP     = 550;
	public static final int SUB_VERIFY_W_PAGING     = 551;

    private static HashMap<Integer, String> msg = new HashMap<Integer, String>();
    static
    {
        msg.put(MAIN_NULL, "MAIN_NULL");
        msg.put(MAIN_REGISTED, "MAIN_REGISTED");
        msg.put(MAIN_CALLING_CONNECTED, "MAIN_CALLING_CONNECTED");
        msg.put(MAIN_CALLED_CONNECTED, "MAIN_CALLED_CONNECTED");
        msg.put(MAIN_REGISTING, "MAIN_REGISTING");
        msg.put(MAIN_CALLING, "MAIN_CALLING");
        msg.put(MAIN_CALLED, "MAIN_CALLED");
        msg.put(MAIN_SEND_SMS, "MAIN_SEND_SMS");
        msg.put(MAIN_RECV_SMS, "MAIN_RECV_SMS");
        msg.put(MAIN_LAU, "MAIN_LAU");
        msg.put(MAIN_KEEPALIVE, "MAIN_KEEPALIVE");
        
        msg.put(MAIN_VOIP_CALLING, "MAIN_VOIP_CALLING");
        msg.put(MAIN_VOIP_CALLED, "MAIN_VOIP_CALLED");
        msg.put(MAIN_VOIP_CALLING_CONNECTED, "MAIN_VOIP_CALLING_CONNECTED");
        msg.put(MAIN_VOIP_CALLED_CONNECTED, "MAIN_VOIP_CALLED_CONNECTED");
        
        msg.put(MAIN_TEST, "MAIN_TEST");
        msg.put(MAIN_VERIFY, "MAIN_VERIFY");
        
        msg.put(SUB_NULL, "SUB_NULL");
        msg.put(SUB_CONNECT_W_APP_RECV_SMS_RSP, "SUB_CONNECT_W_APP_RECV_SMS_RSP");
        
        msg.put(SUB_REGISTING_W_FMT_AUTH_REQ, "SUB_REGISTING_W_FMT_AUTH_REQ");
        msg.put(SUB_REGISTING_W_SIM_AUTH_RSP, "SUB_REGISTING_W_SIM_AUTH_RSP");
        msg.put(SUB_REGISTING_W_FMT_REG_RSP, "SUB_REGISTING_W_FMT_REG_RSP");
        
        msg.put(SUB_CALLING_W_FMT_AUTH_REQ, "SUB_CALLING_W_FMT_AUTH_REQ");
        msg.put(SUB_CALLING_W_SIM_AUTH_RSP, "SUB_CALLING_W_SIM_AUTH_RSP");
        msg.put(SUB_CALLING_W_FMT_ALERT, "SUB_CALLING_W_FMT_ALERT");
        msg.put(SUB_CALLING_W_FMT_CALL_RSP, "SUB_CALLING_W_FMT_CALL_RSP");
        
        msg.put(SUB_CALLED_W_FMT_AUTH_REQ, "SUB_CALLED_W_FMT_AUTH_REQ");
        msg.put(SUB_CALLED_W_SIM_AUTH_RSP, "SUB_CALLED_W_SIM_AUTH_RSP");
        msg.put(SUB_CALLED_W_FMT_CALL_REQ, "SUB_CALLED_W_FMT_CALL_REQ");
        msg.put(SUB_CALLED_W_UI_CALL_RSP, "SUB_CALLED_W_UI_CALL_RSP");
        
        msg.put(SUB_SEND_SMS_W_FMT_AUTH_REQ, "SUB_SEND_SMS_W_FMT_AUTH_REQ");
        msg.put(SUB_SEND_SMS_W_SIM_AUTH_RSP, "SUB_SEND_SMS_W_SIM_AUTH_RSP");
        msg.put(SUB_SEND_SMS_W_FMT_SEND_RSP, "SUB_SEND_SMS_W_FMT_SEND_RSP");
        
        msg.put(SUB_RECV_SMS_W_FMT_AUTH_REQ, "SUB_RECV_SMS_W_FMT_AUTH_REQ");
        msg.put(SUB_RECV_SMS_W_SIM_AUTH_RSP, "SUB_RECV_SMS_W_SIM_AUTH_RSP");
        msg.put(SUB_RECV_SMS_W_FMT_RECV_REQ, "SUB_RECV_SMS_W_FMT_RECV_REQ");
        
        msg.put(SUB_LAU_W_FMT_AUTH_REQ, "SUB_LAU_W_FMT_AUTH_REQ");
        msg.put(SUB_LAU_W_SIM_AUTH_RSP, "SUB_LAU_W_SIM_AUTH_RSP");
        msg.put(SUB_LAU_W_FMT_RSP, "SUB_LAU_W_FMT_RSP");
        
        msg.put(SUB_VOIP_CALLING_W_FMT_CALL_RSP, "SUB_VOIP_CALLING_W_FMT_CALL_RSP");
    
        msg.put(SUB_VERIFY_W_RSP, "SUB_VERIFY_W_RSP");
        msg.put(SUB_VERIFY_W_PAGING, "SUB_VERIFY_W_PAGING");
    }

    public static String getMain(int iUeMainState)
    {
        String result = msg.get(iUeMainState);
        if (TextUtils.isEmpty(result))
        {
            result = "UNKNOW";
        }
        return result;
    }

	public static String getSub(int iUeSubState)
	{
        String result = msg.get(iUeSubState);
        if (TextUtils.isEmpty(result))
        {
            result = "UNKNOW";
        }
        return result;
    }
}
