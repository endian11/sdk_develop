package com.travelrely.core.nr;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.text.TextUtils;

import com.travelrely.core.Engine;
import com.travelrely.core.nr.TcpManager.TcpCallback;
import com.travelrely.core.nr.util.ByteUtil;
import com.travelrely.v2.AesLib;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.SpUtil;

public class TcpClient
{
    private static final String TAG = TcpClient.class.getSimpleName();
    private boolean mStop = false;
    
    private TcpCallback mCallback;
    private ExecutorService singleThreadExecutor;

    private String host = "115.29.43.99";//默认改成99
    private int port = 30005;
    
    private Queue<FrameData> mFrames = new LinkedList<FrameData>();


    /* 缓冲区大小 */
    private static int BLOCK = 2048;
    /* 接受数据缓冲区 */
    private static ByteBuffer recvBuffer = ByteBuffer.allocate(BLOCK);

    /* 服务器端地址 */
    private static InetSocketAddress SERVER_ADDRESS;

    public TcpClient(String ip, int p)
    {
        this.host = ip;
        this.port = p;
        LogUtil.i(TAG, "new SERVER = " + host+":"+port);
        
        SERVER_ADDRESS = new InetSocketAddress(host, port);
//        SERVER_ADDRESS = new InetSocketAddress("115.28.167.215", port);
        
        //测试个推
        
        
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }
    
    public void setTcpCallback(TcpCallback callback)
    {
        this.mCallback = callback;
    }
    
    public boolean isStoped()
    {
        return (this.mStop == true);
    }
    
    public void sendCmdMsg(byte[] msg)
    {
        if (!Engine.getInstance().isEncEnable)
        {
            FrameData frame = new FrameData();
            frame.data = msg;
            frame.size = msg.length;
            synchronized (mFrames)
            {
                mFrames.offer(frame);//加入队列
                mFrames.notify();
            }
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

        FrameData frame = new FrameData();
        frame.data = msg_out;
        frame.size = msg_out.length;
        synchronized (mFrames)
        {
            mFrames.offer(frame);
            mFrames.notify();
        }
    }

    public void start()
    {
        synchronized (this)
        {
            mStop = false;
        }

        host = SpUtil.getNRFemtoIp();
        if(TextUtils.isEmpty(host)){
        	host = "115.29.43.99";
        }
        LogUtil.i(TAG, "start SERVER = " + host+":"+port);
        SERVER_ADDRESS = new InetSocketAddress(host, port);
//        SERVER_ADDRESS = new InetSocketAddress("115.28.167.215", port);
        //mTcpThread = new TcpThread("TcpThread");
        //mTcpThread.start();`
        
        singleThreadExecutor.execute(mTcpTask);
    }
    
    synchronized public void stop()
    {
        this.mCallback = null;
        this.mStop = true;
    }
    
    synchronized public void reset()
    {
        this.mStop = true;
    }
    
    private class FrameData
    {
        public byte[] data;
        public int size;
    }
    
    private Runnable mTcpTask = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                mainloop();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finally
            {
                LogUtil.w(TAG, "tcp thread Exception");
                if (mCallback == null)
                {
                    LogUtil.e(TAG, "mCallback == null");
                    return;
                }
                mCallback.onTcpDisconnected();
            }
        }

        private void mainloop() throws IOException, InterruptedException
        {
            // 打开socket通道,设置为非阻塞方式
            SocketChannel clientChannel = SocketChannel.open();
            clientChannel.socket().setSoTimeout(2000);
            clientChannel.socket().setKeepAlive(true);
            clientChannel.configureBlocking(false);
            
            // 打开选择器
            Selector selector = Selector.open();

            // 注册连接服务端socket动作  
            clientChannel.register(selector, SelectionKey.OP_CONNECT
                    | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            // 连接  
            clientChannel.connect(SERVER_ADDRESS);
            SocketChannel socketChannel;
            Set<SelectionKey> selectionKeys;
            int count = 0;
            
            while (true)
            {
                if (mStop)
                {
                    selector.close();
                    clientChannel.close();
                    LogUtil.w(TAG, "tcp thread was stoped");
                    if (mCallback == null)
                    {
                        LogUtil.e(TAG, "mCallback == null");
                        return;
                    }
                    mCallback.onTcpDisconnected();
                    break;
                }
                //选择一组键，其相应的通道已为 I/O 操作准备就绪。  
                //监控所有注册的 channel ，当其中有注册的 IO 操作可以进行时，该函数返回，并将对应的 SelectionKey 加入 selected-key set 
                selector.select();
                
                //返回此选择器的已选择键集。  
                selectionKeys = selector.selectedKeys();
                //System.out.println(selectionKeys.size());
                
                for(SelectionKey selectionKey : selectionKeys)
                {
                    //判断是否为建立连接的事件
                    if (selectionKey.isConnectable())
                    {
                        LogUtil.i(TAG, "client connect...");
                        socketChannel = (SocketChannel) selectionKey.channel();
                        // 判断此通道上是否正在进行连接操作
                        // 完成套接字通道的连接过程
                        if (socketChannel.isConnectionPending())
                        {
                            // 完成连接的建立（TCP三次握手）
                            socketChannel.finishConnect();
                            LogUtil.i(TAG, "connect success !");
                            
                            if (mCallback == null)
                            {
                                LogUtil.e(TAG, "mCallback == null");
                                return;
                            }
                            mCallback.onTcpConnected();
                        }
                        else
                        {
                            mStop = true;
                        }
                    }
                    else if (selectionKey.isReadable())
                    {
                        socketChannel = (SocketChannel) selectionKey.channel();
                        
                        //将缓冲区清空以备下次读取  
                        recvBuffer.clear();
                        
                        //读取服务器发送来的数据到缓冲区中
                        count = socketChannel.read(recvBuffer);
                        if (count > 0)
                        {
                            String recvText = ByteUtil.toHexString(
                                    ByteUtil.subArray(recvBuffer.array(), 0, count));
                            //LogUtil.i(TAG, "msg from SVR--:"+recvText);
                            
                            if (mCallback == null)
                            {
                                LogUtil.e(TAG, "mCallback == null");
                                return;
                            }
                            mCallback.onTcpReceived(recvBuffer.array(), count);
                        }
                    }
                    else if (selectionKey.isWritable())
                    {
                        //System.out.println("selectionKey.isWritable");
                        if (mFrames.isEmpty())
                        {
                            //System.out.println("no data to send");
                            synchronized(mFrames)
                            {
                                mFrames.wait(500);
                            }
                        }
                        
                        if (mFrames.size() > 0)
                        {
                            socketChannel = (SocketChannel) selectionKey.channel();
                            
                            FrameData frame = null;
                            synchronized(mFrames)
                            {
                                frame = mFrames.poll();
                            }
                            
                            if (frame == null)
                            {
                                continue;
                            }
                            count = socketChannel.write(
                                    ByteBuffer.wrap(frame.data, 0, frame.size));
                            if (count != frame.size)
                            {
                                LogUtil.e(TAG, "send packet fail[%d/%d]", count, frame.size);
                            }
                            String sendText = ByteUtil.toHexString(frame.data);
                            //LogUtil.i(TAG, "msg to SVR--:"+sendText);
                        }
                    }
                    else
                    {
                        LogUtil.e(TAG, "select error");
                    }
                }
                
                selectionKeys.clear();
            }
        }
    };
}
