package com.travelrely.v2.model;

import java.io.Serializable;
import java.util.List;

//The model of Sim Card
public class SimCard implements Serializable
{
    private static final long serialVersionUID = 4318838659250781722L;

    // 行程描述
    private String description;

    // 行程开始日期
    private String beginDate;

    // 行程结束日期
    private String endDate;

    // 目的地国家码
    private String mcc;

    // 目的地通信运营商
    private String mnc;

    // SIM卡类型，1-一次性实体硬卡，2-无vSIM卡、首次使用，3-已有vSIM卡
    private int simcardType;

    // SIM卡尺寸，0-标准卡 1-Micro卡 2-Nano卡
    private int simcardSize;

    // 流量包ID
    private String dataPackageId;

    // 语音包ID
    private String voicePackageId;

    // 套餐包天数
    private String packagePeriod;

    // 套餐包国际长途分钟数
    private String packageIddVoice;
    
    // 套餐包当地通话分钟数
    private String packageLocalVoice;

    // 套餐包数据流量
    private String packageData;
    
    // 套餐类型 0-预付费 1-后付费
    private int iPkgType;
    
    // 是否购买蓝牙盒子
    private int iBtBoxBuyFlag;

    // 货币计算单位
    private String currencyUnit;

    // 价格包
    private double pricePackage;

    // 商品数量
    private int numItems;

    // 使用人手机号以及订购的卡类型和大小
    private List<UsrSimInfo> operatorPhoneNumbers;
    
    //sim卡信息
    private UsrSimInfo usrSimInfo;

    public UsrSimInfo getUsrSimInfo() {
        return usrSimInfo;
    }

    public void setUsrSimInfo(UsrSimInfo usrSimInfo) {
        this.usrSimInfo = usrSimInfo;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    
    /**
     * @return produc_id
     */
    public String getMcc()
    {
        return mcc;
    }

    /**2015-8-22
     * @param mcc produc_id
     */
    public void setMcc(String mcc)
    {
        this.mcc = mcc;
    }

    /**
     * @return imei
     */
    public String getMnc()
    {
        return mnc;
    }

    /**设置IMEI号
     * @param mnc imei号
     */
    public void setMnc(String mnc)
    {
        this.mnc = mnc;
    }

    public int getSimcardType()
    {
        return simcardType;
    }

    public void setSimcardType(int simcardType)
    {
        this.simcardType = simcardType;
    }

    public int getSimcardSize()
    {
        return simcardSize;
    }

    public void setSimcardSize(int simcardSize)
    {
        this.simcardSize = simcardSize;
    }

    public String getDataPackageId()
    {
        return dataPackageId;
    }

    public void setDataPackageId(String dataPackageId)
    {
        this.dataPackageId = dataPackageId;
    }

    public String getVoicePackageId()
    {
        return voicePackageId;
    }

    public void setVoicePackageId(String voicePackageId)
    {
        this.voicePackageId = voicePackageId;
    }

    public String getPackagePeriod()
    {
        return packagePeriod;
    }

    public void setPackagePeriod(String packagePeriod)
    {
        this.packagePeriod = packagePeriod;
    }

    public String getIddVoice()
    {
        return packageIddVoice;
    }

    public void setIddVoice(String packageVoice)
    {
        this.packageIddVoice = packageVoice;
    }
    
    public String getLocalVoice()
    {
        return packageLocalVoice;
    }

    public void setLocalVoice(String packageVoice)
    {
        this.packageLocalVoice = packageVoice;
    }

    public String getPackageData()
    {
        return packageData;
    }

    public void setPackageData(String packageData)
    {
        this.packageData = packageData;
    }

    public String getCurrencyUnit()
    {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit)
    {
        this.currencyUnit = currencyUnit;
    }

    public double getPricePackage()
    {
        return pricePackage;
    }

    public void setPricePackage(double pricePackage)
    {
        this.pricePackage = pricePackage;
    }

    public int getNumItems()
    {
        return numItems;
    }

    public void setNumItems(int numItems)
    {
        this.numItems = numItems;
    }

    public List<UsrSimInfo> getOperatorPhoneNumbers()
    {
        return operatorPhoneNumbers;
    }

    public void setOperatorPhoneNumbers(List<UsrSimInfo> operatorPhoneNumbers)
    {
        this.operatorPhoneNumbers = operatorPhoneNumbers;
    }
    
    public int getPkgType()
    {
        return iPkgType;
    }

    public void setPkgType(int iPkgType)
    {
        this.iPkgType = iPkgType;
    }
    
    public int getBtBoxFlag()
    {
        return iBtBoxBuyFlag;
    }

    public void setBtBoxFlag(int iBtBoxBuyFlag)
    {
        this.iBtBoxBuyFlag = iBtBoxBuyFlag;
    }

    @Override
    public String toString()
    {
        return "SimCard [description=" + description + ", beginDate="
                + beginDate + ", endDate=" + endDate + ", mcc=" + mcc
                + ", mnc=" + mnc
                + ", simcardSize=" + simcardSize
                + ", dataPackageId="
                + dataPackageId + ", voicePackageId=" + voicePackageId
                + ", packagePeriod=" + packagePeriod + ", packageVoice="
                + packageIddVoice + ", packageData=" + packageData
                + ", currencyUnit=" + currencyUnit + ", pricePackage="
                + pricePackage + ", numItems=" + numItems
                + ", operatorPhoneNumbers=" + operatorPhoneNumbers + "]";
    }

}
