package sdk.travelrely.lib.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil
{
    /**
     * UCS2解码
     * 
     * @param src
     *            UCS2 源串
     * @return 解码后的UTF-16BE字符串
     * @throws Exception
     */
    public static String DecodeUCS2(String src) throws Exception
    {
        byte[] bytes = new byte[src.length() / 2];
        for (int i = 0; i < src.length(); i += 2)
        {
            bytes[i / 2] = (byte) (Integer
                    .parseInt(src.substring(i, i + 2), 16));
        }
        String reValue;
        try
        {
            reValue = new String(bytes, "UTF-16BE");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new Exception(e);
        }
        return reValue;
    }
    
    public static String DecodeUCS2(byte[] src)
    {
        String reValue = "";
        try
        {
            reValue = new String(src, "UTF-16BE");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return "";
        }
        return reValue;
    }

    /**
     * UCS2编码
     * 
     * @param src
     *            UTF-16BE编码的源串
     * @return 编码后的UCS2串
     * @throws Exception
     */
    public static String EncodeUCS2(String src) throws Exception
    {
        byte[] bytes;
        try
        {
            bytes = src.getBytes("UTF-16BE");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new Exception(e);
        }

        StringBuffer reValue = new StringBuffer();
        StringBuffer tem = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            tem.delete(0, tem.length());
            tem.append(Integer.toHexString(bytes[i] & 0xFF));
            if (tem.length() == 1)
            {
                tem.insert(0, '0');
            }

            reValue.append(tem);
        }

        return reValue.toString().toUpperCase();
    }

    public static byte[] EncodeUCS2BE(String src)
    {
        byte[] bytes;

        try
        {
            bytes = src.getBytes("UTF-16BE");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }

        return bytes;
    }

    /**
     * 检测IP
     * 
     * @param address
     * @return
     */
    public static boolean isIpAddress(String address)
    {
        String regex = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(address);
        return m.matches();
    }

    public static byte[] intToByteArray2(int i) throws Exception
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);
        out.writeInt(i);
        byte[] b = buf.toByteArray();
        out.close();
        buf.close();
        return b;
    }

    public static String getRandomString(int length)//length表示生成字符串的长度
    {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   
        Random random = new Random();   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++)
        {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }
        return sb.toString();   
    }
    
    public static byte[] getRandomNumSeries(int length)//length表示生成字符串的长度
    {
        Random random = new Random();
        byte[] rslt = new byte[length];
        
        for (int i = 0; i < length; i++)
        {   
            rslt[i] = (byte) random.nextInt(10);
        }
        return rslt;   
    }
}
