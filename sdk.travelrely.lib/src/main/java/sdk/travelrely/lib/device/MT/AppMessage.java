package sdk.travelrely.lib.device.MT;

/**
 * Created by john on 2016/3/3.
 */
/**
 *
 */

import java.util.List;

import java.util.Vector;

import android.util.Log;


public class AppMessage {

    private static final String TAG = "AppMessage";

    //2G or 3G instruction
    public static int cmdMode;

    //Currently handled instruction set
    public static List<byte[]> lCmdRefer;

    public static List<byte[]> lCmdResetCard = new Vector<byte[]>();
    public static List<byte[]> lCmdTestCard = new Vector<byte[]>();
    public static List<byte[]> lCmdAuthBox = new Vector<byte[]>();
    public static List<byte[]> lCmdUpgradeBox = new Vector<byte[]>();

    public static List<byte[]> lCmdReadEssen = new Vector<byte[]>();
    public static List<byte[]> lCmdUpdateEssen = new Vector<byte[]>();
    public static List<byte[]> lCmdReadAuth = new Vector<byte[]>();

    public static List<byte[]> lCmdReadEssenUicc = new Vector<byte[]>();
    public static List<byte[]> lCmdUpdateEssenUicc = new Vector<byte[]>();
    public static List<byte[]> lCmdReadAuthUicc = new Vector<byte[]>();
    //Used to test
    public static List<byte[]> lCmdClearSqnUicc = new Vector<byte[]>();

    public static int initAppMessage() {

        lCmdResetCard.add(0,BTMessage.cmdResetCard);

        lCmdTestCard.add(0,BTMessage.cmdSelectMfUicc);

        lCmdAuthBox.add(0,BTMessage.cmdAuthBox);

        lCmdUpgradeBox.add(0,BTMessage.cmdUpgradeStart);
        lCmdUpgradeBox.add(1,BTMessage.cmdUpgradeData);
        //lCmdUpgradeBox.add(2,BTMessage.cmdUpgradeCheck);
        //lCmdUpgradeBox.add(3,BTMessage.cmdUpgradeEnable);

        lCmdReadEssen.add(0,BTMessage.cmdSelectMf);
        lCmdReadEssen.add(1,BTMessage.cmdSelectDfGsm);
        lCmdReadEssen.add(2,BTMessage.cmdSelectEfImsi);
        lCmdReadEssen.add(3,BTMessage.cmdReadImsi);
        lCmdReadEssen.add(4,BTMessage.cmdSelectMf);
        lCmdReadEssen.add(5,BTMessage.cmdSelectDfTel);
        lCmdReadEssen.add(6,BTMessage.cmdSelectEfSmsp);
        lCmdReadEssen.add(7,BTMessage.cmdGetResp);
        lCmdReadEssen.add(8,BTMessage.cmdReadRecord);
        lCmdReadEssen.add(9,BTMessage.cmdSelectMf);
        lCmdReadEssen.add(10,BTMessage.cmdSelectDfTel);
        lCmdReadEssen.add(11,BTMessage.cmdSelectEfMsisdn);
        lCmdReadEssen.add(12,BTMessage.cmdGetResp);
        lCmdReadEssen.add(13,BTMessage.cmdReadRecord);
        lCmdReadEssen.add(14,BTMessage.cmdReadDeviceSN);
        lCmdReadEssen.add(15,BTMessage.cmdReadFwVersion);

        lCmdUpdateEssen.add(0,BTMessage.cmdSelectMf);
        lCmdUpdateEssen.add(1,BTMessage.cmdSelectDfGsm);
        lCmdUpdateEssen.add(2,BTMessage.cmdSelectEfLoci);
        lCmdUpdateEssen.add(3,BTMessage.cmdUpdateLoci);

        //lCmdReadAuth.add(0,BTMessage.cmdGetCardInfo);
        lCmdReadAuth.add(0,BTMessage.cmdSelectMf);
        lCmdReadAuth.add(1,BTMessage.cmdSelectDfGsm);
        lCmdReadAuth.add(2,BTMessage.cmdRunGsmAlgo);
        lCmdReadAuth.add(3,BTMessage.cmdGetGsmAlgo);

        lCmdReadEssenUicc.add(0, BTMessage.cmdSelectMfUicc);
        lCmdReadEssenUicc.add(1, BTMessage.cmdSelectEfImsiUicc);
        lCmdReadEssenUicc.add(2, BTMessage.cmdReadImsiUicc);
        lCmdReadEssenUicc.add(3, BTMessage.cmdSelectMfUicc);
        lCmdReadEssenUicc.add(4, BTMessage.cmdSelectEfSmspUicc);
        lCmdReadEssenUicc.add(5, BTMessage.cmdGetRespUicc);
        lCmdReadEssenUicc.add(6, BTMessage.cmdReadRecordUicc);
        lCmdReadEssenUicc.add(7, BTMessage.cmdSelectMfUicc);
        lCmdReadEssenUicc.add(8, BTMessage.cmdSelectEfMsisdnUicc);
        lCmdReadEssenUicc.add(9, BTMessage.cmdGetRespUicc);
        lCmdReadEssenUicc.add(10, BTMessage.cmdReadRecordUicc);
        lCmdReadEssenUicc.add(11,BTMessage.cmdReadDeviceSN);
        lCmdReadEssenUicc.add(12,BTMessage.cmdReadFwVersion);

        lCmdUpdateEssenUicc.add(0, BTMessage.cmdSelectMfUicc);
        lCmdUpdateEssenUicc.add(1, BTMessage.cmdSelectEfLociUicc);
        lCmdUpdateEssenUicc.add(2, BTMessage.cmdUpdateLociUicc);

        //lCmdReadAuthUicc.add(0,BTMessage.cmdGetCardInfo);
        lCmdReadAuthUicc.add(0, BTMessage.cmdSelectMfUicc);
        lCmdReadAuthUicc.add(1, BTMessage.cmdSelectUsimUicc);
        lCmdReadAuthUicc.add(2, BTMessage.cmdAuthenticateUicc);
        lCmdReadAuthUicc.add(3, BTMessage.cmdGetRespUicc);

        //Used to test
		/*byte cmdSelectSqnUicc[] = {(byte)0xF0,(byte)0xF0,0x75,0x02,0x00,(byte)0xA4,0x00,0x04,0x02,0x6F,0x1D};
		byte cmdClearSqnUicc[] = {(byte)0xF0,(byte)0xF0,0x75,0x02,0x00,(byte)0xDC,0x01,0x04,0x06,0x00,0x00,0x00,0x00,0x00,0x00};
		byte[] tmp = null;
		lCmdClearSqnUicc.add(0,BTMessage.cmdSelectMfUicc);
		lCmdClearSqnUicc.add(1,BTMessage.cmdSelectUsimUicc);
		tmp = new byte[cmdSelectSqnUicc.length];
		System.arraycopy(cmdSelectSqnUicc, 0, tmp, 0, tmp.length);
		lCmdClearSqnUicc.add(2,tmp);
		for(byte i=0;i<0x21;i++) {
			tmp = new byte[cmdClearSqnUicc.length];
			System.arraycopy(cmdClearSqnUicc, 0, tmp, 0, tmp.length);
			tmp[3] = (byte)(cmdClearSqnUicc[6] + i);
			lCmdClearSqnUicc.add(i+3,tmp);
		}*/

        return 0;
    }

