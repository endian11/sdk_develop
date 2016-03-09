package com.travelrely.v2.NR;

import com.travelrely.v2.util.LogUtil;

public class RayLib
{
    static
    {
        System.loadLibrary("travelrely");
    }

    public static interface Listener
    {
        public void onAudioEncoded(byte[] data, int size);
        public void onAudioDecoded(byte[] data, int size);
    }

    private static Listener mListener;

    synchronized public static void setListener(Listener l)
    {
        mListener = l;
    }

    native public static String getJniString();
    
    native public static void testSocket();
    
    native public static int newSocket();
    native public static int connectSocket(int fd, String ip, int port);
    native public static int sendSocket(int fd, byte[] bytes);
    native public static int recvSocket(int fd, byte[] bytes);
    native public static int closeSocket(int fd);
    
    native public static int descram(byte[]src, byte[]arg, byte[]dst);

    private static void onTcpReceived(byte[] data, int size)
    {
        LogUtil.d("onTcpReceived", new String(data));
    }
}
