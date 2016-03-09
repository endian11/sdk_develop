package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class GetMultiTripRsp extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetMultiTripRsp.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public GetMultiTripRsp.Data getData()
    {
        return data;
    }

    public void setData(GetMultiTripRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetMultiTripRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        // 用户名
        String userName;

        // 订单列表
        List<Trip> tripList;

        public List<Trip> getTrips()
        {
            return tripList;
        }

        public void setTrips(List<Trip> tripList)
        {
            this.tripList = tripList;
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

            JSONArray tripArray = jsonObject.optJSONArray("triplist");
            if (tripArray == null)
            {
                return;
            }
            
            tripList = new ArrayList<Trip>();
            for (int i = 0; i < tripArray.length(); i++)
            {
                JSONObject tripJsonObj = tripArray.optJSONObject(i);
                Trip trip = new Trip();
                trip.setValue(tripJsonObj);
                tripList.add(trip);
            }
        }

        public static class Trip extends BaseData implements Serializable
        {
            private static final long serialVersionUID = 1L;

            // 行程ID
            String tripId;

            // 行程开始时间
            String beginDate;

            // 行程结束时间
            String endDate;

            String mcc;

            String mnc;

            String roamingNum;
            
            int status;

            public String getTripId()
            {
                return tripId;
            }

            public void setTripId(String trip_id)
            {
                this.tripId = trip_id;
            }

            public String getBeginDate()
            {
                return beginDate;
            }

            public void setBeginDate(String begin_date)
            {
                this.beginDate = begin_date;
            }

            public String getEndDate()
            {
                return endDate;
            }

            public void setEndDate(String end_date)
            {
                this.endDate = end_date;
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
            
            public String getRoamingNum()
            {
                return roamingNum;
            }

            public void setRoamingNum(String roamingNum)
            {
                this.roamingNum = roamingNum;
            }
            
            public boolean isActive()
            {
                return (status == 1 ? true : false);
            }

            @Override
            public void setValue(JSONObject jsonObject)
            {
                super.setValue(jsonObject);

                tripId = jsonObject.optString("tripid");
                beginDate = jsonObject.optString("begindate");
                endDate = jsonObject.optString("enddate");
                mcc = jsonObject.optString("mcc");
                mnc = jsonObject.optString("mnc");
                status = jsonObject.optInt("status");
                roamingNum = jsonObject.optString("raomnum");
            }
        }
    }
}
