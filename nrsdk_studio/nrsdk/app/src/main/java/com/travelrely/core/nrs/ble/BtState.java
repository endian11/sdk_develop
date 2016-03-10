package com.travelrely.core.nrs.ble;

import java.util.HashMap;

import android.text.TextUtils;

public class BtState {
	/* main state */
	public static final int MAIN_NULL = 0;
	public static final int MAIN_FIND_BOX = 1;
	public static final int MAIN_MATCH_BOX = 2;
	public static final int MAIN_READ_SIM = 3;
	public static final int MAIN_AUTH_SIM = 4;
	public static final int MAIN_CHK_BOX = 5;
	public static final int MAIN_WRITE_SIM = 6;
	public static final int MAIN_CANCEL_VSIM = 7;

	/* sub state */
	public static final int SUB_NULL = 100;
	public static final int SUB_W_CONNECT_RSP = SUB_NULL + 1;
	public static final int SUB_W_SERVICE_RSP = SUB_NULL + 2;
	public static final int SUB_W_BOX_KEY_RSP = SUB_NULL + 3;
	public static final int SUB_W_BOX_MAC_RSP = SUB_NULL + 4;
	public static final int SUB_W_BOX_SET_KEY_RSP = SUB_NULL + 5;
	public static final int SUB_W_BOX_CHK_KEY_RSP = SUB_NULL + 6;
	public static final int SUB_W_MT100_READY = SUB_NULL + 7;
	public static final int SUB_W_SIM_INFO = SUB_NULL + 8;
	public static final int SUB_W_SIM_AUTH_RSLT = SUB_NULL + 9;
	public static final int SUB_W_BOX_INFO = SUB_NULL + 10;
	public static final int SUB_W_NR_AUTH_IND = SUB_NULL + 11;
	public static final int SUB_W_BOX_SN_RSP = SUB_NULL + 12;
	public static final int SUB_W_COS_VER_RSP = SUB_NULL + 13;
	public static final int SUB_W_BOX_CIPHER_RSP = SUB_NULL + 14;
	public static final int SUB_W_NR_CIPHER_IND = SUB_NULL + 15;

	public static final int SUB_W_SIM_UL01OTA = SUB_NULL + 16;
	public static final int SUB_W_SVR_DL01OTA = SUB_NULL + 17;
	public static final int SUB_W_SIM_DL01OTA_SAVE_RSP = SUB_NULL + 18;
	public static final int SUB_W_VSIM_CANCEL_RSP = SUB_NULL + 19;

	public static final int SUB_W_NR_RAND_IND = SUB_NULL + 20;
	public static final int SUB_W_AES_KEY_RSP = SUB_NULL + 21;

	public static final int SUB_W_BATTERY = SUB_NULL + 22;

	private static HashMap<Integer, String> msg = new HashMap<Integer, String>();
	static {
		msg.put(MAIN_NULL, "MAIN_NULL");
		msg.put(MAIN_FIND_BOX, "MAIN_FIND_BOX");
		msg.put(MAIN_MATCH_BOX, "MAIN_MATCH_BOX");
		msg.put(MAIN_READ_SIM, "MAIN_READ_SIM");
		msg.put(MAIN_AUTH_SIM, "MAIN_AUTH_SIM");
		msg.put(MAIN_CHK_BOX, "MAIN_CHK_BOX");
		msg.put(MAIN_WRITE_SIM, "MAIN_WRITE_SIM");
		msg.put(MAIN_CANCEL_VSIM, "MAIN_CANCEL_VSIM");

		msg.put(SUB_NULL, "SUB_NULL");
		msg.put(SUB_W_CONNECT_RSP, "SUB_W_CONNECT_RSP");
		msg.put(SUB_W_SERVICE_RSP, "SUB_W_SERVICE_RSP");
		msg.put(SUB_W_BOX_KEY_RSP, "SUB_W_BOX_KEY_RSP");
		msg.put(SUB_W_BOX_MAC_RSP, "SUB_W_BOX_MAC_RSP");
		msg.put(SUB_W_BOX_SET_KEY_RSP, "SUB_W_BOX_SET_KEY_RSP");
		msg.put(SUB_W_BOX_CHK_KEY_RSP, "SUB_W_BOX_CHK_KEY_RSP");
		msg.put(SUB_W_MT100_READY, "SUB_W_MT100_READY");
		msg.put(SUB_W_SIM_INFO, "SUB_W_SIM_INFO");
		msg.put(SUB_W_SIM_AUTH_RSLT, "SUB_W_SIM_AUTH_RSLT");
		msg.put(SUB_W_BOX_INFO, "SUB_W_BOX_INFO");
		msg.put(SUB_W_NR_AUTH_IND, "SUB_W_NR_AUTH_IND");
		msg.put(SUB_W_BOX_SN_RSP, "SUB_W_BOX_SN_RSP");
		msg.put(SUB_W_COS_VER_RSP, "SUB_W_COS_VER_RSP");
		msg.put(SUB_W_BOX_CIPHER_RSP, "SUB_W_BOX_CIPHER_RSP");
		msg.put(SUB_W_NR_CIPHER_IND, "SUB_W_NR_CIPHER_IND");

		msg.put(SUB_W_SIM_UL01OTA, "SUB_W_SIM_UL01OTA");
		msg.put(SUB_W_SVR_DL01OTA, "SUB_W_SVR_DL01OTA");
		msg.put(SUB_W_SIM_DL01OTA_SAVE_RSP, "SUB_W_SIM_DL01OTA_SAVE_RSP");
		msg.put(SUB_W_VSIM_CANCEL_RSP, "SUB_W_VSIM_CANCEL_RSP");
		msg.put(SUB_W_NR_RAND_IND, "SUB_W_NR_RAND_IND");
		msg.put(SUB_W_AES_KEY_RSP, "SUB_W_AES_KEY_RSP");

		msg.put(SUB_W_BATTERY, "SUB_W_BATTERY");
	}

	public static String getStateStr(int iState) {
		String result = msg.get(iState);
		if (TextUtils.isEmpty(result)) {
			result = "UNKNOW";
		}
		return result;
	}
}
