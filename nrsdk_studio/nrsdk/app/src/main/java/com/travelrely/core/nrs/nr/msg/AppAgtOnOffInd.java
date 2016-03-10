package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AppAgtOnOffInd
{
    public int cmdCode = MsgId.APP_AGENT_ON_OFF_IND;
    public int contentLen = 40;
    public OctArray28_s stUserName;
    private int iType;
    private int iRslt;

    public AppAgtOnOffInd(String userName, int type, int iRslt)
    {
        this.stUserName = new OctArray28_s(userName);
        this.iType = type;
        this.iRslt = iRslt;
    }

    public byte[] toMsg()
    {
        byte[] rtmsg = new byte[contentLen + 8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);
        System.arraycopy(stUserName.toByte(), 0, rtmsg, 8, 32);
        System.arraycopy(ByteUtil.getBytes(iType), 0, rtmsg, 40, 4);
        System.arraycopy(ByteUtil.getBytes(iRslt), 0, rtmsg, 44, 4);
        return rtmsg;
    }
}
