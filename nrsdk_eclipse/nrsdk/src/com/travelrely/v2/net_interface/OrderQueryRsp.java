package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.net_interface.GetUsrInfoRsp.CreditCard;
import com.travelrely.v2.net_interface.GetUsrInfoRsp.PersonalInfo;
import com.travelrely.v2.net_interface.GetUsrInfoRsp.ShipmentInfo;
import com.travelrely.v2.response.BaseData;

public class OrderQueryRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    OrderQueryRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public void setBaseRsp(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public OrderQueryRsp.Data getData()
    {
        return data;
    }

    public void setData(OrderQueryRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new OrderQueryRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        // 用户名
        String userName;

        // 订单列表
        List<Order> orders;

        public List<Order> getOrders()
        {
            return orders;
        }

        public void setOrders(List<Order> orders)
        {
            this.orders = orders;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            this.userName = jsonObject.optString("username");
            JSONArray ordersArray = jsonObject.optJSONArray("order_list");
            if (ordersArray == null)
            {
                return;
            }

            orders = new ArrayList<Order>();
            for (int i = 0; i < ordersArray.length(); i++)
            {
                JSONObject orderJsonObj = ordersArray.optJSONObject(i);
                if (orderJsonObj == null)
                {
                    continue;
                }
                Order order = new Order();
                orders.add(order);
                order.setValue(orderJsonObj);
            }
        }

        public static class Order extends BaseData implements Serializable
        {
            private static final long serialVersionUID = 1L;

            // 订单ID
            String order_id;

            // 快递单号
            String express_barcode;

            // 订单状态0-待付款，1-已付款，2-正在执行，3-已执行完成
            int order_status;

            // 货币单位
            int currency_unit;

            // double package_price;

            // 快递价格
            double express_price;

            // 优惠券价格
            double favorable_price;

            // 总价格
            double total_fee;

            // OrderInfo的列表
            List<OrderInfo> orderInfos;

            // 个人信息
            PersonalInfo personalInfo;

            // 信用卡信息
            CreditCard payment_info;

            // 快递信息
            ShipmentInfo shipmentInfo;

            public String getOrderId()
            {
                return order_id;
            }

            public void setOrderId(String order_id)
            {
                this.order_id = order_id;
            }

            public String getExpressBarcode()
            {
                return express_barcode;
            }

            public void setExpressBarcode(String express_barcode)
            {
                this.express_barcode = express_barcode;
            }

            public int getOrderStatus()
            {
                return order_status;
            }

            public void setOrderStatus(int order_status)
            {
                this.order_status = order_status;
            }

            public int getCurrencyUnit()
            {
                return currency_unit;
            }

            public void setCurrencyUnit(int currency_unit)
            {
                this.currency_unit = currency_unit;
            }

            public double getExpressPrice()
            {
                return express_price;
            }

            public void setExpressPrice(double express_price)
            {
                this.express_price = express_price;
            }

            public double getFavorablePrice()
            {
                return favorable_price;
            }

            public void setFavorablePrice(double favorable_price)
            {
                this.favorable_price = favorable_price;
            }

            public double getTotalFee()
            {
                return total_fee;
            }

            public void setTotalFee(double total_fee)
            {
                this.total_fee = total_fee;
            }

            public List<OrderInfo> getOrderInfos()
            {
                return orderInfos;
            }

            public void setOrderInfos(List<OrderInfo> orderInfos)
            {
                this.orderInfos = orderInfos;
            }

            public PersonalInfo getPersonalInfo()
            {
                return personalInfo;
            }

            public void setPersonalInfo(PersonalInfo personalInfo)
            {
                this.personalInfo = personalInfo;
            }

            public CreditCard getPaymentInfo()
            {
                return payment_info;
            }

            public void setPaymentInfo(CreditCard payment_info)
            {
                this.payment_info = payment_info;
            }

            public ShipmentInfo getShipmentInfo()
            {
                return shipmentInfo;
            }

            public void setShipmentInfo(ShipmentInfo shipmentInfo)
            {
                this.shipmentInfo = shipmentInfo;
            }

            @Override
            public void setValue(JSONObject jsonObject)
            {
                super.setValue(jsonObject);

                order_id = jsonObject.optString("orderid");
                express_price = jsonObject.optDouble("expressprice");
                favorable_price = jsonObject.optDouble("favorableprice");
                total_fee = jsonObject.optDouble("totalfee");
                order_status = jsonObject.optInt("order_status");

                JSONArray orderInfoJsonArray = jsonObject
                        .optJSONArray("order_list");
                if (orderInfoJsonArray == null)
                {
                    return;
                }

                orderInfos = new ArrayList<OrderQueryRsp.Data.OrderInfo>();
                for (int i = 0; i < orderInfoJsonArray.length(); i++)
                {
                    JSONObject orderInfoJsonObj = orderInfoJsonArray
                            .optJSONObject(i);
                    if (orderInfoJsonObj == null)
                    {
                        continue;
                    }
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setValue(orderInfoJsonObj);
                    orderInfos.add(orderInfo);
                }

                JSONObject personInfoJsonObject = jsonObject
                        .optJSONObject("personal_info");
                if (personInfoJsonObject != null)
                {
                    personalInfo = new PersonalInfo();
                    personalInfo.setValue(personInfoJsonObject);
                }

                JSONObject creditCard = jsonObject
                        .optJSONObject("payment_info");
                if (creditCard != null)
                {
                    payment_info = new CreditCard();
                    payment_info.setValue(creditCard);
                }

                JSONObject shipment_infoJsonObject = jsonObject
                        .optJSONObject("shipment_info");
                if (shipment_infoJsonObject != null)
                {
                    shipmentInfo = new ShipmentInfo();
                    shipmentInfo.setValue(shipment_infoJsonObject);
                }
            }
        }

        public static class OrderInfo extends BaseData implements Serializable
        {
            private static final long serialVersionUID = 1L;

            // 订单类型，用于区分是普通订单和香港友好用户订单
            int orderType;

            // 订购人的用户名
            String username;

            String orderId;

            String subOrderId;

            // 订购的目的地的移动国家码
            String mcc;

            // 订购的目的地的移动运营商码
            String mnc;

            // 使用人数目
            int userNum;

            // sim卡大小
            int cardSize;

            private double suborderfee;

            // 行程列表
            List<Trip> trips;

            public String getUsername()
            {
                return username;
            }

            public void setUsername(String username)
            {
                this.username = username;
            }

            public String getOrderId()
            {
                return orderId;
            }

            public void setOrderId(String orderId)
            {
                this.orderId = orderId;
            }

            public String getSubOrderId()
            {
                return subOrderId;
            }

            public void setSubOrderId(String subOrderId)
            {
                this.subOrderId = subOrderId;
            }

            public String getMcc()
            {
                return mcc;
            }

            public void setMcc(String mcc)
            {
                this.mcc = mcc;
            }

            public String getMnc()
            {
                return mnc;
            }

            public void setMnc(String mnc)
            {
                this.mnc = mnc;
            }

            public int getUserNum()
            {
                return userNum;
            }

            public void setUserNum(int userNum)
            {
                this.userNum = userNum;
            }

            public int getCardSize()
            {
                return cardSize;
            }

            public void setCardSize(int cardSize)
            {
                this.cardSize = cardSize;
            }

            public double getSubOrderFee()
            {
                return suborderfee;
            }

            public void setSubOrderFee(double ttlPrice)
            {
                this.suborderfee = ttlPrice;
            }

            public int getOrderType()
            {
                return orderType;
            }

            public void setOrderType(int strOrderType)
            {
                this.orderType = strOrderType;
            }

            public List<Trip> getTrips()
            {
                return trips;
            }

            public void setTrips(List<Trip> trips)
            {
                this.trips = trips;
            }

            @Override
            public void setValue(JSONObject jsonObject)
            {
                super.setValue(jsonObject);

                username = jsonObject.optString("username");
                orderId = jsonObject.optString("orderid");
                subOrderId = jsonObject.optString("suborderid");
                mcc = jsonObject.optString("mcc");
                mnc = jsonObject.optString("mnc");
                userNum = jsonObject.optInt("usernum");
                cardSize = jsonObject.optInt("cardsize");
                suborderfee = jsonObject.optDouble("suborderfee");
                orderType = jsonObject.optInt("ordertype");

                JSONArray tripsJsonArray = jsonObject.optJSONArray("suborder_list");
                if (tripsJsonArray == null)
                {
                    return;
                }

                trips = new ArrayList<OrderQueryRsp.Data.Trip>();
                for (int i = 0; i < tripsJsonArray.length(); i++)
                {
                    JSONObject tripJsonObj = tripsJsonArray.optJSONObject(i);
                    if (tripJsonObj == null)
                    {
                        continue;
                    }
                    Trip trip = new Trip();
                    trip.setValue(tripJsonObj);
                    trips.add(trip);
                }
            }
        }

        public static class Trip extends BaseData implements Serializable
        {
            private static final long serialVersionUID = 1L;

            // 使用人的手机号码
            String username;

            // 行程ID
            String trip_id;

            // 行程描述
            String description;

            // 行程开始时间
            String begin_date;

            // 行程结束时间
            String end_date;

            String mcc;

            String mnc;

            int currency_unit;

            // 手机卡类型
            int simcardType;

            // 手机卡尺寸
            int simcardSize;

            int crbtType;// 彩铃类型
            int crbtDuration;// 彩铃服务天数

            double iddVoice;
            double localVoice;
            double packageData;

            double packagePrice;

            // 剩余数据流量
            double remainData;
            double remainVoiceIdd;
            double remainVoiceLocal;
            
            String strIddVoice;
            String strLocalVoice;
            String strPackageData;

            String strPackagePrice;

            // 剩余数据流量
            String strRemainData;
            String strRemainVoiceIdd;
            String strRemainVoiceLocal;

            // 是否购买蓝牙盒子
            int bt_buyflag;

            public String getUserName()
            {
                return username;
            }

            public void setUserName(String username)
            {
                this.username = username;
            }

            public int getCrbtType()
            {
                return crbtType;
            }

            public void setCrbtType(int crbtType)
            {
                this.crbtType = crbtType;
            }

            public int getCrbtDuration()
            {
                return crbtDuration;
            }

            public void setCrbtDuration(int crbtDuration)
            {
                this.crbtDuration = crbtDuration;
            }

            public String getTripId()
            {
                return trip_id;
            }

            public void setTripId(String trip_id)
            {
                this.trip_id = trip_id;
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
                return begin_date;
            }

            public void setBeginDate(String begin_date)
            {
                this.begin_date = begin_date;
            }

            public String getEndDate()
            {
                return end_date;
            }

            public void setEndDate(String end_date)
            {
                this.end_date = end_date;
            }

            public String getMcc()
            {
                return mcc;
            }

            public void setMcc(String mcc)
            {
                this.mcc = mcc;
            }

            public String getMnc()
            {
                return mnc;
            }

            public void setMnc(String mnc)
            {
                this.mnc = mnc;
            }

            public int getCurrencyUnit()
            {
                return currency_unit;
            }

            public void setCurrencyUnit(int currency_unit)
            {
                this.currency_unit = currency_unit;
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

            public double getPkgData()
            {
                return packageData;
            }

            public void setPkgData(double packageData)
            {
                this.packageData = packageData;
            }

            public double getIddVoice()
            {
                return iddVoice;
            }

            public void setIddVoice(double idd)
            {
                this.iddVoice = idd;
            }

            public double getLocalVoice()
            {
                return localVoice;
            }

            public void setLocalVoice(double idd)
            {
                this.localVoice = idd;
            }

            public double getPackagePrice()
            {
                return packagePrice;
            }

            public void setPackagePrice(double packagePrice)
            {
                this.packagePrice = packagePrice;
            }

            public double getRemainData()
            {
                return remainData;
            }

            public void setRemainData(double remainData)
            {
                this.remainData = remainData;
            }

            public double getRemainIdd()
            {
                return remainVoiceIdd;
            }

            public void setRemainIdd(double remainIdd)
            {
                this.remainVoiceIdd = remainIdd;
            }

            public double getRemainLocal()
            {
                return remainVoiceLocal;
            }

            public void setRemainLocal(double remainLocal)
            {
                this.remainVoiceLocal = remainLocal;
            }

            public int getBtBuyFlag()
            {
                return bt_buyflag;
            }

            public void setBtBuyFlag(int bt_buyflag)
            {
                this.bt_buyflag = bt_buyflag;
            }
            
            public String getPkgPriceStr()
            {
                return strPackagePrice;
            }
            
            public String getPkgDataStr()
            {
                return strPackageData;
            }
            
            public String getLocalVoiceStr()
            {
                return strLocalVoice;
            }
            
            public String getIddVoiceStr()
            {
                return strIddVoice;
            }
            
            public String getRemainDataStr()
            {
                return strRemainData;
            }
            
            public String getRemainLocalStr()
            {
                return strRemainVoiceLocal;
            }
            
            public String getRemainIddStr()
            {
                return strRemainVoiceIdd;
            }

            @Override
            public void setValue(JSONObject jsonObject)
            {
                super.setValue(jsonObject);

                username = jsonObject.optString("username");
                trip_id = jsonObject.optString("tripid");
                
                simcardType = jsonObject.optInt("simcardtype");
                simcardSize = jsonObject.optInt("simcardsize");
                
                crbtType = jsonObject.optInt("crbttype");
                crbtDuration = jsonObject.optInt("crbtduration");
                
                description = jsonObject.optString("description");
                begin_date = jsonObject.optString("begin_date");
                end_date = jsonObject.optString("end_date");
                mcc = jsonObject.optString("mcc");
                mnc = jsonObject.optString("mnc");
                
                currency_unit = jsonObject.optInt("currency_unit");

                packagePrice = jsonObject.optDouble("packageprice");
                packageData = jsonObject.optDouble("package_data");
                localVoice = jsonObject.optDouble("local_voice");
                iddVoice = jsonObject.optDouble("idd_voice");

                remainData = jsonObject.optDouble("remaindata");
                remainVoiceIdd = jsonObject.optDouble("remainvoice_idd");
                remainVoiceLocal = jsonObject.optDouble("remainvoice_local");
                
                strPackagePrice = jsonObject.optString("packageprice");
                strPackageData = jsonObject.optString("package_data");
                strLocalVoice = jsonObject.optString("local_voice");
                strIddVoice = jsonObject.optString("idd_voice");

                strRemainData = jsonObject.optString("remaindata");
                strRemainVoiceIdd = jsonObject.optString("remainvoice_idd");
                strRemainVoiceLocal = jsonObject.optString("remainvoice_local");

                bt_buyflag = jsonObject.optInt("bt_buyflag");
            }
        }
    }
}
