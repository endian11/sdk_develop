/**
 * 
 */
package com.travelrely.core.nr.mt100;



/**
 * @author Administrator
 *
 */
public class Common {
  
	public static class SynchToken
	{
	}
	  public static byte[] HexStringToBytes(String paramString)
	  {
	    int i = paramString.length() / 2;
	    if (paramString.length() % 2 == 1)
	      ++i;
	    byte[] arrayOfByte = new byte[i];
	    int j = 0;
	    for (int k = 0; ; k += 2)
	    {
	      if (k > -1 + paramString.length())
	        return arrayOfByte;
	      int l = k + 2;
	      if (l > paramString.length())
	        l = k + 1;
	      arrayOfByte[j] = (byte)(0xFF & Integer.parseInt(paramString.substring(k, l), 16));
	      ++j;
	    }
	  }
	  
	  public static String bytesToString(byte[] paramArrayOfByte, int paramInt)
	  {
	    StringBuffer localStringBuffer = new StringBuffer("");
	    for (int i = 0; ; ++i)
	    {
	      if (i >= paramInt)
	        return localStringBuffer.toString();
	      localStringBuffer.append((char)paramArrayOfByte[i]);
	    }
	  }
	  
	  public static String bytesToHexString(byte[] data)
	  {
		  if(data == null)
			  return null;
	    	String content = "";
	    	for(int i=0;i<data.length;i++)
	    	{
	    		String hex = Integer.toHexString(data[i]&0xFF);
	    		if(hex.length()==1)
	    			hex="0"+hex;
	    		content += hex;
	    	}
	    	return content;
	  }
}


