package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AppAgtUserVerifyReq
{
    public int cmdCode = MsgId.APP_AGENT_USER_VERIFY_REQ;
    public int contentLen = 96;
    public OctArray28_s user;
    public OctArray28_s imsi;
    public OctArray28_s random;

    public AppAgtUserVerifyReq(String user, byte[] imsi, byte[] random)
    {
        this.user = new OctArray28_s(user);
        this.imsi = new OctArray28_s(imsi.length, imsi);
        this.random = new OctArray28_s(random.length, random);
    }

    public byte[] toMsg()
    {
        byte[] rtmsg = new byte[contentLen + 8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);
        System.arraycopy(user.toByte(), 0, rtmsg, 8, 32);
        System.arraycopy(imsi.toByte(), 0, rtmsg, 40, 32);
        System.arraycopy(random.toByte(), 0, rtmsg, 72, 32);
        return rtmsg;
    }
    
    public String getRandom()
    {
        int len = random.octcnt;
        byte[] oct = new byte[len];
        
        for (int i = 0; i < len; i++)
        {
            oct[i] = (byte) (random.data[i] + 0x30);
        }
        
        return new String(oct);
    }
}
