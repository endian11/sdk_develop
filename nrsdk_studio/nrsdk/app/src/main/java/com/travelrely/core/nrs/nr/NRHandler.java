package com.travelrely.core.nrs.nr;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.ble.BleMsgId;
import com.travelrely.core.nrs.ble.BleService;
import com.travelrely.core.nrs.nr.SmsReceiver.SmsCallback;
import com.travelrely.core.nrs.nr.msg.AgtAppAuthReq;
import com.travelrely.core.nrs.nr.msg.AgtAppCalledReq;
import com.travelrely.core.nrs.nr.msg.AgtAppCallingRsp;
import com.travelrely.core.nrs.nr.msg.AgtAppErrInd;
import com.travelrely.core.nrs.nr.msg.AgtAppGpnsInd;
import com.travelrely.core.nrs.nr.msg.AgtAppHwInfoRsp;
import com.travelrely.core.nrs.nr.msg.AgtAppLauRsp;
import com.travelrely.core.nrs.nr.msg.AgtAppMsgInd;
import com.travelrely.core.nrs.nr.msg.AgtAppRegRsp;
import com.travelrely.core.nrs.nr.msg.AgtAppSmsRecvReq;
import com.travelrely.core.nrs.nr.msg.AgtAppSmsSendRsp;
import com.travelrely.core.nrs.nr.msg.AgtAppStateErr;
import com.travelrely.core.nrs.nr.msg.AgtAppSysInfoInd;
import com.travelrely.core.nrs.nr.msg.AgtAppTcpRsp;
import com.travelrely.core.nrs.nr.msg.AgtAppUeAlert;
import com.travelrely.core.nrs.nr.msg.AgtAppUserVerifyRsp;
import com.travelrely.core.nrs.nr.msg.AgtAppVoipAlert;
import com.travelrely.core.nrs.nr.msg.AgtAppVoipReq;
import com.travelrely.core.nrs.nr.msg.AgtAppVoipRsp;
import com.travelrely.core.nrs.nr.msg.AppAgtAuthRsp;
import com.travelrely.core.nrs.nr.msg.AppAgtByeInd;
import com.travelrely.core.nrs.nr.msg.AppAgtCalledInd;
import com.travelrely.core.nrs.nr.msg.AppAgtCalledRsp;
import com.travelrely.core.nrs.nr.msg.AppAgtHwInfoReq;
import com.travelrely.core.nrs.nr.msg.AppAgtKeepAliveReq;
import com.travelrely.core.nrs.nr.msg.AppAgtLauReq;
import com.travelrely.core.nrs.nr.msg.AppAgtOnOffInd;
import com.travelrely.core.nrs.nr.msg.AppAgtRegReq;
import com.travelrely.core.nrs.nr.msg.AppAgtSimCardFeeRsp;
import com.travelrely.core.nrs.nr.msg.AppAgtSmsRecvInd;
import com.travelrely.core.nrs.nr.msg.AppAgtSmsRecvRsp;
import com.travelrely.core.nrs.nr.msg.AppAgtStoreKeyRsltInd;
import com.travelrely.core.nrs.nr.msg.AppAgtUserVerifyReq;
import com.travelrely.core.nrs.nr.msg.AppAgtVoipAlert;
import com.travelrely.core.nrs.nr.msg.AppAgtVoipByeInd;
import com.travelrely.core.nrs.nr.msg.AppAgtVoipRsp;
import com.travelrely.core.nrs.nr.msg.MsgId;
import com.travelrely.core.nrs.nr.msg.OctArray28_s;
import com.travelrely.core.nrs.nr.mt100.Common;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.nrs.nr.util.CallUtil;
import com.travelrely.core.nrs.nr.util.TextUtil;
import com.travelrely.core.nrs.nr.voice.rtpTerminal;
import com.travelrely.core.util.LogUtil;
import com.travelrely.core.util.NetUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.TimeUtil;
import com.travelrely.v2.NR.RayLib;
import com.travelrely.v2.Rent.msg.AgtRentCalledReq;
import com.travelrely.v2.Rent.msg.RentAgtByeInd;
import com.travelrely.v2.Rent.msg.RentAgtCalledRsp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.media.AudioManager;
public class NRHandler extends Handler
{
    private final static String TAG = NRHandler.class.getSimpleName();

    private Context mContext;
    
    private TcpClient cmdSocket;
    private Handler svrHandler;

    // 初始状态
    public int iUeMainState = UeState.MAIN_NULL;
    public int iUeSubState = UeState.SUB_NULL;
    
    private static rtpTerminal rTerminal = null;

    private static AgtAppCalledReq calledReq = null;
    private static AgtAppVoipAlert voipAlert = null;
    private static AgtAppVoipReq voipReq = null;
    private static AgtRentCalledReq xhCalledReq = null;
    
    private static AppAgtUserVerifyReq verifyReq = null;
    private static AgtAppUserVerifyRsp verifyRsp = null;
    
    
    private AudioManager mAudioManager;
    
