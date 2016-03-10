package com.travelrely.v2.Rent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.nrs.nr.voice.rtpTerminal;
import com.travelrely.v2.Rent.msg.AgtRentCalledReq;
import com.travelrely.v2.Rent.msg.RentAgtByeInd;
import com.travelrely.v2.Rent.msg.RentAgtCalledRsp;
import com.travelrely.v2.Rent.msg.RentMsgId;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class RentWorker
{
    private static final String TAG = "RentWorker";
    
    private static String AGENT_IP = "115.29.43.99";
    private static final int AGENT_PORT = 30003;
    
    private Handler mLocHandler, mRemoteHandler;
    
    private static DatagramSocket rtps = null;
    private static int cpPort2 = 7635;
    private static rtpTerminal rTerminal = null;

    private static AgtRentCalledReq calledReq = null;

    public RentWorker(Context context, Handler handler)
    {
        this.mRemoteHandler = handler;

        new Worker("RentWorkerThread").start();
    }

    private class Worker extends Thread
    {
        public Worker(String name)
        {
            setName(name);
        }

        @Override
        public void run()
        {
            Looper.prepare();
            mLocHandler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    Bundle b = msg.getData();
                    if (b == null)
                    {
                        return;
                    }

                    byte[] msgContent = b.getByteArray("cmd");
                    int iMsgId = msgContent[0];
                    MsgEntry(iMsgId, msgContent, msgContent.length);
                }
            };
            Looper.loop();
        }
    }
    
    public synchronized void recvMsg(int iMsgId, byte[] msgContent)
    {
        if (mLocHandler == null)
        {
            Log.e(TAG, "mLocHandler == null");
            return;
        }
        if (msgContent == null)
        {
            msgContent = ByteUtil.getBytes(iMsgId);
        }

        Bundle bundle = new Bundle();
        bundle.putByteArray("cmd", msgContent);

        Message msg = new Message();
        msg.setData(bundle);
        mLocHandler.sendMessage(msg);
    }
    
    private synchronized void sendMsg(int iMsgId, byte[] msgContent)
    {
        if (mRemoteHandler == null)
        {
            Log.e(TAG, "mRemoteHandler == null");
            return;
        }
        if (msgContent == null)
        {
            msgContent = ByteUtil.getBytes(iMsgId);
        }

        Bundle bundle = new Bundle();
        bundle.putByteArray("cmd", msgContent);

        Message msg = new Message();
        msg.setData(bundle);
        mRemoteHandler.sendMessage(msg);
    }
    
    private synchronized void MsgEntry(int iMsgId, byte[] msg, int iMsgLen)
    {
        switch (iMsgId)
        {
            case RentMsgId.APP_CALLED_REQ:
                calledReq = new AgtRentCalledReq(msg);
                //sendMsg(RentMsgId.APP_CALLED_REQ, calledReq.toByte());
                hdlCalledReqMsg();
                break;
                
            case RentMsgId.APP_ANSWER:
                RentAgtCalledRsp Rsp = new RentAgtCalledRsp(Engine.getInstance()
                        .userName(), 0);
                sendCmdToAgent(Rsp.toMsg());

                String strIp = calledReq.getIp();
                int port = calledReq.getPort();
                Log.i(TAG, "IP=" + strIp + ":" + port);

                rTerminal = new rtpTerminal();
                rTerminal.startRtpSession(strIp, port, cpPort2, rtps);
                break;
                
            case RentMsgId.APP_HANG_UP:
                hdlAppByeMsg();
                break;
                
            case RentMsgId.APP_BE_HUNG_UP:
                hdlAgentByeMsg();
                break;

            default:
                Log.e(TAG, "[" + iMsgId + "]");
                break;
        }
    }
    
    private synchronized void hdlCalledReqMsg()
    {
        try
        {
            rtps = new DatagramSocket();
        }
        catch (SocketException e)
        {
            Log.e(TAG, "error:" + e.toString());
        }
    }
    
    private synchronized void hdlAppByeMsg()
    {
        RentAgtByeInd aInd = new RentAgtByeInd(Engine.getInstance().userName());
        sendCmdToAgent(aInd.toMsg());

        if (rTerminal != null)
        {
            rTerminal.endRtpSession();
            rTerminal = null;
        }
        if (rtps != null)
        {
            rtps.disconnect();
            rtps.close();
            rtps = null;
        }
    }

    private synchronized void hdlAgentByeMsg()
    {
        if (rTerminal != null)
        {
            rTerminal.endRtpSession();
            rTerminal = null;
        }
        if (rtps != null)
        {
            rtps.disconnect();
            rtps.close();
            rtps = null;
        }
    }
    
    private synchronized void sendCmdToAgent(byte[] msg)
    {
        InetAddress svrAddr = null;
        DatagramSocket socket = null;
        
        try
        {
            svrAddr = InetAddress.getByName(AGENT_IP);
        }
        catch (UnknownHostException e1)
        {
            e1.printStackTrace();
            return;
        }
        
        DatagramPacket packet = new DatagramPacket(msg, msg.length,
                svrAddr, AGENT_PORT);
        try
        {
            socket = new DatagramSocket();
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        
        try
        {
            socket.send(packet);
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
