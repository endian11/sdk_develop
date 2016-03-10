package com.travelrely.core.nrs.nr.mt100;

public interface Mt100Callback
{
    public void callRslt(byte[] rslt);
    public void sendBtMsg(byte[] msg);
}