    private SmsReceiver mSmsReceiver;
    private SmsCallback mSmsCallback = new SmsCallback()
    {
        @Override
        public void onSmsReceived(String peer, String sms)
        {
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
    
    public NRHandler(Looper looper)
    {
        super(looper);
    }
    
    public NRHandler(Looper looper, TcpClient cmdSocket)
    {
        super(looper);
        this.cmdSocket = cmdSocket;
    }

    public NRHandler(Looper looper, TcpClient cmdSocket, Context c, Handler h)
    {
        super(looper);
        
        this.mContext = c;
        this.cmdSocket = cmdSocket;
        this.svrHandler = h;
        
        mSmsReceiver = new SmsReceiver();
        mSmsReceiver.setSmsCallback(mSmsCallback);
    }
    
    private void notifyService(int iMsgId, byte[] msgContent)
    {
        if (msgContent == null)
        {
            msgContent = new byte[8];
            System.arraycopy(ByteUtil.getBytes(iMsgId), 0, msgContent, 0, 4);
        }

        Bundle bundle = new Bundle();
        bundle.putByteArray("cmd", msgContent);
        bundle.putInt("cmd_len", msgContent.length);

        Message msg = svrHandler.obtainMessage();
        msg.what = iMsgId;
        msg.setData(bundle);
        svrHandler.sendMessage(msg);
    }

    public synchronized void procMsg(int iMsgId, byte[] msgContent)
    {
        if (msgContent == null)
        {
            msgContent = new byte[8];
            System.arraycopy(ByteUtil.getBytes(iMsgId), 0, msgContent, 0, 4);
        }

        Bundle bundle = new Bundle();
        bundle.putByteArray("cmd", msgContent);
        bundle.putInt("cmd_len", msgContent.length);

        Message msg = obtainMessage();
        msg.setData(bundle);
        msg.what = iMsgId;
        sendMessage(msg);
    }

    @Override
    public void handleMessage(Message msg)
    {
        Bundle b = msg.getData();
        if (b == null)
        {
            return;
        }

        byte[] msgContent = b.getByteArray("cmd");
        int iCmdLen = b.getInt("cmd_len");
        
        //LogUtil.i(TAG, "Recv " + iCmdLen +"byte:"+ ByteUtil.toHexString(msgContent));

        CmdMsgEntry(msg.what, msgContent, iCmdLen);
    }
    
    private void CmdMsgEntry(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        // 状态无关的消息
        switch (iMsgId)
        {
            case MsgId.APP_INNER_RESET:
                Engine.getInstance().isEncNeed = true;
                Engine.getInstance().isEncEnable = false;
                setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                return;

            case MsgId.AGENT_APP_TCP_RSP:
                print_i("Recv AGENT_APP_TCP_RSP[0x%02X]", iMsgId);
                AgtAppTcpRsp rsp = new AgtAppTcpRsp(bMsg);
                if (rsp.getRslt() == 0)
                {
                    LogUtil.w(4, "NtoA001");
                    print_w("TCP重连成功");
                    notifyService(MsgId.AGENT_APP_TCP_RSP, null);
                }
                else
                {
                    print_e("TCP重连失败");
                }
                return;
                
            case MsgId.APP_AGENT_HW_INFO_REQ:
                print_i("Send APP_AGENT_HW_INFO_REQ[0x%02X]", iMsgId);
                LogUtil.i(4, "AtoN016");
                byte[] sn = Common.HexStringToBytes(SpUtil.getBoxSn());
                AppAgtHwInfoReq hwReq = new AppAgtHwInfoReq(Engine
                        .getInstance().userName(), sn);
                cmdSocket.sendCmdMsg(hwReq.toMsg());
                return;

            case MsgId.AGENT_APP_HW_INFO_RSP:
                print_i("Recv AGENT_APP_HW_INFO_RSP[0x%02X]", iMsgId);
                AgtAppHwInfoRsp hwRsp = new AgtAppHwInfoRsp(bMsg);
                if (hwRsp.isEncEnable())
                {
                    LogUtil.i(4, "NtoA016,1,%s", Common.bytesToHexString(hwRsp.getRandom()));
                    Engine.getInstance().isEncNeed = false;
                    Engine.getInstance().isEncEnable = true;
                    Engine.getInstance().startBleService(mContext,
                            BleMsgId.NR_BLE_RAND_IND, hwRsp.getRandom());
                }
                else
                {
                    LogUtil.i(4, "NtoA016,0");
                    Engine.getInstance().isEncNeed = false;
                    Engine.getInstance().isEncEnable = false;
                    Engine.getInstance().startBleService(mContext,
                            BleMsgId.NR_BLE_NO_RAND_IND);
                }
                return;

            case MsgId.APP_AGENT_DTMF_IND:
                cmdSocket.sendCmdMsg(bMsg);
                return;

            case MsgId.AGENT_APP_PAGING_REQ:
                if (iUeMainState == UeState.MAIN_VERIFY)
                {
                    hdlMsgOnMainVerify(iMsgId, bMsg, iMsgLen);
                }
                else
                {
                    hdlPagingMsg(bMsg);
                }
                return;

            case MsgId.AGENT_APP_SMS_RECV_REQ:
                if (iUeMainState == UeState.MAIN_VERIFY)
                {
                    hdlMsgOnMainVerify(iMsgId, bMsg, iMsgLen);
                }
                else
                {
                    print_i("Recv AGENT_APP_SMS_RECV_REQ[0x%02X]", iMsgId);
                    AgtAppSmsRecvReq smsRecvReq = new AgtAppSmsRecvReq(bMsg);
                    LogUtil.i(4, "NtoA011,%s", smsRecvReq.getSenderArray28_s().trim());
                    recvSMS(smsRecvReq);
                    AppAgtSmsRecvRsp aRsp = new AppAgtSmsRecvRsp(Engine
                            .getInstance().userName(), 0);
                    cmdSocket.sendCmdMsg(aRsp.toMsg());
                    print_i("Send APP_AGENT_SMS_RECV_RSP");
                    LogUtil.i(4, "AtoN011,0");
                }
                return;

            case MsgId.AGENT_APP_NEED_REG_IND:
                if (TextUtils.isEmpty(SpUtil.getBtAddr()))
                {
                    print_e("Recv AGENT_APP_NEED_REG_IND  but box not matched");
                    return;
                }
                print_w("Recv AGENT_APP_NEED_REG_IND[0x%02X]", iMsgId);
                LogUtil.i(4, "NtoA014");

                // 发起注册
                notifyService(MsgId.APP_SHOW_REG_FAIL, null);
                setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                Engine.getInstance().stopBleService(mContext);
                Engine.getInstance().startBleService(mContext,
                        BleMsgId.NR_BLE_READ_SIM_IND);
                return;

            case MsgId.AGENT_APP_MESSAGE_IND:
                print_w("Recv AGENT_APP_MESSAGE_IND[0x%02X]", iMsgId);
                AgtAppMsgInd ind = new AgtAppMsgInd(bMsg);
                recvMsgInd(ind);
                return;
                
            case MsgId.AGENT_APP_ERR_IND:
                print_w("Recv AGENT_APP_ERR_IND[0x%02X]", iMsgId);
                AgtAppErrInd stMsg = new AgtAppErrInd(bMsg);
                Engine.getInstance().startNRService(mContext,
                        MsgId.APP_SHOW_ERR_MSG, stMsg.getErrMsg());
                return;
                
            case MsgId.AGENT_APP_STATE_ERR:
                print_w("Recv AGENT_APP_STATE_ERR[0x%02X]", iMsgId);
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
                print_w("Recv AGENT_APP_SYSINFO_IND[0x%02X]", iMsgId);
                AgtAppSysInfoInd i = new AgtAppSysInfoInd(bMsg);
                Engine.getInstance().recvLxMsg(i.getMsg(), "旅信助手");
                return;
                
            case MsgId.AGENT_APP_SIM_CARD_FEE_IND:
                print_w("Recv AGENT_APP_SIM_CARD_FEE_IND[0x%02X]", iMsgId);
                notifyService(MsgId.NR_UI_SIM_FEE_ALERT, bMsg);
                return;
                
            case MsgId.APP_AGENT_SIM_CARD_FEE_RSP:
                AppAgtSimCardFeeRsp sfp = new AppAgtSimCardFeeRsp(Engine
                        .getInstance().userName(), 0);
                cmdSocket.sendCmdMsg(sfp.toMsg());
                return;
                
            case MsgId.APP_CLOSE_NR:
                print_w("Recv APP_CLOSE_NR[0x%02X]", iMsgId);
                LogUtil.i(4, "AtoN015,0");
                AppAgtOnOffInd off = new AppAgtOnOffInd(Engine
                        .getInstance().userName(), 0, 0);
                cmdSocket.sendCmdMsg(off.toMsg());
                setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                Engine.getInstance().broadcast(IAction.NR_CLOSE_SUCC);
                
                Engine.getInstance().isEncNeed = true;
                Engine.getInstance().isEncEnable = false;
                return;
                
            case MsgId.APP_OPEN_NR:
                print_w("Recv APP_OPEN_NR[0x%02X]", iMsgId);
                LogUtil.i(4, "AtoN015,1");
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
                print_i("Recv AGT_APP_XH_CALLED_REQ[0x%02X]", iMsgId);
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
                
                Log.e("nrworker1", "voice ctrl "+voiceCtrl);
                Log.e("nrworker1", "payLoadname "+payLoadname);
                Log.e("nrworker1", "payloadValue "+payloadValue);
                Log.e("nrworker1", "sample rate "+samples);
                Log.e("nrworker1", "local ipp "+localIp);
                Log.e("nrworker1", "local port "+localPort);
                Log.e("nrworker1", "remote ip "+remoteIp);
                Log.e("nrworker1", "remote port "+remotePort);
                Log.e("nrworker1", "isEncEnable is "+Engine.getInstance().isEncEnable);
                Log.e("nrworker1", "BleService.gStrAesKeyString is "+BleService.gStrAesKeyString);
//              Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG);
								
								mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			        	mAudioManager.setSpeakerphoneOn(false);
			        	CallUtil.enableSpeaker(mAudioManager,false);
                
			        	HMediaManager.mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
                        remoteIp, remotePort,
                        BleService.gStrAesKeyString, payloadValue, samples);
                
                mAudioManager.setSpeakerphoneOn(false);
			   				CallUtil.enableSpeaker(mAudioManager,false);
                HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "off");
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
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainNull(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        switch (iMsgId)
        {                
            case MsgId.APP_AGENT_REGISTER_REQ:
                startReg();
                break;
                
            case MsgId.APP_AGENT_KEEPALIVE_REQ:
                int type = NetUtil.getNetType(mContext);
                AppAgtKeepAliveReq keep = new AppAgtKeepAliveReq(
                        Engine.getInstance().userName(),
                        type, (int) (NetUtil.getMobileTtlBytes()/1024),0);
                cmdSocket.sendCmdMsg(keep.toMsg());
                print_w("Send APP_AGENT_KEEPALIVE_REQ");
                LogUtil.w(4, "AtoN002,"+type);
                
                setState(iUeMainState, iUeSubState, 10);
                break;
                
            case MsgId.AGENT_APP_KEEPALIVE_RSP:
                print_w("Recv AGENT_APP_KEEPALIVE_RSP[0x%02X]", iMsgId);
                LogUtil.w(4, "NtoA002");
                setState(iUeMainState, iUeSubState, 0);
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                int type = NetUtil.getNetType(mContext);
                AppAgtKeepAliveReq keep = new AppAgtKeepAliveReq(
                        Engine.getInstance().userName(),
                        type,
                        (int) (NetUtil.getMobileTtlBytes()/1024),connRslt);
                cmdSocket.sendCmdMsg(keep.toMsg());
                print_w("Send APP_AGENT_KEEPALIVE_REQ");
                LogUtil.w(4, "AtoN002,"+type);
                setState(iUeMainState, iUeSubState, 10);
                break;
                
            case MsgId.AGENT_APP_KEEPALIVE_RSP: 
                print_w("Recv AGENT_APP_KEEPALIVE_RSP[0x%02X]", iMsgId);
                LogUtil.w(4, "NtoA002");
                setState(iUeMainState, iUeSubState, 0);
                break;
                
            case MsgId.APP_AGENT_STORE_KEY_RSLT_IND:
                AppAgtStoreKeyRsltInd ind = new AppAgtStoreKeyRsltInd(
                        Engine.getInstance().userName(), 1);
                cmdSocket.sendCmdMsg(ind.toMsg());
                print_w("Send APP_AGENT_STORE_KEY_RSLT_IND");
                break;
                
            case MsgId.AGENT_APP_LAU_IND:
                print_w("Recv AGENT_APP_LAU_IND[0x%02X]", iMsgId);
                LogUtil.i(4, "NtoA013");
                AppAgtLauReq lauReq = new AppAgtLauReq(Engine.getInstance()
                        .userName());
                cmdSocket.sendCmdMsg(lauReq.toMsg());
                print_w("Send APP_AGENT_LAU_REQ");
                LogUtil.i(4, "AtoN012");
                setState(UeState.MAIN_LAU, UeState.SUB_LAU_W_FMT_AUTH_REQ, 10);
                break;

            case MsgId.APP_AGENT_CALLING_REQ:
                hdlCallingReqMsg(bMsg);
                break;
                
            case MsgId.APP_AGENT_LSMS_SEND_REQ:
                cmdSocket.sendCmdMsg(bMsg);
                print_i("Send APP_AGENT_LSMS_SEND_REQ");
                LogUtil.i(4, "AtoN010,%s", new String(ByteUtil.subArray(bMsg, 44, 28)));
                setState(UeState.MAIN_SEND_SMS,
                        UeState.SUB_SEND_SMS_W_FMT_AUTH_REQ, 10);
                break;
                
            case MsgId.AGENT_APP_LSMS_AUTH_REQ:
                print_i("Recv AGENT_APP_LSMS_AUTH_REQ[0x%02X]", iMsgId);
                AgtAppAuthReq authReq = new AgtAppAuthReq(bMsg);
                btBoxAuth(authReq);
                break;
                
            case MsgId.APP_AGENT_AUTH_RSP:
                byte[] sres = BoxManager.getDefault().getAuthRslt();
                OctArray28_s xresArray28_s = new OctArray28_s(sres.length, sres);
                AppAgtAuthRsp ausResp = new AppAgtAuthRsp(Engine.getInstance()
                        .userName(), 0, xresArray28_s);
                cmdSocket.sendCmdMsg(ausResp.toMsg());

                print_i("Send APP_AGENT_AUTH_RSP");
                BoxManager.getDefault().clearAuth();
                break;

            case MsgId.AGENT_APP_VOIP_REQ:
                hdlVoipReqMsg(bMsg);
                break;

            case MsgId.APP_AGENT_USER_VERIFY_REQ:
                byte[] imsi = BoxManager.getDefault().getImsi();
                byte[] random = TextUtil.getRandomNumSeries(12);
                String user = Engine.getInstance().userName();
                LogUtil.i(TAG, ByteUtil.toHexString(random));
                verifyReq = new AppAgtUserVerifyReq(user, imsi, random);

                cmdSocket.sendCmdMsg(verifyReq.toMsg());
                print_i("Send APP_AGENT_USER_VERIFY_REQ");
                setState(UeState.MAIN_VERIFY, UeState.SUB_VERIFY_W_RSP, 10);
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_REGISTING,
                            UeState.SUB_REGISTING_W_FMT_REG_RSP, 20);
                }
                else
                {
                    setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                    print_e("鉴权参数处理失败");
                    notifyService(MsgId.APP_REG_FAIL, null);
                }
                break;

            case UeState.SUB_REGISTING_W_FMT_REG_RSP:
                if (iMsgId != MsgId.AGENT_APP_REGISTER_RSP)
                {
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                print_i("Recv AGENT_APP_REGISTER_RSP[0x%02X]", iMsgId);

                AgtAppRegRsp regRsp = new AgtAppRegRsp(bMsg);
                if (regRsp.result != 0)
                {
                    setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                    print_e("Regist Failed!!!");
                    LogUtil.w(4, "NtoA003,"+regRsp.result);
                    notifyService(MsgId.APP_REG_FAIL, null);
                    return;
                }

                notifyService(MsgId.APP_REG_SUCC, null);
                setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                print_i("Regist Success[%s]", regRsp.getTimeStamp());
                LogUtil.w(4, "NtoA003,0");
                
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
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_i("Recv AGENT_APP_VOIP_ALERT[0x%02X]", iMsgId);
                    voipAlert = new AgtAppVoipAlert(bMsg);

                    setState(UeState.MAIN_VOIP_CALLING,
                            UeState.SUB_VOIP_CALLING_W_FMT_CALL_RSP, 60);

                    // 通知响铃音
                    notifyService(MsgId.APP_PLAY_RINGTONE, null);
                }
                
                if (iMsgId == MsgId.AGENT_APP_CALLING_RSP)
                {
                    print_w("Recv AGENT_APP_CALLING_RSP[0x%02X]", iMsgId);
                    AgtAppCallingRsp CallingRsp = new AgtAppCallingRsp(bMsg);
                    if (CallingRsp.result != 0)
                    {
                        notifyService(MsgId.AGENT_APP_BYE_IND, null);
                        setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                    }
                }
                break;

            case UeState.SUB_CALLING_W_SIM_AUTH_RSP:
                if (iMsgId != MsgId.APP_AGENT_AUTH_RSP)
                {
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                            "请检查蓝牙配件是否有电，SIM卡是否放好");
                }
                break;

            case UeState.SUB_CALLING_W_FMT_ALERT:
                if (iMsgId != MsgId.AGENT_APP_UE_ALERT)
                {
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                // 将本地的RTP语音地址告诉FEMTO(APP_AGENT_RTP_IND)
                print_i("Recv AGENT_APP_UE_ALERT[0x%02X]", iMsgId);
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
                
                	
                Log.e("nrworker3", "voice ctrl "+voiceCtrl);
                Log.e("nrworker3", "payLoadname "+payLoadname);
                Log.e("nrworker3", "payloadValue "+payloadValue);
                Log.e("nrworker3", "sample rate "+samples);
                Log.e("nrworker3", "local ipp "+localIp);
                Log.e("nrworker3", "local port "+localPort);
                Log.e("nrworker3", "remote ip "+remoteIp);
                Log.e("nrworker3", "remote port "+remotePort);
                Log.e("nrworker3", "isEncEnable is "+Engine.getInstance().isEncEnable);
                Log.e("nrworker3", "BleService.gStrAesKeyString is "+BleService.gStrAesKeyString);
//              Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG);
							
				mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			        	mAudioManager.setSpeakerphoneOn(false);
			        	CallUtil.enableSpeaker(mAudioManager,false);
                //主叫通路 
			        	HMediaManager.mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
                        remoteIp, remotePort,
                        BleService.gStrAesKeyString, payloadValue, samples);
                if (HMediaManager.mCall !=0 && HMediaManager.loudspeaker_st ==1){
                	Log.e("CallActivity.mCall(nrhandler)--- ", HMediaManager.mCall+"");
//                	CallActivity.loudspeaker_st = 1;
                	mAudioManager.setSpeakerphoneOn(true);
                	CallUtil.enableSpeaker(mAudioManager,true);
                	HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "on");
                	
                }else{
                	
                	mAudioManager.setSpeakerphoneOn(false);
                	CallUtil.enableSpeaker(mAudioManager,false);
                	HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "off");
                }
                
