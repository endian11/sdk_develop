package sdk.travelrely.lib.device.MT;

/**
 * Created by john on 2016/3/3.
 */

import android.util.Log;

public class BTMessage {

    private static final String TAG = "BTMessage";

    public static byte cmdResetCard[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x01, 0x00, 0x50, 0x00, 0x00, 0x00};

    public static byte cmdReadDeviceSN[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, 0x51, 0x00, 0x01, 0x0A};

    public static byte cmdReadFwVersion[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, 0x51, 0x00, 0x02, 0x04};

    public static byte cmdAuthBox[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0x53, 0x00, 0x00, 0x10,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static byte cmdUpgradeStart[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, 0x57, 0x00, 0x00, 0x0F,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static byte cmdUpgradeData[] = null;

    public static byte cmdUpgradeCheck[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, 0x57, 0x02, 0x00, 0x11,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static byte cmdUpgradeEnable[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, 0x57, 0x03, 0x00, 0x01, 0x01, 0x00};
    //public static byte cmdGetCardInfo[] = {(byte)0xF0,(byte)0xF0,0x75,0x03,0x00,0x55,0x00,0x01,0x09};
    //SELECT_MF

    public static byte cmdSelectMf[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xA4, 0x00, 0x00, 0x02, 0x3F, 0x00};
    //SELECT_DF_GSM
    public static byte cmdSelectDfGsm[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xA4, 0x00, 0x00, 0x02, 0x7F, 0x20};
    //SELECT_DF_TEL
    public static byte cmdSelectDfTel[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xA4, 0x00, 0x00, 0x02, 0x7F, 0x10};
    //SELECT_EF_IMSI
    public static byte cmdSelectEfImsi[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xA4, 0x00, 0x00, 0x02, 0x6F, 0x07};
    //SELECT_EF_LOCI
    public static byte cmdSelectEfLoci[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xA4, 0x00, 0x00, 0x02, 0x6F, 0x7E};
    //SELECT_EF_LOCIGPRS
    public static byte cmdSelectEfLocigprs[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xA4, 0x00, 0x00, 0x02, 0x6F, 0x53};
    //SELECT_EF_SMSP
    public static byte cmdSelectEfSmsp[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xA4, 0x00, 0x00, 0x02, 0x6F, 0x42};
    //SELECT_EF_MSISDN
    public static byte cmdSelectEfMsisdn[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xA4, 0x00, 0x00, 0x02, 0x6F, 0x40};
    //READ_IMSI
    public static byte cmdReadImsi[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, (byte) 0xA0, (byte) 0xB0, 0x00, 0x00, 0x09};
    //READ_LOCI
    public static byte cmdReadLoci[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, (byte) 0xA0, (byte) 0xB0, 0x00, 0x00, 0x0B};
    //READ_LOCIGPRS
    public static byte cmdReadLocigprs[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, (byte) 0xA0, (byte) 0xB0, 0x00, 0x00, 0x0E};
    //READ_RECORD
    public static byte cmdReadRecord[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, (byte) 0xA0, (byte) 0xB2, 0x01, 0x04, 0x00};
    //UPDATE_LOCI
    public static byte cmdUpdateLoci[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xD6, 0x00, 0x00, 0x0B,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    //UPDATE_LOCIGPRS
    public static byte cmdUpdateLocigprs[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0xD6, 0x00, 0x00, 0x0E,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    //RUN_GSM_ALGO
    public static byte cmdRunGsmAlgo[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, (byte) 0xA0, (byte) 0x88, 0x00, 0x00, 0x10,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    //GET_GSM_ALGO
    public static byte cmdGetGsmAlgo[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, (byte) 0xA0, (byte) 0xC0, 0x00, 0x00, 0x0C};
    //GET_RESP
    public static byte cmdGetResp[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, (byte) 0xA0, (byte) 0xC0, 0x00, 0x00, 0x00};

    /********************
     * 3G Command
     **********************/
    public static byte cmdSelectMfUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x00, 0x04, 0x02, 0x3F, 0x00};

    public static byte cmdSelectEfDirUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x00, 0x04, 0x02, 0x2F, 0x00};

    public static byte cmdActivateUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x04, 0x04, 0x10,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static byte cmdSelectUsimUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x00, 0x04, 0x02, (byte) 0x7F, (byte) 0xFF};

    public static byte cmdSelectEfImsiUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x08, 0x04, 0x04, (byte) 0x7F, (byte) 0xFF, 0x6F, 0x07};

    public static byte cmdSelectEfLociUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x08, 0x04, 0x04, (byte) 0x7F, (byte) 0xFF, 0x6F, 0x7E};

    public static byte cmdSelectEfLocigprsUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x08, 0x04, 0x04, (byte) 0x7F, (byte) 0xFF, 0x6F, 0x73};

