package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AppAgtStoreKeyRsltInd
{
    public int cmdCode = MsgId.APP_AGENT_STORE_KEY_RSLT_IND;
    public int contentLen = 36;
    public OctArray28_s usernameArray28_s;
    private int connRslt;

    public AppAgtStoreKeyRsltInd(String username, int connRslt)
    {
        usernameArray28_s = new OctArray28_s(username);
        this.connRslt = connRslt;
    }

    public byte[] toByte()
    {
        // 不指定编码，默认ascii
        byte[] rtByte = new byte[contentLen];
        System.arraycopy(usernameArray28_s.toByte(), 0, rtByte, 0, 32);
        System.arraycopy(ByteUtil.getBytes(connRslt), 0, rtByte, 32, 4);
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
