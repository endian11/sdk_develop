package com.travelrely.core.nr;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.travelrely.app.activity.CallActivity;
import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.core.ble.BleMsgId;
import com.travelrely.core.nr.SmsReceiver.SmsCallback;
import com.travelrely.core.nr.msg.AgtAppAuthReq;
import com.travelrely.core.nr.msg.AgtAppCalledReq;
import com.travelrely.core.nr.msg.AgtAppCallingRsp;
import com.travelrely.core.nr.msg.AgtAppErrInd;
import com.travelrely.core.nr.msg.AgtAppGpnsInd;
import com.travelrely.core.nr.msg.AgtAppHwInfoRsp;
import com.travelrely.core.nr.msg.AgtAppLauRsp;
import com.travelrely.core.nr.msg.AgtAppMsgInd;
import com.travelrely.core.nr.msg.AgtAppRegRsp;
import com.travelrely.core.nr.msg.AgtAppSmsRecvReq;
import com.travelrely.core.nr.msg.AgtAppSmsSendRsp;
import com.travelrely.core.nr.msg.AgtAppStateErr;
import com.travelrely.core.nr.msg.AgtAppSysInfoInd;
import com.travelrely.core.nr.msg.AgtAppTcpRsp;
import com.travelrely.core.nr.msg.AgtAppUeAlert;
import com.travelrely.core.nr.msg.AgtAppUserVerifyRsp;
import com.travelrely.core.nr.msg.AgtAppVoipAlert;
import com.travelrely.core.nr.msg.AgtAppVoipReq;
import com.travelrely.core.nr.msg.AgtAppVoipRsp;
import com.travelrely.core.nr.msg.AppAgtAuthRsp;
import com.travelrely.core.nr.msg.AppAgtByeInd;
import com.travelrely.core.nr.msg.AppAgtCalledInd;
import com.travelrely.core.nr.msg.AppAgtCalledRsp;
import com.travelrely.core.nr.msg.AppAgtHwInfoReq;
import com.travelrely.core.nr.msg.AppAgtKeepAliveReq;
import com.travelrely.core.nr.msg.AppAgtLauReq;
import com.travelrely.core.nr.msg.AppAgtOnOffInd;
import com.travelrely.core.nr.msg.AppAgtRegReq;
import com.travelrely.core.nr.msg.AppAgtSimCardFeeRsp;
import com.travelrely.core.nr.msg.AppAgtSmsRecvInd;
import com.travelrely.core.nr.msg.AppAgtSmsRecvRsp;
import com.travelrely.core.nr.msg.AppAgtStoreKeyRsltInd;
import com.travelrely.core.nr.msg.AppAgtTestCallingReq;
import com.travelrely.core.nr.msg.AppAgtUserVerifyReq;
import com.travelrely.core.nr.msg.AppAgtVoipAlert;
import com.travelrely.core.nr.msg.AppAgtVoipByeInd;
import com.travelrely.core.nr.msg.AppAgtVoipRsp;
import com.travelrely.core.nr.msg.MsgId;
import com.travelrely.core.nr.msg.OctArray28_s;
import com.travelrely.core.nr.mt100.Common;
import com.travelrely.core.nr.util.ByteUtil;
import com.travelrely.core.nr.util.TextUtil;
import com.travelrely.core.nr.voice.rtpTerminal;
import com.travelrely.v2.NR.RayLib;
import com.travelrely.v2.Rent.msg.AgtRentCalledReq;
import com.travelrely.v2.Rent.msg.RentAgtByeInd;
import com.travelrely.v2.Rent.msg.RentAgtCalledRsp;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.NetUtil;
import com.travelrely.v2.util.SpUtil;
import com.travelrely.v2.util.TimeUtil;

@SuppressLint("ShowToast")
public class NRWorker implements Runnable {
    private final static String TAG = "NRWorker";

    private Context mContext;
    private Handler handler;

    private TcpManager cmdSocket;
    
    private SmsReceiver mSmsReceiver;

    // 初始状态
    public int iUeMainState = UeState.MAIN_NULL;
    public int iUeSubState = UeState.SUB_NULL;

    private static DatagramSocket rtps = null;

    private static rtpTerminal rTerminal = null;

    private static AgtAppCalledReq calledReq = null;
    private static AgtAppVoipAlert voipAlert = null;
    private static AgtAppVoipReq voipReq = null;
    private static AgtRentCalledReq xhCalledReq = null;
    
    private static AppAgtUserVerifyReq verifyReq = null;
    private static AgtAppUserVerifyRsp verifyRsp = null;
    
	public static int mCall = 0;
	private SmsCallback mSmsCallback = new SmsCallback() {
        @Override
        public void onSmsReceived(String peer, String sms) {
            if (TextUtils.isEmpty(peer))
            {
                peer = "未知号码";
            }
            
            if (!TextUtils.isEmpty(sms))
            {
                Engine.getInstance().recvSMS(sms, peer);
            }
        }
    };

    public NRWorker(Context c, TcpManager cmdSocket) {
        mContext = c;
        this.cmdSocket = cmdSocket;

        mSmsReceiver = new SmsReceiver();
        mSmsReceiver.setSmsCallback(mSmsCallback);
    }
    
