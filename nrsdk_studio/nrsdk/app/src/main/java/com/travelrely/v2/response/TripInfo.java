
package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;

public class TripInfo extends BaseResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    Data data;
    

    private String version;

    private String charset;

    private int ret;

    private int errorcode;

    private String msg;
    
  

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseResponse implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public String username;

        public List<TripInfoList> tripinfolist;

//        public List<Daylist> daylists;
//
//        public List<ActivityList> activityLists;
//
//        public Alarm alarm;
//
//        public Alarm getAlarm() {
//            return alarm;
//        }
//
//        public void setAlarm(Alarm alarm) {
//            this.alarm = alarm;
//        }
//
//        public List<Daylist> getDaylists() {
//            return daylists;
//        }
//
//        public void setDaylists(List<Daylist> daylists) {
//            this.daylists = daylists;
//        }
//
//        public List<ActivityList> getActivityLists() {
//            return activityLists;
//        }
//
//        public void setActivityLists(List<ActivityList> activityLists) {
//            this.activityLists = activityLists;
//        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<TripInfoList> getlInfoLists() {
            return tripinfolist;
        }

        public void setlInfoLists(List<TripInfoList> lInfoLists) {
            this.tripinfolist = lInfoLists;
        }

        public void setValue(JSONObject jsonObject) {

            this.username = jsonObject.optString("username");
            this.tripinfolist = new ArrayList<TripInfoList>();

            JSONArray jsonArray = jsonObject.optJSONArray("tripinfolist");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject tripinfoObject = jsonArray.optJSONObject(i);

                    if (tripinfoObject != null) {
                        TripInfoList tripInfoList = new TripInfoList();
                        tripInfoList.setValue(tripinfoObject);
                        this.tripinfolist.add(tripInfoList);
                    }
                }
            }
        }
    }
}
