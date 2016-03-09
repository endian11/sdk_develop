package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppRegRsp
{
    public String usernameString;
    public int result;
    private byte[] timeStamp;
    
    byte[] boxKeyLen;
    byte[] boxKey;

    public AgtAppRegRsp(byte[] byteInput)
    {
        usernameString = new String(ByteUtil.subArray(byteInput, 12, 28));
        result = ByteUtil.getInt(ByteUtil.subArray(byteInput, 40, 4));
        timeStamp = ByteUtil.subArray(byteInput, 44, 32);
        
        boxKeyLen = ByteUtil.subArray(byteInput, 76, 4);
        boxKey = ByteUtil.subArray(byteInput, 80, 28);
    }

    public String getTimeStamp()
    {
        if (timeStamp == null)
        {
            return "";
        }
        
        return new String(timeStamp).trim();
    }
    
    public int getBoxKeyLen()
    {
        return ByteUtil.getInt(boxKeyLen);
    }
    
    public byte[] getBoxKey()
    {
        int len = ByteUtil.getInt(boxKeyLen);
        return ByteUtil.subArray(boxKey, 0, len);
    }
}