    public static byte cmdSelectEfSmspUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x08, 0x04, 0x04, 0x7F, (byte) 0xFF, 0x6F, 0x42};

    public static byte cmdSelectEfMsisdnUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xA4, 0x08, 0x04, 0x04, 0x7F, (byte) 0xFF, 0x6F, 0x40};

    public static byte cmdReadRecordUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, (byte) 0xB2, 0x01, 0x04, 0x00};

    public static byte cmdReadImsiUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, (byte) 0xB0, 0x00, 0x00, 0x09};

    public static byte cmdReadLociUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, (byte) 0xB0, 0x00, 0x00, 0x0B};

    public static byte cmdReadLocigprsUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, (byte) 0xB0, 0x00, 0x00, 0x0E};

    public static byte cmdUpdateLociUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xD6, 0x00, 0x00, 0x0B,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static byte cmdUpdateLocigprsUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0xD6, 0x00, 0x00, 0x0E,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static byte cmdAuthenticateUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x02, 0x00, (byte) 0x88, 0x00, (byte) 0x81, 0x22,
            0x10,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x10,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    public static byte cmdGetRespUicc[] = {(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, (byte) 0xC0, 0x00, 0x00, 0x00};

    public static boolean repeatCmd;
    public static byte repeatNum;
    public static int respCounter;
    public static byte[] respBuffer = new byte[256];
    public static int respLen;
    public static int respType;
    public static int cmdLen;

    public static byte lastDatum;
    public static byte essenItemCode;
    public static byte essenItemLen;
    public static byte essenItemIndex;

    public static byte[] initImsi = new byte[8];

    public static boolean upgradeDataEnd;
    public static byte[] md5;
    public static byte[] upgradeBuffer;
    public static int upgradeDataLen;
    public static int upgradeDataPos;
    public static int upgradeDataSeq;

    public static void setRespType(byte[] cmd) {
        switch (cmd[5]) {
            case (byte) 0xA4:
            case (byte) 0xD6:
            case (byte) 0xDC:
            case (byte) 0x88:
            case 0x57:
                respType = 1;
                break;
            case (byte) 0xB0:
            case (byte) 0xB2:
            case (byte) 0xC0:
                respType = 2;
                break;
            case 0x50:
            case 0x51:
            case 0x53:
            case 0x55:
                respType = 3;
                break;
            default:
                respType = 0;
                break;
        }
    }

    public static void setRespLen(byte[] cmd) {
        if (respType == 1) {
            respLen = 3;
        } else if (respType == 2) {
            respLen = cmd[8] + 3;
        } else {
            respLen = 0;
        }
        //Log.i(TAG,"Type:"+Integer.toString(respType)+",set resp len:"+Integer.toString(respLen));
    }

    public static void setCmdLen(byte[] cmd) {
        if ((cmd[3] & 0x02) != 0) {
            if ((cmd[3] & 0x01) != 0)
                cmdLen = 9;
            else
                cmdLen = cmd[8] + 9;
        } else {
            cmdLen = 9;
        }
    }

    public static byte[] appendRespData(byte[] respData) {
        int len = respData.length;
        System.arraycopy(respData, 0, respBuffer, respCounter, respData.length);
        respCounter += len;

        return respBuffer;
    }

    public static boolean parseRespData(byte[] respData) {
        if (respBuffer[0] == (byte) 0x80) {
            if (respLen != 3)
                respLen = 3;
        } else {
            if (respType == 3) {
                if (respCounter < 2) {
                    return false;
                } else {
                    if (respLen == 0)
                        respLen = respBuffer[1] + 4;
                }
            }
            if (respCounter >= 3) {
                byte[] data = new byte[2];
                checkBTMessageError(data);
                if (data[0] != 0x00) {
                    Log.i(TAG, "Found error bt resp msg: " + Integer.toHexString((int) respBuffer[0])
                            + Integer.toHexString((int) respBuffer[1]) + Integer.toHexString((int) respBuffer[2]));
                    respLen = (int) data[1];
                }
            }
        }
        if (respCounter < respLen) {
            Log.i(TAG, "respCounter:" + Integer.toString(respCounter) + "respLen:" + Integer.toString(respLen) + ",msg parse undone");
            return false;
        }
        Log.i(TAG, "respCounter:" + Integer.toString(respCounter) + "respLen:" + Integer.toString(respLen) + ",msg parse done");
        return true;
    }

    public static int checkBTMessageError(byte[] data) {
        byte[] cmd = AppMessage.lCmdRefer.get(AppMessage.msgProgress);
        if (cmd[5] == (byte) 0x88) {
            if ((cmd[4] == 0x00) && (respBuffer[1] != 0x61)) {
                data[0] = (byte) 0x88;
                data[1] = 0x03;

                return 1;
            }
        }

        return 0;
    }

    public static void handleBTMessage(int index, byte[] cmd) {
        byte[] resp = null;
        byte crc = (byte) 0xA5;

        if (AppMessage.appMsg == BTBoxIf.MSG_READ_ESSEN_INFO) {
            if (cmd[5] == (byte) 0xB2) {
                cmd[8] = lastDatum;
            } else if (cmd[5] == (byte) 0x53) {
                ;
            } else if (cmd[5] == (byte) 0xA4) {
                if (cmd[4] == (byte) 0xA0) {
                    if ((cmd[9] == 0x6F) && (cmd[10] == 0x07)) {
                        essenItemCode = BTBoxIf.ESSEN_INFO_IMSI;
                        essenItemLen = BTBoxIf.ESSEN_INFO_IMSI_LEN;
                    } else if ((cmd[9] == 0x6F) && (cmd[10] == 0x42)) {
                        essenItemCode = BTBoxIf.ESSEN_INFO_SMSP;
                        essenItemLen = BTBoxIf.ESSEN_INFO_SMSP_LEN;
                    } else if ((cmd[9] == 0x6F) && (cmd[10] == 0x40)) {
                        essenItemCode = BTBoxIf.ESSEN_INFO_MSISDN;
                        essenItemLen = BTBoxIf.ESSEN_INFO_MSISDN_LEN;
                    }
                } else if (cmd[4] == 0x00) {
                    if (cmd.length >= 13) {
                        if ((cmd[11] == 0x6F) && (cmd[12] == 0x07)) {
                            essenItemCode = BTBoxIf.ESSEN_INFO_IMSI;
                            essenItemLen = BTBoxIf.ESSEN_INFO_IMSI_LEN;
                        } else if ((cmd[11] == 0x6F) && (cmd[12] == 0x42)) {
                            essenItemCode = BTBoxIf.ESSEN_INFO_SMSP;
                            essenItemLen = BTBoxIf.ESSEN_INFO_SMSP_LEN;
                        } else if ((cmd[11] == 0x6F) && (cmd[12] == 0x40)) {
                            essenItemCode = BTBoxIf.ESSEN_INFO_MSISDN;
                            essenItemLen = BTBoxIf.ESSEN_INFO_MSISDN_LEN;
                        }
                    }
                }
            } else if (cmd[5] == (byte) 0x51) {
                if (cmd[7] == 0x01) {
                    essenItemCode = BTBoxIf.ESSEN_INFO_DEVICESN;
                    essenItemLen = BTBoxIf.ESSEN_INFO_DEVICESN_LEN;
                } else if (cmd[7] == 0x02) {
                    essenItemCode = BTBoxIf.ESSEN_INFO_FWVERSION;
                    essenItemLen = BTBoxIf.ESSEN_INFO_FWVERSION_LEN;
                }
            }
        }

        if (cmd[5] == (byte) 0xC0) {
            cmd[8] = lastDatum;
        }
        if (cmd == cmdAuthBox) {
            //2G authenticate param,only 16 bytes random
            for (int i = 0; i < 0x10; i++) {
                cmd[9 + i] = AppMessage.msgParam[3 + i];
            }
        } else if (cmd == cmdRunGsmAlgo) {
            //2G authenticate param,only 16 bytes random
            for (int i = 0; i < 0x10; i++) {
                cmd[9 + i] = AppMessage.msgParam[3 + i];
            }
        } else if (cmd == cmdAuthenticateUicc) {
            int paramLen = AppMessage.msgParam[0] - 1;
            cmdAuthenticateUicc[7] = (paramLen < 0x22) ? (byte) 0x80 : (byte) 0x81;
            cmdAuthenticateUicc[8] = (byte) paramLen;
            for (int i = 0; i < paramLen; i++) {
                cmdAuthenticateUicc[9 + i] = AppMessage.msgParam[2 + i];
            }
        } else {
            ;
        }

		/*if(AppMessage.appMsg == BTBoxIf.MSG_UPGRADE_BOX) {
            if(AppMessage.msgProgress == 0) {
				if(FileManager.fileOpen==false)
					FileManager.openFile();
				VersionUpgrade.initExtractRecord();
				VersionUpgrade.initMD5();
				//Load image start
				upgradeBuffer = new byte[32*1024];
				upgradeDataLen = 0;
				while(true) {
					String record = FileManager.readLine();
					if(record==null) {
						FileManager.closeFile();
						break;
					}
					VersionUpgrade.extractRecord(record);
					if(VersionUpgrade.getRecordType() != 0)
						continue;
					int addr = VersionUpgrade.getRecordAddr();
					if(addr < 0x40000)
						continue;
					byte[] data = VersionUpgrade.getRawData();
					VersionUpgrade.updateMD5(data);
					System.arraycopy(data, 0, upgradeBuffer, upgradeDataLen, data.length);
					upgradeDataLen += data.length;
				}
				md5 = VersionUpgrade.getMD5();
				//Load image end
				System.arraycopy(AppMessage.msgParam, 1, cmd, 9, 14);
				upgradeDataEnd = false;
				upgradeDataPos = 0;
				upgradeDataSeq = 0;
			}
			else if(AppMessage.msgProgress == 1) {
				int left = upgradeDataLen - upgradeDataPos;
				int len = 0;
				if(left > 128) {
					len = 128;
				}
				else if(left <= 128) {
					len = left;
					upgradeDataEnd = true;
				}
				cmd = new byte[9+4+len+1];
				cmd[0] = (byte)0xF0;
				cmd[1] = (byte)0xF0;
				cmd[2] = 0x75;
				cmd[3] = 0x02;
				cmd[4] = 0x00;
				cmd[5] = 0x57;
				cmd[6] = 0x01;
				cmd[7] = (byte)((upgradeDataEnd == true) ? 0x01 : 0x00);
				cmd[8] = (byte)(4+len+1);
				cmd[9] = (byte)(upgradeDataSeq & 0x000000FF);
				cmd[10] = (byte)((upgradeDataSeq >> 8) & 0x000000FF);
				cmd[11] = (byte)((upgradeDataSeq >> 16) & 0x000000FF);
				cmd[12] = (byte)((upgradeDataSeq >> 24) & 0x000000FF);
				System.arraycopy(upgradeBuffer, upgradeDataPos, cmd, 9+4, len);
				//byte[] tmp = new byte[len];
				//System.arraycopy(cmd, 12+4, tmp, 0, len);
				//Log.e(TAG,Common.bytesToHexString(tmp));
				upgradeDataPos += len;
				upgradeDataSeq ++;
			}
			else if(AppMessage.msgProgress == 2) {
				System.arraycopy(BTMessage.md5, 0, cmd, 9, 16);
			}
			else if(AppMessage.msgProgress == 3) {
				;
			}
			int l = (cmd[8] < 0) ? (256+cmd[8]) : cmd[8];
			for(int i=0;i<(l-1);i++)
			{
				crc ^= cmd[9+i];
			}
			cmd[cmd.length-1] = crc;
		}*/


        respCounter = 0;
        //respBuffer = null;
        setCmdLen(cmd);
        setRespType(cmd);
        setRespLen(cmd);

        //writeData(cmd,cmd.length);
        if (BTBoxIf.mCallback != null) {
            Log.i(TAG, "send cmd:" + Common.bytesToHexString(cmd));
            BTBoxIf.mCallback.sendBtMsg(cmd);
        }
        return;
    }

    public static int notifyData(byte[] data, int len) {
        boolean cmdRespEnd = false;
        byte fileNotFound = 0x00;
        byte[] cmd = AppMessage.lCmdRefer.get(AppMessage.msgProgress);

        appendRespData(data);
        cmdRespEnd = parseRespData(data);
        if (cmdRespEnd == false)
            return 1;
        if ((respBuffer[0] == (byte) 0x80) && (respBuffer[1] == 0x00) && (respBuffer[2] == 0x02)) {
            AppMessage.transactTerminate = true;
            AppMessage.TerminateCode = (byte) 0x80;
            AppMessage.postTransact();
            return 2;
        }
        if ((respBuffer[0] == (byte) 0x80) && (respBuffer[1] == 0x00) && (respBuffer[2] == 0x00)) {
            if (false == repeatCmd)
                repeatCmd = true;
            if (++repeatNum < 3) {
                handleBTMessage(AppMessage.msgProgress, cmd);
                return 3;
            } else {
                AppMessage.transactTerminate = true;
                AppMessage.TerminateCode = 0x02;
                AppMessage.postTransact();
                return 4;
            }
        }

        if (AppMessage.appMsg == BTBoxIf.MSG_READ_ESSEN_INFO) {
            if (cmd[5] == (byte) 0xA4) {
                if ((cmd[4] == (byte) 0xA0) && (respBuffer[1] != (byte) 0x9F)) {
                    fileNotFound = 0x01;
                    if ((cmd[9] == 0x6F) && (cmd[10] == 0x42) || (cmd[9] == 0x6F) && (cmd[10] == 0x40)) {
                        int offset = AppMessage.tmpResp[0] + 1;
                        AppMessage.tmpResp[offset++] = essenItemCode;
                        AppMessage.tmpResp[offset++] = 0x00;
                        AppMessage.tmpResp[0] += 2;
                    }
                } else if ((cmd[4] == 0x00) && (respBuffer[1] != 0x61)) {
                    fileNotFound = 0x01;
                    if ((cmd[11] == 0x6F) && (cmd[12] == 0x42) || (cmd[11] == 0x6F) && (cmd[12] == 0x40)) {
                        int offset = AppMessage.tmpResp[0] + 1;
                        AppMessage.tmpResp[offset++] = essenItemCode;
                        AppMessage.tmpResp[offset++] = 0x00;
                        AppMessage.tmpResp[0] += 2;
                    }
                }
                lastDatum = (fileNotFound == 0x01) ? 0x00 : respBuffer[2];
            } else if (cmd[5] == (byte) 0xC0) {
                if (cmd[4] == (byte) 0xA0)
                    lastDatum = respBuffer[15];
                else
                    lastDatum = respBuffer[8];
            } else if (cmd[5] == (byte) 0xB0) {
                int i = 0;
                int offset = AppMessage.tmpResp[0] + 1;
                essenItemIndex = 1;
                AppMessage.tmpResp[offset++] = essenItemCode;
                AppMessage.tmpResp[offset++] = essenItemLen;
                Log.i(TAG, "respBuffer " + Common.bytesToHexString(respBuffer));
                for (i = 0; i < essenItemLen; i++) {
                    AppMessage.tmpResp[offset++] = respBuffer[essenItemIndex + i];
                }
                AppMessage.tmpResp[0] += (2 + essenItemLen);
                //Save card initImsi when register
                if (BTBoxIf.ESSEN_INFO_IMSI == essenItemCode) {
                    System.arraycopy(respBuffer, essenItemIndex + 1, initImsi, 0, 8);
                }
            } else if (cmd[5] == (byte) 0xB2) {
                int i = 0;
                int offset = AppMessage.tmpResp[0] + 1;
                essenItemIndex = (essenItemCode == (byte) BTBoxIf.ESSEN_INFO_SMSP) ? (byte) (lastDatum - 28 + 14) : (byte) (lastDatum - 14 + 1);
                AppMessage.tmpResp[offset++] = essenItemCode;
                AppMessage.tmpResp[offset++] = essenItemLen;
                for (i = 0; i < essenItemLen; i++) {
                    AppMessage.tmpResp[offset++] = respBuffer[essenItemIndex + i];
                }
                AppMessage.tmpResp[0] += (2 + essenItemLen);
            } else if (cmd[5] == 0x51) {
                int i = 0;
                int offset = AppMessage.tmpResp[0] + 1;
                essenItemIndex = 2;
                AppMessage.tmpResp[offset++] = essenItemCode;
                AppMessage.tmpResp[offset++] = essenItemLen;
                for (i = 0; i < essenItemLen; i++) {
                    AppMessage.tmpResp[offset++] = respBuffer[essenItemIndex + i];
                }
                AppMessage.tmpResp[0] += (2 + essenItemLen);
            }
        } else if (AppMessage.appMsg == BTBoxIf.MSG_READ_AUTH_DATA) {
            if (cmd[5] == (byte) 0x88) {
                if (cmd[4] == 0x00) {
                    if ((respBuffer[1] == (byte) 0x98) && (respBuffer[2] == 0x62)) {
                        AppMessage.transactTerminate = true;
                        AppMessage.TerminateCode = 0x40;
                        AppMessage.postTransact();
                        return 6;
                    } else if (respBuffer[1] != 0x61) {
                        AppMessage.transactTerminate = true;
                        AppMessage.TerminateCode = 0x01;
                        AppMessage.postTransact();
                        return 8;
                    }
                } else {
                    if ((respBuffer[1] != (byte) 0x9F) || (respBuffer[2] != 0x0C)) {
                        AppMessage.transactTerminate = true;
                        AppMessage.TerminateCode = 0x01;
                        AppMessage.postTransact();
                        return 8;
                    }
                }
                lastDatum = respBuffer[2];
            } else if (cmd[5] == (byte) 0xC0) {
                if ((cmd[4] == 0x00) && (respBuffer[1] == (byte) 0xDC)) {
                    AppMessage.transactTerminate = true;
                    AppMessage.TerminateCode = 0x02;
                    AppMessage.postTransact();
                    return 10;
                }
            }

            if ((respBuffer[1] == 0x6E) && (respBuffer[2] == 0x00)) {
                if (AppMessage.cmdMode == 1) {
                    AppMessage.transactTerminate = true;
                    AppMessage.TerminateCode = 0x40;
                    AppMessage.postTransact();
                    return 6;
                }
            }
        }
		/*else if(AppMessage.appMsg == BTBoxIf.MSG_UPGRADE_BOX)
		{
			if((AppMessage.msgProgress == 1) && (false == BTMessage.upgradeDataEnd)) {
				;
			}
			else {
				AppMessage.msgProgress ++;
			}
			if(AppMessage.msgProgress == AppMessage.msgCmdNumber) {
				AppMessage.postTransact();
			}
		}*/
        AppMessage.msgProgress = (fileNotFound == 0x01) ? (AppMessage.msgProgress + 3) : (AppMessage.msgProgress + 1);
        if (AppMessage.msgProgress == AppMessage.msgCmdNumber) {
            AppMessage.postTransact();
        } else {
            repeatCmd = false;
            repeatNum = 0;
            cmd = AppMessage.lCmdRefer.get(AppMessage.msgProgress);
			/*try{
				Thread.sleep(10);
			}catch(Exception e){e.printStackTrace();}*/
            handleBTMessage(AppMessage.msgProgress, cmd);
        }
        return 0;
    }
}