//              Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG).show();
                setState(UeState.MAIN_CALLING,
                        UeState.SUB_CALLING_W_FMT_CALL_RSP, 0);
                break;

            case UeState.SUB_CALLING_W_FMT_CALL_RSP:
                if (iMsgId != MsgId.AGENT_APP_CALLING_RSP)
                {
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                print_i("Recv AGENT_APP_CALLING_RSP[0x%02X]", iMsgId);
                AgtAppCallingRsp rsp = new AgtAppCallingRsp(bMsg);
                if (rsp.result == 0)
                {
                    // 主叫被接通
                    notifyService(MsgId.APP_CALLING_CONNECTED, null);

                    setState(UeState.MAIN_CALLING_CONNECTED,
                            UeState.SUB_CONNECT_W_APP_RECV_SMS_RSP, 0);

                    print_i("Calling Success!!!");
                    LogUtil.i(4, "NtoA005,0");
                }
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainCalled(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        if (iMsgId == MsgId.APP_HANG_UP)
        {
            print_i("Send APP_AGENT_CALLED_RSP");
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                print_i("Recv AGENT_APP_CALLED_REQ[0x%02X]", iMsgId);
                calledReq = new AgtAppCalledReq(bMsg);
                
                LogUtil.i(4, "NtoA007," + calledReq.getCaller());

                // 此处弹出是否接听对话框
                Engine.getInstance().startNRService(mContext,
                        MsgId.APP_CALLED_REQ, calledReq.getCaller());

                setState(UeState.MAIN_CALLED,
                        UeState.SUB_CALLED_W_UI_CALL_RSP, 50);
                break;

            case UeState.SUB_CALLED_W_UI_CALL_RSP:
                if (iMsgId != MsgId.APP_ANSWER)
                {
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                print_i("Send APP_AGENT_CALLED_RSP");
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

                
                Log.e("nrworker2", "voice ctrl "+voiceCtrl);
                Log.e("nrworker2", "payLoadname "+payLoadname);
                Log.e("nrworker2", "payloadValue "+payloadValue);
                Log.e("nrworker2", "sample rate "+samples);
                Log.e("nrworker2", "local ipp "+localIp);
                Log.e("nrworker2", "local port "+localPort);
                Log.e("nrworker2", "remote ip "+remoteIp);
                Log.e("nrworker2", "remote port "+remotePort);
                Log.e("nrworker2", "isEncEnable is "+Engine.getInstance().isEncEnable);
                Log.e("nrworker2", "BleService.gStrAesKeyString is "+BleService.gStrAesKeyString);
//              Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG);
                
				mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			        	mAudioManager.setSpeakerphoneOn(false);
			        	CallUtil.enableSpeaker(mAudioManager,false);
			        	
                //被叫通路
                HMediaManager.mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
                        remoteIp, remotePort,
                        BleService.gStrAesKeyString, payloadValue, samples);
                
                mAudioManager.setSpeakerphoneOn(false);
			   				CallUtil.enableSpeaker(mAudioManager,false);
                HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "off");
                
                setState(UeState.MAIN_CALLED_CONNECTED,
                        UeState.SUB_CONNECT_W_APP_RECV_SMS_RSP, 0);
                
                print_i("Called Success!!!");
                LogUtil.i(4, "AtoN007,0");
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                print_i("Recv AGENT_APP_SMS_SEND_RSP[0x%02X]", iMsgId);
                AgtAppSmsSendRsp rsp = new AgtAppSmsSendRsp(bMsg);
                if (rsp.result != 0)
                {
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SEND_SMS_FAIL);
                    print_e("send SMS failed[%d]", rsp.result);
                }
                else
                {
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SEND_SMS_SUCC);
                    print_i("send SMS success");
                }
                LogUtil.i(4, "NtoA010,%d", rsp.result);

                setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                }
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                break;
        }
    }

    private void hdlMsgOnMainVerify(int iMsgId, byte[] bMsg, int iMsgLen)
    {
        if (iMsgId == MsgId.AGENT_APP_SMS_RECV_REQ)
        {
            print_i("Recv AGENT_APP_SMS_RECV_REQ[0x%02X]", iMsgId);
            AgtAppSmsRecvReq smsRecvReq = new AgtAppSmsRecvReq(bMsg);
            LogUtil.i(4, "NtoA011,%s", smsRecvReq.getSenderArray28_s().trim());
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
                        print_i("USER verify success");
                        
                        AppAgtSmsRecvRsp aRsp = new AppAgtSmsRecvRsp(Engine
                                .getInstance().userName(), 0);
                        cmdSocket.sendCmdMsg(aRsp.toMsg());
                        print_i("Send APP_AGENT_SMS_RECV_RSP");

                        setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                        
                        Engine.getInstance().broadcast(IAction.USER_VERIFY_SUCC, 0);
                        return;
                    }
                    else
                    {
                        recvSMS(smsRecvReq);// recv sms as normal
                    }
                }
                else
                {
                    print_e(ByteUtil.toHexString(sms));
                }
            }
            else
            {
                recvSMS(smsRecvReq);// recv sms as normal
            }
        
            AppAgtSmsRecvRsp aRsp = new AppAgtSmsRecvRsp(Engine
                    .getInstance().userName(), 0);
            cmdSocket.sendCmdMsg(aRsp.toMsg());
            print_i("Send APP_AGENT_SMS_RECV_RSP");
            LogUtil.i(4, "AtoN011,0");
            setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
            return;
        }

        switch (iUeSubState)
        {
            case UeState.SUB_VERIFY_W_RSP:
                if (iMsgId != MsgId.AGENT_APP_USER_VERIFY_RSP)
                {
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }
                
                print_i("Recv AGENT_APP_USER_VERIFY_RSP[0x%02X]", iMsgId);
                
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }
                print_i("Recv AGENT_APP_PAGING_REQ[0x%02X]", iMsgId);
                
                AgtAppGpnsInd gpns = new AgtAppGpnsInd(bMsg);
                if (0x01 == gpns.type) // sms
                {
                    AppAgtSmsRecvInd recvInd = new AppAgtSmsRecvInd(
                            Engine.getInstance().userName());
                    cmdSocket.sendCmdMsg(recvInd.toMsg());
                    print_i("Send APP_AGENT_SMS_RECV_IND");

                    setState(UeState.MAIN_VERIFY,
                            UeState.SUB_RECV_SMS_W_FMT_AUTH_REQ, 10);
                }
                else
                {
                    print_e("收到无效的paging");
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_VERIFY,
                            UeState.SUB_RECV_SMS_W_FMT_RECV_REQ, 18);
                }
                break;

            case UeState.SUB_RECV_SMS_W_FMT_RECV_REQ:
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }
                if (hdlAuthRsltMsg())
                {
                    setState(UeState.MAIN_LAU,
                            UeState.SUB_LAU_W_FMT_RSP, 20);
                }
                break;

            case UeState.SUB_LAU_W_FMT_RSP:
                if (iMsgId != MsgId.AGENT_APP_LAU_RSP)
                {
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                print_i("Recv AGENT_APP_LAU_RSP[0x%02X]", iMsgId);
                AgtAppLauRsp rsp = new AgtAppLauRsp(bMsg);
                if (rsp.result == 0)
                {
                    print_i("LAU Success");
                    setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
                    notifyService(MsgId.APP_SHOW_REG_SUCC, null);
                }
                else
                {
                    print_e("LAU Failed");
                    setState(UeState.MAIN_NULL, UeState.SUB_NULL, 0);
                    notifyService(MsgId.APP_SHOW_REG_FAIL, null);
                }
                LogUtil.i(4, "NtoA012,%d", rsp.result);
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                print_i("Recv AGENT_APP_VOIP_RSP[0x%02X]", iMsgId);
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
                    

                    Log.e("nrworker5", "voice ctrl "+voiceCtrl);
                    Log.e("nrworker5", "payLoadname "+payLoadname);
                    Log.e("nrworker5", "payloadValue "+payloadValue);
                    Log.e("nrworker5", "sample rate "+samples);
                    Log.e("nrworker5", "local ipp "+localIp);
                    Log.e("nrworker5", "local port "+localPort);
                    Log.e("nrworker5", "remote ip "+remoteIp);
                    Log.e("nrworker5", "remote port "+remotePort);
                    Log.e("nrworker5", "isEncEnable is "+Engine.getInstance().isEncEnable);
                    Log.e("nrworker5", "BleService.gStrAesKeyString is "+BleService.gStrAesKeyString);
//                  Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG);
                            
                mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			        	mAudioManager.setSpeakerphoneOn(false);
			        	CallUtil.enableSpeaker(mAudioManager,false);
                
                HMediaManager.mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
                        remoteIp, remotePort,
                        BleService.gStrAesKeyString, payloadValue, samples);
                
                mAudioManager.setSpeakerphoneOn(false);
			   				CallUtil.enableSpeaker(mAudioManager,false);
                HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "off");
                
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
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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
                    print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                    return;
                }

                print_i("Send APP_AGENT_VOIP_RSP");
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
                
                Log.e("nrworker4", "voice ctrl "+voiceCtrl);
                Log.e("nrworker4", "payLoadname "+payLoadname);
                Log.e("nrworker4", "payloadValue "+payloadValue);
                Log.e("nrworker4", "sample rate "+samples);
                Log.e("nrworker4", "local ipp "+localIp);
                Log.e("nrworker4", "local port "+localPort);
                Log.e("nrworker4", "remote ip "+remoteIp);
                Log.e("nrworker4", "remote port "+remotePort);
                Log.e("nrworker4", "isEncEnable is "+Engine.getInstance().isEncEnable);
                Log.e("nrworker4", "BleService.gStrAesKeyString is "+BleService.gStrAesKeyString);
