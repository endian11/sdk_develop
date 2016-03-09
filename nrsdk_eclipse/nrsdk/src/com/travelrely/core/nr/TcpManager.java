package com.travelrely.core.nr;

import android.text.TextUtils;

import com.travelrely.core.Engine;
import com.travelrely.core.nr.msg.MsgId;
import com.travelrely.core.nr.util.ByteUtil;
import com.travelrely.v2.AesLib;
import com.travelrely.v2.NR.RayLib;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.NetUtil;

public class TcpManager
{
    private static final String TAG = TcpManager.class.getSimpleName();

    private static String SERVER_IP = "211.103.2.204";// 209
    private static final int SERVER_PORT = 30005;

    private TcpCallback mCallback;

    private TcpThread tcpThread = null;

    public TcpManager(String ip, int p)
    {
        SERVER_IP = ip;
        LogUtil.i(TAG, "SERVER_IP = " + SERVER_IP);
    }

    public void start()
    {
        tcpThread = new TcpThread("TcpThread");
        tcpThread.start();
    }

    public void stop()
    {
        tcpThread.cancel();
        LogUtil.e(TAG, "stop " + tcpThread.getName() + tcpThread.getId());
    }

    public void sendCmdMsg(byte[] msg)
    {
        if (tcpThread == null)
        {
            LogUtil.e(TAG, "tcpThread == null");
            return;
        }
        
        if (!Engine.getInstance().isEncEnable)
        {
            tcpThread.send(msg);
            return;
        }

        byte[] tmp = ByteUtil.subArray(msg, 40, msg.length-40);
        int m = tmp.length % 16;
        int n = 16 - m;
        byte[] enc_in = new byte[tmp.length + n];
        System.arraycopy(tmp, 0, enc_in, 0, tmp.length);
        
        AesLib.encrypt(enc_in, enc_in);
        
        byte[] msg_out = new byte[enc_in.length + 40];
        System.arraycopy(msg, 0, msg_out, 0, 40);
        System.arraycopy(enc_in, 0, msg_out, 40, enc_in.length);
        
        int contentLen = enc_in.length + 32;
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, msg_out, 4, 4);
        
        //LogUtil.i(TAG, ByteUtil.toHexString(msg_out));

        tcpThread.send(msg_out);
    }

    private void recvCmdMsg(byte[] msg, int len)
    {
        if (mCallback == null)
        {
            LogUtil.e(TAG, "mCallback == null");
            return;
        }

        mCallback.onTcpReceived(msg, len);
    }

    private void notifyApp(int iMsgId)
    {
        if (mCallback == null)
        {
            LogUtil.e(TAG, "mCallback == null");
            return;
        }

        if (iMsgId == MsgId.APP_TCP_CONNECTED)
        {
            mCallback.onTcpConnected();
        }
        
        if (iMsgId == MsgId.APP_TCP_DISCONNECTED)
        {
            mCallback.onTcpDisconnected();
        }
    }

    private class TcpThread extends Thread
    {
        private String id = "";
        private boolean mStop = false;
        private int iSocket = -1;

        public TcpThread(String name)
        {
            super(name);
            id = getName() + "-" + getId();
            LogUtil.i(id, "new thread");
        }

        /**
         * 初始化socket对象
         */
        public boolean initTcpSocket()
        {
            iSocket = RayLib.newSocket();
            if (iSocket <= 0)
            {
                LogUtil.i(id, "socket创建失败");
                return false;
            }

            int iRslt = RayLib.connectSocket(iSocket, SERVER_IP, SERVER_PORT);
            if (iRslt < 0)
            {
                LogUtil.i(id, "socket连接失败");
                return false;
            }

            return true;
        }

        @Override
        public void run()
        {
            mStop = false;
            byte[] recvbuf = new byte[1024];
            
            if (TextUtils.isEmpty(SERVER_IP))
            {
                LogUtil.e(id, "SERVER_IP == null");
                return;
            }
            
            if (initTcpSocket())
            {
                LogUtil.e(id, "TCP init succ");
                // TCP连接成功后发重连消息，发送成功则表示已经连接上，发送失败表示已经断开
                notifyApp(MsgId.APP_TCP_CONNECTED);
            }
            else
            {
                LogUtil.e(id, "socket初始化失败");
            }

            while (true)
            {
                int size = RayLib.recvSocket(iSocket, recvbuf);
                if (size > 0)
                {
                    recvCmdMsg(recvbuf, size);
                }
                else
                {
                    if (mStop)
                    {
                        LogUtil.e(id, "TCP线程已被停止");
                        return;
                    }

                    closeSocket();
                    notifyApp(MsgId.APP_TCP_DISCONNECTED);
                    LogUtil.e(id, "socket接收错误");
                    
                    while (!NetUtil.isNetworkAvailable(Engine.getInstance()
                            .getContext()))
                    {
                        try
                        {
                            Thread.sleep(10000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        LogUtil.e(id, "没有网络");
                    }
                    
                    if (initTcpSocket())
                    {
                        LogUtil.e(id, "TCP init succ");
                        // TCP连接成功后发重连消息，发送成功则表示已经连接上，发送失败表示已经断开
                        notifyApp(MsgId.APP_TCP_CONNECTED);
                    }
                    else
                    {
                        LogUtil.e(id, "socket初始化失败");
                    }
                }
            }
        }

        /**
         * 发送数据，发送失败返回false,发送成功返回true
         */
        public boolean send(byte[] msgBuf)
        {
            if (iSocket < 0)
            {
                LogUtil.e(id, "TCP未连接");
                return false;
            }

            int size = RayLib.sendSocket(iSocket, msgBuf);
            if (size == msgBuf.length)
            {
                System.currentTimeMillis();
            }

            return true;
        }
        
        public void cancel()
        {
            mStop = true;
            closeSocket();
        }

        private void closeSocket()
        {
            if (RayLib.closeSocket(iSocket) == 0)
            {
                iSocket = -1;
            }
            else
            {
                LogUtil.e(id, "close socket fail");
            }
        }
    }

    public void setTcpCallback(TcpCallback callback)
    {
        this.mCallback = callback;
    }

    public interface TcpCallback
    {
        public void onTcpConnected();
        public void onTcpDisconnected();
        public void onTcpReceived(byte[] buf, int len);
    }
}
