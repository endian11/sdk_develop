package com.travelrely.core.nrs.nr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.nrs.nr.util.TextUtil;
import com.travelrely.core.util.LogUtil;
import com.travelrely.app.db.SmsSegDbHelper;
import com.travelrely.v2.model.SmsSeg;

public class SmsReceiver
{
    private static final String TAG = SmsReceiver.class.getSimpleName();
    
    private SmsCallback mCallback;
    
    private HashMap<Integer, Integer> xx_num = new HashMap<Integer, Integer>();
    private HashMap<Integer, List<SmsSeg>> xx_sms = new HashMap<Integer, List<SmsSeg>>();
    
    public String cacheToMem(byte[] sms, int len)
    {
        LogUtil.e(TAG, ByteUtil.toHexString(sms));

        if (sms[0] == 0x05 && sms[1] == 0x00 && sms[2] == 0x03)
        {
            int xx = sms[3];
            int num = sms[4];
            int sn = sms[5];
            byte[] paylaod = ByteUtil.subArray(sms, 6, len - 6);
            
            SmsSeg mSMS = new SmsSeg();
            mSMS.setSn(sn);
            mSMS.setContent(new String(paylaod));
            
            List<SmsSeg> smsList = xx_sms.get(xx);
            if (smsList == null)
            {
                smsList = new ArrayList<SmsSeg>();
                xx_sms.put(xx, smsList);
                xx_num.put(xx, num);
            }
            
            smsList.add(mSMS);
            
            if (xx_num.get(xx) != num)
            {
                LogUtil.e(TAG, "SMS segment num error");
            }

            // 分段收集完整
            if (smsList.size() == xx_num.get(xx))
            {
                Collections.sort(smsList, new Comparator<SmsSeg>()
                {  
                    public int compare(SmsSeg a, SmsSeg b)
                    {  
                        int one = a.getSn();
                        int two = b.getSn();
                        return one- two ;   
                    }  
                });
                
                byte[] tmp = new byte[1024];
                int offset = 0;
                for (SmsSeg s : smsList)
                {
                    System.arraycopy(s.getContent(), 0, tmp, offset, s.getLen());
                    offset = offset + s.getLen();
                }
                
                String body = "";
                
                try
                {
                    body = TextUtil.DecodeUCS2(ByteUtil.subArray(tmp, 0, offset));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
                return body;
            }
            return null;
        }
        
        if (sms[0] == 0x06 && sms[1] == 0x08 && sms[2] == 0x04)
        {
            int xx = ByteUtil.getShort(ByteUtil.subArray(sms, 3, 2));
            int num = sms[5];
            int sn = sms[6];
            byte[] paylaod = ByteUtil.subArray(sms, 7, len - 7);
            
            SmsSeg mSMS = new SmsSeg();
            mSMS.setSn(sn);
            mSMS.setContent(new String(paylaod));
            
            List<SmsSeg> smsList = xx_sms.get(xx);
            if (smsList == null)
            {
                smsList = new ArrayList<SmsSeg>();
                xx_sms.put(xx, smsList);
                xx_num.put(xx, num);
            }
            
            smsList.add(mSMS);
            
            if (xx_num.get(xx) != num)
            {
                LogUtil.e(TAG, "SMS segment num error");
            }

            // 分段收集完整
            if (smsList.size() == xx_num.get(xx))
            {
                Collections.sort(smsList, new Comparator<SmsSeg>()
                {  
                    public int compare(SmsSeg a, SmsSeg b)
                    {  
                        int one = a.getSn();
                        int two = b.getSn();
                        return one- two ;   
                    }  
                });
                
                byte[] tmp = new byte[1024];
                int offset = 0;
                for (SmsSeg s : smsList)
                {
                    System.arraycopy(s.getContent(), 0, tmp, offset, s.getLen());
                    offset = offset + s.getLen();
                }
                
                String body = "";
                
                try
                {
                    body = TextUtil.DecodeUCS2(ByteUtil.subArray(tmp, 0, offset));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
                return body;
            }
            return null;
        }
        
        // 未分段的短信
        String body = "";
        
        try
        {
            body = TextUtil.DecodeUCS2(ByteUtil.subArray(sms, 0, len));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return body;
    }
    
    public void cacheToDB(String peer, byte[] sms, int len)
    {
        LogUtil.e(TAG, ByteUtil.toHexString(sms));

        if (sms[0] == 0x05 && sms[1] == 0x00 && sms[2] == 0x03)
        {
            int xx = sms[3];
            int num = sms[4];
            int sn = sms[5];
            byte[] paylaod = ByteUtil.subArray(sms, 6, len - 6);
            
            SmsSeg mSMS = new SmsSeg();
            mSMS.setPeer(peer);
            mSMS.setXx(xx);
            mSMS.setNum(num);
            mSMS.setSn(sn);
            mSMS.setLen(len - 6);
            mSMS.setContent(TextUtil.DecodeUCS2(paylaod));
            
            SmsSegDbHelper.getInstance().insert(mSMS);
            
            List<SmsSeg> smsList = SmsSegDbHelper.getInstance()
                    .query(peer, String.valueOf(xx));
            if (smsList.size() == num)
            {
                String msg = "";
                for (SmsSeg s : smsList)
                {
                    msg = msg + s.getContent();
                }

                if (mCallback != null)
                {
                    mCallback.onSmsReceived(peer, msg);
                }
                SmsSegDbHelper.getInstance().delet(peer, String.valueOf(xx));
            }
            return;
        }
        
        if (sms[0] == 0x06 && sms[1] == 0x08 && sms[2] == 0x04)
        {
            int xx = ByteUtil.getShort(ByteUtil.subArray(sms, 3, 2));
            int num = sms[5];
            int sn = sms[6];
            byte[] paylaod = ByteUtil.subArray(sms, 7, len - 7);
            
            SmsSeg mSMS = new SmsSeg();
            mSMS.setPeer(peer);
            mSMS.setXx(xx);
            mSMS.setNum(num);
            mSMS.setSn(sn);
            mSMS.setLen(len - 7);
            mSMS.setContent(TextUtil.DecodeUCS2(paylaod));
            
            SmsSegDbHelper.getInstance().insert(mSMS);
            
            List<SmsSeg> smsList = SmsSegDbHelper.getInstance()
                    .query(peer, String.valueOf(xx));
            if (smsList.size() == num)
            {
                String msg = "";
                for (SmsSeg s : smsList)
                {
                    msg = msg + s.getContent();
                }

                if (mCallback != null)
                {
                    mCallback.onSmsReceived(peer, msg);
                }
                SmsSegDbHelper.getInstance().delet(peer, String.valueOf(xx));
            }
            
            return;
        }
        
        // 未分段的短信
        String body = TextUtil.DecodeUCS2(ByteUtil.subArray(sms, 0, len));
        if (mCallback != null)
        {
            mCallback.onSmsReceived(peer, body);
        }
    }
    
    public void setSmsCallback(SmsCallback callback)
    {
        this.mCallback = callback;
    }
    
    public interface SmsCallback
    {
        public void onSmsReceived(String peer, String sms);
    }
}
