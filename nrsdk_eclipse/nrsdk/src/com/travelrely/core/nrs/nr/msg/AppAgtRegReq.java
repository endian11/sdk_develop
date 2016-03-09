package com.travelrely.core.nrs.nr.msg;

import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.Utils;

public class AppAgtRegReq
{
	public int			cmdCode		= MsgId.APP_AGENT_REGISTER_REQ;
	public int			contentLen	= 608;
	public OctArray28_s	username;

	public OctArray28_s	imsi;
	public OctArray28_s	phone;
	public OctArray28_s	smc;
	public OctArray28_s btMac;
	public int         iNetType    = 0;
	public OctArray28_s vendor;
	public OctArray28_s cosVer;
	public OctArray28_s cos_date;

	byte[] mcc = new byte[4];
    byte[] mnc = new byte[4];
    
    byte[] device_token = new byte[100];
    byte[] voip_token = new byte[100];

    public int         phone_type    = 1;
    public OctArray28_s phone_des;
    
    public OctArray28_s partner_id;
    public OctArray28_s app_ver;
    public OctArray28_s bluetooth_desc;//蓝牙盒子名称

	public AppAgtRegReq(String username,
			byte[] imsi,  String smc, String phone, int iNetType)
	{
		this.username = new OctArray28_s(username);

		this.imsi = new OctArray28_s(imsi.length, imsi);
		this.phone = new OctArray28_s(phone);
		this.smc = new OctArray28_s(smc);
		
		this.btMac = new OctArray28_s("");
		this.iNetType = iNetType;
		
	    this.vendor = new OctArray28_s(SpUtil.getBoxSn());

	    int cos = SpUtil.getCosVer();
	    this.cosVer = new OctArray28_s(4, ByteUtil.getBytesByBigEnd(cos));
	    
	    this.cos_date = new OctArray28_s("2015-4-23");

        if (TextUtils.isEmpty(Engine.getInstance().getSimMcc()))
        {
            mcc = "".getBytes();
        }
        else
        {
            mcc = Engine.getInstance().getSimMcc().getBytes();
        }
        
        if (TextUtils.isEmpty(Engine.getInstance().getSimMnc()))
        {
            mnc = "".getBytes();
        }
        else
        {
            mnc = Engine.getInstance().getSimMnc().getBytes();
        }
	    
	    device_token = "".getBytes();
	    String token = PushManager.getInstance().getClientid(Engine.getInstance().getContext());
	    if (TextUtils.isEmpty(token))
	    {
	        voip_token = "".getBytes();
	    }
	    else
	    {
	        voip_token = token.getBytes();
	    }
	    phone_type    = 1;
	    this.phone_des = new OctArray28_s("2015-4-23");
	    
	    this.partner_id = new OctArray28_s(ConstantValue.PartnerID);
	    this.bluetooth_desc = new OctArray28_s(SpUtil.getBtName());
	    this.app_ver = new OctArray28_s(Utils.getVersion(Engine.getInstance().getContext()));
	}

	public byte[] toMsg()
	{
		byte[] rtmsg = new byte[contentLen + 8];
		System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);// cmd code
		System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);// length
		System.arraycopy(username.toByte(), 0, rtmsg, 8, 32);// content
		System.arraycopy(imsi.toByte(), 0, rtmsg, 40, 32);
		System.arraycopy(phone.toByte(), 0, rtmsg, 72, 32);
		System.arraycopy(smc.toByte(), 0, rtmsg, 104, 32);
		
		System.arraycopy(btMac.toByte(), 0, rtmsg, 136, 32);
		System.arraycopy(ByteUtil.getBytes(iNetType), 0, rtmsg, 168, 4);
		
	    System.arraycopy(vendor.toByte(), 0, rtmsg, 172, 32);
	    System.arraycopy(cosVer.toByte(), 0, rtmsg, 204, 32);
	    System.arraycopy(cos_date.toByte(), 0, rtmsg, 236, 32);
	    
	    System.arraycopy(mcc, 0, rtmsg, 268, mcc.length);
	    System.arraycopy(mnc, 0, rtmsg, 272, mnc.length);
	    
	    System.arraycopy(device_token, 0, rtmsg, 276, device_token.length);
	    System.arraycopy(voip_token, 0, rtmsg, 376, voip_token.length);
	    
	    System.arraycopy(ByteUtil.getBytes(phone_type), 0, rtmsg, 476, 4);
	    System.arraycopy(phone_des.toByte(), 0, rtmsg, 480, 32);
	    byte[] byte1 = partner_id.toByte();
	    for (int i=0; i<byte1.length;i++){
	    	byte1[i] = (byte) (byte1[i]^0xff);
	    }
	    System.arraycopy(byte1, 0, rtmsg, 512, 32);
	    System.arraycopy(app_ver.toByte(), 0, rtmsg, 544, 32);
	    System.arraycopy(bluetooth_desc.toByte(), 0, rtmsg, 576, 32);

		return rtmsg;
	}
}
