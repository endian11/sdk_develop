package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AppAgtTestCallingReq
{
    public int cmdCode = MsgId.APP_AGENT_TEST_CALLING_REQ;
    public int contentLen = 36;
    public OctArray28_s stUserName;
    private int type;

    public AppAgtTestCallingReq(String userName, int type)
    {
        this.stUserName = new OctArray28_s(userName);
        this.type = type;
    }

    public byte[] toMsg()
    {
        byte[] rtmsg = new byte[contentLen + 8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);
        System.arraycopy(stUserName.toByte(), 0, rtmsg, 8, 32);
        System.arraycopy(ByteUtil.getBytes(type), 0, rtmsg, 40, 4);
        return rtmsg;
    }
}
