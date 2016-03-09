package com.travelrely.model;

import java.io.Serializable;

/** 
 * 
 * @author zhangyao
 * @version 2014年7月28日上午10:38:50
 */

public class CountrySelectModel implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String countryName;
    
    String countryNum;
    
    int countryIconId;
    
    String sortLetters;  //显示数据拼音的首字母

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryNum() {
        return countryNum;
    }

    public void setCountryNum(String countryNum) {
        this.countryNum = countryNum;
    }

    public int getCountryIconId() {
        return countryIconId;
    }

    public void setCountryIconId(int countryIconId) {
        this.countryIconId = countryIconId;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
    
    

}
