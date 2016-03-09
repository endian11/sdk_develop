
package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;

public class GetCarriers extends BaseResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    GetCarriersData getCarriersData;

    ResponseInfo responseInfo;

    public GetCarriersData getGetCarriersData() {
        return getCarriersData;
    }

    public void setGetCarriersData(GetCarriersData getCarriersData) {
        this.getCarriersData = getCarriersData;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    @Override
    public void setValue(JSONObject jsonObject) {

        super.setValue(jsonObject);

        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        getCarriersData = new GetCarriersData();
        JSONObject data = jsonObject.optJSONObject("data");
        getCarriersData.setValue(data);
    }

    @Override
    public String toString() {
        return "GetGarriers [getCarriersData=" + getCarriersData + ", responseInfo=" + responseInfo
                + ", toString()=" + super.toString() + "]";
    }

}
