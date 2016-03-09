package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;

public class GetReorderPrice extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;
    GetReorderPrice.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetReorderPrice.Data getData()
    {
        return data;
    }

    public void setData(GetReorderPrice.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);

        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);

        data = new GetReorderPrice.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        String userName;

        List<DataPrice> dataPrices;
        List<VoicePrice> voicPrices;

        public List<VoicePrice> getVoicPrices()
        {
            return voicPrices;
        }

        public void setVoicPrices(List<VoicePrice> voicPrices)
        {
            this.voicPrices = voicPrices;
        }

        public List<DataPrice> getDataPrices()
        {
            return dataPrices;
        }

        public void setDataPrices(List<DataPrice> dataPrices)
        {
            this.dataPrices = dataPrices;
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
            
            JSONArray ordersArray = jsonObject.optJSONArray("data_price");
            if (ordersArray != null)
            {
                dataPrices = new ArrayList<DataPrice>();
                for (int i = 0; i < ordersArray.length(); i++)
                {
                    JSONObject orderJsonObject = ordersArray.optJSONObject(i);
                    DataPrice order = new DataPrice();
                    order.setValue(orderJsonObject);
                    dataPrices.add(order);
                }
            }

            JSONArray voiceArray = jsonObject.optJSONArray("voice_price");
            if (voiceArray != null)
            {
                voicPrices = new ArrayList<VoicePrice>();
                for (int i = 0; i < voiceArray.length(); i++)
                {
                    JSONObject orderJsonObject = voiceArray.optJSONObject(i);
                    VoicePrice order = new VoicePrice();
                    order.setValue(orderJsonObject);
                    voicPrices.add(order);
                }
            }
        }

        public static class DataPrice extends BaseResponse implements
                Serializable
        {
            /**
             * 数据价格包
             */
            private static final long serialVersionUID = 1L;

            int packageid;
            double packageprice;
            int packagecurrency;
            int packagedata;

            public int getPackageid()
            {
                return packageid;
            }

            public void setPackageid(int packageid)
            {
                this.packageid = packageid;
            }

            public double getPackageprice()
            {
                return packageprice;
            }

            public void setPackageprice(double packageprice)
            {
                this.packageprice = packageprice;
            }

            public int getPackagecurrency()
            {
                return packagecurrency;
            }

            public void setPackagecurrency(int packagecurrency)
            {
                this.packagecurrency = packagecurrency;
            }

            public int getPackagedata()
            {
                return packagedata;
            }

            public void setPackagedata(int packagedata)
            {
                this.packagedata = packagedata;
            }

            @Override
            public void setValue(JSONObject jsonObject)
            {
                super.setValue(jsonObject);
                this.packageid = jsonObject.optInt("packageid");
                this.packageprice = jsonObject.optDouble("packageprice");
                this.packagecurrency = jsonObject.optInt("packagecurrency");
                this.packagedata = jsonObject.optInt("packagedata");
            }
        }

        public static class VoicePrice extends BaseResponse implements
                Serializable
        {
            /**
             * 语音价格包
             */
            private static final long serialVersionUID = 1L;

            int packageid;
            double packageprice;
            int packagecurrency;
            int packagevoice;

            public int getPackageid()
            {
                return packageid;
            }

            public void setPackageid(int packageid)
            {
                this.packageid = packageid;
            }

            public double getPackageprice()
            {
                return packageprice;
            }

            public void setPackageprice(double packageprice)
            {
                this.packageprice = packageprice;
            }

            public int getPackagecurrency()
            {
                return packagecurrency;
            }

            public void setPackagecurrency(int packagecurrency)
            {
                this.packagecurrency = packagecurrency;
            }

            public int getPackagevoice()
            {
                return packagevoice;
            }

            public void setPackagevoice(int packagedata)
            {
                this.packagevoice = packagedata;
            }

            @Override
            public void setValue(JSONObject jsonObject)
            {
                super.setValue(jsonObject);
                this.packageid = jsonObject.optInt("packageid");
                this.packageprice = jsonObject.optDouble("packageprice");
                this.packagecurrency = jsonObject.optInt("packagecurrency");
                this.packagevoice = jsonObject.optInt("packagevoice");
            }
        }
    }
}
