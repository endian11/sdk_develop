package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppHwInfoRsp
{
    public String usernameString;
    public int enc_flag;
    private byte[] random;

    public AgtAppHwInfoRsp(byte[] byteInput)
    {
        int nameLen = ByteUtil.getInt(ByteUtil.subArray(byteInput, 8, 4));
        usernameString = new String(ByteUtil.subArray(byteInput, 12, nameLen));
        
        enc_flag = ByteUtil.getInt(ByteUtil.subArray(byteInput, 40, 4));
        
        int randomLen = ByteUtil.getInt(ByteUtil.subArray(byteInput, 44, 4));
        random = ByteUtil.subArray(byteInput, 48, randomLen);
    }
    
    public boolean isEncEnable()
    {
        return enc_flag == 1;
    }
    
    public byte[] getRandom()
    {
        return random;
    }
}
