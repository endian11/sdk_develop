package com.travelrely.core.nrs.nr.mt100;

import android.util.Log;



public class BTBoxIf {
	private static final String TAG = "BTBoxIf";

	public final static byte AUTH_TYPE_GSM = 0x00;
	public final static byte AUTH_TYPE_UMTS = 0x01;

	public final static byte ESSEN_INFO_ICCID = 0x00;
	public final static byte ESSEN_INFO_IMSI = 0x01;
	public final static byte ESSEN_INFO_SST = 0x02;
	public final static byte ESSEN_INFO_PLMNSEL = 0x03;
	public final static byte ESSEN_INFO_FPLMN = 0x04;
	public final static byte ESSEN_INFO_FDN = 0x05;
	public final static byte ESSEN_INFO_BDN = 0x06;
	public final static byte ESSEN_INFO_LOCI = 0x07;
	public final static byte ESSEN_INFO_LOCIGPRS = 0x08;
	public final static byte ESSEN_INFO_SMSP = 0x09;
	public final static byte ESSEN_INFO_MSISDN = 0x0A;
	public final static byte ESSEN_INFO_FWVERSION = (byte)0xFE;
	public final static byte ESSEN_INFO_DEVICESN = (byte)0xFF;

	public final static byte ESSEN_INFO_ICCID_LEN = 0x0A;
	public final static byte ESSEN_INFO_IMSI_LEN = 0x09;
	public final static byte ESSEN_INFO_SST_LEN = 0x00;
	public final static byte ESSEN_INFO_PLMNSEL_LEN = 0x00;
	public final static byte ESSEN_INFO_FPLMN_LEN = 0x00;
	public final static byte ESSEN_INFO_FDN_LEN = 0x00;
	public final static byte ESSEN_INFO_BDN_LEN = 0x00;
	public final static byte ESSEN_INFO_LOCI_LEN = 0x0B;
	public final static byte ESSEN_INFO_LOCIGPRS_LEN = 0x0E;
	public final static byte ESSEN_INFO_SMSP_LEN = 0x09;
	public final static byte ESSEN_INFO_MSISDN_LEN = 0x09;
	public final static byte ESSEN_INFO_FWVERSION_LEN = 0x04;
	public final static byte ESSEN_INFO_DEVICESN_LEN = 0x0A;

	public final static byte MSG_ERROR = 0x01;
	public final static byte MSG_CONNECT_STATUS = 0x02;
	public final static byte MSG_READ_ESSEN_INFO = 0x03;
	public final static byte MSG_UPDATE_ESSEN_INFO = 0x04;
	public final static byte MSG_READ_AUTH_DATA = 0x05;
	public final static byte MSG_INIT_CARD_COMPLETE = 0x06;
	public final static byte MSG_AUTH_BOX = 0x07;
	public final static byte MSG_UPGRADE_BOX = 0x08;
	public final static byte MSG_CLEAR_SQN = (byte)0xFC;
	public final static byte MSG_ACTIVATE_USIM = (byte)0xFD;
	public final static byte MSG_TEST_CARD = (byte)0xFE;
	public final static byte MSG_RESET_CARD = (byte)0xFF;

	public final static byte MAX_MSG_FIELD_NUM = 0x08;
	public final static byte MAX_MSG_FIELD_LEN = 0x20;

	public static int intfOpt;
	public static Mt100Callback mCallback;
	
	public static int initBTBoxIntf(int opt, Mt100Callback callback) {

		intfOpt = opt;
		mCallback = callback;
		AppMessage.initAppMessage();
		testCard(null);
		return 0;

	}

	public static void uninitBTBoxIntf() {
		AppMessage.uninitAppMessage();
	}

	public static void resetCard(byte[] param) {
		AppMessage.appMsg = MSG_RESET_CARD;
		AppMessage.msgParam = param;
		AppMessage.resetCardTransact();
	}
	
	public static void authBox(byte[] param) {
		Log.i(TAG,"Auth box");
		AppMessage.appMsg = MSG_AUTH_BOX;
		AppMessage.msgParam = param;
		AppMessage.authBoxTransact();
	}

	public static void testCard(byte[] param) {
		Log.i(TAG,"Test card type......");
		AppMessage.appMsg = MSG_TEST_CARD;
		AppMessage.msgParam = param;
		AppMessage.testCardTransact();
	}

	public static void readEssenInfo(byte[] param) {
		Log.i(TAG,"Read essential information......");
		AppMessage.appMsg = MSG_READ_ESSEN_INFO;
		AppMessage.msgParam = param;
		AppMessage.readEssenInfoTransact();
	}

	public static void updateEssenInfo(byte[] param) {
		AppMessage.appMsg = MSG_UPDATE_ESSEN_INFO;
		AppMessage.msgParam = param;
		AppMessage.updateEssenInfoTransact();
	}

	public static void readAuthData(byte[] param) {
		Log.i(TAG,"Read auth data");
		AppMessage.appMsg = MSG_READ_AUTH_DATA;
		AppMessage.msgParam = param;
		AppMessage.readAuthDataTransact();
	}
	
	public static void clearSqn(byte[] param) {
		AppMessage.appMsg = MSG_CLEAR_SQN;
		AppMessage.msgParam = param;
		AppMessage.clearSqnTransact();
	}
	
	public static void upgradeBox(byte[] param) {
		AppMessage.appMsg = MSG_UPGRADE_BOX;
		AppMessage.msgParam = param;
		AppMessage.upgradeBoxTransact();
	}
}


