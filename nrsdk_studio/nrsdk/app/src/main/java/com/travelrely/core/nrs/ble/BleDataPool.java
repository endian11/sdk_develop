package com.travelrely.core.nrs.ble;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BleDataPool
{
    private AtomicInteger mCounter;
    private HashMap<Integer, byte[]> datas = new HashMap<Integer, byte[]>();

    public BleDataPool()
    {
        mCounter = new AtomicInteger();
    }

    public void setDataPool(byte[] msg)
    {
        datas.clear();
        mCounter.set(0);

        int len = msg.length;
        int pos = 0;
        while (pos < len)
        {
            int sendLen = (len-pos) < 20 ? (len-pos) : 20;
            byte[] tmp = new byte[sendLen];
            for (int i = 0; i < sendLen; i++)
            {
                tmp[i] = msg[pos + i];
            }
            datas.put(mCounter.getAndIncrement(), tmp);
            pos += sendLen;
        }
    }
    
    public byte[] getData()
    {
        int index = datas.size() - mCounter.getAndDecrement();
        return datas.get(index);
    }
    
    public void moveToPreData()
    {
        mCounter.getAndIncrement();
    }
    
    public boolean hasData()
    {
        if (mCounter.get() > 0)
        {
            return true;
        }
        else
        {
            datas.clear();
            mCounter.set(0);
            return false;
        }
    }
    
    public void clear()
    {
        datas.clear();
        mCounter.set(0);
    }
}