    public static void uninitAppMessage() {
        lCmdResetCard.clear();
        lCmdTestCard.clear();
        lCmdAuthBox.clear();
        lCmdUpgradeBox.clear();
        lCmdReadEssen.clear();
        lCmdUpdateEssen.clear();
        lCmdReadAuth.clear();
        lCmdReadEssenUicc.clear();
        lCmdUpdateEssenUicc.clear();
        lCmdReadAuthUicc.clear();
        //lCmdClearSqnUicc.clear();

        return;
    }

    public static byte convertBcd(byte[] src,byte sl,byte[] dst) {
        byte a,b;
        byte ret = 0;
        int i = 0;
        int tmp = 0;

        for(i=0;i<sl;i++)
        {
            if(src[i]==(byte)0xFF)
                break;
        }
        sl = (byte)i;
        //Log.i(TAG,"src "+Common.bytesToHexString(src));
        //Log.i(TAG,"sl "+Integer.toString(sl));
        //688116705648F0
        for(i=0;i<(sl-1);i++)
        {
            tmp = (src[i] < 0) ? (256+src[i]) : src[i];
            //Log.i(TAG,"tmp "+tmp);
            a = (byte)((tmp % 16) + 0x30);
            b = (byte)((tmp / 16) + 0x30);
            //Log.i(TAG,"Express "+((tmp % 16) + 0x30)+" "+(tmp / 16) + 0x30);
            //Log.i(TAG,"Digit "+a+" "+b);
            dst[2*i] = a;
            dst[2*i+1] = b;
        }
        tmp = (src[sl-1] < 0) ? (256+src[sl-1]) : src[sl-1];
        if((tmp & 0xF0) == 0xF0)
        {
            dst[2*i] = (byte)((tmp % 16) + 0x30);
            ret = (byte)(2*i+1);
        }
        else
        {
            dst[2*i] = (byte)((tmp % 16) + 0x30);
            dst[2*i+1] = (byte)((tmp / 16) + 0x30);
            ret = (byte)(2*i+2);
        }

        return ret;
    }