//              Toast.makeText(CallActivity.Instance, "femeto port:"+remotePort, Toast.LENGTH_LONG);

                
                
                mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			        	mAudioManager.setSpeakerphoneOn(false);
			        	CallUtil.enableSpeaker(mAudioManager,false);
                
			    HMediaManager.mCall = HMediaManager.getHMedia().NewDirectCall(voiceCtrl, localIp, localPort,
                        remoteIp, remotePort,
                        BleService.gStrAesKeyString, payloadValue, samples);
                
                mAudioManager.setSpeakerphoneOn(false);
			   				CallUtil.enableSpeaker(mAudioManager,false);
                HMediaManager.getHMedia().OptWebrtcCall(HMediaManager.mCall, 1 /* HMC_APFlags*/, "off");
                
                setState(UeState.MAIN_VOIP_CALLED_CONNECTED,
                        UeState.SUB_CONNECT_W_APP_RECV_SMS_RSP, 0);

                LogUtil.i(TAG, "被叫成功");
                break;

            default:
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
                break;
        }
    }
    
    private void startReg()//开始登网注册
    {
        byte[] imsi = BoxManager.getDefault().getImsi();//获取imsi
        String smc = BoxManager.getDefault().getSmsp();
        String phone = BoxManager.getDefault().getMsisdn();
        int iNetType = NetUtil.getNetType(mContext);

        if (null == imsi || imsi.length == 0)
        {
            print_e("imsi == null");
            return;
        }

        //String imei = DeviceInfo.getInstance(mContext).imei;

        AppAgtRegReq regReq = new AppAgtRegReq(Engine.getInstance().userName(),
                imsi, smc, phone, iNetType);
        cmdSocket.sendCmdMsg(regReq.toMsg());
        print_i("Send APP_AGENT_REGISTER_REQ[0x%02X]", MsgId.APP_AGENT_REGISTER_REQ);
        LogUtil.w(4, "AtoN003");

        setState(UeState.MAIN_REGISTING, UeState.SUB_REGISTING_W_FMT_AUTH_REQ, 10);
    }

    /**处理鉴权请求
     * @param iMsgId
     * @param bMsg
     * @return
     */
    private boolean hdlAuthReqMsg(int iMsgId, byte[] bMsg)
    {
        if (iMsgId != MsgId.AGENT_APP_AUTH_REQ)
        {
            print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
            return false;
        }

        print_i("Recv AGENT_APP_AUTH_REQ[0x%02X]", iMsgId);
        AgtAppAuthReq authReq = new AgtAppAuthReq(bMsg);
        return btBoxAuth(authReq);
    }

    private boolean btBoxAuth(final AgtAppAuthReq authReq)
    {        
        if (authReq.is2G())
        {
            BoxManager.getDefault().setAuthParam(
                    authReq.randArray, null);
            LogUtil.i(4, "NtoA004,%s",Common.bytesToHexString(authReq.randArray));
        }
        else
        {
            BoxManager.getDefault().setAuthParam(
                    authReq.randArray, authReq.autnArray);
            LogUtil.i(4, "NtoA004,%s,%s",Common.bytesToHexString(authReq.randArray),
                    Common.bytesToHexString(authReq.autnArray));
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
            print_e("没有得到鉴权结果");
            sres = new byte[]{0x00, 0x00, 0x00, 0x00};
        }
        OctArray28_s xresArray28_s = new OctArray28_s(sres.length, sres);
        AppAgtAuthRsp ausResp = new AppAgtAuthRsp(Engine.getInstance()
                .userName(), iRslt, xresArray28_s);
        cmdSocket.sendCmdMsg(ausResp.toMsg());

        print_i("Send APP_AGENT_AUTH_RSP[0x%02X]", MsgId.APP_AGENT_AUTH_RSP);
        LogUtil.i(4, "AtoN004,0,%s", Common.bytesToHexString(sres));
        BoxManager.getDefault().clearAuth();
        
        return true;
    }
    
    private synchronized void creatRtpSocket()
    {}

    private synchronized void closeRtp() {
        if (HMediaManager.mCall!=0 && HMediaManager.getHMedia()!=null) {
            HMediaManager.getHMedia().DelCall(HMediaManager.mCall);
            HMediaManager.mCall = 0;
            HMediaManager.loudspeaker_st=0;
        }
    }
    
    private synchronized void hdlAppByeXiaohao() {
        print_i("Send APP_AGT_XH_BYE_IND");
        RentAgtByeInd aInd = new RentAgtByeInd(Engine.getInstance().userName());
        cmdSocket.sendCmdMsg(aInd.toMsg());

        closeRtp();
    }
    
    private void hdlXiaohaoByeApp() {
        print_i("Recv XIAOHAO_APP_BYE_IND");
        Engine.getInstance().startNRService(mContext,
                MsgId.APP_XH_BE_HUNG_UP);

        closeRtp();
    }
    
    private void hdlCallingReqMsg(byte[] bMsg) {
        creatRtpSocket();

        cmdSocket.sendCmdMsg(bMsg);
        print_i("Send APP_AGENT_CALLING_REQ");
        LogUtil.i(4, "AtoN005,%s", new String(ByteUtil.subArray(bMsg, 44, 28)));

        setState(UeState.MAIN_CALLING, UeState.SUB_CALLING_W_FMT_AUTH_REQ, 10);
    }
    
    private void hdlPagingMsg(byte[] bMsg)
    {
        print_i("Recv AGENT_APP_PAGING_REQ");
        
        AgtAppGpnsInd gpns = new AgtAppGpnsInd(bMsg);
        if (0x01 == gpns.type) // sms
        {
            AppAgtSmsRecvInd recvInd = new AppAgtSmsRecvInd(
                    Engine.getInstance().userName());
            cmdSocket.sendCmdMsg(recvInd.toMsg());
            print_i("Send APP_AGENT_SMS_RECV_IND");

            setState(UeState.MAIN_RECV_SMS,
                    UeState.SUB_RECV_SMS_W_FMT_AUTH_REQ, 10);
        }
        else if (0x00 == gpns.type) // called
        {
            hdlCalled();
        }
        LogUtil.i(4, "NtoA006,%d", gpns.type);
    }

    private void hdlCalled()
    {
        // bugfix:913
        if (NetUtil.getNetType(mContext) == 1)
        {
            print_w("reject called in 2G,send APP_AGENT_BYE_IND");
            AppAgtByeInd aInd = new AppAgtByeInd(Engine.getInstance().userName());
            cmdSocket.sendCmdMsg(aInd.toMsg());
            return;
        }

        creatRtpSocket();

        AppAgtCalledInd calledInd = new AppAgtCalledInd(Engine.getInstance().userName());
        cmdSocket.sendCmdMsg(calledInd.toMsg());
        
        print_i("Send APP_AGENT_CALLED_IND");

        setState(UeState.MAIN_CALLED, UeState.SUB_CALLED_W_FMT_AUTH_REQ, 10);
    }

    private void hdlAppByeMsg()
    {
        print_i("Send APP_AGENT_BYE_IND");
        LogUtil.i(4, "AtoN008");
        AppAgtByeInd aInd = new AppAgtByeInd(Engine.getInstance().userName());
        cmdSocket.sendCmdMsg(aInd.toMsg());
        
        closeRtp();

        setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
    }

    private void hdlAgentByeMsg()
    {
        print_i("Recv AGENT_APP_BYE_IND");
        LogUtil.i(4, "NtoA009");
        Engine.getInstance().startNRService(mContext, MsgId.APP_BE_HUNG_UP);

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

    private void setState(final int iMainState, final int iSubState, int delay)
    {
        // 如果上一个状态没有超时,则先关掉定时器
        removeMessages(MsgId.APP_STATE_EXPIRE);

        iUeMainState = iMainState;
        iUeSubState = iSubState;

        LogUtil.i(TAG, "Switch To State[" + UeState.getMain(iUeMainState) + ", "
                + UeState.getSub(iUeSubState) + "]");

        if (delay == 0)
        {
            return;
        }

        byte[] msgContent = new byte[16];
        byte[] msgId = ByteUtil.getBytes(MsgId.APP_STATE_EXPIRE);
        byte[] len = ByteUtil.getBytes(8);
        byte[] main = ByteUtil.getBytes(iMainState);
        byte[] sub = ByteUtil.getBytes(iSubState);
        System.arraycopy(msgId, 0, msgContent, 0, 4);
        System.arraycopy(len, 0, msgContent, 4, 4);
        System.arraycopy(main, 0, msgContent, 8, 4);
        System.arraycopy(sub, 0, msgContent, 12, 4);
        
        Bundle bundle = new Bundle();
        bundle.putByteArray("cmd", msgContent);
        bundle.putInt("cmd_len", msgContent.length);

        Message msg = obtainMessage();
        msg.what = MsgId.APP_STATE_EXPIRE;
        msg.setData(bundle);
        sendMessageDelayed(msg, delay * 1000);
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
                cmdSocket.reset();
                break;
                
            case UeState.MAIN_REGISTING:
                if (tempSub == UeState.SUB_REGISTING_W_FMT_REG_RSP)
                {
                    iUeMainState = UeState.MAIN_REGISTED;
                    iUeSubState = UeState.SUB_NULL;
                    return;
                }
                notifyService(MsgId.APP_REG_EXPIRE, null);
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
                            "网络连接不稳定，请您检查网络连接后再次尝试");
                }
                
                if (tempSub == UeState.SUB_CALLING_W_SIM_AUTH_RSP)
                {
                    Engine.getInstance().startNRService(mContext,
                            MsgId.APP_SHOW_ERR_MSG,
                            "请检查蓝牙配件是否有电，SIM卡是否放好");
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
                if (tempSub == UeState.SUB_LAU_W_FMT_RSP)
                {
                    iUeMainState = UeState.MAIN_REGISTED;
                    iUeSubState = UeState.SUB_NULL;
                    return;
                }
                iUeMainState = UeState.MAIN_NULL;
                iUeSubState = UeState.SUB_NULL;

                notifyService(MsgId.APP_SHOW_REG_FAIL, null);
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
        print_i("Send APP_AGENT_VOIP_BYE_IND");
        AppAgtVoipByeInd aInd = new AppAgtVoipByeInd(Engine.getInstance()
                .userName(), "");
        cmdSocket.sendCmdMsg(aInd.toMsg());

        closeRtp();

        setState(UeState.MAIN_REGISTED, UeState.SUB_NULL, 0);
    }

    private void hdlAgentVoipByeMsg()
    {
        print_i("Recv AGENT_APP_VOIP_BYE_IND");
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
                print_e("Recv %s[%d]", MsgId.getMsgStr(iMsgId), iMsgId);
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

    
    public void print_d(String format, Object... args)
    {
        print(LogUtil.DBG, String.format(format, args));
    }
    
    public void print_i(String format, Object... args)
    {
        print(LogUtil.INF, String.format(format, args));
    }
    
    public void print_w(String format, Object... args)
    {
        print(LogUtil.WAR, String.format(format, args));
    }
    
    public void print_e(String format, Object... args)
    {
        print(LogUtil.ERR, String.format(format, args));
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
