package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AppAgtCallingReq
{
    public int cmdCode = MsgId.APP_AGENT_CALLING_REQ;
    public int contentLen = 64;
    
    public OctArray28_s usernameArray28_s;
    public OctArray28_s calledArray28_s;
    public int result;

    public AppAgtCallingReq(String username, String peer)
    {
        usernameArray28_s = new OctArray28_s(username);
        calledArray28_s = new OctArray28_s(peer);
    }
    
    public AppAgtCallingReq(String usernameStringInput, OctArray28_s calledInput)
    {
        usernameArray28_s = new OctArray28_s(usernameStringInput);
        calledArray28_s = calledInput;
    }

    public AppAgtCallingReq(String usernameStringInput, int resultInput)
    {
        usernameArray28_s = new OctArray28_s(usernameStringInput);
        calledArray28_s = new OctArray28_s(ByteUtil.subArray(toByte(), 40, 32));
        result = resultInput;
    }

    public AppAgtCallingReq(byte[] byteInput)
    {
        usernameArray28_s = new OctArray28_s(
                ByteUtil.subArray(byteInput, 8, 32));
        calledArray28_s = new OctArray28_s(ByteUtil.subArray(byteInput, 40, 32));
    }

    public byte[] toByte()
    {
        // 不指定编码，默认ascii
        byte[] rtByte = new byte[contentLen];
        System.arraycopy(usernameArray28_s.toByte(), 0, rtByte, 0, 32);
        System.arraycopy(calledArray28_s.toByte(), 0, rtByte, 32, 32);
        System.arraycopy(ByteUtil.getBytes(result), 0, rtByte, 32, 4);
        return rtByte;
    }

    public byte[] toMsg()
    {
        byte[] rtmsg = new byte[contentLen + 8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);
        System.arraycopy(toByte(), 0, rtmsg, 8, contentLen);
        rtmsg[rtmsg.length - 32] = (byte) calledArray28_s.stringLeng();
        return rtmsg;
    }

    public String getUsername()
    {
        return new String(ByteUtil.subArray(usernameArray28_s.data, 4,
                usernameArray28_s.octcnt));
    }

    public String getCalledNum()
    {
        return new String(ByteUtil.subArray(calledArray28_s.data, 4,
                calledArray28_s.octcnt));
    }
}