    public static byte[] encapResp(byte msg, byte[] data) {
        int len = data.length;
        byte[] res = null;

        if(true == transactTerminate)
        {
			/*switch(AppMessage.TerminateCode)
			{
			case 0x01:
			case 0x02:
				res = new byte[0x04];
				res[0] = 0x03;
				res[1] = BTBoxIf.MSG_ERROR;
				res[2] = 0x01;
				res[3] = AppMessage.TerminateCode;
				return res;
			default:
				break;
			}*/
            switch(msg)
            {
                case BTBoxIf.MSG_READ_AUTH_DATA:
                    if(0x00 == msgParam[1]) {
                        res = new byte[0x08];
                        res[0] = 0x07;
                        res[1] = BTBoxIf.MSG_READ_AUTH_DATA;
                        res[2] = msgParam[1];
                        res[3] = AppMessage.TerminateCode;
                    }
                    else {
                        res = new byte[0x0C];
                        res[0] = 0x0B;
                        res[1] = BTBoxIf.MSG_READ_AUTH_DATA;
                        res[2] = msgParam[1];
                        res[3] = AppMessage.TerminateCode;
                    }
                    break;
                case BTBoxIf.MSG_INIT_CARD_COMPLETE:
                    Log.i(TAG,"Init card fail");
                    res = new byte[3];
                    res[0] = 0x02;
                    res[1] = BTBoxIf.MSG_INIT_CARD_COMPLETE;
                    res[2] = AppMessage.TerminateCode;
                    break;
                default:
                    break;
            }
            return res;
        }

        if(msg == BTBoxIf.MSG_READ_ESSEN_INFO) {
            byte smsp[] = new byte[32];
            byte msisdn[] = new byte[32];
            byte number[] = new byte[16];
            byte numberLen = 0;
            int index = 0;
            Log.i(TAG,"tmpResp "+Common.bytesToHexString(tmpResp));
            index = 2+2+BTBoxIf.ESSEN_INFO_IMSI_LEN;
            System.arraycopy(tmpResp, index, smsp, 0, 2+BTBoxIf.ESSEN_INFO_SMSP_LEN);
            index += (BTBoxIf.ESSEN_INFO_SMSP_LEN + 2);
            System.arraycopy(tmpResp, index, msisdn, 0, 2+BTBoxIf.ESSEN_INFO_MSISDN_LEN);
            tmpResp[0] = 12;
            int offset = 2+2+BTBoxIf.ESSEN_INFO_IMSI_LEN;

            byte[] nm = null;
            if(smsp[2]!=(byte)0xFF)
            {
                nm = new byte[smsp.length-4];
                System.arraycopy(smsp,4,nm,0,smsp.length-4);
                //numberLen = convertBcd(nm,(byte)(smsp[1]-2),number);
                numberLen = convertBcd(nm,(byte)(smsp[2]-1),number);
                Log.i(TAG,"number "+numberLen+" "+Common.bytesToHexString(number));
                tmpResp[offset++] = BTBoxIf.ESSEN_INFO_SMSP;
                tmpResp[offset++] = numberLen;
                System.arraycopy(number,0,tmpResp,offset,numberLen);
                offset += numberLen;
                tmpResp[0] += (byte)(2+numberLen);
            }
            else
            {
                tmpResp[offset++] = BTBoxIf.ESSEN_INFO_SMSP;
                tmpResp[offset++] = 0x00;
                tmpResp[0] += (byte)2;
            }

            if(msisdn[2] != (byte)0xFF)
            {
                nm = new byte[msisdn.length-4];
                System.arraycopy(msisdn,4,nm,0,msisdn.length-4);
                //numberLen = convertBcd(nm,(byte)(msisdn[1]-2),number);
                numberLen = convertBcd(nm,(byte)(msisdn[2]-1),number);
                Log.i(TAG,"number "+numberLen+" "+Common.bytesToHexString(number));
                tmpResp[offset++] = BTBoxIf.ESSEN_INFO_MSISDN;
                tmpResp[offset++] = numberLen;
                System.arraycopy(number,0,tmpResp,offset,numberLen);
                offset += numberLen;
                tmpResp[0] += (byte)(2+numberLen);
            }
            else
            {
                tmpResp[offset++] = BTBoxIf.ESSEN_INFO_MSISDN;
                tmpResp[offset++] = 0x00;
                tmpResp[0] += (byte)2;
            }

            res = new byte[tmpResp[0]+1];
            System.arraycopy(tmpResp, 0, res, 0, tmpResp[0]+1);
            Log.i(TAG,"Essen info msg "+Common.bytesToHexString(res));
        }
        else if(msg == BTBoxIf.MSG_UPDATE_ESSEN_INFO) {
            res = new byte[3];
            res[0] = 0x02;
            res[1] = BTBoxIf.MSG_UPDATE_ESSEN_INFO;
            if((data[1]==0x90) && (data[2]==0x00))
                res[2] = 0x00;
            else
                res[2] = 0x01;
        }
        else if(msg == BTBoxIf.MSG_READ_AUTH_DATA) {
            if(msgParam[1] == 0x00) {
                res = new byte[0x08];
                res[0] = 0x07;
                res[1] = BTBoxIf.MSG_READ_AUTH_DATA;
                res[2] = 0x00;
                res[3] = 0x00;
                if(cmdMode == 0x00) {
                    for(int i=0;i<0x04;i++) {
                        res[4+i] = data[i+1];
                    }
                }
                else {
                    for(int i=0;i<0x04;i++) {
                        res[4+i] = data[i+2];
                    }
                }
            }
            else {
                res = new byte[0x0C];
                res[0] = 0x0B;
                res[1] = BTBoxIf.MSG_READ_AUTH_DATA;
                res[2] = 0x01;
                res[3] = 0x00;
                for(int i=0;i<0x08;i++) {
                    res[4+i] = data[i+3];
                }
            }
        }
        else if(msg == BTBoxIf.MSG_AUTH_BOX) {
            res = new byte[0x10];
            int i;
            res[0] = 0x0F;
            res[1] = BTBoxIf.MSG_AUTH_BOX;
            res[2] = 0x01;
            res[3] = 0x00;
            for(i=0;i<0x0C;i++) {
                res[4+i] = data[i+1];
            }
        }
        else if(msg == BTBoxIf.MSG_UPGRADE_BOX) {
            res = new byte[0x04];
            int i;
            res[0] = 0x03;
            res[1] = BTBoxIf.MSG_UPGRADE_BOX;
            res[2] = 0x00;
            res[3] = 0x00;
        }
        else if(msg == BTBoxIf.MSG_CLEAR_SQN) {
            res = new byte[3];
            res[0] = 0x02;
            res[1] = BTBoxIf.MSG_CLEAR_SQN;
            if((data[1]==0x90) && (data[2]==0x00))
                res[2] = 0x00;
            else
                res[2] = 0x01;
        }
        else if(msg == BTBoxIf.MSG_INIT_CARD_COMPLETE) {
            Log.i(TAG,"Init card ok");
            res = new byte[3];
            res[0] = 0x02;
            res[1] = BTBoxIf.MSG_INIT_CARD_COMPLETE;
            res[2] = 0;
        }

        return res;
    }

