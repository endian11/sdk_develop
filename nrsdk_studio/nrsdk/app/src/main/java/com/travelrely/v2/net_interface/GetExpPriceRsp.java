package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.v2.model.ExpressPrice;
import com.travelrely.v2.response.BaseData;

public class GetExpPriceRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    ResponseInfo responseInfo;
    GetExpPriceRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public void setBaseRsp(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetExpPriceRsp.Data getData()
    {
        return data;
    }

    public void setData(GetExpPriceRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetExpPriceRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        List<ExpressPrice> expressPrices;

        private int expPriceVer;

        public int getExpressPriceVersion()
        {
            return expPriceVer;
        }

        public void setExpress_price_version(int express_price_version)
        {
            this.expPriceVer = express_price_version;
        }

        public List<ExpressPrice> getExpressPrices()
        {
            return expressPrices;
        }

        public void setExpressPrices(List<ExpressPrice> expressPrices)
        {
            this.expressPrices = expressPrices;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);

            JSONArray jsonArray = jsonObject.optJSONArray("express_price_list");
            if (jsonArray == null)
            {
                return;
            }
            
            expressPrices = new ArrayList<ExpressPrice>();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject expJson = jsonArray.optJSONObject(i);
                if (expJson != null)
                {
                    ExpressPrice expPrice = new ExpressPrice();
                    expPrice.setCurrencyUnit(expJson.optInt("currency_unit"));
                    expPrice.setExpressPrice(expJson.optInt("express_price"));
                    expPrice.setStateName(expJson.optString("state_name"));
                    
                    expressPrices.add(expPrice);
                }
            }
            
            this.expPriceVer = jsonObject.optInt("express_price_version");
        }
    }
}
