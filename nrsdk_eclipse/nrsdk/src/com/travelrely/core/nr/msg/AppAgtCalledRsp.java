package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AppAgtCalledRsp
{
    public int cmdCode = MsgId.APP_AGENT_CALLED_RSP;
    public int contentLen = 36;
    public OctArray28_s usernameArray28_s;
    public int result;

    public AppAgtCalledRsp(String usernameStringInput, int resultInput)
    {
        usernameArray28_s = new OctArray28_s(usernameStringInput);
        result = resultInput;
    }

    public byte[] toByte()
    {// 不指定编码，默认ascii
        byte[] rtByte = new byte[contentLen];
        System.arraycopy(usernameArray28_s.toByte(), 0, rtByte, 0, 32);
        System.arraycopy(ByteUtil.getBytes(result), 0, rtByte, 32, 4);
        return rtByte;
    }

    //
    // public void ByteInit(byte[] byteInput){
    // byte[] byteUsername=new byte[29];
    // System.arraycopy(byteInput, 0, byteUsername, 0, 28);
    // usernameString=new String(byteUsername);
    // imsiPresent=byteInput[28];
    // System.arraycopy(byteInput, 29, byteUsername, 0, 28);
    // imsiString=new String(byteUsername);
    // }

    public byte[] toMsg()
    {
        byte[] rtmsg = new byte[contentLen + 8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);
        System.arraycopy(toByte(), 0, rtmsg, 8, contentLen);
        return rtmsg;
    }
}