    //Synchronize between BTBoxIf and AppMessage
    public static Common.SynchToken token = new Common.SynchToken();

    public static int appMsg;
    public static byte[] msgParam;
    public static int msgProgress;
    public static int msgCmdNumber;
    public static byte[] msgResp;
    public static byte[] tmpResp = new byte[256];
    public static boolean transactTerminate;
    public static byte TerminateCode;//1-No card,2-Timeout,3-Card changed


    public static int execTransact() {
        msgProgress = 0;
        msgCmdNumber = lCmdRefer.size();
        BTMessage.repeatCmd = false;
        BTMessage.repeatNum = 0;
        byte[] cmd = lCmdRefer.get(msgProgress);
        BTMessage.handleBTMessage(msgProgress, cmd);

        return 0;
    }

    public static void preTransact() {
        transactTerminate = false;
        switch(AppMessage.appMsg) {
            case BTBoxIf.MSG_AUTH_BOX:
                lCmdRefer = lCmdAuthBox;
                break;
            case BTBoxIf.MSG_UPGRADE_BOX:
                lCmdRefer = lCmdUpgradeBox;
                break;
            case BTBoxIf.MSG_READ_ESSEN_INFO:
                lCmdRefer = (cmdMode == 0) ? lCmdReadEssen : lCmdReadEssenUicc;
                tmpResp[0] = 1;
                tmpResp[1] = BTBoxIf.MSG_READ_ESSEN_INFO;
                Log.i(TAG,"tmpResp[0] tmpResp[1] "+tmpResp[0]+" "+tmpResp[1]);
                break;
            case BTBoxIf.MSG_UPDATE_ESSEN_INFO:
                lCmdRefer = (cmdMode == 0) ? lCmdUpdateEssen : lCmdUpdateEssenUicc;
                break;
            case BTBoxIf.MSG_READ_AUTH_DATA:
                lCmdRefer = (cmdMode == 0) ? lCmdReadAuth : lCmdReadAuthUicc;
                break;
            case BTBoxIf.MSG_CLEAR_SQN:
                //lCmdRefer = lCmdClearSqnUicc;
                break;
            case BTBoxIf.MSG_TEST_CARD:
                lCmdRefer = lCmdTestCard;
                break;
            case BTBoxIf.MSG_RESET_CARD:
                lCmdRefer = lCmdResetCard;
                break;
            default:
                return;
        }
    }

