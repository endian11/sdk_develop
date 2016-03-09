package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AppAgtTcpReconn
{
	public int			cmdCode		= MsgId.APP_AGENT_TCP_RECONN;
	public int			contentLen	= 68;
	public OctArray28_s	usernameArray28_s;
	private int net;
	public OctArray28_s ipAddr;

	public AppAgtTcpReconn(String username, int net, String ip)
	{
		usernameArray28_s = new OctArray28_s(username);
		this.net = net;
		ipAddr = new OctArray28_s(ip);
	}

	public byte[] toByte()
	{
	    // 不指定编码，默认ascii
		byte[] rtByte = new byte[contentLen];
		System.arraycopy(usernameArray28_s.toByte(), 0, rtByte, 0, 32);
		System.arraycopy(ByteUtil.getBytes(net), 0, rtByte, 32, 4);
		System.arraycopy(ipAddr.toByte(), 0, rtByte, 36, 32);
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
