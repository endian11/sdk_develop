package com.travelrely.core.nrs.nr.msg;

import com.travelrely.core.nrs.nr.util.ByteUtil;

public class AgtAppKeepAliveRsp
{
    public OctArray28_s usernameArray28_s;

    public AgtAppKeepAliveRsp(byte[] byteInput)
    {
        usernameArray28_s = new OctArray28_s(
                ByteUtil.subArray(byteInput, 0, 32));
    }
}
