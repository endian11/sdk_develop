package com.travelrely.model;

import java.io.Serializable;

import net.sourceforge.pinyin4j.PinyinHelper;

public class ServiceNum implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // 公共服务号需要使用
    private String strMcc; // 区分是哪个国家的公共服务号
    private String strCountryName;
    private String strNumName;// 服务号名称
    private String strNum; // 服务号
    private String strFlag;  // 0-旅信服务号码 1—公共服务号码
    
    private String pys;
    
    public void setMcc(String strMcc)
    {
        this.strMcc = strMcc;
    }
    
    public String getMcc()
    {
        return strMcc;
    }
    
    public void setCountryName(String strCountryName)
    {
        this.strCountryName = strCountryName;
    }
    
    public String getCountryName()
    {
        return strCountryName;
    }
    
    public void setNumName(String strNumName)
    {
        this.strNumName = strNumName;
    }
    
    public String getNumName()
    {
        return strNumName;
    }
    
    public void setNum(String strNum)
    {
        this.strNum = strNum;
    }
    
    public String getNum()
    {
        return strNum;
    }
    
    public void setFlag(String strFlag)
    {
        this.strFlag = strFlag;
    }
    
    public String getFlag()
    {
        return strFlag;
    }
    
    public String getPys() {
        return pys;
    }

    public void setPys(String pys) {
        this.pys = pys;
    }

    public String getSortKey()
    {
        char firstChar = ' ';
        if (strCountryName != null && !strCountryName.equals(""))
        {
            firstChar = strCountryName.toUpperCase().charAt(0);
        }

        String[] strrs = PinyinHelper.toHanyuPinyinStringArray(firstChar);
        char strr = ' ';
        if (strrs == null || strrs.length == 0)
        {
            strr = firstChar;
        }
        else
        {
            strr = strrs[0].toUpperCase().charAt(0);
        }

        // 如果不是A-Z就是#
        if (strr < 'A' || strr > 'Z')
        {
            strr = '#';
        }

        pys = strr + "";

        return pys.substring(0, 1);
    }
}
