package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.util.SpUtil;

public class AppAgtHwInfoReq
{
	public int			cmdCode		= MsgId.APP_AGENT_HW_INFO_REQ;
	public int			contentLen	= 96;
	public OctArray28_s	username;

	public OctArray28_s cosVer;
	public OctArray28_s blue_sn;

	public AppAgtHwInfoReq(String username, String blue_sn)
	{
		this.username = new OctArray28_s(username);
		
	    this.blue_sn = new OctArray28_s(blue_sn);
	    
	    int cos = SpUtil.getCosVer();
	    this.cosVer = new OctArray28_s(4, ByteUtil.getBytesByBigEnd(cos));
	}
	
	public AppAgtHwInfoReq(String username, byte[] blue_sn)
    {
        this.username = new OctArray28_s(username);

        this.blue_sn = new OctArray28_s(blue_sn.length, blue_sn);

        int cos = SpUtil.getCosVer();
        this.cosVer = new OctArray28_s(4, ByteUtil.getBytesByBigEnd(cos));
    }

	public byte[] toMsg()
	{
		byte[] rtmsg = new byte[contentLen + 8];
		System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);// cmd code
		System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);// length
		System.arraycopy(username.toByte(), 0, rtmsg, 8, 32);// content
		System.arraycopy(cosVer.toByte(), 0, rtmsg, 40, 32);
		System.arraycopy(blue_sn.toByte(), 0, rtmsg, 72, 32);

		return rtmsg;
	}
}
