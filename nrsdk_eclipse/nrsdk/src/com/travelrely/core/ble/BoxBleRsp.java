package com.travelrely.core.ble;

import com.travelrely.core.nr.util.ByteUtil;
import com.travelrely.v2.util.Utils;

public class BoxBleRsp
{
    byte len;
	byte[]			content;
	byte[] err;
	
	int				result;
	String         errCode;

	public BoxBleRsp(byte[] msg)
	{		
        if ((msg[msg.length-2]&0xFF) == 0x90
                && (msg[msg.length-1]&0xFF) == 0x00)
        {
            result = 0;
        }
        else
        {
            result = 1;
            errCode = Utils.bytesToHexString(ByteUtil.subArray(msg, msg.length-2, 2));
            return;
        }
        
        len = msg[0];
        if (len > 0)
        {
            content = ByteUtil.subArray(msg, 1, len&0xFF);
        }
	}
	
	public BoxBleRsp(byte[] content, byte[] err)
    {       
        if (content == null || content.length == 0)
        {
            len = 0x00;
        }
        else
        {
            len = (byte) content.length;
        }
        
        this.err = err;
    }
	
	public byte[] toByte()
	{
	    int len = this.len&0xFF + err.length;
	    byte[] msg = new byte[len];
	    if ((this.len&0xFF) > 0)
	    {
	        msg[0] = this.len;
	        System.arraycopy(content, 0, msg, 1, this.len&0xFF);
	        System.arraycopy(err, 0, msg, 1 + (this.len&0xFF), err.length);
	    }
	    else
	    {
	           msg[0] = 0x00;
	            System.arraycopy(err, 0, msg, 1, err.length);
	    }
	    
	    return msg;
	}
	
	public int getRslt()
	{
	    return result;
	}
	
    public String getErrCode()
    {
        return errCode;
    }
	
	public byte[] getContent()
	{
	    return content;
	}
}
