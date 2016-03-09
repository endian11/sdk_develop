package com.travelrely.core.nr.msg;

import com.travelrely.core.nr.util.ByteUtil;

public class AgtAppAuthReq
{
    public OctArray28_s usernameArray;
    
    public byte[] randArray;

    public int autnPresent;
    public byte[] autnArray;

    public AgtAppAuthReq(byte[] byteInput)
    {
        usernameArray = new OctArray28_s(ByteUtil.subArray(byteInput, 8, 32));
        
        randArray = ByteUtil.subArray(byteInput, 44, 16);
        
        autnPresent = ByteUtil.getInt(ByteUtil.subArray(byteInput, 72, 4));
        autnArray = ByteUtil.subArray(byteInput, 76, 16);
    }

    public boolean is2G()
    {
        return autnPresent == 0;
    }
}