    public static void postTransact() {
        if(appMsg == BTBoxIf.MSG_TEST_CARD) {
            if(BTMessage.respBuffer[1] == 0x61)
                cmdMode = 1;
            else
                cmdMode = 0;
        }

        if(appMsg == BTBoxIf.MSG_TEST_CARD) {
            appMsg = BTBoxIf.MSG_INIT_CARD_COMPLETE;
        }

        byte[] result = encapResp((byte)appMsg, BTMessage.respBuffer);

        switch(appMsg) {
            case BTBoxIf.MSG_INIT_CARD_COMPLETE:
            case BTBoxIf.MSG_AUTH_BOX:
            case BTBoxIf.MSG_READ_ESSEN_INFO:
            case BTBoxIf.MSG_READ_AUTH_DATA:
                //case BTBoxIf.MSG_CLEAR_SQN:
                break;
            default:
                return;
        }
        //callResult(appMsg,result);
        if (BTBoxIf.mCallback != null)
        {
            Log.i(TAG,"callRslt RSLT: "+Common.bytesToHexString(result));
            BTBoxIf.mCallback.callRslt(result);
        }
    }

    public static int resetCardTransact() {
        preTransact();
        execTransact();

        return 0;
    }

    public static int authBoxTransact() {
        preTransact();
        execTransact();

        return 0;
    }

    public static int testCardTransact() {
        preTransact();
        execTransact();

        return 0;
    }

    public static int readEssenInfoTransact() {
        preTransact();
        execTransact();

        return 0;
    }

    public static int updateEssenInfoTransact() {
        preTransact();
        execTransact();

        return 0;
    }

    public static int readAuthDataTransact() {
        preTransact();
        execTransact();

        return 0;
    }

    public static int upgradeBoxTransact() {
        preTransact();
        execTransact();

        return 0;
    }

    public static int clearSqnTransact() {
        preTransact();
        execTransact();

        return 0;
    }

    public static int handleAppMessage2(byte cmdCode, byte[] param, byte[] data,byte[] result) {
        int dataLen;
        int paramLen;
        int count;

        if(null == data)
            return -1;

        dataLen = data[0]+3;
        if(dataLen < 3)
            return -2;

        count = 1;

        switch(cmdCode)
        {
            case 0x58:
                paramLen = param[0];
                result[count++] = 0x05;
                result[count++] = (byte)((paramLen > 0x12) ? 0x01 : 0x00);
                if(data[dataLen-2] == (byte)0x90)
                {
                    result[count++] = 0x00;
                    System.arraycopy(data,1,result,count,data[0]);
                    count += data[0];
                    break;
                }
                else if(data[dataLen-2] == (byte)0xCE)
                {
                    switch(data[dataLen-1])
                    {
                        case 0x00:
                            result[count++] = (byte)0x80;
                            break;
                        case 0x02:
                            result[count++] = 0x40;
                            break;
                        case 0x04:
                            break;
                    }
                    break;
                }
                if(paramLen > 0x12)
                {
                    if((data[1] == 0x04) && (data[4] == (byte)0xDC))
                    {
                        result[count++] = 0x02;
                        System.arraycopy(data,6,result,count,data[5]);
                        count += data[5];
                        break;
                    }
                }
                result[count++] = 0x01;
                break;
            default:
                break;
        }
        result[0] = (byte)(count-1);

        return 1;

    }
}





