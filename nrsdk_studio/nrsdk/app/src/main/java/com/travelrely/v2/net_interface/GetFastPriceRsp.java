package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class GetFastPriceRsp extends BaseResponse implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;
    GetFastPriceRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetFastPriceRsp.Data getData()
    {
        return data;
    }

    public void setData(GetFastPriceRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetFastPriceRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        List<FastDataPrice> dataPriceList;
        List<FastDataPrice> crbtPriceList;

        public List<FastDataPrice> getDataPrices()
        {
            return dataPriceList;
        }

        public void setDataPrices(List<FastDataPrice> dataPrices)
        {
            this.dataPriceList = dataPrices;
        }
        
        public List<FastDataPrice> getCrbtPriceList()
        {
            return crbtPriceList;
        }

        public void setCrbtPriceList(List<FastDataPrice> dataPrices)
        {
            this.crbtPriceList = dataPrices;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);

            JSONArray ordersArray = jsonObject.optJSONArray("pakagelist");
            if (ordersArray != null)
            {
                dataPriceList = new ArrayList<FastDataPrice>();
                for (int i = 0; i < ordersArray.length(); i++)
                {
                    JSONObject orderJsonObject = ordersArray.optJSONObject(i);
                    FastDataPrice order = new FastDataPrice();
                    order.setValue(orderJsonObject);
                    dataPriceList.add(order);
                }
            }
            
            JSONArray crbtArray = jsonObject.optJSONArray("crbtlist");
            if (crbtArray != null)
            {
                crbtPriceList = new ArrayList<FastDataPrice>();
                for (int i = 0; i < crbtArray.length(); i++)
                {
                    JSONObject orderJsonObject = crbtArray.optJSONObject(i);
                    FastDataPrice order = new FastDataPrice();
                    order.setValue(orderJsonObject);
                    crbtPriceList.add(order);
                }
            }
        }

        public static class FastDataPrice extends BaseResponse implements
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
                this.packageprice = jsonObject.optDouble("price");
            }
        }
    }
}
