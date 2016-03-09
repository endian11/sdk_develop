package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;

public class FetchToken extends BaseResponse implements Serializable
{
    private static final long serialVersionUID = 1L;

    ResponseInfo responseInfo;

    FetchToken.Data data;

    public ResponseInfo getResponseInfo()
    {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo)
    {
        this.responseInfo = responseInfo;
    }

    public FetchToken.Data getData()
    {
        return data;
    }

    public void setData(FetchToken.Data data)
    {
        this.data = data;
    }

    @Override
    public void setValue(JSONObject jsonObject)
    {
        super.setValue(jsonObject);
        responseInfo = new ResponseInfo();
        responseInfo.setValue(jsonObject);
        data = new FetchToken.Data();
        JSONObject dataobj = jsonObject.optJSONObject("data");
        data.setValue(dataobj);
    }

    public static class Data extends BaseData implements Serializable
    {
        private static final long serialVersionUID = 1L;

        String userName;

        List<FetchTokenContact> phone_list;

        public List<FetchTokenContact> getPhone_list()
        {
            return phone_list;
        }

        public void setPhone_list(List<FetchTokenContact> phone_list)
        {
            this.phone_list = phone_list;
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
            JSONArray contactArray = jsonObject.optJSONArray("contact_list");
            if (contactArray == null)
            {
                return;
            }
            
            phone_list = new ArrayList<FetchToken.Data.FetchTokenContact>();
            for (int i = 0; i < contactArray.length(); i++)
            {
                JSONObject jsonObj = (JSONObject) contactArray.opt(i);
                FetchTokenContact fetchTokenContact = new FetchTokenContact();
                fetchTokenContact.setValue(jsonObj);
                phone_list.add(fetchTokenContact);
            }
        }

        public static class FetchTokenContact extends BaseResponse
        {
            private String phone_number;
            private String new_num;

            private int register;

            private String token;

            /**
             * "2012-12-08
             */
            private String valid_time;

            private int presence;

            private String time_difference;

            private String nick_name;
            private String headportrait;

            public String getNick_name()
            {
                return nick_name;
            }

            public void setNick_name(String nick_name)
            {
                this.nick_name = nick_name;
            }

            public String getHeadportrait()
            {
                return headportrait;
            }

            public void setHeadportrait(String headportrait)
            {
                this.headportrait = headportrait;
            }

            public String getPhone_number()
            {
                return phone_number;
            }

            public void setPhone_number(String phone_number)
            {
                this.phone_number = phone_number;
            }
            
            public String getNewNum()
            {
                return new_num;
            }

            public void setNewNum(String phone_number)
            {
                this.new_num = phone_number;
            }

            public int getRegister()
            {
                return register;
            }

            public void setRegister(int register)
            {
                this.register = register;
            }

            public String getToken()
            {
                return token;
            }

            public void setToken(String token)
            {
                this.token = token;
            }

            public String getValid_time()
            {
                return valid_time;
            }

            public void setValid_time(String valid_time)
            {
                this.valid_time = valid_time;
            }

            public int getPresence()
            {
                return presence;
            }

            public void setPresence(int presence)
            {
                this.presence = presence;
            }

            public String getTime_difference()
            {
                return time_difference;
            }

            public void setTime_difference(String time_difference)
            {
                this.time_difference = time_difference;
            }

            @Override
            public void setValue(JSONObject jsonObject)
            {
                super.setValue(jsonObject);

                this.phone_number = jsonObject.optString("phone_number");
                this.token = jsonObject.optString("token");
                this.valid_time = jsonObject.optString("valid_time");
                this.time_difference = jsonObject.optString("time_difference");
                this.register = jsonObject.optInt("register");
                this.presence = jsonObject.optInt("presence");
                this.headportrait = jsonObject.optString("headportrait");
                this.nick_name = jsonObject.optString("nick_name");
                this.new_num = jsonObject.optString("new_number");
            }
        }
    }
}
