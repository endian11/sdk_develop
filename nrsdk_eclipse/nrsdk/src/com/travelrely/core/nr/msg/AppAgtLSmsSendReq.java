package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AppAgtLSmsSendReq
{
    public int cmdCode = MsgId.APP_AGENT_LSMS_SEND_REQ;
    public int contentLen;
    
    public OctArray28_s usernameArray28_s;
    public OctArray28_s receiverArray28_s;
    public CharString_s messageCharString_s;
    
    public AppAgtLSmsSendReq(String user, String receiver, byte[] msg)
    {
        usernameArray28_s = new OctArray28_s(user);
        receiverArray28_s = new OctArray28_s(receiver);
        
        messageCharString_s = new CharString_s(msg.length, msg);
        contentLen = 32 * 2 + messageCharString_s.getLen();
    }

    public byte[] toByte()
    {
        // 不指定编码，默认ascii
        byte[] rtByte = new byte[contentLen];
        System.arraycopy(usernameArray28_s.toByte(), 0, rtByte, 0, 32);
        System.arraycopy(receiverArray28_s.toByte(), 0, rtByte, 32, 32);
        System.arraycopy(messageCharString_s.toByte(), 0, rtByte, 64,
                messageCharString_s.getLen());
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
