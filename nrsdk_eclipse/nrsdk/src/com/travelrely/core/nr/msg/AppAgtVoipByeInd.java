package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AppAgtVoipByeInd
{
    public int cmdCode = MsgId.APP_AGENT_VOIP_BYE_IND;
    public int contentLen = 64;

    public OctArray28_s userName;
    public OctArray28_s peerName;

    public AppAgtVoipByeInd(String username, String peername)
    {
        userName = new OctArray28_s(username);
        peerName = new OctArray28_s(peername);
    }

    public byte[] toByte()
    {
        // 不指定编码，默认ascii
        byte[] rtByte = new byte[contentLen];
        System.arraycopy(userName.toByte(), 0, rtByte, 0, 32);
        System.arraycopy(peerName.toByte(), 0, rtByte, 32, 32);
        return rtByte;
    }

    public byte[] toMsg()
    {
        byte[] rtmsg = new byte[contentLen + 8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);
        System.arraycopy(toByte(), 0, rtmsg, 8, contentLen);
        return rtmsg;
    }
}
