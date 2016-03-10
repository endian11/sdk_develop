package com.travelrely.core.nrs.nr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.travelrely.core.nrs.nr.msg.MsgId;
import com.travelrely.core.util.LogUtil;

public class TcpService
{
    private static final String TAG = TcpService.class.getSimpleName();;
    
    private static String SERVER_IP = "211.103.2.204";
    private static final int SERVER_PORT = 30005;
    
    private boolean mStop = false;

    private Socket socket;

    private TcpThread tcpThread = null;

    // 定义向UI线程发送消息的Handler对象
    private Handler handler;

    public TcpService(Handler handler)
    {
        this.handler = handler;

        LogUtil.i(TAG, "SERVER_IP = " + SERVER_IP);
    }

    public void start()
    {
        if (tcpThread != null)
        {
            tcpThread.cancel();
            tcpThread = null;
        }

        try
        {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            socket.setKeepAlive(true);
            // Log.e("", "s: " + socket.getLocalAddress().toString());
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        mStop = false;
        tcpThread = new TcpThread(socket);
        tcpThread.setName("TcpThread");
        tcpThread.start();
        
        // TCP连接成功后发重连消息
        byte[] buf = new byte[4];
        buf[0] = MsgId.APP_AGENT_TCP_RECONN;
        recvCmdMsg(buf, 4);
    }

    public void stop()
    {
        mStop = true;
        handler = null;
    }

    public void sendCmdMsg(byte[] msg)
    {
        if (tcpThread == null)
        {
            TcpService.this.start();
        }

        if (tcpThread != null)
        {
            tcpThread.write(msg);
        }
        else
        {
            reconnect();
        }
    }
    
    private void recvCmdMsg(byte[] msg, int len)
    {
        if (handler == null)
        {
            LogUtil.e(TAG, "handler == null");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putByteArray("cmd", msg);
        bundle.putInt("cmd_len", len);
        Message stMsg = new Message();
        stMsg.setData(bundle);
        handler.sendMessage(stMsg);
    }

    private void reconnect()
    {
        //TcpService.this.start();
        try
        {
            socket.connect(null);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class TcpThread extends Thread
    {
        private Socket mSocket = null;
        private InputStream is = null;
        private OutputStream os = null;

        public TcpThread(Socket socket)
        {
            this.mSocket = socket;
            try
            {
                is = mSocket.getInputStream();
                os = mSocket.getOutputStream();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            int size = 0;
            byte[] buf = new byte[1024];

            while (!mStop)
            {
                // 不断读取Socket输入流中的内容
                try
                {
                    // 发探测包
                    //Log.i(getName(), "mSocket.sendUrgentData(0xFF)");
                    //mSocket.sendUrgentData(0xFF);

                    // 接收数据
                    size = is.read(buf);

                    // 每当读到来自服务器的数据之后，发送消息通知程序界面显示该数据
                    if (size > 0)
                    {
                        recvCmdMsg(buf, size);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    LogUtil.e(getName(), "接收端检测到disconnected");
                    reconnect();
                    return;
                }
            }
            
            LogUtil.e(getName(), "TCP disconnected");
            cancel();
        }

        public void write(byte[] msgBuf)
        {
            if (mSocket.isClosed() || mSocket.isInputShutdown()
                    || mSocket.isOutputShutdown())
            {
                LogUtil.e(getName(), "发送端检测到socket disconnected");
                reconnect();
                return;
            }

            try
            {
                os.write(msgBuf);
                os.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                LogUtil.e(getName(), "发送端检测到buf disconnected");
                reconnect();
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
}
