package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.model.UserRoamProfile;
import com.travelrely.v2.response.BaseData;

/**
 * @author zhangyao
 * @version 2014年12月4日下午4:26:35
 */

public class GetUsrRoamProfileRsp extends BaseData implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    GetUsrRoamProfileRsp.Data data;

    public ResponseInfo getBaseRsp()
    {
        return responseInfo;
    }

    public GetUsrRoamProfileRsp.Data getData()
    {
        return data;
    }

    public void setData(GetUsrRoamProfileRsp.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);

        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new GetUsrRoamProfileRsp.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        if (!dataobj.isNull("triplist"))
        {
            data.setValue(dataobj);
        }
    }

    public static class Data extends BaseData implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        ArrayList<UserRoamProfile> triplist;

        int user_roam_version;

        public int getUser_roam_version()
        {
            return user_roam_version;
        }

        public void setUser_roam_version(int user_roam_version)
        {
            this.user_roam_version = user_roam_version;
        }

        public ArrayList<UserRoamProfile> getTriplist()
        {
            return triplist;
        }

        public void setTriplist(ArrayList<UserRoamProfile> triplist)
        {
            this.triplist = triplist;
        }

        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);

            user_roam_version = jsonObject.optInt("user_roam_version");
            triplist = new ArrayList<UserRoamProfile>();
            try
            {
                JSONArray tripArray = jsonObject.getJSONArray("triplist");
                if (tripArray != null)
                {
                    for (int i = 0; i < tripArray.length(); i++)
                    {
                        jsonObject = tripArray.optJSONObject(i);
                        if (jsonObject != null)
                        {
                            UserRoamProfile tripList = new UserRoamProfile();
                            tripList.setUser_roam_version(user_roam_version);
                            tripList.setJsonValue(jsonObject);
                            this.triplist.add(tripList);
                        }
                    }

                }
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
