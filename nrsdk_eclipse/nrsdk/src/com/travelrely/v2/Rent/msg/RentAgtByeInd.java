package com.travelrely.v2.Rent.msg;

import com.travelrely.core.nr.msg.MsgId;
import com.travelrely.core.nr.msg.OctArray28_s;
import com.travelrely.core.nr.util.ByteUtil;

public class RentAgtByeInd
{
    public int 				cmdCode = MsgId.APP_AGT_XH_BYE_IND;
    public int 				contentLen = 32;

    public OctArray28_s     usernameArray28_s;

    public RentAgtByeInd(String usernameStringInput)
    {
        usernameArray28_s=new OctArray28_s(usernameStringInput);
    }

    public byte[] toByte()
    {
        //不指定编码，默认ascii
        byte[] rtByte = new byte[contentLen];
        System.arraycopy(usernameArray28_s.toByte(), 0, rtByte, 0, 32);
        return rtByte;
    }

    public byte[] toMsg()
    {
        byte[] rtmsg=new byte[contentLen+8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);
        System.arraycopy(toByte(), 0, rtmsg, 8, contentLen);
        return rtmsg;
    }
}