    public void stop() {
        setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
        handler.getLooper().quit();
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void run() {
        Looper.prepare();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b == null) {
                    return;
                }

                byte[] msgContent = b.getByteArray("cmd");
                int iCmdLen = b.getInt("cmd_len");
                
                //LogUtil.i(TAG, "Recv " + iCmdLen +"byte:"+ ByteUtil.toHexString(msgContent));
                
                int iMsgId = ByteUtil.getInt(ByteUtil.subArray(msgContent, 0, 4));
                CmdMsgEntry(iMsgId, msgContent, iCmdLen);
                
                /*int i = 0;
                while (i < iCmdLen)
                {
                    //AppLog.i(TAG, "i = %d", i);
                    int iMsgId = ByteUtil.getInt(ByteUtil.subArray(msgContent, i, 4));
                    int iContentLen = ByteUtil.getInt(ByteUtil.subArray(msgContent, i + 4, 4));
                    byte[] tmpBuf = ByteUtil.subArray(msgContent, i, 8 + iContentLen);
                    
                    i = i + 8 + iContentLen;
                    
                    CmdMsgEntry(iMsgId, tmpBuf, tmpBuf.length);
                }*/
            }
        };

        Looper.loop();
    }

    public synchronized void procMsg(int iMsgId, byte[] msgContent) {
        if (handler == null) {
            return;
        }
        if (msgContent == null) {
            msgContent = new byte[8];
            System.arraycopy(ByteUtil.getBytes(iMsgId), 0, msgContent, 0, 4);
        }

        Bundle bundle = new Bundle();
        bundle.putByteArray("cmd", msgContent);
        bundle.putInt("cmd_len", msgContent.length);

        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    private void CmdMsgEntry(int iMsgId, byte[] bMsg, int iMsgLen) {
        // 状态无关的消息
        switch (iMsgId)
        {
            case MsgId.APP_INNER_RESET:
                Engine.getInstance().isEncNeed = true;
                Engine.getInstance().isEncEnable = false;
                setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                return;

            case MsgId.AGENT_APP_TCP_RSP:
                print(LogUtil.WAR, "Recv AGENT_APP_TCP_RSP");
                AgtAppTcpRsp rsp = new AgtAppTcpRsp(bMsg);
                if (rsp.getRslt() == 0)
                {
                    print(LogUtil.WAR, "TCP重连成功");
                    Engine.getInstance().startNRService(mContext,
                            MsgId.AGENT_APP_TCP_RSP);
                }
                else
                {
                    print(LogUtil.ERR, "TCP重连失败");
                }
                return;
                
            case MsgId.APP_AGENT_HW_INFO_REQ:
                print(LogUtil.WAR, "Send APP_AGENT_HW_INFO_REQ");
                byte[] sn = Common.HexStringToBytes(SpUtil.getBoxSn());
                AppAgtHwInfoReq hwReq = new AppAgtHwInfoReq(Engine
                        .getInstance().userName(), sn);
                //print(LogUtil.WAR, ByteUtil.toHexString(hwReq.toMsg()));
                cmdSocket.sendCmdMsg(hwReq.toMsg());
                return;

            case MsgId.AGENT_APP_HW_INFO_RSP:
                print(LogUtil.WAR, "Recv AGENT_APP_HW_INFO_RSP");
                //print(LogUtil.WAR, ByteUtil.toHexString(bMsg));
                Engine.getInstance().startNRService(mContext,
                        MsgId.AGENT_APP_TCP_RSP);
                AgtAppHwInfoRsp hwRsp = new AgtAppHwInfoRsp(bMsg);
                if (hwRsp.isEncEnable())
                {
                    Engine.getInstance().isEncNeed = false;
                    Engine.getInstance().isEncEnable = true;
                    Engine.getInstance().startBleService(mContext,
                            BleMsgId.NR_BLE_RAND_IND, hwRsp.getRandom());
                }
                else
                {
                    Engine.getInstance().isEncNeed = false;
                    Engine.getInstance().isEncEnable = false;
                    Engine.getInstance().startBleService(mContext,
                            BleMsgId.NR_BLE_NO_RAND_IND);
                }
                return;

            case MsgId.APP_AGENT_DTMF_IND:
                cmdSocket.sendCmdMsg(bMsg);
                return;
                
            case MsgId.AGENT_APP_SMS_RECV_REQ:
                print(LogUtil.INF, "收到AGENT_APP_SMS_RECV_REQ");
                AgtAppSmsRecvReq smsRecvReq = new AgtAppSmsRecvReq(bMsg);
                if (iUeMainState == UeState.MAIN_VERIFY)
                {
                    if (smsRecvReq.getSenderArray28_s().trim()
                            .equals(verifyRsp.getRecvPhone()))
                    {
                        String user = Engine.getInstance().getUserName();
                        byte[] sms = smsRecvReq.getSmsByte();
                        byte[] dst = new byte[1024];

                        int size = RayLib.descram(sms, user.getBytes(), dst);
                        if (size > 0)
                        {
                            String tmp = new String(ByteUtil.subArray(dst, 0, size));
                            if (tmp.equals(verifyReq.getRandom()))
                            {
                                print(LogUtil.INF, "USER verify succ");
                                
                                AppAgtSmsRecvRsp aRsp = new AppAgtSmsRecvRsp(Engine
                                        .getInstance().userName(), 0);
                                cmdSocket.sendCmdMsg(aRsp.toMsg());
                                print(LogUtil.INF, "发送APP_AGENT_SMS_RECV_RSP");

                                setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                                
                                Engine.getInstance().broadcast(IAction.USER_VERIFY_SUCC, 0);
                                return;
                            }
                            else
                            {
                                recvSMS(smsRecvReq);
                            }
                        }
                    }
                    else
                    {
                        recvSMS(smsRecvReq);
                    }
                }
                else
                {
                    recvSMS(smsRecvReq);
                }

                AppAgtSmsRecvRsp aRsp = new AppAgtSmsRecvRsp(Engine
                        .getInstance().userName(), 0);
                cmdSocket.sendCmdMsg(aRsp.toMsg());
                print(LogUtil.INF, "发送APP_AGENT_SMS_RECV_RSP");
                return;
                
            case MsgId.AGENT_APP_NEED_REG_IND:
                if (TextUtils.isEmpty(SpUtil.getBtAddr()))
                {
                    print(LogUtil.ERR, "Recv AGENT_APP_NEED_REG_IND"
                            + " but box not matched");
                    return;
                }
                print(LogUtil.WAR, "Recv AGENT_APP_NEED_REG_IND");

                // 发起注册
                setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                Engine.getInstance().stopBleService(mContext);
                Engine.getInstance().startBleService(mContext,
                        BleMsgId.NR_BLE_READ_SIM_IND);
                return;

            case MsgId.AGENT_APP_MESSAGE_IND:
                print(LogUtil.WAR, "Recv AGENT_APP_MESSAGE_IND");
                AgtAppMsgInd ind = new AgtAppMsgInd(bMsg);
                recvMsgInd(ind);
                return;
                
            case MsgId.AGENT_APP_ERR_IND:
                print(LogUtil.ERR, "Recv AGENT_APP_ERR_IND");
                AgtAppErrInd stMsg = new AgtAppErrInd(bMsg);
                Engine.getInstance().startNRService(mContext,
                        MsgId.APP_SHOW_ERR_MSG, stMsg.getErrMsg());
                return;
                
            case MsgId.AGENT_APP_STATE_ERR:
                print(LogUtil.ERR, "Recv AGENT_APP_STATE_ERR");
                AgtAppStateErr err = new AgtAppStateErr(bMsg);
                Engine.getInstance().startNRService(mContext,
                        MsgId.APP_SHOW_ERR_MSG, err.getErrMsg());
                
                // 发起注册
                setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                Engine.getInstance().stopBleService(mContext);
                Engine.getInstance().startBleService(mContext,
                        BleMsgId.NR_BLE_READ_SIM_IND);
                return;
                
            case MsgId.AGENT_APP_SYSINFO_IND:
                print(LogUtil.ERR, "Recv AGENT_APP_SYSINFO_IND");
                AgtAppSysInfoInd i = new AgtAppSysInfoInd(bMsg);
                Engine.getInstance().recvLxMsg(i.getMsg(), "旅信助手");
                return;
                
            case MsgId.AGENT_APP_SIM_CARD_FEE_IND:
                print(LogUtil.ERR, "Recv AGENT_APP_SIM_CARD_FEE_IND");
                Engine.getInstance().startNRService(mContext,
                        MsgId.NR_UI_SIM_FEE_ALERT, bMsg);
                return;
                
            case MsgId.APP_AGENT_SIM_CARD_FEE_RSP:
                AppAgtSimCardFeeRsp sfp = new AppAgtSimCardFeeRsp(Engine
                        .getInstance().userName(), 0);
                cmdSocket.sendCmdMsg(sfp.toMsg());
                return;
                
            case MsgId.APP_CLOSE_NR:
                print(LogUtil.INF, "Recv APP_CLOSE_NR");
                AppAgtOnOffInd off = new AppAgtOnOffInd(Engine
                        .getInstance().userName(), 0, 0);
                cmdSocket.sendCmdMsg(off.toMsg());
                setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                Engine.getInstance().broadcast(IAction.NR_CLOSE_SUCC);
                
                Engine.getInstance().isEncNeed = true;
                Engine.getInstance().isEncEnable = false;
                return;
                
            case MsgId.APP_OPEN_NR:
                print(LogUtil.INF, "Recv APP_OPEN_NR");
                AppAgtOnOffInd on = new AppAgtOnOffInd(Engine
                        .getInstance().userName(), 0, 1);
                cmdSocket.sendCmdMsg(on.toMsg());
                return;

            case MsgId.APP_STATE_EXPIRE:
                hdlStateExpireMsg(bMsg, bMsg.length);
                Engine.getInstance().startBleService(mContext, 
                        BleMsgId.NR_BLE_DISCONNECT_IND);
                return;
                
            case MsgId.AGT_APP_XH_CALLED_REQ:
                print(LogUtil.INF, "收到AGT_APP_XH_CALLED_REQ");
                xhCalledReq = new AgtRentCalledReq(bMsg);

                // 此处弹出是否接听对话框
                Engine.getInstance().startNRService(mContext,
                        MsgId.APP_XH_CALLED_REQ, xhCalledReq.getCaller());
                creatRtpSocket();
                return;
                
            case MsgId.APP_XH_ANSWER:
                RentAgtCalledRsp Rsp = new RentAgtCalledRsp(Engine.getInstance()
                        .userName(), 0);
                cmdSocket.sendCmdMsg(Rsp.toMsg());

                String strIp = xhCalledReq.getIp();
                int port = xhCalledReq.getPort();
                LogUtil.i(TAG, "IP=" + strIp + ":" + port);

//                rTerminal = new rtpTerminal();
//                rTerminal.startRtpSession(strIp, port, cpPort2, rtps);
                
                String localIp = "0.0.0.0";
            	int localPort = 11500;
            	String remoteIp=strIp;
            	int remotePort = port;
            	String payLoadname = "AMR";
            	int payloadValue = 114;
            	int samples = 8000;	
            	int voiceCtrl = 0x1113;
				
            	Log.e("nrworker", "voice ctrl "+voiceCtrl);
            	Log.e("nrworker", "payLoadname "+payLoadname);
            	Log.e("nrworker", "payloadValue "+payloadValue);
            	Log.e("nrworker", "sample rate "+samples);
            	Log.e("nrworker", "local ipp "+localIp);
            	Log.e("nrworker", "local port "+localPort);
            	Log.e("nrworker", "remote ip "+remoteIp);
            	Log.e("nrworker", "remote port "+remotePort);
//            	Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG).show();
            	
            	mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
            			remoteIp, remotePort,
						payLoadname, payloadValue, samples);
                return;
                
            case MsgId.AGT_APP_XH_BYE_IND:
                hdlXiaohaoByeApp();
                return;
                
            case MsgId.APP_XH_HANG_UP:
                hdlAppByeXiaohao();
                return;

            default:
                break;
        }

        switch (iUeMainState)
        {
            case UeState.MAIN_NULL:
                hdlMsgOnMainNull(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_REGISTED:
                hdlMsgOnMainRegisted(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_CALLING_CONNECTED:
            case UeState.MAIN_CALLED_CONNECTED:
                hdlMsgOnMainConnected(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_VOIP_CALLING_CONNECTED:
            case UeState.MAIN_VOIP_CALLED_CONNECTED:
                hdlMsgOnMainVoipConnected(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_REGISTING:
                hdlMsgOnMainRegisting(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_CALLING:
                hdlMsgOnMainCalling(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_CALLED:
                hdlMsgOnMainCalled(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_SEND_SMS:
                hdlMsgOnMainSendSMS(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_RECV_SMS:
                hdlMsgOnMainRecvSMS(iMsgId, bMsg, iMsgLen);
                break;
                
            case UeState.MAIN_VERIFY:
                hdlMsgOnMainVerify(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_LAU:
                hdlMsgOnMainLAU(iMsgId, bMsg, iMsgLen);
                break;

            case UeState.MAIN_VOIP_CALLING:
                hdlMsgOnMainVoipCalling(iMsgId, bMsg, iMsgLen);
                break;
                
            case UeState.MAIN_VOIP_CALLED:
                hdlMsgOnMainVoipCalled(iMsgId, bMsg, iMsgLen);
                break;
                
            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainNull(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iMsgId)
        {
            case MsgId.APP_AGENT_TEST_CALLING_REQ:
                startTestCalling();
                break;
                
            case MsgId.APP_AGENT_REGISTER_REQ:
                startReg();
                break;
                
            case MsgId.APP_AGENT_KEEPALIVE_REQ:
                AppAgtKeepAliveReq keep = new AppAgtKeepAliveReq(
                        Engine.getInstance().userName(),
                        NetUtil.getNetType(mContext),
                        (int) (NetUtil.getMobileTtlBytes()/1024),0);
                cmdSocket.sendCmdMsg(keep.toMsg());
                print(LogUtil.WAR, "发送APP_AGENT_KEEPALIVE_REQ");
                
                setState(iUeMainState, iUeSubState, 10);
                break;
                
            case MsgId.AGENT_APP_KEEPALIVE_RSP:
                print(LogUtil.WAR, "收到AGENT_APP_KEEPALIVE_RSP");
                setState(iUeMainState, iUeSubState, 0);
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainRegisted(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        if (iUeSubState != UeState.SUB_NULL)
        {
            setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
        }

        switch (iMsgId)
        {
            case MsgId.APP_AGENT_KEEPALIVE_REQ:
                int connRslt = 1;
                if (BoxManager.getDefault().isConnected)
                {
                    connRslt = 0;
                }
                AppAgtKeepAliveReq keep = new AppAgtKeepAliveReq(
                        Engine.getInstance().userName(),
                        NetUtil.getNetType(mContext),
                        (int) (NetUtil.getMobileTtlBytes()/1024),connRslt);
                cmdSocket.sendCmdMsg(keep.toMsg());
                print(LogUtil.WAR, "发送APP_AGENT_KEEPALIVE_REQ");
                setState(iUeMainState, iUeSubState, 10);
                break;
                
            case MsgId.AGENT_APP_KEEPALIVE_RSP: 
                print(LogUtil.WAR, "收到AGENT_APP_KEEPALIVE_RSP");
                setState(iUeMainState, iUeSubState, 0);
                break;
                
            case MsgId.APP_AGENT_STORE_KEY_RSLT_IND:
                AppAgtStoreKeyRsltInd ind = new AppAgtStoreKeyRsltInd(
                        Engine.getInstance().userName(), 1);
                cmdSocket.sendCmdMsg(ind.toMsg());
                print(LogUtil.WAR, "发送APP_AGENT_STORE_KEY_RSLT_IND");
                break;
                
            case MsgId.AGENT_APP_LAU_IND:
                print(LogUtil.WAR, "收到AGENT_APP_LAU_IND");
                AppAgtLauReq lauReq = new AppAgtLauReq(Engine.getInstance()
                        .userName());
                cmdSocket.sendCmdMsg(lauReq.toMsg());
                print(LogUtil.WAR, "发送APP_AGENT_LAU_REQ");

                setState(UeState.MAIN_LAU, UeState.SUB_LAU_W_FMT_AUTH_REQ, 10);
                break;

            case MsgId.APP_AGENT_CALLING_REQ:
                hdlCallingReqMsg(bMsg);
                break;

            case MsgId.AGENT_APP_PAGING_REQ:
                hdlPagingMsg(bMsg);
                break;
                
            case MsgId.APP_AGENT_LSMS_SEND_REQ:
                cmdSocket.sendCmdMsg(bMsg);
                print(LogUtil.INF, "发送APP_AGENT_LSMS_SEND_REQ");
                setState(UeState.MAIN_SEND_SMS,
                        UeState.SUB_SEND_SMS_W_FMT_AUTH_REQ, 10);
                break;
                
            case MsgId.AGENT_APP_VOIP_REQ:
                hdlVoipReqMsg(bMsg);
                break;
                
            case MsgId.APP_AGENT_TEST_CALLING_REQ:
                startTestCalling();
                break;

            case MsgId.APP_AGENT_USER_VERIFY_REQ:
                byte[] imsi = BoxManager.getDefault().getImsi();
                byte[] random = TextUtil.getRandomNumSeries(12);
                String user = Engine.getInstance().userName();
                LogUtil.i(TAG, ByteUtil.toHexString(random));
                verifyReq = new AppAgtUserVerifyReq(user, imsi, random);

                cmdSocket.sendCmdMsg(verifyReq.toMsg());
                print(LogUtil.INF, "发送APP_AGENT_USER_VERIFY_REQ");
                setState(UeState.MAIN_VERIFY, UeState.SUB_VERIFY_W_RSP, 10);
                break;
                
            case MsgId.AGENT_APP_LSMS_AUTH_REQ:
                print(LogUtil.INF, "收到AGENT_APP_LSMS_AUTH_REQ");
                AgtAppAuthReq authReq = new AgtAppAuthReq(bMsg);
                btBoxAuth(authReq);
                break;
                
            case MsgId.APP_AGENT_AUTH_RSP:
                byte[] sres = BoxManager.getDefault().getAuthRslt();
                OctArray28_s xresArray28_s = new OctArray28_s(sres.length, sres);
                AppAgtAuthRsp ausResp = new AppAgtAuthRsp(Engine.getInstance()
                        .userName(), 0, xresArray28_s);
                cmdSocket.sendCmdMsg(ausResp.toMsg());

                print(LogUtil.INF, "发送APP_AGENT_AUTH_RSP");
                BoxManager.getDefault().clearAuth();
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainConnected(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iMsgId)
        {
            case MsgId.APP_STOP_RECORD:
                stopRecord(true);
                break;
                
            case MsgId.APP_START_RECORD:
                stopRecord(false);
                break;
                
            case MsgId.APP_HANG_UP:
                hdlAppByeMsg();
                break;

            case MsgId.AGENT_APP_BYE_IND:
                hdlAgentByeMsg();
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainRegisting(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iUeSubState)
        {
            case UeState.SUB_REGISTING_W_FMT_AUTH_REQ:
                if (hdlAuthReqMsg(iMsgId, bMsg))
                {
                    setState(UeState.MAIN_REGISTING,
                            UeState.SUB_REGISTING_W_SIM_AUTH_RSP, 10);
                }
                break;
                
            case UeState.SUB_REGISTING_W_SIM_AUTH_RSP:
                if (iMsgId != MsgId.APP_AGENT_AUTH_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_REGISTING,
                            UeState.SUB_REGISTING_W_FMT_REG_RSP, 18);
                }
                else
                {
                    setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                    print(LogUtil.ERR, "鉴权失败");
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_REG_FAIL);
                }
                break;

            case UeState.SUB_REGISTING_W_FMT_REG_RSP:
                if (iMsgId != MsgId.AGENT_APP_REGISTER_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                print(LogUtil.INF, "收到AGENT_APP_REGISTER_RSP");

                AgtAppRegRsp regRsp = new AgtAppRegRsp(bMsg);
                if (regRsp.result != 0)
                {
                    setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                    print(LogUtil.ERR, "注册失败");
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_REG_FAIL);
                    return;
                }

                Engine.getInstance().startNRService(mContext,
                        MsgId.APP_REG_SUCC);

                setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                print(LogUtil.INF, "["+regRsp.getTimeStamp()+"]" + "注册成功");
                
                if (regRsp.getBoxKeyLen() > 0)
                {
                    BoxManager.getDefault().setAuthCipherKey(regRsp.getBoxKey());
                    Engine.getInstance().startBleService(mContext,
                            BleMsgId.NR_BLE_CIPHER_ON_IND);
                }
                else
                {
                    Engine.getInstance().startBleService(mContext,
                            BleMsgId.NR_BLE_CIPHER_OFF_IND);
                }
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    
    private void hdlMsgOnMainCalling(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        if (iMsgId == MsgId.APP_HANG_UP)
        {
            hdlAppByeMsg();
            return;
        }

        if (iMsgId == MsgId.AGENT_APP_BYE_IND)
        {
            hdlAgentByeMsg();
            return;
        }

        switch (iUeSubState)
        {
            case UeState.SUB_CALLING_W_FMT_AUTH_REQ:
                if (iMsgId == MsgId.AGENT_APP_AUTH_REQ)
                {
                    if (hdlAuthReqMsg(iMsgId, bMsg))
                    {
                        setState(UeState.MAIN_CALLING,
                                UeState.SUB_CALLING_W_SIM_AUTH_RSP, 10);
                    }
                }

                if (iMsgId == MsgId.AGENT_APP_VOIP_ALERT)
                {
                    print(LogUtil.INF, "收到AGENT_APP_VOIP_ALERT");
                    voipAlert = new AgtAppVoipAlert(bMsg);

                    setState(UeState.MAIN_VOIP_CALLING,
                            UeState.SUB_VOIP_CALLING_W_FMT_CALL_RSP, 60);

                    // 通知响铃音
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_PLAY_RINGTONE);
                }
                
                if (iMsgId == MsgId.AGENT_APP_CALLING_RSP)
                {
                    print(LogUtil.WAR, "收到AGENT_APP_CALLING_RSP");
                    AgtAppCallingRsp CallingRsp = new AgtAppCallingRsp(bMsg);
                    if (CallingRsp.result != 0)
                    {
                        Engine.getInstance().startNRService(mContext,
                                MsgId.AGENT_APP_BYE_IND);
                        
                        setState(UeState.MAIN_REGISTED,
                                UeState.SUB_NULL, 0);
                    }
                }
                break;

            case UeState.SUB_CALLING_W_SIM_AUTH_RSP:
                if (iMsgId != MsgId.APP_AGENT_AUTH_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_CALLING,
                            UeState.SUB_CALLING_W_FMT_ALERT, 18);
                }
                else
                {
                    setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                    Engine.getInstance().startNRService(mContext, 
                            MsgId.APP_BE_HUNG_UP);
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SHOW_ERR_MSG,
                            "通话连接未成功，请再次尝试(D1)");
                }
                break;

            case UeState.SUB_CALLING_W_FMT_ALERT:
                if (iMsgId != MsgId.AGENT_APP_UE_ALERT)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                // 将本地的RTP语音地址告诉FEMTO(APP_AGENT_RTP_IND)
                print(LogUtil.INF, "收到AGENT_APP_UE_ALERT");
                AgtAppUeAlert alert = new AgtAppUeAlert(bMsg);
                String strIp = alert.getIp();
                int port = alert.getPort();
                LogUtil.i(TAG, "IP=" + strIp + ":" + port);

//                rTerminal = new rtpTerminal();
//                rTerminal.startRtpSession(strIp, port, cpPort2, rtps);
                //audio = new AudioManager(strIp, port);
                //audio.start();
                	
                String localIp = "0.0.0.0";
            	int localPort = 11500;
            	String remoteIp=strIp;
            	int remotePort = port;
            	String payLoadname = "AMR";
            	int payloadValue = 114;
            	int samples = 8000;	
            	int voiceCtrl = 0x1113;
				

            	Log.e("nrworker", "voice ctrl "+voiceCtrl);
            	Log.e("nrworker", "payLoadname "+payLoadname);
            	Log.e("nrworker", "payloadValue "+payloadValue);
            	Log.e("nrworker", "sample rate "+samples);
            	Log.e("nrworker", "local ipp "+localIp);
            	Log.e("nrworker", "local port "+localPort);
            	Log.e("nrworker", "remote ip "+remoteIp);
            	Log.e("nrworker", "remote port "+remotePort);
//            	Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG);
            	mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
            			remoteIp, remotePort,
						payLoadname, payloadValue, samples);
//            	Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG).show();
                setState(UeState.MAIN_CALLING,
                        UeState.SUB_CALLING_W_FMT_CALL_RSP, 0);
                break;

            case UeState.SUB_CALLING_W_FMT_CALL_RSP:
                if (iMsgId != MsgId.AGENT_APP_CALLING_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                print(LogUtil.INF, "收到AGENT_APP_CALLING_RSP");
                AgtAppCallingRsp rsp = new AgtAppCallingRsp(bMsg);
                if (rsp.result == 0)
                {
                    // 主叫被接通
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_CALLING_CONNECTED);

                    setState(UeState.MAIN_CALLING_CONNECTED,
                            UeState.SUB_CONNECT_W_APP_RECV_SMS_RSP, 0);
                    LogUtil.i(TAG, "主叫成功");
                }
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainCalled(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        if (iMsgId == MsgId.APP_HANG_UP)
        {
            print(LogUtil.INF, "发送APP_AGENT_CALLED_RSP");
            // 在未接听直接挂断的时候需要called rsp,接听之后挂断则不需要
            AppAgtCalledRsp calledRsp = new AppAgtCalledRsp(Engine
                    .getInstance().userName(), 1);
            cmdSocket.sendCmdMsg(calledRsp.toMsg());

            hdlAppByeMsg();
            return;
        }

        if (iMsgId == MsgId.AGENT_APP_BYE_IND)
        {
            hdlAgentByeMsg();
            return;
        }

        switch (iUeSubState)
        {
            case UeState.SUB_CALLED_W_FMT_AUTH_REQ:
                if (hdlAuthReqMsg(iMsgId, bMsg))
                {
                    setState(UeState.MAIN_CALLED,
                            UeState.SUB_CALLED_W_SIM_AUTH_RSP, 10);
                }
                break;
                
            case UeState.SUB_CALLED_W_SIM_AUTH_RSP:
                if (iMsgId != MsgId.APP_AGENT_AUTH_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_CALLED,
                            UeState.SUB_CALLED_W_FMT_CALL_REQ, 18);
                }
                break;

            case UeState.SUB_CALLED_W_FMT_CALL_REQ:
                if (iMsgId != MsgId.AGENT_APP_CALLED_REQ)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                print(LogUtil.INF, "收到AGENT_APP_CALLED_REQ");
                calledReq = new AgtAppCalledReq(bMsg);

                // 此处弹出是否接听对话框
                Engine.getInstance().startNRService(mContext,
                        MsgId.APP_CALLED_REQ, calledReq.getCaller());

                setState(UeState.MAIN_CALLED,
                        UeState.SUB_CALLED_W_UI_CALL_RSP, 50);
                break;

            case UeState.SUB_CALLED_W_UI_CALL_RSP:
                if (iMsgId != MsgId.APP_ANSWER)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                print(LogUtil.INF, "发送APP_AGENT_CALLED_RSP");
                AppAgtCalledRsp Rsp = new AppAgtCalledRsp(Engine.getInstance()
                        .userName(), 0);
                cmdSocket.sendCmdMsg(Rsp.toMsg());

                String strIp = calledReq.getIp();
                int port = calledReq.getPort();
                LogUtil.i(TAG, "IP=" + strIp + ":" + port);

//                rTerminal = new rtpTerminal();
//                rTerminal.startRtpSession(strIp, port, cpPort2, rtps);

                String localIp = "0.0.0.0";
            	int localPort = 11500;
            	String remoteIp=strIp;
            	int remotePort = port;
            	String payLoadname = "AMR";
            	int payloadValue = 114;
            	int samples = 8000;	
            	int voiceCtrl = 0x1113;

            	
            	Log.e("nrworker", "voice ctrl "+voiceCtrl);
            	Log.e("nrworker", "payLoadname "+payLoadname);
            	Log.e("nrworker", "payloadValue "+payloadValue);
            	Log.e("nrworker", "sample rate "+samples);
            	Log.e("nrworker", "local ipp "+localIp);
            	Log.e("nrworker", "local port "+localPort);
            	Log.e("nrworker", "remote ip "+remoteIp);
            	Log.e("nrworker", "remote port "+remotePort);
//            	Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG).show();
            	mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
            			remoteIp, remotePort,
						payLoadname, payloadValue, samples);
                
                
                setState(UeState.MAIN_CALLED_CONNECTED,
                        UeState.SUB_CONNECT_W_APP_RECV_SMS_RSP, 0);

                LogUtil.i(TAG, "被叫成功");
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainSendSMS(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iUeSubState)
        {
            case UeState.SUB_SEND_SMS_W_FMT_AUTH_REQ:
                if (hdlAuthReqMsg(iMsgId, bMsg))
                {
                    setState(UeState.MAIN_SEND_SMS,
                            UeState.SUB_SEND_SMS_W_SIM_AUTH_RSP, 10);
                }
                break;
                
            case UeState.SUB_SEND_SMS_W_SIM_AUTH_RSP:
                if (iMsgId != MsgId.APP_AGENT_AUTH_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_SEND_SMS,
                            UeState.SUB_SEND_SMS_W_FMT_SEND_RSP, 18);
                }
                else
                {
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SEND_SMS_FAIL);
                    setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                }
                break;

            case UeState.SUB_SEND_SMS_W_FMT_SEND_RSP:
                if (iMsgId != MsgId.AGENT_APP_SMS_SEND_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                print(LogUtil.INF, "收到AGENT_APP_SMS_SEND_RSP");
                AgtAppSmsSendRsp rsp = new AgtAppSmsSendRsp(bMsg);
                if (rsp.result != 0)
                {
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SEND_SMS_FAIL);
                    LogUtil.e(TAG, "send SMS failed[" + rsp.result + "]");
                }
                else
                {
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SEND_SMS_SUCC);
                    LogUtil.i(TAG, "SEND SMS SUCCESS");
                }

                setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainRecvSMS(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iUeSubState)
        {
            case UeState.SUB_RECV_SMS_W_FMT_AUTH_REQ:
                if (hdlAuthReqMsg(iMsgId, bMsg))
                {
                    setState(UeState.MAIN_RECV_SMS,
                            UeState.SUB_RECV_SMS_W_SIM_AUTH_RSP, 10);
                }
                break;
                
            case UeState.SUB_RECV_SMS_W_SIM_AUTH_RSP:
                if (iMsgId != MsgId.APP_AGENT_AUTH_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                }
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }
    
    private void hdlMsgOnMainVerify(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iUeSubState)
        {
            case UeState.SUB_VERIFY_W_RSP:
                if (iMsgId != MsgId.AGENT_APP_USER_VERIFY_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                
                print(LogUtil.INF, "收到AGENT_APP_USER_VERIFY_RSP");
                
                verifyRsp = new AgtAppUserVerifyRsp(bMsg);
                if (verifyRsp.getRslt() == 0)
                {
                    setState(UeState.MAIN_VERIFY,
                            UeState.SUB_VERIFY_W_PAGING, 60);
                }
                else if (verifyRsp.getRslt() == 1)
                {
                    setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                    Engine.getInstance().broadcast(IAction.USER_VERIFY_SUCC, 1);
                }
                break;
                
            case UeState.SUB_VERIFY_W_PAGING:
                if (iMsgId != MsgId.AGENT_APP_PAGING_REQ)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                print(LogUtil.INF, "收到AGENT_APP_PAGING_REQ");
                
                AgtAppGpnsInd gpns = new AgtAppGpnsInd(
                        ByteUtil.subArray(bMsg, 8, bMsg.length-8));
                if (0x01 == gpns.type) // sms
                {
                    AppAgtSmsRecvInd recvInd = new AppAgtSmsRecvInd(
                            gpns.usernameString);
                    cmdSocket.sendCmdMsg(recvInd.toMsg());
                    print(LogUtil.INF, "发送APP_AGENT_SMS_RECV_IND");

                    setState(UeState.MAIN_VERIFY,
                            UeState.SUB_RECV_SMS_W_FMT_AUTH_REQ, 10);
                }
                else
                {
                    print(LogUtil.ERR, "收到无效的paging");
                }
                break;
                
            case UeState.SUB_RECV_SMS_W_FMT_AUTH_REQ:
                if (hdlAuthReqMsg(iMsgId, bMsg))
                {
                    setState(UeState.MAIN_VERIFY,
                            UeState.SUB_RECV_SMS_W_SIM_AUTH_RSP, 10);
                }
                break;
                
            case UeState.SUB_RECV_SMS_W_SIM_AUTH_RSP:
                if (iMsgId != MsgId.APP_AGENT_AUTH_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_VERIFY,
                            UeState.SUB_RECV_SMS_W_FMT_RECV_REQ, 18);
                }
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainLAU(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iUeSubState)
        {
            case UeState.SUB_LAU_W_FMT_AUTH_REQ:
                if (hdlAuthReqMsg(iMsgId, bMsg))
                {
                    setState(UeState.MAIN_LAU,
                            UeState.SUB_LAU_W_SIM_AUTH_RSP, 10);
                }
                break;
                
            case UeState.SUB_LAU_W_SIM_AUTH_RSP:
                if (iMsgId != MsgId.APP_AGENT_AUTH_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_LAU,
                            UeState.SUB_LAU_W_FMT_RSP, 10);
                }
                break;

            case UeState.SUB_LAU_W_FMT_RSP:
                if (iMsgId != MsgId.AGENT_APP_LAU_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                print(LogUtil.INF, "AGENT_APP_LAU_RSP");
                AgtAppLauRsp rsp = new AgtAppLauRsp(bMsg);
                if (rsp.result == 0)
                {
                    print(LogUtil.INF, "LAU成功");
                    setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                    Engine.getInstance().startNRService(mContext, 
                            MsgId.APP_SHOW_REG_SUCC);
                }
                else
                {
                    print(LogUtil.ERR, "LAU失败");
                    setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                    Engine.getInstance().startNRService(mContext, 
                            MsgId.APP_SHOW_REG_FAIL);
                }
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }
    
    private void hdlMsgOnMainVoipCalling(int iMsgId, byte[] bMsg, int iMsgLen)
    {        
        if (iMsgId == MsgId.APP_HANG_UP)
        {
            hdlAppVoipByeMsg();
            return;
        }

        if (iMsgId == MsgId.AGENT_APP_VOIP_BYE_IND)
        {
            hdlAgentVoipByeMsg();
            return;
        }
        
        switch (iUeSubState)
        {
            case UeState.SUB_VOIP_CALLING_W_FMT_CALL_RSP:
                if (iMsgId != MsgId.AGENT_APP_VOIP_RSP)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                print(LogUtil.INF, "收到AGENT_APP_VOIP_RSP");
                AgtAppVoipRsp rsp = new AgtAppVoipRsp(bMsg);
                if (rsp.getRslt() == 0)
                {
                    String strIp = voipAlert.getIp();
                    int port = voipAlert.getPort();
                    LogUtil.i(TAG, "IP=" + strIp + ":" + port);

//                    rTerminal = new rtpTerminal();
//                    rTerminal.startRtpSession(strIp, port, cpPort2, rtps);
                    String localIp = "0.0.0.0";
                	int localPort = 11500;
                	String remoteIp=strIp;
                	int remotePort = port;
                	String payLoadname = "AMR";
                	int payloadValue = 114;
                	int samples = 8000;	
                	int voiceCtrl = 0x1113;
    				

                	Log.e("nrworker", "voice ctrl "+voiceCtrl);
                	Log.e("nrworker", "payLoadname "+payLoadname);
                	Log.e("nrworker", "payloadValue "+payloadValue);
                	Log.e("nrworker", "sample rate "+samples);
                	Log.e("nrworker", "local ipp "+localIp);
                	Log.e("nrworker", "local port "+localPort);
                	Log.e("nrworker", "remote ip "+remoteIp);
                	Log.e("nrworker", "remote port "+remotePort);
//                	Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG).show();
                	mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
                			remoteIp, remotePort,
    						payLoadname, payloadValue, samples);
                    // 主叫被接通
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_CALLING_CONNECTED);

                    setState(UeState.MAIN_VOIP_CALLING_CONNECTED,
                            UeState.SUB_CONNECT_W_APP_RECV_SMS_RSP, 0);
                    LogUtil.i(TAG, "主叫成功");
                }
                else
                {
                    hdlAgentVoipByeMsg();
                }
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }
    
    private void hdlMsgOnMainVoipCalled(int iMsgId, byte[] bMsg, int iMsgLen)
    {        
        if (iMsgId == MsgId.APP_HANG_UP)
        {
            hdlAppVoipByeMsg();
            return;
        }

        if (iMsgId == MsgId.AGENT_APP_VOIP_BYE_IND)
        {
            hdlAgentVoipByeMsg();
            return;
        }
        
        switch (iUeSubState)
        {
            case UeState.SUB_CALLED_W_UI_CALL_RSP:
                if (iMsgId != MsgId.APP_ANSWER)
                {
                    print(LogUtil.ERR, iMsgId);
                    return;
                }

                print(LogUtil.INF, "发送APP_AGENT_VOIP_RSP");
                AppAgtVoipRsp Rsp = new AppAgtVoipRsp(Engine.getInstance()
                        .userName(), 0);
                cmdSocket.sendCmdMsg(Rsp.toMsg());

                String strIp = voipReq.getIp();
                int port = voipReq.getPort();
                LogUtil.i(TAG, "IP=" + strIp + ":" + port);

//                rTerminal = new rtpTerminal();
//                rTerminal.startRtpSession(strIp, port, cpPort2, rtps);

                String localIp = "0.0.0.0";
            	int localPort = 11500;
            	String remoteIp=strIp;
            	int remotePort = port;
            	String payLoadname = "AMR";
            	int payloadValue = 114;
            	int samples = 8000;	
            	int voiceCtrl = 0x1113;
				
            	Log.e("nrworker", "voice ctrl "+voiceCtrl);
            	Log.e("nrworker", "payLoadname "+payLoadname);
            	Log.e("nrworker", "payloadValue "+payloadValue);
            	Log.e("nrworker", "sample rate "+samples);
            	Log.e("nrworker", "local ipp "+localIp);
            	Log.e("nrworker", "local port "+localPort);
            	Log.e("nrworker", "remote ip "+remoteIp);
            	Log.e("nrworker", "remote port "+remotePort);
//            	Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG).show();
            	mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
            			remoteIp, remotePort,
						payLoadname, payloadValue, samples);
                
                setState(UeState.MAIN_VOIP_CALLED_CONNECTED,
                        UeState.SUB_CONNECT_W_APP_RECV_SMS_RSP, 0);

                LogUtil.i(TAG, "被叫成功");
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }
    
    private void startTestCalling()
    {
        creatRtpSocket();
        
        AppAgtTestCallingReq req = new AppAgtTestCallingReq(Engine.getInstance()
                .userName(), 0);
        cmdSocket.sendCmdMsg(req.toMsg());
        print(LogUtil.INF, "发送APP_AGENT_TEST_CALLING_REQ");

        setState(UeState.MAIN_TEST, iUeMainState, 0);
    }
    
    private void startReg()
    {
        byte[] imsi = BoxManager.getDefault().getImsi();
        String smc = BoxManager.getDefault().getSmsp();
        String phone = BoxManager.getDefault().getMsisdn();
        int iNetType = NetUtil.getNetType(mContext);

        if (null == imsi || imsi.length == 0)
        {
            print(LogUtil.ERR, "imsi == null");
            return;
        }

        //String imei = DeviceInfo.getInstance(mContext).imei;

        AppAgtRegReq regReq = new AppAgtRegReq(Engine.getInstance().userName(),
                imsi, smc, phone, iNetType);
        cmdSocket.sendCmdMsg(regReq.toMsg());
        print(LogUtil.INF, "发送APP_AGENT_REGISTER_REQ");

        setState(UeState.MAIN_REGISTING, UeState.SUB_REGISTING_W_FMT_AUTH_REQ,
                10);
    }

    private boolean hdlAuthReqMsg(int iMsgId, byte[] bMsg)
    {
        if (iMsgId != MsgId.AGENT_APP_AUTH_REQ)
        {
            print(LogUtil.ERR, iMsgId);
            return false;
        }

        print(LogUtil.INF, "收到AGENT_APP_AUTH_REQ");
        AgtAppAuthReq authReq = new AgtAppAuthReq(bMsg);
        return btBoxAuth(authReq);
    }

    private boolean btBoxAuth(final AgtAppAuthReq authReq)
    {        
        if (authReq.is2G())
        {
            BoxManager.getDefault().setAuthParam(
                    authReq.randArray, null);
        }
        else
        {
            BoxManager.getDefault().setAuthParam(
                    authReq.randArray, authReq.autnArray);
        }
        
        Engine.getInstance().startBleService(mContext,
                BleMsgId.NR_BLE_AUTH_SIM_IND);
        
        return true;
    }
    
    private boolean hdlAuthRsltMsg()
    {
        int iRslt = BoxManager.getDefault().getAuthErr();
        byte[] sres = BoxManager.getDefault().getAuthRslt();
        if (sres==null || sres.length==0)
        {
            print(LogUtil.ERR, "没有得到鉴权结果");
            sres = new byte[]{0x00, 0x00, 0x00, 0x00};
        }
        OctArray28_s xresArray28_s = new OctArray28_s(sres.length, sres);
        AppAgtAuthRsp ausResp = new AppAgtAuthRsp(Engine.getInstance()
                .userName(), iRslt, xresArray28_s);
        cmdSocket.sendCmdMsg(ausResp.toMsg());

        print(LogUtil.INF, "发送APP_AGENT_AUTH_RSP");
        BoxManager.getDefault().clearAuth();
        
        return true;
    }
    
    private synchronized void creatRtpSocket()
    {
        try
        {
            rtps = new DatagramSocket();
        }
        catch (SocketException e)
        {
            LogUtil.e(TAG, "error:" + e.toString());
        }
    }

    private synchronized void closeRtp()
    {        
//        if (rTerminal != null)
//        {
//            rTerminal.endRtpSession();
//            rTerminal = null;
//        }
//        if (rtps != null)
//        {
//            rtps.disconnect();
//            rtps.close();
//            rtps = null;
//        }
    	
    	if (mCall!=0 && HMediaManager.getHMedia()!=null) {
    		HMediaManager.getHMedia().DelCall(mCall);
			mCall = 0;
		}
    }
    
    private synchronized void hdlAppByeXiaohao()
    {
        print(LogUtil.INF, "发送APP_AGT_XH_BYE_IND");
        RentAgtByeInd aInd = new RentAgtByeInd(Engine.getInstance().userName());
        cmdSocket.sendCmdMsg(aInd.toMsg());

        closeRtp();
    }
    
    private void hdlXiaohaoByeApp()
    {
        print(LogUtil.INF, "XIAOHAO_APP_BYE_IND");
        Engine.getInstance().startNRService(mContext,
                MsgId.APP_XH_BE_HUNG_UP);

        closeRtp();
    }
    
    private void hdlCallingReqMsg(byte[] bMsg)
    {        
        creatRtpSocket();

        cmdSocket.sendCmdMsg(bMsg);
        print(LogUtil.INF, "发送APP_AGENT_CALLING_REQ");

        setState(UeState.MAIN_CALLING, UeState.SUB_CALLING_W_FMT_AUTH_REQ, 10);
    }
    
    private void hdlPagingMsg(byte[] bMsg)
    {
        print(LogUtil.INF, "收到AGENT_APP_PAGING_REQ");
        
        AgtAppGpnsInd gpns = new AgtAppGpnsInd(
                ByteUtil.subArray(bMsg, 8, bMsg.length-8));
        if (0x01 == gpns.type) // sms
        {
            AppAgtSmsRecvInd recvInd = new AppAgtSmsRecvInd(
                    gpns.usernameString);
            cmdSocket.sendCmdMsg(recvInd.toMsg());
            print(LogUtil.INF, "发送APP_AGENT_SMS_RECV_IND");

            setState(UeState.MAIN_RECV_SMS,
                    UeState.SUB_RECV_SMS_W_FMT_AUTH_REQ, 10);
        }
        else if (0x00 == gpns.type) // called
        {
            hdlCalled(gpns.usernameString);
        }
    }

    private void hdlCalled(String userName)
    {
        // bugfix:913
        if (NetUtil.getNetType(mContext) == 1)
        {
            print(LogUtil.WAR, "reject called in 2G,send APP_AGENT_BYE_IND");
            AppAgtByeInd aInd = new AppAgtByeInd(Engine.getInstance().userName());
            cmdSocket.sendCmdMsg(aInd.toMsg());
            return;
        }

        creatRtpSocket();

        AppAgtCalledInd calledInd = new AppAgtCalledInd(userName);
        cmdSocket.sendCmdMsg(calledInd.toMsg());
        
        print(LogUtil.INF, "发送APP_AGENT_CALLED_IND");

        setState(UeState.MAIN_CALLED, UeState.SUB_CALLED_W_FMT_AUTH_REQ, 10);
    }

    private void hdlAppByeMsg()
    {
        print(LogUtil.INF, "发送APP_AGENT_BYE_IND");
        AppAgtByeInd aInd = new AppAgtByeInd(Engine.getInstance().userName());
        cmdSocket.sendCmdMsg(aInd.toMsg());
        
        closeRtp();

        setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
    }

    private void hdlAgentByeMsg()
    {
        print(LogUtil.INF, "AGENT_APP_BYE_IND");
        Engine.getInstance().startNRService(mContext,
                MsgId.APP_BE_HUNG_UP);

        closeRtp();

        setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
    }

    private void recvSMS(AgtAppSmsRecvReq smsRecvReq)
    {
        String from = smsRecvReq.getSenderArray28_s().trim();
        LogUtil.e(TAG, "FROM=" + from);
        
        if (from == null || from.equals(""))
        {
            from = "未知号码";
        }
        
        mSmsReceiver.cacheToDB(from, smsRecvReq.getSmsByte(),
                smsRecvReq.getSmsByte().length);
    }

    private void recvMsgInd(AgtAppMsgInd msgInd)
    {
        String from = msgInd.getSender();
        LogUtil.e(TAG, "FROM=" + from);
        
        if (from == null || from.equals(""))
        {
            from = "未知号码";
        }
        
        String body = TimeUtil.getDateString(System.currentTimeMillis(),
                TimeUtil.dateFormat2) + " " + from + " " + msgInd.getMsg();
        
        Engine.getInstance().recvLxMsg(body, "旅信助手");
    }

    private Timer mTimer = null;

    private void setState(final int iMainState, final int iSubState, int delay)
    {
        // 如果上一个状态没有超时,则先关掉定时器
        if (mTimer != null)
        {
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }

        iUeMainState = iMainState;
        iUeSubState = iSubState;

        LogUtil.i(TAG, "To State[" + UeState.getMain(iUeMainState) + ", "
                + UeState.getSub(iUeSubState) + "]");

        if (delay == 0)
        {
            return;
        }

        mTimer = new Timer();
        mTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                byte[] msg = new byte[16];
                byte[] msgId = ByteUtil.getBytes(MsgId.APP_STATE_EXPIRE);
                byte[] len = ByteUtil.getBytes(8);
                byte[] main = ByteUtil.getBytes(iMainState);
                byte[] sub = ByteUtil.getBytes(iSubState);
                System.arraycopy(msgId, 0, msg, 0, 4);
                System.arraycopy(len, 0, msg, 4, 4);
                System.arraycopy(main, 0, msg, 8, 4);
                System.arraycopy(sub, 0, msg, 12, 4);
                procMsg(MsgId.APP_STATE_EXPIRE, msg);

                mTimer.cancel();
                mTimer = null;
            }
        }, delay * 1000);
    }

    private void hdlStateExpireMsg(byte[] msg, int iMsgLen)
    {
        if (iMsgLen < 16)
        {
            LogUtil.e(TAG, "StateExpireMsg Error Len[" + iMsgLen + "]");
            return;
        }

        int tempMain = ByteUtil.getInt(ByteUtil.subArray(msg, 8, 4));
        int tempSub = ByteUtil.getInt(ByteUtil.subArray(msg, 12, 4));
        if (tempMain != iUeMainState || tempSub != iUeSubState)
        {
            LogUtil.e(TAG, "过期的状态超时消息[" + UeState.getMain(tempMain) + ", "
                    + UeState.getSub(tempSub) + "]");
            return;
        }

        LogUtil.e(TAG, "超时状态[" + UeState.getMain(tempMain) + ", "
                + UeState.getSub(tempSub) + "]");

        switch (iUeMainState)
        {
            case UeState.MAIN_NULL:
            case UeState.MAIN_REGISTED:
                cmdSocket.stop();
                cmdSocket.start();
                break;
                
            case UeState.MAIN_REGISTING:
                Engine.getInstance().startNRService(mContext, 
                        MsgId.APP_REG_EXPIRE);
                iUeMainState = UeState.MAIN_NULL;
                iUeSubState = UeState.SUB_NULL;
                break;
                
            case UeState.MAIN_CALLING:
                iUeMainState = UeState.MAIN_REGISTED;
                iUeSubState = UeState.SUB_NULL;
                
                // 超时挂断
                Engine.getInstance().startNRService(mContext, 
                        MsgId.APP_BE_HUNG_UP);
                
                if (tempSub == UeState.SUB_CALLING_W_FMT_AUTH_REQ)
                {
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SHOW_ERR_MSG,
                            "通话连接未成功，请再次尝试(C1)");
                }
                
                if (tempSub == UeState.SUB_CALLING_W_SIM_AUTH_RSP)
                {
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SHOW_ERR_MSG,
                            "通话连接未成功，请再次尝试(D1)");
                }
                break;
                
            case UeState.MAIN_CALLED:
                iUeMainState = UeState.MAIN_REGISTED;
                iUeSubState = UeState.SUB_NULL;
                
                // 超时挂断
                Engine.getInstance().startNRService(mContext, 
                        MsgId.APP_BE_HUNG_UP);
                break;
                
            case UeState.MAIN_SEND_SMS:
                Engine.getInstance().startNRService(mContext, 
                        MsgId.APP_SEND_SMS_EXPIRE);
                iUeMainState = UeState.MAIN_REGISTED;
                iUeSubState = UeState.SUB_NULL;
                break;

            case UeState.MAIN_LAU:
                iUeMainState = UeState.SUB_NULL;
                iUeSubState = UeState.SUB_NULL;

                Engine.getInstance().startNRService(mContext, 
                        MsgId.APP_SHOW_REG_FAIL);
                break;
                
            case UeState.MAIN_VERIFY:
                iUeMainState = UeState.MAIN_REGISTED;
                iUeSubState = UeState.SUB_NULL;

                Engine.getInstance().broadcast(IAction.USER_VERIFY_FAIL);
                break;

            default:
                iUeMainState = UeState.MAIN_REGISTED;
                iUeSubState = UeState.SUB_NULL;
                break;
        }

        LogUtil.e(TAG, "To State[" + UeState.getMain(iUeMainState) + ", "
                + UeState.getSub(iUeSubState) + "]");
    }
    
    private void hdlVoipReqMsg(byte[] bMsg)
    {
        creatRtpSocket();

        AppAgtVoipAlert alert = new AppAgtVoipAlert(Engine
                .getInstance().userName());
        cmdSocket.sendCmdMsg(alert.toMsg());
        
        voipReq = new AgtAppVoipReq(
                ByteUtil.subArray(bMsg, 8, bMsg.length-8));
        
        // 此处弹出是否接听对话框
        Engine.getInstance().startNRService(mContext,
                MsgId.APP_CALLED_REQ, voipReq.getCaller());

        setState(UeState.MAIN_VOIP_CALLED,
                UeState.SUB_CALLED_W_UI_CALL_RSP, 0);
    }
    
    private void hdlAppVoipByeMsg()
    {
        print(LogUtil.INF, "发送APP_AGENT_VOIP_BYE_IND");
        AppAgtVoipByeInd aInd = new AppAgtVoipByeInd(Engine.getInstance()
                .userName(), "");
        cmdSocket.sendCmdMsg(aInd.toMsg());

        closeRtp();

        setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
    }

    private void hdlAgentVoipByeMsg()
    {
        print(LogUtil.INF, "AGENT_APP_VOIP_BYE_IND");
        Engine.getInstance().startNRService(mContext,
                MsgId.APP_BE_HUNG_UP);

        closeRtp();

        setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
    }
    
    private void hdlMsgOnMainVoipConnected(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iMsgId)
        {
            case MsgId.APP_STOP_RECORD:
                stopRecord(true);
                break;
                
            case MsgId.APP_START_RECORD:
                stopRecord(false);
                break;
                
            case MsgId.APP_HANG_UP:
                hdlAppVoipByeMsg();
                break;

            case MsgId.AGENT_APP_VOIP_BYE_IND:
                hdlAgentVoipByeMsg();
                break;

            default:
                print(LogUtil.ERR, iMsgId);
                break;
        }
    }
    
    private void stopRecord(boolean mute)
    {
        if (rTerminal != null)
        {
            rTerminal.setMuteState(mute);
        }
    }

    public void print(int iLevel, int iMsgId)
    {
        String strMain, strSub, msg;

        strMain = UeState.getMain(iUeMainState);
        strSub = UeState.getSub(iUeSubState);

        msg = "Recv " + MsgId.getMsgStr(iMsgId) + "["
                + String.format("0x%02X", iMsgId)
                + "] in state[" + strMain + ", " + strSub + "]";

        switch (iLevel)
        {
            case LogUtil.DBG:
                LogUtil.d(TAG, msg);
                break;
                
            case LogUtil.INF:
                LogUtil.i(TAG, msg);
                break;
                
            case LogUtil.WAR:
                LogUtil.w(TAG, msg);
                break;
                
            case LogUtil.ERR:
                LogUtil.e(TAG, msg);
                break;
                
            default:
                break;
        }
    }

    public void print(int iLevel, String msg)
    {
        String strMain, strSub;

        strMain = UeState.getMain(iUeMainState);
        strSub = UeState.getSub(iUeSubState);

        switch (iLevel)
        {
            case LogUtil.DBG:
                LogUtil.d(TAG, msg + " in state[" + strMain + ", " + strSub + "]");
                break;
                
            case LogUtil.INF:
                LogUtil.i(TAG, msg + " in state[" + strMain + ", " + strSub + "]");
                break;
                
            case LogUtil.WAR:
                LogUtil.w(TAG, msg + " in state[" + strMain + ", " + strSub + "]");
                break;
                
            case LogUtil.ERR:
                LogUtil.e(TAG, msg + " in state[" + strMain + ", " + strSub + "]");
                break;
                
            default:
                break;
        }
    }
}
