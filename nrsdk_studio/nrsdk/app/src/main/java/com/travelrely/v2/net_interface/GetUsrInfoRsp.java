package com.travelrely.v2.net_interface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class GetUsrInfoRsp extends BaseResponse implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetUsrInfoRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetUsrInfoRsp.Data getData()
    {
        return data;
    }

    public void setData(GetUsrInfoRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetUsrInfoRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    /**
     * 序列化对象
     * 
     * @param person
     * @return
     * @throws IOException
     */
    public final static String serialize(GetUsrInfoRsp getUserInfo)
            throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(getUserInfo);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    /**
     * 反序列化对象
     * 
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public final static GetUsrInfoRsp deSerialization(String str)
            throws IOException, ClassNotFoundException
    {
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        GetUsrInfoRsp getUserInfo = (GetUsrInfoRsp) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return getUserInfo;
    }

    public static class Data extends BaseData implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        String userName;
        
        String tokenNum;
        
        String remain_voice;

        CreditCard creditCard;

        PersonalInfo personalInfo;

        List<ShipmentInfo> shipmentInfos;

        public CreditCard getCredit_card()
        {
            return creditCard;
        }

        public void setCredit_card(CreditCard credit_card)
        {
            this.creditCard = credit_card;
        }

        public PersonalInfo getPersonal_info()
        {
            return personalInfo;
        }

        public void setPersonal_info(PersonalInfo personal_info)
        {
            this.personalInfo = personal_info;
        }

        public List<ShipmentInfo> getShipment_info()
        {
            return shipmentInfos;
        }

        public void setShipment_info(List<ShipmentInfo> shipment_infos)
        {
            this.shipmentInfos = shipment_infos;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }
        
        public String getTokenNum()
        {
            return tokenNum;
        }

        public void setTokenNum(String tokenNum)
        {
            this.tokenNum = tokenNum;
        }
        
        public String getRemainVoice()
        {
            return remain_voice;
        }

        public void setRemainVoice(String remain_voice)
        {
            this.remain_voice = remain_voice;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
            this.tokenNum = jsonObject.optString("token_num");
            this.remain_voice = jsonObject.optString("remain_voice");

            this.creditCard = new CreditCard();
            this.personalInfo = new PersonalInfo();
            this.shipmentInfos = new ArrayList<ShipmentInfo>();

            JSONObject usrInfo = jsonObject.optJSONObject("user_info");
            if (usrInfo == null)
            {
                return;
            }
        
            JSONObject personJsonObj = usrInfo.optJSONObject("personal_info");
            JSONObject creditJsonObj = usrInfo.optJSONObject("credit_card");
            JSONArray shipmentInfoJsonArray = usrInfo
                    .optJSONArray("shipment_info");
            if (creditJsonObj != null)
            {
                creditCard.setValue(creditJsonObj);
            }

            if (personJsonObj != null)
            {
                personalInfo.setValue(personJsonObj);
            }

            if (shipmentInfoJsonArray != null)
            {
                for (int i = 0; i < shipmentInfoJsonArray.length(); i++)
                {
                    JSONObject shipJsonObject = shipmentInfoJsonArray
                            .optJSONObject(i);

                    if (shipJsonObject != null)
                    {
                        ShipmentInfo shipmentInfo = new ShipmentInfo();
                        shipmentInfo.setValue(shipJsonObject);
                        shipmentInfos.add(shipmentInfo);
                    }
                }
            }
        }
    }

    public static class PersonalInfo extends BaseResponse implements
            Serializable
    {
        String passport_number;

        String id_card_number;

        String mobile_phone;

        String email;

        String nick_name;

        

        String head_portrait;

        public String getHeadPortrait()
        {
            return head_portrait;
        }

        public void setHeadPortrait(String head_portrait)
        {
            this.head_portrait = head_portrait;
        }

        public String getNickname()
        {
            return nick_name;
        }

        public void setNickname(String nick_name)
        {
            this.nick_name = nick_name;
        }

        public String getPassportNum()
        {
            return passport_number;
        }

        public void setPassportNum(String passport_number)
        {
            this.passport_number = passport_number;
        }

        public String getId_card_number()
        {
            return id_card_number;
        }

        public void setId_card_number(String id_card_number)
        {
            this.id_card_number = id_card_number;
        }

        public String getMobilePhone()
        {
            return mobile_phone;
        }

        public void setMobilePhone(String mobile_phone)
        {
            this.mobile_phone = mobile_phone;
        }

        public String getEmail()
        {
            return email;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);

            this.nick_name = jsonObject.optString("nick_name");
            this.passport_number = jsonObject.optString("passport_number");
            this.id_card_number = jsonObject.optString("id_card_number");
            this.mobile_phone = jsonObject.optString("mobile_phone");
            this.email = jsonObject.optString("email");
            this.head_portrait = jsonObject.optString("head_portrait");
        }
    }

    public static class CreditCard extends BaseResponse implements Serializable
    {
        int payment_mode;

        String credit_card_type;

        String card_number;

        String expiration_date;

        String security_code;

        public int getPayment_mode()
        {
            return payment_mode;
        }

        public void setPayment_mode(int payment_mode)
        {
            this.payment_mode = payment_mode;
        }

        public String getCredit_card_type()
        {
            return credit_card_type;
        }

        public void setCredit_card_type(String credit_card_type)
        {
            this.credit_card_type = credit_card_type;
        }

        public String getCard_number()
        {
            return card_number;
        }

        public void setCard_number(String card_number)
        {
            this.card_number = card_number;
        }

        public String getExpiration_date()
        {
            return expiration_date;
        }

        public void setExpiration_date(String expiration_date)
        {
            this.expiration_date = expiration_date;
        }

        public String getSecurity_code()
        {
            return security_code;
        }

        public void setSecurity_code(String security_code)
        {
            this.security_code = security_code;
        }

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);

            this.credit_card_type = jsonObject.optString("credit_card_type");
            this.card_number = jsonObject.optString("card_number");
            this.expiration_date = jsonObject.optString("expiration_date");
            this.security_code = jsonObject.optString("security_code");
            this.payment_mode = jsonObject.optInt("payment_mode");
        }
    }

    public static class ShipmentInfo extends BaseResponse implements
            Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        String id;

        String consignee;// 收件人姓名

        String country;// 国家

        String state;// 省份(州)

        String city;// 城市

        String county;// 区(县)

        String address;// 详细地址

        String postal_code;// 邮政编码

        String phone_number;// 收件人电话

        String name;

        int delivery_mode;
        
        public boolean isSelected;// 表示该快递地址是否被选中

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getDeliveryMode()
        {
            return delivery_mode;
        }

        public void setDeliveryMode(int delivery_mode)
        {
            this.delivery_mode = delivery_mode;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getConsignee()
        {
            return consignee;
        }

        public void setConsignee(String consignee)
        {
            this.consignee = consignee;
        }

        public String getCountry()
        {
            return country;
        }

        public void setCountry(String country)
        {
            this.country = country;
        }

        public String getState()
        {
            return state;
        }

        public void setState(String state)
        {
            this.state = state;
        }

        public String getCity()
        {
            return city;
        }

        public void setCity(String city)
        {
            this.city = city;
        }

        public String getCounty()
        {
            return county;
        }

        public void setCounty(String county)
        {
            this.county = county;
        }

        public String getAddress()
        {
            return address;
        }

        public void setAddress(String address)
        {
            this.address = address;
        }

        public String getPostalCode()
        {
            return postal_code;
        }

        public void setPostalCode(String postal_code)
        {
            this.postal_code = postal_code;
        }

        public String getPhoneNum()
        {
            return phone_number;
        }

        public void setPhoneNum(String phone_number)
        {
            this.phone_number = phone_number;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);

            this.id = jsonObject.optString("id");
            this.consignee = jsonObject.optString("consignee");
            this.country = jsonObject.optString("country");
            this.state = jsonObject.optString("state");
            this.city = jsonObject.optString("city");
            this.county = jsonObject.optString("county");
            this.address = jsonObject.optString("address");
            this.postal_code = jsonObject.optString("postal_code");
            this.phone_number = jsonObject.optString("phone_number");
            this.delivery_mode = jsonObject.optInt("delivery_mode");
            this.name = jsonObject.optString("name");
        }
    }
}
