package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AppAgtPowerOnInd
{
    public int cmdCode = MsgId.APP_AGENT_POWER_ON_IND;
    public int contentLen = 32;

    public OctArray28_s usernameArray28_s;

    public AppAgtPowerOnInd(String usernameStringInput)
    {
        usernameArray28_s = new OctArray28_s(usernameStringInput);
    }

    public byte[] toMsg()
    {
        byte[] rtmsg = new byte[contentLen + 8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);

        System.arraycopy(usernameArray28_s.toByte(), 0, rtmsg, 8, 32);
        return rtmsg;
    }
}
