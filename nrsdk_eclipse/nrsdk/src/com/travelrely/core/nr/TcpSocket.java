package com.travelrely.core.nr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.travelrely.core.Engine;
import com.travelrely.core.nr.msg.MsgId;
import com.travelrely.core.nr.util.ByteUtil;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.NetUtil;
import com.travelrely.v2.util.SpUtil;

public class TcpSocket
{
    private static final String TAG = TcpSocket.class.getSimpleName();
    
    private static String SERVER_IP = "211.103.2.209";//204
    private static final int SERVER_PORT = 30005;
    
    private TcpCallback mCallback;
    
    private boolean mStop = false;

    private TcpThread tcpThread = null;

    public TcpSocket()
    {
        SERVER_IP = SpUtil.getNRFemtoIp();
        LogUtil.i(TAG, "SERVER_IP = " + SERVER_IP);
    }

    public void start()
    {
        mStop = true;
        if (tcpThread != null)
        {
            while (tcpThread.isAlive())
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            tcpThread = null;
        }

        mStop = false;
        tcpThread = new TcpThread();
        tcpThread.setName("TcpThread");
        tcpThread.start();
    }

    public void stop()
    {
        mStop = true;
        mCallback = null;
    }
    
    public void sendCmdMsg(byte[] msg)
    {
        if (tcpThread != null)
        {
            tcpThread.send(msg);
        }
    }
    
    private void recvCmdMsg(byte[] msg, int len)
    {
        if (mCallback == null)
        {
            LogUtil.e(TAG, "mCallback == null");
            return;
        }
        
        mCallback.onTcpReceived(msg, len);
        
        SpUtil.setNRRxBytes(SpUtil.getNRRxBytes() + len);
    }
    
    private void notifyApp(int iMsgId)
    {
        byte[] msgContent = new byte[8];
        System.arraycopy(ByteUtil.getBytes(iMsgId), 0, msgContent, 0, 4);

        recvCmdMsg(msgContent, 8);
    }

    private class TcpThread extends Thread
    {
        private Socket mSocket = null;
        private InputStream is = null;
        private OutputStream os = null;

        public TcpThread()
        {
            if (initTcpSocket())
            {
                // TCP连接成功后发重连消息，发送成功则表示已经连接上，发送失败表示已经断开
                notifyApp(MsgId.APP_TCP_CONNECTED);
            }
        }
        
        /**
         * 初始化socket对象
         */
        public boolean initTcpSocket()
        {
            try
            {
                mSocket = new Socket(SERVER_IP, SERVER_PORT);
                mSocket.setTcpNoDelay(true);
                mSocket.setKeepAlive(true);//开启保持活动状态的套接字
                
                is = mSocket.getInputStream();
                os = mSocket.getOutputStream();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }
            
            return true;
        }

        @Override
        public void run()
        {
            int size = 0;
            byte[] recvbuf = new byte[1024];
            
            while (true)
            {
                if (mStop)
                {
                    break;
                }
                
                //---------读数据---------------------------
                boolean close = isServerClose(mSocket);//判断是否断开
                if (!close)
                {
                    // 没有断开，开始读数据
                    try
                    {
                        size = is.read(recvbuf);
                        if (size > 0)
                        {
                            recvCmdMsg(recvbuf, size);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                
                //---------创建连接-------------------------
                while (close)
                {
                    notifyApp(MsgId.APP_TCP_DISCONNECTED);
                    while (!NetUtil.isNetworkAvailable(Engine.getInstance()
                            .getContext()))
                    {
                        LogUtil.e(getName(), "没有网络连接");
                        try
                        {
                            Thread.sleep(10000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    // 已经断开，重新建立连接
                    if (initTcpSocket())
                    {
                        // TCP连接成功后发重连消息，发送成功则表示已经连接上，发送失败表示已经断开
                        notifyApp(MsgId.APP_TCP_CONNECTED);
                        
                        close = false;
                    }
                    else
                    {
                        close = true;
                    }
                }
            }
            
            LogUtil.e(getName(), "主动释放TCP连接");
            cancel();
        }

        /**
         * 发送数据，发送失败返回false,发送成功返回true
         */
        public boolean send(byte[] msgBuf)
        {
            if (mSocket == null || os == null)
            {
                LogUtil.e(getName(), "TCP未连接");
                return false;
            }
            try
            {
                os.write(msgBuf);
                os.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                LogUtil.e(getName(), "发送失败");
                return false;
            }
            
            SpUtil.setNRTxBytes(SpUtil.getNRTxBytes() + msgBuf.length);
            return true;
        }

        /**
         * 判断是否断开连接，断开返回true,没有返回false
         */
        public boolean isServerClose(Socket socket)
        {
            try
            {
                /*
                 * 发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，
                 * 不影响正常通信
                 */
                socket.sendUrgentData(0);
                return false;
            }
            catch(Exception se)
            {
                return true;
            }
        }
        
        public void cancel()
        {
            try
            {
                is.close();
                is = null;
                os.close();
                os = null;
                //mSocket.shutdownInput();
                //mSocket.shutdownOutput();
                mSocket.close();
                mSocket = null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void setTcpCallback(TcpCallback callback)
    {
        this.mCallback = callback;
    }
    
    public interface TcpCallback
    {
        public void onTcpReceived(byte[] buf, int len);
    }
}
