package com.travelrely.core.nrs.nr.msg;

import android.text.TextUtils;

import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.Utils;

public class AppAgtKeepAliveReq
{
    public int cmdCode = MsgId.APP_AGENT_KEEPALIVE_REQ;
    public int contentLen = 148;
    public OctArray28_s usernameArray28_s;
    private int net;
    private int throughPut;
    private int connRslt;
    
    public OctArray28_s partner_id;
    public OctArray28_s app_ver;
    public OctArray28_s last_time;
    byte[] mcc = new byte[4];
    byte[] mnc = new byte[4];

    public AppAgtKeepAliveReq(String username, int net, int throughPut, int connRslt)
    {
        usernameArray28_s = new OctArray28_s(username);
        this.net = net;
        this.throughPut = throughPut;
        this.connRslt = connRslt;
        
        partner_id = new OctArray28_s(ConstantValue.PartnerID);
        app_ver = new OctArray28_s(Utils.getVersion(Engine.getInstance().getContext()));
        last_time = new OctArray28_s(SpUtil.getLastFreground());
        
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
    }

    public byte[] toMsg()
    {
        byte[] rtmsg = new byte[contentLen + 8];
        System.arraycopy(ByteUtil.getBytes(cmdCode), 0, rtmsg, 0, 4);
        System.arraycopy(ByteUtil.getBytes(contentLen), 0, rtmsg, 4, 4);

        System.arraycopy(usernameArray28_s.toByte(), 0, rtmsg, 8, 32);
        System.arraycopy(ByteUtil.getBytes(net), 0, rtmsg, 40, 4);
        System.arraycopy(ByteUtil.getBytes(throughPut), 0, rtmsg, 44, 4);
        System.arraycopy(ByteUtil.getBytes(connRslt), 0, rtmsg, 48, 4);
        
        System.arraycopy(partner_id.toByte(), 0, rtmsg, 52, 32);
        System.arraycopy(app_ver.toByte(), 0, rtmsg, 84, 32);
        System.arraycopy(last_time.toByte(), 0, rtmsg, 116, 32);
        
        System.arraycopy(mcc, 0, rtmsg, 148, mcc.length);
        System.arraycopy(mnc, 0, rtmsg, 152, mnc.length);
        
        return rtmsg;
    }
}
